package com.myorg.lab5.utils;

import com.myorg.lab5.model.Coordinates;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.model.MusicGenre;
import com.myorg.lab5.model.Studio;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MusicBandParser {
    private final Scanner scanner;
    private final boolean interactive;
    
    public MusicBandParser(Scanner scanner) {
        this.scanner = scanner;
        this.interactive = true;
    }
    
    public MusicBandParser() {
        this.scanner = null;
        this.interactive = false;
    }
    
    public MusicBand parseFromString(String line) {
        List<String> fields = parseCsvLine(line);
        return buildFromFields(fields);
    }
    
    private List<String> parseCsvLine(String line) {
        line = line.trim().replace("\r", "");
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
        return fields;
    }
    
    private MusicBand buildFromFields(List<String> fields) {
        if (fields.size() < 6) {
            throw new IllegalArgumentException("Not enough fields");
        }
        String[] data = fields.toArray(new String[0]);
        
        String name = data[0].trim();
        int x = Integer.parseInt(data[1].trim());
        int y = Integer.parseInt(data[2].trim());
        Coordinates coordinates = new Coordinates(x, y);
        int numOfPart = Integer.parseInt(data[3].trim());
        int albumsCount = Integer.parseInt(data[4].trim());
        MusicGenre genre = MusicGenre.valueOf(data[5].trim().toUpperCase());
        
        Studio studio = null;
        if (data.length > 6 && data[6] != null && !data[6].trim().isEmpty()) {
            String studioName = data[6].trim();
            if (!studioName.equals("null")) { 
                studio = new Studio(studioName);
            }
        }
        
        Integer singlesCount = null;
        if (data.length > 7 && !data[7].trim().isEmpty()) {
            singlesCount = Integer.parseInt(data[7].trim());
        }

        int ownerId = 0;
        if (data.length > 8 && !data[8].trim().isEmpty()) {
            ownerId = Integer.parseInt(data[8].trim());
        }

        int id = 0;
        if (data.length > 9 && !data[9].trim().isEmpty()) {
            id = Integer.parseInt(data[9].trim());
        }

        MusicBand band = new MusicBand(name, coordinates, numOfPart, albumsCount, genre, studio, singlesCount);
        band.setOwnerId(ownerId);
        band.setId(id);
        
        return band;
    }
    
    public MusicBand parseInteractively() {
        if (!interactive) throw new IllegalStateException("Not in interactive mode");
        
        System.out.print("\nAdding new music band: ");
        String name = readString("Name: ", s -> !s.isEmpty(), "Cannot be empty");
        
        System.out.println("\nCoordinates: ");
        int x = readInt("x (max 290): ", v -> v <= 290, "Value must be ≤ 290");
        int y = readInt("y: ", v -> true, "");
        Coordinates coordinates = new Coordinates(x, y);
        
        int participants = readPositiveInt("Number of participants (>0): ");
        int albums = readPositiveInt("Number of albums (>0): ");
        
        MusicGenre genre = readGenre();
        Studio studio = readOptionalStudio();
        Integer singles = readOptionalInt("Number of singles");
        
        return new MusicBand(name, coordinates, participants, albums, genre, studio, singles);
    }
    
    public String toCsv(MusicBand band) {
        StringBuilder sb = new StringBuilder();
        sb.append(band.getName()).append(",");
        sb.append(band.getCoordinates().getX()).append(",");
        sb.append(band.getCoordinates().getY()).append(",");
        sb.append(band.getNumberOfParticipants()).append(",");
        sb.append(band.getAlbumsCount()).append(",");
        sb.append(band.getGenre());
        
        String studioName = "";
        if (band.getStudio() != null) {
            String name = band.getStudio().getName();
            if (name != null && !name.equals("null") && !name.isEmpty()) {
                studioName = name;
            }
        }
        sb.append(",").append(studioName);
        
        sb.append(",").append(band.getSinglesCount() != null ? band.getSinglesCount() : "");
        sb.append(",").append(band.getOwnerId());
        sb.append(",").append(band.getId());
        return sb.toString();
    }
    
    private String readString(String prompt, java.util.function.Predicate<String> validator, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (validator.test(input)) return input;
            System.out.println("Error: " + errorMsg);
        }
    }
    
    private int readInt(String prompt, java.util.function.Predicate<Integer> validator, String errorMsg) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (validator.test(value)) return value;
                if (!errorMsg.isEmpty()) System.out.println("Error: " + errorMsg);
            } catch (NumberFormatException e) {
                System.out.println("Error: Enter an integer");
            }
        }
    }
    
    private int readPositiveInt(String prompt) {
        return readInt(prompt, v -> v > 0, "Must be greater than 0");
    }
    
    private MusicGenre readGenre() {
        while (true) {
            System.out.println("\nAvailable genres: RAP, SOUL, POP");
            System.out.print("Select genre: ");
            String input = scanner.nextLine().trim().toUpperCase();
            switch (input) {
                case "RAP": return MusicGenre.RAP;
                case "SOUL": return MusicGenre.SOUL;
                case "POP": return MusicGenre.POP;
                default: System.out.println("Error: Invalid genre");
            }
        }
    }
    
    private Studio readOptionalStudio() {
        System.out.print("Studio name (press Enter to skip): ");
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty() || input.equals("null")) {
            System.out.println("-> returning null");
            return null;
        }
        Studio studio = new Studio(input);
        return studio;
    }
    
    private Integer readOptionalInt(String fieldName) {
        while (true) {
            System.out.print(fieldName + " (press Enter to skip): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            try {
                int value = Integer.parseInt(input);
                if (value > 0) return value;
                System.out.println("Error: Must be greater than 0");
            } catch (NumberFormatException e) {
                System.out.println("Error: Enter an integer or leave empty");
            }
        }
    }
}