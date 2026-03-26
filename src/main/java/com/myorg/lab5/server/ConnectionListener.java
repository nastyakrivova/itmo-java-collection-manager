package com.myorg.lab5.server;


import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.myorg.lab5.CommandRequest;
import com.myorg.lab5.CommandResponse;

public class ConnectionListener {

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
        System.out.println("Сервер запущен, ожидание подключений...");
        ByteBuffer buffer = ByteBuffer.allocate(8192);

        while(running){
            try{
                buffer.clear();
                SocketAddress clientAddress = channel.receive(buffer);

                if(clientAddress != null){
                    buffer.flip();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    System.out.println("\n Получен пакет от: " + clientAddress);

                    CommandRequest request = requestReader.readRequest(data);

                    if(request == null){
                        System.err.println("Не удалось прочитать запрос");
                        continue;
                    }


                    CommandResponse response = commandExecutor.execute(request);

                    responseSender.send(response, clientAddress);
                    System.out.println("Ответ отправлен клиенту: " + clientAddress);
                }

            } catch (Exception e) {
                if(running) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            }
            
        }
    }

    public void stop(){
        running = false;
    }
}
