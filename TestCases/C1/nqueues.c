// a program to solve the N-queens problem (from Cornell CS412 website)
#define size 8
//const int size = 8;
int N;
int *col, *row, *di1, * di2;

void printBoard(int* col, int N);

void try(int c, int* row, int* col, int* diag1, int* diag2, int length);

int* clearArray(int* a, int length) {
    int i = length - 1;
    while (i >= 0) {
        a[i] = 0;
		i--;
    }

    return a;
}

void init(int n) {
    N = n;
    col = clearArray((int*)malloc(N*sizeof(int)), N);
    row = clearArray((int*)malloc(N*sizeof(int)), N);
    di1 = clearArray((int*)malloc(N*sizeof(int)), N);
    di2 = clearArray((int*)malloc(N*sizeof(int)), N);
}

void solve() {
    try(0, col, row, di1, di2, N);
}

void main() {
    init(size);
    solve();
}

void printBoard(int* col, int N) {
    int i = 0;
    while (i < N) {
        int j = 0;
        while (j < N) {
            if (col[i] == j) {
                printf(1);
            } else {
                printf(0);
            }
        }
        printf("\n");
        i++;
    }
}

void try(int c, int* row, int* col, int* diag1, int* diag2, int length) {
    int N = length, r = 0;
    if (c == N) {
        printBoard(col, N);
    } else {
        while (r < N) {
            if (row[r] == 0 && diag1[r+c] == 0 && diag2[r+(N-1)-c] == 0) {
                row[r] = 1;
                diag1[r+c] = 1;
                diag2[r+(N-1)-c] = 1;
                col[c] = r;
                try(c+1, row, col, diag1, diag2, length);
                row[r] = 0;
                diag1[r+c] = 0;
                (*(diag2+r+(N-1)-c)) = 0;
            }
            r = r + 1;
        }
    }
}
