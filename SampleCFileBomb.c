//#include <stdio.h>
//#include "phase.h"
//#include <stdlib.h>
int main() {
    printf("Welcome to Justin bomb\nYou have 3 phase to go\nGood luck to you\n"); 

    char *input = NULL;
    int size;
    int read;

    /* input */
    if( (read = getline(&input,&size, stdin)) == -1) {
        exit(1);
    }
    input[read-1] = 0;
    phase1(input);
    free(input);
    input = NULL;
    printf("Nice, you pass the phase 1\n");

    if( (read = getline(&input,&size, stdin)) == -1) {
        exit(1);
    }
    input[read-1] = 0;
    phase2(input);
    free(input);
    input = NULL;
    printf("Nice, you pass the phase 2\n");

    if( (read = getline(&input,&size, stdin)) == -1) {
        exit(1);
    }
    input[read-1] = 0;
    phase3(input);
    free(input);
    input = NULL;
    printf("Congs, you pass them all\n");
    return 0;
}
