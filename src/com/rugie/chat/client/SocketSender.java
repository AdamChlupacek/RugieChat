package com.rugie.chat.client;

import java.io.IOException;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 13:53
 */
public class SocketSender {

  private DatagramSocket socket;

  public SocketSender(int port) throws SocketException {

    this.socket = new DatagramSocket();
  }

  public void send(String message) throws IOException {

    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName("localhost"), 9000);
    socket.send(packet);

  }
}
