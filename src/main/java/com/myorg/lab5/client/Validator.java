package com.myorg.lab5.client;

import java.util.Set;

public class Validator {
    private static final Set<String> commands = Set.of(
        "help", "info", "show", "add", "update", "remove_by_id",
        "clear", "add_if_min", "remove_greater", "remove_lower",
        "count_by_studio", "filter_less_than_number_of_participants",
        "print_descending", "execute_script");
    
    private static final Set<String> numericCommands = Set.of(
        "remove_by_id", "filter_less_than_number_of_participants"
    );

    public boolean isValidCommand(String commandName){
        return commands.contains(commandName);
    }

    public boolean isValidNumCommand(String commandName){
        return numericCommands.contains(commandName);
    }

    public boolean validateExecuteScriptArgs(String args) {
        if (args == null || args.isEmpty()) return false;
        return args.endsWith(".txt") || args.contains("/") || args.contains("\\");
    }

    public boolean validNumericalArg(String args){
        if (args == null || args.isEmpty()) {
            return false;
        }
        try{
            Integer.parseInt(args);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
