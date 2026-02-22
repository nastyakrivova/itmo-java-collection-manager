package com.myorg.lab5.model;

import java.util.ArrayList;
import java.util.Comparator;

import io.FileManager;

public class CollectionManager {
    private ArrayList<MusicBand> list;
    private FileManager fileManager;

    //подается какой-то рандомный тип данных и он его в сит оборачивает?
    //существует два способа: из скрипта и из консоли ручками, но это на уровень выше важно
    //ничего не передается при создании, оно само создается, а потом заполняется
    public CollectionManager(){
        this.list = new ArrayList<MusicBand>();
        this.fileManager = new FileManager("data.csv");
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
        fileManager.save(list);
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
        int count = 0;
        for (MusicBand musicBand : list) {
            if(musicBand.getStudio() == studio){
                count += 1;
            }
        }
        return count;
    }

    public ArrayList<MusicBand> filterLessThanNumbOfParticipants(int numberOfParticipants){
        ArrayList<MusicBand> result = new ArrayList<MusicBand>();
        for (MusicBand musicBand : list) {
            if (musicBand.getNumberOfParticipants() == numberOfParticipants){
                result.add(musicBand);
            }
        }
        return result;
    }
}
