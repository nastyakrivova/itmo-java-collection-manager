package com.myorg.lab5.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.myorg.lab5.data_exchange.Batch;
import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.data_exchange.CommandResponse;
import com.myorg.lab5.data_exchange.SerializationUtil;

public class NetworkClient implements AutoCloseable {

    private static final int TIMEOUT = 5000;
    private static final int BUFFER_SIZE = 65535;
    private static final int BATCH_SIZE = 10; 

    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort;

    public NetworkClient(String host, int port) throws Exception {
        this.socket = new DatagramSocket();
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
        this.socket.setSoTimeout(TIMEOUT);
    }

    public CommandResponse sendCommand(CommandRequest request) throws Exception {

        clearSocketBuffer();

        byte[] requestData = SerializationUtil.serialize(request);
        DatagramPacket sendPacket = new DatagramPacket(requestData, requestData.length,
                serverAddress, serverPort);
        socket.send(sendPacket);

        byte[] receiveBuffer = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);

        byte[] dataResponse = new byte[receivePacket.getLength()];
        System.arraycopy(receivePacket.getData(), 0, dataResponse, 0, receivePacket.getLength());

        return (CommandResponse) SerializationUtil.deserialize(dataResponse);
    }

    public List<CommandResponse> sendScript(List<CommandRequest> allRequests) throws Exception {
        if (allRequests == null || allRequests.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Batch> batches = splitIntoBatches(allRequests);
        System.out.println("Скрипт разбит на " + batches.size() + " батчей (по " + BATCH_SIZE + " команд)");
        List<CommandResponse> allResponses = new ArrayList<>();
        
        for (int i = 0; i < batches.size(); i++) {
            Batch batch = batches.get(i);
            
            System.out.println("Отправка батча " + (i+1) + "/" + batches.size() + 
                              " (" + batch.getRequests().size() + " команд)");
            
            byte[] requestData = SerializationUtil.serialize(batch);
            DatagramPacket sendPacket = new DatagramPacket(requestData, requestData.length,
                    serverAddress, serverPort);
            socket.send(sendPacket);
            
            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            
            byte[] dataResponse = new byte[receivePacket.getLength()];
            System.arraycopy(receivePacket.getData(), 0, dataResponse, 0, receivePacket.getLength());
            
            Object obj = SerializationUtil.deserialize(dataResponse);
            
            List<CommandResponse> batchResponses = new ArrayList<>();
            
            if (obj instanceof Batch) {
                Batch responseBatch = (Batch) obj;
                batchResponses = responseBatch.getResponses();
                System.out.println("Получен батч ответов: " + batchResponses.size());
            } 
            else if (obj instanceof CommandResponse) {
                batchResponses.add((CommandResponse) obj);
                System.out.println("Получен одиночный ответ");
            }
            else {
                System.err.println("Неизвестный тип ответа: " + obj.getClass().getName());
                continue;
            }
            allResponses.addAll(batchResponses);
        }
        
        System.out.println("Получены все ответы: " + allResponses.size() + " шт.");
        
        return allResponses;
    }
 
    private List<Batch> splitIntoBatches(List<CommandRequest> requests) {
        List<Batch> batches = new ArrayList<>();
        
        for (int i = 0; i < requests.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, requests.size());
            List<CommandRequest> batchRequests = requests.subList(i, end);
            
            Batch batch = new Batch();
            batch.setSequenceNumber(batches.size());
            batch.setTotalBatches((requests.size() + BATCH_SIZE - 1) / BATCH_SIZE);
            batch.setRequests(new ArrayList<>(batchRequests));
            batches.add(batch);
        }
        
        return batches;
    }


    private void clearSocketBuffer() throws IOException {
        byte[] buffer = new byte[65535];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.setSoTimeout(1);
        try {
            while (true){
                socket.receive(packet);
            }
        } catch (SocketException e) {

        } finally {
            socket.setSoTimeout(TIMEOUT);
        }
    }

    @Override
    public void close() {
        socket.close();
    }
}