package com.myorg.lab7.commands;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Интерфейс для всех команд приложения.
 * Определяет базовый контракт для реализации паттерна "Команда".
 * Каждая конкретная команда должна реализовывать этот интерфейс.
 */
public interface Command {
    /**
     * Выполняет команду с заданными аргументами.
     * Конкретная реализация зависит от типа команды.
     * 
     * @param args массив аргументов команды. 
     *             Может быть пустым для команд без аргументов.
     */
    public void execute(String[] args, int userId);
    
    /**
     * Возвращает описание команды для отображения в справке.
     * Формат: "- краткое описание команды"
     * 
     * @return строковое описание команды
     */
    public String getDescription();

    
    default String executeAndReturn(String[] args, int userId) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(buffer));
            execute(args, userId);
            return buffer.toString().trim();
        } finally {
            System.setOut(originalOut);
        }
    }
}
