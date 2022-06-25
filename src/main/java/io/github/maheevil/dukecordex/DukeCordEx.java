package io.github.maheevil.dukecordex;

import io.github.maheevil.dukecordex.commandhandler.ChatCommandContainer;
import io.github.maheevil.dukecordex.commandhandler.CommandHandler;
import io.github.maheevil.dukecordex.commandhandler.Extension;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandHandler;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandRegisterer;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandEx;
import io.github.maheevil.dukecordex.test.BanCommand;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

public class DukeCordEx{
    public static HashMap<String, ChatCommandContainer<?>> CommandMap = new HashMap<>();
    public static HashMap<String, SlashCommandEx> SlashCommandMap = new HashMap<>();
    public static void main(String[] args){
        DiscordApi discordApi = new DiscordApiBuilder()
                .setToken(args[0])
                .login().join();
        init(
                discordApi,
                new BanCommand()
        );

    }

    public static void init(DiscordApi discordApi, Extension... extensions){
        discordApi.addMessageCreateListener(CommandHandler::onMessageCreate);
        SlashCommandRegisterer.pushAllSlashCommands(SlashCommandMap.values().stream().toList(),discordApi);
        discordApi.addSlashCommandCreateListener(SlashCommandHandler::handleSlashCommandEvent);
        Arrays.stream(extensions).toList().forEach(
                extension -> System.out.println(extension.getClass().getSimpleName() + " extension loaded!")
        );
    }
    //TODO - Make the chat commands also viable in non-guild environments?
}
