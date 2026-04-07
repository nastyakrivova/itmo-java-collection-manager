package com.myorg.lab5.server;


import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.myorg.lab5.CommandRequest;
import com.myorg.lab5.CommandResponse;

public class ConnectionListener {

    private static final Logger logger = LogManager.getLogger(ServerMain.class);

    private final RequestReader requestReader;
    private final CommandExecutor commandExecutor;
    private final ResponseSender responseSender;

    private final DatagramChannel channel;
    private boolean running = true;


    public ConnectionListener(DatagramChannel channel, 
                              RequestReader requestReader,
                              CommandExecutor commandExecutor,
                              ResponseSender responseSender) throws Exception{
        this.requestReader = requestReader;
        this.commandExecutor = commandExecutor;
        this.responseSender = responseSender;
        this.channel = channel;
    }

    public void start() {
        System.out.println("dgrtg");
        logger.info("Server is running, waiting for connection...");
        ByteBuffer buffer = ByteBuffer.allocate(8192);

        while(running){
            try{
                buffer.clear();
                SocketAddress clientAddress = channel.receive(buffer);

                if(clientAddress != null){
                    buffer.flip();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    logger.info("\n Package received from: " + clientAddress);

                    CommandRequest request = requestReader.readRequest(data);

                    if(request == null){
                        logger.error("Failed to read request");
                        continue;
                    }


                    CommandResponse response = commandExecutor.execute(request);

                    responseSender.send(response, clientAddress);
                    logger.info("Response was sent to client: " + clientAddress);
                }

            } catch (Exception e) {
                if(running) {
                    logger.info("Error: " + e.getMessage());
                }
            }
            
        }
    }

    public void stop(){
        running = false;
    }
}
