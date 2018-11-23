package se.juneday.test;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import se.juneday.ObjectCache;
import se.juneday.test.User;

public class ObjectCacheStoreManyTest {

  public static void main(String[] args) {
    ObjectCache<User> cache = new ObjectCache<>(User.class);

    System.out.print("Testing creating objects: ");
    List<User> users = new ArrayList<>();

    users.add(new User("Henrik Sandklef", "hesa@sandklef.com"));
    users.add(new User("Rikard Fröberg", "rille@rillefroberg.se"));
    assert (users.size()==2) : "Expected 2 users, found " + users.size();
    
    // Add the users created above. Now the users are in RAM
    cache.storeObjects(users);
    // Store the users set above to file. Now the users are serialized to file
    assert (cache.size()==2) : "Expected 2 users, found " + cache.size();
    
    System.out.println("OK");
  }
}
