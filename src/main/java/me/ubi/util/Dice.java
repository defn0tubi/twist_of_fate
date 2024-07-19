package me.ubi.util;

import me.ubi.game.Event;
import me.ubi.game.Game;

import java.util.ArrayList;
import java.util.Random;

public class Dice {
    private int sides;
    private Game game;

    private static final Random rng = new Random();

    public Dice(int sides, Game game) {
        this.sides = sides;
        this.game = game;
    }

    public static ArrayList<Integer> roll(int sides, Game game) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(rng.nextInt(1, sides + 1));

        if (game.getCurrentEvent() == Event.DOUBLE_DICE)
            result.add(rng.nextInt(1, sides + 1));

        return result;
    }

    public static int roll(int sides) {
        return rng.nextInt(1, sides + 1);
    }

    public static String getRollString(ArrayList<Integer> rolls) {
        return rolls.get(0).toString() + " -> " + rolls.get(1).toString();
    }
}
