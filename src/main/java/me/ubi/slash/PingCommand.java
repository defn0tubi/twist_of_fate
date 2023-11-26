package me.ubi.slash;

import me.ubi.handler.Slash;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand implements Slash {
    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        event.reply("Pong").queue();
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Pong";
    }

    @Override
    public boolean isSpecificGuild() {
        return false;
    }
}
