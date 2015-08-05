//we don't check the upbound of array, so this may overwrite other data......
void main() {
    int* arr;
    arr = (int*)malloc(sizeof(int)*2);
    arr[2] = 0;
    printf("after:", arr[2]);
}