#include "syscall-asm.h"

/* Tiny chroot utility suitable buidling without a C library. */

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
