package io.github.maheevil.dukecordex.test;

import io.github.maheevil.dukecordex.commandhandler.ChatCommandEntry;
import io.github.maheevil.dukecordex.commandhandler.Extension;
import io.github.maheevil.dukecordex.commandhandler.annotations.*;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandCreator;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.Locale;
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

    /*Object ttt = registerGroupedSlashCommand(
            "named",
            "grouped",
            "870341202652827648",
            SlashCommandCreator.createGroup(
                    "group",
                    "eee",
                    SlashCommandCreator.create(
                            "test", "test",
                            BaneCommandArgs.class,
                            (baneCommandArgs, slashCommandContext) -> {}
                    ),
                    SlashCommandCreator.create(
                            "teste", "test",
                            BaneCommandArgs.class,
                            (baneCommandArgs, slashCommandContext) -> {}
                    )
            ),
            SlashCommandCreator.createGroup(
                    "hroup",
                    "eee",
                    SlashCommandCreator.create(
                            "htest", "test",
                            BaneCommandArgs.class,
                            (baneCommandArgs, slashCommandContext) -> {}
                    ),
                    SlashCommandCreator.create(
                            "hteste", "test",
                            BaneCommandArgs.class,
                            (baneCommandArgs, slashCommandContext) -> {}
                    )
            )
    );

    Object cgg = registerBasicSlashCommand(
            "ea",
            "ea",
            "870341202652827648",
            SlashCommandCreator.create(
                    "test", "test",
                    BaneCommandArgs.class,
                    (baneCommandArgs, slashCommandContext) -> {}
            ),
            SlashCommandCreator.create(
                    "teste", "test",
                    BaneCommandArgs.class,
                    (baneCommandArgs, slashCommandContext) -> {}
            )
    );*/

    Object command = registerBasicSlashCommand(
            "ban",
            "ban",
            "870341202652827648",
            BaneCommandArgs.class,
            (x,v) -> {
                v.getSlashCommandInteraction().createImmediateResponder().append("Hello").respond().join();
                System.out.println("Yay" + x.reasonArg);
            }

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
        @SlashCommandArgField(type = SlashCommandOptionType.STRING, required = false)
        public String reasonArg;
    }
}
