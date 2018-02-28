package io.mattdunn;

public class EventProcessor {
    public void onEvent(Event event){
        if(event.getEventType() == Event.KEYBOARD_COMMAND){
            System.out.println("Keyboard input received:");
            System.out.println(event.getMessage());
        }

        if(event.getEventType() == Event.NETWORK_MESSAGE){
            System.out.println("Message received from TCP:");
            System.out.println(event.getMessage());
        }
    }

    public static void main(String[] args){
        int portNum = 0;
        try {
            portNum = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e){
            System.err.println("Invalid command line usage. Must specify port.");
            return;
        } catch (NumberFormatException e){
            System.err.println("Invalid command line usage. Port must an integer between 1024 and 65535.");
            return;
        }

        if(portNum < 1025 || portNum > 65534){
            System.err.println("Error. Port number must be between 1024 and 65535.");
        }

        EventProcessor handler = new EventProcessor();

        EventFactory eventFactory = new EventFactory(handler);
        Thread eventFactoryThread = new Thread(eventFactory);
        eventFactoryThread.start();

        KeyboardListener listener = new KeyboardListener(eventFactory);
        Thread keyboardListenerThread = new Thread(listener);
        keyboardListenerThread.start();

        TCPServerThread server = new TCPServerThread(portNum, eventFactory);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
