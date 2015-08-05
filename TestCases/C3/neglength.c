//since we check the subscript of array, we will claim the error here.
bool* arr;

void create(int size) {
    arr = (bool*)malloc(sizeof(bool)*size);
}

void main() {
    create(-1);
    printf("after");
}
