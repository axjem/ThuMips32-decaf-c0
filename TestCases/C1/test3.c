int foo(int x, int y, int z) {
    return (x + y + z)/3;
}

int method() {
    int x;
    int y;
    int z;

    x = 3;
    y = x + x * x;
    z = (x - y / x) % y;

    return foo(x, y, z);
}

void main() {
	method();
	return 0;
}