package me.ubi.game;

import java.util.List;

public class Player {
    private String name;
    private int points;
    private int floatingPoints;
    private List<Bonus> bonuses;

    public String getName() {
        return this.name;
    }

    public int getPoints() {
        return this.points;
    }

    public int getFloatingPoints() {
        return this.floatingPoints;
    }

    public List<Bonus> getBonuses() {
        return this.bonuses;
    }
}
