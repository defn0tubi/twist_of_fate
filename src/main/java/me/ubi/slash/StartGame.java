package me.ubi.slash;

import me.ubi.handler.Slash;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class StartGame implements Slash {

    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR))
            event.reply("Начать новую игру может только администратор").setEphemeral(true).queue();

        event.reply("Начат поиск игроков для новой игры").setEphemeral(true).queue();

        TextChannel channel = event.getChannel().asTextChannel();
        Emoji emoji = Emoji.fromUnicode("✅");
        final Message[] queue = new Message[1];
        channel.sendMessage("Нажмите на ✅ чтобы присоединиться к новой игре.\nУ вас есть 15 секунд.").queue(
                message -> {
                    queue[0] = message;
                    queue[0].addReaction(emoji).queue();
                }
        );

        // Have to update messages cache of JDA with channel.getHistory(), or else it will not see new reactions :skull:
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
        }), 15, TimeUnit.SECONDS);

        future.thenAccept(usersToJoin -> {
            if (usersToJoin.isEmpty()) {
                channel.sendMessage("Никто не присоединился к игре.").queue();
            } else {
                StringBuilder userString = new StringBuilder();
                for (User user : usersToJoin) {
                    String name = user.getName();
                    userString.append(name);
                }
                channel.sendMessage("Люди которые присоединятся к игре(" + usersToJoin.size() + "): " + userString).queue();
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
