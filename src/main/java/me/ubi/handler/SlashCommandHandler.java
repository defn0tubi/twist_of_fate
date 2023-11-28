package me.ubi.handler;

import io.github.classgraph.ClassGraph;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SlashCommandHandler extends ListenerAdapter {
    private static final Map<String, Slash> slashMap = new HashMap<>();
    private final CommandListUpdateAction globalCommandsData;
    private final CommandListUpdateAction guildCommandsData;

    public SlashCommandHandler(JDA jda, Guild guild) {
        this.globalCommandsData = jda.updateCommands();
        this.guildCommandsData = guild.updateCommands();

        ClassGraph classGraph = new ClassGraph();
        List<Slash> slashes = new ArrayList<>();

        classGraph.enableClassInfo()
                .scan()
                .getClassesImplementing(Slash.class)
                .forEach(classInfo -> {
                    try {
                        Slash slash = (Slash) classInfo.loadClass().getDeclaredConstructor().newInstance();
                        slashes.add(slash);
                    } catch (RuntimeException | NoSuchMethodException | InvocationTargetException |
                             InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException("Unable to load commands - " + e);
                    }
                });

        registerSlashCommands(slashes);
    }

    private void registerSlashCommand(Slash slash) {
        slashMap.put(slash.getName(), slash);

        if (slash.isSpecificGuild()) {
            guildCommandsData.addCommands(slash.getCommandData()).queue();
        } else {
            globalCommandsData.addCommands(slash.getCommandData()).queue();
        }
    }

    public void registerSlashCommands(List<Slash> slashList) {
        slashList.forEach(this::registerSlashCommand);

        queueCommands();
    }

    private void queueCommands() {
        globalCommandsData.queue();
        guildCommandsData.queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Slash slash = slashMap.get(event.getName());
        try {
            slash.onSlashCommandEvent(event);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
