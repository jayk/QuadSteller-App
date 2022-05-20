package org.greenrobot.eventbus.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExceptionToResourceMapping {
  public final Map<Class<? extends Throwable>, Integer> throwableToMsgIdMap = new HashMap<Class<? extends Throwable>, Integer>();
  
  public ExceptionToResourceMapping addMapping(Class<? extends Throwable> paramClass, int paramInt) {
    this.throwableToMsgIdMap.put(paramClass, Integer.valueOf(paramInt));
    return this;
  }
  
  public Integer mapThrowable(Throwable paramThrowable) {
    // Byte code:
    //   0: aload_1
    //   1: astore_2
    //   2: bipush #20
    //   4: istore_3
    //   5: aload_0
    //   6: aload_2
    //   7: invokevirtual mapThrowableFlat : (Ljava/lang/Throwable;)Ljava/lang/Integer;
    //   10: astore #4
    //   12: aload #4
    //   14: ifnull -> 22
    //   17: aload #4
    //   19: astore_1
    //   20: aload_1
    //   21: areturn
    //   22: aload_2
    //   23: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   26: astore #4
    //   28: iinc #3, -1
    //   31: iload_3
    //   32: ifle -> 49
    //   35: aload #4
    //   37: aload_1
    //   38: if_acmpeq -> 49
    //   41: aload #4
    //   43: astore_2
    //   44: aload #4
    //   46: ifnonnull -> 5
    //   49: getstatic org/greenrobot/eventbus/EventBus.TAG : Ljava/lang/String;
    //   52: new java/lang/StringBuilder
    //   55: dup
    //   56: invokespecial <init> : ()V
    //   59: ldc 'No specific message ressource ID found for '
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: aload_1
    //   65: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   68: invokevirtual toString : ()Ljava/lang/String;
    //   71: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   74: pop
    //   75: aconst_null
    //   76: astore_1
    //   77: goto -> 20
  }
  
  protected Integer mapThrowableFlat(Throwable paramThrowable) {
    Class<?> clazz2;
    Class<?> clazz1 = paramThrowable.getClass();
    Integer integer1 = this.throwableToMsgIdMap.get(clazz1);
    Integer integer2 = integer1;
    if (integer1 == null) {
      Class<?> clazz = null;
      Iterator<Map.Entry> iterator = this.throwableToMsgIdMap.entrySet().iterator();
      while (true) {
        integer2 = integer1;
        if (iterator.hasNext()) {
          Map.Entry entry = iterator.next();
          clazz2 = (Class)entry.getKey();
          if (clazz2.isAssignableFrom(clazz1) && (clazz == null || clazz.isAssignableFrom(clazz2))) {
            clazz = clazz2;
            integer1 = (Integer)entry.getValue();
          } 
          continue;
        } 
        break;
      } 
    } 
    return (Integer)clazz2;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/util/ExceptionToResourceMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */