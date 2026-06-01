package com.myorg.lab7.server;


import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.myorg.lab7.data_exchange.Batch;
import com.myorg.lab7.data_exchange.CommandRequest;
import com.myorg.lab7.data_exchange.CommandResponse;
import com.myorg.lab7.data_exchange.SerializationUtil;

public class ConnectionListener {

    private static final Logger logger = LogManager.getLogger(ServerMain.class);

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalTimeNanos = new AtomicLong(0);
    private long minTimeNanos = Long.MAX_VALUE;
    private long maxTimeNanos = 0;

    private final RequestReader requestReader;
    private final CommandExecutor commandExecutor;
    private final ResponseSender responseSender;

    private final DatagramChannel channel;
    private volatile boolean running = true;

    private final ExecutorService readingPool;
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

        String poolType = System.getenv().getOrDefault("POOL_TYPE", "fixed").trim();
        String poolSizeStr = System.getenv().getOrDefault("POOL_SIZE", String.valueOf(cores * 2)).trim();
        int poolSize = Integer.parseInt(poolSizeStr);
        logger.info("Конфигурация пула: type={}, size={}", poolType, poolSize);

        ExecutorService processingPool;
        switch (poolType) {
            case "cached":
                processingPool = Executors.newCachedThreadPool();
                logger.info("Используем CachedThreadPool");
                break;
            case "workstealing":
                processingPool = Executors.newWorkStealingPool(poolSize);
                logger.info("Используем WorkStealingPool с размером {}", poolSize);
                break;
            case "fixed":
            default:
                processingPool = Executors.newFixedThreadPool(poolSize);
                logger.info("Используем FixedThreadPool с размером {}", poolSize);
                break;
        }
        
        this.readingPool = processingPool;

        
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

                    readingPool.submit(() ->{
                        Thread handlerThread = new Thread(() -> processRequest(data, clientAddress));
                        handlerThread.start();
                    });

                }

            } catch (Exception e) {
                if(running) {
                    logger.info("Error: " + e.getMessage());
                }
            }
            
        }
    }

    private void processRequest(byte[] data, SocketAddress clientAddress){
        long start = System.nanoTime();
        try{
            logger.info("[{}] Начало обработки запроса от {}", 
                Thread.currentThread().getName(), clientAddress);
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
        }finally {
            long duration = System.nanoTime() - start;
            totalRequests.incrementAndGet();
            totalTimeNanos.addAndGet(duration);

            synchronized(this) {
                if (duration < minTimeNanos) minTimeNanos = duration;
                if (duration > maxTimeNanos) maxTimeNanos = duration;
            }
        }
    }

    public void stop(){
        running = false;
    }

    public void printStats() {
        long count = totalRequests.get();
        if (count  == 0) return;

        double avgMs = (totalTimeNanos.get() / count) / 1_000_000.0;
        System.out.println("СТАТИСТИКА ПУЛА ПОТОКОВ");
        System.out.println("  Обработано запросов: " + count);
        System.out.println("  Среднее время: " + avgMs + " мс");
        System.out.println("  Мин. время: " + (minTimeNanos / 1_000_000.0) + " мс");
        System.out.println("  Макс. время: " + (maxTimeNanos / 1_000_000.0) + " мс");
        System.out.println("  Пропускная способность: " + (count * 1000 / (totalTimeNanos.get() / 1_000_000)) + " req/сек");

    }
}
