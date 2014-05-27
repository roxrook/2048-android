package chan.android.game2048;


import android.content.Context;
import android.content.SharedPreferences;

public class GamePreferences {

    enum Key {
        BEST_SCORE,
        SOUND_ENABLE,
    }

    private static SharedPreferences prefs;

    public static void initialize(Context context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("chan.android.game._2048.prefs_", Context.MODE_PRIVATE);
        }
    }

    public static void saveBestScore(int score) {
        prefs.edit().putInt(Key.BEST_SCORE.name(), score).commit();
    }

    public static int getBestScore() {
        return prefs.getInt(Key.BEST_SCORE.name(), 0);
    }
}
