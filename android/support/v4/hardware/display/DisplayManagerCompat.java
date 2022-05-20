package android.support.v4.hardware.display;

import android.content.Context;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public abstract class DisplayManagerCompat {
  public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
  
  private static final WeakHashMap<Context, DisplayManagerCompat> sInstances = new WeakHashMap<Context, DisplayManagerCompat>();
  
  public static DisplayManagerCompat getInstance(Context paramContext) {
    synchronized (sInstances) {
      DisplayManagerCompat displayManagerCompat1 = sInstances.get(paramContext);
      DisplayManagerCompat displayManagerCompat2 = displayManagerCompat1;
      if (displayManagerCompat1 == null) {
        if (Build.VERSION.SDK_INT >= 17) {
          displayManagerCompat2 = new JellybeanMr1Impl();
          super(paramContext);
        } else {
          displayManagerCompat2 = new LegacyImpl(paramContext);
        } 
        sInstances.put(paramContext, displayManagerCompat2);
      } 
      return displayManagerCompat2;
    } 
  }
  
  public abstract Display getDisplay(int paramInt);
  
  public abstract Display[] getDisplays();
  
  public abstract Display[] getDisplays(String paramString);
  
  private static class JellybeanMr1Impl extends DisplayManagerCompat {
    private final Object mDisplayManagerObj;
    
    public JellybeanMr1Impl(Context param1Context) {
      this.mDisplayManagerObj = DisplayManagerJellybeanMr1.getDisplayManager(param1Context);
    }
    
    public Display getDisplay(int param1Int) {
      return DisplayManagerJellybeanMr1.getDisplay(this.mDisplayManagerObj, param1Int);
    }
    
    public Display[] getDisplays() {
      return DisplayManagerJellybeanMr1.getDisplays(this.mDisplayManagerObj);
    }
    
    public Display[] getDisplays(String param1String) {
      return DisplayManagerJellybeanMr1.getDisplays(this.mDisplayManagerObj, param1String);
    }
  }
  
  private static class LegacyImpl extends DisplayManagerCompat {
    private final WindowManager mWindowManager;
    
    public LegacyImpl(Context param1Context) {
      this.mWindowManager = (WindowManager)param1Context.getSystemService("window");
    }
    
    public Display getDisplay(int param1Int) {
      Display display = this.mWindowManager.getDefaultDisplay();
      if (display.getDisplayId() != param1Int)
        display = null; 
      return display;
    }
    
    public Display[] getDisplays() {
      return new Display[] { this.mWindowManager.getDefaultDisplay() };
    }
    
    public Display[] getDisplays(String param1String) {
      return (param1String == null) ? getDisplays() : new Display[0];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/hardware/display/DisplayManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */