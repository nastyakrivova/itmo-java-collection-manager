package com.myorg.lab7.data_exchange;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtil {

    public static byte[] serialize(Object obj) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException{
        ByteArrayInputStream bias = new ByteArrayInputStream(bytes);
        try(ObjectInputStream ois = new ObjectInputStream(bias)){
            return ois.readObject();
        }

    }
}
