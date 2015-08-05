// Computing Fibonacci Sequence

// gets F_i
int get(int index);
   
void main() {
    int i = 0;                  // for i from 0 to 9, prints F_i
    while (i < 10) {
        printf(get(i), "\n");
        i++;
    }
}

int get(int index) {
    if (index < 2) {
        return 1;
    }
    return get(index - 1) + get(index - 2);
}
