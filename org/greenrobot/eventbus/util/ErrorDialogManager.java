package org.greenrobot.eventbus.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;

public class ErrorDialogManager {
  public static final String KEY_EVENT_TYPE_ON_CLOSE = "de.greenrobot.eventbus.errordialog.event_type_on_close";
  
  public static final String KEY_FINISH_AFTER_DIALOG = "de.greenrobot.eventbus.errordialog.finish_after_dialog";
  
  public static final String KEY_ICON_ID = "de.greenrobot.eventbus.errordialog.icon_id";
  
  public static final String KEY_MESSAGE = "de.greenrobot.eventbus.errordialog.message";
  
  public static final String KEY_TITLE = "de.greenrobot.eventbus.errordialog.title";
  
  protected static final String TAG_ERROR_DIALOG = "de.greenrobot.eventbus.error_dialog";
  
  protected static final String TAG_ERROR_DIALOG_MANAGER = "de.greenrobot.eventbus.error_dialog_manager";
  
  public static ErrorDialogFragmentFactory<?> factory;
  
  public static void attachTo(Activity paramActivity) {
    attachTo(paramActivity, false, null);
  }
  
  public static void attachTo(Activity paramActivity, Object paramObject, boolean paramBoolean, Bundle paramBundle) {
    if (factory == null)
      throw new RuntimeException("You must set the static factory field to configure error dialogs for your app."); 
    if (isSupportActivity(paramActivity)) {
      SupportManagerFragment.attachTo(paramActivity, paramObject, paramBoolean, paramBundle);
      return;
    } 
    HoneycombManagerFragment.attachTo(paramActivity, paramObject, paramBoolean, paramBundle);
  }
  
  public static void attachTo(Activity paramActivity, boolean paramBoolean) {
    attachTo(paramActivity, paramBoolean, null);
  }
  
  public static void attachTo(Activity paramActivity, boolean paramBoolean, Bundle paramBundle) {
    attachTo(paramActivity, paramActivity.getClass(), paramBoolean, paramBundle);
  }
  
  protected static void checkLogException(ThrowableFailureEvent paramThrowableFailureEvent) {
    if (factory.config.logExceptions) {
      String str1 = factory.config.tagForLoggingExceptions;
      String str2 = str1;
      if (str1 == null)
        str2 = EventBus.TAG; 
      Log.i(str2, "Error dialog manager received exception", paramThrowableFailureEvent.throwable);
    } 
  }
  
  private static boolean isInExecutionScope(Object paramObject, ThrowableFailureEvent paramThrowableFailureEvent) {
    if (paramThrowableFailureEvent != null) {
      Object object = paramThrowableFailureEvent.getExecutionScope();
      if (object != null && !object.equals(paramObject))
        return false; 
    } 
    return true;
  }
  
  private static boolean isSupportActivity(Activity paramActivity) {
    boolean bool = false;
    for (Class<?> clazz = paramActivity.getClass().getSuperclass();; clazz = clazz.getSuperclass()) {
      if (clazz == null)
        throw new RuntimeException("Illegal activity type: " + paramActivity.getClass()); 
      String str = clazz.getName();
      if (str.equals("android.support.v4.app.FragmentActivity"))
        return true; 
      if (str.startsWith("com.actionbarsherlock.app") && (str.endsWith(".SherlockActivity") || str.endsWith(".SherlockListActivity") || str.endsWith(".SherlockPreferenceActivity")))
        throw new RuntimeException("Please use SherlockFragmentActivity. Illegal activity: " + str); 
      if (str.equals("android.app.Activity")) {
        if (Build.VERSION.SDK_INT < 11)
          throw new RuntimeException("Illegal activity without fragment support. Either use Android 3.0+ or android.support.v4.app.FragmentActivity."); 
        return bool;
      } 
    } 
  }
  
  @TargetApi(11)
  public static class HoneycombManagerFragment extends Fragment {
    protected Bundle argumentsForErrorDialog;
    
    private EventBus eventBus;
    
    private Object executionScope;
    
    protected boolean finishAfterDialog;
    
    public static void attachTo(Activity param1Activity, Object param1Object, boolean param1Boolean, Bundle param1Bundle) {
      FragmentManager fragmentManager = param1Activity.getFragmentManager();
      HoneycombManagerFragment honeycombManagerFragment2 = (HoneycombManagerFragment)fragmentManager.findFragmentByTag("de.greenrobot.eventbus.error_dialog_manager");
      HoneycombManagerFragment honeycombManagerFragment1 = honeycombManagerFragment2;
      if (honeycombManagerFragment2 == null) {
        honeycombManagerFragment1 = new HoneycombManagerFragment();
        fragmentManager.beginTransaction().add(honeycombManagerFragment1, "de.greenrobot.eventbus.error_dialog_manager").commit();
        fragmentManager.executePendingTransactions();
      } 
      honeycombManagerFragment1.finishAfterDialog = param1Boolean;
      honeycombManagerFragment1.argumentsForErrorDialog = param1Bundle;
      honeycombManagerFragment1.executionScope = param1Object;
    }
    
    public void onEventMainThread(ThrowableFailureEvent param1ThrowableFailureEvent) {
      if (ErrorDialogManager.isInExecutionScope(this.executionScope, param1ThrowableFailureEvent)) {
        ErrorDialogManager.checkLogException(param1ThrowableFailureEvent);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();
        DialogFragment dialogFragment2 = (DialogFragment)fragmentManager.findFragmentByTag("de.greenrobot.eventbus.error_dialog");
        if (dialogFragment2 != null)
          dialogFragment2.dismiss(); 
        DialogFragment dialogFragment1 = (DialogFragment)ErrorDialogManager.factory.prepareErrorFragment(param1ThrowableFailureEvent, this.finishAfterDialog, this.argumentsForErrorDialog);
        if (dialogFragment1 != null)
          dialogFragment1.show(fragmentManager, "de.greenrobot.eventbus.error_dialog"); 
      } 
    }
    
    public void onPause() {
      this.eventBus.unregister(this);
      super.onPause();
    }
    
    public void onResume() {
      super.onResume();
      this.eventBus = ErrorDialogManager.factory.config.getEventBus();
      this.eventBus.register(this);
    }
  }
  
  public static class SupportManagerFragment extends Fragment {
    protected Bundle argumentsForErrorDialog;
    
    private EventBus eventBus;
    
    private Object executionScope;
    
    protected boolean finishAfterDialog;
    
    private boolean skipRegisterOnNextResume;
    
    public static void attachTo(Activity param1Activity, Object param1Object, boolean param1Boolean, Bundle param1Bundle) {
      FragmentManager fragmentManager = ((FragmentActivity)param1Activity).getSupportFragmentManager();
      SupportManagerFragment supportManagerFragment2 = (SupportManagerFragment)fragmentManager.findFragmentByTag("de.greenrobot.eventbus.error_dialog_manager");
      SupportManagerFragment supportManagerFragment1 = supportManagerFragment2;
      if (supportManagerFragment2 == null) {
        supportManagerFragment1 = new SupportManagerFragment();
        fragmentManager.beginTransaction().add(supportManagerFragment1, "de.greenrobot.eventbus.error_dialog_manager").commit();
        fragmentManager.executePendingTransactions();
      } 
      supportManagerFragment1.finishAfterDialog = param1Boolean;
      supportManagerFragment1.argumentsForErrorDialog = param1Bundle;
      supportManagerFragment1.executionScope = param1Object;
    }
    
    public void onCreate(Bundle param1Bundle) {
      super.onCreate(param1Bundle);
      this.eventBus = ErrorDialogManager.factory.config.getEventBus();
      this.eventBus.register(this);
      this.skipRegisterOnNextResume = true;
    }
    
    public void onEventMainThread(ThrowableFailureEvent param1ThrowableFailureEvent) {
      if (ErrorDialogManager.isInExecutionScope(this.executionScope, param1ThrowableFailureEvent)) {
        ErrorDialogManager.checkLogException(param1ThrowableFailureEvent);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();
        DialogFragment dialogFragment2 = (DialogFragment)fragmentManager.findFragmentByTag("de.greenrobot.eventbus.error_dialog");
        if (dialogFragment2 != null)
          dialogFragment2.dismiss(); 
        DialogFragment dialogFragment1 = (DialogFragment)ErrorDialogManager.factory.prepareErrorFragment(param1ThrowableFailureEvent, this.finishAfterDialog, this.argumentsForErrorDialog);
        if (dialogFragment1 != null)
          dialogFragment1.show(fragmentManager, "de.greenrobot.eventbus.error_dialog"); 
      } 
    }
    
    public void onPause() {
      this.eventBus.unregister(this);
      super.onPause();
    }
    
    public void onResume() {
      super.onResume();
      if (this.skipRegisterOnNextResume) {
        this.skipRegisterOnNextResume = false;
        return;
      } 
      this.eventBus = ErrorDialogManager.factory.config.getEventBus();
      this.eventBus.register(this);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/util/ErrorDialogManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */