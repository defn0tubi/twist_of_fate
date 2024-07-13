package me.ubi.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Slash {
    void onSlashCommandEvent(SlashCommandInteractionEvent event) throws InterruptedException, ExecutionException, TimeoutException;

    String getName();

    String getDescription();

    boolean isSpecificGuild();

    default List<OptionData> getOptions() {
        return List.of();
    }

    default CommandData getCommandData() {
        CommandDataImpl commandData = new CommandDataImpl(getName(), getDescription());

        for (OptionData option : getOptions()) {
            commandData.addOptions(option);
        }
        return commandData;
    }

}
