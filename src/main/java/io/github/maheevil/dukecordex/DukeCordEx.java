package io.github.maheevil.dukecordex;

import io.github.maheevil.dukecordex.commandhandler.ChatCommandContainer;
import io.github.maheevil.dukecordex.commandhandler.CommandHandler;
import io.github.maheevil.dukecordex.test.BanCommand;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

public class DukeCordEx{
    public static HashMap<String, ChatCommandContainer<?>> CommandMap = new HashMap<>();
    public static void main(String[] args){
        Predicate<String> stringPredicate = x -> true;
        DiscordApi discordApi = new DiscordApiBuilder()
                .setToken(args[0])
                .login().join();
       System.out.println("done");
       init(discordApi);
    }

    public static void init(DiscordApi discordApi){
        discordApi.addMessageCreateListener(CommandHandler::onMessageCreate);
        new BanCommand();
    }
    //TODO - Make the chat commands also viable in non-guild environments?
}
