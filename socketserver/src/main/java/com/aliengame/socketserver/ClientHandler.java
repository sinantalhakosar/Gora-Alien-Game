package com.aliengame.socketserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private String name;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private Socket s;

    ClientHandler(Socket s, String name,
                  DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
    }

    /**
     * handles the message routing
     */
    public void run() {
        String received, user1 = "", user2 = "", score1, score2;
        while (true) {
            try {
                received = dis.readUTF();
                System.out.println(received);
                if (received.equals("logout")) {
                    for (ClientHandler mc : Server.clientHandlers) {
                        mc.s.close();
                        mc.dis.close();
                        mc.dos.close();
                    }
                    Server.clientHandlers.clear();
                    break;
                } else if (received.contains("close")) {
                    this.dos.close();
                    this.dis.close();
                    this.s.close();
                    Server.clientHandlers.remove(this);
                    if (Server.counter == 1 && received.split("-")[1].equals("4")) {
                        Server.counter = 0;
                    }
                    if (this.name.equals("client 1")) {
                        Server.clientNumber = 1;
                    }
                } else if (received.equals("level5")) {
                    Server.counter++;
                    if (Server.counter == 2) {
                        System.out.println("starting level 5");
                        Server.clientHandlers.get(0).dos.writeUTF("start");
                        Server.clientHandlers.get(1).dos.writeUTF("start");
                        Server.counter = 0;
                    }
                } else if (received.contains("user")) {
                    if (Server.clientHandlers.get(0).name.equals("client 1")) {
                        ClientHandler clientHandler = Server.clientHandlers.get(1);
                        Server.clientHandlers.set(1, Server.clientHandlers.get(0));
                        Server.clientHandlers.set(0, clientHandler);
                    }
                    if (this.name.equals("client 0")) {
                        Server.clientHandlers.get(1).dos.writeUTF(received);
                    } else if (this.name.equals("client 1")) {
                        Server.clientHandlers.get(0).dos.writeUTF(received);
                    }
                } else {
                    if (this.name.equals("client 0")) {
                        Server.clientHandlers.get(1).dos.writeUTF(received);
                    } else if (this.name.equals("client 1")) {
                        Server.clientHandlers.get(0).dos.writeUTF(received);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

        }
    }
}
