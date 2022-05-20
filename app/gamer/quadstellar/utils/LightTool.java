package app.gamer.quadstellar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import app.gamer.quadstellar.App;
import java.util.regex.Pattern;

public class LightTool {
  public static int dip2px(Context paramContext, float paramFloat) {
    return (int)(paramFloat * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static String get2Number(int paramInt) {
    return (paramInt / 10 == 0) ? ("0" + paramInt) : (paramInt + "");
  }
  
  public static String getTransStr(Context paramContext, int paramInt, Object paramObject) {
    return String.format(paramContext.getResources().getString(paramInt), new Object[] { paramObject });
  }
  
  public static String getVersion(Context paramContext) {
    String str;
    try {
      str = (paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0)).versionName;
    } catch (Exception exception) {
      exception.printStackTrace();
      str = "1.0";
    } 
    return str;
  }
  
  public static boolean isConnected(Context paramContext) {
    boolean bool1 = false;
    ConnectivityManager connectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
    boolean bool2 = bool1;
    if (connectivityManager != null) {
      NetworkInfo[] arrayOfNetworkInfo = connectivityManager.getAllNetworkInfo();
      bool2 = bool1;
      if (arrayOfNetworkInfo != null) {
        int i = arrayOfNetworkInfo.length;
        for (byte b = 0;; b++) {
          bool2 = bool1;
          if (b < i) {
            if (arrayOfNetworkInfo[b].getState() == NetworkInfo.State.CONNECTED)
              return true; 
          } else {
            return bool2;
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public static boolean isMobileNO(String paramString) {
    return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(paramString).matches();
  }
  
  public static boolean passwordFormat(String paramString) {
    return Pattern.compile("^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,20}$").matcher(paramString).matches();
  }
  
  public static int px2dip(Context paramContext, float paramFloat) {
    return (int)(paramFloat / (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static void setCloseOther(ImageButton[] paramArrayOfImageButton, int paramInt, View[] paramArrayOfView) {
    for (byte b = 0; b < paramArrayOfImageButton.length; b++) {
      if (b == paramInt) {
        boolean bool;
        if (!paramArrayOfImageButton[b].isSelected()) {
          bool = true;
        } else {
          bool = false;
        } 
        paramArrayOfImageButton[b].setSelected(bool);
        if (bool) {
          paramArrayOfView[b].setVisibility(0);
        } else {
          paramArrayOfView[b].setVisibility(8);
        } 
      } else {
        paramArrayOfImageButton[b].setSelected(false);
        paramArrayOfView[b].setVisibility(8);
      } 
    } 
  }
  
  public static String setTimerFormat(String paramString1, String paramString2) {
    return paramString1 + App.getInstance().getResources().getString(2131296741) + paramString2 + App.getInstance().getResources().getString(2131296836);
  }
  
  public static void setVisibility(View paramView1, View paramView2) {
    paramView1.setVisibility(0);
    paramView2.setVisibility(8);
  }
  
  public static boolean validationPhone(Context paramContext, EditText paramEditText) {
    null = false;
    String str = paramEditText.getText().toString().trim();
    if (TextUtils.isEmpty(str)) {
      Toast.makeText(paramContext, paramContext.getString(2131296654), 0).show();
      return null;
    } 
    if (!isMobileNO(str)) {
      Toast.makeText(paramContext, paramContext.getString(2131296666), 0).show();
      return null;
    } 
    return true;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/LightTool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */