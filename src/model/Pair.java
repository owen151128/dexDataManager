package model;

/**
 * Data type class for return type
 *
 * @param <K> 1 Data
 * @param <V> 2 Data
 * @param <S> 3 Data
 */
public class Pair<K, V, S> {
    public final K k;
    public final V v;
    public final S s;

    public Pair(K k, V v, S s) {
        this.k = k;
        this.v = v;
        this.s = s;
    }

    @Override
    public int hashCode() {
        return k.hashCode() ^ v.hashCode() ^ s.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;

        return this.k.equals(pair.k) && this.v.equals(pair.v) && this.s.equals(pair.s);
    }
}
