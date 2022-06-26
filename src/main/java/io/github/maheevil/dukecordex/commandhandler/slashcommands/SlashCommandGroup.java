package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import java.util.HashMap;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
public class SlashCommandGroup {
    public final String name;
    public final String description;
    public final HashMap<String,SlashCommandRunner<?>> runners = new HashMap<>();

    public SlashCommandGroup(String name, String description){
        this.name = name;
        this.description = description;
    }
}
