package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.R;
import android.support.design.internal.SnackbarContentLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

public final class Snackbar extends BaseTransientBottomBar<Snackbar> {
  public static final int LENGTH_INDEFINITE = -2;
  
  public static final int LENGTH_LONG = 0;
  
  public static final int LENGTH_SHORT = -1;
  
  @Nullable
  private BaseTransientBottomBar.BaseCallback<Snackbar> mCallback;
  
  private Snackbar(ViewGroup paramViewGroup, View paramView, BaseTransientBottomBar.ContentViewCallback paramContentViewCallback) {
    super(paramViewGroup, paramView, paramContentViewCallback);
  }
  
  private static ViewGroup findSuitableParent(View paramView) {
    View view1 = null;
    View view2 = paramView;
    while (true) {
      ViewGroup viewGroup1;
      ViewParent viewParent2;
      if (view2 instanceof CoordinatorLayout)
        return (ViewGroup)view2; 
      paramView = view1;
      if (view2 instanceof android.widget.FrameLayout) {
        if (view2.getId() == 16908290)
          return (ViewGroup)view2; 
        viewGroup1 = (ViewGroup)view2;
      } 
      View view = view2;
      if (view2 != null) {
        viewParent2 = view2.getParent();
        if (viewParent2 instanceof View) {
          View view3 = (View)viewParent2;
        } else {
          viewParent2 = null;
        } 
      } 
      ViewGroup viewGroup2 = viewGroup1;
      ViewParent viewParent1 = viewParent2;
      if (viewParent2 == null)
        return viewGroup1; 
    } 
  }
  
  @NonNull
  public static Snackbar make(@NonNull View paramView, @StringRes int paramInt1, int paramInt2) {
    return make(paramView, paramView.getResources().getText(paramInt1), paramInt2);
  }
  
  @NonNull
  public static Snackbar make(@NonNull View paramView, @NonNull CharSequence paramCharSequence, int paramInt) {
    ViewGroup viewGroup = findSuitableParent(paramView);
    SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout)LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_layout_snackbar_include, viewGroup, false);
    Snackbar snackbar = new Snackbar(viewGroup, (View)snackbarContentLayout, (BaseTransientBottomBar.ContentViewCallback)snackbarContentLayout);
    snackbar.setText(paramCharSequence);
    snackbar.setDuration(paramInt);
    return snackbar;
  }
  
  @NonNull
  public Snackbar setAction(@StringRes int paramInt, View.OnClickListener paramOnClickListener) {
    return setAction(getContext().getText(paramInt), paramOnClickListener);
  }
  
  @NonNull
  public Snackbar setAction(CharSequence paramCharSequence, final View.OnClickListener listener) {
    Button button = ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView();
    if (TextUtils.isEmpty(paramCharSequence) || listener == null) {
      button.setVisibility(8);
      button.setOnClickListener(null);
      return this;
    } 
    button.setVisibility(0);
    button.setText(paramCharSequence);
    button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            listener.onClick(param1View);
            Snackbar.this.dispatchDismiss(1);
          }
        });
    return this;
  }
  
  @NonNull
  public Snackbar setActionTextColor(@ColorInt int paramInt) {
    ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView().setTextColor(paramInt);
    return this;
  }
  
  @NonNull
  public Snackbar setActionTextColor(ColorStateList paramColorStateList) {
    ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView().setTextColor(paramColorStateList);
    return this;
  }
  
  @Deprecated
  @NonNull
  public Snackbar setCallback(Callback paramCallback) {
    if (this.mCallback != null)
      removeCallback(this.mCallback); 
    if (paramCallback != null)
      addCallback(paramCallback); 
    this.mCallback = paramCallback;
    return this;
  }
  
  @NonNull
  public Snackbar setText(@StringRes int paramInt) {
    return setText(getContext().getText(paramInt));
  }
  
  @NonNull
  public Snackbar setText(@NonNull CharSequence paramCharSequence) {
    ((SnackbarContentLayout)this.mView.getChildAt(0)).getMessageView().setText(paramCharSequence);
    return this;
  }
  
  public static class Callback extends BaseTransientBottomBar.BaseCallback<Snackbar> {
    public static final int DISMISS_EVENT_ACTION = 1;
    
    public static final int DISMISS_EVENT_CONSECUTIVE = 4;
    
    public static final int DISMISS_EVENT_MANUAL = 3;
    
    public static final int DISMISS_EVENT_SWIPE = 0;
    
    public static final int DISMISS_EVENT_TIMEOUT = 2;
    
    public void onDismissed(Snackbar param1Snackbar, int param1Int) {}
    
    public void onShown(Snackbar param1Snackbar) {}
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static final class SnackbarLayout extends BaseTransientBottomBar.SnackbarBaseLayout {
    public SnackbarLayout(Context param1Context) {
      super(param1Context);
    }
    
    public SnackbarLayout(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/Snackbar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */