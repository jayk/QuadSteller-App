package org.greenrobot.greendao.internal;

import java.util.Arrays;
import org.greenrobot.greendao.DaoLog;

public final class LongHashMap<T> {
  private int capacity;
  
  private int size;
  
  private Entry<T>[] table;
  
  private int threshold;
  
  public LongHashMap() {
    this(16);
  }
  
  public LongHashMap(int paramInt) {
    this.capacity = paramInt;
    this.threshold = paramInt * 4 / 3;
    this.table = (Entry<T>[])new Entry[paramInt];
  }
  
  public void clear() {
    this.size = 0;
    Arrays.fill((Object[])this.table, (Object)null);
  }
  
  public boolean containsKey(long paramLong) {
    // Byte code:
    //   0: lload_1
    //   1: bipush #32
    //   3: lushr
    //   4: l2i
    //   5: istore_3
    //   6: lload_1
    //   7: l2i
    //   8: istore #4
    //   10: aload_0
    //   11: getfield capacity : I
    //   14: istore #5
    //   16: aload_0
    //   17: getfield table : [Lorg/greenrobot/greendao/internal/LongHashMap$Entry;
    //   20: iload_3
    //   21: iload #4
    //   23: ixor
    //   24: ldc 2147483647
    //   26: iand
    //   27: iload #5
    //   29: irem
    //   30: aaload
    //   31: astore #6
    //   33: aload #6
    //   35: ifnull -> 64
    //   38: aload #6
    //   40: getfield key : J
    //   43: lload_1
    //   44: lcmp
    //   45: ifne -> 54
    //   48: iconst_1
    //   49: istore #7
    //   51: iload #7
    //   53: ireturn
    //   54: aload #6
    //   56: getfield next : Lorg/greenrobot/greendao/internal/LongHashMap$Entry;
    //   59: astore #6
    //   61: goto -> 33
    //   64: iconst_0
    //   65: istore #7
    //   67: goto -> 51
  }
  
  public T get(long paramLong) {
    // Byte code:
    //   0: lload_1
    //   1: bipush #32
    //   3: lushr
    //   4: l2i
    //   5: istore_3
    //   6: lload_1
    //   7: l2i
    //   8: istore #4
    //   10: aload_0
    //   11: getfield capacity : I
    //   14: istore #5
    //   16: aload_0
    //   17: getfield table : [Lorg/greenrobot/greendao/internal/LongHashMap$Entry;
    //   20: iload_3
    //   21: iload #4
    //   23: ixor
    //   24: ldc 2147483647
    //   26: iand
    //   27: iload #5
    //   29: irem
    //   30: aaload
    //   31: astore #6
    //   33: aload #6
    //   35: ifnull -> 68
    //   38: aload #6
    //   40: getfield key : J
    //   43: lload_1
    //   44: lcmp
    //   45: ifne -> 58
    //   48: aload #6
    //   50: getfield value : Ljava/lang/Object;
    //   53: astore #6
    //   55: aload #6
    //   57: areturn
    //   58: aload #6
    //   60: getfield next : Lorg/greenrobot/greendao/internal/LongHashMap$Entry;
    //   63: astore #6
    //   65: goto -> 33
    //   68: aconst_null
    //   69: astore #6
    //   71: goto -> 55
  }
  
  public void logStats() {
    byte b1 = 0;
    Entry<T>[] arrayOfEntry = this.table;
    int i = arrayOfEntry.length;
    for (byte b2 = 0; b2 < i; b2++) {
      for (Entry<T> entry = arrayOfEntry[b2]; entry != null && entry.next != null; entry = entry.next)
        b1++; 
    } 
    DaoLog.d("load: " + (this.size / this.capacity) + ", size: " + this.size + ", capa: " + this.capacity + ", collisions: " + b1 + ", collision ratio: " + (b1 / this.size));
  }
  
  public T put(long paramLong, T paramT) {
    T t;
    int i = (((int)(paramLong >>> 32L) ^ (int)paramLong) & Integer.MAX_VALUE) % this.capacity;
    Entry<T> entry1 = this.table[i];
    for (Entry<T> entry2 = entry1; entry2 != null; entry2 = entry2.next) {
      if (entry2.key == paramLong) {
        t = entry2.value;
        entry2.value = paramT;
        return t;
      } 
    } 
    this.table[i] = new Entry<T>(paramLong, paramT, (Entry<T>)t);
    this.size++;
    if (this.size > this.threshold)
      setCapacity(this.capacity * 2); 
    return null;
  }
  
  public T remove(long paramLong) {
    // Byte code:
    //   0: lload_1
    //   1: bipush #32
    //   3: lushr
    //   4: l2i
    //   5: lload_1
    //   6: l2i
    //   7: ixor
    //   8: ldc 2147483647
    //   10: iand
    //   11: aload_0
    //   12: getfield capacity : I
    //   15: irem
    //   16: istore_3
    //   17: aconst_null
    //   18: astore #4
    //   20: aload_0
    //   21: getfield table : [Lorg/greenrobot/greendao/internal/LongHashMap$Entry;
    //   24: iload_3
    //   25: aaload
    //   26: astore #5
    //   28: aload #5
    //   30: ifnull -> 104
    //   33: aload #5
    //   35: getfield next : Lorg/greenrobot/greendao/internal/LongHashMap$Entry;
    //   38: astore #6
    //   40: aload #5
    //   42: getfield key : J
    //   45: lload_1
    //   46: lcmp
    //   47: ifne -> 93
    //   50: aload #4
    //   52: ifnonnull -> 83
    //   55: aload_0
    //   56: getfield table : [Lorg/greenrobot/greendao/internal/LongHashMap$Entry;
    //   59: iload_3
    //   60: aload #6
    //   62: aastore
    //   63: aload_0
    //   64: aload_0
    //   65: getfield size : I
    //   68: iconst_1
    //   69: isub
    //   70: putfield size : I
    //   73: aload #5
    //   75: getfield value : Ljava/lang/Object;
    //   78: astore #5
    //   80: aload #5
    //   82: areturn
    //   83: aload #4
    //   85: aload #6
    //   87: putfield next : Lorg/greenrobot/greendao/internal/LongHashMap$Entry;
    //   90: goto -> 63
    //   93: aload #5
    //   95: astore #4
    //   97: aload #6
    //   99: astore #5
    //   101: goto -> 28
    //   104: aconst_null
    //   105: astore #5
    //   107: goto -> 80
  }
  
  public void reserveRoom(int paramInt) {
    setCapacity(paramInt * 5 / 3);
  }
  
  public void setCapacity(int paramInt) {
    Entry[] arrayOfEntry = new Entry[paramInt];
    int i = this.table.length;
    for (byte b = 0; b < i; b++) {
      for (Entry<T> entry = this.table[b]; entry != null; entry = entry1) {
        long l = entry.key;
        int j = (((int)(l >>> 32L) ^ (int)l) & Integer.MAX_VALUE) % paramInt;
        Entry<T> entry1 = entry.next;
        entry.next = arrayOfEntry[j];
        arrayOfEntry[j] = entry;
      } 
    } 
    this.table = (Entry<T>[])arrayOfEntry;
    this.capacity = paramInt;
    this.threshold = paramInt * 4 / 3;
  }
  
  public int size() {
    return this.size;
  }
  
  static final class Entry<T> {
    final long key;
    
    Entry<T> next;
    
    T value;
    
    Entry(long param1Long, T param1T, Entry<T> param1Entry) {
      this.key = param1Long;
      this.value = param1T;
      this.next = param1Entry;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/internal/LongHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */