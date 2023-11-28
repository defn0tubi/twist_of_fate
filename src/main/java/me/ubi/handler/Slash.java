package me.ubi.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Slash {
    void onSlashCommandEvent(SlashCommandInteractionEvent event) throws InterruptedException, ExecutionException, TimeoutException;

    String getName();

    String getDescription();

    boolean isSpecificGuild();

    default CommandData getCommandData() {
        return new CommandDataImpl(getName(), getDescription());
    }
}
