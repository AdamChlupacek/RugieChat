package com.rugie.chat.server;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 13:11
 */
public class User {
  private String userName;

  private InetAddress address;
  private int port;

  private int id;

  private int pingOver;


  public User(String userName, InetAddress address, int port, int id) {
    this.userName = userName;
    this.address = address;
    this.port = port;
    this.id = id;
    this.pingOver = 0;
  }


  public String getUserName() {
    return userName;
  }

  public InetAddress getAddress() {
    return address;
  }

  public int getPort() {
    return port;
  }

  public int getId() {
    return id;
  }

  public void incrementPing(){
    this.pingOver++;
  }

  public void resetPing(){
    this.pingOver = 0;
  }

  public int getPingOver() {
    return pingOver;
  }
}
