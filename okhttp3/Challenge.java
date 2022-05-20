package okhttp3;

import javax.annotation.Nullable;

public final class Challenge {
  private final String realm;
  
  private final String scheme;
  
  public Challenge(String paramString1, String paramString2) {
    if (paramString1 == null)
      throw new NullPointerException("scheme == null"); 
    if (paramString2 == null)
      throw new NullPointerException("realm == null"); 
    this.scheme = paramString1;
    this.realm = paramString2;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    return (paramObject instanceof Challenge && ((Challenge)paramObject).scheme.equals(this.scheme) && ((Challenge)paramObject).realm.equals(this.realm));
  }
  
  public int hashCode() {
    return (this.realm.hashCode() + 899) * 31 + this.scheme.hashCode();
  }
  
  public String realm() {
    return this.realm;
  }
  
  public String scheme() {
    return this.scheme;
  }
  
  public String toString() {
    return this.scheme + " realm=\"" + this.realm + "\"";
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Challenge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */