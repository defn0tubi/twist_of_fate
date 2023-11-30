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
    private List<Player> players;
    private ScheduledExecutorService executorService;
    private static int totalRounds = Integer.parseInt(GameSettings.TOTAL_ROUNDS.getValue());
    private int round;
    private boolean robPhase = false;

    public Game(List<Player> players, TextChannel channel) {
        activeChannels.put(channel, this);
        removeChannelFromQueue(channel);
        this.players = players;
        this.round = 0;
        this.channel = channel;
    }

    public void startTurn(Game game) {
        int turnTime = Integer.parseInt(GameSettings.TURN_TIME.getValue());
        executorService = Executors.newSingleThreadScheduledExecutor();
        game.round += 1;

        Map<Player, String> rollData = game.calculateRoundStartPoints();
        StringBuilder rollDataString;
        if (game.round == totalRounds) {
            rollDataString = new StringBuilder(String.format("**РАУНД %d/%d**\n**ФИНАЛЬНЫЙ РАУНД**\n\nБроски к8 на начало этого раунда: \n\n", game.getRound(), totalRounds));
        } else {
            rollDataString = new StringBuilder(String.format("**РАУНД %d/%d**\n\nБроски к8 на начало этого раунда: \n\n", game.getRound(), totalRounds));
        }

        for (var entry : rollData.entrySet()) {
            Player player = entry.getKey();
            String data = entry.getValue();

            rollDataString.append("`").append(player.getName()).append(": ").append(data).append("`");
        }
        rollDataString.append("\n\nТеперь вы можете начать грабить друг друга! У вас есть 15 секунд.")
                .append("\n** **");
        channel.sendMessage(rollDataString).queue();

        executorService.schedule(() -> endTurn(game), turnTime, TimeUnit.SECONDS);
    }

    public void endTurn(Game game) {
        executorService.shutdown();

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

    public Map<Player, String> calculateRoundStartPoints() {
        Map<Player, String> rollData = new HashMap<>();

        for (Player player : players) {
            int rollResult = Dice.roll(8);
            StringBuilder data = new StringBuilder();
            player.addPoints(rollResult);
            data.append(rollResult);
            if (rollResult == 8) {
                int bonusRoll = Dice.roll(8);
                player.addPoints(bonusRoll);
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
}
