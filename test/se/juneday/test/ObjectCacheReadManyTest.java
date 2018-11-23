package se.juneday.test;

import java.io.Serializable;
import java.util.Collection;

import se.juneday.ObjectCache;
import se.juneday.test.User;

public class ObjectCacheReadManyTest {

  public static void main(String[] args) {
    ObjectCache<User> cache = new ObjectCache<>(User.class);
    
    System.out.print("Testing reading objects: ");
    // Get the users from cache
    Collection<User> cached = cache.readObjects();
    assert (cached.size()==2) : "Expected 2, found " + cached.size();
    System.out.println("OK");
  }
}
