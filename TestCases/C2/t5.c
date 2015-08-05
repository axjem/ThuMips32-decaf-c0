float height, weight;

void init(int w, int h) {
    weight = w;
    height = h;
}

void Moo() {
    printf( height, " ", weight, "\n" );
}

void main() {
    init(100, 122);
    Moo();
}
