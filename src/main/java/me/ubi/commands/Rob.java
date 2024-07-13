package me.ubi.commands;

import me.ubi.game.Game;
import me.ubi.game.Player;
import me.ubi.handler.Slash;
import me.ubi.util.Dice;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Rob implements Slash {
    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) throws InterruptedException, ExecutionException, TimeoutException {
        Game game = Game.getActiveChannels().get(event.getChannel().asTextChannel());

        if (game == null) {
            event.reply("Сейчас в этом канале не идёт игра").setEphemeral(true).queue();
        } else if (!game.getCanRob()) {
            event.reply("Сейчас грабить нельзя!").setEphemeral(true).queue();
        }

        List<Player> players = game.getPlayers();

        Member member = event.getOption("user", OptionMapping::getAsMember);
        int amount = event.getOption("amount", OptionMapping::getAsInt);
        Member selfMember = event.getMember();
        Player robber = null;

        // Get the robber
        for (Player player : players) {
            assert selfMember != null;
            if (player.getName().equals(selfMember.getUser().getName())) {
                robber = player;
            }
        }

        for (Player player : players) {
            assert member != null;
            if (player.getName().equals(member.getUser().getName())) {

                if (player.getFloatingPoints() < amount) {
                    event.reply(member.getUser().getName() + " слишком бедный, укажите меньшее количество очков")
                            .setEphemeral(true).queue();
                }

                event.reply("Вы пытаетесь ограбить " + member.getUser().getName() + "...").setEphemeral(true).queue();

                StringBuilder string = new StringBuilder(selfMember.getUser().getName());
                int roll = Dice.roll(8);

                if (roll < amount) {
                    string.append(", вы выбили ")
                            .append("`")
                            .append(roll)
                            .append("` ")
                            .append("очков и не смогли ограбить ")
                            .append(member.getUser().getName())
                            .append("!");
                } else {
                    player.addFloatingPoints(-amount);
                    assert robber != null;
                    robber.addFloatingPoints(amount);

                    string.append(", вы выбили ")
                            .append("`")
                            .append(roll)
                            .append("` ")
                            .append("очков и успешно грабите ")
                            .append(member.getUser().getName())
                            .append(" на `")
                            .append(amount)
                            .append("` очков")
                            .append("!");
                }
                event.getChannel().sendMessage(string).queue();
            } else {
                event.reply("Игрок не найден").setEphemeral(true).queue();
            }
        }

    }

    @Override
    public String getName() {
        return "rob";
    }

    @Override
    public String getDescription() {
        return "Ограбить игрока";
    }

    @Override
    public boolean isSpecificGuild() {
        return false;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.USER, "user", "Игрок, которого хотите ограбить", true),
                new OptionData(OptionType.INTEGER, "amount", "Количество очков, которое хотите забрать", true)
                        .setMinValue(1)
                        .setMaxValue(8)
        );
    }
}
