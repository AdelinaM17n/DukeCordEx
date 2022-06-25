package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.commandhandler.Extension;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlashCommandEx {
    public final String baseName;
    public final String description;
    public final String guildOrNotId;
    public final Extension extensionInstance;
    public final HashMap<String,SlashCommandGroup> slashCommandGroups = new HashMap<>();
    public final HashMap<String,SlashCommandRunner<?>> baseBranchingCommands = new HashMap<>();

    public SlashCommandEx(String baseName, String description,String guildOrNotId, Extension extensionInstance){
        this.baseName = baseName;
        this.description = description;
        this.guildOrNotId = guildOrNotId;
        this.extensionInstance = extensionInstance;
    }
}
