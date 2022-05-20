package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.appcompat.R;
import android.support.v7.text.AllCapsTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

@TargetApi(9)
@RequiresApi(9)
class AppCompatTextHelper {
  private TintInfo mDrawableBottomTint;
  
  private TintInfo mDrawableLeftTint;
  
  private TintInfo mDrawableRightTint;
  
  private TintInfo mDrawableTopTint;
  
  final TextView mView;
  
  AppCompatTextHelper(TextView paramTextView) {
    this.mView = paramTextView;
  }
  
  static AppCompatTextHelper create(TextView paramTextView) {
    return (Build.VERSION.SDK_INT >= 17) ? new AppCompatTextHelperV17(paramTextView) : new AppCompatTextHelper(paramTextView);
  }
  
  protected static TintInfo createTintInfo(Context paramContext, AppCompatDrawableManager paramAppCompatDrawableManager, int paramInt) {
    ColorStateList colorStateList = paramAppCompatDrawableManager.getTintList(paramContext, paramInt);
    if (colorStateList != null) {
      TintInfo tintInfo = new TintInfo();
      tintInfo.mHasTintList = true;
      tintInfo.mTintList = colorStateList;
      return tintInfo;
    } 
    return null;
  }
  
  final void applyCompoundDrawableTint(Drawable paramDrawable, TintInfo paramTintInfo) {
    if (paramDrawable != null && paramTintInfo != null)
      AppCompatDrawableManager.tintDrawable(paramDrawable, paramTintInfo, this.mView.getDrawableState()); 
  }
  
  void applyCompoundDrawablesTints() {
    if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawables();
      applyCompoundDrawableTint(arrayOfDrawable[0], this.mDrawableLeftTint);
      applyCompoundDrawableTint(arrayOfDrawable[1], this.mDrawableTopTint);
      applyCompoundDrawableTint(arrayOfDrawable[2], this.mDrawableRightTint);
      applyCompoundDrawableTint(arrayOfDrawable[3], this.mDrawableBottomTint);
    } 
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    ColorStateList colorStateList2;
    ColorStateList colorStateList3;
    Context context = this.mView.getContext();
    AppCompatDrawableManager appCompatDrawableManager1 = AppCompatDrawableManager.get();
    TintTypedArray tintTypedArray1 = TintTypedArray.obtainStyledAttributes(context, paramAttributeSet, R.styleable.AppCompatTextHelper, paramInt, 0);
    int i = tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
    if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft))
      this.mDrawableLeftTint = createTintInfo(context, appCompatDrawableManager1, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0)); 
    if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop))
      this.mDrawableTopTint = createTintInfo(context, appCompatDrawableManager1, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0)); 
    if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight))
      this.mDrawableRightTint = createTintInfo(context, appCompatDrawableManager1, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0)); 
    if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom))
      this.mDrawableBottomTint = createTintInfo(context, appCompatDrawableManager1, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0)); 
    tintTypedArray1.recycle();
    boolean bool1 = this.mView.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    TintTypedArray tintTypedArray2 = null;
    tintTypedArray1 = null;
    AppCompatDrawableManager appCompatDrawableManager2 = null;
    appCompatDrawableManager1 = null;
    TintTypedArray tintTypedArray3 = null;
    if (i != -1) {
      ColorStateList colorStateList;
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, i, R.styleable.TextAppearance);
      bool2 = bool3;
      bool4 = bool5;
      if (!bool1) {
        bool2 = bool3;
        bool4 = bool5;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
          bool4 = true;
          bool2 = tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        } 
      } 
      tintTypedArray1 = tintTypedArray2;
      tintTypedArray2 = tintTypedArray3;
      if (Build.VERSION.SDK_INT < 23) {
        ColorStateList colorStateList5;
        appCompatDrawableManager1 = appCompatDrawableManager2;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor))
          colorStateList5 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor); 
        colorStateList3 = colorStateList5;
        tintTypedArray2 = tintTypedArray3;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
          colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
          colorStateList3 = colorStateList5;
        } 
      } 
      tintTypedArray.recycle();
      colorStateList2 = colorStateList;
    } 
    tintTypedArray3 = TintTypedArray.obtainStyledAttributes(context, paramAttributeSet, R.styleable.TextAppearance, paramInt, 0);
    bool3 = bool2;
    paramInt = bool4;
    if (!bool1) {
      bool3 = bool2;
      paramInt = bool4;
      if (tintTypedArray3.hasValue(R.styleable.TextAppearance_textAllCaps)) {
        paramInt = 1;
        bool3 = tintTypedArray3.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
      } 
    } 
    ColorStateList colorStateList4 = colorStateList3;
    ColorStateList colorStateList1 = colorStateList2;
    if (Build.VERSION.SDK_INT < 23) {
      if (tintTypedArray3.hasValue(R.styleable.TextAppearance_android_textColor))
        colorStateList3 = tintTypedArray3.getColorStateList(R.styleable.TextAppearance_android_textColor); 
      colorStateList4 = colorStateList3;
      colorStateList1 = colorStateList2;
      if (tintTypedArray3.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
        colorStateList1 = tintTypedArray3.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
        colorStateList4 = colorStateList3;
      } 
    } 
    tintTypedArray3.recycle();
    if (colorStateList4 != null)
      this.mView.setTextColor(colorStateList4); 
    if (colorStateList1 != null)
      this.mView.setHintTextColor(colorStateList1); 
    if (!bool1 && paramInt != 0)
      setAllCaps(bool3); 
  }
  
  void onSetTextAppearance(Context paramContext, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramInt, R.styleable.TextAppearance);
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps))
      setAllCaps(tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false)); 
    if (Build.VERSION.SDK_INT < 23 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
      ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
      if (colorStateList != null)
        this.mView.setTextColor(colorStateList); 
    } 
    tintTypedArray.recycle();
  }
  
  void setAllCaps(boolean paramBoolean) {
    TransformationMethod transformationMethod;
    TextView textView = this.mView;
    if (paramBoolean) {
      transformationMethod = (TransformationMethod)new AllCapsTransformationMethod(this.mView.getContext());
    } else {
      transformationMethod = null;
    } 
    textView.setTransformationMethod(transformationMethod);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/AppCompatTextHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */