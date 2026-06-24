package Game;

import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
    private Timer timer;
    private int secondsLeft;
    private Runnable onTimeout;
    private Runnable onTick;

    public TimerManager(int seconds, Runnable onTimeout, Runnable onTick) {
        this.secondsLeft = seconds;
        this.onTimeout = onTimeout;
        this.onTick = onTick;
    }

    public void start() {
        stop();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsLeft--;
                if (onTick != null) onTick.run();
                if (secondsLeft <= 0) {
                    stop();
                    if (onTimeout != null) onTimeout.run();
                }
            }
        }, 1000, 1000);
    }

    public void stop() {
        if (timer != null) { timer.cancel(); timer = null; }
    }

    public void reset(int seconds) {
        stop();
        this.secondsLeft = seconds;
    }

    public int getSecondsLeft() { return secondsLeft; }
}
