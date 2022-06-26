package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.commandhandler.Extension;
import java.util.HashMap;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
