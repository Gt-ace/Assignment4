package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

// This Class handles the continuous listening for incoming messages from the server
public class LTClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final LamportTimestamp lc;
  byte[] receiveData = new byte[1024];

  public LTClientThread(DatagramSocket clientSocket, LamportTimestamp lc) {
    this.clientSocket = clientSocket;
    this.lc = lc;
  }

  @Override
  public void run() {
    // Continuously listen for incoming messages from the server and display them.
    while (true) {
      try {
        // Prepare a packet to receive data
        receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // Receive the packet from the server (blocking call)
        clientSocket.receive(receivePacket);

        // Convert the received data to a string
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

        // Parse the message format: "message:timestamp:id"
        String[] parts = receivedMessage.split(":");

        if (parts.length >= 3) {
          String message = parts[0];
          int receivedTimestamp = Integer.parseInt(parts[1]);
          String senderId = parts[2];

          // Update the clock based on the timestamp received from the server
          lc.updateClock(receivedTimestamp);

          // Display the received message with sender info and timestamp
          System.out.println("Client " + senderId + ": " + message + ":" + receivedTimestamp);
          System.out.println("Current clock: " + lc.getCurrentTimestamp());
        }

      } catch (IOException e) {
        // If the socket is closed or an error occurs, break the loop
        break;
      } catch (NumberFormatException e) {
        // Handle invalid timestamp format
        System.err.println("Error parsing timestamp: " + e.getMessage());
      }
    }
  }
}