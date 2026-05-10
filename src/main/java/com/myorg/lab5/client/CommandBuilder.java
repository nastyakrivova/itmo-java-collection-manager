package com.myorg.lab5.client;

import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.MusicBandParser;
import java.util.Scanner;

public class CommandBuilder {
    private final ConsoleManager consoleManager;
    private final Validator validator;
    private final MusicBandParser interactiveParser;
    private final MusicBandParser scriptParser;
    
    public CommandBuilder(ConsoleManager consoleManager, Validator validator, Scanner scanner) {
        this.consoleManager = consoleManager;
        this.validator = validator;
        this.interactiveParser = new MusicBandParser(scanner);
        this.scriptParser = new MusicBandParser();
    }

    public CommandRequest build(String input, String login, String password) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] parts = input.trim().split("\\s+", 2);
        String commandName = parts[0];
        String argsString = parts.length > 1 ? parts[1] : "";

        if (!validator.isValidCommand(commandName)) {
            consoleManager.show("Неизвестная команда: " + commandName);
            return null;
        }

        if (commandName.equals("exit")) {
            return null;
        }

        if (commandName.equals("add") ||
            commandName.equals("remove_greater") ||
            commandName.equals("remove_lower") ||
            commandName.equals("add_if_min")) {

            consoleManager.show("Создание нового элемента\n");
            MusicBand band = interactiveParser.parseInteractively();

            if (band == null) {
                consoleManager.show("Ошибка при создании объекта");
                return null;
            }

            String bandData = interactiveParser.toCsv(band);
            return new CommandRequest(commandName, new Object[]{bandData}, login, password);
        }

        if (commandName.equals("execute_script")) {
            if (!validator.validateExecuteScriptArgs(argsString)) {
                consoleManager.show("Ошибка: укажите путь к скрипту (.txt)");
                return null;
            }
            return new CommandRequest(commandName, new Object[]{argsString}, login, password);
        }

        if (commandName.equals("update")) {
            if (argsString == null || argsString.isEmpty()) {
                consoleManager.show("Error: Usage: update <id>");
                return null;
            }
            
            int id;
            try {
                id = Integer.parseInt(argsString);
            } catch (NumberFormatException e) {
                consoleManager.show("Error: ID must be a number");
                return null;
            }
            
            return new CommandRequest("check_update", new Object[]{id}, login, password);
        }

        if (validator.isValidNumCommand(commandName)) {
            if (!validator.validNumericalArg(argsString)) {
                consoleManager.show("Ошибка: требуется число");
                return null;
            }
            return new CommandRequest(commandName, new Object[]{argsString}, login, password);
        }

        if (argsString.isEmpty()) {
            return new CommandRequest(commandName, login, password);
        } else {
            return new CommandRequest(commandName, new Object[]{argsString}, login, password);
        }
    }
    
    public CommandRequest buildUpdateWithData(int id, String login, String password) {
        consoleManager.show("Обновление элемента (ID: " + id + ")\n");
        MusicBand band = interactiveParser.parseInteractively();
        if (band == null) {
            consoleManager.show("Ошибка при создании объекта");
            return null;
        }
        String bandData = interactiveParser.toCsv(band);
        return new CommandRequest("update", new Object[]{id, bandData}, login, password);
    }
}