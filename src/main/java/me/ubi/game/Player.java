package me.ubi.game;

import java.util.List;

public class Player {
    private final String name;
    private int points;
    private int floatingPoints;
    private List<Bonus> bonuses;

    private boolean canRob;

    public Player(String name, int points, int floatingPoints, List<Bonus> bonuses, boolean canRob) {
        this.name = name;
        this.points = points;
        this.floatingPoints = floatingPoints;
        this.bonuses = bonuses;
        this.canRob = false;
    }

    public String getName() {
        return this.name;
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getFloatingPoints() {
        return this.floatingPoints;
    }

    public void addFloatingPoints(int points) {
        this.floatingPoints += points;
    }

    public List<Bonus> getBonuses() {
        return this.bonuses;
    }

    public void setCanRob(boolean canRob) {
        this.canRob = canRob;
    }

    public boolean getCanRob() {
        return this.canRob;
    }
}
