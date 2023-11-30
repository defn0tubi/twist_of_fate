package me.ubi.game;

public enum GameSettings {

    // Time in seconds after the game starts when using "/startgame"
    QUEUE_TIME("15"),

    // Time in seconds for each turn
    TURN_TIME("15"),

    // How many rounds in a game
    TOTAL_ROUNDS("2"),

    // % Chance for a random event to happen IF RANDOM_EVENTS_ENABLED is set to true
    EVENT_CHANCE("5"),

    // If set to true, random events will happen with a RANDOM_EVENT_CHANCE chance
    RANDOM_EVENTS_ENABLED("false");

    private String value;

    GameSettings(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
