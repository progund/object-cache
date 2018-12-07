package se.juneday.test;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import se.juneday.ObjectCache;
import se.juneday.test.User;

public class ObjectCacheArrayTest {

  public static void main(String[] args) {
    ObjectCache<User[]> cache = new ObjectCache<>(User.class);

    System.out.print("Testing creating Map of objects: ");
    User[] users = new User[4];
    
    users[0] = new User("Henrik Sandklef", "hesa@sandklef.com");
    users[1] = new User("Rikard Fröberg", "rille@rillefroberg.se");
    users[2] = new User("Rikard Fröberg", "rille@rillefroberg.se");
    users[3] = new User("Rikard Fröberg", "rille@rillefroberg.se");
    assert (users.length == 4) : "Expected 4 users, found " + users.length;
    
    // Add the users created above. Now the users are in RAM
    cache.storeObject(users);
    // Store the users set above to file. Now the users are serialized to file
    assert (cache.readObject().length==4) : "Expected 4 users, found " + cache.readObject().length;
    
    System.out.println("OK");
  }
}
