package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

/**
 * Команда вывода информации о коллекции.
 * Показывает тип коллекции, дату инициализации и количество элементов.
 */
public class InfoCommand implements Command{
    private CollectionManager collectionManager;
    private final ConsoleManager consoleManager;

    /**
     * Создает команду info.
     * 
     * @param collectionManager менеджер коллекции для получения информации
     * @param console консольный менеджер для вывода
     */
    public InfoCommand(CollectionManager collectionManager, ConsoleManager consoleManager){
        this.collectionManager = collectionManager;
        this.consoleManager = consoleManager;
    }

    @Override
    public void execute(String args[]){
        consoleManager.show(collectionManager.toString());
    }

    @Override
    public String getDescription(){
        return "- Show collection information (type, initialization date, size)";
    }
}