package chan.android.game2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class GameActivity extends Activity implements View.OnTouchListener {

    private MatrixView matrixView;

    private ScoreBoxView currentScore;

    private ScoreBoxView bestScore;

    private Button buttonNewGame;

    private SoundManager soundManager;

    private SwipeListener swipeListener;

    private LinearLayout linearLayoutOutermost;

    @Override
    protected void onResume() {
        bestScore.setScore(GamePreferences.getBestScore());
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        soundManager = new SoundManager(getApplicationContext());


        currentScore = (ScoreBoxView) findViewById(R.id.main_$_scoreboxview_current);
        bestScore = (ScoreBoxView) findViewById(R.id.main_$_scoreboxview_best);
        matrixView = (MatrixView) findViewById(R.id.main_$_matrixview);
        matrixView.setMoveListener(new MoveListener() {
            @Override
            public void onMove(int score, boolean gameOver) {
                if (gameOver) {
                    displayGameOverDialog();
                } else {
                    soundManager.playSliding();
                    if (score > 0) {
                        currentScore.addScore(score);
                        if (currentScore.getScore() > bestScore.getScore()) {
                            bestScore.setScore(currentScore.getScore());
                            GamePreferences.saveBestScore(bestScore.getScore());
                        }
                    }
                }
            }
        });

        buttonNewGame = (Button) findViewById(R.id.main_$_button_new_game);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewGameClick();
            }
        });

        linearLayoutOutermost = (LinearLayout) findViewById(R.id.main_$_linearlayout_outermost);
        linearLayoutOutermost.setOnTouchListener(this);
        swipeListener = new SwipeListener(this, new Swiper() {
            @Override
            public void onSwipeLeft() {
                matrixView.onSwipeLeft();
            }

            @Override
            public void onSwipeRight() {
                matrixView.onSwipeRight();
            }

            @Override
            public void onSwipeUp() {
                matrixView.onSwipeUp();
            }

            @Override
            public void onSwipeDown() {
                matrixView.onSwipeDown();
            }
        });
    }

    private void onNewGameClick() {
        matrixView.reset();
        currentScore.resetScore();
    }

    private void displayGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getResources().getDrawable(R.drawable.ic_crying));
        builder.setTitle("Game Over");
        builder.setMessage("Game is over! Do you want to start a new game now?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onNewGameClick();
            }
        });
        // create alert dialog
        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return swipeListener.getGestureDetector().onTouchEvent(event);
    }
}
