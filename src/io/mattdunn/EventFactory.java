package io.mattdunn;

import java.util.LinkedList;
import java.util.Queue;

public class EventFactory implements Runnable {
    private Queue<Event> eventQueue;

    //Given to the event processor opon creation, this is the class
    //that will handle events as they come in.
    private EventProcessor eventProcessorReference;

    public EventFactory(EventProcessor processor) {
        eventProcessorReference = processor;
        eventQueue = new LinkedList<>();
    }

    //This is the method that is called by the event producing threads
    //to add an event that needs to be processed. The 'synchronized'
    //keyword means that this method can only be accessed by one thread
    //at a time to avoid concurrency issues.
    public synchronized void addEvent(Event event) {
        eventQueue.add(event);
        this.notify();
    }

    @Override
    public void run() {
        while(true){
            synchronized (this) {
                while (eventQueue.isEmpty()) {
                    try {
                        //The wait() method causes thread execution to pause
                        //until the notify() method is called on the object
                        //or the wait is interupted.
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                //at least one event is in the queue, process it.
                eventProcessorReference.onEvent(eventQueue.remove());
            }
        }
    }
}

