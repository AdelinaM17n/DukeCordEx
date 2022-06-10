package io.github.maheevil.dukecordex;

import io.github.maheevil.dukecordex.commandhandler.ChatCommandContainer;
import io.github.maheevil.dukecordex.commandhandler.CommandHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.HashMap;

public class DukeCordEx {
    public static HashMap<String, ChatCommandContainer> CommandMap = new HashMap<>();
    public static void main(String[]args){
        /*DiscordApi discordApi = new DiscordApiBuilder()
                .setToken("I am that dumb")
                .login().join();*/
        System.out.println("done");
    }

    public void init(DiscordApi discordApi){
        discordApi.addMessageCreateListener(CommandHandler::onMessageCreate);
    }
}
