package org.greenrobot.greendao.identityscope;

public enum IdentityScopeType {
  None, Session;
  
  static {
    None = new IdentityScopeType("None", 1);
    $VALUES = new IdentityScopeType[] { Session, None };
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/identityscope/IdentityScopeType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */