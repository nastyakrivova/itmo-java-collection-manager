package com.myorg.lab5.io;

import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.MusicBandParser;

public class ConsoleManager {
    private MusicBandParser parser;

    public void show(String message){
        System.out.println(message);
    }

    public MusicBand parse(){
        parser.parse();
        return ...;
    }


}
