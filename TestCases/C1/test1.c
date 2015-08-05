int* foo(int y) {
    float* q, a, **b;
    int i = 0;
    q = (float*)malloc(y*sizeof(float));
    while (i < y) {
        q[i] = i;
        i++;
    }

    return q;
}

void main() {
	const bool b = true , c = false;
	float d[10], ***e[10];
    char* str = "hello";
	d[0] = 0.1;
	if(b)
		if (b)
			b = false;
		else
			b = true;

    return foo(5)[1];
}