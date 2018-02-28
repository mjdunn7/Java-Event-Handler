package io.mattdunn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyboardListener implements Runnable{
    private EventFactory factoryReference;

    public KeyboardListener(EventFactory factory){
        factoryReference = factory;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String command = null;
            try {
                command = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            factoryReference.addEvent(new Event(command));
        }
    }
}
