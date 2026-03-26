package com.myorg.lab5.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.myorg.lab5.io.FileManager;

public class CollectionManager {
    private ArrayList<MusicBand> list;
    private FileManager fileManager;
    private final LocalDate initDate;

    //подается какой-то рандомный тип данных и он его в сит оборачивает?
    //существует два способа: из скрипта и из консоли ручками, но это на уровень выше важно
    //ничего не передается при создании, оно само создается, а потом заполняется
    public CollectionManager(){
        this.list = new ArrayList<MusicBand>();
        this.fileManager = new FileManager("data.csv");
        this.initDate = LocalDate.now();
    }

    public void add(MusicBand musicBand){
        list.add(musicBand);
    }

    public void updateId(int id, MusicBand musicBand){
        list.set(id, musicBand);
    }

    public void removeById(int id){
        list.remove(id);
    }

    public void clear(){
        list.clear();
    }

    public void save(){
        try{
            fileManager.save(list);
        }catch(IOException e){
            e.getStackTrace();
        }
        
    }

    public void addIfMin(MusicBand musicBand){
        list.sort(Comparator.naturalOrder());
        if (musicBand.compareTo(list.get(0)) < 0){
            list.add(musicBand);
        }
    }

    public void removeGreater(MusicBand musicBand){
        list.removeIf(i -> i.compareTo(musicBand) > 0);
    }

    public void removeLower(MusicBand musicBand){
        list.removeIf(i -> i.compareTo(musicBand) < 0);
    }

    public int countByStudio(Studio studio){
        return (int)list.stream()
            .filter(band -> studio.equals(band.getStudio()))
            .count();
    }

    public List<MusicBand> filterLessThanNumbOfParticipants(int numberOfParticipants){
        return list.stream()
            .filter(band -> band.getNumberOfParticipants() < numberOfParticipants)
            .collect(Collectors.toList());
    }

    public String showElements(){
        if (list.isEmpty()) {
        return "Collection is empty";
        }

        return list.stream()
            .sorted(Comparator.comparing(MusicBand::getName))
            .map(MusicBand::toString)
            .collect(Collectors.joining("\n"));
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
