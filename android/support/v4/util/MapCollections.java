package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class MapCollections<K, V> {
  EntrySet mEntrySet;
  
  KeySet mKeySet;
  
  ValuesCollection mValues;
  
  public static <K, V> boolean containsAllHelper(Map<K, V> paramMap, Collection<?> paramCollection) {
    // Byte code:
    //   0: aload_1
    //   1: invokeinterface iterator : ()Ljava/util/Iterator;
    //   6: astore_1
    //   7: aload_1
    //   8: invokeinterface hasNext : ()Z
    //   13: ifeq -> 35
    //   16: aload_0
    //   17: aload_1
    //   18: invokeinterface next : ()Ljava/lang/Object;
    //   23: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   28: ifne -> 7
    //   31: iconst_0
    //   32: istore_2
    //   33: iload_2
    //   34: ireturn
    //   35: iconst_1
    //   36: istore_2
    //   37: goto -> 33
  }
  
  public static <T> boolean equalsSetHelper(Set<T> paramSet, Object paramObject) {
    boolean bool = true;
    boolean bool1 = false;
    if (paramSet == paramObject)
      return true; 
    boolean bool2 = bool1;
    if (paramObject instanceof Set) {
      paramObject = paramObject;
      try {
        if (paramSet.size() == paramObject.size()) {
          bool2 = paramSet.containsAll((Collection<?>)paramObject);
          if (bool2)
            return bool; 
        } 
        bool2 = false;
      } catch (NullPointerException nullPointerException) {
        bool2 = bool1;
      } catch (ClassCastException classCastException) {
        bool2 = bool1;
      } 
    } 
    return bool2;
  }
  
  public static <K, V> boolean removeAllHelper(Map<K, V> paramMap, Collection<?> paramCollection) {
    int i = paramMap.size();
    Iterator<?> iterator = paramCollection.iterator();
    while (iterator.hasNext())
      paramMap.remove(iterator.next()); 
    return (i != paramMap.size());
  }
  
  public static <K, V> boolean retainAllHelper(Map<K, V> paramMap, Collection<?> paramCollection) {
    int i = paramMap.size();
    Iterator iterator = paramMap.keySet().iterator();
    while (iterator.hasNext()) {
      if (!paramCollection.contains(iterator.next()))
        iterator.remove(); 
    } 
    return (i != paramMap.size());
  }
  
  protected abstract void colClear();
  
  protected abstract Object colGetEntry(int paramInt1, int paramInt2);
  
  protected abstract Map<K, V> colGetMap();
  
  protected abstract int colGetSize();
  
  protected abstract int colIndexOfKey(Object paramObject);
  
  protected abstract int colIndexOfValue(Object paramObject);
  
  protected abstract void colPut(K paramK, V paramV);
  
  protected abstract void colRemoveAt(int paramInt);
  
  protected abstract V colSetValue(int paramInt, V paramV);
  
  public Set<Map.Entry<K, V>> getEntrySet() {
    if (this.mEntrySet == null)
      this.mEntrySet = new EntrySet(); 
    return this.mEntrySet;
  }
  
  public Set<K> getKeySet() {
    if (this.mKeySet == null)
      this.mKeySet = new KeySet(); 
    return this.mKeySet;
  }
  
  public Collection<V> getValues() {
    if (this.mValues == null)
      this.mValues = new ValuesCollection(); 
    return this.mValues;
  }
  
  public Object[] toArrayHelper(int paramInt) {
    int i = colGetSize();
    Object[] arrayOfObject = new Object[i];
    for (byte b = 0; b < i; b++)
      arrayOfObject[b] = colGetEntry(b, paramInt); 
    return arrayOfObject;
  }
  
  public <T> T[] toArrayHelper(T[] paramArrayOfT, int paramInt) {
    int i = colGetSize();
    T[] arrayOfT = paramArrayOfT;
    if (paramArrayOfT.length < i)
      arrayOfT = (T[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i); 
    for (byte b = 0; b < i; b++)
      arrayOfT[b] = (T)colGetEntry(b, paramInt); 
    if (arrayOfT.length > i)
      arrayOfT[i] = null; 
    return arrayOfT;
  }
  
  final class ArrayIterator<T> implements Iterator<T> {
    boolean mCanRemove = false;
    
    int mIndex;
    
    final int mOffset;
    
    int mSize;
    
    ArrayIterator(int param1Int) {
      this.mOffset = param1Int;
      this.mSize = MapCollections.this.colGetSize();
    }
    
    public boolean hasNext() {
      return (this.mIndex < this.mSize);
    }
    
    public T next() {
      Object object = MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
      this.mIndex++;
      this.mCanRemove = true;
      return (T)object;
    }
    
    public void remove() {
      if (!this.mCanRemove)
        throw new IllegalStateException(); 
      this.mIndex--;
      this.mSize--;
      this.mCanRemove = false;
      MapCollections.this.colRemoveAt(this.mIndex);
    }
  }
  
  final class EntrySet implements Set<Map.Entry<K, V>> {
    public boolean add(Map.Entry<K, V> param1Entry) {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends Map.Entry<K, V>> param1Collection) {
      int i = MapCollections.this.colGetSize();
      for (Map.Entry<K, V> entry : param1Collection)
        MapCollections.this.colPut(entry.getKey(), entry.getValue()); 
      return (i != MapCollections.this.colGetSize());
    }
    
    public void clear() {
      MapCollections.this.colClear();
    }
    
    public boolean contains(Object param1Object) {
      boolean bool = false;
      if (param1Object instanceof Map.Entry) {
        param1Object = param1Object;
        int i = MapCollections.this.colIndexOfKey(param1Object.getKey());
        if (i >= 0)
          bool = ContainerHelpers.equal(MapCollections.this.colGetEntry(i, 1), param1Object.getValue()); 
      } 
      return bool;
    }
    
    public boolean containsAll(Collection<?> param1Collection) {
      // Byte code:
      //   0: aload_1
      //   1: invokeinterface iterator : ()Ljava/util/Iterator;
      //   6: astore_1
      //   7: aload_1
      //   8: invokeinterface hasNext : ()Z
      //   13: ifeq -> 33
      //   16: aload_0
      //   17: aload_1
      //   18: invokeinterface next : ()Ljava/lang/Object;
      //   23: invokevirtual contains : (Ljava/lang/Object;)Z
      //   26: ifne -> 7
      //   29: iconst_0
      //   30: istore_2
      //   31: iload_2
      //   32: ireturn
      //   33: iconst_1
      //   34: istore_2
      //   35: goto -> 31
    }
    
    public boolean equals(Object param1Object) {
      return MapCollections.equalsSetHelper(this, param1Object);
    }
    
    public int hashCode() {
      int i = 0;
      for (int j = MapCollections.this.colGetSize() - 1; j >= 0; j--) {
        int k;
        int m;
        Object object1 = MapCollections.this.colGetEntry(j, 0);
        Object object2 = MapCollections.this.colGetEntry(j, 1);
        if (object1 == null) {
          k = 0;
        } else {
          k = object1.hashCode();
        } 
        if (object2 == null) {
          m = 0;
        } else {
          m = object2.hashCode();
        } 
        i += m ^ k;
      } 
      return i;
    }
    
    public boolean isEmpty() {
      return (MapCollections.this.colGetSize() == 0);
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
      return new MapCollections.MapIterator();
    }
    
    public boolean remove(Object param1Object) {
      throw new UnsupportedOperationException();
    }
    
    public boolean removeAll(Collection<?> param1Collection) {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> param1Collection) {
      throw new UnsupportedOperationException();
    }
    
    public int size() {
      return MapCollections.this.colGetSize();
    }
    
    public Object[] toArray() {
      throw new UnsupportedOperationException();
    }
    
    public <T> T[] toArray(T[] param1ArrayOfT) {
      throw new UnsupportedOperationException();
    }
  }
  
  final class KeySet implements Set<K> {
    public boolean add(K param1K) {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends K> param1Collection) {
      throw new UnsupportedOperationException();
    }
    
    public void clear() {
      MapCollections.this.colClear();
    }
    
    public boolean contains(Object param1Object) {
      return (MapCollections.this.colIndexOfKey(param1Object) >= 0);
    }
    
    public boolean containsAll(Collection<?> param1Collection) {
      return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), param1Collection);
    }
    
    public boolean equals(Object param1Object) {
      return MapCollections.equalsSetHelper(this, param1Object);
    }
    
    public int hashCode() {
      int i = 0;
      for (int j = MapCollections.this.colGetSize() - 1; j >= 0; j--) {
        int k;
        Object object = MapCollections.this.colGetEntry(j, 0);
        if (object == null) {
          k = 0;
        } else {
          k = object.hashCode();
        } 
        i += k;
      } 
      return i;
    }
    
    public boolean isEmpty() {
      return (MapCollections.this.colGetSize() == 0);
    }
    
    public Iterator<K> iterator() {
      return new MapCollections.ArrayIterator<K>(0);
    }
    
    public boolean remove(Object param1Object) {
      int i = MapCollections.this.colIndexOfKey(param1Object);
      if (i >= 0) {
        MapCollections.this.colRemoveAt(i);
        return true;
      } 
      return false;
    }
    
    public boolean removeAll(Collection<?> param1Collection) {
      return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), param1Collection);
    }
    
    public boolean retainAll(Collection<?> param1Collection) {
      return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), param1Collection);
    }
    
    public int size() {
      return MapCollections.this.colGetSize();
    }
    
    public Object[] toArray() {
      return MapCollections.this.toArrayHelper(0);
    }
    
    public <T> T[] toArray(T[] param1ArrayOfT) {
      return (T[])MapCollections.this.toArrayHelper((Object[])param1ArrayOfT, 0);
    }
  }
  
  final class MapIterator implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V> {
    int mEnd = MapCollections.this.colGetSize() - 1;
    
    boolean mEntryValid = false;
    
    int mIndex = -1;
    
    public final boolean equals(Object param1Object) {
      boolean bool1 = true;
      boolean bool2 = false;
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects"); 
      if (param1Object instanceof Map.Entry) {
        param1Object = param1Object;
        if (ContainerHelpers.equal(param1Object.getKey(), MapCollections.this.colGetEntry(this.mIndex, 0)) && ContainerHelpers.equal(param1Object.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1)))
          return bool1; 
        bool2 = false;
      } 
      return bool2;
    }
    
    public K getKey() {
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects"); 
      return (K)MapCollections.this.colGetEntry(this.mIndex, 0);
    }
    
    public V getValue() {
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects"); 
      return (V)MapCollections.this.colGetEntry(this.mIndex, 1);
    }
    
    public boolean hasNext() {
      return (this.mIndex < this.mEnd);
    }
    
    public final int hashCode() {
      int j;
      int i = 0;
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects"); 
      Object object1 = MapCollections.this.colGetEntry(this.mIndex, 0);
      Object object2 = MapCollections.this.colGetEntry(this.mIndex, 1);
      if (object1 == null) {
        j = 0;
      } else {
        j = object1.hashCode();
      } 
      if (object2 != null)
        i = object2.hashCode(); 
      return i ^ j;
    }
    
    public Map.Entry<K, V> next() {
      this.mIndex++;
      this.mEntryValid = true;
      return this;
    }
    
    public void remove() {
      if (!this.mEntryValid)
        throw new IllegalStateException(); 
      MapCollections.this.colRemoveAt(this.mIndex);
      this.mIndex--;
      this.mEnd--;
      this.mEntryValid = false;
    }
    
    public V setValue(V param1V) {
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects"); 
      return (V)MapCollections.this.colSetValue(this.mIndex, param1V);
    }
    
    public final String toString() {
      return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
    }
  }
  
  final class ValuesCollection implements Collection<V> {
    public boolean add(V param1V) {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends V> param1Collection) {
      throw new UnsupportedOperationException();
    }
    
    public void clear() {
      MapCollections.this.colClear();
    }
    
    public boolean contains(Object param1Object) {
      return (MapCollections.this.colIndexOfValue(param1Object) >= 0);
    }
    
    public boolean containsAll(Collection<?> param1Collection) {
      // Byte code:
      //   0: aload_1
      //   1: invokeinterface iterator : ()Ljava/util/Iterator;
      //   6: astore_1
      //   7: aload_1
      //   8: invokeinterface hasNext : ()Z
      //   13: ifeq -> 33
      //   16: aload_0
      //   17: aload_1
      //   18: invokeinterface next : ()Ljava/lang/Object;
      //   23: invokevirtual contains : (Ljava/lang/Object;)Z
      //   26: ifne -> 7
      //   29: iconst_0
      //   30: istore_2
      //   31: iload_2
      //   32: ireturn
      //   33: iconst_1
      //   34: istore_2
      //   35: goto -> 31
    }
    
    public boolean isEmpty() {
      return (MapCollections.this.colGetSize() == 0);
    }
    
    public Iterator<V> iterator() {
      return new MapCollections.ArrayIterator<V>(1);
    }
    
    public boolean remove(Object param1Object) {
      int i = MapCollections.this.colIndexOfValue(param1Object);
      if (i >= 0) {
        MapCollections.this.colRemoveAt(i);
        return true;
      } 
      return false;
    }
    
    public boolean removeAll(Collection<?> param1Collection) {
      int i = MapCollections.this.colGetSize();
      boolean bool = false;
      int j = 0;
      while (j < i) {
        int k = i;
        int m = j;
        if (param1Collection.contains(MapCollections.this.colGetEntry(j, 1))) {
          MapCollections.this.colRemoveAt(j);
          m = j - 1;
          k = i - 1;
          bool = true;
        } 
        j = m + 1;
        i = k;
      } 
      return bool;
    }
    
    public boolean retainAll(Collection<?> param1Collection) {
      int i = MapCollections.this.colGetSize();
      boolean bool = false;
      int j = 0;
      while (j < i) {
        int k = i;
        int m = j;
        if (!param1Collection.contains(MapCollections.this.colGetEntry(j, 1))) {
          MapCollections.this.colRemoveAt(j);
          m = j - 1;
          k = i - 1;
          bool = true;
        } 
        j = m + 1;
        i = k;
      } 
      return bool;
    }
    
    public int size() {
      return MapCollections.this.colGetSize();
    }
    
    public Object[] toArray() {
      return MapCollections.this.toArrayHelper(1);
    }
    
    public <T> T[] toArray(T[] param1ArrayOfT) {
      return (T[])MapCollections.this.toArrayHelper((Object[])param1ArrayOfT, 1);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/util/MapCollections.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */