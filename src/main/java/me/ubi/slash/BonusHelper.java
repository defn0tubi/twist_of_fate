package me.ubi.slash;

import me.ubi.game.Bonus;
import me.ubi.handler.Slash;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BonusHelper implements Slash {
    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        // Yes i just copied EventHelper mad?
        User user = event.getUser();
        List<String> helpMessage = new ArrayList<>();
        Bonus[] events = Bonus.class.getEnumConstants();
        for (Bonus e : events) {
            helpMessage.add("**" + e.getName() + "** " + e.getDescription() + "\nЦена: " + e.getCost() + "\n\n");
        }

        ListIterator<String> iterator = helpMessage.listIterator();
        StringBuilder messageToSend = new StringBuilder();

        while (iterator.hasNext()) {
            String message = iterator.next();
            if (messageToSend.length() + message.length() < 2000) {
                messageToSend.append(message);
                iterator.remove();
            } else {
                String finalMessageToSend = messageToSend.toString();
                user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(finalMessageToSend).queue());
                messageToSend = new StringBuilder();
                messageToSend.append(message);
            }
        }

        if (!messageToSend.isEmpty()) {
            String leftOverMessage = messageToSend.toString();
            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(leftOverMessage).queue());
        }

        event.reply("Список бонусов был отправлен вам в личные сообщения").queue();
    }

    @Override
    public String getName() {
        return "bonuses";
    }

    @Override
    public String getDescription() {
        return "Бонусы и их описание";
    }

    @Override
    public boolean isSpecificGuild() {
        return true;
    }
}
