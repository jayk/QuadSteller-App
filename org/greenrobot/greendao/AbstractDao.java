package org.greenrobot.greendao;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.internal.TableStatements;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxDao;
import rx.schedulers.Schedulers;

public abstract class AbstractDao<T, K> {
  protected final DaoConfig config;
  
  protected final Database db;
  
  protected final IdentityScope<K, T> identityScope;
  
  protected final IdentityScopeLong<T> identityScopeLong;
  
  protected final boolean isStandardSQLite;
  
  protected final int pkOrdinal;
  
  private volatile RxDao<T, K> rxDao;
  
  private volatile RxDao<T, K> rxDaoPlain;
  
  protected final AbstractDaoSession session;
  
  protected final TableStatements statements;
  
  public AbstractDao(DaoConfig paramDaoConfig) {
    this(paramDaoConfig, null);
  }
  
  public AbstractDao(DaoConfig paramDaoConfig, AbstractDaoSession paramAbstractDaoSession) {
    byte b;
    this.config = paramDaoConfig;
    this.session = paramAbstractDaoSession;
    this.db = paramDaoConfig.db;
    this.isStandardSQLite = this.db.getRawDatabase() instanceof android.database.sqlite.SQLiteDatabase;
    this.identityScope = paramDaoConfig.getIdentityScope();
    if (this.identityScope instanceof IdentityScopeLong) {
      this.identityScopeLong = (IdentityScopeLong)this.identityScope;
    } else {
      this.identityScopeLong = null;
    } 
    this.statements = paramDaoConfig.statements;
    if (paramDaoConfig.pkProperty != null) {
      b = paramDaoConfig.pkProperty.ordinal;
    } else {
      b = -1;
    } 
    this.pkOrdinal = b;
  }
  
  private void deleteByKeyInsideSynchronized(K paramK, DatabaseStatement paramDatabaseStatement) {
    if (paramK instanceof Long) {
      paramDatabaseStatement.bindLong(1, ((Long)paramK).longValue());
    } else {
      if (paramK == null)
        throw new DaoException("Cannot delete entity, key is null"); 
      paramDatabaseStatement.bindString(1, paramK.toString());
    } 
    paramDatabaseStatement.execute();
  }
  
  private void deleteInTxInternal(Iterable<T> paramIterable, Iterable<K> paramIterable1) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual assertSinglePk : ()V
    //   4: aload_0
    //   5: getfield statements : Lorg/greenrobot/greendao/internal/TableStatements;
    //   8: invokevirtual getDeleteStatement : ()Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   11: astore_3
    //   12: aconst_null
    //   13: astore #4
    //   15: aload_0
    //   16: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   19: invokeinterface beginTransaction : ()V
    //   24: aload_3
    //   25: monitorenter
    //   26: aload_0
    //   27: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   30: ifnull -> 52
    //   33: aload_0
    //   34: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   37: invokeinterface lock : ()V
    //   42: new java/util/ArrayList
    //   45: astore #4
    //   47: aload #4
    //   49: invokespecial <init> : ()V
    //   52: aload_1
    //   53: ifnull -> 145
    //   56: aload_1
    //   57: invokeinterface iterator : ()Ljava/util/Iterator;
    //   62: astore #5
    //   64: aload #5
    //   66: invokeinterface hasNext : ()Z
    //   71: ifeq -> 145
    //   74: aload_0
    //   75: aload #5
    //   77: invokeinterface next : ()Ljava/lang/Object;
    //   82: invokevirtual getKeyVerified : (Ljava/lang/Object;)Ljava/lang/Object;
    //   85: astore_1
    //   86: aload_0
    //   87: aload_1
    //   88: aload_3
    //   89: invokespecial deleteByKeyInsideSynchronized : (Ljava/lang/Object;Lorg/greenrobot/greendao/database/DatabaseStatement;)V
    //   92: aload #4
    //   94: ifnull -> 64
    //   97: aload #4
    //   99: aload_1
    //   100: invokeinterface add : (Ljava/lang/Object;)Z
    //   105: pop
    //   106: goto -> 64
    //   109: astore_1
    //   110: aload_0
    //   111: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   114: ifnull -> 126
    //   117: aload_0
    //   118: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   121: invokeinterface unlock : ()V
    //   126: aload_1
    //   127: athrow
    //   128: astore_1
    //   129: aload_3
    //   130: monitorexit
    //   131: aload_1
    //   132: athrow
    //   133: astore_1
    //   134: aload_0
    //   135: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   138: invokeinterface endTransaction : ()V
    //   143: aload_1
    //   144: athrow
    //   145: aload_2
    //   146: ifnull -> 195
    //   149: aload_2
    //   150: invokeinterface iterator : ()Ljava/util/Iterator;
    //   155: astore_2
    //   156: aload_2
    //   157: invokeinterface hasNext : ()Z
    //   162: ifeq -> 195
    //   165: aload_2
    //   166: invokeinterface next : ()Ljava/lang/Object;
    //   171: astore_1
    //   172: aload_0
    //   173: aload_1
    //   174: aload_3
    //   175: invokespecial deleteByKeyInsideSynchronized : (Ljava/lang/Object;Lorg/greenrobot/greendao/database/DatabaseStatement;)V
    //   178: aload #4
    //   180: ifnull -> 156
    //   183: aload #4
    //   185: aload_1
    //   186: invokeinterface add : (Ljava/lang/Object;)Z
    //   191: pop
    //   192: goto -> 156
    //   195: aload_0
    //   196: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   199: ifnull -> 211
    //   202: aload_0
    //   203: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   206: invokeinterface unlock : ()V
    //   211: aload_3
    //   212: monitorexit
    //   213: aload_0
    //   214: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   217: invokeinterface setTransactionSuccessful : ()V
    //   222: aload #4
    //   224: ifnull -> 245
    //   227: aload_0
    //   228: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   231: ifnull -> 245
    //   234: aload_0
    //   235: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   238: aload #4
    //   240: invokeinterface remove : (Ljava/lang/Iterable;)V
    //   245: aload_0
    //   246: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   249: invokeinterface endTransaction : ()V
    //   254: return
    // Exception table:
    //   from	to	target	type
    //   24	26	133	finally
    //   26	52	128	finally
    //   56	64	109	finally
    //   64	92	109	finally
    //   97	106	109	finally
    //   110	126	128	finally
    //   126	128	128	finally
    //   129	131	128	finally
    //   131	133	133	finally
    //   149	156	109	finally
    //   156	178	109	finally
    //   183	192	109	finally
    //   195	211	128	finally
    //   211	213	128	finally
    //   213	222	133	finally
    //   227	245	133	finally
  }
  
  private long executeInsert(T paramT, DatabaseStatement paramDatabaseStatement, boolean paramBoolean) {
    long l;
    if (this.db.isDbLockedByCurrentThread()) {
      l = insertInsideTx(paramT, paramDatabaseStatement);
    } else {
      this.db.beginTransaction();
      try {
        l = insertInsideTx(paramT, paramDatabaseStatement);
        this.db.setTransactionSuccessful();
        this.db.endTransaction();
      } finally {
        this.db.endTransaction();
      } 
      return l;
    } 
    if (paramBoolean)
      updateKeyAfterInsertAndAttach(paramT, l, true); 
  }
  
  private void executeInsertInTx(DatabaseStatement paramDatabaseStatement, Iterable<T> paramIterable, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   4: invokeinterface beginTransaction : ()V
    //   9: aload_1
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   15: ifnull -> 27
    //   18: aload_0
    //   19: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   22: invokeinterface lock : ()V
    //   27: aload_0
    //   28: getfield isStandardSQLite : Z
    //   31: ifeq -> 140
    //   34: aload_1
    //   35: invokeinterface getRawStatement : ()Ljava/lang/Object;
    //   40: checkcast android/database/sqlite/SQLiteStatement
    //   43: astore #4
    //   45: aload_2
    //   46: invokeinterface iterator : ()Ljava/util/Iterator;
    //   51: astore_2
    //   52: aload_2
    //   53: invokeinterface hasNext : ()Z
    //   58: ifeq -> 200
    //   61: aload_2
    //   62: invokeinterface next : ()Ljava/lang/Object;
    //   67: astore #5
    //   69: aload_0
    //   70: aload #4
    //   72: aload #5
    //   74: invokevirtual bindValues : (Landroid/database/sqlite/SQLiteStatement;Ljava/lang/Object;)V
    //   77: iload_3
    //   78: ifeq -> 132
    //   81: aload_0
    //   82: aload #5
    //   84: aload #4
    //   86: invokevirtual executeInsert : ()J
    //   89: iconst_0
    //   90: invokevirtual updateKeyAfterInsertAndAttach : (Ljava/lang/Object;JZ)V
    //   93: goto -> 52
    //   96: astore_2
    //   97: aload_0
    //   98: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   101: ifnull -> 113
    //   104: aload_0
    //   105: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   108: invokeinterface unlock : ()V
    //   113: aload_2
    //   114: athrow
    //   115: astore_2
    //   116: aload_1
    //   117: monitorexit
    //   118: aload_2
    //   119: athrow
    //   120: astore_1
    //   121: aload_0
    //   122: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   125: invokeinterface endTransaction : ()V
    //   130: aload_1
    //   131: athrow
    //   132: aload #4
    //   134: invokevirtual execute : ()V
    //   137: goto -> 52
    //   140: aload_2
    //   141: invokeinterface iterator : ()Ljava/util/Iterator;
    //   146: astore_2
    //   147: aload_2
    //   148: invokeinterface hasNext : ()Z
    //   153: ifeq -> 200
    //   156: aload_2
    //   157: invokeinterface next : ()Ljava/lang/Object;
    //   162: astore #4
    //   164: aload_0
    //   165: aload_1
    //   166: aload #4
    //   168: invokevirtual bindValues : (Lorg/greenrobot/greendao/database/DatabaseStatement;Ljava/lang/Object;)V
    //   171: iload_3
    //   172: ifeq -> 191
    //   175: aload_0
    //   176: aload #4
    //   178: aload_1
    //   179: invokeinterface executeInsert : ()J
    //   184: iconst_0
    //   185: invokevirtual updateKeyAfterInsertAndAttach : (Ljava/lang/Object;JZ)V
    //   188: goto -> 147
    //   191: aload_1
    //   192: invokeinterface execute : ()V
    //   197: goto -> 147
    //   200: aload_0
    //   201: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   204: ifnull -> 216
    //   207: aload_0
    //   208: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   211: invokeinterface unlock : ()V
    //   216: aload_1
    //   217: monitorexit
    //   218: aload_0
    //   219: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   222: invokeinterface setTransactionSuccessful : ()V
    //   227: aload_0
    //   228: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   231: invokeinterface endTransaction : ()V
    //   236: return
    // Exception table:
    //   from	to	target	type
    //   9	11	120	finally
    //   11	27	115	finally
    //   27	52	96	finally
    //   52	77	96	finally
    //   81	93	96	finally
    //   97	113	115	finally
    //   113	115	115	finally
    //   116	118	115	finally
    //   118	120	120	finally
    //   132	137	96	finally
    //   140	147	96	finally
    //   147	171	96	finally
    //   175	188	96	finally
    //   191	197	96	finally
    //   200	216	115	finally
    //   216	218	115	finally
    //   218	227	120	finally
  }
  
  private long insertInsideTx(T paramT, DatabaseStatement paramDatabaseStatement) {
    // Byte code:
    //   0: aload_2
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isStandardSQLite : Z
    //   6: ifeq -> 36
    //   9: aload_2
    //   10: invokeinterface getRawStatement : ()Ljava/lang/Object;
    //   15: checkcast android/database/sqlite/SQLiteStatement
    //   18: astore_3
    //   19: aload_0
    //   20: aload_3
    //   21: aload_1
    //   22: invokevirtual bindValues : (Landroid/database/sqlite/SQLiteStatement;Ljava/lang/Object;)V
    //   25: aload_3
    //   26: invokevirtual executeInsert : ()J
    //   29: lstore #4
    //   31: aload_2
    //   32: monitorexit
    //   33: lload #4
    //   35: lreturn
    //   36: aload_0
    //   37: aload_2
    //   38: aload_1
    //   39: invokevirtual bindValues : (Lorg/greenrobot/greendao/database/DatabaseStatement;Ljava/lang/Object;)V
    //   42: aload_2
    //   43: invokeinterface executeInsert : ()J
    //   48: lstore #4
    //   50: aload_2
    //   51: monitorexit
    //   52: goto -> 33
    //   55: astore_1
    //   56: aload_2
    //   57: monitorexit
    //   58: aload_1
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	55	finally
    //   36	52	55	finally
    //   56	58	55	finally
  }
  
  private void loadAllUnlockOnWindowBounds(Cursor paramCursor, CursorWindow paramCursorWindow, List<T> paramList) {
    int i = paramCursorWindow.getStartPosition() + paramCursorWindow.getNumRows();
    for (byte b = 0;; b++) {
      paramList.add(loadCurrent(paramCursor, 0, false));
      if (++b >= i) {
        paramCursorWindow = moveToNextUnlocked(paramCursor);
        if (paramCursorWindow == null)
          return; 
        i = paramCursorWindow.getStartPosition() + paramCursorWindow.getNumRows();
      } else if (!paramCursor.moveToNext()) {
        return;
      } 
    } 
  }
  
  private CursorWindow moveToNextUnlocked(Cursor paramCursor) {
    this.identityScope.unlock();
    try {
      if (paramCursor.moveToNext())
        return ((CrossProcessCursor)paramCursor).getWindow(); 
      paramCursor = null;
      return (CursorWindow)paramCursor;
    } finally {
      this.identityScope.lock();
    } 
  }
  
  protected void assertSinglePk() {
    if (this.config.pkColumns.length != 1)
      throw new DaoException(this + " (" + this.config.tablename + ") does not have a single-column primary key"); 
  }
  
  protected void attachEntity(T paramT) {}
  
  protected final void attachEntity(K paramK, T paramT, boolean paramBoolean) {
    attachEntity(paramT);
    if (this.identityScope != null && paramK != null) {
      if (paramBoolean) {
        this.identityScope.put(paramK, paramT);
        return;
      } 
    } else {
      return;
    } 
    this.identityScope.putNoLock(paramK, paramT);
  }
  
  protected abstract void bindValues(SQLiteStatement paramSQLiteStatement, T paramT);
  
  protected abstract void bindValues(DatabaseStatement paramDatabaseStatement, T paramT);
  
  public long count() {
    return this.statements.getCountStatement().simpleQueryForLong();
  }
  
  public void delete(T paramT) {
    assertSinglePk();
    deleteByKey(getKeyVerified(paramT));
  }
  
  public void deleteAll() {
    this.db.execSQL("DELETE FROM '" + this.config.tablename + "'");
    if (this.identityScope != null)
      this.identityScope.clear(); 
  }
  
  public void deleteByKey(K paramK) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual assertSinglePk : ()V
    //   4: aload_0
    //   5: getfield statements : Lorg/greenrobot/greendao/internal/TableStatements;
    //   8: invokevirtual getDeleteStatement : ()Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   11: astore_2
    //   12: aload_0
    //   13: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   16: invokeinterface isDbLockedByCurrentThread : ()Z
    //   21: ifeq -> 57
    //   24: aload_2
    //   25: monitorenter
    //   26: aload_0
    //   27: aload_1
    //   28: aload_2
    //   29: invokespecial deleteByKeyInsideSynchronized : (Ljava/lang/Object;Lorg/greenrobot/greendao/database/DatabaseStatement;)V
    //   32: aload_2
    //   33: monitorexit
    //   34: aload_0
    //   35: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   38: ifnull -> 51
    //   41: aload_0
    //   42: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   45: aload_1
    //   46: invokeinterface remove : (Ljava/lang/Object;)V
    //   51: return
    //   52: astore_1
    //   53: aload_2
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    //   57: aload_0
    //   58: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   61: invokeinterface beginTransaction : ()V
    //   66: aload_2
    //   67: monitorenter
    //   68: aload_0
    //   69: aload_1
    //   70: aload_2
    //   71: invokespecial deleteByKeyInsideSynchronized : (Ljava/lang/Object;Lorg/greenrobot/greendao/database/DatabaseStatement;)V
    //   74: aload_2
    //   75: monitorexit
    //   76: aload_0
    //   77: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   80: invokeinterface setTransactionSuccessful : ()V
    //   85: aload_0
    //   86: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   89: invokeinterface endTransaction : ()V
    //   94: goto -> 34
    //   97: astore_1
    //   98: aload_2
    //   99: monitorexit
    //   100: aload_1
    //   101: athrow
    //   102: astore_1
    //   103: aload_0
    //   104: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   107: invokeinterface endTransaction : ()V
    //   112: aload_1
    //   113: athrow
    // Exception table:
    //   from	to	target	type
    //   26	34	52	finally
    //   53	55	52	finally
    //   66	68	102	finally
    //   68	76	97	finally
    //   76	85	102	finally
    //   98	100	97	finally
    //   100	102	102	finally
  }
  
  public void deleteByKeyInTx(Iterable<K> paramIterable) {
    deleteInTxInternal(null, paramIterable);
  }
  
  public void deleteByKeyInTx(K... paramVarArgs) {
    deleteInTxInternal(null, Arrays.asList(paramVarArgs));
  }
  
  public void deleteInTx(Iterable<T> paramIterable) {
    deleteInTxInternal(paramIterable, null);
  }
  
  public void deleteInTx(T... paramVarArgs) {
    deleteInTxInternal(Arrays.asList(paramVarArgs), null);
  }
  
  public boolean detach(T paramT) {
    if (this.identityScope != null) {
      K k = getKeyVerified(paramT);
      return this.identityScope.detach(k, paramT);
    } 
    return false;
  }
  
  public void detachAll() {
    if (this.identityScope != null)
      this.identityScope.clear(); 
  }
  
  public String[] getAllColumns() {
    return this.config.allColumns;
  }
  
  public Database getDatabase() {
    return this.db;
  }
  
  protected abstract K getKey(T paramT);
  
  protected K getKeyVerified(T paramT) {
    K k = getKey(paramT);
    if (k == null) {
      if (paramT == null)
        throw new NullPointerException("Entity may not be null"); 
      throw new DaoException("Entity has no key");
    } 
    return k;
  }
  
  public String[] getNonPkColumns() {
    return this.config.nonPkColumns;
  }
  
  public String[] getPkColumns() {
    return this.config.pkColumns;
  }
  
  public Property getPkProperty() {
    return this.config.pkProperty;
  }
  
  public Property[] getProperties() {
    return this.config.properties;
  }
  
  public AbstractDaoSession getSession() {
    return this.session;
  }
  
  TableStatements getStatements() {
    return this.config.statements;
  }
  
  public String getTablename() {
    return this.config.tablename;
  }
  
  protected abstract boolean hasKey(T paramT);
  
  public long insert(T paramT) {
    return executeInsert(paramT, this.statements.getInsertStatement(), true);
  }
  
  public void insertInTx(Iterable<T> paramIterable) {
    insertInTx(paramIterable, isEntityUpdateable());
  }
  
  public void insertInTx(Iterable<T> paramIterable, boolean paramBoolean) {
    executeInsertInTx(this.statements.getInsertStatement(), paramIterable, paramBoolean);
  }
  
  public void insertInTx(T... paramVarArgs) {
    insertInTx(Arrays.asList(paramVarArgs), isEntityUpdateable());
  }
  
  public long insertOrReplace(T paramT) {
    return executeInsert(paramT, this.statements.getInsertOrReplaceStatement(), true);
  }
  
  public void insertOrReplaceInTx(Iterable<T> paramIterable) {
    insertOrReplaceInTx(paramIterable, isEntityUpdateable());
  }
  
  public void insertOrReplaceInTx(Iterable<T> paramIterable, boolean paramBoolean) {
    executeInsertInTx(this.statements.getInsertOrReplaceStatement(), paramIterable, paramBoolean);
  }
  
  public void insertOrReplaceInTx(T... paramVarArgs) {
    insertOrReplaceInTx(Arrays.asList(paramVarArgs), isEntityUpdateable());
  }
  
  public long insertWithoutSettingPk(T paramT) {
    return executeInsert(paramT, this.statements.getInsertOrReplaceStatement(), false);
  }
  
  protected abstract boolean isEntityUpdateable();
  
  public T load(K paramK) {
    assertSinglePk();
    if (paramK == null)
      return null; 
    if (this.identityScope != null) {
      Object object2 = this.identityScope.get(paramK);
      Object object1 = object2;
      if (object2 == null) {
        object1 = this.statements.getSelectByKey();
        str = paramK.toString();
        return loadUniqueAndCloseCursor(this.db.rawQuery((String)object1, new String[] { str }));
      } 
      return (T)object1;
    } 
    null = this.statements.getSelectByKey();
    String str = str.toString();
    return loadUniqueAndCloseCursor(this.db.rawQuery(null, new String[] { str }));
  }
  
  public List<T> loadAll() {
    return loadAllAndCloseCursor(this.db.rawQuery(this.statements.getSelectAll(), null));
  }
  
  protected List<T> loadAllAndCloseCursor(Cursor paramCursor) {
    try {
      return loadAllFromCursor(paramCursor);
    } finally {
      paramCursor.close();
    } 
  }
  
  protected List<T> loadAllFromCursor(Cursor paramCursor) {
    // Byte code:
    //   0: aload_1
    //   1: invokeinterface getCount : ()I
    //   6: istore_2
    //   7: iload_2
    //   8: ifne -> 21
    //   11: new java/util/ArrayList
    //   14: dup
    //   15: invokespecial <init> : ()V
    //   18: astore_1
    //   19: aload_1
    //   20: areturn
    //   21: new java/util/ArrayList
    //   24: dup
    //   25: iload_2
    //   26: invokespecial <init> : (I)V
    //   29: astore_3
    //   30: aconst_null
    //   31: astore #4
    //   33: iconst_0
    //   34: istore #5
    //   36: iload #5
    //   38: istore #6
    //   40: aload_1
    //   41: astore #7
    //   43: aload_1
    //   44: instanceof android/database/CrossProcessCursor
    //   47: ifeq -> 104
    //   50: aload_1
    //   51: checkcast android/database/CrossProcessCursor
    //   54: invokeinterface getWindow : ()Landroid/database/CursorWindow;
    //   59: astore #8
    //   61: iload #5
    //   63: istore #6
    //   65: aload #8
    //   67: astore #4
    //   69: aload_1
    //   70: astore #7
    //   72: aload #8
    //   74: ifnull -> 104
    //   77: aload #8
    //   79: invokevirtual getNumRows : ()I
    //   82: iload_2
    //   83: if_icmpne -> 191
    //   86: new org/greenrobot/greendao/internal/FastCursor
    //   89: dup
    //   90: aload #8
    //   92: invokespecial <init> : (Landroid/database/CursorWindow;)V
    //   95: astore #7
    //   97: iconst_1
    //   98: istore #6
    //   100: aload #8
    //   102: astore #4
    //   104: aload_3
    //   105: astore_1
    //   106: aload #7
    //   108: invokeinterface moveToFirst : ()Z
    //   113: ifeq -> 19
    //   116: aload_0
    //   117: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   120: ifnull -> 142
    //   123: aload_0
    //   124: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   127: invokeinterface lock : ()V
    //   132: aload_0
    //   133: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   136: iload_2
    //   137: invokeinterface reserveRoom : (I)V
    //   142: iload #6
    //   144: ifne -> 243
    //   147: aload #4
    //   149: ifnull -> 243
    //   152: aload_0
    //   153: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   156: ifnull -> 243
    //   159: aload_0
    //   160: aload #7
    //   162: aload #4
    //   164: aload_3
    //   165: invokespecial loadAllUnlockOnWindowBounds : (Landroid/database/Cursor;Landroid/database/CursorWindow;Ljava/util/List;)V
    //   168: aload_3
    //   169: astore_1
    //   170: aload_0
    //   171: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   174: ifnull -> 19
    //   177: aload_0
    //   178: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   181: invokeinterface unlock : ()V
    //   186: aload_3
    //   187: astore_1
    //   188: goto -> 19
    //   191: new java/lang/StringBuilder
    //   194: dup
    //   195: invokespecial <init> : ()V
    //   198: ldc_w 'Window vs. result size: '
    //   201: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   204: aload #8
    //   206: invokevirtual getNumRows : ()I
    //   209: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   212: ldc_w '/'
    //   215: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: iload_2
    //   219: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   222: invokevirtual toString : ()Ljava/lang/String;
    //   225: invokestatic d : (Ljava/lang/String;)I
    //   228: pop
    //   229: iload #5
    //   231: istore #6
    //   233: aload #8
    //   235: astore #4
    //   237: aload_1
    //   238: astore #7
    //   240: goto -> 104
    //   243: aload_3
    //   244: aload_0
    //   245: aload #7
    //   247: iconst_0
    //   248: iconst_0
    //   249: invokevirtual loadCurrent : (Landroid/database/Cursor;IZ)Ljava/lang/Object;
    //   252: invokeinterface add : (Ljava/lang/Object;)Z
    //   257: pop
    //   258: aload #7
    //   260: invokeinterface moveToNext : ()Z
    //   265: istore #9
    //   267: iload #9
    //   269: ifne -> 243
    //   272: goto -> 168
    //   275: astore_1
    //   276: aload_0
    //   277: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   280: ifnull -> 292
    //   283: aload_0
    //   284: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   287: invokeinterface unlock : ()V
    //   292: aload_1
    //   293: athrow
    // Exception table:
    //   from	to	target	type
    //   152	168	275	finally
    //   243	267	275	finally
  }
  
  public T loadByRowId(long paramLong) {
    String str = Long.toString(paramLong);
    return loadUniqueAndCloseCursor(this.db.rawQuery(this.statements.getSelectByRowId(), new String[] { str }));
  }
  
  protected final T loadCurrent(Cursor paramCursor, int paramInt, boolean paramBoolean) {
    Object object = null;
    if (this.identityScopeLong != null) {
      if (paramInt == 0 || !paramCursor.isNull(this.pkOrdinal + paramInt)) {
        Object object1;
        long l = paramCursor.getLong(this.pkOrdinal + paramInt);
        if (paramBoolean) {
          object1 = this.identityScopeLong.get2(l);
        } else {
          object1 = this.identityScopeLong.get2NoLock(l);
        } 
        object = object1;
        if (object1 == null) {
          object = readEntity(paramCursor, paramInt);
          attachEntity((T)object);
          if (paramBoolean) {
            this.identityScopeLong.put2(l, object);
            return (T)object;
          } 
          this.identityScopeLong.put2NoLock(l, object);
        } 
      } 
      return (T)object;
    } 
    if (this.identityScope != null) {
      K k = readKey(paramCursor, paramInt);
      if (paramInt == 0 || k != null) {
        Object object1;
        if (paramBoolean) {
          object1 = this.identityScope.get(k);
        } else {
          object1 = this.identityScope.getNoLock(k);
        } 
        object = object1;
        if (object1 == null) {
          object = readEntity(paramCursor, paramInt);
          attachEntity(k, (T)object, paramBoolean);
        } 
      } 
      return (T)object;
    } 
    if (paramInt == 0 || readKey(paramCursor, paramInt) != null) {
      object = readEntity(paramCursor, paramInt);
      attachEntity((T)object);
    } 
    return (T)object;
  }
  
  protected final <O> O loadCurrentOther(AbstractDao<O, ?> paramAbstractDao, Cursor paramCursor, int paramInt) {
    return paramAbstractDao.loadCurrent(paramCursor, paramInt, true);
  }
  
  protected T loadUnique(Cursor paramCursor) {
    if (!paramCursor.moveToFirst())
      return null; 
    if (!paramCursor.isLast())
      throw new DaoException("Expected unique result, but count was " + paramCursor.getCount()); 
    return loadCurrent(paramCursor, 0, true);
  }
  
  protected T loadUniqueAndCloseCursor(Cursor paramCursor) {
    try {
      return loadUnique(paramCursor);
    } finally {
      paramCursor.close();
    } 
  }
  
  public QueryBuilder<T> queryBuilder() {
    return QueryBuilder.internalCreate(this);
  }
  
  public List<T> queryRaw(String paramString, String... paramVarArgs) {
    return loadAllAndCloseCursor(this.db.rawQuery(this.statements.getSelectAll() + paramString, paramVarArgs));
  }
  
  public Query<T> queryRawCreate(String paramString, Object... paramVarArgs) {
    return queryRawCreateListArgs(paramString, Arrays.asList(paramVarArgs));
  }
  
  public Query<T> queryRawCreateListArgs(String paramString, Collection<Object> paramCollection) {
    return Query.internalCreate(this, this.statements.getSelectAll() + paramString, paramCollection.toArray());
  }
  
  protected abstract T readEntity(Cursor paramCursor, int paramInt);
  
  protected abstract void readEntity(Cursor paramCursor, T paramT, int paramInt);
  
  protected abstract K readKey(Cursor paramCursor, int paramInt);
  
  public void refresh(T paramT) {
    StringBuilder stringBuilder;
    DaoException daoException;
    assertSinglePk();
    K k = getKeyVerified(paramT);
    String str1 = this.statements.getSelectByKey();
    String str2 = k.toString();
    Cursor cursor = this.db.rawQuery(str1, new String[] { str2 });
    try {
      if (!cursor.moveToFirst()) {
        DaoException daoException1 = new DaoException();
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        this(stringBuilder1.append("Entity does not exist in the database anymore: ").append(paramT.getClass()).append(" with key ").append(k).toString());
        throw daoException1;
      } 
    } finally {
      cursor.close();
    } 
    readEntity(cursor, (T)stringBuilder, 0);
    attachEntity((K)daoException, (T)stringBuilder, true);
    cursor.close();
  }
  
  @Experimental
  public RxDao<T, K> rx() {
    if (this.rxDao == null)
      this.rxDao = new RxDao(this, Schedulers.io()); 
    return this.rxDao;
  }
  
  @Experimental
  public RxDao<T, K> rxPlain() {
    if (this.rxDaoPlain == null)
      this.rxDaoPlain = new RxDao(this); 
    return this.rxDaoPlain;
  }
  
  public void save(T paramT) {
    if (hasKey(paramT)) {
      update(paramT);
      return;
    } 
    insert(paramT);
  }
  
  public void saveInTx(Iterable<T> paramIterable) {
    byte b1 = 0;
    byte b2 = 0;
    Iterator<T> iterator = paramIterable.iterator();
    while (iterator.hasNext()) {
      if (hasKey(iterator.next())) {
        b1++;
        continue;
      } 
      b2++;
    } 
    if (b1 > 0 && b2 > 0) {
      ArrayList<Iterable<T>> arrayList1 = new ArrayList(b1);
      ArrayList<Iterable<T>> arrayList2 = new ArrayList(b2);
      for (Iterable<T> paramIterable : paramIterable) {
        if (hasKey((T)paramIterable)) {
          arrayList1.add(paramIterable);
          continue;
        } 
        arrayList2.add(paramIterable);
      } 
      this.db.beginTransaction();
      try {
        updateInTx((Iterable)arrayList1);
        insertInTx((Iterable)arrayList2);
        this.db.setTransactionSuccessful();
        return;
      } finally {
        this.db.endTransaction();
      } 
    } 
    if (b2 > 0) {
      insertInTx(paramIterable);
      return;
    } 
    if (b1 > 0)
      updateInTx(paramIterable); 
  }
  
  public void saveInTx(T... paramVarArgs) {
    saveInTx(Arrays.asList(paramVarArgs));
  }
  
  public void update(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual assertSinglePk : ()V
    //   4: aload_0
    //   5: getfield statements : Lorg/greenrobot/greendao/internal/TableStatements;
    //   8: invokevirtual getUpdateStatement : ()Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   11: astore_2
    //   12: aload_0
    //   13: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   16: invokeinterface isDbLockedByCurrentThread : ()Z
    //   21: ifeq -> 66
    //   24: aload_2
    //   25: monitorenter
    //   26: aload_0
    //   27: getfield isStandardSQLite : Z
    //   30: ifeq -> 51
    //   33: aload_0
    //   34: aload_1
    //   35: aload_2
    //   36: invokeinterface getRawStatement : ()Ljava/lang/Object;
    //   41: checkcast android/database/sqlite/SQLiteStatement
    //   44: iconst_1
    //   45: invokevirtual updateInsideSynchronized : (Ljava/lang/Object;Landroid/database/sqlite/SQLiteStatement;Z)V
    //   48: aload_2
    //   49: monitorexit
    //   50: return
    //   51: aload_0
    //   52: aload_1
    //   53: aload_2
    //   54: iconst_1
    //   55: invokevirtual updateInsideSynchronized : (Ljava/lang/Object;Lorg/greenrobot/greendao/database/DatabaseStatement;Z)V
    //   58: goto -> 48
    //   61: astore_1
    //   62: aload_2
    //   63: monitorexit
    //   64: aload_1
    //   65: athrow
    //   66: aload_0
    //   67: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   70: invokeinterface beginTransaction : ()V
    //   75: aload_2
    //   76: monitorenter
    //   77: aload_0
    //   78: aload_1
    //   79: aload_2
    //   80: iconst_1
    //   81: invokevirtual updateInsideSynchronized : (Ljava/lang/Object;Lorg/greenrobot/greendao/database/DatabaseStatement;Z)V
    //   84: aload_2
    //   85: monitorexit
    //   86: aload_0
    //   87: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   90: invokeinterface setTransactionSuccessful : ()V
    //   95: aload_0
    //   96: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   99: invokeinterface endTransaction : ()V
    //   104: goto -> 50
    //   107: astore_1
    //   108: aload_2
    //   109: monitorexit
    //   110: aload_1
    //   111: athrow
    //   112: astore_1
    //   113: aload_0
    //   114: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   117: invokeinterface endTransaction : ()V
    //   122: aload_1
    //   123: athrow
    // Exception table:
    //   from	to	target	type
    //   26	48	61	finally
    //   48	50	61	finally
    //   51	58	61	finally
    //   62	64	61	finally
    //   75	77	112	finally
    //   77	86	107	finally
    //   86	95	112	finally
    //   108	110	107	finally
    //   110	112	112	finally
  }
  
  public void updateInTx(Iterable<T> paramIterable) {
    // Byte code:
    //   0: aload_0
    //   1: getfield statements : Lorg/greenrobot/greendao/internal/TableStatements;
    //   4: invokevirtual getUpdateStatement : ()Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   7: astore_2
    //   8: aload_0
    //   9: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   12: invokeinterface beginTransaction : ()V
    //   17: aload_2
    //   18: monitorenter
    //   19: aload_0
    //   20: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   23: ifnull -> 35
    //   26: aload_0
    //   27: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   30: invokeinterface lock : ()V
    //   35: aload_0
    //   36: getfield isStandardSQLite : Z
    //   39: ifeq -> 118
    //   42: aload_2
    //   43: invokeinterface getRawStatement : ()Ljava/lang/Object;
    //   48: checkcast android/database/sqlite/SQLiteStatement
    //   51: astore_3
    //   52: aload_1
    //   53: invokeinterface iterator : ()Ljava/util/Iterator;
    //   58: astore_1
    //   59: aload_1
    //   60: invokeinterface hasNext : ()Z
    //   65: ifeq -> 149
    //   68: aload_0
    //   69: aload_1
    //   70: invokeinterface next : ()Ljava/lang/Object;
    //   75: aload_3
    //   76: iconst_0
    //   77: invokevirtual updateInsideSynchronized : (Ljava/lang/Object;Landroid/database/sqlite/SQLiteStatement;Z)V
    //   80: goto -> 59
    //   83: astore_1
    //   84: aload_0
    //   85: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   88: ifnull -> 100
    //   91: aload_0
    //   92: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   95: invokeinterface unlock : ()V
    //   100: aload_1
    //   101: athrow
    //   102: astore_1
    //   103: aload_2
    //   104: monitorexit
    //   105: aload_1
    //   106: athrow
    //   107: astore_2
    //   108: aload_0
    //   109: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   112: invokeinterface endTransaction : ()V
    //   117: return
    //   118: aload_1
    //   119: invokeinterface iterator : ()Ljava/util/Iterator;
    //   124: astore_1
    //   125: aload_1
    //   126: invokeinterface hasNext : ()Z
    //   131: ifeq -> 149
    //   134: aload_0
    //   135: aload_1
    //   136: invokeinterface next : ()Ljava/lang/Object;
    //   141: aload_2
    //   142: iconst_0
    //   143: invokevirtual updateInsideSynchronized : (Ljava/lang/Object;Lorg/greenrobot/greendao/database/DatabaseStatement;Z)V
    //   146: goto -> 125
    //   149: aload_0
    //   150: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   153: ifnull -> 165
    //   156: aload_0
    //   157: getfield identityScope : Lorg/greenrobot/greendao/identityscope/IdentityScope;
    //   160: invokeinterface unlock : ()V
    //   165: aload_2
    //   166: monitorexit
    //   167: aload_0
    //   168: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   171: invokeinterface setTransactionSuccessful : ()V
    //   176: aload_0
    //   177: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   180: invokeinterface endTransaction : ()V
    //   185: goto -> 117
    //   188: astore_1
    //   189: iconst_0
    //   190: ifeq -> 203
    //   193: ldc_w 'Could not end transaction (rethrowing initial exception)'
    //   196: aload_1
    //   197: invokestatic w : (Ljava/lang/String;Ljava/lang/Throwable;)I
    //   200: pop
    //   201: aconst_null
    //   202: athrow
    //   203: aload_1
    //   204: athrow
    //   205: astore_1
    //   206: aload_2
    //   207: ifnull -> 220
    //   210: ldc_w 'Could not end transaction (rethrowing initial exception)'
    //   213: aload_1
    //   214: invokestatic w : (Ljava/lang/String;Ljava/lang/Throwable;)I
    //   217: pop
    //   218: aload_2
    //   219: athrow
    //   220: aload_1
    //   221: athrow
    //   222: astore_1
    //   223: aload_0
    //   224: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   227: invokeinterface endTransaction : ()V
    //   232: aload_1
    //   233: athrow
    //   234: astore_1
    //   235: iconst_0
    //   236: ifeq -> 249
    //   239: ldc_w 'Could not end transaction (rethrowing initial exception)'
    //   242: aload_1
    //   243: invokestatic w : (Ljava/lang/String;Ljava/lang/Throwable;)I
    //   246: pop
    //   247: aconst_null
    //   248: athrow
    //   249: aload_1
    //   250: athrow
    // Exception table:
    //   from	to	target	type
    //   17	19	107	java/lang/RuntimeException
    //   17	19	222	finally
    //   19	35	102	finally
    //   35	59	83	finally
    //   59	80	83	finally
    //   84	100	102	finally
    //   100	102	102	finally
    //   103	105	102	finally
    //   105	107	107	java/lang/RuntimeException
    //   105	107	222	finally
    //   108	117	205	java/lang/RuntimeException
    //   118	125	83	finally
    //   125	146	83	finally
    //   149	165	102	finally
    //   165	167	102	finally
    //   167	176	107	java/lang/RuntimeException
    //   167	176	222	finally
    //   176	185	188	java/lang/RuntimeException
    //   223	232	234	java/lang/RuntimeException
  }
  
  public void updateInTx(T... paramVarArgs) {
    updateInTx(Arrays.asList(paramVarArgs));
  }
  
  protected void updateInsideSynchronized(T paramT, SQLiteStatement paramSQLiteStatement, boolean paramBoolean) {
    bindValues(paramSQLiteStatement, paramT);
    int i = this.config.allColumns.length + 1;
    K k = getKey(paramT);
    if (k instanceof Long) {
      paramSQLiteStatement.bindLong(i, ((Long)k).longValue());
    } else {
      if (k == null)
        throw new DaoException("Cannot update entity without key - was it inserted before?"); 
      paramSQLiteStatement.bindString(i, k.toString());
    } 
    paramSQLiteStatement.execute();
    attachEntity(k, paramT, paramBoolean);
  }
  
  protected void updateInsideSynchronized(T paramT, DatabaseStatement paramDatabaseStatement, boolean paramBoolean) {
    bindValues(paramDatabaseStatement, paramT);
    int i = this.config.allColumns.length + 1;
    K k = getKey(paramT);
    if (k instanceof Long) {
      paramDatabaseStatement.bindLong(i, ((Long)k).longValue());
    } else {
      if (k == null)
        throw new DaoException("Cannot update entity without key - was it inserted before?"); 
      paramDatabaseStatement.bindString(i, k.toString());
    } 
    paramDatabaseStatement.execute();
    attachEntity(k, paramT, paramBoolean);
  }
  
  protected abstract K updateKeyAfterInsert(T paramT, long paramLong);
  
  protected void updateKeyAfterInsertAndAttach(T paramT, long paramLong, boolean paramBoolean) {
    if (paramLong != -1L) {
      attachEntity(updateKeyAfterInsert(paramT, paramLong), paramT, paramBoolean);
      return;
    } 
    DaoLog.w("Could not insert row (executeInsert returned -1)");
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/AbstractDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */