#define a 3
#define b 2.3
char x = '0', y = 'o';
const int i = 0;

void main() {
	float f0 = 0.1, f1 = 0.2;
	bool b = f0 > f1;
	printf(b, ' ', f0*x, " ", y/f1, ' ', f1 != i, ' ', (int)f0, '\n');
}