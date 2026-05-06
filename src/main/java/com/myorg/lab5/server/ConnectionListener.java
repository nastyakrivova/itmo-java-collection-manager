package com.myorg.lab5.server;


import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.List;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.myorg.lab5.data_exchange.Batch;
import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.data_exchange.CommandResponse;
import com.myorg.lab5.data_exchange.SerializationUtil;

public class ConnectionListener {

    private static final Logger logger = LogManager.getLogger(ServerMain.class);

    private final RequestReader requestReader;
    private final CommandExecutor commandExecutor;
    private final ResponseSender responseSender;

    private final DatagramChannel channel;
    private volatile boolean running = true;

    private final ExecutorService processingPool;
    private final ForkJoinPool responsePool;

    public ConnectionListener(DatagramChannel channel, 
                              RequestReader requestReader,
                              CommandExecutor commandExecutor,
                              ResponseSender responseSender) throws Exception{
        this.requestReader = requestReader;
        this.commandExecutor = commandExecutor;
        this.responseSender = responseSender;
        this.channel = channel;

        int cores = Runtime.getRuntime().availableProcessors();
        this.processingPool = Executors.newFixedThreadPool(cores*2);
        this.responsePool = ForkJoinPool.commonPool();
        logger.info("ConnectionListener initialized with {} processing threads", cores * 2);
    }

    public void start() {
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

                    processingPool.submit(() -> handleRequest(data, clientAddress));
                }

            } catch (Exception e) {
                if(running) {
                    logger.info("Error: " + e.getMessage());
                }
            }
            
        }
    }

    private void handleRequest(byte[] data, SocketAddress clientAddress){
        try{
            Object obj = SerializationUtil.deserialize(data);
            if(obj instanceof Batch){
                Batch batch = (Batch) obj;
                List<CommandRequest> requests = batch.getRequests();
                List<CommandResponse> responses = new ArrayList<>();

                for (CommandRequest request : requests){
                    CommandResponse response = commandExecutor.execute(request);
                    responses.add(response);
                }

                Batch responseBatch = new Batch();
                responseBatch.setResponses(responses);

                responsePool.submit(() -> responseSender.send(responseBatch, clientAddress));
            } else if (obj instanceof CommandRequest) {
                CommandRequest request = (CommandRequest) obj;
                CommandResponse response = commandExecutor.execute(request);
                responsePool.submit(() -> responseSender.send(response, clientAddress));
            }else {
                logger.error("Unknown object type received");
            }
        }catch (Exception e) {
            logger.error("Error handling request: " + e.getMessage());
        }
    }

    public void stop(){
        running = false;
    }
}
