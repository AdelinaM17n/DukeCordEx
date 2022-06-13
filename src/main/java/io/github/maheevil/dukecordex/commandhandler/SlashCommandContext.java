package io.github.maheevil.dukecordex.commandhandler;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class SlashCommandContext{
    public DiscordApi api;
    public Server guild;
    public User commandUser;
    private Object args;

    public void SlashCommandContainer(DiscordApi api, Server guild, User commandUser, Object args){
        this.api = api;
        this.guild = guild;
        this.commandUser = commandUser;
        this.args = args;
    }

    public <T> T getArgs(Class<T> type){
        return this.args.getClass() == type ? type.cast(args) : null;
        // TODO - Someway to error?
    }
}
