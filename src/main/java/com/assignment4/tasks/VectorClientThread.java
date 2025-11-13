package com.assignment4.tasks;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VectorClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final VectorClock vcl;
  private final int id;
  private final byte[] receiveData = new byte[1024];
  private final List<Message> buffer = new ArrayList<>();

  public VectorClientThread(DatagramSocket clientSocket, VectorClock vcl, int id) {
    this.clientSocket = clientSocket;
    this.vcl = vcl;
    this.id = id;
  }

  @Override
  public void run() {
    while (true) {
      try {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        String[] parts = receivedMessage.split(":");

        if (parts.length >= 3) {
          String messageBody = parts[0];
          String vectorClockStr = parts[1];
          int senderId = Integer.parseInt(parts[2]);

          String[] clockValues = vectorClockStr.replaceAll("[\\[\\]]", "").split(",\\s*");
          VectorClock receivedClock = new VectorClock(4);
          for (int i = 0; i < clockValues.length; i++) {
            receivedClock.setVectorClock(i, Integer.parseInt(clockValues[i]));
          }

          Message message = new Message(messageBody, receivedClock, senderId);

          if (vcl.checkAcceptMessage(senderId, receivedClock)) {
            displayMessage(message);
            checkBufferedMessages();
          } else {
            buffer.add(message);
          }
        }
      } catch (Exception e) {
        break;
      }
    }
  }

  private void displayMessage(Message message) {
    System.out.println("Client " + message.getSenderID() + ": " + message.getMessage() + ": " + message.getClock().showClock());
    vcl.updateClock(message.getClock());
    System.out.println("Current clock: " + vcl.showClock());
  }

  private void checkBufferedMessages() {
    boolean foundMessage = true;
    while (foundMessage) {
      foundMessage = false;
      Iterator<Message> iterator = buffer.iterator();
      while (iterator.hasNext()) {
        Message bufferedMessage = iterator.next();
        if (vcl.checkAcceptMessage(bufferedMessage.getSenderID(), bufferedMessage.getClock())) {
          displayMessage(bufferedMessage);
          iterator.remove();
          foundMessage = true;
          break;
        }
      }
    }
  }
}
