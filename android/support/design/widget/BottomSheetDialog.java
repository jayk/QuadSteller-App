package android.support.design.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.R;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class BottomSheetDialog extends AppCompatDialog {
  private BottomSheetBehavior<FrameLayout> mBehavior;
  
  private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
      public void onSlide(@NonNull View param1View, float param1Float) {}
      
      public void onStateChanged(@NonNull View param1View, int param1Int) {
        if (param1Int == 5)
          BottomSheetDialog.this.cancel(); 
      }
    };
  
  boolean mCancelable = true;
  
  private boolean mCanceledOnTouchOutside = true;
  
  private boolean mCanceledOnTouchOutsideSet;
  
  public BottomSheetDialog(@NonNull Context paramContext) {
    this(paramContext, 0);
  }
  
  public BottomSheetDialog(@NonNull Context paramContext, @StyleRes int paramInt) {
    super(paramContext, getThemeResId(paramContext, paramInt));
    supportRequestWindowFeature(1);
  }
  
  protected BottomSheetDialog(@NonNull Context paramContext, boolean paramBoolean, DialogInterface.OnCancelListener paramOnCancelListener) {
    super(paramContext, paramBoolean, paramOnCancelListener);
    supportRequestWindowFeature(1);
    this.mCancelable = paramBoolean;
  }
  
  private static int getThemeResId(Context paramContext, int paramInt) {
    null = paramInt;
    if (paramInt == 0) {
      TypedValue typedValue = new TypedValue();
      if (paramContext.getTheme().resolveAttribute(R.attr.bottomSheetDialogTheme, typedValue, true))
        return typedValue.resourceId; 
    } else {
      return null;
    } 
    return R.style.Theme_Design_Light_BottomSheetDialog;
  }
  
  private View wrapInBottomSheet(int paramInt, View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)View.inflate(getContext(), R.layout.design_bottom_sheet_dialog, null);
    View view = paramView;
    if (paramInt != 0) {
      view = paramView;
      if (paramView == null)
        view = getLayoutInflater().inflate(paramInt, coordinatorLayout, false); 
    } 
    FrameLayout frameLayout = (FrameLayout)coordinatorLayout.findViewById(R.id.design_bottom_sheet);
    this.mBehavior = BottomSheetBehavior.from(frameLayout);
    this.mBehavior.setBottomSheetCallback(this.mBottomSheetCallback);
    this.mBehavior.setHideable(this.mCancelable);
    if (paramLayoutParams == null) {
      frameLayout.addView(view);
      coordinatorLayout.findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              if (BottomSheetDialog.this.mCancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside())
                BottomSheetDialog.this.cancel(); 
            }
          });
      ViewCompat.setAccessibilityDelegate((View)frameLayout, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
              super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
              if (BottomSheetDialog.this.mCancelable) {
                param1AccessibilityNodeInfoCompat.addAction(1048576);
                param1AccessibilityNodeInfoCompat.setDismissable(true);
                return;
              } 
              param1AccessibilityNodeInfoCompat.setDismissable(false);
            }
            
            public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
              if (param1Int == 1048576 && BottomSheetDialog.this.mCancelable) {
                BottomSheetDialog.this.cancel();
                return true;
              } 
              return super.performAccessibilityAction(param1View, param1Int, param1Bundle);
            }
          });
      return (View)coordinatorLayout;
    } 
    frameLayout.addView(view, paramLayoutParams);
    coordinatorLayout.findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (BottomSheetDialog.this.mCancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside())
              BottomSheetDialog.this.cancel(); 
          }
        });
    ViewCompat.setAccessibilityDelegate((View)frameLayout, new AccessibilityDelegateCompat() {
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            if (BottomSheetDialog.this.mCancelable) {
              param1AccessibilityNodeInfoCompat.addAction(1048576);
              param1AccessibilityNodeInfoCompat.setDismissable(true);
              return;
            } 
            param1AccessibilityNodeInfoCompat.setDismissable(false);
          }
          
          public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
            if (param1Int == 1048576 && BottomSheetDialog.this.mCancelable) {
              BottomSheetDialog.this.cancel();
              return true;
            } 
            return super.performAccessibilityAction(param1View, param1Int, param1Bundle);
          }
        });
    return (View)coordinatorLayout;
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    getWindow().setLayout(-1, -1);
  }
  
  public void setCancelable(boolean paramBoolean) {
    super.setCancelable(paramBoolean);
    if (this.mCancelable != paramBoolean) {
      this.mCancelable = paramBoolean;
      if (this.mBehavior != null)
        this.mBehavior.setHideable(paramBoolean); 
    } 
  }
  
  public void setCanceledOnTouchOutside(boolean paramBoolean) {
    super.setCanceledOnTouchOutside(paramBoolean);
    if (paramBoolean && !this.mCancelable)
      this.mCancelable = true; 
    this.mCanceledOnTouchOutside = paramBoolean;
    this.mCanceledOnTouchOutsideSet = true;
  }
  
  public void setContentView(@LayoutRes int paramInt) {
    super.setContentView(wrapInBottomSheet(paramInt, (View)null, (ViewGroup.LayoutParams)null));
  }
  
  public void setContentView(View paramView) {
    super.setContentView(wrapInBottomSheet(0, paramView, (ViewGroup.LayoutParams)null));
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    super.setContentView(wrapInBottomSheet(0, paramView, paramLayoutParams));
  }
  
  boolean shouldWindowCloseOnTouchOutside() {
    if (!this.mCanceledOnTouchOutsideSet) {
      if (Build.VERSION.SDK_INT < 11) {
        this.mCanceledOnTouchOutside = true;
      } else {
        TypedArray typedArray = getContext().obtainStyledAttributes(new int[] { 16843611 });
        this.mCanceledOnTouchOutside = typedArray.getBoolean(0, true);
        typedArray.recycle();
      } 
      this.mCanceledOnTouchOutsideSet = true;
    } 
    return this.mCanceledOnTouchOutside;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/BottomSheetDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */