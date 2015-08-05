#define limit 10
const int limit2 = 4;
const int limit3 = 17;

void init_QueueItem(int* host, int data, int* next, int* prev) {
    host[0] = data;
    host[1] = (int)next;
    host[2] = (int)prev;
    next[2] = (int)host;
    prev[1] = (int)host;
}

int sp;
int* head;

void init_Queue();
void enqueue(int i);
int dequeue();

void main() {
    int i;
    init_Queue();
    for (i = 0; i < limit; i++)
        enqueue(i);

    for (i = 0; i < limit2; i += 1)
        printf(dequeue(), " ");
    printf("\n");

    for (i = 0; i < limit; ++i)
        enqueue(i);
    for (i = 0; i < limit3; i = i + 1)
        printf(dequeue(), " ");
    printf("\n");
}

void init_Queue() {
    head = (int*)malloc(sizeof(int)+2*sizeof(int*));
    init_QueueItem(head, 0, head, head);
}

void enqueue(int i) {
    int* temp = (int*)malloc(sizeof(int)+2*sizeof(int*));
    init_QueueItem(temp, i, (int*)head[1], head);
}

int dequeue() {
    if ( ((int)((int*)head[2])) == (int)head) {
        printf("Queue Is Empty");
        return 0;
    } else {
        int* temp = (int*)head[2];
        int val = temp[0];
        ((int*)temp[2])[1] = temp[1];
        ((int*)temp[1])[2] = temp[2];
        return val;
    }
}