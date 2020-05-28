package yocto.util;

public class IntegerPair extends Pair<Integer, Integer> {

    public IntegerPair(Integer a, Integer b) {
        super(a, b);
    }
    
    @Override
    public int hashCode() {
        return a * 7639 + b;
    }
}