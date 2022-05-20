package org.greenrobot.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.greenrobot.eventbus.meta.SubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

class SubscriberMethodFinder {
  private static final int BRIDGE = 64;
  
  private static final FindState[] FIND_STATE_POOL;
  
  private static final Map<Class<?>, List<SubscriberMethod>> METHOD_CACHE = new ConcurrentHashMap<Class<?>, List<SubscriberMethod>>();
  
  private static final int MODIFIERS_IGNORE = 5192;
  
  private static final int POOL_SIZE = 4;
  
  private static final int SYNTHETIC = 4096;
  
  private final boolean ignoreGeneratedIndex;
  
  private final boolean strictMethodVerification;
  
  private List<SubscriberInfoIndex> subscriberInfoIndexes;
  
  static {
    FIND_STATE_POOL = new FindState[4];
  }
  
  SubscriberMethodFinder(List<SubscriberInfoIndex> paramList, boolean paramBoolean1, boolean paramBoolean2) {
    this.subscriberInfoIndexes = paramList;
    this.strictMethodVerification = paramBoolean1;
    this.ignoreGeneratedIndex = paramBoolean2;
  }
  
  static void clearCaches() {
    METHOD_CACHE.clear();
  }
  
  private List<SubscriberMethod> findUsingInfo(Class<?> paramClass) {
    FindState findState = prepareFindState();
    findState.initForSubscriber(paramClass);
    while (findState.clazz != null) {
      findState.subscriberInfo = getSubscriberInfo(findState);
      if (findState.subscriberInfo != null) {
        for (SubscriberMethod subscriberMethod : findState.subscriberInfo.getSubscriberMethods()) {
          if (findState.checkAdd(subscriberMethod.method, subscriberMethod.eventType))
            findState.subscriberMethods.add(subscriberMethod); 
        } 
      } else {
        findUsingReflectionInSingleClass(findState);
      } 
      findState.moveToSuperclass();
    } 
    return getMethodsAndRelease(findState);
  }
  
  private List<SubscriberMethod> findUsingReflection(Class<?> paramClass) {
    FindState findState = prepareFindState();
    findState.initForSubscriber(paramClass);
    while (findState.clazz != null) {
      findUsingReflectionInSingleClass(findState);
      findState.moveToSuperclass();
    } 
    return getMethodsAndRelease(findState);
  }
  
  private void findUsingReflectionInSingleClass(FindState paramFindState) {
    // Byte code:
    //   0: aload_1
    //   1: getfield clazz : Ljava/lang/Class;
    //   4: invokevirtual getDeclaredMethods : ()[Ljava/lang/reflect/Method;
    //   7: astore_2
    //   8: aload_2
    //   9: arraylength
    //   10: istore_3
    //   11: iconst_0
    //   12: istore #4
    //   14: iload #4
    //   16: iload_3
    //   17: if_icmpge -> 335
    //   20: aload_2
    //   21: iload #4
    //   23: aaload
    //   24: astore #5
    //   26: aload #5
    //   28: invokevirtual getModifiers : ()I
    //   31: istore #6
    //   33: iload #6
    //   35: iconst_1
    //   36: iand
    //   37: ifeq -> 256
    //   40: iload #6
    //   42: sipush #5192
    //   45: iand
    //   46: ifne -> 256
    //   49: aload #5
    //   51: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   54: astore #7
    //   56: aload #7
    //   58: arraylength
    //   59: iconst_1
    //   60: if_icmpne -> 166
    //   63: aload #5
    //   65: ldc org/greenrobot/eventbus/Subscribe
    //   67: invokevirtual getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   70: checkcast org/greenrobot/eventbus/Subscribe
    //   73: astore #8
    //   75: aload #8
    //   77: ifnull -> 143
    //   80: aload #7
    //   82: iconst_0
    //   83: aaload
    //   84: astore #7
    //   86: aload_1
    //   87: aload #5
    //   89: aload #7
    //   91: invokevirtual checkAdd : (Ljava/lang/reflect/Method;Ljava/lang/Class;)Z
    //   94: ifeq -> 143
    //   97: aload #8
    //   99: invokeinterface threadMode : ()Lorg/greenrobot/eventbus/ThreadMode;
    //   104: astore #9
    //   106: aload_1
    //   107: getfield subscriberMethods : Ljava/util/List;
    //   110: new org/greenrobot/eventbus/SubscriberMethod
    //   113: dup
    //   114: aload #5
    //   116: aload #7
    //   118: aload #9
    //   120: aload #8
    //   122: invokeinterface priority : ()I
    //   127: aload #8
    //   129: invokeinterface sticky : ()Z
    //   134: invokespecial <init> : (Ljava/lang/reflect/Method;Ljava/lang/Class;Lorg/greenrobot/eventbus/ThreadMode;IZ)V
    //   137: invokeinterface add : (Ljava/lang/Object;)Z
    //   142: pop
    //   143: iinc #4, 1
    //   146: goto -> 14
    //   149: astore_2
    //   150: aload_1
    //   151: getfield clazz : Ljava/lang/Class;
    //   154: invokevirtual getMethods : ()[Ljava/lang/reflect/Method;
    //   157: astore_2
    //   158: aload_1
    //   159: iconst_1
    //   160: putfield skipSuperClasses : Z
    //   163: goto -> 8
    //   166: aload_0
    //   167: getfield strictMethodVerification : Z
    //   170: ifeq -> 143
    //   173: aload #5
    //   175: ldc org/greenrobot/eventbus/Subscribe
    //   177: invokevirtual isAnnotationPresent : (Ljava/lang/Class;)Z
    //   180: ifeq -> 143
    //   183: new java/lang/StringBuilder
    //   186: dup
    //   187: invokespecial <init> : ()V
    //   190: aload #5
    //   192: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   195: invokevirtual getName : ()Ljava/lang/String;
    //   198: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: ldc '.'
    //   203: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   206: aload #5
    //   208: invokevirtual getName : ()Ljava/lang/String;
    //   211: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   214: invokevirtual toString : ()Ljava/lang/String;
    //   217: astore_1
    //   218: new org/greenrobot/eventbus/EventBusException
    //   221: dup
    //   222: new java/lang/StringBuilder
    //   225: dup
    //   226: invokespecial <init> : ()V
    //   229: ldc '@Subscribe method '
    //   231: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: aload_1
    //   235: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   238: ldc 'must have exactly 1 parameter but has '
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: aload #7
    //   245: arraylength
    //   246: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   249: invokevirtual toString : ()Ljava/lang/String;
    //   252: invokespecial <init> : (Ljava/lang/String;)V
    //   255: athrow
    //   256: aload_0
    //   257: getfield strictMethodVerification : Z
    //   260: ifeq -> 143
    //   263: aload #5
    //   265: ldc org/greenrobot/eventbus/Subscribe
    //   267: invokevirtual isAnnotationPresent : (Ljava/lang/Class;)Z
    //   270: ifeq -> 143
    //   273: new java/lang/StringBuilder
    //   276: dup
    //   277: invokespecial <init> : ()V
    //   280: aload #5
    //   282: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   285: invokevirtual getName : ()Ljava/lang/String;
    //   288: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   291: ldc '.'
    //   293: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   296: aload #5
    //   298: invokevirtual getName : ()Ljava/lang/String;
    //   301: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   304: invokevirtual toString : ()Ljava/lang/String;
    //   307: astore_1
    //   308: new org/greenrobot/eventbus/EventBusException
    //   311: dup
    //   312: new java/lang/StringBuilder
    //   315: dup
    //   316: invokespecial <init> : ()V
    //   319: aload_1
    //   320: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: ldc ' is a illegal @Subscribe method: must be public, non-static, and non-abstract'
    //   325: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   328: invokevirtual toString : ()Ljava/lang/String;
    //   331: invokespecial <init> : (Ljava/lang/String;)V
    //   334: athrow
    //   335: return
    // Exception table:
    //   from	to	target	type
    //   0	8	149	java/lang/Throwable
  }
  
  private List<SubscriberMethod> getMethodsAndRelease(FindState paramFindState) {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: aload_1
    //   5: getfield subscriberMethods : Ljava/util/List;
    //   8: invokespecial <init> : (Ljava/util/Collection;)V
    //   11: astore_2
    //   12: aload_1
    //   13: invokevirtual recycle : ()V
    //   16: getstatic org/greenrobot/eventbus/SubscriberMethodFinder.FIND_STATE_POOL : [Lorg/greenrobot/eventbus/SubscriberMethodFinder$FindState;
    //   19: astore_3
    //   20: aload_3
    //   21: monitorenter
    //   22: iconst_0
    //   23: istore #4
    //   25: iload #4
    //   27: iconst_4
    //   28: if_icmpge -> 47
    //   31: getstatic org/greenrobot/eventbus/SubscriberMethodFinder.FIND_STATE_POOL : [Lorg/greenrobot/eventbus/SubscriberMethodFinder$FindState;
    //   34: iload #4
    //   36: aaload
    //   37: ifnonnull -> 51
    //   40: getstatic org/greenrobot/eventbus/SubscriberMethodFinder.FIND_STATE_POOL : [Lorg/greenrobot/eventbus/SubscriberMethodFinder$FindState;
    //   43: iload #4
    //   45: aload_1
    //   46: aastore
    //   47: aload_3
    //   48: monitorexit
    //   49: aload_2
    //   50: areturn
    //   51: iinc #4, 1
    //   54: goto -> 25
    //   57: astore_1
    //   58: aload_3
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   31	47	57	finally
    //   47	49	57	finally
    //   58	60	57	finally
  }
  
  private SubscriberInfo getSubscriberInfo(FindState paramFindState) {
    // Byte code:
    //   0: aload_1
    //   1: getfield subscriberInfo : Lorg/greenrobot/eventbus/meta/SubscriberInfo;
    //   4: ifnull -> 46
    //   7: aload_1
    //   8: getfield subscriberInfo : Lorg/greenrobot/eventbus/meta/SubscriberInfo;
    //   11: invokeinterface getSuperSubscriberInfo : ()Lorg/greenrobot/eventbus/meta/SubscriberInfo;
    //   16: ifnull -> 46
    //   19: aload_1
    //   20: getfield subscriberInfo : Lorg/greenrobot/eventbus/meta/SubscriberInfo;
    //   23: invokeinterface getSuperSubscriberInfo : ()Lorg/greenrobot/eventbus/meta/SubscriberInfo;
    //   28: astore_2
    //   29: aload_1
    //   30: getfield clazz : Ljava/lang/Class;
    //   33: aload_2
    //   34: invokeinterface getSubscriberClass : ()Ljava/lang/Class;
    //   39: if_acmpne -> 46
    //   42: aload_2
    //   43: astore_1
    //   44: aload_1
    //   45: areturn
    //   46: aload_0
    //   47: getfield subscriberInfoIndexes : Ljava/util/List;
    //   50: ifnull -> 100
    //   53: aload_0
    //   54: getfield subscriberInfoIndexes : Ljava/util/List;
    //   57: invokeinterface iterator : ()Ljava/util/Iterator;
    //   62: astore_3
    //   63: aload_3
    //   64: invokeinterface hasNext : ()Z
    //   69: ifeq -> 100
    //   72: aload_3
    //   73: invokeinterface next : ()Ljava/lang/Object;
    //   78: checkcast org/greenrobot/eventbus/meta/SubscriberInfoIndex
    //   81: aload_1
    //   82: getfield clazz : Ljava/lang/Class;
    //   85: invokeinterface getSubscriberInfo : (Ljava/lang/Class;)Lorg/greenrobot/eventbus/meta/SubscriberInfo;
    //   90: astore_2
    //   91: aload_2
    //   92: ifnull -> 63
    //   95: aload_2
    //   96: astore_1
    //   97: goto -> 44
    //   100: aconst_null
    //   101: astore_1
    //   102: goto -> 44
  }
  
  private FindState prepareFindState() {
    // Byte code:
    //   0: getstatic org/greenrobot/eventbus/SubscriberMethodFinder.FIND_STATE_POOL : [Lorg/greenrobot/eventbus/SubscriberMethodFinder$FindState;
    //   3: astore_1
    //   4: aload_1
    //   5: monitorenter
    //   6: iconst_0
    //   7: istore_2
    //   8: iload_2
    //   9: iconst_4
    //   10: if_icmpge -> 39
    //   13: getstatic org/greenrobot/eventbus/SubscriberMethodFinder.FIND_STATE_POOL : [Lorg/greenrobot/eventbus/SubscriberMethodFinder$FindState;
    //   16: iload_2
    //   17: aaload
    //   18: astore_3
    //   19: aload_3
    //   20: ifnull -> 33
    //   23: getstatic org/greenrobot/eventbus/SubscriberMethodFinder.FIND_STATE_POOL : [Lorg/greenrobot/eventbus/SubscriberMethodFinder$FindState;
    //   26: iload_2
    //   27: aconst_null
    //   28: aastore
    //   29: aload_1
    //   30: monitorexit
    //   31: aload_3
    //   32: areturn
    //   33: iinc #2, 1
    //   36: goto -> 8
    //   39: aload_1
    //   40: monitorexit
    //   41: new org/greenrobot/eventbus/SubscriberMethodFinder$FindState
    //   44: dup
    //   45: invokespecial <init> : ()V
    //   48: astore_3
    //   49: goto -> 31
    //   52: astore_3
    //   53: aload_1
    //   54: monitorexit
    //   55: aload_3
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   13	19	52	finally
    //   23	31	52	finally
    //   39	41	52	finally
    //   53	55	52	finally
  }
  
  List<SubscriberMethod> findSubscriberMethods(Class<?> paramClass) {
    List<SubscriberMethod> list = METHOD_CACHE.get(paramClass);
    if (list != null)
      return list; 
    if (this.ignoreGeneratedIndex) {
      list = findUsingReflection(paramClass);
    } else {
      list = findUsingInfo(paramClass);
    } 
    if (list.isEmpty())
      throw new EventBusException("Subscriber " + paramClass + " and its super classes have no public methods with the @Subscribe annotation"); 
    METHOD_CACHE.put(paramClass, list);
    return list;
  }
  
  static class FindState {
    final Map<Class, Object> anyMethodByEventType = (Map)new HashMap<Class<?>, Object>();
    
    Class<?> clazz;
    
    final StringBuilder methodKeyBuilder = new StringBuilder(128);
    
    boolean skipSuperClasses;
    
    Class<?> subscriberClass;
    
    final Map<String, Class> subscriberClassByMethodKey = (Map)new HashMap<String, Class<?>>();
    
    SubscriberInfo subscriberInfo;
    
    final List<SubscriberMethod> subscriberMethods = new ArrayList<SubscriberMethod>();
    
    private boolean checkAddWithMethodSignature(Method param1Method, Class<?> param1Class) {
      boolean bool = false;
      this.methodKeyBuilder.setLength(0);
      this.methodKeyBuilder.append(param1Method.getName());
      this.methodKeyBuilder.append('>').append(param1Class.getName());
      String str = this.methodKeyBuilder.toString();
      Class<?> clazz = param1Method.getDeclaringClass();
      Class clazz1 = this.subscriberClassByMethodKey.put(str, clazz);
      if (clazz1 == null || clazz1.isAssignableFrom(clazz))
        return true; 
      this.subscriberClassByMethodKey.put(str, clazz1);
      return bool;
    }
    
    boolean checkAdd(Method param1Method, Class<?> param1Class) {
      Object object = this.anyMethodByEventType.put(param1Class, param1Method);
      if (object == null)
        return true; 
      if (object instanceof Method) {
        if (!checkAddWithMethodSignature((Method)object, param1Class))
          throw new IllegalStateException(); 
        this.anyMethodByEventType.put(param1Class, this);
      } 
      return checkAddWithMethodSignature(param1Method, param1Class);
    }
    
    void initForSubscriber(Class<?> param1Class) {
      this.clazz = param1Class;
      this.subscriberClass = param1Class;
      this.skipSuperClasses = false;
      this.subscriberInfo = null;
    }
    
    void moveToSuperclass() {
      if (this.skipSuperClasses) {
        this.clazz = null;
        return;
      } 
      this.clazz = this.clazz.getSuperclass();
      String str = this.clazz.getName();
      if (str.startsWith("java.") || str.startsWith("javax.") || str.startsWith("android."))
        this.clazz = null; 
    }
    
    void recycle() {
      this.subscriberMethods.clear();
      this.anyMethodByEventType.clear();
      this.subscriberClassByMethodKey.clear();
      this.methodKeyBuilder.setLength(0);
      this.subscriberClass = null;
      this.clazz = null;
      this.skipSuperClasses = false;
      this.subscriberInfo = null;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/SubscriberMethodFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */