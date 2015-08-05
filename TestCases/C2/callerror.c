int add(int a, int b) {
    return a + b;
}

int sub(int a, bool b) {
    return a - b;
}

void main() {
    printf(add(1));
    int* a;
    a = (int*)malloc(sizeof(int)*true);
    int b = 1;
    sub(1, 2);
    mul(1, 2);
}