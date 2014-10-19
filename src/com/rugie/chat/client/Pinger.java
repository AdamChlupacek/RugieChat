package com.rugie.chat.client;

import com.rugie.chat.Delay;
import com.rugie.chat.server.Constants;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 19/10/14
 * Time: 12:38
 */
public class Pinger implements Runnable {

  private int notReceived;

  private Delay receive;
  private SocketManager manager;
  private boolean running;
  private Action stopAction;

  public Pinger(SocketManager manager) {
    this.manager = manager;
    this.receive = new Delay(1000);
    receive.start();
    this.running = true;
  }

  public void pingReceived(){
    this.notReceived = 0;
    receive.start();
  }

  @Override
  public void run() {
    while(running){

      if (notReceived >= 5){
        System.out.println("Toomuch not received");
        stop();
        stopAction.action();
        return;
      }

      try {
        manager.send(Constants.PING);
        if (receive.isOver()) notReceived++;
        Thread.sleep(1000);

      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void stopAction(Action action){
    this.stopAction = action;
  }

  public void stop(){
    this.running = false;
  }
}
