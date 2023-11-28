package me.ubi.util;

import java.util.Random;

public class Dice {
    private int sides;

    public Dice(int sides) {
        this.sides = sides;
    }

    public static int roll(int sides) {
        Random rng = new Random();
        return rng.nextInt(1, sides + 1);
    }
}
