package com.myorg.lab5.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;

/**
 * Команда вывода элементов в порядке убывания.
 * Сортирует коллекцию в обратном порядке и выводит все элементы.
 */
public class PrintDescendingCommand implements Command{
    private CollectionManager collectionManager;

    /**
     * Создает команду print_descending.
     * 
     * @param collectionManager менеджер коллекции для получения элементов
     */
    public PrintDescendingCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String args[]){
        List<MusicBand> sortedList = new ArrayList<>(collectionManager.getList());
        sortedList.sort(Comparator.reverseOrder());

        for (MusicBand band : sortedList) {
            System.out.println(band.toString());
        }
    }

    @Override
    public String executeAndReturn(String[] args){
        return collectionManager.getList().stream()
            .sorted(Comparator.reverseOrder())
            .map(MusicBand::toString)
            .collect(Collectors.joining("\n"));
    }

    @Override
    public String getDescription(){
        return "- Show elements in descending order";
    }
}
