package chan.android.game2048;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ScoreBoxView extends FrameLayout {

    private int score = 0;

    private Animation animation;

    private TextView textViewAnim;

    private TextView textViewLabel;

    private TextView textViewScore;

    public ScoreBoxView(Context context) {
        this(context, null, 0);
    }

    public ScoreBoxView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
        textViewScore.setText(Integer.toString(score));
    }

    public void setScore(int score) {
        this.score = score;
        textViewScore.setText(Integer.toString(score));
    }

    public ScoreBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        animation = AnimationUtils.loadAnimation(context, R.anim.bubble_up);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewAnim.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.score_box, this);
        textViewAnim = (TextView) v.findViewById(R.id.score_box_$_textview_animation);
        textViewLabel = (TextView) v.findViewById(R.id.score_box_$_textview_label);
        textViewScore = (TextView) v.findViewById(R.id.score_box_$_textview_score);
        textViewScore.setText(Integer.toString(score));
        parseAttributes(context, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScoreBoxView, 0, 0);
        String label;
        try {
            label = a.getString(R.styleable.ScoreBoxView_label_text);
            textViewLabel.setText(label);
        } finally {
            a.recycle();
        }
    }

    public void addScore(int amount) {
        score += amount;
        textViewAnim.setVisibility(View.VISIBLE);
        textViewAnim.setText("+" + amount);
        textViewAnim.startAnimation(animation);
        textViewScore.setText(Integer.toString(score));
    }


}
