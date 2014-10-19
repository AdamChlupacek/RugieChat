package com.rugie.chat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 15:37
 */
public class UniqueId {
  private static List<Integer> ids = new ArrayList<Integer>();
  private static final int RANGE = 10000;

  private static int index = 0;

  static {
    for (int i = 0; i < RANGE; i++) {
      ids.add(i);
    }
    Collections.shuffle(ids);
  }

  private UniqueId() {
  }

  public static int getIdentifier() {
    if (index > ids.size() - 1) index = 0;
    return ids.get(index++);
  }

}
