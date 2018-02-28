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

    public class TCPReceiver implements Runnable{
        @Override
        public void run() {
            int dataLength;
            while (socket != null) {
                try {
                    //First byte indicates the length of the message
                    dataLength = din.readInt();
                    //Remaining bytes are the message in ASCII.
                    byte[] data = new byte[dataLength];
                    din.readFully(data, 0, dataLength);

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
