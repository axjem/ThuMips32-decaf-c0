//we don't check the upbound of array, but OS may check for us
void main() {
    int* arr;
    arr = (int*)malloc(sizeof(int)*2);
    arr[-1] = 0;
    printf("after:", arr[-1]);
}