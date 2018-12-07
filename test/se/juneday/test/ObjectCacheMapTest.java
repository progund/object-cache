package se.juneday.test;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import se.juneday.ObjectCache;
import se.juneday.test.User;

public class ObjectCacheMapTest {

  public static void main(String[] args) {
    ObjectCache<Map<String,User>> cache = new ObjectCache<>(User.class);

    System.out.print("Testing creating Map of objects: ");
    Map<String, User> users = new HashMap<>();
    
    users.put("henrik", new User("Henrik Sandklef", "hesa@sandklef.com"));
    users.put("rikard", new User("Rikard Fröberg", "rille@rillefroberg.se"));
    users.put("rikard1", new User("Rikard Fröberg", "rille@rillefroberg.se"));
    users.put("rikard2", new User("Rikard Fröberg", "rille@rillefroberg.se"));
    assert (users.size()==4) : "Expected 4 users, found " + users.size();
    
    // Add the users created above. Now the users are in RAM
    cache.storeObject(users);
    // Store the users set above to file. Now the users are serialized to file
    assert (cache.readObject().size()==4) : "Expected 4 users, found " + cache.readObject().size();
    
    System.out.println("OK");
  }
}
