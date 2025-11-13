package com.assignment4.tasks;

import java.util.Arrays;

public class VectorClock {

  private final int[] timestamps;

  public VectorClock(int numOfClients) {
    timestamps = new int[numOfClients];
    Arrays.fill(timestamps, 0);
  }

  public synchronized void setVectorClock(int processId, int time) {
    timestamps[processId] = time;
  }

  public synchronized void tick(int processId) {
    timestamps[processId]++;
  }

  public synchronized int getCurrentTimestamp(int processId) {
    return timestamps[processId];
  }

  public synchronized void updateClock(VectorClock other) {
    for (int i = 0; i < timestamps.length; i++) {
      timestamps[i] = Math.max(timestamps[i], other.timestamps[i]);
    }
  }

  public synchronized String showClock() {
    return Arrays.toString(timestamps);
  }

  public synchronized boolean checkAcceptMessage(int senderId, VectorClock senderClock) {
    int senderIndex = senderId - 1;

    if (senderClock.timestamps[senderIndex] != timestamps[senderIndex] + 1) {
      return false;
    }

    for (int i = 0; i < timestamps.length; i++) {
      if (i != senderIndex) {
        if (senderClock.timestamps[i] > timestamps[i]) {
          return false;
        }
      }
    }
    return true;
  }
}
