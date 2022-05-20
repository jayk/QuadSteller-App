package butterknife.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.View;
import java.lang.reflect.Array;
import java.util.List;

public final class Utils {
  private static final TypedValue VALUE = new TypedValue();
  
  private Utils() {
    throw new AssertionError("No instances.");
  }
  
  @SafeVarargs
  public static <T> T[] arrayOf(T... paramVarArgs) {
    return filterNull(paramVarArgs);
  }
  
  public static <T> T castParam(Object paramObject, String paramString1, int paramInt1, String paramString2, int paramInt2, Class<T> paramClass) {
    try {
      return paramClass.cast(paramObject);
    } catch (ClassCastException classCastException) {
      throw new IllegalStateException("Parameter #" + (paramInt1 + 1) + " of method '" + paramString1 + "' was of the wrong type for parameter #" + (paramInt2 + 1) + " of method '" + paramString2 + "'. See cause for more info.", classCastException);
    } 
  }
  
  public static <T> T castView(View paramView, @IdRes int paramInt, String paramString, Class<T> paramClass) {
    try {
      return paramClass.cast(paramView);
    } catch (ClassCastException classCastException) {
      String str = getResourceEntryName(paramView, paramInt);
      throw new IllegalStateException("View '" + str + "' with ID " + paramInt + " for " + paramString + " was of the wrong type. See cause for more info.", classCastException);
    } 
  }
  
  private static <T> T[] filterNull(T[] paramArrayOfT) {
    int i = paramArrayOfT.length;
    byte b = 0;
    int j = 0;
    while (b < i) {
      T t = paramArrayOfT[b];
      if (t != null) {
        int k = j + 1;
        paramArrayOfT[j] = t;
        j = k;
      } 
      b++;
    } 
    if (j != i) {
      Object[] arrayOfObject = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), j);
      System.arraycopy(paramArrayOfT, 0, arrayOfObject, 0, j);
      paramArrayOfT = (T[])arrayOfObject;
    } 
    return paramArrayOfT;
  }
  
  public static <T> T findOptionalViewAsType(View paramView, @IdRes int paramInt, String paramString, Class<T> paramClass) {
    return castView(paramView.findViewById(paramInt), paramInt, paramString, paramClass);
  }
  
  public static View findRequiredView(View paramView, @IdRes int paramInt, String paramString) {
    View view = paramView.findViewById(paramInt);
    if (view != null)
      return view; 
    String str = getResourceEntryName(paramView, paramInt);
    throw new IllegalStateException("Required view '" + str + "' with ID " + paramInt + " for " + paramString + " was not found. If this view is optional add '@Nullable' (fields) or '@Optional' (methods) annotation.");
  }
  
  public static <T> T findRequiredViewAsType(View paramView, @IdRes int paramInt, String paramString, Class<T> paramClass) {
    return castView(findRequiredView(paramView, paramInt, paramString), paramInt, paramString, paramClass);
  }
  
  @UiThread
  public static float getFloat(Context paramContext, @DimenRes int paramInt) {
    TypedValue typedValue = VALUE;
    paramContext.getResources().getValue(paramInt, typedValue, true);
    if (typedValue.type == 4)
      return typedValue.getFloat(); 
    throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(paramInt) + " type #0x" + Integer.toHexString(typedValue.type) + " is not valid");
  }
  
  private static String getResourceEntryName(View paramView, @IdRes int paramInt) {
    return paramView.isInEditMode() ? "<unavailable while editing>" : paramView.getContext().getResources().getResourceEntryName(paramInt);
  }
  
  @UiThread
  public static Drawable getTintedDrawable(Context paramContext, @DrawableRes int paramInt1, @AttrRes int paramInt2) {
    if (!paramContext.getTheme().resolveAttribute(paramInt2, VALUE, true))
      throw new Resources.NotFoundException("Required tint color attribute with name " + paramContext.getResources().getResourceEntryName(paramInt2) + " and attribute ID " + paramInt2 + " was not found."); 
    Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(paramContext, paramInt1).mutate());
    DrawableCompat.setTint(drawable, ContextCompat.getColor(paramContext, VALUE.resourceId));
    return drawable;
  }
  
  @SafeVarargs
  public static <T> List<T> listOf(T... paramVarArgs) {
    return new ImmutableList<T>(filterNull(paramVarArgs));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/butterknife/internal/Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */