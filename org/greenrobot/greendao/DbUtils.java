package org.greenrobot.greendao;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.greenrobot.greendao.database.Database;

public class DbUtils {
  public static int copyAllBytes(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
    int i = 0;
    byte[] arrayOfByte = new byte[4096];
    while (true) {
      int j = paramInputStream.read(arrayOfByte);
      if (j == -1)
        return i; 
      paramOutputStream.write(arrayOfByte, 0, j);
      i += j;
    } 
  }
  
  public static int executeSqlScript(Context paramContext, Database paramDatabase, String paramString) throws IOException {
    return executeSqlScript(paramContext, paramDatabase, paramString, true);
  }
  
  public static int executeSqlScript(Context paramContext, Database paramDatabase, String paramString, boolean paramBoolean) throws IOException {
    String[] arrayOfString = (new String(readAsset(paramContext, paramString), "UTF-8")).split(";(\\s)*[\n\r]");
    if (paramBoolean) {
      int j = executeSqlStatementsInTx(paramDatabase, arrayOfString);
      DaoLog.i("Executed " + j + " statements from SQL script '" + paramString + "'");
      return j;
    } 
    int i = executeSqlStatements(paramDatabase, arrayOfString);
    DaoLog.i("Executed " + i + " statements from SQL script '" + paramString + "'");
    return i;
  }
  
  public static int executeSqlStatements(Database paramDatabase, String[] paramArrayOfString) {
    int i = 0;
    int j = paramArrayOfString.length;
    byte b = 0;
    while (b < j) {
      String str = paramArrayOfString[b].trim();
      int k = i;
      if (str.length() > 0) {
        paramDatabase.execSQL(str);
        k = i + 1;
      } 
      b++;
      i = k;
    } 
    return i;
  }
  
  public static int executeSqlStatementsInTx(Database paramDatabase, String[] paramArrayOfString) {
    paramDatabase.beginTransaction();
    try {
      int i = executeSqlStatements(paramDatabase, paramArrayOfString);
      paramDatabase.setTransactionSuccessful();
      return i;
    } finally {
      paramDatabase.endTransaction();
    } 
  }
  
  public static void logTableDump(SQLiteDatabase paramSQLiteDatabase, String paramString) {
    Cursor cursor = paramSQLiteDatabase.query(paramString, null, null, null, null, null, null);
    try {
      DaoLog.d(DatabaseUtils.dumpCursorToString(cursor));
      return;
    } finally {
      cursor.close();
    } 
  }
  
  public static byte[] readAllBytes(InputStream paramInputStream) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    copyAllBytes(paramInputStream, byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
  
  public static byte[] readAsset(Context paramContext, String paramString) throws IOException {
    InputStream inputStream = paramContext.getResources().getAssets().open(paramString);
    try {
      return readAllBytes(inputStream);
    } finally {
      inputStream.close();
    } 
  }
  
  public static void vacuum(Database paramDatabase) {
    paramDatabase.execSQL("VACUUM");
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/DbUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */