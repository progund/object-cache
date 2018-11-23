package se.juneday;

import java.util.Collection;
import java.io.File;

public class ObjectCacheReader<T> {

  ObjectCache<T> cache;


  /**
   * Creates a new ObjectCacheReader instance.
   *
   * @param fileName A String used to pass to the ObjectCache constructor.
   */ 
  public ObjectCacheReader(String fileName) {
    cache = new ObjectCache<>(fileName);
  }

  /**
   * Retrieves and prints the list of stored objects from
   * file. 
   *
   */ 
  public void printObjects() {
    Collection<T> objects = objects();

    if (objects!=null) {
      for (T o : objects) {
        System.out.println(" * " + o );
      }
    }
  }

  /**
   * Retrieves and returns the list of stored objects from
   * file. 
   *
   */ 
  public Collection<T> objects() {
      return cache.readObjects();
  }

  /**
   * Retrieves and returns the stored object from
   * file. 
   *
   */ 
  public T object() {
      return cache.readObject();
  }

  /**
   * Program to read and print Serialized objects
   *
   * Starting the program with the command line arguments "--test" is
   * a way for you to test if the ObjectCache classes can be
   * found. This is useful for softwares such as ADHD
   * (https://github.com/progund/adhd)
   *
   * @param args Either a file or "--test"
   */ 
  public static void main(String args[]) {
    if (args.length==0) {
      System.err.println("Missing argument (filename)");
      System.exit(1);
    }

    if (args[0].equals("--test")) {
      System.exit(0);
    }
    
    ObjectCacheReader<Object> ocr = new ObjectCacheReader<>(args[0]);
    ocr.printObjects();
  }
  
}
