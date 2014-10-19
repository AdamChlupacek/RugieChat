package com.rugie.chat;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 22:22
 */
public class Tuple2<T1,T2> {

  private T1 first;
  private T2 second;

  public Tuple2(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  public T1 getFirst() {
    return first;
  }

  public T2 getSecond() {
    return second;
  }
}
