package com.myorg.lab5.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.io.FileManager;

public class CollectionManager {
    private ArrayList<MusicBand> list;
    private FileManager fileManager;
    private final LocalDate initDate;
    private ConsoleManager consoleManager;

  
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
            consoleManager.show("error saving file");
            e.printStackTrace();
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
        int count = 0;
        for (MusicBand musicBand : list) {
            if(musicBand.getStudio() != null && musicBand.getStudio().equals(studio)){
                count += 1;
            }
        }
        return count;
    }

    public ArrayList<MusicBand> filterLessThanNumbOfParticipants(int numberOfParticipants){
        ArrayList<MusicBand> result = new ArrayList<MusicBand>();
        for (MusicBand musicBand : list) {
            if (musicBand.getNumberOfParticipants() < numberOfParticipants){
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
