package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.Studio;

/**
 * Команда подсчета элементов с указанной студией.
 * Подсчитывает количество групп, у которых поле studio совпадает с заданным.
 */
public class CountByStudioCommand implements Command {
    private CollectionManager collectionManager;

    public CountByStudioCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет подсчет элементов с указанной студией.
     * 
     * @param args массив аргументов, где args[0] - название студии (опционально)
     */
    @Override
    public void execute(String args[]){
        System.out.println(String.valueOf(collectionManager.countByStudio(new Studio(args[0]))));
    }

    @Override
    public String getDescription(){
        return "- Count elements with the specified studio";
    }
}
