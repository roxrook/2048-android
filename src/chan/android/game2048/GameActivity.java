package chan.android.game2048;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends Activity implements View.OnTouchListener {

    private MatrixView matrixView;

    private ScoreBoxView currentScore;

    private ScoreBoxView bestScore;

    private Button buttonNewGame;

    private SoundManager soundManager;

    private SwipeListener swipeListener;

    private LinearLayout linearLayoutOutermost;

    private TextView textViewGameTitle;

    private Animation slideUpAnimation;

    private TextView textViewLucky;

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

        textViewLucky = (TextView) findViewById(R.id.main_$_textview_lucky);
        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.all_the_way_up);
        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewLucky.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textViewGameTitle = (TextView) findViewById(R.id.main_$_textview_game_title);
        textViewGameTitle.setText(Html.fromHtml(getString(R.string.game_title)));

        currentScore = (ScoreBoxView) findViewById(R.id.main_$_scoreboxview_current);
        bestScore = (ScoreBoxView) findViewById(R.id.main_$_scoreboxview_best);
        matrixView = (MatrixView) findViewById(R.id.main_$_matrixview);
        matrixView.setMoveListener(new MoveListener() {
            @Override
            public void onMove(int score, boolean gameOver, boolean newSquare) {
                if (gameOver) {
                    displayGameOverDialog();
                } else {
                    soundManager.playSliding();
                    if (!newSquare) {
                        textViewLucky.setVisibility(View.VISIBLE);
                        textViewLucky.startAnimation(slideUpAnimation);
                    }
                    if (score > 0) {
                        currentScore.addScore(score);
                        if (currentScore.getScore() > bestScore.getScore()) {
                            bestScore.setScore(currentScore.getScore());
                            GamePreferences.saveBestScore(bestScore.getScore());
                        }
                        if (score >= 2048) {
                            displayCongratsDialog();
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
        final GameDialog d = new GameDialog(this, "Oops, you lost! Try again now?", "Cancel", "New Game");
        d.setOnGameDialogClickListener(new OnGameDialogClickListener() {
            @Override
            public void onLeftClick() {
                d.dismiss();
            }

            @Override
            public void onRightClick() {
                d.dismiss();
                onNewGameClick();
            }
        });
        d.show();
    }

    private void displayCongratsDialog() {
        final GameDialog d = new GameDialog(this, "Wow, you win! Continue to get a better > 2048?", "New Game", "Continue");
        d.setOnGameDialogClickListener(new OnGameDialogClickListener() {
            @Override
            public void onLeftClick() {
                d.dismiss();
                onNewGameClick();
            }

            @Override
            public void onRightClick() {
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return swipeListener.getGestureDetector().onTouchEvent(event);
    }
}
