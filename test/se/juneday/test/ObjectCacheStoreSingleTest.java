package se.juneday.test;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import se.juneday.ObjectCache;
import se.juneday.test.User;

public class ObjectCacheStoreSingleTest {

  public static void main(String[] args) {
    ObjectCache<User> cache = new ObjectCache<>(User.class);

    System.out.print("Testing creating object: ");

    // Add the users created above. Now the users are in RAM
    cache.storeObject(new User("Henrik Sandklef", "hesa@sandklef.com"));
    // Store the users set above to file. Now the users are serialized to file
    assert (cache.readObject().name().equals("Henrik Sandklef")) : "Expected Henrik Sandklef users, found " + cache.readObject().name();
    
    System.out.println("OK");
  }
}
