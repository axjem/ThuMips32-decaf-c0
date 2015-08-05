int abs(int a) {
    if (a >= 0) {
        return a;
    } else {
        return -a;
    }
}
    
int pow(int a, int b) {
    int i, result = 1;
    for (i = 0; i < b; i++) {
        result = result*a;
    }
    return result;
}

float log(int a) {
    if (a < 1) {
        return -1;
    }
    int result = 0;
    while (a > 1) {
        result += + 1;
        a = a / 2;
    }
    return result;
}
    
float max(float a, int b) {
    if (a > b) {
        return (int)a;
    } else {
        return b;
    }
}
    
float min(float a, int b) {
    if (a < b) {
        return a;
    } else {
        return b;
    }
}
  
void main() {
    printf(abs(-1), "\n");
    printf(pow(2, 3), "\n");
    printf(log(16), "\n");
    printf(max(1, 2), "\n");
    printf(min(1, 2), "\n");
}
