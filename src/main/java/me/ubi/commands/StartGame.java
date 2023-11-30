package me.ubi.commands;

import me.ubi.game.Game;
import me.ubi.game.GameSettings;
import me.ubi.game.Player;
import me.ubi.handler.Slash;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class StartGame implements Slash {

    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {

        int queueTime = Integer.parseInt(GameSettings.QUEUE_TIME.getValue());
        TextChannel channel = event.getChannel().asTextChannel();

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("Начать новую игру может только администратор").setEphemeral(true).queue();
            return;
        }

        if (Game.getActiveChannels().containsKey(channel) || Game.getChannelsInQueue().contains(channel)) {
            event.reply("В этом канале уже идет игра").setEphemeral(true).queue();
            return;
        }

        Game.addChannelToQueue(channel);
        event.reply("Начат поиск игроков для новой игры").setEphemeral(true).queue();

        Emoji emoji = Emoji.fromUnicode("✅");
        final Message[] queue = new Message[1];
        channel.sendMessage("Нажмите на ✅ чтобы присоединиться к новой игре.\nУ вас есть 15 секунд.").queue(
                message -> {
                    queue[0] = message;
                    queue[0].addReaction(emoji).queue();
                }
        );

        CompletableFuture<List<User>> future = new CompletableFuture<>();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> channel.getHistory().retrievePast(10).queue(history -> {
            for (Message message : history) {
                if (message.getId().equals(queue[0].getId())) {
                    message.getReactions().get(0).retrieveUsers().queue(users -> {
                        List<User> nonBotUsers = users.stream()
                                .filter(user -> !user.isBot())
                                .collect(Collectors.toList());
                        future.complete(nonBotUsers);
                    });
                }
            }
        }), queueTime, TimeUnit.SECONDS);

        future.thenAccept(usersToJoin -> {
            List<Player> players = new ArrayList<>();
            if (usersToJoin.isEmpty()) {
                channel.sendMessage("Никто не присоединился к игре.").queue();
                Game.removeChannelFromQueue(channel);
            } else {
                StringBuilder userString = new StringBuilder();
                for (User user : usersToJoin) {
                    Player player = new Player(user.getName(), 0, 0, null);
                    players.add(player);
                    String name = user.getName();
                    userString.append(name);
                }
                channel.sendMessage("Началась новая игра со следующими участниками: " + userString).queue();
                Game game = new Game(players, event.getChannel().asTextChannel());
                game.startTurn(game);
            }
        });
    }

    @Override
    public String getName() {
        return "startgame";
    }

    @Override
    public String getDescription() {
        return "Начать новую игру. Только для администраторов сервера";
    }

    @Override
    public boolean isSpecificGuild() {
        return true;
    }
}
