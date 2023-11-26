package me.ubi.slash;

import me.ubi.game.Event;
import me.ubi.handler.Slash;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class EventHelper implements Slash {

    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        List<String> helpMessage = new ArrayList<>();
        Event[] events = Event.class.getEnumConstants();
        for (Event e : events) {
            helpMessage.add("**" + e.getName() + "** " + e.getDescription() + "\n\n");
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

        event.reply("Список ивентов был отправлен вам в личные сообщения").queue();
    }

    @Override
    public String getName() {
        return "events";
    }

    @Override
    public String getDescription() {
        return "Events and their description";
    }

    @Override
    public boolean isSpecificGuild() {
        return true;
    }
}
