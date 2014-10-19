package com.rugie.chat;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 23:24
 */
public class Util {

  public static String parseMessage(String message, String regEx){
    return message.split(regEx)[1];
  }

}
