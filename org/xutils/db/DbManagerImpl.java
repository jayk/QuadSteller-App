package org.xutils.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.xutils.DbManager;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.LogUtil;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.SqlInfoBuilder;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.ColumnEntity;
import org.xutils.db.table.DbBase;
import org.xutils.db.table.DbModel;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

public final class DbManagerImpl extends DbBase {
  private static final HashMap<DbManager.DaoConfig, DbManagerImpl> DAO_MAP = new HashMap<DbManager.DaoConfig, DbManagerImpl>();
  
  private boolean allowTransaction;
  
  private DbManager.DaoConfig daoConfig;
  
  private SQLiteDatabase database;
  
  private DbManagerImpl(DbManager.DaoConfig paramDaoConfig) {
    if (paramDaoConfig == null)
      throw new IllegalArgumentException("daoConfig may not be null"); 
    this.daoConfig = paramDaoConfig;
    this.allowTransaction = paramDaoConfig.isAllowTransaction();
    this.database = openOrCreateDatabase(paramDaoConfig);
    DbManager.DbOpenListener dbOpenListener = paramDaoConfig.getDbOpenListener();
    if (dbOpenListener != null)
      dbOpenListener.onDbOpened((DbManager)this); 
  }
  
  private void beginTransaction() {
    if (this.allowTransaction) {
      if (Build.VERSION.SDK_INT >= 16 && this.database.isWriteAheadLoggingEnabled()) {
        this.database.beginTransactionNonExclusive();
        return;
      } 
    } else {
      return;
    } 
    this.database.beginTransaction();
  }
  
  private void endTransaction() {
    if (this.allowTransaction)
      this.database.endTransaction(); 
  }
  
  public static DbManager getInstance(DbManager.DaoConfig paramDaoConfig) {
    // Byte code:
    //   0: ldc org/xutils/db/DbManagerImpl
    //   2: monitorenter
    //   3: aload_0
    //   4: astore_1
    //   5: aload_0
    //   6: ifnonnull -> 17
    //   9: new org/xutils/DbManager$DaoConfig
    //   12: astore_1
    //   13: aload_1
    //   14: invokespecial <init> : ()V
    //   17: getstatic org/xutils/db/DbManagerImpl.DAO_MAP : Ljava/util/HashMap;
    //   20: aload_1
    //   21: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   24: checkcast org/xutils/db/DbManagerImpl
    //   27: astore_0
    //   28: aload_0
    //   29: ifnonnull -> 106
    //   32: new org/xutils/db/DbManagerImpl
    //   35: astore_0
    //   36: aload_0
    //   37: aload_1
    //   38: invokespecial <init> : (Lorg/xutils/DbManager$DaoConfig;)V
    //   41: getstatic org/xutils/db/DbManagerImpl.DAO_MAP : Ljava/util/HashMap;
    //   44: aload_1
    //   45: aload_0
    //   46: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   49: pop
    //   50: aload_0
    //   51: getfield database : Landroid/database/sqlite/SQLiteDatabase;
    //   54: astore_2
    //   55: aload_2
    //   56: invokevirtual getVersion : ()I
    //   59: istore_3
    //   60: aload_1
    //   61: invokevirtual getDbVersion : ()I
    //   64: istore #4
    //   66: iload_3
    //   67: iload #4
    //   69: if_icmpeq -> 101
    //   72: iload_3
    //   73: ifeq -> 95
    //   76: aload_1
    //   77: invokevirtual getDbUpgradeListener : ()Lorg/xutils/DbManager$DbUpgradeListener;
    //   80: astore_1
    //   81: aload_1
    //   82: ifnull -> 120
    //   85: aload_1
    //   86: aload_0
    //   87: iload_3
    //   88: iload #4
    //   90: invokeinterface onUpgrade : (Lorg/xutils/DbManager;II)V
    //   95: aload_2
    //   96: iload #4
    //   98: invokevirtual setVersion : (I)V
    //   101: ldc org/xutils/db/DbManagerImpl
    //   103: monitorexit
    //   104: aload_0
    //   105: areturn
    //   106: aload_0
    //   107: aload_1
    //   108: putfield daoConfig : Lorg/xutils/DbManager$DaoConfig;
    //   111: goto -> 50
    //   114: astore_0
    //   115: ldc org/xutils/db/DbManagerImpl
    //   117: monitorexit
    //   118: aload_0
    //   119: athrow
    //   120: aload_0
    //   121: invokevirtual dropDb : ()V
    //   124: goto -> 95
    //   127: astore_1
    //   128: aload_1
    //   129: invokevirtual getMessage : ()Ljava/lang/String;
    //   132: aload_1
    //   133: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   136: goto -> 95
    // Exception table:
    //   from	to	target	type
    //   9	17	114	finally
    //   17	28	114	finally
    //   32	50	114	finally
    //   50	66	114	finally
    //   76	81	114	finally
    //   85	95	114	finally
    //   95	101	114	finally
    //   106	111	114	finally
    //   120	124	127	org/xutils/ex/DbException
    //   120	124	114	finally
    //   128	136	114	finally
  }
  
  private long getLastAutoIncrementId(String paramString) throws DbException {
    long l1 = -1L;
    Cursor cursor = execQuery("SELECT seq FROM sqlite_sequence WHERE name='" + paramString + "' LIMIT 1");
    long l2 = l1;
    if (cursor != null)
      try {
        if (cursor.moveToNext())
          l1 = cursor.getLong(0); 
        return l1;
      } catch (Throwable throwable) {
        DbException dbException = new DbException();
        this(throwable);
        throw dbException;
      } finally {
        IOUtil.closeQuietly(cursor);
      }  
    return l2;
  }
  
  private SQLiteDatabase openOrCreateDatabase(DbManager.DaoConfig paramDaoConfig) {
    File file = paramDaoConfig.getDbDir();
    return (file != null && (file.exists() || file.mkdirs())) ? SQLiteDatabase.openOrCreateDatabase(new File(file, paramDaoConfig.getDbName()), null) : x.app().openOrCreateDatabase(paramDaoConfig.getDbName(), 0, null);
  }
  
  private boolean saveBindingIdWithoutTransaction(TableEntity<?> paramTableEntity, Object paramObject) throws DbException {
    boolean bool = true;
    ColumnEntity columnEntity = paramTableEntity.getId();
    if (columnEntity.isAutoId()) {
      execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(paramTableEntity, paramObject));
      long l = getLastAutoIncrementId(paramTableEntity.getName());
      if (l == -1L)
        return false; 
      columnEntity.setAutoIdValue(paramObject, l);
      return bool;
    } 
    execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(paramTableEntity, paramObject));
    return bool;
  }
  
  private void saveOrUpdateWithoutTransaction(TableEntity<?> paramTableEntity, Object paramObject) throws DbException {
    ColumnEntity columnEntity = paramTableEntity.getId();
    if (columnEntity.isAutoId()) {
      if (columnEntity.getColumnValue(paramObject) != null) {
        execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(paramTableEntity, paramObject, new String[0]));
        return;
      } 
      saveBindingIdWithoutTransaction(paramTableEntity, paramObject);
      return;
    } 
    execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(paramTableEntity, paramObject));
  }
  
  private void setTransactionSuccessful() {
    if (this.allowTransaction)
      this.database.setTransactionSuccessful(); 
  }
  
  public void close() throws IOException {
    if (DAO_MAP.containsKey(this.daoConfig)) {
      DAO_MAP.remove(this.daoConfig);
      this.database.close();
    } 
  }
  
  public int delete(Class<?> paramClass, WhereBuilder paramWhereBuilder) throws DbException {
    null = getTable(paramClass);
    if (!null.tableIsExist())
      return 0; 
    try {
      beginTransaction();
      int i = executeUpdateDelete(SqlInfoBuilder.buildDeleteSqlInfo(null, paramWhereBuilder));
      setTransactionSuccessful();
      return i;
    } finally {
      endTransaction();
    } 
  }
  
  public void delete(Class<?> paramClass) throws DbException {
    delete(paramClass, (WhereBuilder)null);
  }
  
  public void delete(Object paramObject) throws DbException {
    try {
      beginTransaction();
      if (paramObject instanceof List) {
        List<E> list = (List)paramObject;
        boolean bool = list.isEmpty();
        if (bool)
          return; 
        paramObject = getTable(list.get(0).getClass());
        bool = paramObject.tableIsExist();
        if (!bool)
          return; 
        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext())
          execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo((TableEntity)paramObject, iterator.next())); 
      } else {
        TableEntity tableEntity = getTable(paramObject.getClass());
        boolean bool = tableEntity.tableIsExist();
        if (!bool)
          return; 
        execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(tableEntity, paramObject));
      } 
    } finally {
      endTransaction();
    } 
    endTransaction();
  }
  
  public void deleteById(Class<?> paramClass, Object paramObject) throws DbException {
    TableEntity tableEntity = getTable(paramClass);
    if (tableEntity.tableIsExist())
      try {
        beginTransaction();
        execNonQuery(SqlInfoBuilder.buildDeleteSqlInfoById(tableEntity, paramObject));
        setTransactionSuccessful();
        return;
      } finally {
        endTransaction();
      }  
  }
  
  public void execNonQuery(String paramString) throws DbException {
    try {
      this.database.execSQL(paramString);
      return;
    } catch (Throwable throwable) {
      throw new DbException(throwable);
    } 
  }
  
  public void execNonQuery(SqlInfo paramSqlInfo) throws DbException {
    SQLiteStatement sQLiteStatement1 = null;
    SQLiteStatement sQLiteStatement2 = null;
    try {
      SQLiteStatement sQLiteStatement = paramSqlInfo.buildStatement(this.database);
      sQLiteStatement2 = sQLiteStatement;
      sQLiteStatement1 = sQLiteStatement;
      sQLiteStatement.execute();
      return;
    } catch (Throwable throwable) {
      sQLiteStatement1 = sQLiteStatement2;
      DbException dbException = new DbException();
      sQLiteStatement1 = sQLiteStatement2;
      this(throwable);
      sQLiteStatement1 = sQLiteStatement2;
      throw dbException;
    } finally {
      if (sQLiteStatement1 != null)
        try {
          sQLiteStatement1.releaseReference();
        } catch (Throwable throwable) {
          LogUtil.e(throwable.getMessage(), throwable);
        }  
    } 
  }
  
  public Cursor execQuery(String paramString) throws DbException {
    try {
      return this.database.rawQuery(paramString, null);
    } catch (Throwable throwable) {
      throw new DbException(throwable);
    } 
  }
  
  public Cursor execQuery(SqlInfo paramSqlInfo) throws DbException {
    try {
      return this.database.rawQuery(paramSqlInfo.getSql(), paramSqlInfo.getBindArgsAsStrArray());
    } catch (Throwable throwable) {
      throw new DbException(throwable);
    } 
  }
  
  public int executeUpdateDelete(String paramString) throws DbException {
    SQLiteStatement sQLiteStatement1 = null;
    SQLiteStatement sQLiteStatement2 = null;
    try {
      SQLiteStatement sQLiteStatement = this.database.compileStatement(paramString);
      sQLiteStatement2 = sQLiteStatement;
      sQLiteStatement1 = sQLiteStatement;
      return sQLiteStatement.executeUpdateDelete();
    } catch (Throwable throwable) {
      sQLiteStatement1 = sQLiteStatement2;
      DbException dbException = new DbException();
      sQLiteStatement1 = sQLiteStatement2;
      this(throwable);
      sQLiteStatement1 = sQLiteStatement2;
      throw dbException;
    } finally {
      if (sQLiteStatement1 != null)
        try {
          sQLiteStatement1.releaseReference();
        } catch (Throwable throwable) {
          LogUtil.e(throwable.getMessage(), throwable);
        }  
    } 
  }
  
  public int executeUpdateDelete(SqlInfo paramSqlInfo) throws DbException {
    SQLiteStatement sQLiteStatement1 = null;
    SQLiteStatement sQLiteStatement2 = null;
    try {
      SQLiteStatement sQLiteStatement = paramSqlInfo.buildStatement(this.database);
      sQLiteStatement2 = sQLiteStatement;
      sQLiteStatement1 = sQLiteStatement;
      return sQLiteStatement.executeUpdateDelete();
    } catch (Throwable throwable) {
      sQLiteStatement1 = sQLiteStatement2;
      DbException dbException = new DbException();
      sQLiteStatement1 = sQLiteStatement2;
      this(throwable);
      sQLiteStatement1 = sQLiteStatement2;
      throw dbException;
    } finally {
      if (sQLiteStatement1 != null)
        try {
          sQLiteStatement1.releaseReference();
        } catch (Throwable throwable) {
          LogUtil.e(throwable.getMessage(), throwable);
        }  
    } 
  }
  
  public <T> List<T> findAll(Class<T> paramClass) throws DbException {
    return selector(paramClass).findAll();
  }
  
  public <T> T findById(Class<T> paramClass, Object paramObject) throws DbException {
    Class<T> clazz = null;
    TableEntity<?> tableEntity = getTable(paramClass);
    if (!tableEntity.tableIsExist())
      return (T)clazz; 
    paramObject = execQuery(Selector.from(tableEntity).where(tableEntity.getId().getName(), "=", paramObject).limit(1).toString());
    paramClass = clazz;
    if (paramObject != null)
      try {
        if (paramObject.moveToNext()) {
          paramClass = CursorUtils.getEntity(tableEntity, (Cursor)paramObject);
          return (T)paramClass;
        } 
        IOUtil.closeQuietly((Cursor)paramObject);
      } catch (Throwable throwable) {
        DbException dbException = new DbException();
        this(throwable);
        throw dbException;
      } finally {
        IOUtil.closeQuietly((Cursor)paramObject);
      }  
    return (T)paramClass;
  }
  
  public List<DbModel> findDbModelAll(SqlInfo paramSqlInfo) throws DbException {
    ArrayList<DbModel> arrayList = new ArrayList();
    Cursor cursor = execQuery(paramSqlInfo);
    if (cursor != null)
      try {
        while (cursor.moveToNext())
          arrayList.add(CursorUtils.getDbModel(cursor)); 
      } catch (Throwable throwable) {
        DbException dbException = new DbException();
        this(throwable);
        throw dbException;
      } finally {
        IOUtil.closeQuietly(cursor);
      }  
    return arrayList;
  }
  
  public DbModel findDbModelFirst(SqlInfo paramSqlInfo) throws DbException {
    Cursor cursor = execQuery(paramSqlInfo);
    if (cursor != null)
      try {
        if (cursor.moveToNext())
          return CursorUtils.getDbModel(cursor); 
        return null;
      } catch (Throwable throwable) {
        DbException dbException = new DbException();
        this(throwable);
        throw dbException;
      } finally {
        IOUtil.closeQuietly(cursor);
      }  
    return null;
  }
  
  public <T> T findFirst(Class<T> paramClass) throws DbException {
    return selector(paramClass).findFirst();
  }
  
  public DbManager.DaoConfig getDaoConfig() {
    return this.daoConfig;
  }
  
  public SQLiteDatabase getDatabase() {
    return this.database;
  }
  
  public void replace(Object paramObject) throws DbException {
    try {
      beginTransaction();
      if (paramObject instanceof List) {
        List<E> list = (List)paramObject;
        boolean bool = list.isEmpty();
        if (bool)
          return; 
        paramObject = getTable(list.get(0).getClass());
        createTableIfNotExist((TableEntity)paramObject);
        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext())
          execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo((TableEntity)paramObject, iterator.next())); 
      } else {
        TableEntity tableEntity = getTable(paramObject.getClass());
        createTableIfNotExist(tableEntity);
        execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(tableEntity, paramObject));
      } 
    } finally {
      endTransaction();
    } 
    endTransaction();
  }
  
  public void save(Object paramObject) throws DbException {
    try {
      beginTransaction();
      if (paramObject instanceof List) {
        List<E> list = (List)paramObject;
        boolean bool = list.isEmpty();
        if (bool)
          return; 
        paramObject = getTable(list.get(0).getClass());
        createTableIfNotExist((TableEntity)paramObject);
        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext())
          execNonQuery(SqlInfoBuilder.buildInsertSqlInfo((TableEntity)paramObject, iterator.next())); 
      } else {
        TableEntity tableEntity = getTable(paramObject.getClass());
        createTableIfNotExist(tableEntity);
        execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(tableEntity, paramObject));
      } 
    } finally {
      endTransaction();
    } 
    endTransaction();
  }
  
  public boolean saveBindingId(Object paramObject) throws DbException {
    boolean bool1 = false;
    boolean bool2 = false;
    try {
      beginTransaction();
      if (paramObject instanceof List) {
        List<E> list = (List)paramObject;
        boolean bool = list.isEmpty();
        if (bool)
          return bool1; 
        paramObject = getTable(list.get(0).getClass());
        createTableIfNotExist((TableEntity)paramObject);
        Iterator<E> iterator = list.iterator();
        while (true) {
          bool1 = bool2;
          if (iterator.hasNext()) {
            if (!saveBindingIdWithoutTransaction((TableEntity<?>)paramObject, iterator.next())) {
              paramObject = new DbException();
              super("saveBindingId error, transaction will not commit!");
              throw paramObject;
            } 
            continue;
          } 
          break;
        } 
      } else {
        TableEntity<?> tableEntity = getTable(paramObject.getClass());
        createTableIfNotExist(tableEntity);
        bool1 = saveBindingIdWithoutTransaction(tableEntity, paramObject);
      } 
    } finally {
      endTransaction();
    } 
    endTransaction();
    return bool1;
  }
  
  public void saveOrUpdate(Object paramObject) throws DbException {
    try {
      beginTransaction();
      if (paramObject instanceof List) {
        List<E> list = (List)paramObject;
        boolean bool = list.isEmpty();
        if (bool)
          return; 
        paramObject = getTable(list.get(0).getClass());
        createTableIfNotExist((TableEntity)paramObject);
        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext())
          saveOrUpdateWithoutTransaction((TableEntity<?>)paramObject, iterator.next()); 
      } else {
        TableEntity<?> tableEntity = getTable(paramObject.getClass());
        createTableIfNotExist(tableEntity);
        saveOrUpdateWithoutTransaction(tableEntity, paramObject);
      } 
    } finally {
      endTransaction();
    } 
    endTransaction();
  }
  
  public <T> Selector<T> selector(Class<T> paramClass) throws DbException {
    return Selector.from(getTable(paramClass));
  }
  
  public int update(Class<?> paramClass, WhereBuilder paramWhereBuilder, KeyValue... paramVarArgs) throws DbException {
    null = getTable(paramClass);
    if (!null.tableIsExist())
      return 0; 
    try {
      beginTransaction();
      int i = executeUpdateDelete(SqlInfoBuilder.buildUpdateSqlInfo(null, paramWhereBuilder, paramVarArgs));
      setTransactionSuccessful();
      return i;
    } finally {
      endTransaction();
    } 
  }
  
  public void update(Object paramObject, String... paramVarArgs) throws DbException {
    try {
      beginTransaction();
      if (paramObject instanceof List) {
        List<E> list = (List)paramObject;
        boolean bool = list.isEmpty();
        if (bool)
          return; 
        paramObject = getTable(list.get(0).getClass());
        bool = paramObject.tableIsExist();
        if (!bool)
          return; 
        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext())
          execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo((TableEntity)paramObject, iterator.next(), paramVarArgs)); 
      } else {
        TableEntity tableEntity = getTable(paramObject.getClass());
        boolean bool = tableEntity.tableIsExist();
        if (!bool)
          return; 
        execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(tableEntity, paramObject, paramVarArgs));
      } 
    } finally {
      endTransaction();
    } 
    endTransaction();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/DbManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */