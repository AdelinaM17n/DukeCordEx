package io.github.maheevil.dukecordex;

import io.github.maheevil.dukecordex.commandhandler.ChatCommandContainer;
import io.github.maheevil.dukecordex.commandhandler.CommandHandler;
import io.github.maheevil.dukecordex.commandhandler.Extension;
import io.github.maheevil.dukecordex.commandhandler.modals.ModalHandler;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandEx;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandHandler;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandRegisterer;
import org.javacord.api.DiscordApi;
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
    protected static String config = "GLOBAL";

    public static void init(DiscordApi discordApi, Extension... extensions){
        discordApi.addMessageCreateListener(CommandHandler::onMessageCreate);
        discordApi.addModalSubmitListener(ModalHandler::modalListener);
        SlashCommandRegisterer.pushAllSlashCommands(SlashCommandMap.values().stream().toList(),discordApi);
        discordApi.addSlashCommandCreateListener(SlashCommandHandler::handleSlashCommandEvent);
        Arrays.stream(extensions).toList().forEach(
                extension -> System.out.println(extension.getClass().getSimpleName() + " extension loaded!")
        );
    }

    public static void setConfig(String config) {
        if(config.length() != 18 && !config.equals("GLOBAL")){
            System.err.println("Invalid entry");
            return;
        }
        DukeCordEx.config = config;
    }

    public static String getConfig(){
        return config;
    }
}
