package com.myorg.lab5.server;

import com.myorg.lab5.CommandRequest;
import com.myorg.lab5.CommandResponse;
import com.myorg.lab5.commands.CommandManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;

public class CommandExecutor {
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;

    public CommandExecutor(CommandManager commandManager, CollectionManager collectionManager){
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public CommandResponse execute(CommandRequest request){
        String cmmandName = request.getCommandName();
        Object[] args = request.getArgs();

        String[] stringArgs = toStringArgs(args);
        String result = commandManager.executeAndGetResult(cmmandName, stringArgs);

        if(result == null) {
            return CommandResponse.error(result);
        }
        if(result.isEmpty()) {
            return CommandResponse.success("Команда выполнена");
        }
        return CommandResponse.success(result);
        
    }

    private String[] toStringArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return new String[0];
        }
        String[] result = new String[args.length];
        for(int i = 0; i < args.length; i++) {
            if (args[i] instanceof MusicBand) {
                ScriptParser parser = new ScriptParser();
                result[i] = parser.toCsv((MusicBand) args[i]);
            } else if (args[i] != null) {
                result[i] = args[i].toString();
            } else {
                result[i] = "";
            }
        }
        return result;
    }
}
