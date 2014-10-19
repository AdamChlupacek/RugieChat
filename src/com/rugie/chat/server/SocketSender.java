package com.rugie.chat.server;

import com.rugie.chat.Util;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
  private List<DatagramPacket> tasks;

  private List<User> users;
  private boolean running;

  public SocketSender(int port) throws IOException {
    this.users = new ArrayList<User>();
    this.tasks = new ArrayList<DatagramPacket>();
    this.socket = new DatagramSocket();

    long time = System.currentTimeMillis();
    File file = new File("ChatLog/" +"log_" + time + ".txt");
//    file.mkdirs();
//    file.createNewFile();

    this.fileW = new BufferedWriter(new FileWriter(file));
    this.running = true;

    writeToFile("Starting server at port: " + port);
  }

  private void doTasks() {
    DatagramPacket[] curTasks = new DatagramPacket[tasks.size()];

    synchronized (this) {
      curTasks = tasks.toArray(curTasks);
      tasks.clear();
    }

    for (DatagramPacket packet: curTasks){
      //if (packet.getData() == null) continue;
      String s = new String(packet.getData());
      System.out.println(s);
      writeToFile(s);

      if (s.startsWith(Constants.NEW_CONNECT)){
        int id = UniqueId.getIdentifier();
        users.add(new User(Util.parseMessage(s,Constants.NEW_CONNECT),packet.getAddress(),packet.getPort(), id));
        send(Constants.ID+id+Constants.ID, findUser(id));
      }
      if (s.startsWith(Constants.MESSAGE)){
        User user = findUser(Integer.parseInt(s.split(Constants.ID)[1]));
        if (user == null){
          System.out.println("User who is not registered send a message");
        }else {
          sendToAll(Constants.MESSAGE + Util.parseMessage(s,Constants.MESSAGE) + Constants.MESSAGE + Constants.NAME + user.getUserName()+Constants.NAME);
        }
      }
      if (s.startsWith(Constants.PING)){
        //sendToAll(s.substring(3));
      }
      if (s.startsWith(Constants.DISCONNECT)){
        User user = findUser(Integer.parseInt(s.split(Constants.ID)[1]));
        users.remove(user);
      }
    }

  }

  private void pingUsers() {

  }

  private User findUser(int id){
    for (User u: users){
      if (id == u.getId()) return u;
    }
    return null;
  }

  private void sendToAll(String s){
    for (User u: users) {
      send(s,u);
    }
  }

  private void send(String message, User u){
    byte[] bytes = message.getBytes();
    DatagramPacket packet = new DatagramPacket(bytes,0,bytes.length,u.getAddress(), u.getPort());
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
    synchronized (this) {
      tasks.add(packet);
    }
  }
  @Override
  public void run() {
    while(running){
      doTasks();
      pingUsers();
    }
    try {
      socket.close();
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
