package com.myorg.lab5.server;

import com.myorg.lab5.CommandRequest;
import com.myorg.lab5.SerializationUtil;

public class RequestReader {
    public CommandRequest readRequest(byte[] data){
        try{
            
            // byte[] data = new byte[packet.getLength()];
            // System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());

            System.out.println("Получено " + data.length + " байт");

            Object obj = SerializationUtil.deserialize(data);

            if(obj instanceof CommandRequest) {
                CommandRequest request = (CommandRequest) obj;
                return request;
            } else {
                System.err.println("Получен объект неизвестного типа: " + obj.getClass().getName());
                return null;
            }

        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }
}
