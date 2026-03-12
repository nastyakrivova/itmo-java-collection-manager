package com.myorg.lab5.io;

import java.util.Scanner;

import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ConsoleParser;

public class ConsoleManager {
    private ConsoleParser parser;
    private Scanner scanner;

    public ConsoleManager(){
        this.scanner = new Scanner(System.in);
        this.parser = new ConsoleParser();
    }

    public void show(String message){
        System.out.println(message);
    }


    /**
     * Читает строку из консоли.
     * Выводит приглашение ">" перед вводом.
     * 
     * @return введенная пользователем строка
     */
    public String read(){
        System.out.print(">");;
        return scanner.nextLine();
    }

    /**
     * Парсит ввод пользователя в объект MusicBand.
     * 
     * @return объект MusicBand или null в случае ошибки
     */
    public MusicBand parse(){
        try{
            return parser.parse();
        } catch(Exception e) {
            System.err.println("Error parsing line: " + e.getMessage());
            return null;
        }
        
    }

}
