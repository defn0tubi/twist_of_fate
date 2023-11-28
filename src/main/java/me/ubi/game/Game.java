package me.ubi.game;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private List<Player> players;
    private Timer timer;
    private int totalRounds;

    public Game(List<Player> players, Timer timer, int totalRounds) {
        this.players = players;
        this.timer = timer;
        this.totalRounds = totalRounds;
    }

    public void startTurn() {
        int turnTime = Integer.parseInt(GameSettings.TURN_TIME.getValue());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                endTurn();
            }
        }, turnTime);
    }

    public void endTurn() {
        timer.cancel();


    }

    public int getRounds() {
        return 0;
    }

    public int getRound() {
        return 0;
    }

    public List<Player> getPlayers() {
        return null;
    }
}
