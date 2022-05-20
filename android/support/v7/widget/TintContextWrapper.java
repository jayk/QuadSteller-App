package android.support.v7.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TintContextWrapper extends ContextWrapper {
  private static final Object CACHE_LOCK = new Object();
  
  private static ArrayList<WeakReference<TintContextWrapper>> sCache;
  
  private final Resources mResources;
  
  private final Resources.Theme mTheme;
  
  private TintContextWrapper(@NonNull Context paramContext) {
    super(paramContext);
    if (VectorEnabledTintResources.shouldBeUsed()) {
      this.mResources = new VectorEnabledTintResources((Context)this, paramContext.getResources());
      this.mTheme = this.mResources.newTheme();
      this.mTheme.setTo(paramContext.getTheme());
      return;
    } 
    this.mResources = new TintResources((Context)this, paramContext.getResources());
    this.mTheme = null;
  }
  
  private static boolean shouldWrap(@NonNull Context paramContext) {
    boolean bool = false;
    null = bool;
    if (!(paramContext instanceof TintContextWrapper)) {
      null = bool;
      if (!(paramContext.getResources() instanceof TintResources)) {
        if (paramContext.getResources() instanceof VectorEnabledTintResources)
          return bool; 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      null = bool;
      return VectorEnabledTintResources.shouldBeUsed() ? true : null;
    } 
    return true;
  }
  
  public static Context wrap(@NonNull Context paramContext) {
    if (shouldWrap(paramContext))
      synchronized (CACHE_LOCK) {
        if (sCache == null) {
          ArrayList<WeakReference<TintContextWrapper>> arrayList1 = new ArrayList();
          this();
          sCache = arrayList1;
        } else {
          int i;
          for (i = sCache.size() - 1; i >= 0; i--) {
            WeakReference weakReference1 = sCache.get(i);
            if (weakReference1 == null || weakReference1.get() == null)
              sCache.remove(i); 
          } 
          i = sCache.size() - 1;
          while (true) {
            if (i >= 0) {
              WeakReference<TintContextWrapper> weakReference2 = sCache.get(i);
              if (weakReference2 != null) {
                TintContextWrapper tintContextWrapper2 = weakReference2.get();
              } else {
                weakReference2 = null;
              } 
              if (weakReference2 != null && weakReference2.getBaseContext() == paramContext)
                return (Context)weakReference2; 
              i--;
              continue;
            } 
            TintContextWrapper tintContextWrapper1 = new TintContextWrapper();
            this(paramContext);
            arrayList = sCache;
            WeakReference<TintContextWrapper> weakReference1 = new WeakReference();
            this((T)tintContextWrapper1);
            arrayList.add(weakReference1);
            return (Context)tintContextWrapper1;
          } 
        } 
        TintContextWrapper tintContextWrapper = new TintContextWrapper();
        this((Context)arrayList);
        ArrayList<WeakReference<TintContextWrapper>> arrayList = sCache;
        WeakReference<TintContextWrapper> weakReference = new WeakReference();
        this((T)tintContextWrapper);
        arrayList.add(weakReference);
        return (Context)tintContextWrapper;
      }  
    return paramContext;
  }
  
  public AssetManager getAssets() {
    return this.mResources.getAssets();
  }
  
  public Resources getResources() {
    return this.mResources;
  }
  
  public Resources.Theme getTheme() {
    return (this.mTheme == null) ? super.getTheme() : this.mTheme;
  }
  
  public void setTheme(int paramInt) {
    if (this.mTheme == null) {
      super.setTheme(paramInt);
      return;
    } 
    this.mTheme.applyStyle(paramInt, true);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/TintContextWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */