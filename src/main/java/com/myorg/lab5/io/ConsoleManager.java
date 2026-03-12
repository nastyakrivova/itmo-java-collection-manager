package com.myorg.lab5.io;

import java.util.Scanner;

import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ConsoleParser;

/**
 * Менеджер консольного ввода-вывода.
 * Отвечает за взаимодействие с пользователем через консоль:
 * вывод сообщений, чтение команд, управление режимами ввода.
 */
public class ConsoleManager {
    private ConsoleParser parser;
    private Scanner scanner;

    public ConsoleManager(){
        this.scanner = new Scanner(System.in);
        this.parser = new ConsoleParser();
    }

    /**
     * Выводит сообщение в консоль.
     * 
     * @param message сообщение для вывода
     */
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
     * Включает режим чтения из скрипта.
     * Парсер переключается на чтение данных из файла, а не с консоли.
     * 
     * @param scriptScanner сканер для чтения из файла скрипта
     */
    public void enableScriptMode(Scanner scriptScanner) {
        parser.setScriptMode(scriptScanner);
    }

    
    /**
     * Выключает режим скрипта.
     * Парсер возвращается к чтению из консоли.
     */
    public void disableScriptMode() {
        parser.setConsoleMode();
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
