package com.myorg.lab5.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.myorg.lab5.CommandResponse;
import com.myorg.lab5.SerializationUtil;


public class ResponseSender {
    private final DatagramChannel channel;
    private static final Logger logger = LogManager.getLogger(ServerMain.class);


    public ResponseSender(DatagramChannel channel){
        this.channel = channel;
    }

    public void send(CommandResponse response, SocketAddress clientAddress){
        try{
            byte[] responseData = SerializationUtil.serialize(response);
            ByteBuffer buffer = ByteBuffer.wrap(responseData);
            int sent = channel.send(buffer, clientAddress);
            logger.info("Response has been sent to the client " + sent + " byte " + response.isSuccess());
            if (response.getMessage() != null) {
                logger.info("Message: " + response.getMessage());
            }
        } catch(IOException e){
            e.getMessage();
        }
    }
}
