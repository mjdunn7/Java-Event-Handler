package io.mattdunn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServerThread implements Runnable{
    private ServerSocket serverSocket;
    private int port;

    private ArrayList<TCPConnection> tcpConnections;
    private EventFactory eventFactoryReference;

    public int getPort(){
        return port;
    }

    public byte[] getIP(){
        return serverSocket.getInetAddress().getAddress();
    }

    public TCPServerThread(int port, EventFactory eventFactory){
        this.port = port;
        tcpConnections = new ArrayList<TCPConnection>();

        try {
            serverSocket = new ServerSocket(port);
            this.port = serverSocket.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        eventFactoryReference = eventFactory;
    }

    @Override
    public void run() {
        while(true){
            try {
                Socket socket = serverSocket.accept();

                tcpConnections.add(new TCPConnection(socket, eventFactoryReference));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
