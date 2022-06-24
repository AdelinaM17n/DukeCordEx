package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlashCommandEx {
    public final String baseName;
    public final String description;
    public HashMap<String,SlashCommandGroup> slashCommandGroups = new HashMap<>();
    public HashMap<String,SlashCommandRunner<?>> baseBranchingCommands = new HashMap<>();

    public SlashCommandEx(String baseName, String description){
        this.baseName = baseName;
        this.description = description;
    }

    /*public <T>void addBaseBrancingRunner(String name,String description,Class<T> argClass ,BiConsumer<T, SlashCommandCreateEvent> consumer){
        var instance = new SlashCommandRunner<T>(
                name,
                description,
                argClass,
                consumer
        );
        baseBranchingCommands.put(name,instance);
    }
    public <T>void addBaseBrancingRunner(String name, String description, Class<T> argClass , Consumer<SlashCommandCreateEvent> consumer){
        var instance = new SlashCommandRunner<T>(
                name,
                description,
                consumer
        );
        baseBranchingCommands.put(name,instance);
    }*/
}
