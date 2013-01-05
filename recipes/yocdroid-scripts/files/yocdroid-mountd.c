#include "syscall-asm.h"

/* As for chroot-static, this is a tiny libc-free utility that does
 * exactly one thing: it polls /proc/mounts and executes a program
 * passed on the command line whenever it changes. */

void _start()
{
    // Parse out the main() arguments
    long *stack = __builtin_frame_address(0);
    long argc = stack[1];
    char **argv = (char**)&stack[2];
    char **environ = (char**)&stack[argc+3];

    if(argc < 2) {
        puts("Usage: yocdroid-mountd <filename> [args...]\n");
        exit(1);
    }

    long fd = _open("/proc/mounts", O_RDONLY, 0);
    if(fd < 0) {
        puts("Cannot open /proc/mounts");
        exit(1);
    }

    // Daemonize, but leave stdio working for the script we run
    // (there's no other debug/log facility available to us).
    if(fork())
        exit(0);
    setsid();
    if(fork())
        exit(0);

    for(;;) {
        long pid;
        int result_dummy;
        struct pollfd pfd;

        // Wait for /proc/mounts to change.
        //
        // Note that there's a race here: a mount that happens between
        // the return of one poll call and the beginning of the next
        // (basically "while the script is running") will be missed.
        // Re-entering poll() immediately (which still has a race, but
        // an infinitiesimal one) would require catching SIGCHLD to
        // know when to re-run the script and handling the resulting
        // EINTR from poll...
        bzero(&pfd, sizeof(pfd));
        pfd.fd = fd;
        if(_poll(&pfd, 1, -1) < 0) {
            puts("poll() failed");
            exit(1);
        }

        // Spawn and run the handler
        if(pid = fork()) {
            waitpid(pid, &result_dummy, 0);
        } else {
            if(execve(argv[1], &argv[1], environ)) {
                puts("cannot exec");
                exit(1);
            }
        }
    }
}
