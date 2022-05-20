package com.blankj.utilcode.util;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public final class SnackbarUtils {
  private static final int COLOR_DEFAULT = -16777217;
  
  private static final int COLOR_ERROR = -65536;
  
  private static final int COLOR_MESSAGE = -1;
  
  private static final int COLOR_SUCCESS = -13912576;
  
  private static final int COLOR_WARNING = -16128;
  
  public static final int LENGTH_INDEFINITE = -2;
  
  public static final int LENGTH_LONG = 0;
  
  public static final int LENGTH_SHORT = -1;
  
  private static WeakReference<Snackbar> snackbarWeakReference;
  
  private View.OnClickListener actionListener;
  
  private CharSequence actionText;
  
  private int actionTextColor;
  
  private int bgColor;
  
  private int bgResource;
  
  private int bottomMargin;
  
  private int duration;
  
  private CharSequence message;
  
  private int messageColor;
  
  private View parent;
  
  private SnackbarUtils(View paramView) {
    setDefault();
    this.parent = paramView;
  }
  
  public static void addView(@LayoutRes int paramInt, @NonNull ViewGroup.LayoutParams paramLayoutParams) {
    View view = getView();
    if (view != null) {
      view.setPadding(0, 0, 0, 0);
      ((Snackbar.SnackbarLayout)view).addView(LayoutInflater.from(view.getContext()).inflate(paramInt, null), -1, paramLayoutParams);
    } 
  }
  
  public static void addView(@NonNull View paramView, @NonNull ViewGroup.LayoutParams paramLayoutParams) {
    View view = getView();
    if (view != null) {
      view.setPadding(0, 0, 0, 0);
      ((Snackbar.SnackbarLayout)view).addView(paramView, paramLayoutParams);
    } 
  }
  
  public static void dismiss() {
    if (snackbarWeakReference != null && snackbarWeakReference.get() != null) {
      ((Snackbar)snackbarWeakReference.get()).dismiss();
      snackbarWeakReference = null;
    } 
  }
  
  public static View getView() {
    Snackbar snackbar = snackbarWeakReference.get();
    return (snackbar == null) ? null : snackbar.getView();
  }
  
  private void setDefault() {
    this.message = "";
    this.messageColor = -16777217;
    this.bgColor = -16777217;
    this.bgResource = -1;
    this.duration = -1;
    this.actionText = "";
    this.actionTextColor = -16777217;
    this.bottomMargin = 0;
  }
  
  public static SnackbarUtils with(@NonNull View paramView) {
    return new SnackbarUtils(paramView);
  }
  
  public SnackbarUtils setAction(@NonNull CharSequence paramCharSequence, @ColorInt int paramInt, @NonNull View.OnClickListener paramOnClickListener) {
    this.actionText = paramCharSequence;
    this.actionTextColor = paramInt;
    this.actionListener = paramOnClickListener;
    return this;
  }
  
  public SnackbarUtils setAction(@NonNull CharSequence paramCharSequence, @NonNull View.OnClickListener paramOnClickListener) {
    return setAction(paramCharSequence, -16777217, paramOnClickListener);
  }
  
  public SnackbarUtils setBgColor(@ColorInt int paramInt) {
    this.bgColor = paramInt;
    return this;
  }
  
  public SnackbarUtils setBgResource(@DrawableRes int paramInt) {
    this.bgResource = paramInt;
    return this;
  }
  
  public SnackbarUtils setBottomMargin(@IntRange(from = 1L) int paramInt) {
    this.bottomMargin = paramInt;
    return this;
  }
  
  public SnackbarUtils setDuration(int paramInt) {
    this.duration = paramInt;
    return this;
  }
  
  public SnackbarUtils setMessage(@NonNull CharSequence paramCharSequence) {
    this.message = paramCharSequence;
    return this;
  }
  
  public SnackbarUtils setMessageColor(@ColorInt int paramInt) {
    this.messageColor = paramInt;
    return this;
  }
  
  public void show() {
    View view = this.parent;
    if (view != null) {
      if (this.messageColor != -16777217) {
        SpannableString spannableString = new SpannableString(this.message);
        spannableString.setSpan(new ForegroundColorSpan(this.messageColor), 0, spannableString.length(), 33);
        snackbarWeakReference = new WeakReference<Snackbar>(Snackbar.make(view, (CharSequence)spannableString, this.duration));
      } else {
        snackbarWeakReference = new WeakReference<Snackbar>(Snackbar.make(view, this.message, this.duration));
      } 
      Snackbar snackbar = snackbarWeakReference.get();
      view = snackbar.getView();
      if (this.bgResource != -1) {
        view.setBackgroundResource(this.bgResource);
      } else if (this.bgColor != -16777217) {
        view.setBackgroundColor(this.bgColor);
      } 
      if (this.bottomMargin != 0)
        ((ViewGroup.MarginLayoutParams)view.getLayoutParams()).bottomMargin = this.bottomMargin; 
      if (this.actionText.length() > 0 && this.actionListener != null) {
        if (this.actionTextColor != -16777217)
          snackbar.setActionTextColor(this.actionTextColor); 
        snackbar.setAction(this.actionText, this.actionListener);
      } 
      snackbar.show();
    } 
  }
  
  public void showError() {
    this.bgColor = -65536;
    this.messageColor = -1;
    this.actionTextColor = -1;
    show();
  }
  
  public void showSuccess() {
    this.bgColor = -13912576;
    this.messageColor = -1;
    this.actionTextColor = -1;
    show();
  }
  
  public void showWarning() {
    this.bgColor = -16128;
    this.messageColor = -1;
    this.actionTextColor = -1;
    show();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Duration {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/SnackbarUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */