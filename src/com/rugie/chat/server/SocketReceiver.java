package com.rugie.chat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 12:21
 */

/**
 * A class for a Thread that will be just receiving messages and then passing them to other Thread to be dealt with
 */
public class SocketReceiver implements Runnable {

  private DatagramSocket socket;
  private boolean running;

  private SocketSender sender;

  /**
   * Constructor for the object
   * @param port
   * @param sender
   * @throws SocketException
   */
  public SocketReceiver(int port, SocketSender sender) throws SocketException {

    this.socket = new DatagramSocket(port);
    this.sender = sender;
    this.running = true;

  }

  /**
   * Since the class will be used in a thread it has to implement Runnable
   */
  @Override
  public void run() {
    while(running){
      receive();
    }
  }

  /**
   * A method to receive any kind of message from the clients
   */
  private void receive(){
    byte[] buffer = new byte[2048];

    DatagramPacket packet = new DatagramPacket(buffer,buffer.length);

    try {
      socket.receive(packet);
      sender.process(packet);
    } catch (IOException e) {
      //e.printStackTrace();
    }

  }

  /**
   * Method to stop teh receiver from running
   */
  public void stop(){
    this.running = false;
    socket.close();
  }


}
