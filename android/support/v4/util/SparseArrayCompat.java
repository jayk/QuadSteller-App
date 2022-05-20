package android.support.v4.util;

public class SparseArrayCompat<E> implements Cloneable {
  private static final Object DELETED = new Object();
  
  private boolean mGarbage = false;
  
  private int[] mKeys;
  
  private int mSize;
  
  private Object[] mValues;
  
  public SparseArrayCompat() {
    this(10);
  }
  
  public SparseArrayCompat(int paramInt) {
    if (paramInt == 0) {
      this.mKeys = ContainerHelpers.EMPTY_INTS;
      this.mValues = ContainerHelpers.EMPTY_OBJECTS;
    } else {
      paramInt = ContainerHelpers.idealIntArraySize(paramInt);
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
  
  public void append(int paramInt, E paramE) {
    if (this.mSize != 0 && paramInt <= this.mKeys[this.mSize - 1]) {
      put(paramInt, paramE);
      return;
    } 
    if (this.mGarbage && this.mSize >= this.mKeys.length)
      gc(); 
    int i = this.mSize;
    if (i >= this.mKeys.length) {
      int j = ContainerHelpers.idealIntArraySize(i + 1);
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
  
  public SparseArrayCompat<E> clone() {
    SparseArrayCompat<E> sparseArrayCompat = null;
    try {
      SparseArrayCompat<E> sparseArrayCompat1 = (SparseArrayCompat)super.clone();
      sparseArrayCompat = sparseArrayCompat1;
      sparseArrayCompat1.mKeys = (int[])this.mKeys.clone();
      sparseArrayCompat = sparseArrayCompat1;
      sparseArrayCompat1.mValues = (Object[])this.mValues.clone();
      sparseArrayCompat = sparseArrayCompat1;
    } catch (CloneNotSupportedException cloneNotSupportedException) {}
    return sparseArrayCompat;
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
      j = ContainerHelpers.idealIntArraySize(this.mSize + 1);
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
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/util/SparseArrayCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */