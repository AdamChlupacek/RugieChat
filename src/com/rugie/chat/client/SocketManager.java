package com.rugie.chat.client;

import com.rugie.chat.Util;
import com.rugie.chat.server.Constants;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 13:53
 */
public class SocketManager implements Runnable{

  private DatagramSocket socket;
  private int port;
  private InetAddress address;

  private TextArea area;
  private boolean running;
  private int id;

  private SetAction action;

  public SocketManager(String address, int port, TextArea area) throws SocketException, UnknownHostException {
    this.socket = new DatagramSocket();
    this.address = InetAddress.getByName(address);
    this.port = port;
    this.area = area;
    this.id = -1;
    this.running = true;
  }

  public void send(String message) throws IOException {
    message += Constants.ID + id + Constants.ID;
    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
    socket.send(packet);
  }

  private void process(String message){

    System.out.println(message);
    if (message.startsWith(Constants.ID)){
      this.id = Integer.parseInt(Util.parseMessage(message, Constants.ID));
    }

    if (message.startsWith(Constants.MESSAGE)){
      Platform.runLater(()->{
        action.setValue("[" + Util.parseMessage(message, Constants.NAME) + "]: " + Util.parseMessage(message, Constants.MESSAGE) + "\n");
      });
    }
  }

  @Override
  public void run() {
    while (running){
      byte[] buffer = new byte[2048];

      DatagramPacket packet = new DatagramPacket(buffer,buffer.length);

      try {
        socket.receive(packet);
        String message = new String(packet.getData());
        process(message);
      } catch (IOException e) {
        //e.printStackTrace();
      }
    }
  }

  public void stop(){
    this.running = false;
    socket.close();
  }

  public void setAction(SetAction action) {
    this.action = action;
  }
}
