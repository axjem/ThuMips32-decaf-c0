int a;
char** b;

int* tester(int d) {
    b = (char**)malloc(sizeof(char*));
    return (int*)malloc(d*sizeof(int));
}

void start() {
    int a;
    bool b;
    int* d;
    a = 1;
    while (a < 5) {
        if (a % 2 == 0) {
            d = tester(a);
            break;
        }
        printf("Loop ", a, "\n");
        a++;
    }
    d[0] = 0;
    printf(d[d[0]], "\n");
}

void main() {
    start();
}