package com.aliengame.socketserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private final static int SERVER_PORT = 8081;
    static Vector<ClientHandler> clientHandlers = new Vector<ClientHandler>();
    static Integer clientNumber = 0;
    static Integer counter = 0;

    /**
     * Holds clients and configure socket and datastreams
     *
     * @param args server runner
     * @throws IOException socket throws
     */
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(SERVER_PORT);
        Socket s;
        while (true) {
            s = ss.accept();
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            ClientHandler clientHandler = new ClientHandler(s, "client " + clientNumber, dis, dos);
            Thread t = new Thread(clientHandler);
            clientHandlers.add(clientHandler);
            clientNumber++;
            if (clientNumber == 2) {
                clientNumber = 0;
            }
            t.start();

        }
    }
}
