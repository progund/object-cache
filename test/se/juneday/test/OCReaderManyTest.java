package se.juneday.test;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import se.juneday.ObjectCacheReader;
import se.juneday.test.User;

public class OCReaderManyTest {

  public static void main(String[] args) {
    ObjectCacheReader<User> reader = new ObjectCacheReader<>(User.class.getSimpleName());

    System.out.print("Testing reading objects: ");
    List<User> cached = reader.objects();
    assert (cached.size()==2) : "Expected 2 users, found " + cached.size();
    
    System.out.println("OK");
  }
}
