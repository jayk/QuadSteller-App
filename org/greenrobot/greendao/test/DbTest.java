package org.greenrobot.greendao.test;

import android.app.Application;
import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import java.util.Random;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.DbUtils;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

public abstract class DbTest extends AndroidTestCase {
  public static final String DB_NAME = "greendao-unittest-db.temp";
  
  private Application application;
  
  protected Database db;
  
  protected final boolean inMemory;
  
  protected final Random random;
  
  public DbTest() {
    this(true);
  }
  
  public DbTest(boolean paramBoolean) {
    this.inMemory = paramBoolean;
    this.random = new Random();
  }
  
  public <T extends Application> T createApplication(Class<T> paramClass) {
    assertNull("Application already created", this.application);
    try {
      Application application = Instrumentation.newApplication(paramClass, getContext());
      application.onCreate();
      this.application = application;
      return (T)application;
    } catch (Exception exception) {
      throw new RuntimeException("Could not create application " + paramClass, exception);
    } 
  }
  
  protected Database createDatabase() {
    if (this.inMemory) {
      SQLiteDatabase sQLiteDatabase1 = SQLiteDatabase.create(null);
      return (Database)new StandardDatabase(sQLiteDatabase1);
    } 
    getContext().deleteDatabase("greendao-unittest-db.temp");
    SQLiteDatabase sQLiteDatabase = getContext().openOrCreateDatabase("greendao-unittest-db.temp", 0, null);
    return (Database)new StandardDatabase(sQLiteDatabase);
  }
  
  public <T extends Application> T getApplication() {
    assertNotNull("Application not yet created", this.application);
    return (T)this.application;
  }
  
  protected void logTableDump(String paramString) {
    if (this.db instanceof StandardDatabase) {
      DbUtils.logTableDump(((StandardDatabase)this.db).getSQLiteDatabase(), paramString);
      return;
    } 
    DaoLog.w("Table dump unsupported for " + this.db);
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    this.db = createDatabase();
  }
  
  protected void tearDown() throws Exception {
    if (this.application != null)
      terminateApplication(); 
    this.db.close();
    if (!this.inMemory)
      getContext().deleteDatabase("greendao-unittest-db.temp"); 
    super.tearDown();
  }
  
  public void terminateApplication() {
    assertNotNull("Application not yet created", this.application);
    this.application.onTerminate();
    this.application = null;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/test/DbTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */