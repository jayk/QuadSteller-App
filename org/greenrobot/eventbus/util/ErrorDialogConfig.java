package org.greenrobot.eventbus.util;

import android.content.res.Resources;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;

public class ErrorDialogConfig {
  int defaultDialogIconId;
  
  final int defaultErrorMsgId;
  
  Class<?> defaultEventTypeOnDialogClosed;
  
  final int defaultTitleId;
  
  EventBus eventBus;
  
  boolean logExceptions = true;
  
  final ExceptionToResourceMapping mapping;
  
  final Resources resources;
  
  String tagForLoggingExceptions;
  
  public ErrorDialogConfig(Resources paramResources, int paramInt1, int paramInt2) {
    this.resources = paramResources;
    this.defaultTitleId = paramInt1;
    this.defaultErrorMsgId = paramInt2;
    this.mapping = new ExceptionToResourceMapping();
  }
  
  public ErrorDialogConfig addMapping(Class<? extends Throwable> paramClass, int paramInt) {
    this.mapping.addMapping(paramClass, paramInt);
    return this;
  }
  
  public void disableExceptionLogging() {
    this.logExceptions = false;
  }
  
  EventBus getEventBus() {
    return (this.eventBus != null) ? this.eventBus : EventBus.getDefault();
  }
  
  public int getMessageIdForThrowable(Throwable paramThrowable) {
    Integer integer = this.mapping.mapThrowable(paramThrowable);
    if (integer != null)
      return integer.intValue(); 
    Log.d(EventBus.TAG, "No specific message ressource ID found for " + paramThrowable);
    return this.defaultErrorMsgId;
  }
  
  public void setDefaultDialogIconId(int paramInt) {
    this.defaultDialogIconId = paramInt;
  }
  
  public void setDefaultEventTypeOnDialogClosed(Class<?> paramClass) {
    this.defaultEventTypeOnDialogClosed = paramClass;
  }
  
  public void setEventBus(EventBus paramEventBus) {
    this.eventBus = paramEventBus;
  }
  
  public void setTagForLoggingExceptions(String paramString) {
    this.tagForLoggingExceptions = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/util/ErrorDialogConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */