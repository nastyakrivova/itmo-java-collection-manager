package com.myorg.lab5.utils;


import com.myorg.lab5.model.Coordinates;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.model.MusicGenre;
import com.myorg.lab5.model.Studio;
import java.util.ArrayList;
import java.util.List;

public class ScriptParser{
    public ScriptParser(){

    }

    public MusicBand parse(String line){
        line = line.trim();
        line = line.replace("\r", "");


        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        
        String[] data = fields.toArray(new String[0]);

        if (data.length < 6) {
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

        Studio studio = null;
        if (data.length > 6 && data[6] != null && !data[6].trim().isEmpty()) {
            String studioName = data[6].trim();
            if (!studioName.equals("null")) {
                studio = new Studio(studioName);
            }
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
        String name = musicBand.getName();
        if (name.contains(",")) {
            line.append("\"").append(name).append("\"");
        } else {
            line.append(name);
        }
        line.append(",");
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