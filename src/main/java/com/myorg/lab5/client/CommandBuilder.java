package com.myorg.lab5.client;

import com.myorg.lab5.CommandRequest;
import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.MusicBand;

public class CommandBuilder {
    private final ConsoleManager consoleManager;
    private final Validator validator;

    public CommandBuilder(ConsoleManager consoleManager, Validator validator){
        this.consoleManager = consoleManager;
        this.validator = validator;
    }

    public CommandRequest build(String input){
        if (input == null || input.isEmpty()){
            return null;
        }

        String[] parts = input.trim().split("\\s+", 2);
        String commandName = parts[0];
        String argsString = parts.length > 1 ? parts[1] : "";

        if(!validator.isValidCommand(commandName)){
            consoleManager.show("Неизвестная команда: " + commandName);
            return null;
        }

        if (commandName.equals("exit")){
            return null;
        }

        if (commandName.equals("add") || 
            commandName.equals("remove_greater") || 
            commandName.equals("remove_lower")||
            commandName.equals("add_if_min")){

            consoleManager.show("Создание нового элемента\n");
            MusicBand band = consoleManager.parse();
            if (band == null){
                consoleManager.show("Ошибка при создании обьекта");
                return null;
            }
            return new CommandRequest(commandName, band);

        }

        if (commandName.equals("execute_script")) {
            if (!validator.validateExecuteScriptArgs(argsString)) {
                consoleManager.show("Ошибка: укажите путь к скрипту (.txt)");
                return null;
            }
            return new CommandRequest(commandName, argsString);
        }

        if (commandName.equals("update")){
            consoleManager.show("Обновление элемента\n");
            int id = Integer.parseInt(argsString);
            MusicBand band = consoleManager.parse();
            if(band == null){
                consoleManager.show("Ошибка при создании обьекта");
                return null;
            }

            return new CommandRequest(commandName, id);
        }


        if (validator.isValidNumCommand(commandName)){
            if (!validator.validNumericalArg(argsString)){
                consoleManager.show("Ошибка: требуется число");
                return null;
            }
            return new CommandRequest(commandName, argsString);
        }



        if (argsString.isEmpty()) {
            return new CommandRequest(commandName);
        } else {
            return new CommandRequest(commandName, argsString);
        }
    }
}
