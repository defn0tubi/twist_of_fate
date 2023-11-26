package me.ubi.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public interface Slash {
    void onSlashCommandEvent(SlashCommandInteractionEvent event);

    String getName();

    String getDescription();

    boolean isSpecificGuild();

    default CommandData getCommandData() {
        return new CommandDataImpl(getName(), getDescription());
    }
}
