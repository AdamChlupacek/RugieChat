package com.rugie.chat.client;

import javafx.application.Application;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 12:20
 */
public class Main {

  public static void main(String[] args){
//    try {
//      SocketManager sender = new SocketManager(9000);
//      sender.send("~NC~Adam");
//    } catch (SocketException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    Application.launch(GuiWindow.class);
  }

}
