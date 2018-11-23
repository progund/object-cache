package se.juneday.test;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import se.juneday.ObjectCacheReader;
import se.juneday.test.User;

public class OCReaderSingleTest {

  public static void main(String[] args) {
    ObjectCacheReader<User> reader =
      new ObjectCacheReader<>(User.class.getSimpleName());
    
    System.out.print("Testing reading objects: ");
    User cached = reader.object();
    assert (cached.equals("Henrik Sandklef")) : "Expected Henrik Sandklef, found " + cached.name();
    
    System.out.println("OK");
  }
}
