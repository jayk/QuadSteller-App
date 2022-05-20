package app.gamer.quadstellar.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import app.gamer.quadstellar.App;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class XTProperties implements Map<String, String> {
  private static final String LOG_TAG = "XTProperties";
  
  private static final String MAC = "mac";
  
  private static String TABLE = "properties";
  
  private static final String VALUE = "value";
  
  private DbHelper dbHelper;
  
  private Map<String, String> properties;
  
  private XTProperties() {}
  
  private void deleteProperty(String paramString) {
    SQLiteDatabase sQLiteDatabase = this.dbHelper.getWritableDatabase();
    sQLiteDatabase.delete(TABLE, "mac=?", new String[] { paramString });
    sQLiteDatabase.close();
    LogUtil.d("XTProperties", "property deleted: " + paramString);
  }
  
  public static XTProperties getInstance() {
    return DodaPropertyHolder.instance;
  }
  
  private void insertProperty(String paramString1, String paramString2) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("mac", paramString1);
    contentValues.put("value", paramString2);
    SQLiteDatabase sQLiteDatabase = this.dbHelper.getWritableDatabase();
    sQLiteDatabase.insert(TABLE, null, contentValues);
    sQLiteDatabase.close();
    LogUtil.d("XTProperties", "property inserted: " + paramString1);
  }
  
  private void loadProperties() {
    SQLiteDatabase sQLiteDatabase = this.dbHelper.getReadableDatabase();
    Cursor cursor = sQLiteDatabase.query(TABLE, new String[] { "mac", "value" }, null, null, null, null, null);
    while (cursor.moveToNext()) {
      String str1 = cursor.getString(cursor.getColumnIndex("mac"));
      String str2 = cursor.getString(cursor.getColumnIndex("value"));
      this.properties.put(str1, str2);
    } 
    cursor.close();
    sQLiteDatabase.close();
  }
  
  private void updateProperty(String paramString1, String paramString2) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("value", paramString2);
    SQLiteDatabase sQLiteDatabase = this.dbHelper.getWritableDatabase();
    sQLiteDatabase.update(TABLE, contentValues, "mac=?", new String[] { paramString1 });
    sQLiteDatabase.close();
    LogUtil.d("XTProperties", "property updated: " + paramString1);
  }
  
  public void clear() {
    throw new UnsupportedOperationException();
  }
  
  public boolean containsKey(Object paramObject) {
    return this.properties.containsKey(paramObject);
  }
  
  public boolean containsValue(Object paramObject) {
    return this.properties.containsValue(paramObject);
  }
  
  public Set<Map.Entry<String, String>> entrySet() {
    return Collections.unmodifiableSet(this.properties.entrySet());
  }
  
  public String get(Object paramObject) {
    return this.properties.get(paramObject);
  }
  
  public boolean getBooleanProperty(String paramString) {
    return Boolean.valueOf(get(paramString)).booleanValue();
  }
  
  public boolean getBooleanProperty(String paramString, boolean paramBoolean) {
    paramString = get(paramString);
    if (paramString != null)
      paramBoolean = Boolean.valueOf(paramString).booleanValue(); 
    return paramBoolean;
  }
  
  public Collection<String> getChildrenNames(String paramString) {
    HashSet<String> hashSet = new HashSet();
    for (String str : this.properties.keySet()) {
      if (str.startsWith(paramString + ".") && !str.equals(paramString)) {
        int i = str.indexOf(".", paramString.length() + 1);
        if (i < 1) {
          if (!hashSet.contains(str))
            hashSet.add(str); 
          continue;
        } 
        hashSet.add(paramString + str.substring(paramString.length(), i));
      } 
    } 
    return hashSet;
  }
  
  public Map<String, String> getProperties() {
    return this.properties;
  }
  
  public String getProperty(String paramString1, String paramString2) {
    paramString1 = this.properties.get(paramString1);
    if (paramString1 == null)
      paramString1 = paramString2; 
    return paramString1;
  }
  
  public Collection<String> getPropertyNames() {
    return this.properties.keySet();
  }
  
  public void init() {
    if (this.properties == null) {
      this.properties = new ConcurrentHashMap<String, String>();
    } else {
      this.properties.clear();
    } 
    this.dbHelper = new DbHelper((Context)App.getInstance());
    loadProperties();
  }
  
  public boolean isEmpty() {
    return this.properties.isEmpty();
  }
  
  public Set<String> keySet() {
    return Collections.unmodifiableSet(this.properties.keySet());
  }
  
  public String put(String paramString1, String paramString2) {
    if (paramString2 == null)
      return remove(paramString1); 
    if (paramString1 == null)
      throw new NullPointerException("Key cannot be null. Key=" + paramString1 + ", " + "value" + "=" + paramString2); 
    String str = paramString1;
    if (paramString1.endsWith("."))
      str = paramString1.substring(0, paramString1.length() - 1); 
    paramString1 = str.trim();
    /* monitor enter ThisExpression{ObjectType{app/gamer/quadstellar/utils/XTProperties}} */
    try {
      if (this.properties.containsKey(paramString1)) {
        if (!((String)this.properties.get(paramString1)).equals(paramString2))
          updateProperty(paramString1, paramString2); 
      } else {
        insertProperty(paramString1, paramString2);
      } 
      paramString1 = this.properties.put(paramString1, paramString2);
      /* monitor exit ThisExpression{ObjectType{app/gamer/quadstellar/utils/XTProperties}} */
      (new HashMap<String, String>()).put("value", paramString2);
    } finally {}
    return paramString1;
  }
  
  public void putAll(Map<? extends String, ? extends String> paramMap) {
    for (Map.Entry<? extends String, ? extends String> entry : paramMap.entrySet())
      put((String)entry.getKey(), (String)entry.getValue()); 
  }
  
  public String remove(Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield properties : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast java/lang/String
    //   15: astore_2
    //   16: aload_0
    //   17: invokevirtual getPropertyNames : ()Ljava/util/Collection;
    //   20: invokeinterface iterator : ()Ljava/util/Iterator;
    //   25: astore_3
    //   26: aload_3
    //   27: invokeinterface hasNext : ()Z
    //   32: ifeq -> 78
    //   35: aload_3
    //   36: invokeinterface next : ()Ljava/lang/Object;
    //   41: checkcast java/lang/String
    //   44: astore #4
    //   46: aload #4
    //   48: aload_1
    //   49: checkcast java/lang/String
    //   52: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   55: ifeq -> 26
    //   58: aload_0
    //   59: getfield properties : Ljava/util/Map;
    //   62: aload #4
    //   64: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   69: pop
    //   70: goto -> 26
    //   73: astore_1
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_1
    //   77: athrow
    //   78: aload_0
    //   79: aload_1
    //   80: checkcast java/lang/String
    //   83: invokespecial deleteProperty : (Ljava/lang/String;)V
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_2
    //   89: areturn
    // Exception table:
    //   from	to	target	type
    //   2	26	73	finally
    //   26	70	73	finally
    //   74	76	73	finally
    //   78	88	73	finally
  }
  
  public int size() {
    return this.properties.size();
  }
  
  public Collection<String> values() {
    return Collections.unmodifiableCollection(this.properties.values());
  }
  
  public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteHelper";
    
    private static final int VERSION = 1;
    
    private static final String name = "device.db";
    
    public DbHelper(Context param1Context) {
      this(param1Context, "device.db", 1);
    }
    
    public DbHelper(Context param1Context, String param1String) {
      this(param1Context, param1String, 1);
    }
    
    public DbHelper(Context param1Context, String param1String, int param1Int) {
      this(param1Context, param1String, (SQLiteDatabase.CursorFactory)null, param1Int);
    }
    
    public DbHelper(Context param1Context, String param1String, SQLiteDatabase.CursorFactory param1CursorFactory, int param1Int) {
      super(param1Context, param1String, param1CursorFactory, param1Int);
    }
    
    public void onCreate(SQLiteDatabase param1SQLiteDatabase) {
      param1SQLiteDatabase.execSQL("CREATE TABLE properties(mac varchar(100) primary key, value TEXT)");
      LogUtil.d("SQLiteHelper", "create properties's db OK!");
    }
    
    public void onUpgrade(SQLiteDatabase param1SQLiteDatabase, int param1Int1, int param1Int2) {
      LogUtil.d("SQLiteHelper", "properties's db onUpgrade!");
    }
  }
  
  private static class DodaPropertyHolder {
    private static final XTProperties instance = new XTProperties();
    
    static {
      instance.init();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/XTProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */