package beats.babel.babelbeats;

public class Pair {
    private final String key;
    private final double value;

    public Pair(String key, double value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + ": " + value;
    }
}