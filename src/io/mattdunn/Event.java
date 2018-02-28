package io.mattdunn;

public class Event {
    public static final byte KEYBOARD_COMMAND = 0;
    public static final byte NETWORK_MESSAGE = 1;

    private String message;
    private byte type;

    public Event(byte[] message){
        this.message = new String(message);
        type = NETWORK_MESSAGE;
    }
    public Event(String message){
        this.message = message;
        type = KEYBOARD_COMMAND;
    }

    public byte getEventType(){
        return type;
    }

    public String getMessage() {
        return message;
    }
}
