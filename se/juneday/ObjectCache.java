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

/**
 * Represents a cache for objects.
 * Uses Serializable and a file.
 *
 */
public class ObjectCache<T> {

  private Collection<T> objects;
  private long localCacheDate ;

  private String cacheFileName;
  private final static long maxDiff = (60 * 60 * 1000);

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
    localCacheDate = 0;
    cacheFileName = clazz.getSimpleName() + "_serialized.data";
  }

  /**
   * Creates a new ObjectCache instance.
   *
   * @param fileName A String used to name the cache file. The string
   * "_serialized.data" is added to the parameter.
   */ 
  public ObjectCache(String fileName) {
    localCacheDate = 0;
    cacheFileName = fileName + "_serialized.data";
  }

  /**
   * Sets a list of objects to cache (in RAM). This does not store the
   * objects to file.
   *
   * @param objects The objects to cache
   */ 
  private void set(Collection<T> objects) {
    this.objects = objects;
    localCacheDate = System.currentTimeMillis();
  }

  /**
   * Sets one object to cache (in RAM). This does not store the
   * object to file.
   *
   * @param object The object to cache
   */ 
  private void setSingle(T object) {
    Collection<T> objectList = new ArrayList<>();
    objectList.add(object);
    this.objects = objectList;
  }


  /**
   * Stores the list of cached (in RAM) objects to a file.
   *
   */ 
  private void push() {
    FileOutputStream fos = null;
    ObjectOutputStream out = null;
    try {
      fos = new FileOutputStream(cacheFileName);
      out = new ObjectOutputStream(fos);
      out.writeObject(objects);      
      out.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }



  /**
   * Retrieves (and returns) the list of stored objects from
   * file. These objects are now stored in RAM.
   *
   * @return The list of objects read from file.
   */ 
  @SuppressWarnings("unchecked")
  private Collection<T> pull() {
    File f = new File(cacheFileName);
    if (!f.exists()) {
      System.err.println("missing cache file: " + f);
      return null;
    }
    long diff =
      System.currentTimeMillis() - f.lastModified();

    if (diff>maxDiff) {
      System.err.println("cache timed out for objects");
      return null;
    }
    FileInputStream fis = null;
    ObjectInputStream in = null;
    Collection<T> tmpObjects;
    try {
      fis = new FileInputStream(cacheFileName);
      in = new ObjectInputStream(fis);
      tmpObjects = (Collection<T>) in.readObject();
      in.close();
      objects = tmpObjects;
    } catch (ClassNotFoundException ex) {
      //      ex.printStackTrace();
      System.err.println("\n***Failed reading objects from " + cacheFileName + " ***") ;
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
    return objects;
  }

  /**
   * Gets a list of cached objects (from RAM). This does not read the
   * objects from file.
   *
   * @return The list of cached objects 
   */ 
  private Collection<T> get() {
    return objects;
  }

  /**
   * Gets the cached object (from RAM). This does not read the object
   * from file.
   *
   * @return The cached object
   */ 
  private T getSingle() {
    if (objects == null) {
      return null;
    }
    Iterator<T> iter = objects.iterator();
    if ( iter.hasNext() ) {
      return iter.next();
    }
    return null;
  }

  /**
   * Gets the cached object (from RAM). This does not read the object
   * from file.
   *
   * @return Number of cached objects, -1 if nothing cached.
   */ 
  public int size() {
    if (objects == null) {
      return -1;
    }
    return objects.size();
  }

  /**
   * Store (cache) objects in the cache
   *
   * @param repos - the objects to cache
   */ 
  public void storeObjects(Collection<T> repos) {
    set(repos);
    push();
  }

  /**
   * Store (cache) one single object in the cache
   *
   * @param repo - the object to cache
   */ 
  public void storeObject(T repo) {
    setSingle(repo);
    push();
  }

  /**
   * Read (cached) objects from the cache
   *
   * @return the cached objects - if no object has been cached, an empty list is returned
   */ 
  public Collection<T> readObjects() {
    pull();

    Collection<T> cachedObjects = get();
    if (cachedObjects!=null) {
      return cachedObjects;
    }
    return new ArrayList<>();
  }

  /**
   * Read one single (cached) object from the cache
   *
   * @return the cached object - if no object has been cached, null is returned
   */ 
  public T readObject() {
    pull();

    return getSingle();
  }

  /**
   * Clears the cache
   *
   */ 
  public void clear() {
    set(new ArrayList<T>());
  }

}
