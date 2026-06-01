package com.myorg.lab7.server;

import com.myorg.lab7.data_exchange.CommandRequest;
import com.myorg.lab7.data_exchange.SerializationUtil;

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
