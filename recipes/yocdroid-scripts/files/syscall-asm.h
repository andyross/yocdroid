#ifndef _SYSCALL_ASM_H
#define _SYSCALL_ASM_H

/*
 * Tiny syscall wrappers suitable for buidling without a C library.
 *
 * There is no AOSP chroot tool, so we have to provide our own.
 * Likewise some Yocdroid work must be done outside the chroot
 * (yocdroid-mountd needs to poll() for filesystem changes).  We don't
 * have an Android toolchain handy here and don't want to require one
 * for sanity reasons, so we can't simply write a bionic program.  And
 * we don't want to link static glibc binaries because they are huge.
 *
 * So we just bite the bullet and write some assembly stubs for the
 * minimal set of syscalls required.  It produces tiny (2-3kb)
 * binaries that seems to work on armv5te and later (including
 * thumb/thumb2), i386, and x86_64.
 */

#include <poll.h>
#include <fcntl.h>
#include <sys/syscall.h>

#define ASMV asm volatile

#ifdef __x86_64

static long syscall1(long num, long arg1)
{
    long result;
    ASMV("syscall" : "=a" (result) : "a"(num), "D"(arg1) : "rcx", "r11");
    return result;
}

static long syscall3(long num, long arg1, long arg2, long arg3)
{
    long result;
    ASMV("syscall" : "=a" (result)
         : "a"(num), "D"(arg1), "S"(arg2), "d"(arg3)
         : "rcx", "r11");
    return result;
}

static long syscall4(long num, long arg1, long arg2, long arg3, long arg4)
{
    long result;
    ASMV("syscall" : "=a" (result)
         : "a"(num), "D"(arg1), "S"(arg2), "d"(arg3), "g"(arg4)
         : "rcx", "r11");
    return result;
}

#elif defined(__i386)

static long syscall1(long num, long arg1)
{
    long result;
    ASMV("int $0x80" : "=a"(result) : "a"(num), "b"(arg1));
    return result;
}

static long syscall3(long num, long arg1, long arg2, long arg3)
{
    long result;
    ASMV("int $0x80" : "=a"(result)
         : "a"(num), "b"(arg1), "c"(arg2), "d"(arg3));
    return result;
}

static long syscall4(long num, long arg1, long arg2, long arg3, long arg4)
{
    long result;
    ASMV("int $0x80" : "=a"(result)
         : "a"(num), "b"(arg1), "c"(arg2), "d"(arg3), "S"(arg4));
    return result;
}

#elif defined(__arm__)

// Note these work identically with -mthumb; no ARM instructions are used.

static long syscall1(long num, long arg1)
{
    long result;
    ASMV("mov r7, %1\n\t"
         "mov r0, %2\n\t"
         "swi $0\n\t"
         "mov %0, r0"
         : "=r"(result)
         : "r"(num), "r"(arg1)
         : "r0", "r7");
    return result;
}

static long syscall3(long num, long arg1, long arg2, long arg3)
{
    long result;
    ASMV("mov r7, %1\n\t"
         "mov r0, %2\n\t"
         "mov r1, %3\n\t"
         "mov r2, %4\n\t"
         "swi $0\n\t"
         "mov %0, r0"
         : "=r"(result)
         : "r"(num), "r"(arg1), "r"(arg2), "r"(arg3)
         : "r0", "r1", "r2", "r7");
    return result;
}

static long syscall4(long num, long arg1, long arg2, long arg3, long arg4)
{
    long result;
    ASMV("mov r7, %1\n\t"
         "mov r0, %2\n\t"
         "mov r1, %3\n\t"
         "mov r2, %4\n\t"
         "mov r3, %5\n\t"
         "swi $0\n\t"
         "mov %0, r0"
         : "=r"(result)
         : "r"(num), "r"(arg1), "r"(arg2), "r"(arg3), "r"(arg4)
         : "r0", "r1", "r2", "r3", "r7");
    return result;
}

#else
#error Unrecognized CPU architecture
#endif

/* Define some syscalls */

int execve(char *f, char **argv, char **envp)
{ return syscall3(SYS_execve, (long)f, (long)argv, (long)envp); }

static void exit(int result)
{ syscall1(SYS_exit, result); }

static long fork(void)
{ return syscall1(SYS_fork, 0); }

/* "_open" because we can't get flags from system headers without a
 * declaration for "open()" */
static long _open(char *f, long flags, long mode)
{ return syscall3(SYS_open, (long)f, flags, mode); }

/* Likewise no declaration of struct pollfd without "poll()" */
static long _poll(struct pollfd *fds, long nfds, long timeout)
{ return syscall3(SYS_poll, (long)fds, nfds, timeout); }

static long setsid(void)
{ return syscall1(SYS_setsid, 0); }

/* waitpid() doesn't exist on all archs, emulate with wait4() */
static long waitpid(long pid, int *status, long opts)
{ return syscall4(SYS_wait4, pid, (long)status, opts, 0); }

static long write(int fd, void *p, long n)
{ return syscall3(SYS_write, fd, (long)p, n); }

int chdir(char *dir)
{ return syscall1(SYS_chdir, (long)dir); }

int chroot(char *dir)
{ return syscall1(SYS_chroot, (long)dir); }

/* And a minimal, um, "C library" */

static long strlen(char *p)
{ int n=0; while(*p++) n++; return n; }

static int puts(char *s)
{ return write(1, s, strlen(s)); }

static void bzero(void *buf, long n)
{ long i; for(i=0; i<n; i++) ((char*)buf)[i]=0; }

#endif /* _SYSCALL_ASM_H */
