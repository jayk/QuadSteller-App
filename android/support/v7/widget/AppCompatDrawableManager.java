package android.support.v7.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.LruCache;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class AppCompatDrawableManager {
  private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
  
  private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
  
  private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
  
  private static final ColorFilterLruCache COLOR_FILTER_CACHE;
  
  private static final boolean DEBUG = false;
  
  private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
  
  private static AppCompatDrawableManager INSTANCE;
  
  private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
  
  private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
  
  private static final String TAG = "AppCompatDrawableManager";
  
  private static final int[] TINT_CHECKABLE_BUTTON_LIST;
  
  private static final int[] TINT_COLOR_CONTROL_NORMAL;
  
  private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
  
  private ArrayMap<String, InflateDelegate> mDelegates;
  
  private final Object mDrawableCacheLock = new Object();
  
  private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDrawableCaches = new WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>>(0);
  
  private boolean mHasCheckedVectorDrawableSetup;
  
  private SparseArray<String> mKnownDrawableIdTags;
  
  private WeakHashMap<Context, SparseArray<ColorStateList>> mTintLists;
  
  private TypedValue mTypedValue;
  
  static {
    COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha };
    TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha };
    COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[] { R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_dark, R.drawable.abc_text_select_handle_middle_mtrl_dark, R.drawable.abc_text_select_handle_right_mtrl_dark, R.drawable.abc_text_select_handle_left_mtrl_light, R.drawable.abc_text_select_handle_middle_mtrl_light, R.drawable.abc_text_select_handle_right_mtrl_light };
    COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[] { R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult };
    TINT_COLOR_CONTROL_STATE_LIST = new int[] { R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material };
    TINT_CHECKABLE_BUTTON_LIST = new int[] { R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material };
  }
  
  private void addDelegate(@NonNull String paramString, @NonNull InflateDelegate paramInflateDelegate) {
    if (this.mDelegates == null)
      this.mDelegates = new ArrayMap(); 
    this.mDelegates.put(paramString, paramInflateDelegate);
  }
  
  private boolean addDrawableToCache(@NonNull Context paramContext, long paramLong, @NonNull Drawable paramDrawable) {
    Drawable.ConstantState constantState = paramDrawable.getConstantState();
    if (constantState != null)
      synchronized (this.mDrawableCacheLock) {
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray2 = this.mDrawableCaches.get(paramContext);
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray1 = longSparseArray2;
        if (longSparseArray2 == null) {
          longSparseArray1 = new LongSparseArray();
          this();
          this.mDrawableCaches.put(paramContext, longSparseArray1);
        } 
        WeakReference weakReference = new WeakReference();
        this((T)constantState);
        longSparseArray1.put(paramLong, weakReference);
        return true;
      }  
    return false;
  }
  
  private void addTintListToCache(@NonNull Context paramContext, @DrawableRes int paramInt, @NonNull ColorStateList paramColorStateList) {
    if (this.mTintLists == null)
      this.mTintLists = new WeakHashMap<Context, SparseArray<ColorStateList>>(); 
    SparseArray<ColorStateList> sparseArray1 = this.mTintLists.get(paramContext);
    SparseArray<ColorStateList> sparseArray2 = sparseArray1;
    if (sparseArray1 == null) {
      sparseArray2 = new SparseArray();
      this.mTintLists.put(paramContext, sparseArray2);
    } 
    sparseArray2.append(paramInt, paramColorStateList);
  }
  
  private static boolean arrayContains(int[] paramArrayOfint, int paramInt) {
    boolean bool = false;
    int i = paramArrayOfint.length;
    for (byte b = 0;; b++) {
      boolean bool1 = bool;
      if (b < i) {
        if (paramArrayOfint[b] == paramInt)
          return true; 
      } else {
        return bool1;
      } 
    } 
  }
  
  private void checkVectorDrawableSetup(@NonNull Context paramContext) {
    if (!this.mHasCheckedVectorDrawableSetup) {
      this.mHasCheckedVectorDrawableSetup = true;
      Drawable drawable = getDrawable(paramContext, R.drawable.abc_vector_test);
      if (drawable == null || !isVectorDrawable(drawable)) {
        this.mHasCheckedVectorDrawableSetup = false;
        throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
      } 
    } 
  }
  
  private ColorStateList createBorderlessButtonColorStateList(@NonNull Context paramContext) {
    return createButtonColorStateList(paramContext, 0);
  }
  
  private ColorStateList createButtonColorStateList(@NonNull Context paramContext, @ColorInt int paramInt) {
    int[][] arrayOfInt = new int[4][];
    int[] arrayOfInt1 = new int[4];
    int i = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlHighlight);
    int j = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorButtonNormal);
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    arrayOfInt1[0] = j;
    j = 0 + 1;
    arrayOfInt[j] = ThemeUtils.PRESSED_STATE_SET;
    arrayOfInt1[j] = ColorUtils.compositeColors(i, paramInt);
    arrayOfInt[++j] = ThemeUtils.FOCUSED_STATE_SET;
    arrayOfInt1[j] = ColorUtils.compositeColors(i, paramInt);
    i = j + 1;
    arrayOfInt[i] = ThemeUtils.EMPTY_STATE_SET;
    arrayOfInt1[i] = paramInt;
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private static long createCacheKey(TypedValue paramTypedValue) {
    return paramTypedValue.assetCookie << 32L | paramTypedValue.data;
  }
  
  private ColorStateList createColoredButtonColorStateList(@NonNull Context paramContext) {
    return createButtonColorStateList(paramContext, ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorAccent));
  }
  
  private ColorStateList createDefaultButtonColorStateList(@NonNull Context paramContext) {
    return createButtonColorStateList(paramContext, ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorButtonNormal));
  }
  
  private Drawable createDrawableIfNeeded(@NonNull Context paramContext, @DrawableRes int paramInt) {
    LayerDrawable layerDrawable;
    if (this.mTypedValue == null)
      this.mTypedValue = new TypedValue(); 
    TypedValue typedValue = this.mTypedValue;
    paramContext.getResources().getValue(paramInt, typedValue, true);
    long l = createCacheKey(typedValue);
    Drawable drawable = getCachedDrawable(paramContext, l);
    if (drawable == null) {
      if (paramInt == R.drawable.abc_cab_background_top_material)
        layerDrawable = new LayerDrawable(new Drawable[] { getDrawable(paramContext, R.drawable.abc_cab_background_internal_bg), getDrawable(paramContext, R.drawable.abc_cab_background_top_mtrl_alpha) }); 
      if (layerDrawable != null) {
        layerDrawable.setChangingConfigurations(typedValue.changingConfigurations);
        addDrawableToCache(paramContext, l, (Drawable)layerDrawable);
      } 
    } 
    return (Drawable)layerDrawable;
  }
  
  private static PorterDuffColorFilter createTintFilter(ColorStateList paramColorStateList, PorterDuff.Mode paramMode, int[] paramArrayOfint) {
    return (paramColorStateList == null || paramMode == null) ? null : getPorterDuffColorFilter(paramColorStateList.getColorForState(paramArrayOfint, 0), paramMode);
  }
  
  public static AppCompatDrawableManager get() {
    if (INSTANCE == null) {
      INSTANCE = new AppCompatDrawableManager();
      installDefaultInflateDelegates(INSTANCE);
    } 
    return INSTANCE;
  }
  
  private Drawable getCachedDrawable(@NonNull Context paramContext, long paramLong) {
    Context context = null;
    synchronized (this.mDrawableCacheLock) {
      LongSparseArray longSparseArray = this.mDrawableCaches.get(paramContext);
      if (longSparseArray == null)
        return (Drawable)context; 
      WeakReference<Drawable.ConstantState> weakReference = (WeakReference)longSparseArray.get(paramLong);
      if (weakReference != null) {
        Drawable.ConstantState constantState = weakReference.get();
        if (constantState != null)
          return constantState.newDrawable(paramContext.getResources()); 
        longSparseArray.delete(paramLong);
      } 
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
    return (Drawable)context;
  }
  
  public static PorterDuffColorFilter getPorterDuffColorFilter(int paramInt, PorterDuff.Mode paramMode) {
    PorterDuffColorFilter porterDuffColorFilter1 = COLOR_FILTER_CACHE.get(paramInt, paramMode);
    PorterDuffColorFilter porterDuffColorFilter2 = porterDuffColorFilter1;
    if (porterDuffColorFilter1 == null) {
      porterDuffColorFilter2 = new PorterDuffColorFilter(paramInt, paramMode);
      COLOR_FILTER_CACHE.put(paramInt, paramMode, porterDuffColorFilter2);
    } 
    return porterDuffColorFilter2;
  }
  
  private ColorStateList getTintListFromCache(@NonNull Context paramContext, @DrawableRes int paramInt) {
    ColorStateList colorStateList1 = null;
    ColorStateList colorStateList2 = colorStateList1;
    if (this.mTintLists != null) {
      SparseArray sparseArray = this.mTintLists.get(paramContext);
      colorStateList2 = colorStateList1;
      if (sparseArray != null)
        colorStateList2 = (ColorStateList)sparseArray.get(paramInt); 
    } 
    return colorStateList2;
  }
  
  static PorterDuff.Mode getTintMode(int paramInt) {
    PorterDuff.Mode mode = null;
    if (paramInt == R.drawable.abc_switch_thumb_material)
      mode = PorterDuff.Mode.MULTIPLY; 
    return mode;
  }
  
  private static void installDefaultInflateDelegates(@NonNull AppCompatDrawableManager paramAppCompatDrawableManager) {
    if (Build.VERSION.SDK_INT < 24) {
      paramAppCompatDrawableManager.addDelegate("vector", new VdcInflateDelegate());
      if (Build.VERSION.SDK_INT >= 11)
        paramAppCompatDrawableManager.addDelegate("animated-vector", new AvdcInflateDelegate()); 
    } 
  }
  
  private static boolean isVectorDrawable(@NonNull Drawable paramDrawable) {
    return (paramDrawable instanceof VectorDrawableCompat || "android.graphics.drawable.VectorDrawable".equals(paramDrawable.getClass().getName()));
  }
  
  private Drawable loadDrawableFromDelegates(@NonNull Context paramContext, @DrawableRes int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mDelegates : Landroid/support/v4/util/ArrayMap;
    //   4: ifnull -> 406
    //   7: aload_0
    //   8: getfield mDelegates : Landroid/support/v4/util/ArrayMap;
    //   11: invokevirtual isEmpty : ()Z
    //   14: ifne -> 406
    //   17: aload_0
    //   18: getfield mKnownDrawableIdTags : Landroid/util/SparseArray;
    //   21: ifnull -> 66
    //   24: aload_0
    //   25: getfield mKnownDrawableIdTags : Landroid/util/SparseArray;
    //   28: iload_2
    //   29: invokevirtual get : (I)Ljava/lang/Object;
    //   32: checkcast java/lang/String
    //   35: astore_3
    //   36: ldc 'appcompat_skip_skip'
    //   38: aload_3
    //   39: invokevirtual equals : (Ljava/lang/Object;)Z
    //   42: ifne -> 60
    //   45: aload_3
    //   46: ifnull -> 77
    //   49: aload_0
    //   50: getfield mDelegates : Landroid/support/v4/util/ArrayMap;
    //   53: aload_3
    //   54: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   57: ifnonnull -> 77
    //   60: aconst_null
    //   61: astore #4
    //   63: aload #4
    //   65: areturn
    //   66: aload_0
    //   67: new android/util/SparseArray
    //   70: dup
    //   71: invokespecial <init> : ()V
    //   74: putfield mKnownDrawableIdTags : Landroid/util/SparseArray;
    //   77: aload_0
    //   78: getfield mTypedValue : Landroid/util/TypedValue;
    //   81: ifnonnull -> 95
    //   84: aload_0
    //   85: new android/util/TypedValue
    //   88: dup
    //   89: invokespecial <init> : ()V
    //   92: putfield mTypedValue : Landroid/util/TypedValue;
    //   95: aload_0
    //   96: getfield mTypedValue : Landroid/util/TypedValue;
    //   99: astore #5
    //   101: aload_1
    //   102: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   105: astore #6
    //   107: aload #6
    //   109: iload_2
    //   110: aload #5
    //   112: iconst_1
    //   113: invokevirtual getValue : (ILandroid/util/TypedValue;Z)V
    //   116: aload #5
    //   118: invokestatic createCacheKey : (Landroid/util/TypedValue;)J
    //   121: lstore #7
    //   123: aload_0
    //   124: aload_1
    //   125: lload #7
    //   127: invokespecial getCachedDrawable : (Landroid/content/Context;J)Landroid/graphics/drawable/Drawable;
    //   130: astore_3
    //   131: aload_3
    //   132: astore #4
    //   134: aload_3
    //   135: ifnonnull -> 63
    //   138: aload_3
    //   139: astore #9
    //   141: aload #5
    //   143: getfield string : Ljava/lang/CharSequence;
    //   146: ifnull -> 255
    //   149: aload_3
    //   150: astore #9
    //   152: aload #5
    //   154: getfield string : Ljava/lang/CharSequence;
    //   157: invokeinterface toString : ()Ljava/lang/String;
    //   162: ldc_w '.xml'
    //   165: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   168: ifeq -> 255
    //   171: aload_3
    //   172: astore #9
    //   174: aload #6
    //   176: iload_2
    //   177: invokevirtual getXml : (I)Landroid/content/res/XmlResourceParser;
    //   180: astore #6
    //   182: aload_3
    //   183: astore #9
    //   185: aload #6
    //   187: invokestatic asAttributeSet : (Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   190: astore #10
    //   192: aload_3
    //   193: astore #9
    //   195: aload #6
    //   197: invokeinterface next : ()I
    //   202: istore #11
    //   204: iload #11
    //   206: iconst_2
    //   207: if_icmpeq -> 216
    //   210: iload #11
    //   212: iconst_1
    //   213: if_icmpne -> 192
    //   216: iload #11
    //   218: iconst_2
    //   219: if_icmpeq -> 281
    //   222: aload_3
    //   223: astore #9
    //   225: new org/xmlpull/v1/XmlPullParserException
    //   228: astore_1
    //   229: aload_3
    //   230: astore #9
    //   232: aload_1
    //   233: ldc_w 'No start tag found'
    //   236: invokespecial <init> : (Ljava/lang/String;)V
    //   239: aload_3
    //   240: astore #9
    //   242: aload_1
    //   243: athrow
    //   244: astore_1
    //   245: ldc 'AppCompatDrawableManager'
    //   247: ldc_w 'Exception while inflating drawable'
    //   250: aload_1
    //   251: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   254: pop
    //   255: aload #9
    //   257: astore #4
    //   259: aload #9
    //   261: ifnonnull -> 63
    //   264: aload_0
    //   265: getfield mKnownDrawableIdTags : Landroid/util/SparseArray;
    //   268: iload_2
    //   269: ldc 'appcompat_skip_skip'
    //   271: invokevirtual append : (ILjava/lang/Object;)V
    //   274: aload #9
    //   276: astore #4
    //   278: goto -> 63
    //   281: aload_3
    //   282: astore #9
    //   284: aload #6
    //   286: invokeinterface getName : ()Ljava/lang/String;
    //   291: astore #4
    //   293: aload_3
    //   294: astore #9
    //   296: aload_0
    //   297: getfield mKnownDrawableIdTags : Landroid/util/SparseArray;
    //   300: iload_2
    //   301: aload #4
    //   303: invokevirtual append : (ILjava/lang/Object;)V
    //   306: aload_3
    //   307: astore #9
    //   309: aload_0
    //   310: getfield mDelegates : Landroid/support/v4/util/ArrayMap;
    //   313: aload #4
    //   315: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   318: checkcast android/support/v7/widget/AppCompatDrawableManager$InflateDelegate
    //   321: astore #12
    //   323: aload_3
    //   324: astore #4
    //   326: aload #12
    //   328: ifnull -> 352
    //   331: aload_3
    //   332: astore #9
    //   334: aload #12
    //   336: aload_1
    //   337: aload #6
    //   339: aload #10
    //   341: aload_1
    //   342: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   345: invokeinterface createFromXmlInner : (Landroid/content/Context;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;
    //   350: astore #4
    //   352: aload #4
    //   354: astore #9
    //   356: aload #4
    //   358: ifnull -> 255
    //   361: aload #4
    //   363: astore #9
    //   365: aload #4
    //   367: aload #5
    //   369: getfield changingConfigurations : I
    //   372: invokevirtual setChangingConfigurations : (I)V
    //   375: aload #4
    //   377: astore #9
    //   379: aload_0
    //   380: aload_1
    //   381: lload #7
    //   383: aload #4
    //   385: invokespecial addDrawableToCache : (Landroid/content/Context;JLandroid/graphics/drawable/Drawable;)Z
    //   388: istore #13
    //   390: aload #4
    //   392: astore #9
    //   394: iload #13
    //   396: ifeq -> 255
    //   399: aload #4
    //   401: astore #9
    //   403: goto -> 255
    //   406: aconst_null
    //   407: astore #4
    //   409: goto -> 63
    // Exception table:
    //   from	to	target	type
    //   174	182	244	java/lang/Exception
    //   185	192	244	java/lang/Exception
    //   195	204	244	java/lang/Exception
    //   225	229	244	java/lang/Exception
    //   232	239	244	java/lang/Exception
    //   242	244	244	java/lang/Exception
    //   284	293	244	java/lang/Exception
    //   296	306	244	java/lang/Exception
    //   309	323	244	java/lang/Exception
    //   334	352	244	java/lang/Exception
    //   365	375	244	java/lang/Exception
    //   379	390	244	java/lang/Exception
  }
  
  private void removeDelegate(@NonNull String paramString, @NonNull InflateDelegate paramInflateDelegate) {
    if (this.mDelegates != null && this.mDelegates.get(paramString) == paramInflateDelegate)
      this.mDelegates.remove(paramString); 
  }
  
  private static void setPorterDuffColorFilter(Drawable paramDrawable, int paramInt, PorterDuff.Mode paramMode) {
    Drawable drawable = paramDrawable;
    if (DrawableUtils.canSafelyMutateDrawable(paramDrawable))
      drawable = paramDrawable.mutate(); 
    PorterDuff.Mode mode = paramMode;
    if (paramMode == null)
      mode = DEFAULT_MODE; 
    drawable.setColorFilter((ColorFilter)getPorterDuffColorFilter(paramInt, mode));
  }
  
  private Drawable tintDrawable(@NonNull Context paramContext, @DrawableRes int paramInt, boolean paramBoolean, @NonNull Drawable paramDrawable) {
    Drawable drawable;
    PorterDuff.Mode mode1;
    ColorStateList colorStateList = getTintList(paramContext, paramInt);
    if (colorStateList != null) {
      drawable = paramDrawable;
      if (DrawableUtils.canSafelyMutateDrawable(paramDrawable))
        drawable = paramDrawable.mutate(); 
      drawable = DrawableCompat.wrap(drawable);
      DrawableCompat.setTintList(drawable, colorStateList);
      mode1 = getTintMode(paramInt);
      Drawable drawable1 = drawable;
      if (mode1 != null) {
        DrawableCompat.setTintMode(drawable, mode1);
        drawable1 = drawable;
      } 
      return drawable1;
    } 
    if (paramInt == R.drawable.abc_seekbar_track_material) {
      LayerDrawable layerDrawable = (LayerDrawable)mode1;
      setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor((Context)drawable, R.attr.colorControlNormal), DEFAULT_MODE);
      setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor((Context)drawable, R.attr.colorControlNormal), DEFAULT_MODE);
      setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor((Context)drawable, R.attr.colorControlActivated), DEFAULT_MODE);
      return (Drawable)mode1;
    } 
    if (paramInt == R.drawable.abc_ratingbar_material || paramInt == R.drawable.abc_ratingbar_indicator_material || paramInt == R.drawable.abc_ratingbar_small_material) {
      LayerDrawable layerDrawable = (LayerDrawable)mode1;
      setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor((Context)drawable, R.attr.colorControlNormal), DEFAULT_MODE);
      setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor((Context)drawable, R.attr.colorControlActivated), DEFAULT_MODE);
      setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor((Context)drawable, R.attr.colorControlActivated), DEFAULT_MODE);
      return (Drawable)mode1;
    } 
    PorterDuff.Mode mode2 = mode1;
    if (!tintDrawableUsingColorFilter((Context)drawable, paramInt, (Drawable)mode1)) {
      mode2 = mode1;
      if (paramBoolean)
        mode2 = null; 
    } 
    return (Drawable)mode2;
  }
  
  static void tintDrawable(Drawable paramDrawable, TintInfo paramTintInfo, int[] paramArrayOfint) {
    if (DrawableUtils.canSafelyMutateDrawable(paramDrawable) && paramDrawable.mutate() != paramDrawable) {
      Log.d("AppCompatDrawableManager", "Mutated drawable is not the same instance as the input.");
      return;
    } 
    if (paramTintInfo.mHasTintList || paramTintInfo.mHasTintMode) {
      PorterDuff.Mode mode;
      ColorStateList colorStateList;
      if (paramTintInfo.mHasTintList) {
        colorStateList = paramTintInfo.mTintList;
      } else {
        colorStateList = null;
      } 
      if (paramTintInfo.mHasTintMode) {
        mode = paramTintInfo.mTintMode;
      } else {
        mode = DEFAULT_MODE;
      } 
      paramDrawable.setColorFilter((ColorFilter)createTintFilter(colorStateList, mode, paramArrayOfint));
    } else {
      paramDrawable.clearColorFilter();
    } 
    if (Build.VERSION.SDK_INT <= 23)
      paramDrawable.invalidateSelf(); 
  }
  
  static boolean tintDrawableUsingColorFilter(@NonNull Context paramContext, @DrawableRes int paramInt, @NonNull Drawable paramDrawable) {
    PorterDuff.Mode mode2;
    byte b2;
    PorterDuff.Mode mode1 = DEFAULT_MODE;
    boolean bool = false;
    int i = 0;
    byte b1 = -1;
    if (arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, paramInt)) {
      i = R.attr.colorControlNormal;
      bool = true;
      mode2 = mode1;
      b2 = b1;
    } else if (arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, paramInt)) {
      i = R.attr.colorControlActivated;
      bool = true;
      b2 = b1;
      mode2 = mode1;
    } else if (arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, paramInt)) {
      i = 16842801;
      bool = true;
      mode2 = PorterDuff.Mode.MULTIPLY;
      b2 = b1;
    } else if (paramInt == R.drawable.abc_list_divider_mtrl_alpha) {
      i = 16842800;
      bool = true;
      b2 = Math.round(40.8F);
      mode2 = mode1;
    } else {
      b2 = b1;
      mode2 = mode1;
      if (paramInt == R.drawable.abc_dialog_material_background) {
        i = 16842801;
        bool = true;
        b2 = b1;
        mode2 = mode1;
      } 
    } 
    if (bool) {
      Drawable drawable = paramDrawable;
      if (DrawableUtils.canSafelyMutateDrawable(paramDrawable))
        drawable = paramDrawable.mutate(); 
      drawable.setColorFilter((ColorFilter)getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(paramContext, i), mode2));
      if (b2 != -1)
        drawable.setAlpha(b2); 
      return true;
    } 
    return false;
  }
  
  public Drawable getDrawable(@NonNull Context paramContext, @DrawableRes int paramInt) {
    return getDrawable(paramContext, paramInt, false);
  }
  
  Drawable getDrawable(@NonNull Context paramContext, @DrawableRes int paramInt, boolean paramBoolean) {
    checkVectorDrawableSetup(paramContext);
    Drawable drawable1 = loadDrawableFromDelegates(paramContext, paramInt);
    Drawable drawable2 = drawable1;
    if (drawable1 == null)
      drawable2 = createDrawableIfNeeded(paramContext, paramInt); 
    drawable1 = drawable2;
    if (drawable2 == null)
      drawable1 = ContextCompat.getDrawable(paramContext, paramInt); 
    drawable2 = drawable1;
    if (drawable1 != null)
      drawable2 = tintDrawable(paramContext, paramInt, paramBoolean, drawable1); 
    if (drawable2 != null)
      DrawableUtils.fixDrawable(drawable2); 
    return drawable2;
  }
  
  ColorStateList getTintList(@NonNull Context paramContext, @DrawableRes int paramInt) {
    ColorStateList colorStateList1 = getTintListFromCache(paramContext, paramInt);
    ColorStateList colorStateList2 = colorStateList1;
    if (colorStateList1 == null) {
      if (paramInt == R.drawable.abc_edit_text_material) {
        colorStateList1 = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_edittext);
      } else if (paramInt == R.drawable.abc_switch_track_mtrl_alpha) {
        colorStateList1 = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_switch_track);
      } else if (paramInt == R.drawable.abc_switch_thumb_material) {
        colorStateList1 = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_switch_thumb);
      } else if (paramInt == R.drawable.abc_btn_default_mtrl_shape) {
        colorStateList1 = createDefaultButtonColorStateList(paramContext);
      } else if (paramInt == R.drawable.abc_btn_borderless_material) {
        colorStateList1 = createBorderlessButtonColorStateList(paramContext);
      } else if (paramInt == R.drawable.abc_btn_colored_material) {
        colorStateList1 = createColoredButtonColorStateList(paramContext);
      } else if (paramInt == R.drawable.abc_spinner_mtrl_am_alpha || paramInt == R.drawable.abc_spinner_textfield_background_material) {
        colorStateList1 = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_spinner);
      } else if (arrayContains(TINT_COLOR_CONTROL_NORMAL, paramInt)) {
        colorStateList1 = ThemeUtils.getThemeAttrColorStateList(paramContext, R.attr.colorControlNormal);
      } else if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, paramInt)) {
        colorStateList1 = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_default);
      } else if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, paramInt)) {
        colorStateList1 = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_btn_checkable);
      } else if (paramInt == R.drawable.abc_seekbar_thumb_material) {
        colorStateList1 = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_seek_thumb);
      } 
      colorStateList2 = colorStateList1;
      if (colorStateList1 != null) {
        addTintListToCache(paramContext, paramInt, colorStateList1);
        colorStateList2 = colorStateList1;
      } 
    } 
    return colorStateList2;
  }
  
  public void onConfigurationChanged(@NonNull Context paramContext) {
    synchronized (this.mDrawableCacheLock) {
      LongSparseArray longSparseArray = this.mDrawableCaches.get(paramContext);
      if (longSparseArray != null)
        longSparseArray.clear(); 
      return;
    } 
  }
  
  Drawable onDrawableLoadedFromResources(@NonNull Context paramContext, @NonNull VectorEnabledTintResources paramVectorEnabledTintResources, @DrawableRes int paramInt) {
    Drawable drawable1 = loadDrawableFromDelegates(paramContext, paramInt);
    Drawable drawable2 = drawable1;
    if (drawable1 == null)
      drawable2 = paramVectorEnabledTintResources.superGetDrawable(paramInt); 
    return (drawable2 != null) ? tintDrawable(paramContext, paramInt, false, drawable2) : null;
  }
  
  @TargetApi(11)
  @RequiresApi(11)
  private static class AvdcInflateDelegate implements InflateDelegate {
    @SuppressLint({"NewApi"})
    public Drawable createFromXmlInner(@NonNull Context param1Context, @NonNull XmlPullParser param1XmlPullParser, @NonNull AttributeSet param1AttributeSet, @Nullable Resources.Theme param1Theme) {
      try {
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.createFromXmlInner(param1Context, param1Context.getResources(), param1XmlPullParser, param1AttributeSet, param1Theme);
      } catch (Exception exception) {
        Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", exception);
        exception = null;
      } 
      return (Drawable)exception;
    }
  }
  
  private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
    public ColorFilterLruCache(int param1Int) {
      super(param1Int);
    }
    
    private static int generateCacheKey(int param1Int, PorterDuff.Mode param1Mode) {
      return (param1Int + 31) * 31 + param1Mode.hashCode();
    }
    
    PorterDuffColorFilter get(int param1Int, PorterDuff.Mode param1Mode) {
      return (PorterDuffColorFilter)get(Integer.valueOf(generateCacheKey(param1Int, param1Mode)));
    }
    
    PorterDuffColorFilter put(int param1Int, PorterDuff.Mode param1Mode, PorterDuffColorFilter param1PorterDuffColorFilter) {
      return (PorterDuffColorFilter)put(Integer.valueOf(generateCacheKey(param1Int, param1Mode)), param1PorterDuffColorFilter);
    }
  }
  
  private static interface InflateDelegate {
    Drawable createFromXmlInner(@NonNull Context param1Context, @NonNull XmlPullParser param1XmlPullParser, @NonNull AttributeSet param1AttributeSet, @Nullable Resources.Theme param1Theme);
  }
  
  private static class VdcInflateDelegate implements InflateDelegate {
    @SuppressLint({"NewApi"})
    public Drawable createFromXmlInner(@NonNull Context param1Context, @NonNull XmlPullParser param1XmlPullParser, @NonNull AttributeSet param1AttributeSet, @Nullable Resources.Theme param1Theme) {
      try {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.createFromXmlInner(param1Context.getResources(), param1XmlPullParser, param1AttributeSet, param1Theme);
      } catch (Exception exception) {
        Log.e("VdcInflateDelegate", "Exception while inflating <vector>", exception);
        exception = null;
      } 
      return (Drawable)exception;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/AppCompatDrawableManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */