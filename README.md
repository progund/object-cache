# ObjectCache - cache your objects between sessions

**Ever wanted to cache objects between session of your program or Android app?**

Well, we sure have. And our students have.

We've seen a couple of frameworks to solve caching of objects. The
overhead of many of these are often too high to make them
understandable and in some cases useful for students. Perhaps using a
database is the proper way of doing this but we feel this is way too
much overhead.

Here's a minimal approach to cache objects (either many object in a
Collection or one single object).

# Supported classes

All classes (and the classes it is composed of recusrively) implementing Serializable

# Requirements

## Build requirements

* *```make```* - we use make to build ObjectCache so you need to install it, unless you want to compile yourself.

### Build requirements for Android

* *```android-stubs```* - we've written a small helper class to ease
up life if you want to use ObjectCache in Android. The tests for this
class need ```android-stubs``` which you can find here:
[android-stubs](https://github.com/progund/android-stubs).

You can either download android-stubs yourself and put the jar file in
directory called libs or use a make target called
```download-dependencies``` for downloading this software. To download
and use android-stubs type

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

You need to add a ```serialVersionUID```. Let's use ```1L``` for just a short while. 

~~~
  private static final long serialVersionUID = 1L;
~~~

Check out the text about serialization below and you'll understand
that ```1L``` is not a good value. We need to generate a value, using
```serialver``` that comes with your Java development kit. Compile the
User class and run ```serialver```:

~~~
$ javac se/juneday/test/User.java
$ $ serialver se.juneday.test.User
se.juneday.test.User:    private static final long serialVersionUID = 4554358036161471220L;
~~~

Copy this part ```private static final long serialVersionUID =
4554358036161471220L;``` in to the User class. It should now look
something like:

~~~
package se.juneday.test;

import java.io.Serializable;

public class User implements Serializable {
  String name;
  String email;

  private static final long serialVersionUID = 4554358036161471220L;

  ....
~~~



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

### Single object

Create an ObjectCache object, including the type of the objects to cache, let's say we want to store a single User:

~~~
    ObjectCache<User> cache = new ObjectCache<>(User.class);
~~~

Store a single object in cache:

~~~
    User u = new User("Henrik Sandklef", "hesa@henriksandklef.com")
    cache.storeObject(u);
~~~

Read object from cache:

~~~
    User cachedUser = cache.readObject();
~~~

### Collection of objects

Create an ObjectCache object, including the type of the objects to cache, let's say we want to store a Collections of User(s):

~~~
    ObjectCache<Collection<User>> cache = new ObjectCache<>(User.class);
~~~

Store a ```Collection``` (e g ArralyList) of objects in cache:

~~~
    Collection<User> users = new ArrayList<>();
    users.add(new User("Henrik Sandklef", "hesa@henriksandklef.com"));
    users.add(new User("Rikard Fröberg", "rille@rillefroberg.se"));
    cache.storeObjects(users);
~~~

Read objects from cache:

~~~
    Collection<User> cached = cache.readObjects();
~~~

## Clear Cache

To clear the cache you simple invoke the ```clear()``` method.

~~~
    cache.clear();
~~~

## Use ObjectCache in Android

We have a more complete guide on how to use ObjectCache in Android, check out: https://github.com/progund/object-cache/blob/master/doc/README-ANDROID.md

### Create an ObjectCache in Android

Creating an ObjectCache instance is the only thing that differs
between normal Java and Android. The reason is that we need to make
sure that we're allowed to write in the directory to store the cache
file.

Create an ObjectCache object, including the type of the objects to cache:

~~~
    String fileName =
        AndroidObjectCacheHelper.objectCacheFileName(context, User.class);
    ObjectCache<Collection<User>> cache = new ObjectCache<>(fileName);
~~~

# Download ObjectCache

## Released jar files

Go to (ObjectCache)[https://github.com/progund/object-cache] and click releases. Aim for the latest jar file (of the latest release) and download it. We download using ```curl``` so:

~~~
curl -LJO https://github.com/progund/object-cache/releases/download/0.1/object-cache-0.1.91.jar
~~~

**Note: the curl command line above downloads version 0.1.91 (0.2 pre-release). You need to change to URL to reflect the version you want to download.**

## Source code 

Go to (ObjectCache)[https://github.com/progund/object-cache], click
```Clone or download``` and copy the link ```Download ZIP```. The link
is ```https://github.com/progund/object-cache/archive/master.zip```.

We're using ```curl``` download - you can use the browser if you want to:

~~~
$ curl -LJO https://github.com/progund/object-cache/archive/master.zip
$ unzip master.zip
~~~

There's a makefile you can if you want to modify ObjectCache. Here are the most useful targets:

```make``` - builds ObjectCache

```make test``` - tests ObjectCache

```make jar``` - create a jar file (for release)

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
