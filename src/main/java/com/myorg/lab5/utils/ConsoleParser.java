package com.myorg.lab5.utils;


import java.util.Scanner;

import com.myorg.lab5.model.Coordinates;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.model.MusicGenre;
import com.myorg.lab5.model.Studio;

public class ConsoleParser{
    private Scanner scanner;
    public ConsoleParser(){
        this.scanner = new Scanner(System.in);
    }

    public MusicBand parse(){
        System.out.print("\nAdding new music band: ");
        String name = readString("Name: ", "Cannot be empty");

        System.out.println("\nCoordinates: ");
        int x = readInt("x (max 290): ", 290);
        int y = readInt("y: ", Integer.MAX_VALUE);
        Coordinates coordinates = new Coordinates(x, y);

        int participants = readPositiveInt("Number of participants (>0): ");
        int albums = readPositiveInt("Number of albums (>0): ");
        
        MusicGenre genre = readGenre();
        
        Studio studio = readOptionalStudio();
        Integer singles = readOptionalInt("Number of singles");
        MusicBand band = new MusicBand(name, coordinates, participants, albums, genre, studio, singles);
        return band;
    }



    private String readString(String message, String errorMessage){
        while(true) {
            System.out.println(message);
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()){ return name; }
            System.out.println("Error: " + errorMessage);
        }
    }

    private int readInt(String message, int maxValue){
        while(true) {
            try {
                System.out.println(message);
                int value = Integer.parseInt(scanner.nextLine());
                if (value <= maxValue) {
                return value;
                }
                System.out.println("Error: Value must be ≤ " + maxValue);
            } catch (NumberFormatException e) {
                System.out.println("Error: Enter an integer");
            }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                System.out.println("Error: Must be greater than 0");
            } catch (NumberFormatException e) {
                System.out.println("Error: Enter an integer");
            }
        }
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
        System.out.print("\nStudio name (press Enter to skip): ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : new Studio(input);
    }

    private Integer readOptionalInt(String fieldName) {
        while (true) {
            System.out.print(fieldName + " (press Enter to skip): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                return null;
            }
            
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("Error: Must be greater than 0");
            } catch (NumberFormatException e) {
                System.out.println("Error: Enter an integer or leave empty");
            }
        }
    }
}