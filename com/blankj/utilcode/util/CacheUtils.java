package com.blankj.utilcode.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONArray;
import org.json.JSONObject;

public class CacheUtils {
  private static final SimpleArrayMap<String, CacheUtils> CACHE_MAP = new SimpleArrayMap();
  
  public static final int DAY = 86400;
  
  private static final int DEFAULT_MAX_COUNT = 2147483647;
  
  private static final long DEFAULT_MAX_SIZE = 9223372036854775807L;
  
  public static final int HOUR = 3600;
  
  public static final int MIN = 60;
  
  public static final int SEC = 1;
  
  private CacheManager mCacheManager;
  
  private CacheUtils(@NonNull File paramFile, long paramLong, int paramInt) {
    if (!paramFile.exists() && !paramFile.mkdirs())
      throw new RuntimeException("can't make dirs in " + paramFile.getAbsolutePath()); 
    this.mCacheManager = new CacheManager(paramFile, paramLong, paramInt);
  }
  
  public static CacheUtils getInstance() {
    return getInstance("", Long.MAX_VALUE, 2147483647);
  }
  
  public static CacheUtils getInstance(long paramLong, int paramInt) {
    return getInstance("", paramLong, paramInt);
  }
  
  public static CacheUtils getInstance(@NonNull File paramFile) {
    return getInstance(paramFile, Long.MAX_VALUE, 2147483647);
  }
  
  public static CacheUtils getInstance(@NonNull File paramFile, long paramLong, int paramInt) {
    String str = paramFile.getAbsoluteFile() + "_" + Process.myPid();
    CacheUtils cacheUtils1 = (CacheUtils)CACHE_MAP.get(str);
    CacheUtils cacheUtils2 = cacheUtils1;
    if (cacheUtils1 == null) {
      cacheUtils2 = new CacheUtils(paramFile, paramLong, paramInt);
      CACHE_MAP.put(str, cacheUtils2);
    } 
    return cacheUtils2;
  }
  
  public static CacheUtils getInstance(String paramString) {
    return getInstance(paramString, Long.MAX_VALUE, 2147483647);
  }
  
  public static CacheUtils getInstance(String paramString, long paramLong, int paramInt) {
    String str = paramString;
    if (isSpace(paramString))
      str = "cacheUtils"; 
    return getInstance(new File(Utils.getApp().getCacheDir(), str), paramLong, paramInt);
  }
  
  private static boolean isSpace(String paramString) {
    boolean bool = true;
    if (paramString == null)
      return bool; 
    byte b = 0;
    int i = paramString.length();
    while (true) {
      boolean bool1 = bool;
      if (b < i) {
        if (!Character.isWhitespace(paramString.charAt(b)))
          return false; 
        b++;
        continue;
      } 
      return bool1;
    } 
  }
  
  public boolean clear() {
    return this.mCacheManager.clear();
  }
  
  public Bitmap getBitmap(@NonNull String paramString) {
    return getBitmap(paramString, null);
  }
  
  public Bitmap getBitmap(@NonNull String paramString, Bitmap paramBitmap) {
    byte[] arrayOfByte = getBytes(paramString);
    if (arrayOfByte != null)
      paramBitmap = CacheHelper.bytes2Bitmap(arrayOfByte); 
    return paramBitmap;
  }
  
  public byte[] getBytes(@NonNull String paramString) {
    return getBytes(paramString, null);
  }
  
  public byte[] getBytes(@NonNull String paramString, byte[] paramArrayOfbyte) {
    File file = this.mCacheManager.getFileIfExists(paramString);
    if (file != null) {
      byte[] arrayOfByte = CacheHelper.readFile2Bytes(file);
      if (CacheHelper.isDue(arrayOfByte)) {
        this.mCacheManager.removeByKey(paramString);
        return paramArrayOfbyte;
      } 
      this.mCacheManager.updateModify(file);
      paramArrayOfbyte = CacheHelper.getDataWithoutDueTime(arrayOfByte);
    } 
    return paramArrayOfbyte;
  }
  
  public int getCacheCount() {
    return this.mCacheManager.getCacheCount();
  }
  
  public long getCacheSize() {
    return this.mCacheManager.getCacheSize();
  }
  
  public Drawable getDrawable(@NonNull String paramString) {
    return getDrawable(paramString, null);
  }
  
  public Drawable getDrawable(@NonNull String paramString, Drawable paramDrawable) {
    byte[] arrayOfByte = getBytes(paramString);
    if (arrayOfByte != null)
      paramDrawable = CacheHelper.bytes2Drawable(arrayOfByte); 
    return paramDrawable;
  }
  
  public JSONArray getJSONArray(@NonNull String paramString) {
    return getJSONArray(paramString, null);
  }
  
  public JSONArray getJSONArray(@NonNull String paramString, JSONArray paramJSONArray) {
    byte[] arrayOfByte = getBytes(paramString);
    if (arrayOfByte != null)
      paramJSONArray = CacheHelper.bytes2JSONArray(arrayOfByte); 
    return paramJSONArray;
  }
  
  public JSONObject getJSONObject(@NonNull String paramString) {
    return getJSONObject(paramString, null);
  }
  
  public JSONObject getJSONObject(@NonNull String paramString, JSONObject paramJSONObject) {
    byte[] arrayOfByte = getBytes(paramString);
    if (arrayOfByte != null)
      paramJSONObject = CacheHelper.bytes2JSONObject(arrayOfByte); 
    return paramJSONObject;
  }
  
  public <T> T getParcelable(@NonNull String paramString, @NonNull Parcelable.Creator<T> paramCreator) {
    return getParcelable(paramString, paramCreator, null);
  }
  
  public <T> T getParcelable(@NonNull String paramString, @NonNull Parcelable.Creator<T> paramCreator, T paramT) {
    byte[] arrayOfByte = getBytes(paramString);
    if (arrayOfByte != null)
      paramT = CacheHelper.bytes2Parcelable(arrayOfByte, paramCreator); 
    return paramT;
  }
  
  public Object getSerializable(@NonNull String paramString) {
    return getSerializable(paramString, null);
  }
  
  public Object getSerializable(@NonNull String paramString, Object paramObject) {
    if (getBytes(paramString) != null)
      paramObject = CacheHelper.bytes2Object(getBytes(paramString)); 
    return paramObject;
  }
  
  public String getString(@NonNull String paramString) {
    return getString(paramString, null);
  }
  
  public String getString(@NonNull String paramString1, String paramString2) {
    byte[] arrayOfByte = getBytes(paramString1);
    if (arrayOfByte != null)
      paramString2 = CacheHelper.bytes2String(arrayOfByte); 
    return paramString2;
  }
  
  public void put(@NonNull String paramString, @NonNull Bitmap paramBitmap) {
    put(paramString, paramBitmap, -1);
  }
  
  public void put(@NonNull String paramString, @NonNull Bitmap paramBitmap, int paramInt) {
    put(paramString, CacheHelper.bitmap2Bytes(paramBitmap), paramInt);
  }
  
  public void put(@NonNull String paramString, @NonNull Drawable paramDrawable) {
    put(paramString, CacheHelper.drawable2Bytes(paramDrawable));
  }
  
  public void put(@NonNull String paramString, @NonNull Drawable paramDrawable, int paramInt) {
    put(paramString, CacheHelper.drawable2Bytes(paramDrawable), paramInt);
  }
  
  public void put(@NonNull String paramString, @NonNull Parcelable paramParcelable) {
    put(paramString, paramParcelable, -1);
  }
  
  public void put(@NonNull String paramString, @NonNull Parcelable paramParcelable, int paramInt) {
    put(paramString, CacheHelper.parcelable2Bytes(paramParcelable), paramInt);
  }
  
  public void put(@NonNull String paramString, @NonNull Serializable paramSerializable) {
    put(paramString, paramSerializable, -1);
  }
  
  public void put(@NonNull String paramString, @NonNull Serializable paramSerializable, int paramInt) {
    put(paramString, CacheHelper.serializable2Bytes(paramSerializable), paramInt);
  }
  
  public void put(@NonNull String paramString1, @NonNull String paramString2) {
    put(paramString1, paramString2, -1);
  }
  
  public void put(@NonNull String paramString1, @NonNull String paramString2, int paramInt) {
    put(paramString1, CacheHelper.string2Bytes(paramString2), paramInt);
  }
  
  public void put(@NonNull String paramString, @NonNull JSONArray paramJSONArray) {
    put(paramString, paramJSONArray, -1);
  }
  
  public void put(@NonNull String paramString, @NonNull JSONArray paramJSONArray, int paramInt) {
    put(paramString, CacheHelper.jsonArray2Bytes(paramJSONArray), paramInt);
  }
  
  public void put(@NonNull String paramString, @NonNull JSONObject paramJSONObject) {
    put(paramString, paramJSONObject, -1);
  }
  
  public void put(@NonNull String paramString, @NonNull JSONObject paramJSONObject, int paramInt) {
    put(paramString, CacheHelper.jsonObject2Bytes(paramJSONObject), paramInt);
  }
  
  public void put(@NonNull String paramString, @NonNull byte[] paramArrayOfbyte) {
    put(paramString, paramArrayOfbyte, -1);
  }
  
  public void put(@NonNull String paramString, @NonNull byte[] paramArrayOfbyte, int paramInt) {
    if (paramArrayOfbyte.length > 0) {
      byte[] arrayOfByte = paramArrayOfbyte;
      if (paramInt >= 0)
        arrayOfByte = CacheHelper.newByteArrayWithTime(paramInt, paramArrayOfbyte); 
      File file = this.mCacheManager.getFileBeforePut(paramString);
      CacheHelper.writeFileFromBytes(file, arrayOfByte);
      this.mCacheManager.updateModify(file);
      this.mCacheManager.put(file);
    } 
  }
  
  public boolean remove(@NonNull String paramString) {
    return this.mCacheManager.removeByKey(paramString);
  }
  
  private static class CacheHelper {
    static final int timeInfoLen = 14;
    
    private static byte[] bitmap2Bytes(Bitmap param1Bitmap) {
      if (param1Bitmap == null)
        return null; 
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      param1Bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
    }
    
    private static Drawable bitmap2Drawable(Bitmap param1Bitmap) {
      return (Drawable)((param1Bitmap == null) ? null : new BitmapDrawable(Utils.getApp().getResources(), param1Bitmap));
    }
    
    private static Bitmap bytes2Bitmap(byte[] param1ArrayOfbyte) {
      return (param1ArrayOfbyte == null || param1ArrayOfbyte.length == 0) ? null : BitmapFactory.decodeByteArray(param1ArrayOfbyte, 0, param1ArrayOfbyte.length);
    }
    
    private static Drawable bytes2Drawable(byte[] param1ArrayOfbyte) {
      return (param1ArrayOfbyte == null) ? null : bitmap2Drawable(bytes2Bitmap(param1ArrayOfbyte));
    }
    
    private static JSONArray bytes2JSONArray(byte[] param1ArrayOfbyte) {
      byte[] arrayOfByte1;
      byte[] arrayOfByte2 = null;
      if (param1ArrayOfbyte == null)
        return (JSONArray)arrayOfByte2; 
      try {
        JSONArray jSONArray2 = new JSONArray();
        String str = new String();
        this(param1ArrayOfbyte);
        this(str);
        JSONArray jSONArray1 = jSONArray2;
      } catch (Exception exception) {
        exception.printStackTrace();
        arrayOfByte1 = arrayOfByte2;
      } 
      return (JSONArray)arrayOfByte1;
    }
    
    private static JSONObject bytes2JSONObject(byte[] param1ArrayOfbyte) {
      byte[] arrayOfByte1;
      byte[] arrayOfByte2 = null;
      if (param1ArrayOfbyte == null)
        return (JSONObject)arrayOfByte2; 
      try {
        JSONObject jSONObject2 = new JSONObject();
        String str = new String();
        this(param1ArrayOfbyte);
        this(str);
        JSONObject jSONObject1 = jSONObject2;
      } catch (Exception exception) {
        exception.printStackTrace();
        arrayOfByte1 = arrayOfByte2;
      } 
      return (JSONObject)arrayOfByte1;
    }
    
    private static Object bytes2Object(byte[] param1ArrayOfbyte) {
      byte[] arrayOfByte3;
      byte[] arrayOfByte1 = null;
      if (param1ArrayOfbyte == null)
        return arrayOfByte1; 
      ObjectInputStream objectInputStream1 = null;
      byte[] arrayOfByte2 = null;
      ObjectInputStream objectInputStream2 = objectInputStream1;
      try {
        Exception exception;
        ObjectInputStream objectInputStream = new ObjectInputStream();
        objectInputStream2 = objectInputStream1;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream();
        objectInputStream2 = objectInputStream1;
        this(param1ArrayOfbyte);
        objectInputStream2 = objectInputStream1;
        this(byteArrayInputStream);
        try {
          Object object = objectInputStream.readObject();
        } catch (Exception exception1) {
          objectInputStream2 = objectInputStream;
          exception = exception1;
        } finally {
          Exception exception1;
          param1ArrayOfbyte = null;
        } 
      } catch (Exception exception) {
        param1ArrayOfbyte = arrayOfByte2;
        arrayOfByte3 = param1ArrayOfbyte;
        exception.printStackTrace();
        CloseUtils.closeIO(new Closeable[] { (Closeable)param1ArrayOfbyte });
        return arrayOfByte1;
      } finally {}
      CloseUtils.closeIO(new Closeable[] { (Closeable)arrayOfByte3 });
      throw param1ArrayOfbyte;
    }
    
    private static <T> T bytes2Parcelable(byte[] param1ArrayOfbyte, Parcelable.Creator<T> param1Creator) {
      if (param1ArrayOfbyte == null)
        return null; 
      Parcel parcel = Parcel.obtain();
      parcel.unmarshall(param1ArrayOfbyte, 0, param1ArrayOfbyte.length);
      parcel.setDataPosition(0);
      Object object = param1Creator.createFromParcel(parcel);
      parcel.recycle();
      return (T)object;
    }
    
    private static String bytes2String(byte[] param1ArrayOfbyte) {
      return (param1ArrayOfbyte == null) ? null : new String(param1ArrayOfbyte);
    }
    
    private static byte[] copyOfRange(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      int i = param1Int2 - param1Int1;
      if (i < 0)
        throw new IllegalArgumentException(param1Int1 + " > " + param1Int2); 
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(param1ArrayOfbyte, param1Int1, arrayOfByte, 0, Math.min(param1ArrayOfbyte.length - param1Int1, i));
      return arrayOfByte;
    }
    
    private static String createDueTime(int param1Int) {
      return String.format(Locale.getDefault(), "_$%010d$_", new Object[] { Long.valueOf(System.currentTimeMillis() / 1000L + param1Int) });
    }
    
    private static Bitmap drawable2Bitmap(Drawable param1Drawable) {
      Bitmap bitmap;
      if (param1Drawable instanceof BitmapDrawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable)param1Drawable;
        if (bitmapDrawable.getBitmap() != null)
          return bitmapDrawable.getBitmap(); 
      } 
      if (param1Drawable.getIntrinsicWidth() <= 0 || param1Drawable.getIntrinsicHeight() <= 0) {
        Bitmap.Config config;
        if (param1Drawable.getOpacity() != -1) {
          config = Bitmap.Config.ARGB_8888;
        } else {
          config = Bitmap.Config.RGB_565;
        } 
        bitmap = Bitmap.createBitmap(1, 1, config);
      } else {
        Bitmap.Config config;
        int i = param1Drawable.getIntrinsicWidth();
        int j = param1Drawable.getIntrinsicHeight();
        if (param1Drawable.getOpacity() != -1) {
          config = Bitmap.Config.ARGB_8888;
        } else {
          config = Bitmap.Config.RGB_565;
        } 
        bitmap = Bitmap.createBitmap(i, j, config);
      } 
      Canvas canvas = new Canvas(bitmap);
      param1Drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      param1Drawable.draw(canvas);
      return bitmap;
    }
    
    private static byte[] drawable2Bytes(Drawable param1Drawable) {
      return (param1Drawable == null) ? null : bitmap2Bytes(drawable2Bitmap(param1Drawable));
    }
    
    private static byte[] getDataWithoutDueTime(byte[] param1ArrayOfbyte) {
      byte[] arrayOfByte = param1ArrayOfbyte;
      if (hasTimeInfo(param1ArrayOfbyte))
        arrayOfByte = copyOfRange(param1ArrayOfbyte, 14, param1ArrayOfbyte.length); 
      return arrayOfByte;
    }
    
    private static long getDueTime(byte[] param1ArrayOfbyte) {
      long l1 = -1L;
      long l2 = l1;
      if (hasTimeInfo(param1ArrayOfbyte)) {
        String str = new String(copyOfRange(param1ArrayOfbyte, 2, 12));
        try {
          l2 = Long.parseLong(str);
          l2 *= 1000L;
        } catch (NumberFormatException numberFormatException) {
          l2 = l1;
        } 
      } 
      return l2;
    }
    
    private static boolean hasTimeInfo(byte[] param1ArrayOfbyte) {
      boolean bool = true;
      if (param1ArrayOfbyte == null || param1ArrayOfbyte.length < 14 || param1ArrayOfbyte[0] != 95 || param1ArrayOfbyte[1] != 36 || param1ArrayOfbyte[12] != 36 || param1ArrayOfbyte[13] != 95)
        bool = false; 
      return bool;
    }
    
    private static boolean isDue(byte[] param1ArrayOfbyte) {
      long l = getDueTime(param1ArrayOfbyte);
      return (l != -1L && System.currentTimeMillis() > l);
    }
    
    private static byte[] jsonArray2Bytes(JSONArray param1JSONArray) {
      return (param1JSONArray == null) ? null : param1JSONArray.toString().getBytes();
    }
    
    private static byte[] jsonObject2Bytes(JSONObject param1JSONObject) {
      return (param1JSONObject == null) ? null : param1JSONObject.toString().getBytes();
    }
    
    private static byte[] newByteArrayWithTime(int param1Int, byte[] param1ArrayOfbyte) {
      byte[] arrayOfByte1 = createDueTime(param1Int).getBytes();
      byte[] arrayOfByte2 = new byte[arrayOfByte1.length + param1ArrayOfbyte.length];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
      System.arraycopy(param1ArrayOfbyte, 0, arrayOfByte2, arrayOfByte1.length, param1ArrayOfbyte.length);
      return arrayOfByte2;
    }
    
    private static byte[] parcelable2Bytes(Parcelable param1Parcelable) {
      if (param1Parcelable == null)
        return null; 
      Parcel parcel = Parcel.obtain();
      param1Parcelable.writeToParcel(parcel, 0);
      byte[] arrayOfByte = parcel.marshall();
      parcel.recycle();
      return arrayOfByte;
    }
    
    private static byte[] readFile2Bytes(File param1File) {
      FileChannel fileChannel1 = null;
      FileChannel fileChannel2 = null;
      FileChannel fileChannel3 = fileChannel2;
      FileChannel fileChannel4 = fileChannel1;
      try {
        RandomAccessFile randomAccessFile = new RandomAccessFile();
        fileChannel3 = fileChannel2;
        fileChannel4 = fileChannel1;
        this(param1File, "r");
        fileChannel3 = fileChannel2;
        fileChannel4 = fileChannel1;
        FileChannel fileChannel = randomAccessFile.getChannel();
        fileChannel3 = fileChannel;
        fileChannel4 = fileChannel;
        int i = (int)fileChannel.size();
        fileChannel3 = fileChannel;
        fileChannel4 = fileChannel;
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, i).load();
        fileChannel3 = fileChannel;
        fileChannel4 = fileChannel;
        byte[] arrayOfByte = new byte[i];
        fileChannel3 = fileChannel;
        fileChannel4 = fileChannel;
        mappedByteBuffer.get(arrayOfByte, 0, i);
        return arrayOfByte;
      } catch (IOException iOException) {
        fileChannel4 = fileChannel3;
        iOException.printStackTrace();
        iOException = null;
        return (byte[])iOException;
      } finally {
        CloseUtils.closeIO(new Closeable[] { fileChannel4 });
      } 
    }
    
    private static byte[] serializable2Bytes(Serializable param1Serializable) {
      Serializable serializable3;
      Serializable serializable1 = null;
      if (param1Serializable == null)
        return (byte[])serializable1; 
      ObjectOutputStream objectOutputStream1 = null;
      Serializable serializable2 = null;
      ObjectOutputStream objectOutputStream2 = objectOutputStream1;
      try {
        Exception exception;
        ObjectOutputStream objectOutputStream = new ObjectOutputStream();
        objectOutputStream2 = objectOutputStream1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        objectOutputStream2 = objectOutputStream1;
        this();
        objectOutputStream2 = objectOutputStream1;
        this(byteArrayOutputStream);
        try {
          objectOutputStream.writeObject(param1Serializable);
          byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
        } catch (Exception exception1) {
          objectOutputStream2 = objectOutputStream;
          exception = exception1;
        } finally {
          param1Serializable = null;
        } 
      } catch (Exception exception) {
        param1Serializable = serializable2;
        serializable3 = param1Serializable;
        exception.printStackTrace();
        CloseUtils.closeIO(new Closeable[] { (Closeable)param1Serializable });
        return (byte[])serializable1;
      } finally {}
      CloseUtils.closeIO(new Closeable[] { (Closeable)serializable3 });
      throw param1Serializable;
    }
    
    private static byte[] string2Bytes(String param1String) {
      return (param1String == null) ? null : param1String.getBytes();
    }
    
    private static void writeFileFromBytes(File param1File, byte[] param1ArrayOfbyte) {
      FileChannel fileChannel1 = null;
      FileChannel fileChannel2 = null;
      FileChannel fileChannel3 = fileChannel2;
      FileChannel fileChannel4 = fileChannel1;
      try {
        FileOutputStream fileOutputStream = new FileOutputStream();
        fileChannel3 = fileChannel2;
        fileChannel4 = fileChannel1;
        this(param1File, false);
        fileChannel3 = fileChannel2;
        fileChannel4 = fileChannel1;
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel3 = fileChannel;
        fileChannel4 = fileChannel;
        fileChannel.write(ByteBuffer.wrap(param1ArrayOfbyte));
        fileChannel3 = fileChannel;
        fileChannel4 = fileChannel;
        fileChannel.force(true);
        return;
      } catch (IOException iOException) {
        fileChannel4 = fileChannel3;
        iOException.printStackTrace();
        return;
      } finally {
        CloseUtils.closeIO(new Closeable[] { fileChannel4 });
      } 
    }
  }
  
  private class CacheManager {
    private final AtomicInteger cacheCount;
    
    private final File cacheDir;
    
    private final AtomicLong cacheSize;
    
    private final int countLimit;
    
    private final Map<File, Long> lastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
    
    private final Thread mThread;
    
    private final long sizeLimit;
    
    private CacheManager(final File cacheDir, long param1Long, int param1Int) {
      this.cacheDir = cacheDir;
      this.sizeLimit = param1Long;
      this.countLimit = param1Int;
      this.cacheSize = new AtomicLong();
      this.cacheCount = new AtomicInteger();
      this.mThread = new Thread(new Runnable() {
            public void run() {
              int i = 0;
              byte b = 0;
              File[] arrayOfFile = cacheDir.listFiles();
              if (arrayOfFile != null) {
                int j = arrayOfFile.length;
                for (byte b1 = 0; b1 < j; b1++) {
                  File file = arrayOfFile[b1];
                  i = (int)(i + file.length());
                  b++;
                  CacheUtils.CacheManager.this.lastUsageDates.put(file, Long.valueOf(file.lastModified()));
                } 
                CacheUtils.CacheManager.this.cacheSize.getAndAdd(i);
                CacheUtils.CacheManager.this.cacheCount.getAndAdd(b);
              } 
            }
          });
      this.mThread.start();
    }
    
    private boolean clear() {
      File[] arrayOfFile = this.cacheDir.listFiles();
      if (arrayOfFile == null || arrayOfFile.length <= 0)
        return true; 
      boolean bool2 = true;
      int i = arrayOfFile.length;
      for (byte b = 0; b < i; b++) {
        File file = arrayOfFile[b];
        if (!file.delete()) {
          bool2 = false;
        } else {
          this.cacheSize.addAndGet(-file.length());
          this.cacheCount.addAndGet(-1);
          this.lastUsageDates.remove(file);
        } 
      } 
      boolean bool1 = bool2;
      if (bool2) {
        this.lastUsageDates.clear();
        this.cacheSize.set(0L);
        this.cacheCount.set(0);
        bool1 = bool2;
      } 
      return bool1;
    }
    
    private int getCacheCount() {
      try {
        this.mThread.join();
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      } 
      return this.cacheCount.get();
    }
    
    private long getCacheSize() {
      try {
        this.mThread.join();
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      } 
      return this.cacheSize.get();
    }
    
    private File getFileBeforePut(String param1String) {
      File file = new File(this.cacheDir, String.valueOf(param1String.hashCode()));
      if (file.exists()) {
        this.cacheCount.addAndGet(-1);
        this.cacheSize.addAndGet(-file.length());
      } 
      return file;
    }
    
    private File getFileIfExists(String param1String) {
      File file2 = new File(this.cacheDir, String.valueOf(param1String.hashCode()));
      File file1 = file2;
      if (!file2.exists())
        file1 = null; 
      return file1;
    }
    
    private void put(File param1File) {
      this.cacheCount.addAndGet(1);
      this.cacheSize.addAndGet(param1File.length());
      while (this.cacheCount.get() > this.countLimit || this.cacheSize.get() > this.sizeLimit) {
        this.cacheSize.addAndGet(-removeOldest());
        this.cacheCount.addAndGet(-1);
      } 
    }
    
    private boolean removeByKey(String param1String) {
      boolean bool = true;
      File file = getFileIfExists(param1String);
      if (file != null) {
        if (!file.delete())
          return false; 
        this.cacheSize.addAndGet(-file.length());
        this.cacheCount.addAndGet(-1);
        this.lastUsageDates.remove(file);
      } 
      return bool;
    }
    
    private long removeOldest() {
      if (this.lastUsageDates.isEmpty())
        return 0L; 
      Long long_ = Long.valueOf(Long.MAX_VALUE);
      null = null;
      Set<Map.Entry<File, Long>> set = this.lastUsageDates.entrySet();
      synchronized (this.lastUsageDates) {
        for (Map.Entry<File, Long> entry : set) {
          Long long_1 = (Long)entry.getValue();
          if (long_1.longValue() < long_.longValue()) {
            long_ = long_1;
            null = (File)entry.getKey();
          } 
        } 
        if (null == null)
          return 0L; 
      } 
      null = SYNTHETIC_LOCAL_VARIABLE_4.length();
      if (SYNTHETIC_LOCAL_VARIABLE_4.delete()) {
        this.lastUsageDates.remove(SYNTHETIC_LOCAL_VARIABLE_4);
        return null;
      } 
      return 0L;
    }
    
    private void updateModify(File param1File) {
      Long long_ = Long.valueOf(System.currentTimeMillis());
      param1File.setLastModified(long_.longValue());
      this.lastUsageDates.put(param1File, long_);
    }
  }
  
  class null implements Runnable {
    public void run() {
      int i = 0;
      byte b = 0;
      File[] arrayOfFile = cacheDir.listFiles();
      if (arrayOfFile != null) {
        int j = arrayOfFile.length;
        for (byte b1 = 0; b1 < j; b1++) {
          File file = arrayOfFile[b1];
          i = (int)(i + file.length());
          b++;
          this.this$1.lastUsageDates.put(file, Long.valueOf(file.lastModified()));
        } 
        this.this$1.cacheSize.getAndAdd(i);
        this.this$1.cacheCount.getAndAdd(b);
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/CacheUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */