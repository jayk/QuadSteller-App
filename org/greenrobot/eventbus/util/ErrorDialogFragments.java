package org.greenrobot.eventbus.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialogFragments {
  public static int ERROR_DIALOG_ICON = 0;
  
  public static Class<?> EVENT_TYPE_ON_CLICK;
  
  public static Dialog createDialog(Context paramContext, Bundle paramBundle, DialogInterface.OnClickListener paramOnClickListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(paramContext);
    builder.setTitle(paramBundle.getString("de.greenrobot.eventbus.errordialog.title"));
    builder.setMessage(paramBundle.getString("de.greenrobot.eventbus.errordialog.message"));
    if (ERROR_DIALOG_ICON != 0)
      builder.setIcon(ERROR_DIALOG_ICON); 
    builder.setPositiveButton(17039370, paramOnClickListener);
    return (Dialog)builder.create();
  }
  
  public static void handleOnClick(DialogInterface paramDialogInterface, int paramInt, Activity paramActivity, Bundle paramBundle) {
    // Byte code:
    //   0: getstatic org/greenrobot/eventbus/util/ErrorDialogFragments.EVENT_TYPE_ON_CLICK : Ljava/lang/Class;
    //   3: ifnull -> 26
    //   6: getstatic org/greenrobot/eventbus/util/ErrorDialogFragments.EVENT_TYPE_ON_CLICK : Ljava/lang/Class;
    //   9: invokevirtual newInstance : ()Ljava/lang/Object;
    //   12: astore_0
    //   13: getstatic org/greenrobot/eventbus/util/ErrorDialogManager.factory : Lorg/greenrobot/eventbus/util/ErrorDialogFragmentFactory;
    //   16: getfield config : Lorg/greenrobot/eventbus/util/ErrorDialogConfig;
    //   19: invokevirtual getEventBus : ()Lorg/greenrobot/eventbus/EventBus;
    //   22: aload_0
    //   23: invokevirtual post : (Ljava/lang/Object;)V
    //   26: aload_3
    //   27: ldc 'de.greenrobot.eventbus.errordialog.finish_after_dialog'
    //   29: iconst_0
    //   30: invokevirtual getBoolean : (Ljava/lang/String;Z)Z
    //   33: ifeq -> 44
    //   36: aload_2
    //   37: ifnull -> 44
    //   40: aload_2
    //   41: invokevirtual finish : ()V
    //   44: return
    //   45: astore_0
    //   46: new java/lang/RuntimeException
    //   49: dup
    //   50: ldc 'Event cannot be constructed'
    //   52: aload_0
    //   53: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   6	13	45	java/lang/Exception
  }
  
  @TargetApi(11)
  public static class Honeycomb extends DialogFragment implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
      ErrorDialogFragments.handleOnClick(param1DialogInterface, param1Int, getActivity(), getArguments());
    }
    
    public Dialog onCreateDialog(Bundle param1Bundle) {
      return ErrorDialogFragments.createDialog((Context)getActivity(), getArguments(), this);
    }
  }
  
  public static class Support extends DialogFragment implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
      ErrorDialogFragments.handleOnClick(param1DialogInterface, param1Int, (Activity)getActivity(), getArguments());
    }
    
    public Dialog onCreateDialog(Bundle param1Bundle) {
      return ErrorDialogFragments.createDialog((Context)getActivity(), getArguments(), this);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/util/ErrorDialogFragments.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */