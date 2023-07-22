package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;

public class SlashCommandContext {
    public final SlashCommandCreateEvent event;
    private final InteractionOriginalResponseUpdater originalResponseUpdater;

    public SlashCommandContext(SlashCommandCreateEvent event, InteractionOriginalResponseUpdater originalResponseUpdater) {
        this.event = event;
        this.originalResponseUpdater = originalResponseUpdater;
    }

    public void respond(String string) {
        this.originalResponseUpdater.setContent(string).update();
    }
}
