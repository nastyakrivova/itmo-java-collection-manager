package com.myorg.lab5.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import com.myorg.lab5.io.FileManager;

/**
 * Менеджер коллекции MusicBand.
 * Содержит основную бизнес-логику приложения:
 * добавление, удаление, обновление, поиск и фильтрация элементов.
 */
public class CollectionManager {
    private ArrayList<MusicBand> list;
    private FileManager fileManager;
    private final LocalDate initDate;

    
    public CollectionManager(){
        this.list = new ArrayList<MusicBand>();
        this.fileManager = new FileManager("data.csv");
        this.initDate = LocalDate.now();
    }

    /**
     * Добавляет элемент в коллекцию.
     * 
     * @param musicBand элемент для добавления
     */
    public void add(MusicBand musicBand){
        list.add(musicBand);
    }


    /**
     * Обновляет элемент коллекции по индексу.
     * 
     * @param id индекс элемента для обновления
     * @param musicBand новый элемент
     */
    public void updateId(int id, MusicBand musicBand){
        list.set(id, musicBand);
    }


    /**
     * Удаляет элемент по индексу.
     * 
     * @param id индекс удаляемого элемента
     */
    public void removeById(int id){
        list.remove(id);
    }

    /**
     * Очищает коллекцию.
     */
    public void clear(){
        list.clear();
    }

    /**
     * Сохраняет коллекцию в файл.
     * Обрабатывает возможные ошибки ввода-вывода.
     */
    public void save(){
        try{
            fileManager.save(list);
        }catch(IOException e){
        
        }
        
    }

    /**
     * Добавляет элемент, если он меньше минимального в коллекции.
     * Предварительно сортирует коллекцию.
     * 
     * @param musicBand элемент для добавления
     */
    public void addIfMin(MusicBand musicBand){
        list.sort(Comparator.naturalOrder());
        if (musicBand.compareTo(list.get(0)) < 0){
            list.add(musicBand);
        }
    }

    /**
     * Удаляет все элементы, превышающие заданный.
     * 
     * @param musicBand эталонный элемент
     */
    public void removeGreater(MusicBand musicBand){
        list.removeIf(i -> i.compareTo(musicBand) > 0);
    }


    /**
     * Удаляет все элементы, меньшие заданного.
     * 
     * @param musicBand эталонный элемент
     */
    public void removeLower(MusicBand musicBand){
        list.removeIf(i -> i.compareTo(musicBand) < 0);
    }


    /**
     * Подсчитывает количество элементов с указанной студией.
     * 
     * @param studio студия для поиска
     * @return количество элементов
     */
    public int countByStudio(Studio studio){
        int count = 0;
        for (MusicBand musicBand : list) {
            if(musicBand.getStudio() == studio){
                count += 1;
            }
        }
        return count;
    }


    /**
     * Фильтрует элементы по количеству участников.
     * 
     * @param numberOfParticipants пороговое значение
     * @return список элементов с участников < порога
     */
    public ArrayList<MusicBand> filterLessThanNumbOfParticipants(int numberOfParticipants){
        ArrayList<MusicBand> result = new ArrayList<MusicBand>();
        for (MusicBand musicBand : list) {
            if (musicBand.getNumberOfParticipants() == numberOfParticipants){
                result.add(musicBand);
            }
        }
        return result;
    }

    public String showElements(){
        if (list.isEmpty()) {
        return "Collection is empty";
        }
    
        StringBuilder sb = new StringBuilder();
        for (MusicBand band : list) {
            sb.append(band.toString()).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<MusicBand> getList(){
        return list;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Collection Information\n");
        sb.append(String.format("Implementation: %s\n", list.getClass().getName()));
        sb.append(String.format("Initialization date: %s\n", initDate));
        sb.append(String.format("Elements count: %d\n", list.size()));
        return sb.toString();
    }
}
