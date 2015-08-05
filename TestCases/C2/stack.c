int sp;
int* elems;

void Push(int i);
void Init();
int Pop();

void Init() {
    elems = (int*)malloc(100*sizeof(int));
    sp = 0;
    Push(3);
}

void Push(int i) {
    *(elems + sp) = i;
    ++sp;
}

int Pop() {
    int val = elems[sp - 1];
    sp--;
    return val;
}

int NumElems() {
    return sp;
}

void main() {
    Init();
    Push(3);
    Push(7);
    Push(4);
    printf(NumElems(), " ", Pop(), " ", Pop(), " ", Pop(), " ", NumElems());
}
