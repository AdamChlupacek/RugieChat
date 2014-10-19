package com.rugie.chat.server;

import com.rugie.chat.Delay;
import com.rugie.chat.Util;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 12:58
 */

/**
 * A class for managing of the server functions, it takes the messages that are
 * received via socket {@link com.rugie.chat.server.SocketSender} and parses them.
 * Also creates a log of received messages.
 */
public class SocketSender implements Runnable {

  private BufferedWriter fileW;
  private DatagramSocket socket;
  private List<DatagramPacket> tasks;

  private List<User> users;
  private boolean running;

  private Delay ping;

  /**
   * Constructor for the SocketSender class
   * @param port on what port will the server operate
   * @throws IOException
   */
  public SocketSender(int port) throws IOException {
    this.users = new ArrayList<>();
    this.tasks = new ArrayList<>();
    this.socket = new DatagramSocket();
    this.ping = new Delay(1000);
    this.ping.start();

    long time = System.currentTimeMillis();
    File file = new File("ChatLog/" +"log_" + time + ".txt");

    this.fileW = new BufferedWriter(new FileWriter(file));
    this.running = true;

    writeToFile("Starting server at port: " + port);
  }

  /**
   * Main function of the server, takes the packet that were received, creates a copy of them
   * and proceeds to parse them and do actions depending on the content of the datagrams
   */
  private void doTasks() {
    DatagramPacket[] curTasks = new DatagramPacket[tasks.size()];

    synchronized (this) {
      curTasks = tasks.toArray(curTasks);
      tasks.clear();
    }

    for (DatagramPacket packet: curTasks){
      String s = new String(packet.getData());
      System.out.println(s);
      writeToFile(s);

      //Parses and adds a new user to the list of all active users
      if (s.startsWith(Constants.NEW_CONNECT)){
        int id = UniqueId.getIdentifier();
        users.add(new User(Util.parseMessage(s,Constants.NEW_CONNECT),packet.getAddress(),packet.getPort(), id));
        send(Constants.ID+id+Constants.ID, findUser(id));
      }
      //Resend a message to all active users
      if (s.startsWith(Constants.MESSAGE)){
        User user = findUser(Integer.parseInt(s.split(Constants.ID)[1]));
        if (user == null){
          System.out.println("User who is not registered send a message");
        }else {
          sendToAll(Constants.MESSAGE + Util.parseMessage(s,Constants.MESSAGE) + Constants.MESSAGE + Constants.NAME + user.getUserName()+Constants.NAME);
        }
      }
      //Ping check
      if (s.startsWith(Constants.PING)){
        User user = findUser(Integer.parseInt(s.split(Constants.ID)[1]));
        user.resetPing();
        send(Constants.PING + Constants.PING, user);
      }
      //Notification of disconnected user
      if (s.startsWith(Constants.DISCONNECT)){
        User user = findUser(Integer.parseInt(s.split(Constants.ID)[1]));
        users.remove(user);
      }
    }

  }

  /**
   * One per cycle check of active and time out users
   */
  private void pingUsers() {
    if (ping.isOver()){
      List<User> remove = new ArrayList<>();
      for (User u:users){
        u.incrementPing();
        if (u.getPingOver() >= 5){
          remove.add(u);
          send(Constants.DISCONNECT,u);
        }
      }
      users.removeAll(remove);
      ping.start();
    }
  }

  /**
   * finding a user in the user list
   * @param id id of the user
   * @return returns found user, or null in case that the user does not exist
   */
  private User findUser(int id){
    for (User u: users){
      if (id == u.getId()) return u;
    }
    return null;
  }

  /**
   * Sends a message to all users
   * @param s contents of the message
   */
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
