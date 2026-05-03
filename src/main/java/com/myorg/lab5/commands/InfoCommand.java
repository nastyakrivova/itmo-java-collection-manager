package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;

/**
 * Команда вывода информации о коллекции.
 * Показывает тип коллекции, дату инициализации и количество элементов.
 */
public class InfoCommand implements Command{
    private CollectionManager collectionManager;
  

    /**
     * Создает команду info.
     * 
     * @param collectionManager менеджер коллекции для получения информации
     * @param console консольный менеджер для вывода
     */
    public InfoCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String args[], int userId){
        System.out.println(collectionManager.toString());
    }

    @Override
    public String getDescription(){
        return "- Show collection information (type, initialization date, size)";
    }
}