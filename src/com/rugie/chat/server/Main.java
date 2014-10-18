package com.rugie.chat.server;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 12:19
 */
public class Main {

  public static void main(String[] args){
    try {
      SocketSender sender = new SocketSender(9000);
      SocketReceiver receiver = new SocketReceiver(9000, sender);

      new Thread(sender).start();
      new Thread(receiver).start();
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
