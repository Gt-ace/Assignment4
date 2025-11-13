package com.assignment4.tasks;


public class LamportTimestamp {
    private int timestamp;
    public LamportTimestamp(int time){

        timestamp = time;
    }
    public synchronized void tick(){
        // TODO: update the timestamp by 1
        timestamp++;
    }
    public synchronized int getCurrentTimestamp(){
        return timestamp;
    }
    public synchronized void updateClock(int receivedTimestamp){
        // TODO: update the function to choose the higher value out of the two received timestamps
        timestamp = Math.max(timestamp, receivedTimestamp) + 1;
    }

}
