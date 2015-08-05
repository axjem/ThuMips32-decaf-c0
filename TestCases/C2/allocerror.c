int* a;

void func();

int main() {
        
}
    
void func() {
    a = (int*)malloc(-1*sizeof(int)); //valid
    a = (int*)malloc("123"*sizeof(int));;
    a = (int*)malloc(23.3*sizeof(int));;
    int a;
    a[1] = 2;
    a = a[false];
}