package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import java.util.HashMap;

public class SlashCommandGroup {
    public final String name;
    public final String description;
    public final HashMap<String,SlashCommandRunner<?>> runners = new HashMap<>();

    public SlashCommandGroup(String name, String description){
        this.name = name;
        this.description = description;
    }
}
