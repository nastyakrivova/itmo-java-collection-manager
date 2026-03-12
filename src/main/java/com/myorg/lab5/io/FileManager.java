package com.myorg.lab5.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;

/**
 * Менеджер файлового ввода-вывода.
 * Отвечает за сохранение и загрузку коллекции в/из CSV файла.
 * Использует BufferedInputStream и BufferedOutputStream согласно требованиям.
 */
public class FileManager{
    private String fileName;
    private ScriptParser parser;

    public FileManager(String fileName){
        this.fileName = fileName;
        this.parser = new ScriptParser();
    }

    /**
     * Сохраняет коллекцию в файл в формате CSV.
     * Каждый элемент коллекции преобразуется в CSV строку и записывается в файл.
     * 
     * @param collection коллекция MusicBand для сохранения
     * @throws IOException если возникает ошибка ввода-вывода
     *         (нет прав на запись, диск переполнен, файл заблокирован)
     */
    public void save(Collection<MusicBand> collection) throws IOException{
        
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        try{
            for (MusicBand musicBand : collection) {
                String csvLine = parser.toCsv(musicBand);
                byte[] bytes = csvLine.getBytes();
                bos.write(bytes);
                bos.write('\n');
            }
            bos.flush();
        } finally {
            bos.close();
        }

    }

    /**
     * Загружает коллекцию из CSV файла.
     * Читает файл построчно, парсит каждую строку в объект MusicBand.
     * Пропускает пустые строки и строки с ошибками.
     * 
     * @return коллекция загруженных MusicBand
     * @throws IOException если возникает ошибка ввода-вывода
     *         (файл не найден, нет прав на чтение)
     */
    public Collection<MusicBand> load() throws IOException{
        List<MusicBand> bandList = new ArrayList<>();

        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        try {
            byte[] allBytes = bis.readAllBytes();
            String data = new String(allBytes);
            String[] lines = data.split("\n");

            for (String line : lines) {
                line.trim();
                if(line.isEmpty()){ continue; }

                try{
                    MusicBand band = parser.parse(line);
                    bandList.add(band);
                }catch(Exception e){
                    System.err.println("Error parsing line: " + line);
                }
                
            }
        } finally {
            bis.close();
        }

        return bandList;
    }

}
