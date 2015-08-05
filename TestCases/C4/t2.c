#define integer 3
void strcpy(char* s, char* str) {
	int i = 0;
	while(str[i] != '\0') {
		s[i] = str[i];
		i++;
	}
	s[i] = '\0';
}

void main() {
    int a;
    char* b = "hello";
    bool c;

	b = (char*)malloc(5*sizeof(char));
    c = true;
	strcpy(b, "wow!");
    a = 3;
    if (c)
        a = a * integer;
    printf(c, " ", a, ' ', b);
}