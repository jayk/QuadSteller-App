package app.gamer.quadstellar.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import app.gamer.quadstellar.App;

public class Tips {
  public static final int LONG = 1;
  
  public static final int SHORT = 0;
  
  public static final String TAG = Tips.class.getSimpleName();
  
  public static void showImageToast(Activity paramActivity, int paramInt1, Object paramObject, int paramInt2) {
    if (paramActivity != null) {
      Toast toast = null;
      if (paramObject instanceof CharSequence) {
        toast = Toast.makeText((Context)paramActivity, (CharSequence)paramObject, paramInt2);
      } else if (paramObject instanceof Integer) {
        toast = Toast.makeText((Context)paramActivity, ((Integer)paramObject).intValue(), paramInt2);
      } 
      toast.setGravity(17, 0, 0);
      paramObject = toast.getView();
      ImageView imageView = new ImageView((Context)paramActivity);
      imageView.setImageResource(paramInt1);
      LinearLayout linearLayout = new LinearLayout((Context)paramActivity);
      linearLayout.addView((View)imageView);
      linearLayout.addView((View)paramObject);
      toast.setView((View)linearLayout);
      toast.show();
    } 
  }
  
  public static void showLongToast(CharSequence paramCharSequence) {
    if (App.getAppContext() != null)
      Toast.makeText(App.getAppContext(), paramCharSequence, 1).show(); 
  }
  
  public static void showShortToast(int paramInt) {
    if (App.getAppContext() != null)
      Toast.makeText(App.getAppContext(), paramInt, 0).show(); 
  }
  
  public static void showShortToast(CharSequence paramCharSequence) {
    if (App.getAppContext() != null)
      Toast.makeText(App.getAppContext(), paramCharSequence, 0).show(); 
  }
  
  public static void showToast(Context paramContext, int paramInt1, int paramInt2) {
    if (paramContext != null)
      Toast.makeText(paramContext, paramInt1, paramInt2).show(); 
  }
  
  public static void showToast(CharSequence paramCharSequence, int paramInt) {
    if (App.getAppContext() != null)
      Toast.makeText(App.getAppContext(), paramCharSequence, paramInt).show(); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/Tips.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */