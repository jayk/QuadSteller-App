package org.greenrobot.greendao.internal;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

public class TableStatements {
  private final String[] allColumns;
  
  private DatabaseStatement countStatement;
  
  private final Database db;
  
  private DatabaseStatement deleteStatement;
  
  private DatabaseStatement insertOrReplaceStatement;
  
  private DatabaseStatement insertStatement;
  
  private final String[] pkColumns;
  
  private volatile String selectAll;
  
  private volatile String selectByKey;
  
  private volatile String selectByRowId;
  
  private volatile String selectKeys;
  
  private final String tablename;
  
  private DatabaseStatement updateStatement;
  
  public TableStatements(Database paramDatabase, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    this.db = paramDatabase;
    this.tablename = paramString;
    this.allColumns = paramArrayOfString1;
    this.pkColumns = paramArrayOfString2;
  }
  
  public DatabaseStatement getCountStatement() {
    if (this.countStatement == null) {
      String str = SqlUtils.createSqlCount(this.tablename);
      this.countStatement = this.db.compileStatement(str);
    } 
    return this.countStatement;
  }
  
  public DatabaseStatement getDeleteStatement() {
    // Byte code:
    //   0: aload_0
    //   1: getfield deleteStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   4: ifnonnull -> 60
    //   7: aload_0
    //   8: getfield tablename : Ljava/lang/String;
    //   11: aload_0
    //   12: getfield pkColumns : [Ljava/lang/String;
    //   15: invokestatic createSqlDelete : (Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;
    //   18: astore_1
    //   19: aload_0
    //   20: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   23: aload_1
    //   24: invokeinterface compileStatement : (Ljava/lang/String;)Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   29: astore_1
    //   30: aload_0
    //   31: monitorenter
    //   32: aload_0
    //   33: getfield deleteStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   36: ifnonnull -> 44
    //   39: aload_0
    //   40: aload_1
    //   41: putfield deleteStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_0
    //   47: getfield deleteStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   50: aload_1
    //   51: if_acmpeq -> 60
    //   54: aload_1
    //   55: invokeinterface close : ()V
    //   60: aload_0
    //   61: getfield deleteStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   64: areturn
    //   65: astore_1
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_1
    //   69: athrow
    // Exception table:
    //   from	to	target	type
    //   32	44	65	finally
    //   44	46	65	finally
    //   66	68	65	finally
  }
  
  public DatabaseStatement getInsertOrReplaceStatement() {
    // Byte code:
    //   0: aload_0
    //   1: getfield insertOrReplaceStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   4: ifnonnull -> 62
    //   7: ldc 'INSERT OR REPLACE INTO '
    //   9: aload_0
    //   10: getfield tablename : Ljava/lang/String;
    //   13: aload_0
    //   14: getfield allColumns : [Ljava/lang/String;
    //   17: invokestatic createSqlInsert : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;
    //   20: astore_1
    //   21: aload_0
    //   22: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   25: aload_1
    //   26: invokeinterface compileStatement : (Ljava/lang/String;)Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   31: astore_1
    //   32: aload_0
    //   33: monitorenter
    //   34: aload_0
    //   35: getfield insertOrReplaceStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   38: ifnonnull -> 46
    //   41: aload_0
    //   42: aload_1
    //   43: putfield insertOrReplaceStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_0
    //   49: getfield insertOrReplaceStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   52: aload_1
    //   53: if_acmpeq -> 62
    //   56: aload_1
    //   57: invokeinterface close : ()V
    //   62: aload_0
    //   63: getfield insertOrReplaceStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   66: areturn
    //   67: astore_1
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_1
    //   71: athrow
    // Exception table:
    //   from	to	target	type
    //   34	46	67	finally
    //   46	48	67	finally
    //   68	70	67	finally
  }
  
  public DatabaseStatement getInsertStatement() {
    // Byte code:
    //   0: aload_0
    //   1: getfield insertStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   4: ifnonnull -> 62
    //   7: ldc 'INSERT INTO '
    //   9: aload_0
    //   10: getfield tablename : Ljava/lang/String;
    //   13: aload_0
    //   14: getfield allColumns : [Ljava/lang/String;
    //   17: invokestatic createSqlInsert : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;
    //   20: astore_1
    //   21: aload_0
    //   22: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   25: aload_1
    //   26: invokeinterface compileStatement : (Ljava/lang/String;)Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   31: astore_1
    //   32: aload_0
    //   33: monitorenter
    //   34: aload_0
    //   35: getfield insertStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   38: ifnonnull -> 46
    //   41: aload_0
    //   42: aload_1
    //   43: putfield insertStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_0
    //   49: getfield insertStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   52: aload_1
    //   53: if_acmpeq -> 62
    //   56: aload_1
    //   57: invokeinterface close : ()V
    //   62: aload_0
    //   63: getfield insertStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   66: areturn
    //   67: astore_1
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_1
    //   71: athrow
    // Exception table:
    //   from	to	target	type
    //   34	46	67	finally
    //   46	48	67	finally
    //   68	70	67	finally
  }
  
  public String getSelectAll() {
    if (this.selectAll == null)
      this.selectAll = SqlUtils.createSqlSelect(this.tablename, "T", this.allColumns, false); 
    return this.selectAll;
  }
  
  public String getSelectByKey() {
    if (this.selectByKey == null) {
      StringBuilder stringBuilder = new StringBuilder(getSelectAll());
      stringBuilder.append("WHERE ");
      SqlUtils.appendColumnsEqValue(stringBuilder, "T", this.pkColumns);
      this.selectByKey = stringBuilder.toString();
    } 
    return this.selectByKey;
  }
  
  public String getSelectByRowId() {
    if (this.selectByRowId == null)
      this.selectByRowId = getSelectAll() + "WHERE ROWID=?"; 
    return this.selectByRowId;
  }
  
  public String getSelectKeys() {
    if (this.selectKeys == null)
      this.selectKeys = SqlUtils.createSqlSelect(this.tablename, "T", this.pkColumns, false); 
    return this.selectKeys;
  }
  
  public DatabaseStatement getUpdateStatement() {
    // Byte code:
    //   0: aload_0
    //   1: getfield updateStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   4: ifnonnull -> 64
    //   7: aload_0
    //   8: getfield tablename : Ljava/lang/String;
    //   11: aload_0
    //   12: getfield allColumns : [Ljava/lang/String;
    //   15: aload_0
    //   16: getfield pkColumns : [Ljava/lang/String;
    //   19: invokestatic createSqlUpdate : (Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;
    //   22: astore_1
    //   23: aload_0
    //   24: getfield db : Lorg/greenrobot/greendao/database/Database;
    //   27: aload_1
    //   28: invokeinterface compileStatement : (Ljava/lang/String;)Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   33: astore_1
    //   34: aload_0
    //   35: monitorenter
    //   36: aload_0
    //   37: getfield updateStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   40: ifnonnull -> 48
    //   43: aload_0
    //   44: aload_1
    //   45: putfield updateStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_0
    //   51: getfield updateStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   54: aload_1
    //   55: if_acmpeq -> 64
    //   58: aload_1
    //   59: invokeinterface close : ()V
    //   64: aload_0
    //   65: getfield updateStatement : Lorg/greenrobot/greendao/database/DatabaseStatement;
    //   68: areturn
    //   69: astore_1
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_1
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   36	48	69	finally
    //   48	50	69	finally
    //   70	72	69	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/internal/TableStatements.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */