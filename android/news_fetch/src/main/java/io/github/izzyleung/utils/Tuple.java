package io.github.izzyleung.utils;

public final class Tuple<L, M, R> {
    private final L left;
    private final M middle;
    private final R right;

    public Tuple(final L left, final M middle, final R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public M getMiddle() {
        return middle;
    }

    public R getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + ", " + middle + ", " + right + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?, ?> tuple = (Tuple<?, ?, ?>) o;

        return (left != null ? left.equals(tuple.left) : tuple.left == null)
                && (middle != null ? middle.equals(tuple.middle) : tuple.middle == null)
                && (right != null ? right.equals(tuple.right) : tuple.right == null);
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (middle != null ? middle.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
