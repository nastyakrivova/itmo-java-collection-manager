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

        if (args.length == 0) {
            System.out.println("Ошибка: не указан число для фильтровки");
            return;
        }

        try {
            Integer participants = Integer.parseInt(args[0]);
            List<MusicBand> filtered = collectionManager.filterLessThanNumbOfParticipants(participants);

            if(filtered.isEmpty()){
                System.out.println("Нет элементов с количеством участников меньше " + participants);
            }else{
                filtered.stream()
                    .map(MusicBand::toString)
                    .forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        }

        // List<MusicBand> filtered = collectionManager.filterLessThanNumbOfParticipants(Integer.parseInt(args[0]));
        // if (filtered.isEmpty()){
        //     System.out.println("Нет элементов к количеством участников меньше" + args[0]);
        // } else {
        //     filtered.stream()
        //         .map(MusicBand::toString)
        //         .forEach(System.out::println);
        // }
    }

    @Override
    public String getDescription(){
        return "- Show elements with participants < N";
    }
}
