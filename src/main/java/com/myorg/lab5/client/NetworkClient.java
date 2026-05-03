package com.myorg.lab5.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.data_exchange.CommandResponse;
import com.myorg.lab5.data_exchange.SerializationUtil;

public class NetworkClient implements AutoCloseable {

    private static final int TIMEOUT = 5000;
    private static final int BUFFER_SIZE = 8192;

    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort;

    public NetworkClient(String host, int port) throws Exception{
        this.socket = new DatagramSocket();
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
        this.socket.setSoTimeout(TIMEOUT);
    }

    public CommandResponse sendCommand(CommandRequest request) throws Exception{

        byte[] requestData = SerializationUtil.serialize(request);
        DatagramPacket sendPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
        socket.send(sendPacket);


        byte[] receiveBuffer = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);

        byte[] dataRespose = new byte[receivePacket.getLength()];
        System.arraycopy(receivePacket.getData(), 0, dataRespose, 0, receivePacket.getLength());

        return (CommandResponse) SerializationUtil.deserialize(dataRespose);
    }

    @Override
    public void close(){
        socket.close();
    }
}
