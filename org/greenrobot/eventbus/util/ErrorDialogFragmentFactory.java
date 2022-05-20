package org.greenrobot.eventbus.util;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class ErrorDialogFragmentFactory<T> {
  protected final ErrorDialogConfig config;
  
  protected ErrorDialogFragmentFactory(ErrorDialogConfig paramErrorDialogConfig) {
    this.config = paramErrorDialogConfig;
  }
  
  protected abstract T createErrorFragment(ThrowableFailureEvent paramThrowableFailureEvent, Bundle paramBundle);
  
  protected String getMessageFor(ThrowableFailureEvent paramThrowableFailureEvent, Bundle paramBundle) {
    int i = this.config.getMessageIdForThrowable(paramThrowableFailureEvent.throwable);
    return this.config.resources.getString(i);
  }
  
  protected String getTitleFor(ThrowableFailureEvent paramThrowableFailureEvent, Bundle paramBundle) {
    return this.config.resources.getString(this.config.defaultTitleId);
  }
  
  protected T prepareErrorFragment(ThrowableFailureEvent paramThrowableFailureEvent, boolean paramBoolean, Bundle paramBundle) {
    if (paramThrowableFailureEvent.isSuppressErrorUi())
      return null; 
    if (paramBundle != null) {
      paramBundle = (Bundle)paramBundle.clone();
    } else {
      paramBundle = new Bundle();
    } 
    if (!paramBundle.containsKey("de.greenrobot.eventbus.errordialog.title"))
      paramBundle.putString("de.greenrobot.eventbus.errordialog.title", getTitleFor(paramThrowableFailureEvent, paramBundle)); 
    if (!paramBundle.containsKey("de.greenrobot.eventbus.errordialog.message"))
      paramBundle.putString("de.greenrobot.eventbus.errordialog.message", getMessageFor(paramThrowableFailureEvent, paramBundle)); 
    if (!paramBundle.containsKey("de.greenrobot.eventbus.errordialog.finish_after_dialog"))
      paramBundle.putBoolean("de.greenrobot.eventbus.errordialog.finish_after_dialog", paramBoolean); 
    if (!paramBundle.containsKey("de.greenrobot.eventbus.errordialog.event_type_on_close") && this.config.defaultEventTypeOnDialogClosed != null)
      paramBundle.putSerializable("de.greenrobot.eventbus.errordialog.event_type_on_close", this.config.defaultEventTypeOnDialogClosed); 
    if (!paramBundle.containsKey("de.greenrobot.eventbus.errordialog.icon_id") && this.config.defaultDialogIconId != 0)
      paramBundle.putInt("de.greenrobot.eventbus.errordialog.icon_id", this.config.defaultDialogIconId); 
    return createErrorFragment(paramThrowableFailureEvent, paramBundle);
  }
  
  @TargetApi(11)
  public static class Honeycomb extends ErrorDialogFragmentFactory<Fragment> {
    public Honeycomb(ErrorDialogConfig param1ErrorDialogConfig) {
      super(param1ErrorDialogConfig);
    }
    
    protected Fragment createErrorFragment(ThrowableFailureEvent param1ThrowableFailureEvent, Bundle param1Bundle) {
      ErrorDialogFragments.Honeycomb honeycomb = new ErrorDialogFragments.Honeycomb();
      honeycomb.setArguments(param1Bundle);
      return (Fragment)honeycomb;
    }
  }
  
  public static class Support extends ErrorDialogFragmentFactory<Fragment> {
    public Support(ErrorDialogConfig param1ErrorDialogConfig) {
      super(param1ErrorDialogConfig);
    }
    
    protected Fragment createErrorFragment(ThrowableFailureEvent param1ThrowableFailureEvent, Bundle param1Bundle) {
      ErrorDialogFragments.Support support = new ErrorDialogFragments.Support();
      support.setArguments(param1Bundle);
      return (Fragment)support;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/util/ErrorDialogFragmentFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */