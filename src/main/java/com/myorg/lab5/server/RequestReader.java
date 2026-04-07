package com.myorg.lab5.server;

import com.myorg.lab5.CommandRequest;
import com.myorg.lab5.SerializationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestReader {

    private static final Logger logger = LogManager.getLogger(ServerMain.class);

    public CommandRequest readRequest(byte[] data){
        try{

            logger.info("Received " + data.length + " byte");

            Object obj = SerializationUtil.deserialize(data);

            if(obj instanceof CommandRequest) {
                CommandRequest request = (CommandRequest) obj;
                return request;
            } else {
                logger.error("Received object unknown type: " + obj.getClass().getName());
                return null;
            }

        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }
}
