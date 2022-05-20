package butterknife.internal;

import java.util.AbstractList;
import java.util.RandomAccess;

final class ImmutableList<T> extends AbstractList<T> implements RandomAccess {
  private final T[] views;
  
  ImmutableList(T[] paramArrayOfT) {
    this.views = paramArrayOfT;
  }
  
  public boolean contains(Object paramObject) {
    boolean bool = false;
    T[] arrayOfT = this.views;
    int i = arrayOfT.length;
    for (byte b = 0;; b++) {
      boolean bool1 = bool;
      if (b < i) {
        if (arrayOfT[b] == paramObject)
          return true; 
      } else {
        return bool1;
      } 
    } 
  }
  
  public T get(int paramInt) {
    return this.views[paramInt];
  }
  
  public int size() {
    return this.views.length;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/butterknife/internal/ImmutableList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */