package adrians.framework;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.util.HashSet;
import java.util.Set;

import adrians.game.state.PlayState;
import adrians.game.state.StateManager;
import pierreSquared.SquareRunner.R;

/**
 * Created by pierre on 06/02/16.
 */
public class GameMainActivity extends Activity {
    public static int GAME_WIDTH, GAME_HEIGHT;
    public static GameView sGame;
    public static AssetManager assets;
//    public static boolean playSound = true;

    private static SharedPreferences prefs;
    private static Set<String> unlocked = new HashSet<>();
    private static String unlocksTag="Unlocked";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        if(Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            GAME_WIDTH = size.x;
            GAME_HEIGHT = size.y;
        } else {
            //noinspection deprecation
            GAME_WIDTH = display.getWidth();
            //noinspection deprecation
            GAME_HEIGHT = display.getHeight();
        }
//        prefs = getPreferences(Activity.MODE_PRIVATE);
//        highScore = retrieveHighScore();
        prefs = getSharedPreferences(unlocksTag, 0);
        unlocked = retrieveUnlocked();

        StateManager.setActivity(this);
        assets=getAssets();
        sGame = new GameView(this, GAME_WIDTH, GAME_HEIGHT);
        setContentView(sGame);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void setUnlocked(Set<String> unlocked) {
        GameMainActivity.unlocked = unlocked;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(unlocksTag, unlocked);
        editor.commit();
    }

    private Set<String> retrieveUnlocked() {
        return prefs.getStringSet(unlocksTag, unlocked);
    }

    public static Set<String> getUnlocked() {
        return unlocked;
    }
//    public static int getHighScore() {
//        return highScore;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Assets.onResume();
        sGame.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Assets.onPause();
        sGame.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == StateManager.GetFileActivityCode) {
            Uri uri = null;
            if(data != null) {
                uri = data.getData();
                assert uri != null;
                Log.i("LOL", uri.toString());

                StateManager.pushState(new PlayState(uri.toString()));
            }
        }
    }

    //    public static void toggleSound() {
//        playSound ^= true;
//        if(playSound) {
//            Assets.resumeMusic();
//        } else {
//            Assets.pauseMusic();
//        }
//    }
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
}
