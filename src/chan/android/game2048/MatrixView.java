package chan.android.game2048;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;

public class MatrixView extends TableLayout implements Swiper {

    private static final int N = Matrix.N;

    private Paint borderPaint;

    private Paint textPaint;

    private Paint squarePaint;

    private Matrix matrix;

    private int borderThickness = 15;

    private VelocityTracker velocityTracker;

    private Button[][] tiles;

    private Animation leftAnimation;

    private Animation rightAnimation;

    private Animation upAnimation;

    private Animation downAnimation;

    private Animation rotateAnimation;

    private Animation appearingAnimation;

    private SwipeListener swipeListener;

    private MoveListener moveListener;

    private boolean moreMove = true;

    public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.matrix, this);

        // Initialize all buttons
        tiles = new Button[N][N];
        tiles[0][0] = (Button) v.findViewById(R.id.button00);
        tiles[0][1] = (Button) v.findViewById(R.id.button01);
        tiles[0][2] = (Button) v.findViewById(R.id.button02);
        tiles[0][3] = (Button) v.findViewById(R.id.button03);

        tiles[1][0] = (Button) v.findViewById(R.id.button10);
        tiles[1][1] = (Button) v.findViewById(R.id.button11);
        tiles[1][2] = (Button) v.findViewById(R.id.button12);
        tiles[1][3] = (Button) v.findViewById(R.id.button13);

        tiles[2][0] = (Button) v.findViewById(R.id.button20);
        tiles[2][1] = (Button) v.findViewById(R.id.button21);
        tiles[2][2] = (Button) v.findViewById(R.id.button22);
        tiles[2][3] = (Button) v.findViewById(R.id.button23);

        tiles[3][0] = (Button) v.findViewById(R.id.button30);
        tiles[3][1] = (Button) v.findViewById(R.id.button31);
        tiles[3][2] = (Button) v.findViewById(R.id.button32);
        tiles[3][3] = (Button) v.findViewById(R.id.button33);

        leftAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        rightAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_right);
        upAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        downAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        appearingAnimation = AnimationUtils.loadAnimation(context, R.anim.appearing);

        matrix = new Matrix();
        displayMatrix(matrix, Direction.DOWN);

        swipeListener = new SwipeListener(context, this);
    }

    public void reset() {
        matrix = new Matrix();
        displayMatrix(matrix, Direction.DOWN);
        moreMove = true;
    }

    public void setMoveListener(MoveListener listener) {
        moveListener = listener;
    }

    public boolean hasMoreMove() {
        return moreMove;
    }

    private void displayMatrix(Matrix m, Direction dir) {
        final int n = Matrix.N;
        int number;
        for (int r = 0; r < n; ++r) {
            for (int c = 0; c < n; ++c) {
                number = m.getSpot(r, c);
                if (number == Matrix.EMPTY) {
                    tiles[r][c].setText("");
                } else {
                    tiles[r][c].setText(Integer.toString(number));
                }
                tiles[r][c].setTextSize(TypedValue.COMPLEX_UNIT_SP, getTextSize(number));
                tiles[r][c].setBackground(getResources().getDrawable(getDrawableId(number)));

                if (matrix.isMergeSpot(r, c)) {
                    tiles[r][c].startAnimation(appearingAnimation);
                }
                // applyEffect(tiles[r][c], dir);
            }
        }
        invalidate();
    }

    private boolean isEdge(int r, int c) {
        return (r == 0) || (r == N - 1) || (c == 0) || (c == N - 1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        swipeListener.getGestureDetector().onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onSwipeUp() {
        Log.e("2048", "swipe up");
        handleSwipe(Direction.UP);
    }

    @Override
    public void onSwipeRight() {
        Log.e("2048", "swipe right");
        handleSwipe(Direction.RIGHT);
    }

    @Override
    public void onSwipeLeft() {
        Log.e("2048", "swipe left");
        handleSwipe(Direction.LEFT);
    }

    @Override
    public void onSwipeDown() {
        Log.e("2048", "swipe down");
        handleSwipe(Direction.DOWN);
    }

    private void handleSwipe(Direction dir) {
        int score = matrix.swipe(dir);
        boolean gameOver = matrix.isStuck();
        boolean newSquare = matrix.generate();
        displayMatrix(matrix, dir);
        moveListener.onMove(score, gameOver, newSquare);
    }

    private void applyEffect(Button button, Direction dir) {
        if (dir == Direction.DOWN) {
            button.startAnimation(downAnimation);
            button.invalidate();
        } else if (dir == Direction.UP) {
            button.startAnimation(upAnimation);
            button.invalidate();
        } else if (dir == Direction.LEFT) {
            button.startAnimation(leftAnimation);
            button.invalidate();
        } else if (dir == Direction.RIGHT) {
            button.startAnimation(rightAnimation);
            button.invalidate();
        }
    }

    private int getTextSize(int n) {
        return 18;
    }

    private int getDrawableId(int n) {
        switch (n) {
            case 2:
                return R.drawable.n_2;

            case 4:
                return R.drawable.n_4;

            case 8:
                return R.drawable.n_8;

            case 16:
                return R.drawable.n_16;

            case 32:
                return R.drawable.n_32;

            case 64:
                return R.drawable.n_64;

            case 128:
                return R.drawable.n_128;

            case 256:
                return R.drawable.n_256;

            case 512:
                return R.drawable.n_512;

            case 1024:
                return R.drawable.n_1024;

            case 2048:
                return R.drawable.n_2048;
        }
        return R.drawable.n_0;
    }
}
