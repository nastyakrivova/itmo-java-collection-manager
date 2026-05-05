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

    public enum OperationResult {
        NOT_FOUND,
        NOT_OWNER,
        SUCCESS,
        ERROR
    }

    public boolean add(MusicBand musicBand, int ownerId){
        synchronized(list) {
            try{
                int id = dbManager.saveMusicBand(musicBand, ownerId);
                if (id > 0){
                    musicBand.setId(id);
                    musicBand.setOwnerId(ownerId);
                    list.add(musicBand);
                    logger.info("Added band to collection: id={}, ownerId={}", id, ownerId);
                    return true;
                } else {
                    logger.error("Failed to save band, returned id={}", id);
                    return false;
                }
            } catch (SQLException e){
                logger.error("Error saving in db: " + e.getMessage());
                return false;
            }
        }
        
        
    }

    public OperationResult updateId(int id, MusicBand musicBand, int userId){
        synchronized(list){

            MusicBand found_band = list.stream()
                    .filter(b -> b.getId() == id)
                    .findFirst()
                    .orElse(null);

            if(found_band == null){
                return OperationResult.NOT_FOUND;
            }
            if(found_band.getOwnerId() != userId){
                return OperationResult.NOT_OWNER;
            }
            try{
                if(dbManager.updateMusicBand(id, musicBand, userId)){
                    musicBand.setId(id);
                    musicBand.setOwnerId(userId);
                    int ind = list.indexOf(found_band);
                    list.set(ind, musicBand);
                    return OperationResult.SUCCESS;
                }
            }catch(SQLException e){
                logger.error("Error updating element in db: " + e.getMessage());
            }
            return OperationResult.ERROR;
        }
        
    }

    public OperationResult removeById(int id, int userId){
        synchronized(list){
            MusicBand band = list.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);

            if (band == null){
                return OperationResult.NOT_FOUND;
            }
            if(band.getOwnerId() != userId){
                return OperationResult.NOT_OWNER;
            }

            try{
                
                if (dbManager.deleteMusicBand(id, userId)){
                    list.remove(band);
                    logger.info("Removed band: id={}, userId={}", id, userId);
                    return OperationResult.SUCCESS;
                }

            }catch(SQLException e){
                logger.error("Error deleting from db: " + e.getMessage());
            }
            return OperationResult.ERROR;
        }
    }

    public OperationResult clear(int userId){
        synchronized(list){
            List<MusicBand> toRemove = list.stream()
                .filter(b -> b.getOwnerId() == userId)
                .collect(Collectors.toList());

            if (toRemove.isEmpty()){
                return OperationResult.NOT_FOUND;
            }
            try{
                for(MusicBand band: toRemove){
                    dbManager.deleteMusicBand(band.getId(), userId);
                }
                list.removeAll(toRemove);
                return OperationResult.SUCCESS;
            } catch (SQLException e) {
                logger.error("Error clearing: " + e.getMessage());
            }
            return OperationResult.ERROR;
        }
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

    public OperationResult removeGreater(MusicBand musicBand, int userId){
        synchronized(list){
            List<MusicBand> greaterBands = list.stream()
                .filter(b -> b.compareTo(musicBand) > 0)
                .collect(Collectors.toList());

            if (greaterBands == null){
                return OperationResult.NOT_FOUND;
            }
            try{
                for(MusicBand band: greaterBands){
                    if(band.getOwnerId() == userId){
                        dbManager.deleteMusicBand(band.getId(), userId);
                        list.remove(list.indexOf(band));
                        return OperationResult.SUCCESS;
                    }
                }
            } catch (SQLException e) {
                logger.error("Error clearing: " + e.getMessage());
            }
            return OperationResult.ERROR;


        }
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

    public MusicBand getBandById(int id) {
    synchronized(list) {
        return list.stream()
            .filter(b -> b.getId() == id)
            .findFirst()
            .orElse(null);
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
