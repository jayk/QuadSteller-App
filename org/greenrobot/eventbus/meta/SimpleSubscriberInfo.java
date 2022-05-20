package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.SubscriberMethod;

public class SimpleSubscriberInfo extends AbstractSubscriberInfo {
  private final SubscriberMethodInfo[] methodInfos;
  
  public SimpleSubscriberInfo(Class paramClass, boolean paramBoolean, SubscriberMethodInfo[] paramArrayOfSubscriberMethodInfo) {
    super(paramClass, null, paramBoolean);
    this.methodInfos = paramArrayOfSubscriberMethodInfo;
  }
  
  public SubscriberMethod[] getSubscriberMethods() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield methodInfos : [Lorg/greenrobot/eventbus/meta/SubscriberMethodInfo;
    //   6: arraylength
    //   7: istore_1
    //   8: iload_1
    //   9: anewarray org/greenrobot/eventbus/SubscriberMethod
    //   12: astore_2
    //   13: iconst_0
    //   14: istore_3
    //   15: iload_3
    //   16: iload_1
    //   17: if_icmpge -> 66
    //   20: aload_0
    //   21: getfield methodInfos : [Lorg/greenrobot/eventbus/meta/SubscriberMethodInfo;
    //   24: iload_3
    //   25: aaload
    //   26: astore #4
    //   28: aload_2
    //   29: iload_3
    //   30: aload_0
    //   31: aload #4
    //   33: getfield methodName : Ljava/lang/String;
    //   36: aload #4
    //   38: getfield eventType : Ljava/lang/Class;
    //   41: aload #4
    //   43: getfield threadMode : Lorg/greenrobot/eventbus/ThreadMode;
    //   46: aload #4
    //   48: getfield priority : I
    //   51: aload #4
    //   53: getfield sticky : Z
    //   56: invokevirtual createSubscriberMethod : (Ljava/lang/String;Ljava/lang/Class;Lorg/greenrobot/eventbus/ThreadMode;IZ)Lorg/greenrobot/eventbus/SubscriberMethod;
    //   59: aastore
    //   60: iinc #3, 1
    //   63: goto -> 15
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_2
    //   69: areturn
    //   70: astore #4
    //   72: aload_0
    //   73: monitorexit
    //   74: aload #4
    //   76: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	70	finally
    //   20	60	70	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/meta/SimpleSubscriberInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */