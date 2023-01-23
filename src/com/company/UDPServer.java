package com.company;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Scanner;

// создал клиента
public class UDPServer {
    public final static int SERVICE_PORT = 50009;
// прописал порт
    public static String getLocalIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual())
                    continue;


                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {
                        ip = addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }

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

                    System.out.println("Server: " + receivedData);

                } catch (IOException ignored) {
                }
            }
        }).start();

        //Отправка сообщения
        new Thread(() -> {
            while (true) {
                try {
                    DatagramSocket clientSocket = new DatagramSocket();
                    Scanner sc = new Scanner(System.in);
                    System.out.print("Client: ");
                    String sentence = sc.nextLine();
                    byte[] buf = new byte[1024];
                    buf = sentence.getBytes();

                    int timeout = 100;
                    String subnet = "192.168.";
                    String ip= String.valueOf(InetAddress.getLocalHost());
                    System.out.println(ip);

                    for (int i = 1; i < 255; i++) {
                        String host = subnet + i + "." + "1";
                        DatagramPacket sendingPacket = new DatagramPacket(sentence.getBytes(), buf.length, InetAddress.getByName(host), 50002);
                        clientSocket.send(sendingPacket);
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
    }
}