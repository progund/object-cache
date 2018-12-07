package se.juneday.test;

import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;

import se.juneday.ObjectCache;
import se.juneday.test.User;

import java.util.concurrent.TimeUnit;



public class TimeoutTest {

  public static void main(String[] args) {
    ObjectCache<User> cache = new ObjectCache<>(User.class);

    System.out.print("Testing creating objects: ");
    Collection<User> users = new ArrayList<>();
    
    users.add(new User("Henrik Sandklef", "hesa@sandklef.com"));
    users.add(new User("Rikard Fröberg", "rille@rillefroberg.se"));
    users.add(new User("Edson", "edson@brazil.com"));
    users.add(new User("diego", "maradona@argentina.com"));
    assert (users.size()==4) : "Expected 2 users, found " + users.size();
    
    // Add the users created above. Now the users are in RAM
    cache.storeObjects(users);
    // Store the users set above to file. Now the users are serialized to file
    assert (cache.size()==4) : "Expected 4 users, found " + cache.size();
    System.out.println("OK");

    System.out.println("Setting time to 2 seconds");
    cache.timeout(2000);  // 2 second
    System.out.println("Sleep 1 second ");
    try {
      TimeUnit.SECONDS.sleep(1); 
      System.out.print("Testing list size is 4: ");
      assert (cache.size()==4) : "Expected 4 users, found " + cache.size();
      System.out.println("OK");
      
      System.out.println("Sleep 2 seconds ");
      TimeUnit.SECONDS.sleep(2);
      System.out.print("Testing list size is 0 (timout expired): ");
      assert (cache.size()==0) : "Expected 0 users, found " + cache.size();
      System.out.println("OK");
    } catch (Exception e) {
      System.out.println("Timeout failed");
      assert false ;
    }

    
    // Clear the cache
    cache.clear();
    // check cache has 0 elements
    assert (cache.size()==0) : "Expected 0 users, found " + cache.size();

  }
}
