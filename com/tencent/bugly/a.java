package com.tencent.bugly;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;

public abstract class a {
  public int id;
  
  public String moduleName;
  
  public String version;
  
  public String versionKey;
  
  public abstract String[] getTables();
  
  public abstract void init(Context paramContext, boolean paramBoolean, BuglyStrategy paramBuglyStrategy);
  
  public void onDbCreate(SQLiteDatabase paramSQLiteDatabase) {}
  
  public void onDbDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
    try {
      if (getTables() != null) {
        String[] arrayOfString = getTables();
        paramInt2 = arrayOfString.length;
        for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
          String str = arrayOfString[paramInt1];
          StringBuilder stringBuilder = new StringBuilder();
          this("DROP TABLE IF EXISTS ");
          paramSQLiteDatabase.execSQL(stringBuilder.append(str).toString());
        } 
        onDbCreate(paramSQLiteDatabase);
      } 
    } catch (Throwable throwable) {}
  }
  
  public void onDbUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
    try {
      if (getTables() != null) {
        String[] arrayOfString = getTables();
        paramInt2 = arrayOfString.length;
        for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
          String str = arrayOfString[paramInt1];
          StringBuilder stringBuilder = new StringBuilder();
          this("DROP TABLE IF EXISTS ");
          paramSQLiteDatabase.execSQL(stringBuilder.append(str).toString());
        } 
        onDbCreate(paramSQLiteDatabase);
      } 
    } catch (Throwable throwable) {}
  }
  
  public void onServerStrategyChanged(StrategyBean paramStrategyBean) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */