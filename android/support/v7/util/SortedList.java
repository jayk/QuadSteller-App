package android.support.v7.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
  private static final int CAPACITY_GROWTH = 10;
  
  private static final int DELETION = 2;
  
  private static final int INSERTION = 1;
  
  public static final int INVALID_POSITION = -1;
  
  private static final int LOOKUP = 4;
  
  private static final int MIN_CAPACITY = 10;
  
  private BatchedCallback mBatchedCallback;
  
  private Callback mCallback;
  
  T[] mData;
  
  private int mMergedSize;
  
  private T[] mOldData;
  
  private int mOldDataSize;
  
  private int mOldDataStart;
  
  private int mSize;
  
  private final Class<T> mTClass;
  
  public SortedList(Class<T> paramClass, Callback<T> paramCallback) {
    this(paramClass, paramCallback, 10);
  }
  
  public SortedList(Class<T> paramClass, Callback<T> paramCallback, int paramInt) {
    this.mTClass = paramClass;
    this.mData = (T[])Array.newInstance(paramClass, paramInt);
    this.mCallback = paramCallback;
    this.mSize = 0;
  }
  
  private int add(T paramT, boolean paramBoolean) {
    int i = findIndexOf(paramT, this.mData, 0, this.mSize, 1);
    if (i == -1) {
      boolean bool = false;
    } else {
      int j = i;
      if (i < this.mSize) {
        T t = this.mData[i];
        j = i;
        if (this.mCallback.areItemsTheSame(t, paramT)) {
          if (this.mCallback.areContentsTheSame(t, paramT)) {
            this.mData[i] = paramT;
            return i;
          } 
          this.mData[i] = paramT;
          this.mCallback.onChanged(i, 1);
          return i;
        } 
      } 
    } 
    addToData(SYNTHETIC_LOCAL_VARIABLE_4, paramT);
    if (paramBoolean)
      this.mCallback.onInserted(SYNTHETIC_LOCAL_VARIABLE_4, 1); 
    return SYNTHETIC_LOCAL_VARIABLE_4;
  }
  
  private void addAllInternal(T[] paramArrayOfT) {
    boolean bool;
    if (!(this.mCallback instanceof BatchedCallback)) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      beginBatchedUpdates(); 
    this.mOldData = this.mData;
    this.mOldDataStart = 0;
    this.mOldDataSize = this.mSize;
    Arrays.sort(paramArrayOfT, this.mCallback);
    int i = deduplicate(paramArrayOfT);
    if (this.mSize == 0) {
      this.mData = paramArrayOfT;
      this.mSize = i;
      this.mMergedSize = i;
      this.mCallback.onInserted(0, i);
    } else {
      merge(paramArrayOfT, i);
    } 
    this.mOldData = null;
    if (bool)
      endBatchedUpdates(); 
  }
  
  private void addToData(int paramInt, T paramT) {
    if (paramInt > this.mSize)
      throw new IndexOutOfBoundsException("cannot add item to " + paramInt + " because size is " + this.mSize); 
    if (this.mSize == this.mData.length) {
      Object[] arrayOfObject = (Object[])Array.newInstance(this.mTClass, this.mData.length + 10);
      System.arraycopy(this.mData, 0, arrayOfObject, 0, paramInt);
      arrayOfObject[paramInt] = paramT;
      System.arraycopy(this.mData, paramInt, arrayOfObject, paramInt + 1, this.mSize - paramInt);
      this.mData = (T[])arrayOfObject;
    } else {
      System.arraycopy(this.mData, paramInt, this.mData, paramInt + 1, this.mSize - paramInt);
      this.mData[paramInt] = paramT;
    } 
    this.mSize++;
  }
  
  private int deduplicate(T[] paramArrayOfT) {
    if (paramArrayOfT.length == 0)
      throw new IllegalArgumentException("Input array must be non-empty"); 
    byte b1 = 0;
    byte b2 = 1;
    for (byte b3 = 1; b3 < paramArrayOfT.length; b3++) {
      T t = paramArrayOfT[b3];
      int i = this.mCallback.compare(paramArrayOfT[b1], t);
      if (i > 0)
        throw new IllegalArgumentException("Input must be sorted in ascending order."); 
      if (i == 0) {
        i = findSameItem(t, paramArrayOfT, b1, b2);
        if (i != -1) {
          paramArrayOfT[i] = t;
        } else {
          if (b2 != b3)
            paramArrayOfT[b2] = t; 
          b2++;
        } 
      } else {
        if (b2 != b3)
          paramArrayOfT[b2] = t; 
        b1 = b2;
        b2++;
      } 
    } 
    return b2;
  }
  
  private int findIndexOf(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: iload_3
    //   1: iload #4
    //   3: if_icmpge -> 114
    //   6: iload_3
    //   7: iload #4
    //   9: iadd
    //   10: iconst_2
    //   11: idiv
    //   12: istore #6
    //   14: aload_2
    //   15: iload #6
    //   17: aaload
    //   18: astore #7
    //   20: aload_0
    //   21: getfield mCallback : Landroid/support/v7/util/SortedList$Callback;
    //   24: aload #7
    //   26: aload_1
    //   27: invokevirtual compare : (Ljava/lang/Object;Ljava/lang/Object;)I
    //   30: istore #8
    //   32: iload #8
    //   34: ifge -> 45
    //   37: iload #6
    //   39: iconst_1
    //   40: iadd
    //   41: istore_3
    //   42: goto -> 0
    //   45: iload #8
    //   47: ifne -> 107
    //   50: aload_0
    //   51: getfield mCallback : Landroid/support/v7/util/SortedList$Callback;
    //   54: aload #7
    //   56: aload_1
    //   57: invokevirtual areItemsTheSame : (Ljava/lang/Object;Ljava/lang/Object;)Z
    //   60: ifeq -> 68
    //   63: iload #6
    //   65: istore_3
    //   66: iload_3
    //   67: ireturn
    //   68: aload_0
    //   69: aload_1
    //   70: iload #6
    //   72: iload_3
    //   73: iload #4
    //   75: invokespecial linearEqualitySearch : (Ljava/lang/Object;III)I
    //   78: istore #4
    //   80: iload #5
    //   82: iconst_1
    //   83: if_icmpne -> 101
    //   86: iload #6
    //   88: istore_3
    //   89: iload #4
    //   91: iconst_m1
    //   92: if_icmpeq -> 66
    //   95: iload #4
    //   97: istore_3
    //   98: goto -> 66
    //   101: iload #4
    //   103: istore_3
    //   104: goto -> 66
    //   107: iload #6
    //   109: istore #4
    //   111: goto -> 0
    //   114: iload #5
    //   116: iconst_1
    //   117: if_icmpne -> 123
    //   120: goto -> 66
    //   123: iconst_m1
    //   124: istore_3
    //   125: goto -> 120
  }
  
  private int findSameItem(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_3
    //   1: iload #4
    //   3: if_icmpge -> 28
    //   6: aload_0
    //   7: getfield mCallback : Landroid/support/v7/util/SortedList$Callback;
    //   10: aload_2
    //   11: iload_3
    //   12: aaload
    //   13: aload_1
    //   14: invokevirtual areItemsTheSame : (Ljava/lang/Object;Ljava/lang/Object;)Z
    //   17: ifeq -> 22
    //   20: iload_3
    //   21: ireturn
    //   22: iinc #3, 1
    //   25: goto -> 0
    //   28: iconst_m1
    //   29: istore_3
    //   30: goto -> 20
  }
  
  private int linearEqualitySearch(T paramT, int paramInt1, int paramInt2, int paramInt3) {
    int i = paramInt1 - 1;
    while (true) {
      if (i >= paramInt2) {
        T t = this.mData[i];
        if (this.mCallback.compare(t, paramT) == 0) {
          if (this.mCallback.areItemsTheSame(t, paramT)) {
            paramInt1 = i;
            continue;
          } 
          i--;
          continue;
        } 
      } 
      paramInt1++;
      while (true) {
        if (paramInt1 < paramInt3) {
          T t = this.mData[paramInt1];
          if (this.mCallback.compare(t, paramT) != 0)
            return -1; 
          if (!this.mCallback.areItemsTheSame(t, paramT)) {
            paramInt1++;
            continue;
          } 
          return paramInt1;
        } 
        return -1;
      } 
      break;
    } 
  }
  
  private void merge(T[] paramArrayOfT, int paramInt) {
    int i = this.mSize;
    this.mData = (T[])Array.newInstance(this.mTClass, i + paramInt + 10);
    this.mMergedSize = 0;
    i = 0;
    while (true) {
      T[] arrayOfT1;
      if (this.mOldDataStart < this.mOldDataSize || i < paramInt) {
        if (this.mOldDataStart == this.mOldDataSize) {
          paramInt -= i;
          System.arraycopy(paramArrayOfT, i, this.mData, this.mMergedSize, paramInt);
          this.mMergedSize += paramInt;
          this.mSize += paramInt;
          this.mCallback.onInserted(this.mMergedSize - paramInt, paramInt);
          return;
        } 
      } else {
        return;
      } 
      if (i == paramInt) {
        paramInt = this.mOldDataSize - this.mOldDataStart;
        System.arraycopy(this.mOldData, this.mOldDataStart, this.mData, this.mMergedSize, paramInt);
        this.mMergedSize += paramInt;
        return;
      } 
      T t1 = this.mOldData[this.mOldDataStart];
      T t2 = paramArrayOfT[i];
      int j = this.mCallback.compare(t1, t2);
      if (j > 0) {
        arrayOfT1 = this.mData;
        j = this.mMergedSize;
        this.mMergedSize = j + 1;
        arrayOfT1[j] = t2;
        this.mSize++;
        i++;
        this.mCallback.onInserted(this.mMergedSize - 1, 1);
        continue;
      } 
      if (j == 0 && this.mCallback.areItemsTheSame(arrayOfT1, (T[])t2)) {
        T[] arrayOfT = this.mData;
        j = this.mMergedSize;
        this.mMergedSize = j + 1;
        arrayOfT[j] = t2;
        j = i + 1;
        this.mOldDataStart++;
        i = j;
        if (!this.mCallback.areContentsTheSame(arrayOfT1, (T[])t2)) {
          this.mCallback.onChanged(this.mMergedSize - 1, 1);
          i = j;
        } 
        continue;
      } 
      T[] arrayOfT2 = this.mData;
      j = this.mMergedSize;
      this.mMergedSize = j + 1;
      arrayOfT2[j] = (T)arrayOfT1;
      this.mOldDataStart++;
    } 
  }
  
  private boolean remove(T paramT, boolean paramBoolean) {
    boolean bool = false;
    int i = findIndexOf(paramT, this.mData, 0, this.mSize, 2);
    if (i == -1)
      return bool; 
    removeItemAtIndex(i, paramBoolean);
    return true;
  }
  
  private void removeItemAtIndex(int paramInt, boolean paramBoolean) {
    System.arraycopy(this.mData, paramInt + 1, this.mData, paramInt, this.mSize - paramInt - 1);
    this.mSize--;
    this.mData[this.mSize] = null;
    if (paramBoolean)
      this.mCallback.onRemoved(paramInt, 1); 
  }
  
  private void throwIfMerging() {
    if (this.mOldData != null)
      throw new IllegalStateException("Cannot call this method from within addAll"); 
  }
  
  public int add(T paramT) {
    throwIfMerging();
    return add(paramT, true);
  }
  
  public void addAll(Collection<T> paramCollection) {
    addAll(paramCollection.toArray((T[])Array.newInstance(this.mTClass, paramCollection.size())), true);
  }
  
  public void addAll(T... paramVarArgs) {
    addAll(paramVarArgs, false);
  }
  
  public void addAll(T[] paramArrayOfT, boolean paramBoolean) {
    throwIfMerging();
    if (paramArrayOfT.length != 0) {
      if (paramBoolean) {
        addAllInternal(paramArrayOfT);
        return;
      } 
      Object[] arrayOfObject = (Object[])Array.newInstance(this.mTClass, paramArrayOfT.length);
      System.arraycopy(paramArrayOfT, 0, arrayOfObject, 0, paramArrayOfT.length);
      addAllInternal((T[])arrayOfObject);
    } 
  }
  
  public void beginBatchedUpdates() {
    throwIfMerging();
    if (!(this.mCallback instanceof BatchedCallback)) {
      if (this.mBatchedCallback == null)
        this.mBatchedCallback = new BatchedCallback(this.mCallback); 
      this.mCallback = this.mBatchedCallback;
    } 
  }
  
  public void clear() {
    throwIfMerging();
    if (this.mSize != 0) {
      int i = this.mSize;
      Arrays.fill((Object[])this.mData, 0, i, (Object)null);
      this.mSize = 0;
      this.mCallback.onRemoved(0, i);
    } 
  }
  
  public void endBatchedUpdates() {
    throwIfMerging();
    if (this.mCallback instanceof BatchedCallback)
      ((BatchedCallback)this.mCallback).dispatchLastEvent(); 
    if (this.mCallback == this.mBatchedCallback)
      this.mCallback = this.mBatchedCallback.mWrappedCallback; 
  }
  
  public T get(int paramInt) throws IndexOutOfBoundsException {
    if (paramInt >= this.mSize || paramInt < 0)
      throw new IndexOutOfBoundsException("Asked to get item at " + paramInt + " but size is " + this.mSize); 
    return (this.mOldData != null && paramInt >= this.mMergedSize) ? this.mOldData[paramInt - this.mMergedSize + this.mOldDataStart] : this.mData[paramInt];
  }
  
  public int indexOf(T paramT) {
    if (this.mOldData != null) {
      int i = findIndexOf(paramT, this.mData, 0, this.mMergedSize, 4);
      if (i == -1) {
        i = findIndexOf(paramT, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
        if (i != -1)
          return i - this.mOldDataStart + this.mMergedSize; 
        i = -1;
      } 
      return i;
    } 
    return findIndexOf(paramT, this.mData, 0, this.mSize, 4);
  }
  
  public void recalculatePositionOfItemAt(int paramInt) {
    throwIfMerging();
    T t = get(paramInt);
    removeItemAtIndex(paramInt, false);
    int i = add(t, false);
    if (paramInt != i)
      this.mCallback.onMoved(paramInt, i); 
  }
  
  public boolean remove(T paramT) {
    throwIfMerging();
    return remove(paramT, true);
  }
  
  public T removeItemAt(int paramInt) {
    throwIfMerging();
    T t = get(paramInt);
    removeItemAtIndex(paramInt, true);
    return t;
  }
  
  public int size() {
    return this.mSize;
  }
  
  public void updateItemAt(int paramInt, T paramT) {
    throwIfMerging();
    T t = get(paramInt);
    if (t == paramT || !this.mCallback.areContentsTheSame(t, paramT)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (t != paramT && this.mCallback.compare(t, paramT) == 0) {
      this.mData[paramInt] = paramT;
      if (i)
        this.mCallback.onChanged(paramInt, 1); 
      return;
    } 
    if (i)
      this.mCallback.onChanged(paramInt, 1); 
    removeItemAtIndex(paramInt, false);
    int i = add(paramT, false);
    if (paramInt != i)
      this.mCallback.onMoved(paramInt, i); 
  }
  
  public static class BatchedCallback<T2> extends Callback<T2> {
    private final BatchingListUpdateCallback mBatchingListUpdateCallback;
    
    final SortedList.Callback<T2> mWrappedCallback;
    
    public BatchedCallback(SortedList.Callback<T2> param1Callback) {
      this.mWrappedCallback = param1Callback;
      this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
    }
    
    public boolean areContentsTheSame(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.areContentsTheSame(param1T21, param1T22);
    }
    
    public boolean areItemsTheSame(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.areItemsTheSame(param1T21, param1T22);
    }
    
    public int compare(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.compare(param1T21, param1T22);
    }
    
    public void dispatchLastEvent() {
      this.mBatchingListUpdateCallback.dispatchLastEvent();
    }
    
    public void onChanged(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onChanged(param1Int1, param1Int2, null);
    }
    
    public void onInserted(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onInserted(param1Int1, param1Int2);
    }
    
    public void onMoved(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onMoved(param1Int1, param1Int2);
    }
    
    public void onRemoved(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onRemoved(param1Int1, param1Int2);
    }
  }
  
  public static abstract class Callback<T2> implements Comparator<T2>, ListUpdateCallback {
    public abstract boolean areContentsTheSame(T2 param1T21, T2 param1T22);
    
    public abstract boolean areItemsTheSame(T2 param1T21, T2 param1T22);
    
    public abstract int compare(T2 param1T21, T2 param1T22);
    
    public abstract void onChanged(int param1Int1, int param1Int2);
    
    public void onChanged(int param1Int1, int param1Int2, Object param1Object) {
      onChanged(param1Int1, param1Int2);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/util/SortedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */