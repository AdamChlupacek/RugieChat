package com.rugie.chat.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 12:19
 */
public class Main {

  public static void main(String[] args){
    SocketSender sender;
    SocketReceiver receiver;

    try {
      sender = new SocketSender(9000);
      receiver = new SocketReceiver(9000, sender);

      new Thread(sender,"Sender").start();
      new Thread(receiver,"Receiver").start();

      boolean running = true;
      Scanner scanner = new Scanner(System.in);
      while(running){
        System.out.println("Input action: ");
        String input = scanner.nextLine();

        if (input.equals("stop")){
          running = false;
        }
      }

      sender.stop();
      receiver.stop();

    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
