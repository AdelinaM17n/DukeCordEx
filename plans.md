# Improving Slash Command
- PERMISSIONS
- AutoComplete
- (Unrealistic Idea) Kordex style checks
- Some abstraction for editing slash commands **OUTSIDE OF COMMAND REGISTRATION**
- Localization
- Age Restricted Commands

**Abstraction changes**
- CommandInfo DSL style method when registering commands
```javascript
public Object ee = registerBasicSlashCommand(
            "test",
            "Testing command",
            commandProperties(
                    ReplyType.Constant,  
                    globalCommand(),
                    AgeRestriction.NONE,
            ),
            CommandArgs.class, 
            (x , y) -> {
                System.out.println(string);
            }
    );
```

# Context Commands
- The name speaks for itself, just like chess

# Universal Commands
- Basically commands that are both chat and slash commands  

**Plans**
- Offer abstractions to things like replying
    - Three? types planned, `reply(string)`, `reply(chatString, slashString)`, and perhaps `reply(chatConsumer, slashConsumer)`
    - Also dedicated `replyChat()` and `replySlash()`
- No new registration system, reuse the normal chat command and slash command registries obviously

# Chat command groupings
- Slash command style groups for chat commands

# Better Error Handling + Exception Handling
- Give actually meaningful error messages with more context
- Disallow users from creating two guild commands in the same guild with the same name **AND ALLOW** two guild commands in different guilds to have the same name