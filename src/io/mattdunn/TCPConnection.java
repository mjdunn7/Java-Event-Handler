package io.mattdunn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPConnection {
    private Socket socket;
    private DataOutputStream dout;

    private DataInputStream din;
    private Thread receiverThread;

    private String ipAddress;
    private int port;

    private EventFactory eventFactoryReference;


    public TCPConnection(Socket socket, EventFactory eventFactory) throws IOException {
        eventFactoryReference = eventFactory;

        this.socket = socket;
        dout = new DataOutputStream(socket.getOutputStream());
        din = new DataInputStream(socket.getInputStream());

        TCPReceiver receiver = new TCPReceiver();

        ipAddress = socket.getRemoteSocketAddress().toString();
        port = socket.getLocalPort();

        receiverThread = new Thread(receiver);
        receiverThread.start();
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendData(byte[] data) throws IOException {
        int dataLength = data.length;
        dout.writeInt(dataLength);
        dout.write(data, 0, dataLength);
        dout.flush();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public class TCPReceiver implements Runnable{
        @Override
        public void run() {
            int dataLength;
            while (socket != null) {
                try {
                    dataLength = din.readInt();
                    byte[] data = new byte[dataLength];
                    din.readFully(data, 0, dataLength);

                    //TODO: Handle incoming connection, give data to EventFactory.
                    eventFactoryReference.addEvent(new Event(data));
                } catch (SocketException se) {
                    System.out.println(se.getMessage());
                    break;
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage()) ;
                    break;
                }
            }
        }
    }
}
