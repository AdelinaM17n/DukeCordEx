package io.github.maheevil.dukecordex.test;

import io.github.maheevil.dukecordex.commandhandler.ChatCommandEntry;
import io.github.maheevil.dukecordex.commandhandler.Extension;
import io.github.maheevil.dukecordex.commandhandler.annotations.*;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.function.BiConsumer;

public class BanCommand extends Extension {

    ChatCommandEntry banCommandEntry = registerChatCommand(
            "ban",
            BanCommandArgs.class,
            new PermissionType[]{PermissionType.BAN_MEMBERS},
            (banCommandArgs, messageCreateEvent) -> {
                messageCreateEvent.getMessage().reply("re").join();
            }
    );

    Object command = registerBasicSlashCommand(
            "ban",
            "ban",
            BaneCommandArgs.class,
            (x,v) -> {}
            );

    @ArgInnerClass
    public class BanCommandArgs {
        @ArgField(type = ArgParseType.NORMAL, index = 0)
        public User targetArg;
        @ArgField(type = ArgParseType.STRING_COALESCING, index = 1, optional = true)
        public String reasonArg;
    }

    @ArgInnerClass
    public class BaneCommandArgs {
        @SlashCommandArgField(type = SlashCommandOptionType.USER)
        public User targetArg;
        @SlashCommandArgField(type = SlashCommandOptionType.STRING)
        public String reasonArg;
    }
}
