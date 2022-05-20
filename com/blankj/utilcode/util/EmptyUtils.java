package com.blankj.utilcode.util;

import android.os.Build;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SimpleArrayMap;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public final class EmptyUtils {
  private EmptyUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static boolean isEmpty(Object paramObject) {
    return (paramObject == null) ? true : ((paramObject instanceof String && paramObject.toString().length() == 0) ? true : ((paramObject.getClass().isArray() && Array.getLength(paramObject) == 0) ? true : ((paramObject instanceof Collection && ((Collection)paramObject).isEmpty()) ? true : ((paramObject instanceof Map && ((Map)paramObject).isEmpty()) ? true : ((paramObject instanceof SimpleArrayMap && ((SimpleArrayMap)paramObject).isEmpty()) ? true : ((paramObject instanceof SparseArray && ((SparseArray)paramObject).size() == 0) ? true : ((paramObject instanceof SparseBooleanArray && ((SparseBooleanArray)paramObject).size() == 0) ? true : ((paramObject instanceof SparseIntArray && ((SparseIntArray)paramObject).size() == 0) ? true : ((Build.VERSION.SDK_INT >= 18 && paramObject instanceof SparseLongArray && ((SparseLongArray)paramObject).size() == 0) ? true : ((paramObject instanceof LongSparseArray && ((LongSparseArray)paramObject).size() == 0) ? true : ((Build.VERSION.SDK_INT >= 16 && paramObject instanceof LongSparseArray && ((LongSparseArray)paramObject).size() == 0))))))))))));
  }
  
  public static boolean isNotEmpty(Object paramObject) {
    return !isEmpty(paramObject);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/EmptyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */