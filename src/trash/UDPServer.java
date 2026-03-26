// package com.myorg.lab5.server;

// import java.io.IOException;
// import java.net.DatagramPacket;
// import java.net.DatagramSocket;
// import java.net.InetAddress;

// public class UDPServer {
//     private static final int PORT = 9806;
//     private static final int BUFER_SIZE = 4096;

//     public static void main(String[] args){
//         System.out.println("Сервер запущен");
//         System.out.println("Порт: " + PORT);
//         System.out.println("Ожидание сообщений\n");

//         try(DatagramSocket socket = new DatagramSocket(PORT)){
//             byte[] recieveBuffer = new byte[BUFER_SIZE];

//             while(true) {
//                 DatagramPacket receivePacket = new DatagramPacket(recieveBuffer, recieveBuffer.length);

//                 System.out.println("Жду сообщения ...");
//                 socket.receive(receivePacket);

//                 String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                
//                 InetAddress clientAddress = receivePacket.getAddress();
//                 int clientPort = receivePacket.getPort();
                
//                 System.out.println("Получено от " + clientAddress + ":" + clientPort);
//                 System.out.println("Сообщение: " + receivedMessage);

//                 String responseMessage = "OK: server received \"" + receivedMessage + "\"";
//                 byte[] responseData = responseMessage.getBytes();
//                 DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);

//                 socket.send(sendPacket);
//                 System.out.println("Отправлен ответ: " + responseMessage);
//             } 
//         }catch(Exception e){
//             e.printStackTrace();
//         }
//     }
// }
