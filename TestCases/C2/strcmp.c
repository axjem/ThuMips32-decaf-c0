int lengthOf(char* s) {
	int l = 0;
	while (s[l] != '\0') {
		l++;
	}
	return l;
}

bool strcmp(char* a, char* b) {
	if (lengthOf(a) != lengthOf(b)) {
		return false;
	}
	int i = 0;
	while (a[i] != '\0') {
		if (a[i] != b[i])
			return false;
		i++;
	}
	return true;
}

void printCompareString(char* a, char* b) {
	printf("\"", a, "\" and \"", b, "\": ", strcmp(a, b), "\n");
}

void main() {
	printCompareString("Jobs", "Gates");
	printCompareString("case sensitive", "CASE SENSITIVE");
	printCompareString("Hello", "Hello");
	char *str1, *str2;
	//should need alloc some space for buffer
	scanf(str1, str2);
	printCompareString(str1, str2);
}
