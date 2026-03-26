package com.myorg.lab5.commands;

import java.util.List;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;


/**
 * Команда фильтрации элементов по количеству участников.
 * Выводит элементы, у которых numberOfParticipants меньше заданного значения.
 */
public class FilterLessThenNumOfPart implements Command{
    private CollectionManager collectionManager;

    public FilterLessThenNumOfPart(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет фильтрацию по количеству участников.
     * 
     * @param args массив аргументов, где args[0] - пороговое значение (целое число)
     */
    @Override
    public void execute(String args[]){
        List<MusicBand> filtered = collectionManager.filterLessThanNumbOfParticipants(Integer.parseInt(args[0]));
        if (filtered.isEmpty()){
            System.out.println("Нет элементов к количеством участников меньше" + args[0]);
        } else {
            filtered.stream()
                .map(MusicBand::toString)
                .forEach(System.out::println);
        }
    }

    @Override
    public String getDescription(){
        return "- Show elements with participants < N";
    }
}
