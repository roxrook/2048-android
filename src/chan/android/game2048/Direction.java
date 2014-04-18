package chan.android.game2048;


public enum Direction {

    UP    (+0, -1, Matrix.N, -1, -1),

    DOWN  (+0, +1, -1, Matrix.N, +1),

    LEFT  (-1, +0, Matrix.N, -1, -1),

    RIGHT (+0, +1, -1, Matrix.N, +1);

    public final int x;
    public final int y;
    public final int begin;
    public final int end;
    public final int shift;

    Direction(int x, int y, int begin, int end, int shift) {
        this.x = x;
        this.y = y;
        this.begin = begin;
        this.end = end;
        this.shift = shift;
    }
}
