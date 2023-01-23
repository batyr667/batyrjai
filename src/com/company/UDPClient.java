package com.company;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class UDPClient {
    public final static int SERVICE_PORT = 5003;
    private static  String client1="172.20.10.4";
    public static void main(String[] args) throws IOException {

        //Прием сообщения

        new Thread(() -> {
            while (true) {
                try {
                    byte[] receivingDataBuffer = new byte[1024];
                    DatagramSocket serverSocket = new DatagramSocket(SERVICE_PORT);
                    DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);

                    serverSocket.receive(inputPacket);

                    String receivedData = new String(receivingDataBuffer, 0, inputPacket.getLength());


                    byte[] byteDecode = Base64.getDecoder().decode(receivedData.getBytes(StandardCharsets.UTF_8));

                    String mess = new String(byteDecode, 0, byteDecode.length);

                    System.out.println(" Сервер: "+mess);
                } catch (IOException ignored) {}
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    DatagramSocket clientSocket = new DatagramSocket();
                    Scanner sc = new Scanner(System.in);
                    System.out.print("Вы пишете: ");
                    String sentence = sc.nextLine();
                    byte[] bytecEncode = Base64.getEncoder().encode(sentence.getBytes());
                    DatagramPacket sendingPacket = new DatagramPacket(bytecEncode, bytecEncode.length, InetAddress.getByName(client1), 5003);
                    clientSocket.send(sendingPacket);
                } catch (IOException ignored) {}
            }
        }).start();


    }
}