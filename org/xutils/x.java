package org.xutils;

import android.app.Application;
import android.content.Context;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import org.xutils.common.TaskController;
import org.xutils.common.task.TaskControllerImpl;
import org.xutils.db.DbManagerImpl;
import org.xutils.http.HttpManagerImpl;
import org.xutils.image.ImageManagerImpl;
import org.xutils.view.ViewInjectorImpl;

public final class x {
  public static Application app() {
    if (Ext.app == null)
      try {
        Context context = (Context)Class.forName("com.android.layoutlib.bridge.impl.RenderAction").getDeclaredMethod("getCurrentContext", new Class[0]).invoke(null, new Object[0]);
        MockApplication mockApplication = new MockApplication();
        this(context);
        Ext.access$102(mockApplication);
        return Ext.app;
      } catch (Throwable throwable) {
        throw new RuntimeException("please invoke x.Ext.init(app) on Application#onCreate() and register your Application in manifest.");
      }  
    return Ext.app;
  }
  
  public static DbManager getDb(DbManager.DaoConfig paramDaoConfig) {
    return DbManagerImpl.getInstance(paramDaoConfig);
  }
  
  public static HttpManager http() {
    if (Ext.httpManager == null)
      HttpManagerImpl.registerInstance(); 
    return Ext.httpManager;
  }
  
  public static ImageManager image() {
    if (Ext.imageManager == null)
      ImageManagerImpl.registerInstance(); 
    return Ext.imageManager;
  }
  
  public static boolean isDebug() {
    return Ext.debug;
  }
  
  public static TaskController task() {
    return Ext.taskController;
  }
  
  public static ViewInjector view() {
    if (Ext.viewInjector == null)
      ViewInjectorImpl.registerInstance(); 
    return Ext.viewInjector;
  }
  
  public static class Ext {
    private static Application app;
    
    private static boolean debug;
    
    private static HttpManager httpManager;
    
    private static ImageManager imageManager;
    
    private static TaskController taskController;
    
    private static ViewInjector viewInjector;
    
    public static void init(Application param1Application) {
      TaskControllerImpl.registerInstance();
      if (app == null)
        app = param1Application; 
    }
    
    public static void setDebug(boolean param1Boolean) {
      debug = param1Boolean;
    }
    
    public static void setDefaultHostnameVerifier(HostnameVerifier param1HostnameVerifier) {
      HttpsURLConnection.setDefaultHostnameVerifier(param1HostnameVerifier);
    }
    
    public static void setHttpManager(HttpManager param1HttpManager) {
      httpManager = param1HttpManager;
    }
    
    public static void setImageManager(ImageManager param1ImageManager) {
      imageManager = param1ImageManager;
    }
    
    public static void setTaskController(TaskController param1TaskController) {
      if (taskController == null)
        taskController = param1TaskController; 
    }
    
    public static void setViewInjector(ViewInjector param1ViewInjector) {
      viewInjector = param1ViewInjector;
    }
  }
  
  private static class MockApplication extends Application {
    public MockApplication(Context param1Context) {
      attachBaseContext(param1Context);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/x.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */