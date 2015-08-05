void strcpy(char* s, char* str) {
    int i = 0;
    while(str[i] != '\0') {
        s[i] = str[i];
        i++;
    }
    s[i] = '\0';
}

int test(float a, float b);

void main() {
    int c;
    char* s = (char*)malloc(sizeof(char)*10);
    strcpy(s, "hello");
    c = test(4, 5);
    printf(c);
    printf(s);
}

int test(float a, float b) {
    return (int)(a + b);
}