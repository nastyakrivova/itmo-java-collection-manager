package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;

public class ExitCommand implements Command{
    private final ConsoleManager consoleManager;

    public ExitCommand(ConsoleManager consoleManager){
        this.consoleManager = consoleManager;
    }

    /**
     * Выполняет команду завершения программы.
     * Выводит сообщение и вызывает {@link System#exit(int)}.
     */
    @Override
    public void execute(String[] args, int userId) {
        consoleManager.show("Exiting...");
        System.exit(0);
    }

    /**
     * Возвращает краткое описание команды.
     *
     * @return строка с описанием назначения команды {@code exit}
     */
    @Override
    public String getDescription() {
        return "- Exit";
    }
}
