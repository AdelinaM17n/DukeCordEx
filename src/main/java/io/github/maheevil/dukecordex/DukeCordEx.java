package io.github.maheevil.dukecordex;

import io.github.maheevil.dukecordex.commandhandler.ChatCommandContainer;
import io.github.maheevil.dukecordex.commandhandler.CommandHandler;
import io.github.maheevil.dukecordex.commandhandler.Extension;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandEx;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandHandler;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandRegisterer;
import io.github.maheevil.dukecordex.test.BanCommand;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import java.util.Arrays;
import java.util.HashMap;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
