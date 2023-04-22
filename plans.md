## Improving Slash Command
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

## Context Commands
- The name speaks for itself, just like chess

## Universal Commands
- Basically commands that are both chat and slash commands  

**Plans**
- Offer abstractions to things like replying
    - Three? types planned, `reply(string)`, `reply(chatString, slashString)`, and perhaps `reply(chatConsumer, slashConsumer)`
    - Also dedicated `replyChat()` and `replySlash()`
- No new registration system, reuse the normal chat command and slash command registries obviously

## Chat command groupings
- Slash command style groups for chat commands

## Better Error Handling + Exception Handling
- Give actually meaningful error messages with more context
- Disallow users from creating two guild commands in the same guild with the same name **AND ALLOW** two guild commands in different guilds to have the same name

## Remove Artificial Limitations
- NO MORE RETURNING NULL FOR COMMAND REGISTRATION
- Allow non inner-class or static inner classes(/records) to be used as arg classes 
- Get. A. Brain.

## Modals
- Offer Modals as arguments, aka just processing the modal into a class 
- ~~***Hashmaps aren't suitable because separate registration of the modal, defining a class kills two birds with one stone***~~
- Offer both hashmaps and classes, hashmaps only the general abstraction over modals, you have to use a class for modals as args
- make use of manual annotation instance creation for hashmap modal registration
- General abstraction over modals where you can create a modal plus also pass on action lambda to run when it is submitted

# Fuck it mode
- ASM to inject modal abstraction to java code
- or mixins (would have to figure out something like loom interface injection)