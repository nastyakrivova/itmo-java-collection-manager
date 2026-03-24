package com.myorg.lab5.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9806;
    private static final int TIMEOUT_MS = 5000;

    public static void main(String[] args){
        System.out.println("Client run");
        System.out.println("Server: " + SERVER_HOST + SERVER_PORT);

        try(DatagramSocket socket = new DatagramSocket()){
            socket.setSoTimeout(TIMEOUT_MS);

            String message = "beeeeee";
            byte[] sendData = message.getBytes();
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);

            System.out.println("Message flied: " + message);
            socket.send(sendPacket);

            byte[] receivedBuffer = new byte[4096];
            DatagramPacket receivedPacket = new DatagramPacket(receivedBuffer, receivedBuffer.length);
            System.out.println("Wating for the answers");

            socket.receive(receivedPacket);
            String response = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            System.out.println("Response has been received! " + response);
        } catch(SocketTimeoutException e){
            System.out.println("Timeout");
        } catch(Exception e){
            e.getStackTrace();
        }
    } 

}
