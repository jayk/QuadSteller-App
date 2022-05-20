package android.support.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

public final class MultiDex {
  private static final boolean IS_VM_MULTIDEX_CAPABLE;
  
  private static final int MAX_SUPPORTED_SDK_VERSION = 20;
  
  private static final int MIN_SDK_VERSION = 4;
  
  private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
  
  private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";
  
  static final String TAG = "MultiDex";
  
  private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
  
  private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
  
  private static final Set<String> installedApk = new HashSet<String>();
  
  static {
    IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));
  }
  
  private static boolean checkValidZipFiles(List<File> paramList) {
    // Byte code:
    //   0: aload_0
    //   1: invokeinterface iterator : ()Ljava/util/Iterator;
    //   6: astore_0
    //   7: aload_0
    //   8: invokeinterface hasNext : ()Z
    //   13: ifeq -> 35
    //   16: aload_0
    //   17: invokeinterface next : ()Ljava/lang/Object;
    //   22: checkcast java/io/File
    //   25: invokestatic verifyZipFile : (Ljava/io/File;)Z
    //   28: ifne -> 7
    //   31: iconst_0
    //   32: istore_1
    //   33: iload_1
    //   34: ireturn
    //   35: iconst_1
    //   36: istore_1
    //   37: goto -> 33
  }
  
  private static void clearOldDexDir(Context paramContext) throws Exception {
    File[] arrayOfFile;
    File file = new File(paramContext.getFilesDir(), "secondary-dexes");
    if (file.isDirectory()) {
      Log.i("MultiDex", "Clearing old secondary dex dir (" + file.getPath() + ").");
      arrayOfFile = file.listFiles();
      if (arrayOfFile == null) {
        Log.w("MultiDex", "Failed to list secondary dex dir content (" + file.getPath() + ").");
        return;
      } 
    } else {
      return;
    } 
    int i = arrayOfFile.length;
    for (byte b = 0; b < i; b++) {
      File file1 = arrayOfFile[b];
      Log.i("MultiDex", "Trying to delete old file " + file1.getPath() + " of size " + file1.length());
      if (!file1.delete()) {
        Log.w("MultiDex", "Failed to delete old file " + file1.getPath());
      } else {
        Log.i("MultiDex", "Deleted old file " + file1.getPath());
      } 
    } 
    if (!file.delete()) {
      Log.w("MultiDex", "Failed to delete secondary dex dir " + file.getPath());
      return;
    } 
    Log.i("MultiDex", "Deleted old secondary dex dir " + file.getPath());
  }
  
  private static void expandFieldArray(Object paramObject, String paramString, Object[] paramArrayOfObject) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Field field = findField(paramObject, paramString);
    Object[] arrayOfObject2 = (Object[])field.get(paramObject);
    Object[] arrayOfObject1 = (Object[])Array.newInstance(arrayOfObject2.getClass().getComponentType(), arrayOfObject2.length + paramArrayOfObject.length);
    System.arraycopy(arrayOfObject2, 0, arrayOfObject1, 0, arrayOfObject2.length);
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject1, arrayOfObject2.length, paramArrayOfObject.length);
    field.set(paramObject, arrayOfObject1);
  }
  
  private static Field findField(Object paramObject, String paramString) throws NoSuchFieldException {
    Class<?> clazz = paramObject.getClass();
    while (clazz != null) {
      try {
        Field field = clazz.getDeclaredField(paramString);
        if (!field.isAccessible())
          field.setAccessible(true); 
        return field;
      } catch (NoSuchFieldException noSuchFieldException) {
        clazz = clazz.getSuperclass();
      } 
    } 
    throw new NoSuchFieldException("Field " + paramString + " not found in " + paramObject.getClass());
  }
  
  private static Method findMethod(Object paramObject, String paramString, Class<?>... paramVarArgs) throws NoSuchMethodException {
    Class<?> clazz = paramObject.getClass();
    while (clazz != null) {
      try {
        Method method = clazz.getDeclaredMethod(paramString, paramVarArgs);
        if (!method.isAccessible())
          method.setAccessible(true); 
        return method;
      } catch (NoSuchMethodException noSuchMethodException) {
        clazz = clazz.getSuperclass();
      } 
    } 
    throw new NoSuchMethodException("Method " + paramString + " with parameters " + Arrays.asList(paramVarArgs) + " not found in " + paramObject.getClass());
  }
  
  private static ApplicationInfo getApplicationInfo(Context paramContext) throws PackageManager.NameNotFoundException {
    PackageManager packageManager;
    String str;
    Context context = null;
    try {
      packageManager = paramContext.getPackageManager();
      str = paramContext.getPackageName();
      paramContext = context;
      if (packageManager != null) {
        if (str == null)
          return (ApplicationInfo)context; 
      } else {
        return (ApplicationInfo)paramContext;
      } 
    } catch (RuntimeException runtimeException) {
      Log.w("MultiDex", "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", runtimeException);
      return (ApplicationInfo)context;
    } 
    return packageManager.getApplicationInfo(str, 128);
  }
  
  public static void install(Context paramContext) {
    Log.i("MultiDex", "install");
    if (IS_VM_MULTIDEX_CAPABLE) {
      Log.i("MultiDex", "VM has multidex support, MultiDex support library is disabled.");
      return;
    } 
    if (Build.VERSION.SDK_INT < 4)
      throw new RuntimeException("Multi dex installation failed. SDK " + Build.VERSION.SDK_INT + " is unsupported. Min SDK version is " + '\004' + "."); 
    try {
      applicationInfo = getApplicationInfo(paramContext);
      if (applicationInfo != null) {
        String str;
        synchronized (installedApk) {
          str = applicationInfo.sourceDir;
          if (installedApk.contains(str))
            return; 
        } 
        installedApk.add(str);
        if (Build.VERSION.SDK_INT > 20) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          Log.w("MultiDex", stringBuilder.append("MultiDex is not guaranteed to work in SDK version ").append(Build.VERSION.SDK_INT).append(": SDK version higher than ").append(20).append(" should be backed by ").append("runtime with built-in multidex capabilty but it's not the ").append("case here: java.vm.version=\"").append(System.getProperty("java.vm.version")).append("\"").toString());
        } 
        try {
          classLoader = paramContext.getClassLoader();
          if (classLoader == null) {
            Log.e("MultiDex", "Context class loader is null. Must be running in test mode. Skip patching.");
            /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
            return;
          } 
        } catch (RuntimeException null) {
          Log.w("MultiDex", "Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", exception);
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
          return;
        } 
        try {
          clearOldDexDir((Context)exception);
        } catch (Throwable throwable) {}
      } else {
        return;
      } 
    } catch (Exception exception) {
      Log.e("MultiDex", "Multidex installation failure", exception);
      throw new RuntimeException("Multi dex installation failed (" + exception.getMessage() + ").");
    } 
    File file = new File();
    this(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);
    ApplicationInfo applicationInfo;
    ClassLoader classLoader;
    List<File> list = MultiDexExtractor.load((Context)exception, applicationInfo, file, false);
    if (checkValidZipFiles(list)) {
      installSecondaryDexes(classLoader, file, list);
    } else {
      Log.w("MultiDex", "Files were not valid zip files.  Forcing a reload.");
      List<File> list1 = MultiDexExtractor.load((Context)exception, applicationInfo, file, true);
      if (checkValidZipFiles(list1)) {
        installSecondaryDexes(classLoader, file, list1);
      } else {
        RuntimeException runtimeException = new RuntimeException();
        this("Zip files were not valid.");
        throw runtimeException;
      } 
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
    Log.i("MultiDex", "install done");
  }
  
  private static void installSecondaryDexes(ClassLoader paramClassLoader, File paramFile, List<File> paramList) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException {
    if (!paramList.isEmpty()) {
      if (Build.VERSION.SDK_INT >= 19) {
        V19.install(paramClassLoader, paramList, paramFile);
        return;
      } 
    } else {
      return;
    } 
    if (Build.VERSION.SDK_INT >= 14) {
      V14.install(paramClassLoader, paramList, paramFile);
      return;
    } 
    V4.install(paramClassLoader, paramList);
  }
  
  static boolean isVMMultidexCapable(String paramString) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramString != null) {
      Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(paramString);
      bool2 = bool1;
      if (matcher.matches())
        try {
          int i = Integer.parseInt(matcher.group(1));
          int j = Integer.parseInt(matcher.group(2));
          if (i > 2 || (i == 2 && j >= 1)) {
            bool2 = true;
          } else {
            bool2 = false;
          } 
        } catch (NumberFormatException numberFormatException) {
          bool2 = bool1;
        }  
    } 
    StringBuilder stringBuilder = (new StringBuilder()).append("VM with version ").append(paramString);
    if (bool2) {
      paramString = " has multidex support";
      Log.i("MultiDex", stringBuilder.append(paramString).toString());
      return bool2;
    } 
    paramString = " does not have multidex support";
    Log.i("MultiDex", stringBuilder.append(paramString).toString());
    return bool2;
  }
  
  private static final class V14 {
    private static void install(ClassLoader param1ClassLoader, List<File> param1List, File param1File) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
      Object object = MultiDex.findField(param1ClassLoader, "pathList").get(param1ClassLoader);
      MultiDex.expandFieldArray(object, "dexElements", makeDexElements(object, new ArrayList<File>(param1List), param1File));
    }
    
    private static Object[] makeDexElements(Object param1Object, ArrayList<File> param1ArrayList, File param1File) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return (Object[])MultiDex.findMethod(param1Object, "makeDexElements", new Class[] { ArrayList.class, File.class }).invoke(param1Object, new Object[] { param1ArrayList, param1File });
    }
  }
  
  private static final class V19 {
    private static void install(ClassLoader param1ClassLoader, List<File> param1List, File param1File) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
      Object object = MultiDex.findField(param1ClassLoader, "pathList").get(param1ClassLoader);
      ArrayList<IOException> arrayList = new ArrayList();
      MultiDex.expandFieldArray(object, "dexElements", makeDexElements(object, new ArrayList<File>(param1List), param1File, arrayList));
      if (arrayList.size() > 0) {
        IOException[] arrayOfIOException;
        Iterator<IOException> iterator = arrayList.iterator();
        while (iterator.hasNext())
          Log.w("MultiDex", "Exception in makeDexElement", iterator.next()); 
        Field field = MultiDex.findField(param1ClassLoader, "dexElementsSuppressedExceptions");
        object = field.get(param1ClassLoader);
        if (object == null) {
          arrayOfIOException = arrayList.<IOException>toArray(new IOException[arrayList.size()]);
        } else {
          arrayOfIOException = new IOException[arrayList.size() + object.length];
          arrayList.toArray(arrayOfIOException);
          System.arraycopy(object, 0, arrayOfIOException, arrayList.size(), object.length);
        } 
        field.set(param1ClassLoader, arrayOfIOException);
      } 
    }
    
    private static Object[] makeDexElements(Object param1Object, ArrayList<File> param1ArrayList, File param1File, ArrayList<IOException> param1ArrayList1) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return (Object[])MultiDex.findMethod(param1Object, "makeDexElements", new Class[] { ArrayList.class, File.class, ArrayList.class }).invoke(param1Object, new Object[] { param1ArrayList, param1File, param1ArrayList1 });
    }
  }
  
  private static final class V4 {
    private static void install(ClassLoader param1ClassLoader, List<File> param1List) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException {
      int i = param1List.size();
      Field field = MultiDex.findField(param1ClassLoader, "path");
      StringBuilder stringBuilder = new StringBuilder((String)field.get(param1ClassLoader));
      String[] arrayOfString = new String[i];
      File[] arrayOfFile = new File[i];
      ZipFile[] arrayOfZipFile = new ZipFile[i];
      DexFile[] arrayOfDexFile = new DexFile[i];
      ListIterator<File> listIterator = param1List.listIterator();
      while (listIterator.hasNext()) {
        File file = listIterator.next();
        String str = file.getAbsolutePath();
        stringBuilder.append(':').append(str);
        i = listIterator.previousIndex();
        arrayOfString[i] = str;
        arrayOfFile[i] = file;
        arrayOfZipFile[i] = new ZipFile(file);
        arrayOfDexFile[i] = DexFile.loadDex(str, str + ".dex", 0);
      } 
      field.set(param1ClassLoader, stringBuilder.toString());
      MultiDex.expandFieldArray(param1ClassLoader, "mPaths", (Object[])arrayOfString);
      MultiDex.expandFieldArray(param1ClassLoader, "mFiles", (Object[])arrayOfFile);
      MultiDex.expandFieldArray(param1ClassLoader, "mZips", (Object[])arrayOfZipFile);
      MultiDex.expandFieldArray(param1ClassLoader, "mDexs", (Object[])arrayOfDexFile);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/multidex/MultiDex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */