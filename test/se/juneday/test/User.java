package se.juneday.test;

import java.io.Serializable;

public class User implements Serializable {
  String name;
  String email;

  // Generated using serialver
  // $ serialver se.juneday.test.User
  private static final long serialVersionUID = 4554358036161471220L;
  
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
