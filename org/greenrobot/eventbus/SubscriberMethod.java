package org.greenrobot.eventbus;

import java.lang.reflect.Method;

public class SubscriberMethod {
  final Class<?> eventType;
  
  final Method method;
  
  String methodString;
  
  final int priority;
  
  final boolean sticky;
  
  final ThreadMode threadMode;
  
  public SubscriberMethod(Method paramMethod, Class<?> paramClass, ThreadMode paramThreadMode, int paramInt, boolean paramBoolean) {
    this.method = paramMethod;
    this.threadMode = paramThreadMode;
    this.eventType = paramClass;
    this.priority = paramInt;
    this.sticky = paramBoolean;
  }
  
  private void checkMethodString() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield methodString : Ljava/lang/String;
    //   6: ifnonnull -> 76
    //   9: new java/lang/StringBuilder
    //   12: astore_1
    //   13: aload_1
    //   14: bipush #64
    //   16: invokespecial <init> : (I)V
    //   19: aload_1
    //   20: aload_0
    //   21: getfield method : Ljava/lang/reflect/Method;
    //   24: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   27: invokevirtual getName : ()Ljava/lang/String;
    //   30: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: pop
    //   34: aload_1
    //   35: bipush #35
    //   37: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   40: aload_0
    //   41: getfield method : Ljava/lang/reflect/Method;
    //   44: invokevirtual getName : ()Ljava/lang/String;
    //   47: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload_1
    //   52: bipush #40
    //   54: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   57: aload_0
    //   58: getfield eventType : Ljava/lang/Class;
    //   61: invokevirtual getName : ()Ljava/lang/String;
    //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: pop
    //   68: aload_0
    //   69: aload_1
    //   70: invokevirtual toString : ()Ljava/lang/String;
    //   73: putfield methodString : Ljava/lang/String;
    //   76: aload_0
    //   77: monitorexit
    //   78: return
    //   79: astore_1
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_1
    //   83: athrow
    // Exception table:
    //   from	to	target	type
    //   2	76	79	finally
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject instanceof SubscriberMethod) {
      checkMethodString();
      paramObject = paramObject;
      paramObject.checkMethodString();
      return this.methodString.equals(((SubscriberMethod)paramObject).methodString);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.method.hashCode();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/SubscriberMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */