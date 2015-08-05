void strcpy(char* s, char* str) {
    int i = 0;
    while(str[i] != '\0') {
        s[i] = str[i];
        i++;
    }
    s[i] = '\0';
}

void main() {
    int m;
    m = 77;
    char c;
    c = (char)m;
    bool b;
    b = (bool)c;
    int i;
    i = (int)b;
    printf(m, ' ', c, ' ', b, ' ', i, '\n');
    float f1 = m, f2 = c, f3 = (float)b;
    char str[10];
    strcpy(str, "aaaa");
    int* trans = (int*)str;
    printf(str[3], ' ', f1, ' ', f2, ' ', f3, ' ', trans[0], '\n');
    printf((char)trans[0]+58621, ' ', (int)b+m, ' ', (int)f1, '\n');
}
