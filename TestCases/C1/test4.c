#define str "this is a string"
#define c 'c'

void main() {
	int var;
	var = 10;
	++var;
	var--;
	int** x;
	x = (int**)malloc(20*sizeof(int*));
	int i;
	for (i = 0; i < 20; i++) {
		x[i] = (int*)malloc(30*sizeof(int));
	}
	++var+1;
	var+++var;

	printf(str, c, var);
	return 0;
}