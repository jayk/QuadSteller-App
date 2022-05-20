package app.lib.pullToRefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint({"ViewConstructor"})
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
  static final Interpolator ANIMATION_INTERPOLATOR = (Interpolator)new LinearInterpolator();
  
  static final String LOG_TAG = "PullToRefresh-LoadingLayout";
  
  protected final ImageView mHeaderImage;
  
  protected final ProgressBar mHeaderProgress;
  
  private final TextView mHeaderText;
  
  private FrameLayout mInnerLayout;
  
  protected final PullToRefreshBase.Mode mMode;
  
  private CharSequence mPullLabel;
  
  private CharSequence mRefreshingLabel;
  
  private CharSequence mReleaseLabel;
  
  protected final PullToRefreshBase.Orientation mScrollDirection;
  
  private final TextView mSubHeaderText;
  
  private boolean mUseIntrinsicAnimation;
  
  public LoadingLayout(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.Orientation paramOrientation, TypedArray paramTypedArray) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial <init> : (Landroid/content/Context;)V
    //   5: aload_0
    //   6: aload_2
    //   7: putfield mMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   10: aload_0
    //   11: aload_3
    //   12: putfield mScrollDirection : Lapp/lib/pullToRefresh/PullToRefreshBase$Orientation;
    //   15: getstatic app/lib/pullToRefresh/LoadingLayout$1.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Orientation : [I
    //   18: aload_3
    //   19: invokevirtual ordinal : ()I
    //   22: iaload
    //   23: tableswitch default -> 40, 1 -> 454
    //   40: aload_1
    //   41: invokestatic from : (Landroid/content/Context;)Landroid/view/LayoutInflater;
    //   44: ldc 2130903286
    //   46: aload_0
    //   47: invokevirtual inflate : (ILandroid/view/ViewGroup;)Landroid/view/View;
    //   50: pop
    //   51: aload_0
    //   52: aload_0
    //   53: ldc 2131756969
    //   55: invokevirtual findViewById : (I)Landroid/view/View;
    //   58: checkcast android/widget/FrameLayout
    //   61: putfield mInnerLayout : Landroid/widget/FrameLayout;
    //   64: aload_0
    //   65: aload_0
    //   66: getfield mInnerLayout : Landroid/widget/FrameLayout;
    //   69: ldc 2131756972
    //   71: invokevirtual findViewById : (I)Landroid/view/View;
    //   74: checkcast android/widget/TextView
    //   77: putfield mHeaderText : Landroid/widget/TextView;
    //   80: aload_0
    //   81: aload_0
    //   82: getfield mInnerLayout : Landroid/widget/FrameLayout;
    //   85: ldc 2131756971
    //   87: invokevirtual findViewById : (I)Landroid/view/View;
    //   90: checkcast android/widget/ProgressBar
    //   93: putfield mHeaderProgress : Landroid/widget/ProgressBar;
    //   96: aload_0
    //   97: aload_0
    //   98: getfield mInnerLayout : Landroid/widget/FrameLayout;
    //   101: ldc 2131756973
    //   103: invokevirtual findViewById : (I)Landroid/view/View;
    //   106: checkcast android/widget/TextView
    //   109: putfield mSubHeaderText : Landroid/widget/TextView;
    //   112: aload_0
    //   113: aload_0
    //   114: getfield mInnerLayout : Landroid/widget/FrameLayout;
    //   117: ldc 2131756970
    //   119: invokevirtual findViewById : (I)Landroid/view/View;
    //   122: checkcast android/widget/ImageView
    //   125: putfield mHeaderImage : Landroid/widget/ImageView;
    //   128: aload_0
    //   129: getfield mInnerLayout : Landroid/widget/FrameLayout;
    //   132: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   135: checkcast android/widget/FrameLayout$LayoutParams
    //   138: astore #5
    //   140: getstatic app/lib/pullToRefresh/LoadingLayout$1.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Mode : [I
    //   143: aload_2
    //   144: invokevirtual ordinal : ()I
    //   147: iaload
    //   148: tableswitch default -> 168, 1 -> 468
    //   168: aload_3
    //   169: getstatic app/lib/pullToRefresh/PullToRefreshBase$Orientation.VERTICAL : Lapp/lib/pullToRefresh/PullToRefreshBase$Orientation;
    //   172: if_acmpne -> 525
    //   175: bipush #80
    //   177: istore #6
    //   179: aload #5
    //   181: iload #6
    //   183: putfield gravity : I
    //   186: aload_0
    //   187: aload_1
    //   188: ldc 2131296997
    //   190: invokevirtual getString : (I)Ljava/lang/String;
    //   193: putfield mPullLabel : Ljava/lang/CharSequence;
    //   196: aload_0
    //   197: aload_1
    //   198: ldc 2131296998
    //   200: invokevirtual getString : (I)Ljava/lang/String;
    //   203: putfield mRefreshingLabel : Ljava/lang/CharSequence;
    //   206: aload_0
    //   207: aload_1
    //   208: ldc 2131297000
    //   210: invokevirtual getString : (I)Ljava/lang/String;
    //   213: putfield mReleaseLabel : Ljava/lang/CharSequence;
    //   216: aload #4
    //   218: iconst_1
    //   219: invokevirtual hasValue : (I)Z
    //   222: ifeq -> 241
    //   225: aload #4
    //   227: iconst_1
    //   228: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   231: astore_3
    //   232: aload_3
    //   233: ifnull -> 241
    //   236: aload_0
    //   237: aload_3
    //   238: invokestatic setBackground : (Landroid/view/View;Landroid/graphics/drawable/Drawable;)V
    //   241: aload #4
    //   243: bipush #10
    //   245: invokevirtual hasValue : (I)Z
    //   248: ifeq -> 276
    //   251: new android/util/TypedValue
    //   254: dup
    //   255: invokespecial <init> : ()V
    //   258: astore_3
    //   259: aload #4
    //   261: bipush #10
    //   263: aload_3
    //   264: invokevirtual getValue : (ILandroid/util/TypedValue;)Z
    //   267: pop
    //   268: aload_0
    //   269: aload_3
    //   270: getfield data : I
    //   273: invokespecial setTextAppearance : (I)V
    //   276: aload #4
    //   278: bipush #11
    //   280: invokevirtual hasValue : (I)Z
    //   283: ifeq -> 311
    //   286: new android/util/TypedValue
    //   289: dup
    //   290: invokespecial <init> : ()V
    //   293: astore_3
    //   294: aload #4
    //   296: bipush #11
    //   298: aload_3
    //   299: invokevirtual getValue : (ILandroid/util/TypedValue;)Z
    //   302: pop
    //   303: aload_0
    //   304: aload_3
    //   305: getfield data : I
    //   308: invokespecial setSubTextAppearance : (I)V
    //   311: aload #4
    //   313: iconst_2
    //   314: invokevirtual hasValue : (I)Z
    //   317: ifeq -> 336
    //   320: aload #4
    //   322: iconst_2
    //   323: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   326: astore_3
    //   327: aload_3
    //   328: ifnull -> 336
    //   331: aload_0
    //   332: aload_3
    //   333: invokespecial setTextColor : (Landroid/content/res/ColorStateList;)V
    //   336: aload #4
    //   338: iconst_3
    //   339: invokevirtual hasValue : (I)Z
    //   342: ifeq -> 361
    //   345: aload #4
    //   347: iconst_3
    //   348: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   351: astore_3
    //   352: aload_3
    //   353: ifnull -> 361
    //   356: aload_0
    //   357: aload_3
    //   358: invokespecial setSubTextColor : (Landroid/content/res/ColorStateList;)V
    //   361: aconst_null
    //   362: astore_3
    //   363: aload #4
    //   365: bipush #6
    //   367: invokevirtual hasValue : (I)Z
    //   370: ifeq -> 381
    //   373: aload #4
    //   375: bipush #6
    //   377: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   380: astore_3
    //   381: getstatic app/lib/pullToRefresh/LoadingLayout$1.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Mode : [I
    //   384: aload_2
    //   385: invokevirtual ordinal : ()I
    //   388: iaload
    //   389: tableswitch default -> 408, 1 -> 561
    //   408: aload #4
    //   410: bipush #7
    //   412: invokevirtual hasValue : (I)Z
    //   415: ifeq -> 531
    //   418: aload #4
    //   420: bipush #7
    //   422: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   425: astore_2
    //   426: aload_2
    //   427: astore_3
    //   428: aload_2
    //   429: ifnonnull -> 444
    //   432: aload_1
    //   433: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   436: aload_0
    //   437: invokevirtual getDefaultDrawableResId : ()I
    //   440: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   443: astore_3
    //   444: aload_0
    //   445: aload_3
    //   446: invokevirtual setLoadingDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   449: aload_0
    //   450: invokevirtual reset : ()V
    //   453: return
    //   454: aload_1
    //   455: invokestatic from : (Landroid/content/Context;)Landroid/view/LayoutInflater;
    //   458: ldc 2130903285
    //   460: aload_0
    //   461: invokevirtual inflate : (ILandroid/view/ViewGroup;)Landroid/view/View;
    //   464: pop
    //   465: goto -> 51
    //   468: aload_3
    //   469: getstatic app/lib/pullToRefresh/PullToRefreshBase$Orientation.VERTICAL : Lapp/lib/pullToRefresh/PullToRefreshBase$Orientation;
    //   472: if_acmpne -> 519
    //   475: bipush #48
    //   477: istore #6
    //   479: aload #5
    //   481: iload #6
    //   483: putfield gravity : I
    //   486: aload_0
    //   487: aload_1
    //   488: ldc 2131296994
    //   490: invokevirtual getString : (I)Ljava/lang/String;
    //   493: putfield mPullLabel : Ljava/lang/CharSequence;
    //   496: aload_0
    //   497: aload_1
    //   498: ldc 2131296995
    //   500: invokevirtual getString : (I)Ljava/lang/String;
    //   503: putfield mRefreshingLabel : Ljava/lang/CharSequence;
    //   506: aload_0
    //   507: aload_1
    //   508: ldc 2131296996
    //   510: invokevirtual getString : (I)Ljava/lang/String;
    //   513: putfield mReleaseLabel : Ljava/lang/CharSequence;
    //   516: goto -> 216
    //   519: iconst_3
    //   520: istore #6
    //   522: goto -> 479
    //   525: iconst_5
    //   526: istore #6
    //   528: goto -> 179
    //   531: aload_3
    //   532: astore_2
    //   533: aload #4
    //   535: bipush #17
    //   537: invokevirtual hasValue : (I)Z
    //   540: ifeq -> 426
    //   543: ldc 'ptrDrawableTop'
    //   545: ldc 'ptrDrawableStart'
    //   547: invokestatic warnDeprecation : (Ljava/lang/String;Ljava/lang/String;)V
    //   550: aload #4
    //   552: bipush #17
    //   554: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   557: astore_2
    //   558: goto -> 426
    //   561: aload #4
    //   563: bipush #8
    //   565: invokevirtual hasValue : (I)Z
    //   568: ifeq -> 582
    //   571: aload #4
    //   573: bipush #8
    //   575: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   578: astore_2
    //   579: goto -> 426
    //   582: aload_3
    //   583: astore_2
    //   584: aload #4
    //   586: bipush #18
    //   588: invokevirtual hasValue : (I)Z
    //   591: ifeq -> 426
    //   594: ldc 'ptrDrawableBottom'
    //   596: ldc 'ptrDrawableEnd'
    //   598: invokestatic warnDeprecation : (Ljava/lang/String;Ljava/lang/String;)V
    //   601: aload #4
    //   603: bipush #18
    //   605: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   608: astore_2
    //   609: goto -> 426
  }
  
  private void setSubHeaderText(CharSequence paramCharSequence) {
    if (this.mSubHeaderText != null) {
      if (TextUtils.isEmpty(paramCharSequence)) {
        this.mSubHeaderText.setVisibility(8);
        return;
      } 
    } else {
      return;
    } 
    this.mSubHeaderText.setText(paramCharSequence);
    if (8 == this.mSubHeaderText.getVisibility())
      this.mSubHeaderText.setVisibility(0); 
  }
  
  private void setSubTextAppearance(int paramInt) {
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setTextAppearance(getContext(), paramInt); 
  }
  
  private void setSubTextColor(ColorStateList paramColorStateList) {
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setTextColor(paramColorStateList); 
  }
  
  private void setTextAppearance(int paramInt) {
    if (this.mHeaderText != null)
      this.mHeaderText.setTextAppearance(getContext(), paramInt); 
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setTextAppearance(getContext(), paramInt); 
  }
  
  private void setTextColor(ColorStateList paramColorStateList) {
    if (this.mHeaderText != null)
      this.mHeaderText.setTextColor(paramColorStateList); 
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setTextColor(paramColorStateList); 
  }
  
  public final int getContentSize() {
    switch (this.mScrollDirection) {
      default:
        return this.mInnerLayout.getHeight();
      case PULL_FROM_END:
        break;
    } 
    return this.mInnerLayout.getWidth();
  }
  
  protected abstract int getDefaultDrawableResId();
  
  public final void hideAllViews() {
    if (this.mHeaderText.getVisibility() == 0)
      this.mHeaderText.setVisibility(4); 
    if (this.mHeaderProgress.getVisibility() == 0)
      this.mHeaderProgress.setVisibility(4); 
    if (this.mHeaderImage.getVisibility() == 0)
      this.mHeaderImage.setVisibility(4); 
    if (this.mSubHeaderText.getVisibility() == 0)
      this.mSubHeaderText.setVisibility(4); 
  }
  
  protected abstract void onLoadingDrawableSet(Drawable paramDrawable);
  
  public final void onPull(float paramFloat) {
    if (!this.mUseIntrinsicAnimation)
      onPullImpl(paramFloat); 
  }
  
  protected abstract void onPullImpl(float paramFloat);
  
  public final void pullToRefresh() {
    if (this.mHeaderText != null)
      this.mHeaderText.setText(this.mPullLabel); 
    pullToRefreshImpl();
  }
  
  protected abstract void pullToRefreshImpl();
  
  public final void refreshing() {
    if (this.mHeaderText != null)
      this.mHeaderText.setText(this.mRefreshingLabel); 
    if (this.mUseIntrinsicAnimation) {
      ((AnimationDrawable)this.mHeaderImage.getDrawable()).start();
    } else {
      refreshingImpl();
    } 
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setVisibility(8); 
  }
  
  protected abstract void refreshingImpl();
  
  public final void releaseToRefresh() {
    if (this.mHeaderText != null)
      this.mHeaderText.setText(this.mReleaseLabel); 
    releaseToRefreshImpl();
  }
  
  protected abstract void releaseToRefreshImpl();
  
  public final void reset() {
    if (this.mHeaderText != null)
      this.mHeaderText.setText(this.mPullLabel); 
    this.mHeaderImage.setVisibility(0);
    if (this.mUseIntrinsicAnimation) {
      ((AnimationDrawable)this.mHeaderImage.getDrawable()).stop();
    } else {
      resetImpl();
    } 
    if (this.mSubHeaderText != null) {
      if (TextUtils.isEmpty(this.mSubHeaderText.getText())) {
        this.mSubHeaderText.setVisibility(8);
        return;
      } 
    } else {
      return;
    } 
    this.mSubHeaderText.setVisibility(0);
  }
  
  protected abstract void resetImpl();
  
  public final void setHeight(int paramInt) {
    (getLayoutParams()).height = paramInt;
    requestLayout();
  }
  
  public void setLastUpdatedLabel(CharSequence paramCharSequence) {
    setSubHeaderText(paramCharSequence);
  }
  
  public final void setLoadingDrawable(Drawable paramDrawable) {
    this.mHeaderImage.setImageDrawable(paramDrawable);
    this.mUseIntrinsicAnimation = paramDrawable instanceof AnimationDrawable;
    onLoadingDrawableSet(paramDrawable);
  }
  
  public void setPullLabel(CharSequence paramCharSequence) {
    this.mPullLabel = paramCharSequence;
  }
  
  public void setRefreshingLabel(CharSequence paramCharSequence) {
    this.mRefreshingLabel = paramCharSequence;
  }
  
  public void setReleaseLabel(CharSequence paramCharSequence) {
    this.mReleaseLabel = paramCharSequence;
  }
  
  public void setTextTypeface(Typeface paramTypeface) {
    this.mHeaderText.setTypeface(paramTypeface);
  }
  
  public final void setWidth(int paramInt) {
    (getLayoutParams()).width = paramInt;
    requestLayout();
  }
  
  public final void showInvisibleViews() {
    if (4 == this.mHeaderText.getVisibility())
      this.mHeaderText.setVisibility(0); 
    if (4 == this.mHeaderProgress.getVisibility())
      this.mHeaderProgress.setVisibility(0); 
    if (4 == this.mHeaderImage.getVisibility())
      this.mHeaderImage.setVisibility(0); 
    if (4 == this.mSubHeaderText.getVisibility())
      this.mSubHeaderText.setVisibility(0); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/LoadingLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */