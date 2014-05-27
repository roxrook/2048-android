package chan.android.game2048;

import android.util.Log;

import java.util.*;

public class Matrix {

    public static final int EMPTY = 0;

    public static final int N = 4;

    private int[][] numbers;

    private Random random;

    private List<Spot> emptySpots;

    private Set<Spot> mergeSpots;

    private Spot newSpot;

    public Matrix() {
        random = new Random();
        numbers = new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                numbers[i][j] = EMPTY;
            }
        }

        emptySpots = new ArrayList<Spot>();
        mergeSpots = new HashSet<Spot>();

        newSpot = new Spot(0, 0);

        // Generate 2 random spots
        for (int i = 0; i < 5; ++i) {
            spawn(2);
        }
    }

    private void generateAll() {
        spawn(2);
        spawn(4);
        spawn(8);
        spawn(16);
        spawn(32);
        spawn(64);
        spawn(128);
        spawn(256);
        spawn(512);
        spawn(1024);
        spawn(2048);
    }

    /**
     * Generate a square with the distribution:
     *      80% = 2
     *      15% = 4
     *      5% = 'empty'
     */
    public boolean generate() {
        int v = random.nextInt(100);
        if (0 <= v && v <= 79) {
            spawn(2);
            return true;
        } else if (80 <= v && v <= 95) {
            spawn(4);
            return true;
        }
        return false;
    }

    public Matrix(Matrix copy) {
        random = copy.random;
        numbers = new int[N][N];
        for (int r = 0; r < N; ++r) {
            for (int c = 0; c < N; ++c) {
                numbers[r][c] = copy.numbers[r][c];
            }
        }
        emptySpots = new ArrayList<Spot>();
        for (Spot spot : copy.emptySpots) {
            emptySpots.add(spot);
        }

        mergeSpots = new HashSet<Spot>();
        for (Spot spot : copy.mergeSpots) {
            mergeSpots.add(spot);
        }

        newSpot = new Spot(copy.newSpot.r, copy.newSpot.c);
    }

    public int getSpot(int r, int c) {
        return numbers[r][c];
    }

    private void spawn(int n) {
        collectEmptySpots();
        if (!emptySpots.isEmpty()) {
            int i = random.nextInt(emptySpots.size());
            newSpot = emptySpots.get(i);
            numbers[newSpot.r][newSpot.c] = n;
        }
    }

    private boolean isPowerOfTwo(int n) {
        if (n < 1)
            return false;
        return  (n & (n - 1)) == 0;
    }

    public Spot getNewSpot() {
        return newSpot;
    }

    private void collectEmptySpots() {
        emptySpots.clear();
        for (int x = 0; x < N; ++x) {
            for (int y = 0; y < N; ++y) {
                if (numbers[x][y] == EMPTY) {
                    emptySpots.add(new Spot(x, y));
                }
            }
        }
    }

    public boolean isStuck() {
        Log.e("2048", "Hello");
        Matrix copy = new Matrix(this);
        int score = 0;
        for (Direction d : Direction.values()) {
            score += copy.swipe(d);
        }
        copy.collectEmptySpots();
        Log.e("2048", "Hello end()");
        return score == 0 && copy.emptySpots.isEmpty();
    }

    public int mergeLeft(int row) {
        int idx = 0;
        int score = 0;
        boolean merged = false;
        for (int i = 0; i < N; ++i) {
            if (numbers[row][i] != EMPTY) {
                // Find the farthest to the right of i
                int farthest = -1;
                for (int j = i + 1; j < N; ++j) {
                    if (numbers[row][j] != EMPTY) {
                        farthest = j;
                        break;
                    }
                }

                merged = false;
                if (farthest != -1) {
                    // Ok merge
                    if (numbers[row][i] == numbers[row][farthest]) {
                        numbers[row][i] += numbers[row][farthest];
                        score += numbers[row][i];
                        numbers[row][farthest] = EMPTY;
                        merged = true;
                    }
                }
                numbers[row][idx] = numbers[row][i];
                if (merged) {
                    mergeSpots.add(new Spot(row, idx));
                }
                if (idx != i) {
                    numbers[row][i] = EMPTY;
                }
                idx++;
            }
        }
        return score;
    }

    public int mergeRight(int row) {
        int idx = N - 1;
        int score = 0;
        boolean merged = false;
        for (int i = N - 1; i >= 0; --i) {
            if (numbers[row][i] != EMPTY) {
                // Find the farthest to the right of i
                int farthest = -1;
                for (int j = i - 1; j >= 0; --j) {
                    if (numbers[row][j] != EMPTY) {
                        farthest = j;
                        break;
                    }
                }

                merged = false;
                if (farthest != -1) {
                    // Ok merge
                    if (numbers[row][i] == numbers[row][farthest]) {
                        numbers[row][i] += numbers[row][farthest];
                        score += numbers[row][i];
                        numbers[row][farthest] = EMPTY;
                        merged = true;
                    }
                }
                numbers[row][idx] = numbers[row][i];
                if (merged) {
                    mergeSpots.add(new Spot(row, idx));
                }
                if (idx != i) {
                    numbers[row][i] = EMPTY;
                }
                idx--;
            }
        }
        return score;
    }

    public int mergeUp(int column) {
        int idx = 0;
        int score = 0;
        boolean merged = false;
        for (int i = 0; i < N; ++i) {
            if (numbers[i][column] != EMPTY) {
                // Find the farthest to the right of i
                int farthest = -1;
                for (int j = i + 1; j < N; ++j) {
                    if (numbers[j][column] != EMPTY) {
                        farthest = j;
                        break;
                    }
                }
                merged = false;
                if (farthest != -1) {
                    // Ok merge
                    if (numbers[i][column] == numbers[farthest][column]) {
                        numbers[i][column] += numbers[farthest][column];
                        score = numbers[i][column];
                        numbers[farthest][column] = EMPTY;
                        merged = true;
                    }
                }
                numbers[idx][column] = numbers[i][column];
                if (merged) {
                    mergeSpots.add(new Spot(idx, column));
                }
                if (idx != i) {
                    numbers[i][column] = EMPTY;
                }
                idx++;
            }
        }
        return score;
    }

    public int mergeDown(int column) {
        int idx = N - 1;
        int score = 0;
        boolean merged;
        for (int i = N - 1; i >= 0; --i) {
            if (numbers[i][column] != EMPTY) {
                // Find the farthest to the right of i
                int farthest = -1;
                for (int j = i - 1; j >= 0; --j) {
                    if (numbers[j][column] != EMPTY) {
                        farthest = j;
                        break;
                    }
                }

                merged = false;
                if (farthest != -1) {
                    // Ok merge
                    if (numbers[i][column] == numbers[farthest][column]) {
                        numbers[i][column] += numbers[farthest][column];
                        score = numbers[i][column];
                        numbers[farthest][column] = EMPTY;
                        merged = true;
                    }
                }
                numbers[idx][column] = numbers[i][column];
                if (merged) {
                    mergeSpots.add(new Spot(idx, column));
                }
                if (idx != i) {
                    numbers[i][column] = EMPTY;
                }
                idx--;
            }
        }
        return score;
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public boolean isMergeSpot(int r, int c) {
        return mergeSpots.contains(new Spot(r, c));
    }

    private int[] cloneRow(int r) {
        int[] copy = new int[4];
        for (int i = 0; i < N; ++i) {
            copy[i] = numbers[r][i];
        }
        return copy;
    }

    private int[] cloneColumn(int c) {
        int[] copy = new int[4];
        for (int i = 0; i < N; ++i) {
            copy[i] = numbers[i][c];
        }
        return copy;
    }

    private boolean isSame(int[] before, int[] after) {
        for (int i = 0; i < N; ++i) {
            if (before[i] != after[i]) {
                return false;
            }
        }
        return true;
    }

    private int[][] cloneNumbers() {
        int[][] copy = new int[N][N];
        for (int r = 0; r < N; ++r) {
            for (int c = 0; c < N; ++c) {
                copy[r][c] = numbers[r][c];
            }
        }
        return copy;
    }

    public int swipe(Direction dir) {
        int totalScore = 0;
        mergeSpots.clear();
        if (dir == Direction.DOWN || dir == Direction.UP) {
            for (int i = 0; i < N; ++i) {
                totalScore += (dir == Direction.DOWN) ? mergeDown(i) : mergeUp(i);
            }
        } else {
            for (int i = 0; i < N; ++i) {
                totalScore += (dir == Direction.LEFT) ? mergeLeft(i) : mergeRight(i);
            }
        }
        return totalScore;
    }

    public static class Spot {
        public final int r;
        public final int c;

        public Spot(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Spot)) return false;

            Spot spot = (Spot) o;

            if (c != spot.c) return false;
            if (r != spot.r) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = r;
            result = 31 * result + c;
            return result;
        }
    }

    public void setSpot(int r, int c, int value) {
        numbers[r][c] = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < N; ++r) {
            for (int c = 0; c < N; ++c) {
                builder.append("|");
                builder.append(Integer.toString(numbers[r][c]));
                builder.append("|");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
