package com.rugie.chat.server;

import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 13:11
 */
public class User {

  private int pingAttNo;
  private String userName;

  private InetAddress address;
  private int port;

  public User(String userName, InetAddress address, int port) {
    this.userName = userName;
    this.address = address;
    this.port = port;
  }

  public int getPingAttNo() {
    return pingAttNo;
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
}
