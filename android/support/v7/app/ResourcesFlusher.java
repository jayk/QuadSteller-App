package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import java.lang.reflect.Field;

class ResourcesFlusher {
  private static final String TAG = "ResourcesFlusher";
  
  private static Field sDrawableCacheField;
  
  private static boolean sDrawableCacheFieldFetched;
  
  private static Field sResourcesImplField;
  
  private static boolean sResourcesImplFieldFetched;
  
  private static Class sThemedResourceCacheClazz;
  
  private static boolean sThemedResourceCacheClazzFetched;
  
  private static Field sThemedResourceCache_mUnthemedEntriesField;
  
  private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;
  
  static boolean flush(@NonNull Resources paramResources) {
    int i = Build.VERSION.SDK_INT;
    return (i >= 24) ? flushNougats(paramResources) : ((i >= 23) ? flushMarshmallows(paramResources) : ((i >= 21) ? flushLollipops(paramResources) : false));
  }
  
  private static boolean flushLollipops(@NonNull Resources paramResources) {
    // Byte code:
    //   0: getstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched : Z
    //   3: ifne -> 27
    //   6: ldc android/content/res/Resources
    //   8: ldc 'mDrawableCache'
    //   10: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   13: putstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheField : Ljava/lang/reflect/Field;
    //   16: getstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheField : Ljava/lang/reflect/Field;
    //   19: iconst_1
    //   20: invokevirtual setAccessible : (Z)V
    //   23: iconst_1
    //   24: putstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched : Z
    //   27: getstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheField : Ljava/lang/reflect/Field;
    //   30: ifnull -> 88
    //   33: aconst_null
    //   34: astore_1
    //   35: getstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheField : Ljava/lang/reflect/Field;
    //   38: aload_0
    //   39: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   42: checkcast java/util/Map
    //   45: astore_0
    //   46: aload_0
    //   47: ifnull -> 88
    //   50: aload_0
    //   51: invokeinterface clear : ()V
    //   56: iconst_1
    //   57: istore_2
    //   58: iload_2
    //   59: ireturn
    //   60: astore_1
    //   61: ldc 'ResourcesFlusher'
    //   63: ldc 'Could not retrieve Resources#mDrawableCache field'
    //   65: aload_1
    //   66: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   69: pop
    //   70: goto -> 23
    //   73: astore_0
    //   74: ldc 'ResourcesFlusher'
    //   76: ldc 'Could not retrieve value from Resources#mDrawableCache'
    //   78: aload_0
    //   79: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   82: pop
    //   83: aload_1
    //   84: astore_0
    //   85: goto -> 46
    //   88: iconst_0
    //   89: istore_2
    //   90: goto -> 58
    // Exception table:
    //   from	to	target	type
    //   6	23	60	java/lang/NoSuchFieldException
    //   35	46	73	java/lang/IllegalAccessException
  }
  
  private static boolean flushMarshmallows(@NonNull Resources paramResources) {
    boolean bool1 = false;
    boolean bool2 = true;
    if (!sDrawableCacheFieldFetched) {
      try {
        sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
        sDrawableCacheField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", noSuchFieldException);
      } 
      sDrawableCacheFieldFetched = true;
    } 
    Object object = null;
    Object object1 = object;
    if (sDrawableCacheField != null)
      try {
        object1 = sDrawableCacheField.get(paramResources);
      } catch (IllegalAccessException illegalAccessException) {
        Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", illegalAccessException);
        object1 = object;
      }  
    if (object1 != null) {
      if (object1 != null && flushThemedResourcesCache(object1))
        return bool2; 
      bool1 = false;
    } 
    return bool1;
  }
  
  private static boolean flushNougats(@NonNull Resources paramResources) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: iconst_1
    //   3: istore_2
    //   4: getstatic android/support/v7/app/ResourcesFlusher.sResourcesImplFieldFetched : Z
    //   7: ifne -> 31
    //   10: ldc android/content/res/Resources
    //   12: ldc 'mResourcesImpl'
    //   14: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   17: putstatic android/support/v7/app/ResourcesFlusher.sResourcesImplField : Ljava/lang/reflect/Field;
    //   20: getstatic android/support/v7/app/ResourcesFlusher.sResourcesImplField : Ljava/lang/reflect/Field;
    //   23: iconst_1
    //   24: invokevirtual setAccessible : (Z)V
    //   27: iconst_1
    //   28: putstatic android/support/v7/app/ResourcesFlusher.sResourcesImplFieldFetched : Z
    //   31: getstatic android/support/v7/app/ResourcesFlusher.sResourcesImplField : Ljava/lang/reflect/Field;
    //   34: ifnonnull -> 52
    //   37: iload_1
    //   38: ireturn
    //   39: astore_3
    //   40: ldc 'ResourcesFlusher'
    //   42: ldc 'Could not retrieve Resources#mResourcesImpl field'
    //   44: aload_3
    //   45: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   48: pop
    //   49: goto -> 27
    //   52: aconst_null
    //   53: astore_3
    //   54: getstatic android/support/v7/app/ResourcesFlusher.sResourcesImplField : Ljava/lang/reflect/Field;
    //   57: aload_0
    //   58: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   61: astore_0
    //   62: aload_0
    //   63: ifnull -> 37
    //   66: getstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched : Z
    //   69: ifne -> 95
    //   72: aload_0
    //   73: invokevirtual getClass : ()Ljava/lang/Class;
    //   76: ldc 'mDrawableCache'
    //   78: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   81: putstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheField : Ljava/lang/reflect/Field;
    //   84: getstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheField : Ljava/lang/reflect/Field;
    //   87: iconst_1
    //   88: invokevirtual setAccessible : (Z)V
    //   91: iconst_1
    //   92: putstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched : Z
    //   95: aconst_null
    //   96: astore #4
    //   98: aload #4
    //   100: astore_3
    //   101: getstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheField : Ljava/lang/reflect/Field;
    //   104: ifnull -> 115
    //   107: getstatic android/support/v7/app/ResourcesFlusher.sDrawableCacheField : Ljava/lang/reflect/Field;
    //   110: aload_0
    //   111: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   114: astore_3
    //   115: aload_3
    //   116: ifnull -> 175
    //   119: aload_3
    //   120: invokestatic flushThemedResourcesCache : (Ljava/lang/Object;)Z
    //   123: ifeq -> 175
    //   126: iload_2
    //   127: istore_1
    //   128: goto -> 37
    //   131: astore_0
    //   132: ldc 'ResourcesFlusher'
    //   134: ldc 'Could not retrieve value from Resources#mResourcesImpl'
    //   136: aload_0
    //   137: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   140: pop
    //   141: aload_3
    //   142: astore_0
    //   143: goto -> 62
    //   146: astore_3
    //   147: ldc 'ResourcesFlusher'
    //   149: ldc 'Could not retrieve ResourcesImpl#mDrawableCache field'
    //   151: aload_3
    //   152: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   155: pop
    //   156: goto -> 91
    //   159: astore_0
    //   160: ldc 'ResourcesFlusher'
    //   162: ldc 'Could not retrieve value from ResourcesImpl#mDrawableCache'
    //   164: aload_0
    //   165: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   168: pop
    //   169: aload #4
    //   171: astore_3
    //   172: goto -> 115
    //   175: iconst_0
    //   176: istore_1
    //   177: goto -> 128
    // Exception table:
    //   from	to	target	type
    //   10	27	39	java/lang/NoSuchFieldException
    //   54	62	131	java/lang/IllegalAccessException
    //   72	91	146	java/lang/NoSuchFieldException
    //   107	115	159	java/lang/IllegalAccessException
  }
  
  private static boolean flushThemedResourcesCache(@NonNull Object paramObject) {
    // Byte code:
    //   0: getstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCacheClazzFetched : Z
    //   3: ifne -> 18
    //   6: ldc 'android.content.res.ThemedResourceCache'
    //   8: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   11: putstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCacheClazz : Ljava/lang/Class;
    //   14: iconst_1
    //   15: putstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCacheClazzFetched : Z
    //   18: getstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCacheClazz : Ljava/lang/Class;
    //   21: ifnonnull -> 41
    //   24: iconst_0
    //   25: istore_1
    //   26: iload_1
    //   27: ireturn
    //   28: astore_2
    //   29: ldc 'ResourcesFlusher'
    //   31: ldc 'Could not find ThemedResourceCache class'
    //   33: aload_2
    //   34: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   37: pop
    //   38: goto -> 14
    //   41: getstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched : Z
    //   44: ifne -> 69
    //   47: getstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCacheClazz : Ljava/lang/Class;
    //   50: ldc 'mUnthemedEntries'
    //   52: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   55: putstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField : Ljava/lang/reflect/Field;
    //   58: getstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField : Ljava/lang/reflect/Field;
    //   61: iconst_1
    //   62: invokevirtual setAccessible : (Z)V
    //   65: iconst_1
    //   66: putstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched : Z
    //   69: getstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField : Ljava/lang/reflect/Field;
    //   72: ifnonnull -> 93
    //   75: iconst_0
    //   76: istore_1
    //   77: goto -> 26
    //   80: astore_2
    //   81: ldc 'ResourcesFlusher'
    //   83: ldc 'Could not retrieve ThemedResourceCache#mUnthemedEntries field'
    //   85: aload_2
    //   86: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: pop
    //   90: goto -> 65
    //   93: aconst_null
    //   94: astore_2
    //   95: getstatic android/support/v7/app/ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField : Ljava/lang/reflect/Field;
    //   98: aload_0
    //   99: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   102: checkcast android/util/LongSparseArray
    //   105: astore_0
    //   106: aload_0
    //   107: ifnull -> 134
    //   110: aload_0
    //   111: invokevirtual clear : ()V
    //   114: iconst_1
    //   115: istore_1
    //   116: goto -> 26
    //   119: astore_0
    //   120: ldc 'ResourcesFlusher'
    //   122: ldc 'Could not retrieve value from ThemedResourceCache#mUnthemedEntries'
    //   124: aload_0
    //   125: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   128: pop
    //   129: aload_2
    //   130: astore_0
    //   131: goto -> 106
    //   134: iconst_0
    //   135: istore_1
    //   136: goto -> 26
    // Exception table:
    //   from	to	target	type
    //   6	14	28	java/lang/ClassNotFoundException
    //   47	65	80	java/lang/NoSuchFieldException
    //   95	106	119	java/lang/IllegalAccessException
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/app/ResourcesFlusher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */