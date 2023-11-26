package me.ubi.game;

import java.util.List;

public class PlayerClass implements Player {
    private String name;
    private int points;
    private int floatingPoints;
    private List<Bonus> bonuses;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public int getFloatingPoints() {
        return this.floatingPoints;
    }

    @Override
    public List<Bonus> getBonuses() {
        return this.bonuses;
    }
}
