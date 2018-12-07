package se.juneday.test;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import se.juneday.ObjectCache;
import se.juneday.test.User;

public class ObjectCacheStoreManyTest {

  public static void main(String[] args) {
    ObjectCache<Map<User>> cache = new ObjectCache<>(User.class);

    System.out.print("Testing creating objects: ");
    Map<User> users = new HashMap<>();
    
    users.put("henrik", new User("Henrik Sandklef", "hesa@sandklef.com"));
    users.put("rikard", new User("Rikard Fröberg", "rille@rillefroberg.se"));
    assert (users.size()==2) : "Expected 2 users, found " + users.size();
    
    // Add the users created above. Now the users are in RAM
    cache.storeObject(users);
    // Store the users set above to file. Now the users are serialized to file
    assert (cache.readObject().size()==2) : "Expected 2 users, found " + cache.readObject().size();
    
    System.out.println("OK");
  }
}
