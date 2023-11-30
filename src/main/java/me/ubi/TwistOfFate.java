package me.ubi;


import io.github.cdimascio.dotenv.Dotenv;
import me.ubi.handler.SlashCommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class TwistOfFate {

    public static void main(String[] args) throws InterruptedException {
        Dotenv config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        JDA jda = JDABuilder.createLight(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MEMBERS)
                .build();

        jda.awaitReady().addEventListener(new SlashCommandHandler(jda, jda.getGuildById("721880018673860608")));

    }
}
