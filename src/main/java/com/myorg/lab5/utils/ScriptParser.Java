package com.myorg.lab5.utils;


import com.myorg.lab5.model.Coordinates;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.model.MusicGenre;
import com.myorg.lab5.model.Studio;

public class ScriptParser{
    public ScriptParser(){

    }

    public MusicBand parse(String line){
        String[] data = line.split(",");

        if (data.length < 8) {
            throw new IllegalArgumentException("Not enough fields in CSV line");
        }

        String name = data[0].trim();
        int x = Integer.parseInt(data[1].trim());
        int y = Integer.parseInt(data[2].trim());
        Coordinates coordinates = new Coordinates(x, y);
        int numOfPart = Integer.parseInt(data[3].trim());
        int albumsCount = Integer.parseInt(data[4].trim());
        String genreStr = data[5].trim().toUpperCase();
        MusicGenre genre = MusicGenre.valueOf(genreStr);

        String studioName = data[6].trim();
        Studio studio = null;
        if (!studioName.isEmpty()) {
            studio = new Studio(studioName);
        }
    
        String singlesStr = data[7].trim();
        Integer singlesCount = null;
        if (!singlesStr.isEmpty()) {
            singlesCount = Integer.parseInt(singlesStr);
        }

        MusicBand band = new MusicBand(name, coordinates, numOfPart, albumsCount, genre, studio, singlesCount);
        return band;
    }

    public String toCsv(MusicBand musicBand){
        StringBuilder line = new StringBuilder();

        line.append(musicBand.getName()).append(",");
        line.append(musicBand.getCoordinates().getX()).append(",");
        line.append(musicBand.getCoordinates().getY()).append(",");
        line.append(musicBand.getNumberOfParticipants()).append(",");
        line.append(musicBand.getAlbumsCount()).append(",");
        line.append(musicBand.getGenre()).append(",");

        Studio studio = musicBand.getStudio();
        if (studio != null) {
            line.append(studio.getName());
        }
        line.append(",");
        
        Integer singles = musicBand.getSinglesCount();
        if (singles != null) {
            line.append(singles);
        }

        return line.toString();
    }
}