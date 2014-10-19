package com.rugie.chat;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 19/10/14
 * Time: 12:31
 */
public class Delay {

  private long start;
  private int duration;


  public Delay(int duration){
    this.duration = duration;
  }

  public void start(){
    this.start = System.currentTimeMillis();
  }

  public boolean isOver(){
    return System.currentTimeMillis() > duration + start;
  }

}
