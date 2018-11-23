package se.juneday.test;

import java.io.Serializable;

public class User implements Serializable {
  String name;
  String email;

  private static final long serialVersionUID = 1L;

  public User(String name, String email) {
    this.name=name;
    this.email=email;
  }

  public String toString() {
    return "\"" + name + "\""+ (email!=null?"<" + email + ">":"");
  }

  public String name() {
    return name;
  }

}
