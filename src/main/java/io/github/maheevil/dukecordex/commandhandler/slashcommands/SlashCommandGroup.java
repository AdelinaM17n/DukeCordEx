package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import java.util.HashMap;

public class SlashCommandGroup {
    public String name;
    public String description;
    public HashMap<String,SlashCommandRunner<?>> runners = new HashMap<>();

    public SlashCommandGroup(String name, String description){
        this.name = name;
        this.description = description;
    }
}
