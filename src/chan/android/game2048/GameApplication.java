package chan.android.game2048;


import android.app.Application;

public class GameApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GamePreferences.initialize(this);
    }
}
