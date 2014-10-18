package com.rugie.chat.server;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 12:58
 */
public class SocketSender implements Runnable {

  private BufferedWriter fileW;
  private DatagramSocket socket;
  private List<String> tasks;

  private List<User> users;
  private boolean running;

  public SocketSender(int port) throws IOException {
    this.tasks = new ArrayList<String>();
    this.users = new ArrayList<User>();

    this.socket = new DatagramSocket();

    long time = System.currentTimeMillis();
    File file = new File("ChatLog/" +"log_" + time + ".txt");
//    file.mkdirs();
//    file.createNewFile();

    this.fileW = new BufferedWriter(new FileWriter(file));
    this.running = true;

    writeToFile("Starting server at port: " + port);
  }

  private void doTasks(){
    String[] curTasks = new String[tasks.size()];
    tasks.toArray(curTasks);
    tasks.clear();

    for (String s: curTasks){
      writeToFile(s);

      if (s.startsWith(Constants.MESSAGE)){
        sendToAll(s.substring(3));
      }
    }
  }

  private void pingUsers() {

  }

  private void sendToAll(String s){
    byte[] message = s.getBytes();
    for (User u: users) {
      send(message,u);
    }
  }

  private void send(byte[] message, User u){
    DatagramPacket packet = new DatagramPacket(message,0,message.length,u.getAddress(), u.getPort());
    try {
      socket.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeToFile(String s){
    try {
      long time = System.currentTimeMillis();
      fileW.write("["+ time +"]" + s,0,s.length());
      fileW.newLine();
      fileW.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void process(DatagramPacket packet) {
    String message = new String(packet.getData());
    if (message.startsWith(Constants.NEW_CONNECT)){
      writeToFile(message);
      this.users.add(new User(message.substring(4),packet.getAddress(),packet.getPort()));
    }else {
      tasks.add(message);
    }
  }

  @Override
  public void run() {
    while(running){
      doTasks();
      pingUsers();
    }
    socket.close();
    try {
      fileW.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to stop the sender from running
   */
  public void stop(){
    this.running = false;
  }
}
