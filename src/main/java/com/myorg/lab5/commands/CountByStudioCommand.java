package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.Studio;

/**
 * Команда подсчета элементов с указанной студией.
 * Подсчитывает количество групп, у которых поле studio совпадает с заданным.
 */
public class CountByStudioCommand implements Command {
    private CollectionManager collectionManager;
    private final ConsoleManager consoleManager;

    public CountByStudioCommand(CollectionManager collectionManager, ConsoleManager consoleManager){
        this.collectionManager = collectionManager;
        this.consoleManager = consoleManager;
    }

    /**
     * Выполняет подсчет элементов с указанной студией.
     * 
     * @param args массив аргументов, где args[0] - название студии (опционально)
     */
    @Override
    public void execute(String args[]){
        consoleManager.show(String.valueOf(collectionManager.countByStudio(new Studio(args[0]))));
    }

    @Override
    public String getDescription(){
        return "- Count elements with the specified studio";
    }
}
