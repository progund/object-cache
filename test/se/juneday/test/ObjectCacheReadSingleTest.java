package se.juneday.test;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import se.juneday.ObjectCache;
import se.juneday.test.User;

public class ObjectCacheReadSingleTest {

  public static void main(String[] args) {
    ObjectCache<User> cache = new ObjectCache<>(User.class);
    
    System.out.print("Testing reading objects: ");
    User cachedUser = cache.readObject();
    assert (cachedUser.name().equals("Henrik Sandklef")) : "Expected Henrik Sandklef, found " + cachedUser.name();
    System.out.println("OK");
  }
}
