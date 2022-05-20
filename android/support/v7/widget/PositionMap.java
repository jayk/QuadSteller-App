package android.support.v7.widget;

import java.util.ArrayList;

class PositionMap<E> implements Cloneable {
  private static final Object DELETED = new Object();
  
  private boolean mGarbage = false;
  
  private int[] mKeys;
  
  private int mSize;
  
  private Object[] mValues;
  
  public PositionMap() {
    this(10);
  }
  
  public PositionMap(int paramInt) {
    if (paramInt == 0) {
      this.mKeys = ContainerHelpers.EMPTY_INTS;
      this.mValues = ContainerHelpers.EMPTY_OBJECTS;
    } else {
      paramInt = idealIntArraySize(paramInt);
      this.mKeys = new int[paramInt];
      this.mValues = new Object[paramInt];
    } 
    this.mSize = 0;
  }
  
  private void gc() {
    int i = this.mSize;
    int j = 0;
    int[] arrayOfInt = this.mKeys;
    Object[] arrayOfObject = this.mValues;
    int k = 0;
    while (k < i) {
      Object object = arrayOfObject[k];
      int m = j;
      if (object != DELETED) {
        if (k != j) {
          arrayOfInt[j] = arrayOfInt[k];
          arrayOfObject[j] = object;
          arrayOfObject[k] = null;
        } 
        m = j + 1;
      } 
      k++;
      j = m;
    } 
    this.mGarbage = false;
    this.mSize = j;
  }
  
  static int idealBooleanArraySize(int paramInt) {
    return idealByteArraySize(paramInt);
  }
  
  static int idealByteArraySize(int paramInt) {
    for (byte b = 4;; b++) {
      int i = paramInt;
      if (b < 32) {
        if (paramInt <= (1 << b) - 12)
          return (1 << b) - 12; 
      } else {
        return i;
      } 
    } 
  }
  
  static int idealCharArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 2) / 2;
  }
  
  static int idealFloatArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 4) / 4;
  }
  
  static int idealIntArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 4) / 4;
  }
  
  static int idealLongArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 8) / 8;
  }
  
  static int idealObjectArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 4) / 4;
  }
  
  static int idealShortArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 2) / 2;
  }
  
  public void append(int paramInt, E paramE) {
    if (this.mSize != 0 && paramInt <= this.mKeys[this.mSize - 1]) {
      put(paramInt, paramE);
      return;
    } 
    if (this.mGarbage && this.mSize >= this.mKeys.length)
      gc(); 
    int i = this.mSize;
    if (i >= this.mKeys.length) {
      int j = idealIntArraySize(i + 1);
      int[] arrayOfInt = new int[j];
      Object[] arrayOfObject = new Object[j];
      System.arraycopy(this.mKeys, 0, arrayOfInt, 0, this.mKeys.length);
      System.arraycopy(this.mValues, 0, arrayOfObject, 0, this.mValues.length);
      this.mKeys = arrayOfInt;
      this.mValues = arrayOfObject;
    } 
    this.mKeys[i] = paramInt;
    this.mValues[i] = paramE;
    this.mSize = i + 1;
  }
  
  public void clear() {
    int i = this.mSize;
    Object[] arrayOfObject = this.mValues;
    for (byte b = 0; b < i; b++)
      arrayOfObject[b] = null; 
    this.mSize = 0;
    this.mGarbage = false;
  }
  
  public PositionMap<E> clone() {
    PositionMap<E> positionMap = null;
    try {
      PositionMap<E> positionMap1 = (PositionMap)super.clone();
      positionMap = positionMap1;
      positionMap1.mKeys = (int[])this.mKeys.clone();
      positionMap = positionMap1;
      positionMap1.mValues = (Object[])this.mValues.clone();
      positionMap = positionMap1;
    } catch (CloneNotSupportedException cloneNotSupportedException) {}
    return positionMap;
  }
  
  public void delete(int paramInt) {
    paramInt = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt);
    if (paramInt >= 0 && this.mValues[paramInt] != DELETED) {
      this.mValues[paramInt] = DELETED;
      this.mGarbage = true;
    } 
  }
  
  public E get(int paramInt) {
    return get(paramInt, null);
  }
  
  public E get(int paramInt, E paramE) {
    paramInt = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt);
    null = paramE;
    if (paramInt >= 0) {
      if (this.mValues[paramInt] == DELETED)
        return paramE; 
    } else {
      return null;
    } 
    return (E)this.mValues[paramInt];
  }
  
  public int indexOfKey(int paramInt) {
    if (this.mGarbage)
      gc(); 
    return ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt);
  }
  
  public int indexOfValue(E paramE) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mGarbage : Z
    //   4: ifeq -> 11
    //   7: aload_0
    //   8: invokespecial gc : ()V
    //   11: iconst_0
    //   12: istore_2
    //   13: iload_2
    //   14: aload_0
    //   15: getfield mSize : I
    //   18: if_icmpge -> 39
    //   21: aload_0
    //   22: getfield mValues : [Ljava/lang/Object;
    //   25: iload_2
    //   26: aaload
    //   27: aload_1
    //   28: if_acmpne -> 33
    //   31: iload_2
    //   32: ireturn
    //   33: iinc #2, 1
    //   36: goto -> 13
    //   39: iconst_m1
    //   40: istore_2
    //   41: goto -> 31
  }
  
  public void insertKeyRange(int paramInt1, int paramInt2) {}
  
  public int keyAt(int paramInt) {
    if (this.mGarbage)
      gc(); 
    return this.mKeys[paramInt];
  }
  
  public void put(int paramInt, E paramE) {
    int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt);
    if (i >= 0) {
      this.mValues[i] = paramE;
      return;
    } 
    int j = i ^ 0xFFFFFFFF;
    if (j < this.mSize && this.mValues[j] == DELETED) {
      this.mKeys[j] = paramInt;
      this.mValues[j] = paramE;
      return;
    } 
    i = j;
    if (this.mGarbage) {
      i = j;
      if (this.mSize >= this.mKeys.length) {
        gc();
        i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt) ^ 0xFFFFFFFF;
      } 
    } 
    if (this.mSize >= this.mKeys.length) {
      j = idealIntArraySize(this.mSize + 1);
      int[] arrayOfInt = new int[j];
      Object[] arrayOfObject = new Object[j];
      System.arraycopy(this.mKeys, 0, arrayOfInt, 0, this.mKeys.length);
      System.arraycopy(this.mValues, 0, arrayOfObject, 0, this.mValues.length);
      this.mKeys = arrayOfInt;
      this.mValues = arrayOfObject;
    } 
    if (this.mSize - i != 0) {
      System.arraycopy(this.mKeys, i, this.mKeys, i + 1, this.mSize - i);
      System.arraycopy(this.mValues, i, this.mValues, i + 1, this.mSize - i);
    } 
    this.mKeys[i] = paramInt;
    this.mValues[i] = paramE;
    this.mSize++;
  }
  
  public void remove(int paramInt) {
    delete(paramInt);
  }
  
  public void removeAt(int paramInt) {
    if (this.mValues[paramInt] != DELETED) {
      this.mValues[paramInt] = DELETED;
      this.mGarbage = true;
    } 
  }
  
  public void removeAtRange(int paramInt1, int paramInt2) {
    paramInt2 = Math.min(this.mSize, paramInt1 + paramInt2);
    while (paramInt1 < paramInt2) {
      removeAt(paramInt1);
      paramInt1++;
    } 
  }
  
  public void removeKeyRange(ArrayList<E> paramArrayList, int paramInt1, int paramInt2) {}
  
  public void setValueAt(int paramInt, E paramE) {
    if (this.mGarbage)
      gc(); 
    this.mValues[paramInt] = paramE;
  }
  
  public int size() {
    if (this.mGarbage)
      gc(); 
    return this.mSize;
  }
  
  public String toString() {
    if (size() <= 0)
      return "{}"; 
    StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
    stringBuilder.append('{');
    for (byte b = 0; b < this.mSize; b++) {
      if (b > 0)
        stringBuilder.append(", "); 
      stringBuilder.append(keyAt(b));
      stringBuilder.append('=');
      E e = valueAt(b);
      if (e != this) {
        stringBuilder.append(e);
      } else {
        stringBuilder.append("(this Map)");
      } 
    } 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public E valueAt(int paramInt) {
    if (this.mGarbage)
      gc(); 
    return (E)this.mValues[paramInt];
  }
  
  static class ContainerHelpers {
    static final boolean[] EMPTY_BOOLEANS = new boolean[0];
    
    static final int[] EMPTY_INTS = new int[0];
    
    static final long[] EMPTY_LONGS = new long[0];
    
    static final Object[] EMPTY_OBJECTS = new Object[0];
    
    static int binarySearch(int[] param1ArrayOfint, int param1Int1, int param1Int2) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_3
      //   2: iload_1
      //   3: iconst_1
      //   4: isub
      //   5: istore #4
      //   7: iload_3
      //   8: istore_1
      //   9: iload #4
      //   11: istore_3
      //   12: iload_1
      //   13: iload_3
      //   14: if_icmpgt -> 61
      //   17: iload_1
      //   18: iload_3
      //   19: iadd
      //   20: iconst_1
      //   21: iushr
      //   22: istore #4
      //   24: aload_0
      //   25: iload #4
      //   27: iaload
      //   28: istore #5
      //   30: iload #5
      //   32: iload_2
      //   33: if_icmpge -> 44
      //   36: iload #4
      //   38: iconst_1
      //   39: iadd
      //   40: istore_1
      //   41: goto -> 12
      //   44: iload #4
      //   46: istore_3
      //   47: iload #5
      //   49: iload_2
      //   50: if_icmple -> 65
      //   53: iload #4
      //   55: iconst_1
      //   56: isub
      //   57: istore_3
      //   58: goto -> 12
      //   61: iload_1
      //   62: iconst_m1
      //   63: ixor
      //   64: istore_3
      //   65: iload_3
      //   66: ireturn
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/PositionMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */