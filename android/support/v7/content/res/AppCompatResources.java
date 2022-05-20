package android.support.v7.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;

public final class AppCompatResources {
  private static final String LOG_TAG = "AppCompatResources";
  
  private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<TypedValue>();
  
  private static final Object sColorStateCacheLock;
  
  private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches = new WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>>(0);
  
  static {
    sColorStateCacheLock = new Object();
  }
  
  private static void addColorStateListToCache(@NonNull Context paramContext, @ColorRes int paramInt, @NonNull ColorStateList paramColorStateList) {
    synchronized (sColorStateCacheLock) {
      SparseArray<ColorStateListCacheEntry> sparseArray1 = sColorStateCaches.get(paramContext);
      SparseArray<ColorStateListCacheEntry> sparseArray2 = sparseArray1;
      if (sparseArray1 == null) {
        sparseArray2 = new SparseArray();
        this();
        sColorStateCaches.put(paramContext, sparseArray2);
      } 
      ColorStateListCacheEntry colorStateListCacheEntry = new ColorStateListCacheEntry();
      this(paramColorStateList, paramContext.getResources().getConfiguration());
      sparseArray2.append(paramInt, colorStateListCacheEntry);
      return;
    } 
  }
  
  @Nullable
  private static ColorStateList getCachedColorStateList(@NonNull Context paramContext, @ColorRes int paramInt) {
    synchronized (sColorStateCacheLock) {
      SparseArray sparseArray = sColorStateCaches.get(paramContext);
      if (sparseArray != null && sparseArray.size() > 0) {
        ColorStateListCacheEntry colorStateListCacheEntry = (ColorStateListCacheEntry)sparseArray.get(paramInt);
        if (colorStateListCacheEntry != null) {
          if (colorStateListCacheEntry.configuration.equals(paramContext.getResources().getConfiguration()))
            return colorStateListCacheEntry.value; 
          sparseArray.remove(paramInt);
        } 
      } 
      return null;
    } 
  }
  
  public static ColorStateList getColorStateList(@NonNull Context paramContext, @ColorRes int paramInt) {
    if (Build.VERSION.SDK_INT >= 23)
      return paramContext.getColorStateList(paramInt); 
    ColorStateList colorStateList2 = getCachedColorStateList(paramContext, paramInt);
    ColorStateList colorStateList1 = colorStateList2;
    if (colorStateList2 == null) {
      colorStateList1 = inflateColorStateList(paramContext, paramInt);
      if (colorStateList1 != null) {
        addColorStateListToCache(paramContext, paramInt, colorStateList1);
        return colorStateList1;
      } 
      colorStateList1 = ContextCompat.getColorStateList(paramContext, paramInt);
    } 
    return colorStateList1;
  }
  
  @Nullable
  public static Drawable getDrawable(@NonNull Context paramContext, @DrawableRes int paramInt) {
    return AppCompatDrawableManager.get().getDrawable(paramContext, paramInt);
  }
  
  @NonNull
  private static TypedValue getTypedValue() {
    TypedValue typedValue1 = TL_TYPED_VALUE.get();
    TypedValue typedValue2 = typedValue1;
    if (typedValue1 == null) {
      typedValue2 = new TypedValue();
      TL_TYPED_VALUE.set(typedValue2);
    } 
    return typedValue2;
  }
  
  @Nullable
  private static ColorStateList inflateColorStateList(Context paramContext, int paramInt) {
    Context context1;
    Context context2 = null;
    if (isColorInt(paramContext, paramInt))
      return (ColorStateList)context2; 
    Resources resources = paramContext.getResources();
    XmlResourceParser xmlResourceParser = resources.getXml(paramInt);
    try {
      ColorStateList colorStateList = AppCompatColorStateListInflater.createFromXml(resources, (XmlPullParser)xmlResourceParser, paramContext.getTheme());
    } catch (Exception exception) {
      Log.e("AppCompatResources", "Failed to inflate ColorStateList, leaving it to the framework", exception);
      context1 = context2;
    } 
    return (ColorStateList)context1;
  }
  
  private static boolean isColorInt(@NonNull Context paramContext, @ColorRes int paramInt) {
    boolean bool = true;
    Resources resources = paramContext.getResources();
    TypedValue typedValue = getTypedValue();
    resources.getValue(paramInt, typedValue, true);
    if (typedValue.type < 28 || typedValue.type > 31)
      bool = false; 
    return bool;
  }
  
  private static class ColorStateListCacheEntry {
    final Configuration configuration;
    
    final ColorStateList value;
    
    ColorStateListCacheEntry(@NonNull ColorStateList param1ColorStateList, @NonNull Configuration param1Configuration) {
      this.value = param1ColorStateList;
      this.configuration = param1Configuration;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/content/res/AppCompatResources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */