# ObjectCache - cache your objects between sessions

**Ever wanted to cache objects between session of your program or Android app?**

Well, we sure have. And our students have.

We've seen a couple of frameworks to solve caching of objects. The
overhead of many of these are often too high to make them
understandable and in some cases useful for students. Perhaps using a
database is the proper way of doing this but we feel this is way too
much overhead.

Here's a minimal approach to cache objects (either many object in a
List or one single object).

# Requirements

## Build requirements

* *```make```* - we use make to build ObjectCache so you need to install it, unless you want to compile yourself.

### Build reuirements for Android

* *```android-stubs```* - if you want to use ObjectCache in Android
we've written a small helper class to ease up life for you. The tests
for this class need ```android-stubs``` which you can find here:
[android-stubs](https://github.com/progund/android-stubs). we have a
make target called ```download-dependencies``` for downloading this
software. To download and use android-stubs type

~~~
    make download-dependencies
~~~

## Requirements in your classes (to be cached)

Let's say you want to cache objects of your super awesome class ```User```:

~~~
  public class User {
    private String name;
    private String email;

    ....
~~~



### Fixing the User class (above)

You need to add a ```serialVersionUID```. Let's use ```1L```.

~~~
  private static final long serialVersionUID = 1L;
~~~

*Note: check out the text about serialization below. ```1L``` is not a good value. It is used as an example here.*

So the class, ```User```, looks like this:

~~~
    public class User implements Serializable {
        private String name;
        private String email;

       private static final long serialVersionUID = 1L;
~~~

etc etc

# Using ObjectCache - a quick guide

## Example class to cache

In this example we'll (again) be looking at a class called ```User``` representing a user in some system:

~~~
  public class User implements Serializable {
    private String name;
    private String email;

    ....
~~~

## Use ObjectCache in Java

Create an ObjectCache object, including the type of the objects to cache:

~~~
    ObjectCache<User> cache = new ObjectCache<>(User.class);
~~~

### Single object

Store a single object in cache:

~~~
    User u = new User("Henrik Sandklef", "hesa@henriksandklef.com")
    cache.storeObject(u);
~~~

Read object from cache:

~~~
    User cachedUser = cache.readObject();
~~~

### List of objects

Store a ```List``` of objects in cache:

~~~
    List<User> users = new ArrayList<>();
    users.add(new User("Henrik Sandklef", "hesa@henriksandklef.com"));
    users.add(new User("Rikard Fröberg", "rille@rillefroberg.se"));
    cache.storeObjects(users);
~~~

Read objects from cache:

~~~
    List<User> cached = cache.readObjects();
~~~

## Use ObjectCache in Android

### Create an ObjectCache in Android

Creating an ObjectCache is the only thing that differs between normal
Java and Android. The reason is that we need to make sure that we're
allowed to write in the directory to store the cache file.

* reate an ObjectCache object, including the type of the objects to cache:

~~~
    String fileName =
        AndroidObjectCacheHelper.objectCacheFileName(context, User.class);
    ObjectCache<User> cache = new ObjectCache<>(fileName);
~~~

# Source code and examples

**TODO:** Download the source from here: [java-extra-lectures](https://github.com/progund/java-extra-lectures). In the directory ```caching``` you'll find the ObjectCache source code (in the ```se/juneday/``` folder) and some test code. To build ObjectCache (and javadoc) and test it:

# Developing #

```make``` - builds ObjectCache

```make test``` - tests ObjectCache

```make jar``` - create a jar file (for release)

```make android-jar``` - create an android jar file (for release)

# Tools using ObjectCache # 

## ADHD

Check out [ADHD](https://github.com/progund/adhd), a tool with which
you can read out database and serialized files (ObjectCache) from an
Android device and create text and html from that. This is useful if
you want to see what's in your app's database etc on the Android
device.

# A short introduction to serialization

We rely completely on [Serializable Objects](https://docs.oracle.com/javase/tutorial/jndi/objects/serial.html). To get your class Serializable you need to (read this link: [Interface Serializable](https://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html)). To make things short, you did read the link didn't you?, we provide some text about serialVersionUID here: 

~~~
   The serialization runtime associates with each serializable class a version number, called a serialVersionUID, which is used during deserialization to verify that the sender and receiver of a serialized object have loaded classes for that object that are compatible with respect to serialization. If the receiver has loaded a class for the object that has a different serialVersionUID than that of the corresponding sender's class, then deserialization will result in an InvalidClassException. A serializable class can declare its own serialVersionUID explicitly by declaring a field named "serialVersionUID" that must be static, final, and of type long:

   ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;
~~~

The above is copied from: [Serializable Objects](https://docs.oracle.com/javase/tutorial/jndi/objects/serial.html)

*Note: if you feel like reading our opinion on inheritance: [Inheritance - Problems with inheritance](http://wiki.juneday.se/mediawiki/index.php/Chapter:Inheritance_-_Problems_with_inheritance)*


# Links

* [Serializable Objects](https://docs.oracle.com/javase/tutorial/jndi/objects/serial.html)
* [Generic Types](https://docs.oracle.com/javase/tutorial/java/generics/types.html)
