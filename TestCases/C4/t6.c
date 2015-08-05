int Binky(int a, int* b, int* c) {
    return b[c[0]];
}

void main() {
    int* c;
    int** d;

    d = (int**)malloc(sizeof(int*)*5);
    d[0] = (int*)malloc(sizeof(int)*12);
    c = (int*)malloc(sizeof(int)*10);
    c[0] = 4 + 5 * 3 / 4 % 2;
    d[0][c[0]] = 55;

    printf(c[0], " ", 2 * Binky(100, d[0], c));
}
