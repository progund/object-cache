package se.juneday.test;

import java.io.Serializable;
import java.util.Map;

import se.juneday.ObjectCacheReader;
import se.juneday.test.User;

public class OCMapTest {

  public static void main(String[] args) {
    ObjectCacheReader<Map<String, User>> reader = new ObjectCacheReader<>(User.class.getSimpleName());

    System.out.print("Testing reading Map of objects: ");
    Map<String, User> cached = reader.object();
    assert (cached.size()==4) : "Expected 4 users, found " + cached.size();
    
    System.out.println("OK");
  }
}
