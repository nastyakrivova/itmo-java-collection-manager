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

public class FileManager{
    private String fileName;
    private ScriptParser parser;

    public FileManager(String fileName){
        this.fileName = fileName;
        this.parser = new ScriptParser();
    }

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
