package com.google.gson.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator {
  public static UnsafeAllocator create() {
    try {
      Class<?> clazz = Class.forName("sun.misc.Unsafe");
      Field field = clazz.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Object object = field.get((Object)null);
      Method method = clazz.getMethod("allocateInstance", new Class[] { Class.class });
      UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
          public <T> T newInstance(Class<T> param1Class) throws Exception {
            return (T)allocateInstance.invoke(unsafe, new Object[] { param1Class });
          }
        };
      super(method, object);
    } catch (Exception exception) {}
    return (UnsafeAllocator)exception;
  }
  
  public abstract <T> T newInstance(Class<T> paramClass) throws Exception;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/UnsafeAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */