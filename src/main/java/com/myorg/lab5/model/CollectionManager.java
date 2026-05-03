package com.myorg.lab5.model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.myorg.lab5.server.DBManager;

public class CollectionManager {
    private static final Logger logger = LogManager.getLogger(CollectionManager.class);
    private final ArrayList<MusicBand> list;
    // private FileManager fileManager;
    private final LocalDate initDate;
    private DBManager dbManager;

    public CollectionManager(DBManager dbManager){
        this.list = new ArrayList<MusicBand>();
        this.dbManager = dbManager;
        this.initDate = LocalDate.now();
        loadFromDB();
    }

    public CollectionManager(){
        this.list = new ArrayList<MusicBand>();
        // this.fileManager = new FileManager("data.csv");
        this.initDate = LocalDate.now();
    }

    public void add(MusicBand musicBand, int ownerId){
        synchronized(list) {
            try{
                int id = dbManager.saveMusicBand(musicBand, ownerId);
                if (id > 0){
                    musicBand.setId(id);
                    list.add(musicBand);
                    logger.info("Added band to collection: id={}, ownerId={}", id, ownerId);
                } else {
                    logger.error("Failed to save band, returned id={}", id);
                }
            } catch (SQLException e){
                logger.error("Error saving in db: " + e.getMessage());
            }
        }
        
        
    }

    public void updateId(int id, MusicBand musicBand, int userId){
        synchronized(list){
            try{
                if(dbManager.updateMusicBand(id, musicBand, userId)){
                    for (int i = 0; i < list.size(); i++){
                        if(list.get(i).getId() == id){
                            list.set(i, musicBand);
                            logger.info("Updated band: id={}, userId={}", id, userId);
                            break;
                        }
                    }
                }else {
                    logger.warn("User {} not owner of band {}", userId, id);
                }
            }catch(SQLException e){
                logger.error("Error updating element in db: " + e.getMessage());
            }
        }
        
    }

    public boolean removeById(int id, int userId){
        synchronized(list){
            try{
                if (dbManager.existsById(id)){
                    if (dbManager.deleteMusicBand(id, userId)){
                        logger.info("Removed band: id={}, userId={}", id, userId);
                        return list.removeIf(band -> band.getId() == id);
                    }
                }
        
            }catch(SQLException e){
                logger.error("Error deleting from db: " + e.getMessage());
            }
        return false;
        }
    }

    public void clear(){
        list.clear();
    }

    public boolean containsId(Integer id){
        for (MusicBand band : list) {
            if (band.getId().equals(id)){
                return true;
            }
        }
        return false;
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
        synchronized(list){
            if (list.isEmpty()) {
            return "Collection is empty";
            }

            return list.stream()
                .sorted(Comparator.comparing(MusicBand::getName))
                .map(MusicBand::toString)
                .collect(Collectors.joining("\n"));
        }
    }

    public ArrayList<MusicBand> getList(){
        return list;
    }

    public void loadFromDB(){
        try{
            ArrayList<MusicBand> loaded = dbManager.loadAllMusicBands();
            synchronized(list){
                list.clear();
                list.addAll(loaded);
            }
            logger.info("Loaded " + list.size() + " elements from db");
        }catch(SQLException e){
            logger.error("Error while loading from db: " + e.getMessage());
        }
        
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
