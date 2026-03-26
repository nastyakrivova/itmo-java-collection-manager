package com.myorg.lab5.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.myorg.lab5.CommandResponse;
import com.myorg.lab5.SerializationUtil;


public class ResponseSender {
    private final DatagramChannel channel;


    public ResponseSender(DatagramChannel channel){
        this.channel = channel;
    }

    public void send(CommandResponse response, SocketAddress clientAddress){
        try{
            byte[] responseData = SerializationUtil.serialize(response);
            ByteBuffer buffer = ByteBuffer.wrap(responseData);
            int sent = channel.send(buffer, clientAddress);
            System.out.println("Клиенту отправлен ответ" + sent + "байт" + response.isSuccess());
            if (response.getMessage() != null) {
                System.out.println("Сообщение: " + response.getMessage());
            }
        } catch(IOException e){
            e.getMessage();
        }
    }
}
