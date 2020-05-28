package yocto.util;

public class Pair<A, B> {
    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Pair<A, B> other = (Pair<A, B>) obj;
        return (a == other.a) && (b == other.b);
        // if (a != other.a)
        // return false;
        // if (b != other.b)
        // return false;

        // return true;
    }
}