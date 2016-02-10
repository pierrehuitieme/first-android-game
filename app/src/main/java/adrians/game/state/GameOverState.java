package adrians.game.state;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.concurrent.Callable;

import adrians.framework.util.Painter;
import adrians.framework.util.ToggleButton;
import adrians.simpleandroidgdf.Assets;
import adrians.simpleandroidgdf.GameMainActivity;

/**
 * Created by pierre on 10/02/16.
 */
public class GameOverState extends State {
    private String playerScore;
    private String gameOverMessage = "GAME OVER";
    private ToggleButton soundButton;

    public GameOverState(int playerScore) {
        this.playerScore = "" + playerScore;
        if(playerScore > GameMainActivity.getHighScore()) {
            GameMainActivity.setHighScore(playerScore);
            gameOverMessage = "HIGH SCORE";
        }
    }
    @Override
    public void init() {
        soundButton = new ToggleButton(20, 20, 80, 80, Assets.soundOn, Assets.soundOff, !GameMainActivity.playSound, new Callable<Integer>() {
            @Override
            public Integer call() {
                GameMainActivity.toggleSound();
                return 0;
            }
        });
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(Painter g) {
        g.setColor(Color.rgb(255, 144, 0));
        g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
        g.setColor(Color.DKGRAY);
        g.setFont(Typeface.DEFAULT_BOLD, 50);
        g.drawString(gameOverMessage, 257, 175);
        g.drawString(playerScore, 385, 250);
        g.drawString("Touch the screen.", 220, 350);
        soundButton.render(g);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if(e.getAction() == MotionEvent.ACTION_UP) {
            if(e.getAction() == MotionEvent.ACTION_UP) {
                if(soundButton.getRect().contains(scaledX, scaledY)) {
                    soundButton.onTouchDown(scaledX, scaledY);
                } else {
                    setCurrentState(new MenuState());
                }
            } else {
                setCurrentState(new MenuState());
            }
        }
        return true;
    }
}
