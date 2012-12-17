#include <unistd.h>
#include <stdio.h>

int main(int argc, char **argv)
{
    char *dir;
    if(argc < 3) {
        fprintf(stderr, "Usage: %s <path> <command> [args...]\n");
        return 1;
    }
    dir = argv[1];
    if(chdir(dir)) {
        perror("cannot chdir to new root");
        return 1;
    }
    if(chroot(dir)) {
        perror("cannot chroot");
        return 1;
    }
    if(execv(argv[2], &argv[2])) {
        perror("cannot exec");
        return 1;
    }
}
