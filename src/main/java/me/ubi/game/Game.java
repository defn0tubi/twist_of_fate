package me.ubi.game;

import me.ubi.util.Dice;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {

    private static Set<TextChannel> channelsInQueue = new HashSet<>();
    private static Map<TextChannel, Game> activeChannels = new HashMap<>();
    private TextChannel channel;
    private final List<Player> players;
    private Event currentEvent;
    private ScheduledExecutorService executorService;
    private static int totalRounds = Integer.parseInt(GameSettings.TOTAL_ROUNDS.getValue());
    private int round;

    public Game(List<Player> players, TextChannel channel) {
        activeChannels.put(channel, this);
        removeChannelFromQueue(channel);
        this.players = players;
        this.round = 0;
        this.channel = channel;
    }

    public void startTurn(Game game) {
        try {
            int turnTime = Integer.parseInt(GameSettings.TURN_TIME.getValue());
            executorService = Executors.newSingleThreadScheduledExecutor();
            game.round += 1;

            if (Boolean.parseBoolean(GameSettings.RANDOM_EVENTS_ENABLED.getValue())) {
                //Event randomEvent = EventController.getRandomEvent();
                this.currentEvent = Event.DOUBLE_DICE;
            }

            Map<Player, String> rollData = game.calculateRoundStartPoints(game);
            StringBuilder rollDataString;
            if (game.round == totalRounds) {
                rollDataString = new StringBuilder(String.format("**РАУНД %d/%d | Текущий ивент: %s**\n**ФИНАЛЬНЫЙ РАУНД**\n\n", game.getRound(), totalRounds, this.getCurrentEvent().getName()));
            } else {
                rollDataString = new StringBuilder(String.format("**РАУНД %d/%d | Текущий ивент: %s**\n\n", game.getRound(), totalRounds, this.getCurrentEvent().getName()));
            }

            rollDataString.append("Банк (Floating | Bank)\n\n");

            for (Player player : players) {
                rollDataString.append("`").append(player.getName()).append(": ").append(player.getFloatingPoints()).append(" | ").append(player.getPoints()).append("`\n");

            }

            rollDataString.append("\nБроски к8 на начало этого раунда: \n\n");

            for (var entry : rollData.entrySet()) {
                Player player = entry.getKey();
                String data = entry.getValue();
                rollDataString.append("`").append(player.getName()).append(": ").append(data).append("`\n");

            }
            rollDataString.append("\nТеперь вы можете начать грабить друг друга! У вас есть 15 секунд.\nИспользуйте `/rob @user amount` для грабежа.")
                    .append("\n** **");
            channel.sendMessage(rollDataString).queue();
            game.resetCanRob();

            executorService.schedule(() -> endTurn(game), turnTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void endTurn(Game game) {
        executorService.shutdown();

        for (Player player : players) {
            player.addPoints(player.getFloatingPoints());
            player.addFloatingPoints(-player.getFloatingPoints());
        }

        if (game.getRound() == totalRounds) {
            StringBuilder endString = new StringBuilder("**КОНЕЦ ИГРЫ!**");
            int maxPoints = 0;
            Player winner = null;

            for (Player player : game.players) {
                int playerPoints = player.getPoints();
                if (playerPoints > maxPoints) {
                    winner = player;
                    maxPoints = playerPoints;
                }
            }
            endString.append("\n\nПоздравим **").append(winner.getName())
                    .append("**, кто благодаря чистейшему скиллу находится на 1 месте с **")
                    .append(winner.getPoints()).append(" очками**!");

            channel.sendMessage(endString).queue();
            activeChannels.remove(game.channel);
        } else {
            startTurn(game);
        }
    }

    public Map<Player, String> calculateRoundStartPoints(Game game) {
        Map<Player, String> rollData = new HashMap<>();

        for (Player player : players) {
            ArrayList<Integer> rollResult = Dice.roll(8, game);
            StringBuilder data = new StringBuilder();
            int roll = rollResult.get(rollResult.size() - 1);
            player.addFloatingPoints(roll);
            if (game.getCurrentEvent() == Event.DOUBLE_DICE) {
                data.append(Dice.getRollString(rollResult));
            } else {
                data.append(rollResult.get(0));
            }
            if (roll == 8) {
                int bonusRoll = Dice.roll(8);
                player.addFloatingPoints(bonusRoll);
                data.append(" + ").append(bonusRoll);
            }

            rollData.put(player, String.valueOf(data));
        }

        return rollData;
    }

    public int getRounds() {
        return totalRounds;
    }

    public int getRound() {
        return this.round;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public static Set<TextChannel> getChannelsInQueue() {
        return channelsInQueue;
    }

    public static void addChannelToQueue(TextChannel channel) {
        channelsInQueue.add(channel);
    }

    public static void removeChannelFromQueue(TextChannel channel) {
        channelsInQueue.remove(channel);
    }

    public static Map<TextChannel, Game> getActiveChannels() {
        return activeChannels;
    }

    public void resetCanRob() {
        for (Player player : this.players) {
            player.setCanRob(true);
        }
    }

    public Event getCurrentEvent() {
        return this.currentEvent;
    }

}
