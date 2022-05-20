package android.support.v7.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

@TargetApi(9)
@RequiresApi(9)
class AppCompatDelegateImplV9 extends AppCompatDelegateImplBase implements MenuBuilder.Callback, LayoutInflaterFactory {
  private ActionMenuPresenterCallback mActionMenuPresenterCallback;
  
  ActionMode mActionMode;
  
  PopupWindow mActionModePopup;
  
  ActionBarContextView mActionModeView;
  
  private AppCompatViewInflater mAppCompatViewInflater;
  
  private boolean mClosingActionMenu;
  
  private DecorContentParent mDecorContentParent;
  
  private boolean mEnableDefaultActionBarUp;
  
  ViewPropertyAnimatorCompat mFadeAnim = null;
  
  private boolean mFeatureIndeterminateProgress;
  
  private boolean mFeatureProgress;
  
  int mInvalidatePanelMenuFeatures;
  
  boolean mInvalidatePanelMenuPosted;
  
  private final Runnable mInvalidatePanelMenuRunnable = new Runnable() {
      public void run() {
        if ((AppCompatDelegateImplV9.this.mInvalidatePanelMenuFeatures & 0x1) != 0)
          AppCompatDelegateImplV9.this.doInvalidatePanelMenu(0); 
        if ((AppCompatDelegateImplV9.this.mInvalidatePanelMenuFeatures & 0x1000) != 0)
          AppCompatDelegateImplV9.this.doInvalidatePanelMenu(108); 
        AppCompatDelegateImplV9.this.mInvalidatePanelMenuPosted = false;
        AppCompatDelegateImplV9.this.mInvalidatePanelMenuFeatures = 0;
      }
    };
  
  private boolean mLongPressBackDown;
  
  private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
  
  private PanelFeatureState[] mPanels;
  
  private PanelFeatureState mPreparedPanel;
  
  Runnable mShowActionModePopup;
  
  private View mStatusGuard;
  
  private ViewGroup mSubDecor;
  
  private boolean mSubDecorInstalled;
  
  private Rect mTempRect1;
  
  private Rect mTempRect2;
  
  private TextView mTitleView;
  
  AppCompatDelegateImplV9(Context paramContext, Window paramWindow, AppCompatCallback paramAppCompatCallback) {
    super(paramContext, paramWindow, paramAppCompatCallback);
  }
  
  private void applyFixedSizeWindow() {
    ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
    View view = this.mWindow.getDecorView();
    contentFrameLayout.setDecorPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    TypedArray typedArray = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
    typedArray.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
    typedArray.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor()); 
    typedArray.recycle();
    contentFrameLayout.requestLayout();
  }
  
  private ViewGroup createSubDecor() {
    ViewGroup viewGroup1;
    ViewGroup viewGroup2;
    TypedArray typedArray = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
    if (!typedArray.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
      typedArray.recycle();
      throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
    } 
    if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
      requestWindowFeature(1);
    } else if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
      requestWindowFeature(108);
    } 
    if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false))
      requestWindowFeature(109); 
    if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false))
      requestWindowFeature(10); 
    this.mIsFloating = typedArray.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
    typedArray.recycle();
    this.mWindow.getDecorView();
    LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
    typedArray = null;
    if (!this.mWindowNoTitle) {
      if (this.mIsFloating) {
        viewGroup1 = (ViewGroup)layoutInflater.inflate(R.layout.abc_dialog_title_material, null);
        this.mOverlayActionBar = false;
        this.mHasActionBar = false;
      } else if (this.mHasActionBar) {
        Context context;
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, typedValue, true);
        if (typedValue.resourceId != 0) {
          ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.mContext, typedValue.resourceId);
        } else {
          context = this.mContext;
        } 
        viewGroup2 = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.abc_screen_toolbar, null);
        this.mDecorContentParent = (DecorContentParent)viewGroup2.findViewById(R.id.decor_content_parent);
        this.mDecorContentParent.setWindowCallback(getWindowCallback());
        if (this.mOverlayActionBar)
          this.mDecorContentParent.initFeature(109); 
        if (this.mFeatureProgress)
          this.mDecorContentParent.initFeature(2); 
        viewGroup1 = viewGroup2;
        if (this.mFeatureIndeterminateProgress) {
          this.mDecorContentParent.initFeature(5);
          viewGroup1 = viewGroup2;
        } 
      } 
    } else {
      if (this.mOverlayActionMode) {
        viewGroup1 = (ViewGroup)viewGroup2.inflate(R.layout.abc_screen_simple_overlay_action_mode, null);
      } else {
        viewGroup1 = (ViewGroup)viewGroup2.inflate(R.layout.abc_screen_simple, null);
      } 
      if (Build.VERSION.SDK_INT >= 21) {
        ViewCompat.setOnApplyWindowInsetsListener((View)viewGroup1, new OnApplyWindowInsetsListener() {
              public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
                int i = param1WindowInsetsCompat.getSystemWindowInsetTop();
                int j = AppCompatDelegateImplV9.this.updateStatusGuard(i);
                WindowInsetsCompat windowInsetsCompat = param1WindowInsetsCompat;
                if (i != j)
                  windowInsetsCompat = param1WindowInsetsCompat.replaceSystemWindowInsets(param1WindowInsetsCompat.getSystemWindowInsetLeft(), j, param1WindowInsetsCompat.getSystemWindowInsetRight(), param1WindowInsetsCompat.getSystemWindowInsetBottom()); 
                return ViewCompat.onApplyWindowInsets(param1View, windowInsetsCompat);
              }
            });
      } else {
        ((FitWindowsViewGroup)viewGroup1).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() {
              public void onFitSystemWindows(Rect param1Rect) {
                param1Rect.top = AppCompatDelegateImplV9.this.updateStatusGuard(param1Rect.top);
              }
            });
      } 
    } 
    if (viewGroup1 == null)
      throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.mHasActionBar + ", windowActionBarOverlay: " + this.mOverlayActionBar + ", android:windowIsFloating: " + this.mIsFloating + ", windowActionModeOverlay: " + this.mOverlayActionMode + ", windowNoTitle: " + this.mWindowNoTitle + " }"); 
    if (this.mDecorContentParent == null)
      this.mTitleView = (TextView)viewGroup1.findViewById(R.id.title); 
    ViewUtils.makeOptionalFitsSystemWindows((View)viewGroup1);
    ContentFrameLayout contentFrameLayout = (ContentFrameLayout)viewGroup1.findViewById(R.id.action_bar_activity_content);
    ViewGroup viewGroup3 = (ViewGroup)this.mWindow.findViewById(16908290);
    if (viewGroup3 != null) {
      while (viewGroup3.getChildCount() > 0) {
        View view = viewGroup3.getChildAt(0);
        viewGroup3.removeViewAt(0);
        contentFrameLayout.addView(view);
      } 
      viewGroup3.setId(-1);
      contentFrameLayout.setId(16908290);
      if (viewGroup3 instanceof FrameLayout)
        ((FrameLayout)viewGroup3).setForeground(null); 
    } 
    this.mWindow.setContentView((View)viewGroup1);
    contentFrameLayout.setAttachListener(new ContentFrameLayout.OnAttachListener() {
          public void onAttachedFromWindow() {}
          
          public void onDetachedFromWindow() {
            AppCompatDelegateImplV9.this.dismissPopups();
          }
        });
    return viewGroup1;
  }
  
  private void ensureSubDecor() {
    if (!this.mSubDecorInstalled) {
      this.mSubDecor = createSubDecor();
      CharSequence charSequence = getTitle();
      if (!TextUtils.isEmpty(charSequence))
        onTitleChanged(charSequence); 
      applyFixedSizeWindow();
      onSubDecorInstalled(this.mSubDecor);
      this.mSubDecorInstalled = true;
      PanelFeatureState panelFeatureState = getPanelState(0, false);
      if (!isDestroyed() && (panelFeatureState == null || panelFeatureState.menu == null))
        invalidatePanelMenu(108); 
    } 
  }
  
  private boolean initializePanelContent(PanelFeatureState paramPanelFeatureState) {
    boolean bool = true;
    if (paramPanelFeatureState.createdPanelView != null) {
      paramPanelFeatureState.shownPanelView = paramPanelFeatureState.createdPanelView;
      return bool;
    } 
    if (paramPanelFeatureState.menu == null)
      return false; 
    if (this.mPanelMenuPresenterCallback == null)
      this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback(); 
    paramPanelFeatureState.shownPanelView = (View)paramPanelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
    if (paramPanelFeatureState.shownPanelView == null)
      bool = false; 
    return bool;
  }
  
  private boolean initializePanelDecor(PanelFeatureState paramPanelFeatureState) {
    paramPanelFeatureState.setStyle(getActionBarThemedContext());
    paramPanelFeatureState.decorView = (ViewGroup)new ListMenuDecorView(paramPanelFeatureState.listPresenterContext);
    paramPanelFeatureState.gravity = 81;
    return true;
  }
  
  private boolean initializePanelMenu(PanelFeatureState paramPanelFeatureState) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mContext : Landroid/content/Context;
    //   4: astore_2
    //   5: aload_1
    //   6: getfield featureId : I
    //   9: ifeq -> 23
    //   12: aload_2
    //   13: astore_3
    //   14: aload_1
    //   15: getfield featureId : I
    //   18: bipush #108
    //   20: if_icmpne -> 175
    //   23: aload_2
    //   24: astore_3
    //   25: aload_0
    //   26: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   29: ifnull -> 175
    //   32: new android/util/TypedValue
    //   35: dup
    //   36: invokespecial <init> : ()V
    //   39: astore #4
    //   41: aload_2
    //   42: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   45: astore #5
    //   47: aload #5
    //   49: getstatic android/support/v7/appcompat/R$attr.actionBarTheme : I
    //   52: aload #4
    //   54: iconst_1
    //   55: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   58: pop
    //   59: aconst_null
    //   60: astore_3
    //   61: aload #4
    //   63: getfield resourceId : I
    //   66: ifeq -> 196
    //   69: aload_2
    //   70: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   73: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   76: astore_3
    //   77: aload_3
    //   78: aload #5
    //   80: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   83: aload_3
    //   84: aload #4
    //   86: getfield resourceId : I
    //   89: iconst_1
    //   90: invokevirtual applyStyle : (IZ)V
    //   93: aload_3
    //   94: getstatic android/support/v7/appcompat/R$attr.actionBarWidgetTheme : I
    //   97: aload #4
    //   99: iconst_1
    //   100: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   103: pop
    //   104: aload_3
    //   105: astore #6
    //   107: aload #4
    //   109: getfield resourceId : I
    //   112: ifeq -> 149
    //   115: aload_3
    //   116: astore #6
    //   118: aload_3
    //   119: ifnonnull -> 138
    //   122: aload_2
    //   123: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   126: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   129: astore #6
    //   131: aload #6
    //   133: aload #5
    //   135: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   138: aload #6
    //   140: aload #4
    //   142: getfield resourceId : I
    //   145: iconst_1
    //   146: invokevirtual applyStyle : (IZ)V
    //   149: aload_2
    //   150: astore_3
    //   151: aload #6
    //   153: ifnull -> 175
    //   156: new android/support/v7/view/ContextThemeWrapper
    //   159: dup
    //   160: aload_2
    //   161: iconst_0
    //   162: invokespecial <init> : (Landroid/content/Context;I)V
    //   165: astore_3
    //   166: aload_3
    //   167: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   170: aload #6
    //   172: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   175: new android/support/v7/view/menu/MenuBuilder
    //   178: dup
    //   179: aload_3
    //   180: invokespecial <init> : (Landroid/content/Context;)V
    //   183: astore_3
    //   184: aload_3
    //   185: aload_0
    //   186: invokevirtual setCallback : (Landroid/support/v7/view/menu/MenuBuilder$Callback;)V
    //   189: aload_1
    //   190: aload_3
    //   191: invokevirtual setMenu : (Landroid/support/v7/view/menu/MenuBuilder;)V
    //   194: iconst_1
    //   195: ireturn
    //   196: aload #5
    //   198: getstatic android/support/v7/appcompat/R$attr.actionBarWidgetTheme : I
    //   201: aload #4
    //   203: iconst_1
    //   204: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   207: pop
    //   208: goto -> 104
  }
  
  private void invalidatePanelMenu(int paramInt) {
    this.mInvalidatePanelMenuFeatures |= 1 << paramInt;
    if (!this.mInvalidatePanelMenuPosted) {
      ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
      this.mInvalidatePanelMenuPosted = true;
    } 
  }
  
  private boolean onKeyDownPanel(int paramInt, KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getRepeatCount() == 0) {
      PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
      if (!panelFeatureState.isOpen)
        return preparePanel(panelFeatureState, paramKeyEvent); 
    } 
    return false;
  }
  
  private boolean onKeyUpPanel(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool3;
    if (this.mActionMode != null)
      return false; 
    boolean bool2 = false;
    PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
    if (paramInt == 0 && this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && !ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext))) {
      if (!this.mDecorContentParent.isOverflowMenuShowing()) {
        bool3 = bool2;
        if (!isDestroyed()) {
          bool3 = bool2;
          if (preparePanel(panelFeatureState, paramKeyEvent))
            bool3 = this.mDecorContentParent.showOverflowMenu(); 
        } 
      } else {
        bool3 = this.mDecorContentParent.hideOverflowMenu();
      } 
    } else if (panelFeatureState.isOpen || panelFeatureState.isHandled) {
      bool3 = panelFeatureState.isOpen;
      closePanel(panelFeatureState, true);
    } else {
      bool3 = bool2;
      if (panelFeatureState.isPrepared) {
        boolean bool = true;
        if (panelFeatureState.refreshMenuContent) {
          panelFeatureState.isPrepared = false;
          bool = preparePanel(panelFeatureState, paramKeyEvent);
        } 
        bool3 = bool2;
        if (bool) {
          openPanel(panelFeatureState, paramKeyEvent);
          bool3 = true;
        } 
      } 
    } 
    boolean bool1 = bool3;
    if (bool3) {
      AudioManager audioManager = (AudioManager)this.mContext.getSystemService("audio");
      if (audioManager != null) {
        audioManager.playSoundEffect(0);
        return bool3;
      } 
      Log.w("AppCompatDelegate", "Couldn't get audio manager");
      bool1 = bool3;
    } 
    return bool1;
  }
  
  private void openPanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent) {
    // Byte code:
    //   0: aload_1
    //   1: getfield isOpen : Z
    //   4: ifne -> 14
    //   7: aload_0
    //   8: invokevirtual isDestroyed : ()Z
    //   11: ifeq -> 15
    //   14: return
    //   15: aload_1
    //   16: getfield featureId : I
    //   19: ifne -> 72
    //   22: aload_0
    //   23: getfield mContext : Landroid/content/Context;
    //   26: astore_3
    //   27: aload_3
    //   28: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   31: invokevirtual getConfiguration : ()Landroid/content/res/Configuration;
    //   34: getfield screenLayout : I
    //   37: bipush #15
    //   39: iand
    //   40: iconst_4
    //   41: if_icmpne -> 107
    //   44: iconst_1
    //   45: istore #4
    //   47: aload_3
    //   48: invokevirtual getApplicationInfo : ()Landroid/content/pm/ApplicationInfo;
    //   51: getfield targetSdkVersion : I
    //   54: bipush #11
    //   56: if_icmplt -> 113
    //   59: iconst_1
    //   60: istore #5
    //   62: iload #4
    //   64: ifeq -> 72
    //   67: iload #5
    //   69: ifne -> 14
    //   72: aload_0
    //   73: invokevirtual getWindowCallback : ()Landroid/view/Window$Callback;
    //   76: astore_3
    //   77: aload_3
    //   78: ifnull -> 119
    //   81: aload_3
    //   82: aload_1
    //   83: getfield featureId : I
    //   86: aload_1
    //   87: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   90: invokeinterface onMenuOpened : (ILandroid/view/Menu;)Z
    //   95: ifne -> 119
    //   98: aload_0
    //   99: aload_1
    //   100: iconst_1
    //   101: invokevirtual closePanel : (Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;Z)V
    //   104: goto -> 14
    //   107: iconst_0
    //   108: istore #4
    //   110: goto -> 47
    //   113: iconst_0
    //   114: istore #5
    //   116: goto -> 62
    //   119: aload_0
    //   120: getfield mContext : Landroid/content/Context;
    //   123: ldc_w 'window'
    //   126: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   129: checkcast android/view/WindowManager
    //   132: astore #6
    //   134: aload #6
    //   136: ifnull -> 14
    //   139: aload_0
    //   140: aload_1
    //   141: aload_2
    //   142: invokespecial preparePanel : (Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   145: ifeq -> 14
    //   148: bipush #-2
    //   150: istore #5
    //   152: aload_1
    //   153: getfield decorView : Landroid/view/ViewGroup;
    //   156: ifnull -> 166
    //   159: aload_1
    //   160: getfield refreshDecorView : Z
    //   163: ifeq -> 408
    //   166: aload_1
    //   167: getfield decorView : Landroid/view/ViewGroup;
    //   170: ifnonnull -> 381
    //   173: aload_0
    //   174: aload_1
    //   175: invokespecial initializePanelDecor : (Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;)Z
    //   178: ifeq -> 14
    //   181: aload_1
    //   182: getfield decorView : Landroid/view/ViewGroup;
    //   185: ifnull -> 14
    //   188: aload_0
    //   189: aload_1
    //   190: invokespecial initializePanelContent : (Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;)Z
    //   193: ifeq -> 14
    //   196: aload_1
    //   197: invokevirtual hasPanelItems : ()Z
    //   200: ifeq -> 14
    //   203: aload_1
    //   204: getfield shownPanelView : Landroid/view/View;
    //   207: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   210: astore_3
    //   211: aload_3
    //   212: astore_2
    //   213: aload_3
    //   214: ifnonnull -> 229
    //   217: new android/view/ViewGroup$LayoutParams
    //   220: dup
    //   221: bipush #-2
    //   223: bipush #-2
    //   225: invokespecial <init> : (II)V
    //   228: astore_2
    //   229: aload_1
    //   230: getfield background : I
    //   233: istore #4
    //   235: aload_1
    //   236: getfield decorView : Landroid/view/ViewGroup;
    //   239: iload #4
    //   241: invokevirtual setBackgroundResource : (I)V
    //   244: aload_1
    //   245: getfield shownPanelView : Landroid/view/View;
    //   248: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   251: astore_3
    //   252: aload_3
    //   253: ifnull -> 274
    //   256: aload_3
    //   257: instanceof android/view/ViewGroup
    //   260: ifeq -> 274
    //   263: aload_3
    //   264: checkcast android/view/ViewGroup
    //   267: aload_1
    //   268: getfield shownPanelView : Landroid/view/View;
    //   271: invokevirtual removeView : (Landroid/view/View;)V
    //   274: aload_1
    //   275: getfield decorView : Landroid/view/ViewGroup;
    //   278: aload_1
    //   279: getfield shownPanelView : Landroid/view/View;
    //   282: aload_2
    //   283: invokevirtual addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   286: iload #5
    //   288: istore #4
    //   290: aload_1
    //   291: getfield shownPanelView : Landroid/view/View;
    //   294: invokevirtual hasFocus : ()Z
    //   297: ifne -> 312
    //   300: aload_1
    //   301: getfield shownPanelView : Landroid/view/View;
    //   304: invokevirtual requestFocus : ()Z
    //   307: pop
    //   308: iload #5
    //   310: istore #4
    //   312: aload_1
    //   313: iconst_0
    //   314: putfield isHandled : Z
    //   317: new android/view/WindowManager$LayoutParams
    //   320: dup
    //   321: iload #4
    //   323: bipush #-2
    //   325: aload_1
    //   326: getfield x : I
    //   329: aload_1
    //   330: getfield y : I
    //   333: sipush #1002
    //   336: ldc_w 8519680
    //   339: bipush #-3
    //   341: invokespecial <init> : (IIIIIII)V
    //   344: astore_2
    //   345: aload_2
    //   346: aload_1
    //   347: getfield gravity : I
    //   350: putfield gravity : I
    //   353: aload_2
    //   354: aload_1
    //   355: getfield windowAnimations : I
    //   358: putfield windowAnimations : I
    //   361: aload #6
    //   363: aload_1
    //   364: getfield decorView : Landroid/view/ViewGroup;
    //   367: aload_2
    //   368: invokeinterface addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   373: aload_1
    //   374: iconst_1
    //   375: putfield isOpen : Z
    //   378: goto -> 14
    //   381: aload_1
    //   382: getfield refreshDecorView : Z
    //   385: ifeq -> 188
    //   388: aload_1
    //   389: getfield decorView : Landroid/view/ViewGroup;
    //   392: invokevirtual getChildCount : ()I
    //   395: ifle -> 188
    //   398: aload_1
    //   399: getfield decorView : Landroid/view/ViewGroup;
    //   402: invokevirtual removeAllViews : ()V
    //   405: goto -> 188
    //   408: iload #5
    //   410: istore #4
    //   412: aload_1
    //   413: getfield createdPanelView : Landroid/view/View;
    //   416: ifnull -> 312
    //   419: aload_1
    //   420: getfield createdPanelView : Landroid/view/View;
    //   423: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   426: astore_2
    //   427: iload #5
    //   429: istore #4
    //   431: aload_2
    //   432: ifnull -> 312
    //   435: iload #5
    //   437: istore #4
    //   439: aload_2
    //   440: getfield width : I
    //   443: iconst_m1
    //   444: if_icmpne -> 312
    //   447: iconst_m1
    //   448: istore #4
    //   450: goto -> 312
  }
  
  private boolean performPanelShortcut(PanelFeatureState paramPanelFeatureState, int paramInt1, KeyEvent paramKeyEvent, int paramInt2) {
    // Byte code:
    //   0: aload_3
    //   1: invokevirtual isSystem : ()Z
    //   4: ifeq -> 13
    //   7: iconst_0
    //   8: istore #5
    //   10: iload #5
    //   12: ireturn
    //   13: iconst_0
    //   14: istore #5
    //   16: aload_1
    //   17: getfield isPrepared : Z
    //   20: ifne -> 36
    //   23: iload #5
    //   25: istore #6
    //   27: aload_0
    //   28: aload_1
    //   29: aload_3
    //   30: invokespecial preparePanel : (Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   33: ifeq -> 60
    //   36: iload #5
    //   38: istore #6
    //   40: aload_1
    //   41: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   44: ifnull -> 60
    //   47: aload_1
    //   48: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   51: iload_2
    //   52: aload_3
    //   53: iload #4
    //   55: invokevirtual performShortcut : (ILandroid/view/KeyEvent;I)Z
    //   58: istore #6
    //   60: iload #6
    //   62: istore #5
    //   64: iload #6
    //   66: ifeq -> 10
    //   69: iload #6
    //   71: istore #5
    //   73: iload #4
    //   75: iconst_1
    //   76: iand
    //   77: ifne -> 10
    //   80: iload #6
    //   82: istore #5
    //   84: aload_0
    //   85: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   88: ifnonnull -> 10
    //   91: aload_0
    //   92: aload_1
    //   93: iconst_1
    //   94: invokevirtual closePanel : (Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;Z)V
    //   97: iload #6
    //   99: istore #5
    //   101: goto -> 10
  }
  
  private boolean preparePanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_0
    //   3: invokevirtual isDestroyed : ()Z
    //   6: ifeq -> 15
    //   9: iload_3
    //   10: istore #4
    //   12: iload #4
    //   14: ireturn
    //   15: aload_1
    //   16: getfield isPrepared : Z
    //   19: ifeq -> 28
    //   22: iconst_1
    //   23: istore #4
    //   25: goto -> 12
    //   28: aload_0
    //   29: getfield mPreparedPanel : Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;
    //   32: ifnull -> 52
    //   35: aload_0
    //   36: getfield mPreparedPanel : Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;
    //   39: aload_1
    //   40: if_acmpeq -> 52
    //   43: aload_0
    //   44: aload_0
    //   45: getfield mPreparedPanel : Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;
    //   48: iconst_0
    //   49: invokevirtual closePanel : (Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;Z)V
    //   52: aload_0
    //   53: invokevirtual getWindowCallback : ()Landroid/view/Window$Callback;
    //   56: astore #5
    //   58: aload #5
    //   60: ifnull -> 78
    //   63: aload_1
    //   64: aload #5
    //   66: aload_1
    //   67: getfield featureId : I
    //   70: invokeinterface onCreatePanelView : (I)Landroid/view/View;
    //   75: putfield createdPanelView : Landroid/view/View;
    //   78: aload_1
    //   79: getfield featureId : I
    //   82: ifeq -> 94
    //   85: aload_1
    //   86: getfield featureId : I
    //   89: bipush #108
    //   91: if_icmpne -> 298
    //   94: iconst_1
    //   95: istore #6
    //   97: iload #6
    //   99: ifeq -> 118
    //   102: aload_0
    //   103: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   106: ifnull -> 118
    //   109: aload_0
    //   110: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   113: invokeinterface setMenuPrepared : ()V
    //   118: aload_1
    //   119: getfield createdPanelView : Landroid/view/View;
    //   122: ifnonnull -> 446
    //   125: iload #6
    //   127: ifeq -> 140
    //   130: aload_0
    //   131: invokevirtual peekSupportActionBar : ()Landroid/support/v7/app/ActionBar;
    //   134: instanceof android/support/v7/app/ToolbarActionBar
    //   137: ifne -> 446
    //   140: aload_1
    //   141: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   144: ifnull -> 154
    //   147: aload_1
    //   148: getfield refreshMenuContent : Z
    //   151: ifeq -> 309
    //   154: aload_1
    //   155: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   158: ifnonnull -> 182
    //   161: iload_3
    //   162: istore #4
    //   164: aload_0
    //   165: aload_1
    //   166: invokespecial initializePanelMenu : (Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;)Z
    //   169: ifeq -> 12
    //   172: iload_3
    //   173: istore #4
    //   175: aload_1
    //   176: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   179: ifnull -> 12
    //   182: iload #6
    //   184: ifeq -> 230
    //   187: aload_0
    //   188: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   191: ifnull -> 230
    //   194: aload_0
    //   195: getfield mActionMenuPresenterCallback : Landroid/support/v7/app/AppCompatDelegateImplV9$ActionMenuPresenterCallback;
    //   198: ifnonnull -> 213
    //   201: aload_0
    //   202: new android/support/v7/app/AppCompatDelegateImplV9$ActionMenuPresenterCallback
    //   205: dup
    //   206: aload_0
    //   207: invokespecial <init> : (Landroid/support/v7/app/AppCompatDelegateImplV9;)V
    //   210: putfield mActionMenuPresenterCallback : Landroid/support/v7/app/AppCompatDelegateImplV9$ActionMenuPresenterCallback;
    //   213: aload_0
    //   214: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   217: aload_1
    //   218: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   221: aload_0
    //   222: getfield mActionMenuPresenterCallback : Landroid/support/v7/app/AppCompatDelegateImplV9$ActionMenuPresenterCallback;
    //   225: invokeinterface setMenu : (Landroid/view/Menu;Landroid/support/v7/view/menu/MenuPresenter$Callback;)V
    //   230: aload_1
    //   231: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   234: invokevirtual stopDispatchingItemsChanged : ()V
    //   237: aload #5
    //   239: aload_1
    //   240: getfield featureId : I
    //   243: aload_1
    //   244: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   247: invokeinterface onCreatePanelMenu : (ILandroid/view/Menu;)Z
    //   252: ifne -> 304
    //   255: aload_1
    //   256: aconst_null
    //   257: invokevirtual setMenu : (Landroid/support/v7/view/menu/MenuBuilder;)V
    //   260: iload_3
    //   261: istore #4
    //   263: iload #6
    //   265: ifeq -> 12
    //   268: iload_3
    //   269: istore #4
    //   271: aload_0
    //   272: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   275: ifnull -> 12
    //   278: aload_0
    //   279: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   282: aconst_null
    //   283: aload_0
    //   284: getfield mActionMenuPresenterCallback : Landroid/support/v7/app/AppCompatDelegateImplV9$ActionMenuPresenterCallback;
    //   287: invokeinterface setMenu : (Landroid/view/Menu;Landroid/support/v7/view/menu/MenuPresenter$Callback;)V
    //   292: iload_3
    //   293: istore #4
    //   295: goto -> 12
    //   298: iconst_0
    //   299: istore #6
    //   301: goto -> 97
    //   304: aload_1
    //   305: iconst_0
    //   306: putfield refreshMenuContent : Z
    //   309: aload_1
    //   310: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   313: invokevirtual stopDispatchingItemsChanged : ()V
    //   316: aload_1
    //   317: getfield frozenActionViewState : Landroid/os/Bundle;
    //   320: ifnull -> 339
    //   323: aload_1
    //   324: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   327: aload_1
    //   328: getfield frozenActionViewState : Landroid/os/Bundle;
    //   331: invokevirtual restoreActionViewStates : (Landroid/os/Bundle;)V
    //   334: aload_1
    //   335: aconst_null
    //   336: putfield frozenActionViewState : Landroid/os/Bundle;
    //   339: aload #5
    //   341: iconst_0
    //   342: aload_1
    //   343: getfield createdPanelView : Landroid/view/View;
    //   346: aload_1
    //   347: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   350: invokeinterface onPreparePanel : (ILandroid/view/View;Landroid/view/Menu;)Z
    //   355: ifne -> 397
    //   358: iload #6
    //   360: ifeq -> 384
    //   363: aload_0
    //   364: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   367: ifnull -> 384
    //   370: aload_0
    //   371: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   374: aconst_null
    //   375: aload_0
    //   376: getfield mActionMenuPresenterCallback : Landroid/support/v7/app/AppCompatDelegateImplV9$ActionMenuPresenterCallback;
    //   379: invokeinterface setMenu : (Landroid/view/Menu;Landroid/support/v7/view/menu/MenuPresenter$Callback;)V
    //   384: aload_1
    //   385: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   388: invokevirtual startDispatchingItemsChanged : ()V
    //   391: iload_3
    //   392: istore #4
    //   394: goto -> 12
    //   397: aload_2
    //   398: ifnull -> 467
    //   401: aload_2
    //   402: invokevirtual getDeviceId : ()I
    //   405: istore #6
    //   407: iload #6
    //   409: invokestatic load : (I)Landroid/view/KeyCharacterMap;
    //   412: invokevirtual getKeyboardType : ()I
    //   415: iconst_1
    //   416: if_icmpeq -> 473
    //   419: iconst_1
    //   420: istore #4
    //   422: aload_1
    //   423: iload #4
    //   425: putfield qwertyMode : Z
    //   428: aload_1
    //   429: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   432: aload_1
    //   433: getfield qwertyMode : Z
    //   436: invokevirtual setQwertyMode : (Z)V
    //   439: aload_1
    //   440: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   443: invokevirtual startDispatchingItemsChanged : ()V
    //   446: aload_1
    //   447: iconst_1
    //   448: putfield isPrepared : Z
    //   451: aload_1
    //   452: iconst_0
    //   453: putfield isHandled : Z
    //   456: aload_0
    //   457: aload_1
    //   458: putfield mPreparedPanel : Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;
    //   461: iconst_1
    //   462: istore #4
    //   464: goto -> 12
    //   467: iconst_m1
    //   468: istore #6
    //   470: goto -> 407
    //   473: iconst_0
    //   474: istore #4
    //   476: goto -> 422
  }
  
  private void reopenMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    if (this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && (!ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext)) || this.mDecorContentParent.isOverflowMenuShowPending())) {
      Window.Callback callback = getWindowCallback();
      if (!this.mDecorContentParent.isOverflowMenuShowing() || !paramBoolean) {
        if (callback != null && !isDestroyed()) {
          if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 0x1) != 0) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuRunnable.run();
          } 
          PanelFeatureState panelFeatureState1 = getPanelState(0, true);
          if (panelFeatureState1.menu != null && !panelFeatureState1.refreshMenuContent && callback.onPreparePanel(0, panelFeatureState1.createdPanelView, (Menu)panelFeatureState1.menu)) {
            callback.onMenuOpened(108, (Menu)panelFeatureState1.menu);
            this.mDecorContentParent.showOverflowMenu();
          } 
        } 
        return;
      } 
      this.mDecorContentParent.hideOverflowMenu();
      if (!isDestroyed())
        callback.onPanelClosed(108, (Menu)(getPanelState(0, true)).menu); 
      return;
    } 
    PanelFeatureState panelFeatureState = getPanelState(0, true);
    panelFeatureState.refreshDecorView = true;
    closePanel(panelFeatureState, false);
    openPanel(panelFeatureState, (KeyEvent)null);
  }
  
  private int sanitizeWindowFeatureId(int paramInt) {
    if (paramInt == 8) {
      Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
      return 108;
    } 
    int i = paramInt;
    if (paramInt == 9) {
      Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
      i = 109;
    } 
    return i;
  }
  
  private boolean shouldInheritContext(ViewParent paramViewParent) {
    if (paramViewParent == null)
      return false; 
    View view = this.mWindow.getDecorView();
    while (true) {
      if (paramViewParent == null)
        return true; 
      if (paramViewParent == view || !(paramViewParent instanceof View) || ViewCompat.isAttachedToWindow((View)paramViewParent))
        return false; 
      paramViewParent = paramViewParent.getParent();
    } 
  }
  
  private void throwFeatureRequestIfSubDecorInstalled() {
    if (this.mSubDecorInstalled)
      throw new AndroidRuntimeException("Window feature must be requested before adding content"); 
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureSubDecor();
    ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(paramView, paramLayoutParams);
    this.mOriginalWindowCallback.onContentChanged();
  }
  
  View callActivityOnCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    if (this.mOriginalWindowCallback instanceof LayoutInflater.Factory) {
      paramView = ((LayoutInflater.Factory)this.mOriginalWindowCallback).onCreateView(paramString, paramContext, paramAttributeSet);
      if (paramView != null)
        return paramView; 
    } 
    return null;
  }
  
  void callOnPanelClosed(int paramInt, PanelFeatureState paramPanelFeatureState, Menu paramMenu) {
    MenuBuilder menuBuilder;
    PanelFeatureState panelFeatureState = paramPanelFeatureState;
    Menu menu = paramMenu;
    if (paramMenu == null) {
      PanelFeatureState panelFeatureState1 = paramPanelFeatureState;
      if (paramPanelFeatureState == null) {
        panelFeatureState1 = paramPanelFeatureState;
        if (paramInt >= 0) {
          panelFeatureState1 = paramPanelFeatureState;
          if (paramInt < this.mPanels.length)
            panelFeatureState1 = this.mPanels[paramInt]; 
        } 
      } 
      panelFeatureState = panelFeatureState1;
      menu = paramMenu;
      if (panelFeatureState1 != null) {
        menuBuilder = panelFeatureState1.menu;
        panelFeatureState = panelFeatureState1;
      } 
    } 
    if ((panelFeatureState == null || panelFeatureState.isOpen) && !isDestroyed())
      this.mOriginalWindowCallback.onPanelClosed(paramInt, (Menu)menuBuilder); 
  }
  
  void checkCloseActionMenu(MenuBuilder paramMenuBuilder) {
    if (!this.mClosingActionMenu) {
      this.mClosingActionMenu = true;
      this.mDecorContentParent.dismissPopups();
      Window.Callback callback = getWindowCallback();
      if (callback != null && !isDestroyed())
        callback.onPanelClosed(108, (Menu)paramMenuBuilder); 
      this.mClosingActionMenu = false;
    } 
  }
  
  void closePanel(int paramInt) {
    closePanel(getPanelState(paramInt, true), true);
  }
  
  void closePanel(PanelFeatureState paramPanelFeatureState, boolean paramBoolean) {
    if (paramBoolean && paramPanelFeatureState.featureId == 0 && this.mDecorContentParent != null && this.mDecorContentParent.isOverflowMenuShowing()) {
      checkCloseActionMenu(paramPanelFeatureState.menu);
      return;
    } 
    WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
    if (windowManager != null && paramPanelFeatureState.isOpen && paramPanelFeatureState.decorView != null) {
      windowManager.removeView((View)paramPanelFeatureState.decorView);
      if (paramBoolean)
        callOnPanelClosed(paramPanelFeatureState.featureId, paramPanelFeatureState, (Menu)null); 
    } 
    paramPanelFeatureState.isPrepared = false;
    paramPanelFeatureState.isHandled = false;
    paramPanelFeatureState.isOpen = false;
    paramPanelFeatureState.shownPanelView = null;
    paramPanelFeatureState.refreshDecorView = true;
    if (this.mPreparedPanel == paramPanelFeatureState)
      this.mPreparedPanel = null; 
  }
  
  public View createView(View paramView, String paramString, @NonNull Context paramContext, @NonNull AttributeSet paramAttributeSet) {
    boolean bool1;
    if (Build.VERSION.SDK_INT < 21) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (this.mAppCompatViewInflater == null)
      this.mAppCompatViewInflater = new AppCompatViewInflater(); 
    if (bool1 && shouldInheritContext((ViewParent)paramView)) {
      boolean bool = true;
      return this.mAppCompatViewInflater.createView(paramView, paramString, paramContext, paramAttributeSet, bool, bool1, true, VectorEnabledTintResources.shouldBeUsed());
    } 
    boolean bool2 = false;
    return this.mAppCompatViewInflater.createView(paramView, paramString, paramContext, paramAttributeSet, bool2, bool1, true, VectorEnabledTintResources.shouldBeUsed());
  }
  
  void dismissPopups() {
    if (this.mDecorContentParent != null)
      this.mDecorContentParent.dismissPopups(); 
    if (this.mActionModePopup != null) {
      this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
      if (this.mActionModePopup.isShowing())
        try {
          this.mActionModePopup.dismiss();
        } catch (IllegalArgumentException illegalArgumentException) {} 
      this.mActionModePopup = null;
    } 
    endOnGoingFadeAnimation();
    PanelFeatureState panelFeatureState = getPanelState(0, false);
    if (panelFeatureState != null && panelFeatureState.menu != null)
      panelFeatureState.menu.close(); 
  }
  
  boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    boolean bool = true;
    if (paramKeyEvent.getKeyCode() != 82 || !this.mOriginalWindowCallback.dispatchKeyEvent(paramKeyEvent)) {
      boolean bool1;
      int i = paramKeyEvent.getKeyCode();
      if (paramKeyEvent.getAction() == 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool1)
        return onKeyDown(i, paramKeyEvent); 
      bool = onKeyUp(i, paramKeyEvent);
    } 
    return bool;
  }
  
  void doInvalidatePanelMenu(int paramInt) {
    PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
    if (panelFeatureState.menu != null) {
      Bundle bundle = new Bundle();
      panelFeatureState.menu.saveActionViewStates(bundle);
      if (bundle.size() > 0)
        panelFeatureState.frozenActionViewState = bundle; 
      panelFeatureState.menu.stopDispatchingItemsChanged();
      panelFeatureState.menu.clear();
    } 
    panelFeatureState.refreshMenuContent = true;
    panelFeatureState.refreshDecorView = true;
    if ((paramInt == 108 || paramInt == 0) && this.mDecorContentParent != null) {
      PanelFeatureState panelFeatureState1 = getPanelState(0, false);
      if (panelFeatureState1 != null) {
        panelFeatureState1.isPrepared = false;
        preparePanel(panelFeatureState1, (KeyEvent)null);
      } 
    } 
  }
  
  void endOnGoingFadeAnimation() {
    if (this.mFadeAnim != null)
      this.mFadeAnim.cancel(); 
  }
  
  PanelFeatureState findMenuPanel(Menu paramMenu) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPanels : [Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;
    //   4: astore_2
    //   5: aload_2
    //   6: ifnull -> 46
    //   9: aload_2
    //   10: arraylength
    //   11: istore_3
    //   12: iconst_0
    //   13: istore #4
    //   15: iload #4
    //   17: iload_3
    //   18: if_icmpge -> 57
    //   21: aload_2
    //   22: iload #4
    //   24: aaload
    //   25: astore #5
    //   27: aload #5
    //   29: ifnull -> 51
    //   32: aload #5
    //   34: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   37: aload_1
    //   38: if_acmpne -> 51
    //   41: aload #5
    //   43: astore_1
    //   44: aload_1
    //   45: areturn
    //   46: iconst_0
    //   47: istore_3
    //   48: goto -> 12
    //   51: iinc #4, 1
    //   54: goto -> 15
    //   57: aconst_null
    //   58: astore_1
    //   59: goto -> 44
  }
  
  @Nullable
  public View findViewById(@IdRes int paramInt) {
    ensureSubDecor();
    return this.mWindow.findViewById(paramInt);
  }
  
  protected PanelFeatureState getPanelState(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPanels : [Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnull -> 18
    //   9: aload_3
    //   10: astore #4
    //   12: aload_3
    //   13: arraylength
    //   14: iload_1
    //   15: if_icmpgt -> 50
    //   18: iload_1
    //   19: iconst_1
    //   20: iadd
    //   21: anewarray android/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState
    //   24: astore #5
    //   26: aload_3
    //   27: ifnull -> 40
    //   30: aload_3
    //   31: iconst_0
    //   32: aload #5
    //   34: iconst_0
    //   35: aload_3
    //   36: arraylength
    //   37: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   40: aload #5
    //   42: astore #4
    //   44: aload_0
    //   45: aload #5
    //   47: putfield mPanels : [Landroid/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState;
    //   50: aload #4
    //   52: iload_1
    //   53: aaload
    //   54: astore #5
    //   56: aload #5
    //   58: astore_3
    //   59: aload #5
    //   61: ifnonnull -> 78
    //   64: new android/support/v7/app/AppCompatDelegateImplV9$PanelFeatureState
    //   67: dup
    //   68: iload_1
    //   69: invokespecial <init> : (I)V
    //   72: astore_3
    //   73: aload #4
    //   75: iload_1
    //   76: aload_3
    //   77: aastore
    //   78: aload_3
    //   79: areturn
  }
  
  ViewGroup getSubDecor() {
    return this.mSubDecor;
  }
  
  public boolean hasWindowFeature(int paramInt) {
    switch (sanitizeWindowFeatureId(paramInt)) {
      default:
        return false;
      case 108:
        return this.mHasActionBar;
      case 109:
        return this.mOverlayActionBar;
      case 10:
        return this.mOverlayActionMode;
      case 2:
        return this.mFeatureProgress;
      case 5:
        return this.mFeatureIndeterminateProgress;
      case 1:
        break;
    } 
    return this.mWindowNoTitle;
  }
  
  public void initWindowDecorActionBar() {
    ensureSubDecor();
    if (this.mHasActionBar && this.mActionBar == null) {
      if (this.mOriginalWindowCallback instanceof Activity) {
        this.mActionBar = new WindowDecorActionBar((Activity)this.mOriginalWindowCallback, this.mOverlayActionBar);
      } else if (this.mOriginalWindowCallback instanceof Dialog) {
        this.mActionBar = new WindowDecorActionBar((Dialog)this.mOriginalWindowCallback);
      } 
      if (this.mActionBar != null)
        this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp); 
    } 
  }
  
  public void installViewFactory() {
    LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
    if (layoutInflater.getFactory() == null) {
      LayoutInflaterCompat.setFactory(layoutInflater, this);
      return;
    } 
    if (!(LayoutInflaterCompat.getFactory(layoutInflater) instanceof AppCompatDelegateImplV9))
      Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's"); 
  }
  
  public void invalidateOptionsMenu() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar == null || !actionBar.invalidateOptionsMenu())
      invalidatePanelMenu(0); 
  }
  
  boolean onBackPressed() {
    boolean bool = true;
    if (this.mActionMode != null) {
      this.mActionMode.finish();
      return bool;
    } 
    ActionBar actionBar = getSupportActionBar();
    if (actionBar == null || !actionBar.collapseActionView())
      bool = false; 
    return bool;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (this.mHasActionBar && this.mSubDecorInstalled) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.onConfigurationChanged(paramConfiguration); 
    } 
    AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
    applyDayNight();
  }
  
  public void onCreate(Bundle paramBundle) {
    ActionBar actionBar;
    if (this.mOriginalWindowCallback instanceof Activity && NavUtils.getParentActivityName((Activity)this.mOriginalWindowCallback) != null) {
      actionBar = peekSupportActionBar();
      if (actionBar == null) {
        this.mEnableDefaultActionBarUp = true;
        return;
      } 
    } else {
      return;
    } 
    actionBar.setDefaultDisplayHomeAsUpEnabled(true);
  }
  
  public final View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    View view = callActivityOnCreateView(paramView, paramString, paramContext, paramAttributeSet);
    return (view != null) ? view : createView(paramView, paramString, paramContext, paramAttributeSet);
  }
  
  public void onDestroy() {
    if (this.mInvalidatePanelMenuPosted)
      this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable); 
    super.onDestroy();
    if (this.mActionBar != null)
      this.mActionBar.onDestroy(); 
  }
  
  boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool2;
    boolean bool1 = true;
    null = true;
    switch (paramInt) {
      default:
        if (Build.VERSION.SDK_INT < 11)
          onKeyShortcut(paramInt, paramKeyEvent); 
        return false;
      case 82:
        onKeyDownPanel(0, paramKeyEvent);
        return SYNTHETIC_LOCAL_VARIABLE_4;
      case 4:
        break;
    } 
    if ((paramKeyEvent.getFlags() & 0x80) != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    this.mLongPressBackDown = bool2;
  }
  
  boolean onKeyShortcut(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = true;
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null && actionBar.onKeyShortcut(paramInt, paramKeyEvent))
      return bool; 
    if (this.mPreparedPanel != null && performPanelShortcut(this.mPreparedPanel, paramKeyEvent.getKeyCode(), paramKeyEvent, 1)) {
      boolean bool1 = bool;
      if (this.mPreparedPanel != null) {
        this.mPreparedPanel.isHandled = true;
        bool1 = bool;
      } 
      return bool1;
    } 
    if (this.mPreparedPanel == null) {
      PanelFeatureState panelFeatureState = getPanelState(0, true);
      preparePanel(panelFeatureState, paramKeyEvent);
      boolean bool2 = performPanelShortcut(panelFeatureState, paramKeyEvent.getKeyCode(), paramKeyEvent, 1);
      panelFeatureState.isPrepared = false;
      boolean bool1 = bool;
      return !bool2 ? false : bool1;
    } 
    return false;
  }
  
  boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = true;
    switch (paramInt) {
      default:
        return false;
      case 82:
        onKeyUpPanel(0, paramKeyEvent);
        return bool;
      case 4:
        break;
    } 
    boolean bool1 = this.mLongPressBackDown;
    this.mLongPressBackDown = false;
    PanelFeatureState panelFeatureState = getPanelState(0, false);
    if (panelFeatureState != null && panelFeatureState.isOpen) {
      boolean bool2 = bool;
      if (!bool1) {
        closePanel(panelFeatureState, true);
        bool2 = bool;
      } 
      return bool2;
    } 
    if (onBackPressed())
      return bool; 
  }
  
  public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem) {
    Window.Callback callback = getWindowCallback();
    if (callback != null && !isDestroyed()) {
      PanelFeatureState panelFeatureState = findMenuPanel((Menu)paramMenuBuilder.getRootMenu());
      if (panelFeatureState != null)
        return callback.onMenuItemSelected(panelFeatureState.featureId, paramMenuItem); 
    } 
    return false;
  }
  
  public void onMenuModeChange(MenuBuilder paramMenuBuilder) {
    reopenMenu(paramMenuBuilder, true);
  }
  
  boolean onMenuOpened(int paramInt, Menu paramMenu) {
    boolean bool = true;
    if (paramInt == 108) {
      ActionBar actionBar = getSupportActionBar();
      boolean bool1 = bool;
      if (actionBar != null) {
        actionBar.dispatchMenuVisibilityChanged(true);
        bool1 = bool;
      } 
      return bool1;
    } 
    return false;
  }
  
  void onPanelClosed(int paramInt, Menu paramMenu) {
    if (paramInt == 108) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.dispatchMenuVisibilityChanged(false); 
      return;
    } 
    if (paramInt == 0) {
      PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
      if (panelFeatureState.isOpen)
        closePanel(panelFeatureState, false); 
    } 
  }
  
  public void onPostCreate(Bundle paramBundle) {
    ensureSubDecor();
  }
  
  public void onPostResume() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.setShowHideAnimationEnabled(true); 
  }
  
  public void onStop() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.setShowHideAnimationEnabled(false); 
  }
  
  void onSubDecorInstalled(ViewGroup paramViewGroup) {}
  
  void onTitleChanged(CharSequence paramCharSequence) {
    if (this.mDecorContentParent != null) {
      this.mDecorContentParent.setWindowTitle(paramCharSequence);
      return;
    } 
    if (peekSupportActionBar() != null) {
      peekSupportActionBar().setWindowTitle(paramCharSequence);
      return;
    } 
    if (this.mTitleView != null)
      this.mTitleView.setText(paramCharSequence); 
  }
  
  public boolean requestWindowFeature(int paramInt) {
    boolean bool = false;
    paramInt = sanitizeWindowFeatureId(paramInt);
    if (!this.mWindowNoTitle || paramInt != 108) {
      if (this.mHasActionBar && paramInt == 1)
        this.mHasActionBar = false; 
      switch (paramInt) {
        default:
          return this.mWindow.requestFeature(paramInt);
        case 108:
          throwFeatureRequestIfSubDecorInstalled();
          this.mHasActionBar = true;
          return true;
        case 109:
          throwFeatureRequestIfSubDecorInstalled();
          this.mOverlayActionBar = true;
          return true;
        case 10:
          throwFeatureRequestIfSubDecorInstalled();
          this.mOverlayActionMode = true;
          return true;
        case 2:
          throwFeatureRequestIfSubDecorInstalled();
          this.mFeatureProgress = true;
          return true;
        case 5:
          throwFeatureRequestIfSubDecorInstalled();
          this.mFeatureIndeterminateProgress = true;
          return true;
        case 1:
          break;
      } 
      throwFeatureRequestIfSubDecorInstalled();
      this.mWindowNoTitle = true;
      bool = true;
    } 
    return bool;
  }
  
  public void setContentView(int paramInt) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    LayoutInflater.from(this.mContext).inflate(paramInt, viewGroup);
    this.mOriginalWindowCallback.onContentChanged();
  }
  
  public void setContentView(View paramView) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    viewGroup.addView(paramView);
    this.mOriginalWindowCallback.onContentChanged();
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    viewGroup.addView(paramView, paramLayoutParams);
    this.mOriginalWindowCallback.onContentChanged();
  }
  
  public void setSupportActionBar(Toolbar paramToolbar) {
    if (this.mOriginalWindowCallback instanceof Activity) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar instanceof WindowDecorActionBar)
        throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead."); 
      this.mMenuInflater = null;
      if (actionBar != null)
        actionBar.onDestroy(); 
      if (paramToolbar != null) {
        ToolbarActionBar toolbarActionBar = new ToolbarActionBar(paramToolbar, ((Activity)this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
        this.mActionBar = toolbarActionBar;
        this.mWindow.setCallback(toolbarActionBar.getWrappedWindowCallback());
      } else {
        this.mActionBar = null;
        this.mWindow.setCallback(this.mAppCompatWindowCallback);
      } 
      invalidateOptionsMenu();
    } 
  }
  
  final boolean shouldAnimateActionModeView() {
    return (this.mSubDecorInstalled && this.mSubDecor != null && ViewCompat.isLaidOut((View)this.mSubDecor));
  }
  
  public ActionMode startSupportActionMode(@NonNull ActionMode.Callback paramCallback) {
    if (paramCallback == null)
      throw new IllegalArgumentException("ActionMode callback can not be null."); 
    if (this.mActionMode != null)
      this.mActionMode.finish(); 
    paramCallback = new ActionModeCallbackWrapperV9(paramCallback);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      this.mActionMode = actionBar.startActionMode(paramCallback);
      if (this.mActionMode != null && this.mAppCompatCallback != null)
        this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode); 
    } 
    if (this.mActionMode == null)
      this.mActionMode = startSupportActionModeFromWindow(paramCallback); 
    return this.mActionMode;
  }
  
  ActionMode startSupportActionModeFromWindow(@NonNull ActionMode.Callback paramCallback) {
    ActionMode.Callback callback1;
    endOnGoingFadeAnimation();
    if (this.mActionMode != null)
      this.mActionMode.finish(); 
    ActionMode.Callback callback2 = paramCallback;
    if (!(paramCallback instanceof ActionModeCallbackWrapperV9))
      callback2 = new ActionModeCallbackWrapperV9(paramCallback); 
    ActionMode.Callback callback3 = null;
    paramCallback = callback3;
    if (this.mAppCompatCallback != null) {
      paramCallback = callback3;
      if (!isDestroyed())
        try {
          ActionMode actionMode = this.mAppCompatCallback.onWindowStartingSupportActionMode(callback2);
        } catch (AbstractMethodError abstractMethodError) {
          callback1 = callback3;
        }  
    } 
    if (callback1 != null) {
      this.mActionMode = (ActionMode)callback1;
    } else {
      if (this.mActionModeView == null)
        if (this.mIsFloating) {
          Context context;
          TypedValue typedValue = new TypedValue();
          Resources.Theme theme = this.mContext.getTheme();
          theme.resolveAttribute(R.attr.actionBarTheme, typedValue, true);
          if (typedValue.resourceId != 0) {
            Resources.Theme theme1 = this.mContext.getResources().newTheme();
            theme1.setTo(theme);
            theme1.applyStyle(typedValue.resourceId, true);
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.mContext, 0);
            contextThemeWrapper.getTheme().setTo(theme1);
          } else {
            context = this.mContext;
          } 
          this.mActionModeView = new ActionBarContextView(context);
          this.mActionModePopup = new PopupWindow(context, null, R.attr.actionModePopupWindowStyle);
          PopupWindowCompat.setWindowLayoutType(this.mActionModePopup, 2);
          this.mActionModePopup.setContentView((View)this.mActionModeView);
          this.mActionModePopup.setWidth(-1);
          context.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
          int i = TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
          this.mActionModeView.setContentHeight(i);
          this.mActionModePopup.setHeight(-2);
          this.mShowActionModePopup = new Runnable() {
              public void run() {
                AppCompatDelegateImplV9.this.mActionModePopup.showAtLocation((View)AppCompatDelegateImplV9.this.mActionModeView, 55, 0, 0);
                AppCompatDelegateImplV9.this.endOnGoingFadeAnimation();
                if (AppCompatDelegateImplV9.this.shouldAnimateActionModeView()) {
                  ViewCompat.setAlpha((View)AppCompatDelegateImplV9.this.mActionModeView, 0.0F);
                  AppCompatDelegateImplV9.this.mFadeAnim = ViewCompat.animate((View)AppCompatDelegateImplV9.this.mActionModeView).alpha(1.0F);
                  AppCompatDelegateImplV9.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
                        public void onAnimationEnd(View param2View) {
                          ViewCompat.setAlpha((View)AppCompatDelegateImplV9.this.mActionModeView, 1.0F);
                          AppCompatDelegateImplV9.this.mFadeAnim.setListener(null);
                          AppCompatDelegateImplV9.this.mFadeAnim = null;
                        }
                        
                        public void onAnimationStart(View param2View) {
                          AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
                        }
                      });
                  return;
                } 
                ViewCompat.setAlpha((View)AppCompatDelegateImplV9.this.mActionModeView, 1.0F);
                AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
              }
            };
        } else {
          ViewStubCompat viewStubCompat = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
          if (viewStubCompat != null) {
            viewStubCompat.setLayoutInflater(LayoutInflater.from(getActionBarThemedContext()));
            this.mActionModeView = (ActionBarContextView)viewStubCompat.inflate();
          } 
        }  
      if (this.mActionModeView != null) {
        boolean bool;
        endOnGoingFadeAnimation();
        this.mActionModeView.killMode();
        Context context = this.mActionModeView.getContext();
        ActionBarContextView actionBarContextView = this.mActionModeView;
        if (this.mActionModePopup == null) {
          bool = true;
        } else {
          bool = false;
        } 
        StandaloneActionMode standaloneActionMode = new StandaloneActionMode(context, actionBarContextView, callback2, bool);
        if (callback2.onCreateActionMode((ActionMode)standaloneActionMode, standaloneActionMode.getMenu())) {
          standaloneActionMode.invalidate();
          this.mActionModeView.initForMode((ActionMode)standaloneActionMode);
          this.mActionMode = (ActionMode)standaloneActionMode;
          if (shouldAnimateActionModeView()) {
            ViewCompat.setAlpha((View)this.mActionModeView, 0.0F);
            this.mFadeAnim = ViewCompat.animate((View)this.mActionModeView).alpha(1.0F);
            this.mFadeAnim.setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
                  public void onAnimationEnd(View param1View) {
                    ViewCompat.setAlpha((View)AppCompatDelegateImplV9.this.mActionModeView, 1.0F);
                    AppCompatDelegateImplV9.this.mFadeAnim.setListener(null);
                    AppCompatDelegateImplV9.this.mFadeAnim = null;
                  }
                  
                  public void onAnimationStart(View param1View) {
                    AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
                    AppCompatDelegateImplV9.this.mActionModeView.sendAccessibilityEvent(32);
                    if (AppCompatDelegateImplV9.this.mActionModeView.getParent() instanceof View)
                      ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV9.this.mActionModeView.getParent()); 
                  }
                });
          } else {
            ViewCompat.setAlpha((View)this.mActionModeView, 1.0F);
            this.mActionModeView.setVisibility(0);
            this.mActionModeView.sendAccessibilityEvent(32);
            if (this.mActionModeView.getParent() instanceof View)
              ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent()); 
          } 
          if (this.mActionModePopup != null)
            this.mWindow.getDecorView().post(this.mShowActionModePopup); 
        } else {
          this.mActionMode = null;
        } 
      } 
    } 
    if (this.mActionMode != null && this.mAppCompatCallback != null)
      this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode); 
    return this.mActionMode;
  }
  
  int updateStatusGuard(int paramInt) {
    boolean bool1 = false;
    int i = 0;
    boolean bool2 = false;
    int j = i;
    int k = paramInt;
    if (this.mActionModeView != null) {
      j = i;
      k = paramInt;
      if (this.mActionModeView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
        int n;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mActionModeView.getLayoutParams();
        int m = 0;
        k = 0;
        if (this.mActionModeView.isShown()) {
          if (this.mTempRect1 == null) {
            this.mTempRect1 = new Rect();
            this.mTempRect2 = new Rect();
          } 
          Rect rect1 = this.mTempRect1;
          Rect rect2 = this.mTempRect2;
          rect1.set(0, paramInt, 0, 0);
          ViewUtils.computeFitSystemWindows((View)this.mSubDecor, rect1, rect2);
          if (rect2.top == 0) {
            i = paramInt;
          } else {
            i = 0;
          } 
          if (marginLayoutParams.topMargin != i) {
            i = 1;
            marginLayoutParams.topMargin = paramInt;
            if (this.mStatusGuard == null) {
              this.mStatusGuard = new View(this.mContext);
              this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
              this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup.LayoutParams(-1, paramInt));
              k = i;
            } else {
              ViewGroup.LayoutParams layoutParams = this.mStatusGuard.getLayoutParams();
              k = i;
              if (layoutParams.height != paramInt) {
                layoutParams.height = paramInt;
                this.mStatusGuard.setLayoutParams(layoutParams);
                k = i;
              } 
            } 
          } 
          if (this.mStatusGuard != null) {
            j = 1;
          } else {
            j = 0;
          } 
          m = k;
          i = j;
          n = paramInt;
          if (!this.mOverlayActionMode) {
            m = k;
            i = j;
            n = paramInt;
            if (j != 0) {
              n = 0;
              i = j;
              m = k;
            } 
          } 
        } else {
          i = bool2;
          n = paramInt;
          if (marginLayoutParams.topMargin != 0) {
            m = 1;
            marginLayoutParams.topMargin = 0;
            i = bool2;
            n = paramInt;
          } 
        } 
        j = i;
        k = n;
        if (m != 0) {
          this.mActionModeView.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
          k = n;
          j = i;
        } 
      } 
    } 
    if (this.mStatusGuard != null) {
      View view = this.mStatusGuard;
      if (j != 0) {
        paramInt = bool1;
      } else {
        paramInt = 8;
      } 
      view.setVisibility(paramInt);
    } 
    return k;
  }
  
  private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      AppCompatDelegateImplV9.this.checkCloseActionMenu(param1MenuBuilder);
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      Window.Callback callback = AppCompatDelegateImplV9.this.getWindowCallback();
      if (callback != null)
        callback.onMenuOpened(108, (Menu)param1MenuBuilder); 
      return true;
    }
  }
  
  class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
    private ActionMode.Callback mWrapped;
    
    public ActionModeCallbackWrapperV9(ActionMode.Callback param1Callback) {
      this.mWrapped = param1Callback;
    }
    
    public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) {
      return this.mWrapped.onActionItemClicked(param1ActionMode, param1MenuItem);
    }
    
    public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      return this.mWrapped.onCreateActionMode(param1ActionMode, param1Menu);
    }
    
    public void onDestroyActionMode(ActionMode param1ActionMode) {
      this.mWrapped.onDestroyActionMode(param1ActionMode);
      if (AppCompatDelegateImplV9.this.mActionModePopup != null)
        AppCompatDelegateImplV9.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImplV9.this.mShowActionModePopup); 
      if (AppCompatDelegateImplV9.this.mActionModeView != null) {
        AppCompatDelegateImplV9.this.endOnGoingFadeAnimation();
        AppCompatDelegateImplV9.this.mFadeAnim = ViewCompat.animate((View)AppCompatDelegateImplV9.this.mActionModeView).alpha(0.0F);
        AppCompatDelegateImplV9.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
              public void onAnimationEnd(View param2View) {
                AppCompatDelegateImplV9.this.mActionModeView.setVisibility(8);
                if (AppCompatDelegateImplV9.this.mActionModePopup != null) {
                  AppCompatDelegateImplV9.this.mActionModePopup.dismiss();
                } else if (AppCompatDelegateImplV9.this.mActionModeView.getParent() instanceof View) {
                  ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV9.this.mActionModeView.getParent());
                } 
                AppCompatDelegateImplV9.this.mActionModeView.removeAllViews();
                AppCompatDelegateImplV9.this.mFadeAnim.setListener(null);
                AppCompatDelegateImplV9.this.mFadeAnim = null;
              }
            });
      } 
      if (AppCompatDelegateImplV9.this.mAppCompatCallback != null)
        AppCompatDelegateImplV9.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImplV9.this.mActionMode); 
      AppCompatDelegateImplV9.this.mActionMode = null;
    }
    
    public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      return this.mWrapped.onPrepareActionMode(param1ActionMode, param1Menu);
    }
  }
  
  class null extends ViewPropertyAnimatorListenerAdapter {
    public void onAnimationEnd(View param1View) {
      AppCompatDelegateImplV9.this.mActionModeView.setVisibility(8);
      if (AppCompatDelegateImplV9.this.mActionModePopup != null) {
        AppCompatDelegateImplV9.this.mActionModePopup.dismiss();
      } else if (AppCompatDelegateImplV9.this.mActionModeView.getParent() instanceof View) {
        ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV9.this.mActionModeView.getParent());
      } 
      AppCompatDelegateImplV9.this.mActionModeView.removeAllViews();
      AppCompatDelegateImplV9.this.mFadeAnim.setListener(null);
      AppCompatDelegateImplV9.this.mFadeAnim = null;
    }
  }
  
  private class ListMenuDecorView extends ContentFrameLayout {
    public ListMenuDecorView(Context param1Context) {
      super(param1Context);
    }
    
    private boolean isOutOfBounds(int param1Int1, int param1Int2) {
      return (param1Int1 < -5 || param1Int2 < -5 || param1Int1 > getWidth() + 5 || param1Int2 > getHeight() + 5);
    }
    
    public boolean dispatchKeyEvent(KeyEvent param1KeyEvent) {
      return (AppCompatDelegateImplV9.this.dispatchKeyEvent(param1KeyEvent) || super.dispatchKeyEvent(param1KeyEvent));
    }
    
    public boolean onInterceptTouchEvent(MotionEvent param1MotionEvent) {
      if (param1MotionEvent.getAction() == 0 && isOutOfBounds((int)param1MotionEvent.getX(), (int)param1MotionEvent.getY())) {
        AppCompatDelegateImplV9.this.closePanel(0);
        return true;
      } 
      return super.onInterceptTouchEvent(param1MotionEvent);
    }
    
    public void setBackgroundResource(int param1Int) {
      setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), param1Int));
    }
  }
  
  protected static final class PanelFeatureState {
    int background;
    
    View createdPanelView;
    
    ViewGroup decorView;
    
    int featureId;
    
    Bundle frozenActionViewState;
    
    Bundle frozenMenuState;
    
    int gravity;
    
    boolean isHandled;
    
    boolean isOpen;
    
    boolean isPrepared;
    
    ListMenuPresenter listMenuPresenter;
    
    Context listPresenterContext;
    
    MenuBuilder menu;
    
    public boolean qwertyMode;
    
    boolean refreshDecorView;
    
    boolean refreshMenuContent;
    
    View shownPanelView;
    
    boolean wasLastOpen;
    
    int windowAnimations;
    
    int x;
    
    int y;
    
    PanelFeatureState(int param1Int) {
      this.featureId = param1Int;
      this.refreshDecorView = false;
    }
    
    void applyFrozenState() {
      if (this.menu != null && this.frozenMenuState != null) {
        this.menu.restorePresenterStates(this.frozenMenuState);
        this.frozenMenuState = null;
      } 
    }
    
    public void clearMenuPresenters() {
      if (this.menu != null)
        this.menu.removeMenuPresenter((MenuPresenter)this.listMenuPresenter); 
      this.listMenuPresenter = null;
    }
    
    MenuView getListMenuView(MenuPresenter.Callback param1Callback) {
      if (this.menu == null)
        return null; 
      if (this.listMenuPresenter == null) {
        this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout);
        this.listMenuPresenter.setCallback(param1Callback);
        this.menu.addMenuPresenter((MenuPresenter)this.listMenuPresenter);
      } 
      return this.listMenuPresenter.getMenuView(this.decorView);
    }
    
    public boolean hasPanelItems() {
      boolean bool1 = true;
      if (this.shownPanelView == null)
        return false; 
      boolean bool2 = bool1;
      if (this.createdPanelView == null) {
        bool2 = bool1;
        if (this.listMenuPresenter.getAdapter().getCount() <= 0)
          bool2 = false; 
      } 
      return bool2;
    }
    
    void onRestoreInstanceState(Parcelable param1Parcelable) {
      param1Parcelable = param1Parcelable;
      this.featureId = ((SavedState)param1Parcelable).featureId;
      this.wasLastOpen = ((SavedState)param1Parcelable).isOpen;
      this.frozenMenuState = ((SavedState)param1Parcelable).menuState;
      this.shownPanelView = null;
      this.decorView = null;
    }
    
    Parcelable onSaveInstanceState() {
      SavedState savedState = new SavedState();
      savedState.featureId = this.featureId;
      savedState.isOpen = this.isOpen;
      if (this.menu != null) {
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
      } 
      return savedState;
    }
    
    void setMenu(MenuBuilder param1MenuBuilder) {
      if (param1MenuBuilder != this.menu) {
        if (this.menu != null)
          this.menu.removeMenuPresenter((MenuPresenter)this.listMenuPresenter); 
        this.menu = param1MenuBuilder;
        if (param1MenuBuilder != null && this.listMenuPresenter != null)
          param1MenuBuilder.addMenuPresenter((MenuPresenter)this.listMenuPresenter); 
      } 
    }
    
    void setStyle(Context param1Context) {
      TypedValue typedValue = new TypedValue();
      Resources.Theme theme = param1Context.getResources().newTheme();
      theme.setTo(param1Context.getTheme());
      theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
      if (typedValue.resourceId != 0)
        theme.applyStyle(typedValue.resourceId, true); 
      theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
      if (typedValue.resourceId != 0) {
        theme.applyStyle(typedValue.resourceId, true);
      } else {
        theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
      } 
      ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(param1Context, 0);
      contextThemeWrapper.getTheme().setTo(theme);
      this.listPresenterContext = (Context)contextThemeWrapper;
      TypedArray typedArray = contextThemeWrapper.obtainStyledAttributes(R.styleable.AppCompatTheme);
      this.background = typedArray.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
      this.windowAnimations = typedArray.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
      typedArray.recycle();
    }
    
    private static class SavedState implements Parcelable {
      public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
            public AppCompatDelegateImplV9.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
              return AppCompatDelegateImplV9.PanelFeatureState.SavedState.readFromParcel(param3Parcel, param3ClassLoader);
            }
            
            public AppCompatDelegateImplV9.PanelFeatureState.SavedState[] newArray(int param3Int) {
              return new AppCompatDelegateImplV9.PanelFeatureState.SavedState[param3Int];
            }
          });
      
      int featureId;
      
      boolean isOpen;
      
      Bundle menuState;
      
      static SavedState readFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        boolean bool = true;
        SavedState savedState = new SavedState();
        savedState.featureId = param2Parcel.readInt();
        if (param2Parcel.readInt() != 1)
          bool = false; 
        savedState.isOpen = bool;
        if (savedState.isOpen)
          savedState.menuState = param2Parcel.readBundle(param2ClassLoader); 
        return savedState;
      }
      
      public int describeContents() {
        return 0;
      }
      
      public void writeToParcel(Parcel param2Parcel, int param2Int) {
        param2Parcel.writeInt(this.featureId);
        if (this.isOpen) {
          param2Int = 1;
        } else {
          param2Int = 0;
        } 
        param2Parcel.writeInt(param2Int);
        if (this.isOpen)
          param2Parcel.writeBundle(this.menuState); 
      }
    }
    
    static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
      public AppCompatDelegateImplV9.PanelFeatureState.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        return AppCompatDelegateImplV9.PanelFeatureState.SavedState.readFromParcel(param2Parcel, param2ClassLoader);
      }
      
      public AppCompatDelegateImplV9.PanelFeatureState.SavedState[] newArray(int param2Int) {
        return new AppCompatDelegateImplV9.PanelFeatureState.SavedState[param2Int];
      }
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public AppCompatDelegateImplV9.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
            return AppCompatDelegateImplV9.PanelFeatureState.SavedState.readFromParcel(param3Parcel, param3ClassLoader);
          }
          
          public AppCompatDelegateImplV9.PanelFeatureState.SavedState[] newArray(int param3Int) {
            return new AppCompatDelegateImplV9.PanelFeatureState.SavedState[param3Int];
          }
        });
    
    int featureId;
    
    boolean isOpen;
    
    Bundle menuState;
    
    static SavedState readFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      boolean bool = true;
      SavedState savedState = new SavedState();
      savedState.featureId = param1Parcel.readInt();
      if (param1Parcel.readInt() != 1)
        bool = false; 
      savedState.isOpen = bool;
      if (savedState.isOpen)
        savedState.menuState = param1Parcel.readBundle(param1ClassLoader); 
      return savedState;
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.featureId);
      if (this.isOpen) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
      if (this.isOpen)
        param1Parcel.writeBundle(this.menuState); 
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<PanelFeatureState.SavedState> {
    public AppCompatDelegateImplV9.PanelFeatureState.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return AppCompatDelegateImplV9.PanelFeatureState.SavedState.readFromParcel(param1Parcel, param1ClassLoader);
    }
    
    public AppCompatDelegateImplV9.PanelFeatureState.SavedState[] newArray(int param1Int) {
      return new AppCompatDelegateImplV9.PanelFeatureState.SavedState[param1Int];
    }
  }
  
  private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      boolean bool;
      MenuBuilder menuBuilder = param1MenuBuilder.getRootMenu();
      if (menuBuilder != param1MenuBuilder) {
        bool = true;
      } else {
        bool = false;
      } 
      AppCompatDelegateImplV9 appCompatDelegateImplV9 = AppCompatDelegateImplV9.this;
      if (bool)
        param1MenuBuilder = menuBuilder; 
      AppCompatDelegateImplV9.PanelFeatureState panelFeatureState = appCompatDelegateImplV9.findMenuPanel((Menu)param1MenuBuilder);
      if (panelFeatureState != null) {
        if (bool) {
          AppCompatDelegateImplV9.this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, (Menu)menuBuilder);
          AppCompatDelegateImplV9.this.closePanel(panelFeatureState, true);
          return;
        } 
      } else {
        return;
      } 
      AppCompatDelegateImplV9.this.closePanel(panelFeatureState, param1Boolean);
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      if (param1MenuBuilder == null && AppCompatDelegateImplV9.this.mHasActionBar) {
        Window.Callback callback = AppCompatDelegateImplV9.this.getWindowCallback();
        if (callback != null && !AppCompatDelegateImplV9.this.isDestroyed())
          callback.onMenuOpened(108, (Menu)param1MenuBuilder); 
      } 
      return true;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/app/AppCompatDelegateImplV9.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */