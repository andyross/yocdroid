#include <sys/syscall.h>

/*
 * Tiny chroot utility suitable buidling without a C library.
 *
 * This is needed because (1) AOSP has no chroot builtin, so we need
 * to use our own, (2) we cannot use a glibc program outside the
 * chroot because of the missing /lib/ld-linux.so.2 ELF interpreter,
 * and (3) we don't want to statically link against glibc because it
 * produces enormous binaries.
 *
 * So we just bite the bullet and write some assembly stubs for the
 * minimal set of syscalls required.  Produces a ~2kb binary that
 * seems to work on armv5te (including thumb), i386, x86_64.
 */

#ifdef __x86_64

long syscall1(long num, long arg1)
{
    long result;
    asm("syscall" : "=a" (result) : "a"(num), "D"(arg1) : "rcx", "r11");
    return result;
}

long syscall3(long num, long arg1, long arg2, long arg3)
{
    long result;
    asm("syscall" : "=a" (result)
        : "a"(num), "D"(arg1), "S"(arg2), "d"(arg3)
        : "rcx", "r11");
    return result;
}

#elif defined(__i386)

long syscall1(long num, long arg1)
{
    long result;
    asm("int $0x80" : "=a"(result) : "a"(num), "b"(arg1));
    return result;
}

long syscall3(long num, long arg1, long arg2, long arg3)
{
    long result;
    asm("int $0x80" : "=a"(result)
        : "a"(num), "b"(arg1), "c"(arg2), "d"(arg3));
    return result;
}

#elif defined(__arm__)

// Note these work identically with -mthumb; no ARM instructions are used.

long syscall1(long num, long arg1)
{
    long result;
    asm("mov r7, %1\n\t"
        "mov r0, %2\n\t"
        "swi $0\n\t"
        "mov r0, %0"
        : "=r"(result)
        : "r"(num), "r"(arg1)
        : "r0", "r7");
    return result;
}

long syscall3(long num, long arg1, long arg2, long arg3)
{
    long result;
    asm("mov r7, %1\n\t"
        "mov r0, %2\n\t"
        "mov r1, %3\n\t"
        "mov r2, %4\n\t"
        "swi $0\n\t"
        "mov r0, %0"
        : "=r"(result)
        : "r"(num), "r"(arg1), "r"(arg2), "r"(arg3)
        : "r0", "r1", "r2", "r7");
    return result;
}

#else
#error Unrecognized CPU architecture
#endif

void exit(int status) { syscall1(SYS_exit, status); }
long write(int fd, void *p, long n) { return syscall3(SYS_write, fd, (long)p, n); }
int chdir(char *dir) { return syscall1(SYS_chdir, (long)dir); }
int chroot(char *dir) { return syscall1(SYS_chroot, (long)dir); }
int execve(char *f, char **argv, char **envp)
{ return syscall3(SYS_execve, (long)f, (long)argv, (long)envp); }

long strlen(char *p) { int n=0; while(*p++) n++; return n; }
int puts(char *s) { return write(1, s, strlen(s)); }

void _start()
{
    char *dir;

    // Parse out the main() arguments
    long *stack = __builtin_frame_address(0);
    long argc = stack[1];
    char **argv = (char**)&stack[2];
    char **environ = (char**)&stack[argc+3];

    if(argc < 3) {
        puts("Usage: chroot-static <path> <command> [args...]\n");
        exit(1);
    }

    dir = argv[1];
    if(chdir(dir)) {
        puts("cannot chdir to new root");
        exit(1);
    }

    if(chroot(dir)) {
        puts("cannot chroot");
        exit(1);
    }

    if(execve(argv[2], &argv[2], environ)) {
        puts("cannot exec");
        exit(1);
    }

    exit(0);
}
