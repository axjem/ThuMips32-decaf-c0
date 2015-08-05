int func(int x, int y);

int A() {
}

int A, B;

char C;

void func2(int x);

void func3(int x, char y, char* z);

void main() {
    func(10, 10);
	int func;
	func();
}

int func(bool x, char y) {
	return x;
}

void func2(int x) {
	x++;
	return;
}

int func3(int x, char y, char* z) {
	return x;
}

void func4(int x) {
	int y;
	{
		int y;
		y = 1;
		bool x;
	}
	int x;
	switch(y) {
		case 1:
			int x;
			break;
		case 2:
			int x;
			break;
	}
	return x;
}
