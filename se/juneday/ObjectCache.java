package se.juneday;

import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.InvalidClassException;

/**
 * Represents a cache for object.
 * Uses Serializable and a file.
 *
 */
public class ObjectCache<T> {
  
  private T object;

  private File cacheFile;
  private String cacheFileName;
  private long maxDiff = (60 * 60 * 1000);
  private long cacheTime;
  
  static enum ExitStatus {
    OC_OK(0),
    OC_CLASS_NOT_FOUND(1);
    
    ExitStatus(int status) {
      this.status = status;
    }
    private int status;
    int status() {
      return status;
    }
    
  }

  /**
   * Creates a new ObjectCache instance.
   *
   * @param clazz A Class object. Used to name the cache file.
   */ 
  public ObjectCache(Class clazz) {
    if (! (clazz instanceof Serializable)) {
      throw new IllegalArgumentException("Class \"" + clazz.getName() + "\" does not implement Serializable");
    }
    cacheFileName = clazz.getSimpleName() + "_serialized.data";
  }
  
  /**
   * Creates a new ObjectCache instance.
   *
   * @param fileName A String used to name the cache file. The string
   * "_serialized.data" is added to the parameter.
   */ 
  public ObjectCache(String fileName) {
    cacheFileName = fileName + "_serialized.data";
  }


  public boolean valid() {
    // System.err.println("valid() ? cacheTime: " + cacheTime);
    long diff =
      System.currentTimeMillis() - cacheTime;
    
    // System.err.println("Cache valid? " +
    //                    diff + " / " + maxDiff + " / " + cacheTime +
    //                    "  ====> " +
    //                    ( ( maxDiff == 0 ) || (diff<=maxDiff) ));
    
    // If non eternal timeout and
    // timeout expired, clear cache and return null;
    return ( ( maxDiff == 0 ) || (diff<=maxDiff) );
  }
  
  private void validateCache() {
    if ( ! valid() ) {
      clear();
    }
  }
  
  
  /**
   * Sets an object to cache (in RAM). This does not store the
   * object to file.
   *
   * @param object The object to cache
   */ 
  private void set(T object) {
    // System.err.println("set(object)");
    this.object = object;
  }

  /**
   * Stores the list of cached (in RAM) object to a file.
   *
   */ 
  private void push() {
    // System.err.println("push()");
    cacheFile = null;
    FileOutputStream fos = null;
    ObjectOutputStream out = null;
    try {
      fos = new FileOutputStream(cacheFileName);
      out = new ObjectOutputStream(fos);
      out.writeObject(object);      
      out.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }



  /**
   * Retrieves (and returns) the stored object from
   * file. These object are now stored in RAM.
   *
   * @return The object read from file.
   */ 
  @SuppressWarnings("unchecked")
  private T pull() {
    // System.err.println(" ** pull **") ;    

    cacheFile = new File(cacheFileName);
    // file null or no file
    if ( (cacheFile==null) ||
         (!cacheFile.exists() )) {
      // System.err.println("missing cache file: " + cacheFile);
      clear();
      return object;
    }

    cacheTime = cacheFile.lastModified();
    
    // invalid (old) file 
    if ( ! valid() ) {
      // System.err.println(" INVALID OLD CACHE FILE");
      clear();
      return object;
    }
    
    FileInputStream fis = null;
    ObjectInputStream in = null;
    T tmpObject;
    try {
      fis = new FileInputStream(cacheFileName);
      in = new ObjectInputStream(fis);
      tmpObject = (T) in.readObject();
      in.close();
      object = tmpObject;
    } catch (InvalidClassException ex) {
      System.err.println("\n***Failed reading object from " + cacheFileName + " ***") ;
      System.err.print("This most likely is because one (or many) of your classes have been changed since the cache was written");
      object = null;
    } catch (ClassNotFoundException ex) {
      //      ex.printStackTrace();
      System.err.println("\n***Failed reading object from " + cacheFileName + " ***") ;
      //      StackTraceElement[] elements = ex.getStackTrace();
      String missingClassName = ex.getMessage();
      System.err.print("This most likely is because one (or many) of your classes can't be loaded.");
      System.err.println(" Suspected missing class: " + missingClassName);
      System.err.println("");
      System.err.println("Make sure that the class \"" + missingClassName + "\" is compiled and its coresponding class file can be found via your CLASSPATH");
      System.exit(ExitStatus.OC_CLASS_NOT_FOUND.status());
    } catch (Throwable ex) {
      ex.printStackTrace();
      return null;
    }

    cacheTime = System.currentTimeMillis();
    
    return object;
  }

  /**
   * Gets the cached object (from RAM). This does not read the
   * object from file.
   *
   * @return The cached object
   */ 
  private T get() {
    validateCache();
    return object;
  }

  /* ---------------------------- */
  
  /**
   * Store (cache) object in the cache
   *
   * @param object - the object to cache
   */ 
  public void storeObject(T object) {
    // System.err.println("storeObject()");
    set(object);
    push();
    cacheTime = System.currentTimeMillis();
  }

  /**
   * Set timeout (milliseconds)
   *
   * @param t - timeout to use
   */ 
  public void timeout(long t) {
    if ( t < 0 ) {
      throw new IllegalArgumentException("Timeout can't be set to lezz than zero. " + t + " not valid");
    }
    maxDiff = t;
  }

  /**
   * Get timeout (milliseconds)
   *
   * @return timeout used
   */ 
  public long timeout() {
    return t;
  }

  /**
   * Get cache time
   *
   * @return time (millisecs) when cache was written
   */ 
  public long cacheTime() {
    return cacheTime;
  }

  /**
   * Get remainging cache time
   *
   * @return time (millisecs) until cache expires (negative if expired or eternal)
   */ 
  public long cacheExpirationTime() {
    if (maxDiff==0) {
      retrun -1;
    }
    return System.currentTimeMillis() - cacheTime;
  }

  /**
   * Read one single (cached) object from the cache
   *
   * @return the cached object - if no object has been cached, null is returned
   */ 
  public T readObject() {
    pull();
    return get();
  }

  /**
   * Clears the cache
   *
   */ 
  public void clear() {
    cacheTime = 0;
    set(null);
  }

}
