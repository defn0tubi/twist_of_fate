package me.ubi.game;

import java.util.Random;

public class EventController {

    public static Event getRandomEvent() {
        Event[] events = Event.values();
        Random random = new Random();
        int randomIndex = random.nextInt(events.length);

        return events[randomIndex];
    }

}
