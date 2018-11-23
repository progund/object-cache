package se.juneday.test;

import android.content.Context;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import se.juneday.ObjectCache;
import se.juneday.AndroidObjectCacheHelper;
import se.juneday.test.User;

public class AndroidObjectCacheHelperTest {

  public static void main(String[] args) {

    try {
      Class clazz = User.class;
      System.out.print("Test file name from valid context: ");
      String fileName =
        AndroidObjectCacheHelper.objectCacheFileName(new Context(), clazz);
      assert (fileName.equals("faked-data-dir/" + clazz.getName())) : "file name " + fileName + " faulty";
      System.out.println("OK");

      System.out.print("Creating ObjectCache object from file name: ");
      ObjectCache<User> cache = new ObjectCache<>(fileName);
      System.out.println("OK");
      

      System.out.print("Test file name from invalid context: ");
      fileName =
        AndroidObjectCacheHelper.objectCacheFileName(null, User.class);
      assert (fileName.equals("faked-data-dir/" + clazz.getName())) : "file name " + fileName + " faulty";
      System.out.println("file name " + fileName + " faulty");
      System.exit(2);
    } catch (Exception e) {
      System.out.println("OK");
    }

      
    
  }
  
}
