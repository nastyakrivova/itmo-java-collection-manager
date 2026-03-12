package com.myorg.lab5.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;

/**
 * Команда вывода элементов в порядке убывания.
 * Сортирует коллекцию в обратном порядке и выводит все элементы.
 */
public class PrintDescendingCommand implements Command{
    private CollectionManager collectionManager;
    private final ConsoleManager consoleManager;

    /**
     * Создает команду print_descending.
     * 
     * @param collectionManager менеджер коллекции для получения элементов
     * @param console консольный менеджер для вывода
     */
    public PrintDescendingCommand(CollectionManager collectionManager, ConsoleManager consoleManager){
        this.collectionManager = collectionManager;
        this.consoleManager = consoleManager;
    }

    @Override
    public void execute(String args[]){
        List<MusicBand> sortedList = new ArrayList<>(collectionManager.getList());
        sortedList.sort(Comparator.reverseOrder());

        for (MusicBand band : sortedList) {
            consoleManager.show(band.toString());
        }
    }

    @Override
    public String getDescription(){
        return "- Show elements in descending order";
    }
}
