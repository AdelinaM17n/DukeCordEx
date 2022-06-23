package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class SlashCommandEx {
    public final String baseName;
    public final String description;
    public HashMap<String,SlashCommandGroup> slashCommandGroups = new HashMap<>();
    public HashMap<String,SlashCommandRunner<?>> baseBranchingCommands = new HashMap<>();

    public SlashCommandEx(String baseName, String description){
        this.baseName = baseName;
        this.description = description;
    }

    public <T>void addBaseBrancingRunner(String name,Class<T> argClass ,BiConsumer<T,SlashCommandContext> consumer){
        var instance = new SlashCommandRunner<T>();
        instance.argsClass = argClass;
        instance.biConsumer = consumer;
        baseBranchingCommands.put(name,instance);
    }
}
