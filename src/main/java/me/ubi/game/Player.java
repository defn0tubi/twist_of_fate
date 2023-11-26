package me.ubi.game;

import java.util.List;

public interface Player {
    String getName();

    int getPoints();

    int getFloatingPoints();

    List<Bonus> getBonuses();

}
