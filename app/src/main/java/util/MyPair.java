package util;

/**
 * Container to ease passing around a tuple of two objects.
 * objects.
 */
public class MyPair {
    public final String first;
    public final String second;

    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public MyPair(String first, boolean second) {
        this.first = first;
        this.second = String.valueOf(second);
    }

    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public MyPair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public MyPair(String first, int second) {
        this.first = first;
        this.second = String.valueOf(second);
    }

    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public MyPair(String first, double second) {
        this.first = first;
        this.second = String.valueOf(second);
    }
}
