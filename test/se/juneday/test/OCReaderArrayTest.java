package se.juneday.test;

import java.io.Serializable;
import java.util.Collection;

import se.juneday.ObjectCacheReader;
import se.juneday.test.User;

public class OCReaderArrayTest {

  public static void main(String[] args) {
    ObjectCacheReader<User[]> reader = new ObjectCacheReader<>(User.class.getSimpleName());

    System.out.print("Testing reading objects (Array): ");
    User[] cached = reader.object();
    assert (cached.length==4) : "Expected 4 users, found " + cached.length;
    
    System.out.println("OK");


    reader.printObject();
  }
}
