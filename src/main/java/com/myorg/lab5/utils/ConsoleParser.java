package com.myorg.lab5.utils;

import java.util.Scanner;
import com.myorg.lab5.model.Coordinates;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.model.MusicGenre;
import com.myorg.lab5.model.Studio;

/**
 * Парсер для ввода данных музыкальной группы из консоли или скрипта.
 * Обеспечивает валидацию ввода и повторные попытки при ошибках.
 * Поддерживает два режима: консольный (с подсказками) и скриптовый (без подсказок).
 */
public class ConsoleParser {
    private Scanner consoleScanner;
    private Scanner scriptScanner;
    private boolean scriptMode = false;

    public ConsoleParser() {
        this.consoleScanner = new Scanner(System.in);
    }

    /**
     * Включает режим чтения из скрипта.
     * 
     * @param scriptScanner сканер для чтения из файла скрипта
     */
    public void setScriptMode(Scanner scriptScanner) {
        this.scriptMode = true;
        this.scriptScanner = scriptScanner;
    }

    /**
     * Выключает режим скрипта, возвращается к консольному вводу.
     */
    public void setConsoleMode() {
        this.scriptMode = false;
        this.scriptScanner = null;
    }


    /**
     * Парсит данные музыкальной группы.
     * В зависимости от режима использует консольный или скриптовый ввод.
     * 
     * @return объект MusicBand или null в случае ошибки
     */
    public MusicBand parse() {
        if (scriptMode) {
            return parseFromScript();
        } else {
            return parseFromConsole();
        }
    }

    private MusicBand parseFromConsole() {
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
        return new MusicBand(name, coordinates, participants, albums, genre, studio, singles);
    }

    private MusicBand parseFromScript() {
        try {
            String name = scriptScanner.nextLine().trim();
            System.out.println("→ Name: " + name);

            int x = Integer.parseInt(scriptScanner.nextLine().trim());
            System.out.println("→ x: " + x);
            int y = Integer.parseInt(scriptScanner.nextLine().trim());
            System.out.println("→ y: " + y);
            Coordinates coordinates = new Coordinates(x, y);

            int participants = Integer.parseInt(scriptScanner.nextLine().trim());
            System.out.println("→ Participants: " + participants);
            int albums = Integer.parseInt(scriptScanner.nextLine().trim());
            System.out.println("→ Albums: " + albums);

            String genreStr = scriptScanner.nextLine().trim().toUpperCase();
            System.out.println("→ Genre: " + genreStr);
            MusicGenre genre = MusicGenre.valueOf(genreStr);

            String studioName = scriptScanner.nextLine().trim();
            System.out.println("→ Studio: " + (studioName.isEmpty() ? "null" : studioName));
            Studio studio = studioName.isEmpty() ? null : new Studio(studioName);

            String singlesStr = scriptScanner.nextLine().trim();
            System.out.println("→ Singles: " + (singlesStr.isEmpty() ? "null" : singlesStr));
            Integer singles = singlesStr.isEmpty() ? null : Integer.parseInt(singlesStr);

            return new MusicBand(name, coordinates, participants, albums, genre, studio, singles);

        } catch (Exception e) {
            System.err.println("Error parsing from script: " + e.getMessage());
            return null;
        }
    }

    
    private String readString(String message, String errorMessage) {
        while (true) {
            System.out.println(message);
            String value = consoleScanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Error: " + errorMessage);
        }
    }

    private int readInt(String message, int maxValue) {
        while (true) {
            try {
                System.out.println(message);
                int value = Integer.parseInt(consoleScanner.nextLine());
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
                int value = Integer.parseInt(consoleScanner.nextLine());
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
            String input = consoleScanner.nextLine().trim().toUpperCase();

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
        String input = consoleScanner.nextLine().trim();
        return input.isEmpty() ? null : new Studio(input);
    }

    private Integer readOptionalInt(String fieldName) {
        while (true) {
            System.out.print(fieldName + " (press Enter to skip): ");
            String input = consoleScanner.nextLine().trim();

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