package app.gamer.quadstellar.domain;

import java.io.Serializable;

public class WifiMessageInfo implements Serializable {
  public String name;
  
  public String passWord;
  
  public boolean equals(Object paramObject) {
    return this.name.equals(((WifiMessageInfo)paramObject).name);
  }
  
  public String toString() {
    return "WifiMessageInfo{, name='" + this.name + '\'' + ", passWord='" + this.passWord + '\'' + '}';
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/WifiMessageInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */