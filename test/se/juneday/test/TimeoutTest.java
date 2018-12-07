package se.juneday.test;

import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;

import se.juneday.ObjectCache;
import se.juneday.test.User;

import java.util.concurrent.TimeUnit;

public class TimeoutTest {

  public static void main(String[] args) {
    //    ArrayList<User> list = new ArrayList<>();
    ObjectCache<Collection<User>> cache = new ObjectCache<>(User.class);

    System.out.print("Testing creating objects: ");
    Collection<User> users = new ArrayList<>();
    
    users.add(new User("Henrik Sandklef", "hesa@sandklef.com"));
    users.add(new User("Rikard Fröberg", "rille@rillefroberg.se"));
    users.add(new User("Edson", "edson@brazil.com"));
    users.add(new User("diego", "maradona@argentina.com"));
    assert (users.size()==4) : "Expected 2 users, found " + users.size();
    
    // Add the users created above. Now the users are in RAM
    cache.storeObject(users);
    // Store the users set above to file. Now the users are serialized to file
    assert (cache.readObject().size()==4) : "Expected 4 users, found " + cache.readObject().size();
    System.out.println("OK");

    System.out.println(" * Setting time to 2 seconds");
    cache.timeout(2000);  // 2 second
    System.out.println(" * Sleep 1 second ");
    try {
      TimeUnit.SECONDS.sleep(1); 
      System.out.print("Testing list size is 4: ");
      assert (cache.readObject().size()==4) : "Expected 4 users, found " + cache.readObject().size();
      System.out.println("OK");
      
      System.out.println(" * Sleep 2 seconds to cause cache timeout");
      TimeUnit.SECONDS.sleep(2);
      System.out.print("Testing list size is 0 (timout expired): ");
      assert (cache.readObject()==null) : "Expected null cache: got " + cache ;
      System.out.println("OK");
    } catch (Exception e) {
      System.out.println("Timeout check failed");
      assert false ;
    }

    
    // Clear the cache
    cache.clear();
    // check cache has 0 elements
    assert (cache.readObject()==null) : "Expected cache null";

  }
}
