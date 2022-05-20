package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@TargetApi(14)
@RequiresApi(14)
class ViewOverlay {
  protected OverlayViewGroup mOverlayViewGroup;
  
  ViewOverlay(Context paramContext, ViewGroup paramViewGroup, View paramView) {
    this.mOverlayViewGroup = new OverlayViewGroup(paramContext, paramViewGroup, paramView, this);
  }
  
  public static ViewOverlay createFrom(View paramView) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic getContentView : (Landroid/view/View;)Landroid/view/ViewGroup;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnull -> 70
    //   9: aload_1
    //   10: invokevirtual getChildCount : ()I
    //   13: istore_2
    //   14: iconst_0
    //   15: istore_3
    //   16: iload_3
    //   17: iload_2
    //   18: if_icmpge -> 53
    //   21: aload_1
    //   22: iload_3
    //   23: invokevirtual getChildAt : (I)Landroid/view/View;
    //   26: astore #4
    //   28: aload #4
    //   30: instanceof android/support/transition/ViewOverlay$OverlayViewGroup
    //   33: ifeq -> 47
    //   36: aload #4
    //   38: checkcast android/support/transition/ViewOverlay$OverlayViewGroup
    //   41: getfield mViewOverlay : Landroid/support/transition/ViewOverlay;
    //   44: astore_0
    //   45: aload_0
    //   46: areturn
    //   47: iinc #3, 1
    //   50: goto -> 16
    //   53: new android/support/transition/ViewGroupOverlay
    //   56: dup
    //   57: aload_1
    //   58: invokevirtual getContext : ()Landroid/content/Context;
    //   61: aload_1
    //   62: aload_0
    //   63: invokespecial <init> : (Landroid/content/Context;Landroid/view/ViewGroup;Landroid/view/View;)V
    //   66: astore_0
    //   67: goto -> 45
    //   70: aconst_null
    //   71: astore_0
    //   72: goto -> 45
  }
  
  static ViewGroup getContentView(View paramView) {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 48
    //   4: aload_0
    //   5: invokevirtual getId : ()I
    //   8: ldc 16908290
    //   10: if_icmpne -> 27
    //   13: aload_0
    //   14: instanceof android/view/ViewGroup
    //   17: ifeq -> 27
    //   20: aload_0
    //   21: checkcast android/view/ViewGroup
    //   24: astore_0
    //   25: aload_0
    //   26: areturn
    //   27: aload_0
    //   28: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   31: instanceof android/view/ViewGroup
    //   34: ifeq -> 0
    //   37: aload_0
    //   38: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   41: checkcast android/view/ViewGroup
    //   44: astore_0
    //   45: goto -> 0
    //   48: aconst_null
    //   49: astore_0
    //   50: goto -> 25
  }
  
  public void add(Drawable paramDrawable) {
    this.mOverlayViewGroup.add(paramDrawable);
  }
  
  public void clear() {
    this.mOverlayViewGroup.clear();
  }
  
  ViewGroup getOverlayView() {
    return this.mOverlayViewGroup;
  }
  
  boolean isEmpty() {
    return this.mOverlayViewGroup.isEmpty();
  }
  
  public void remove(Drawable paramDrawable) {
    this.mOverlayViewGroup.remove(paramDrawable);
  }
  
  static class OverlayViewGroup extends ViewGroup {
    static Method sInvalidateChildInParentFastMethod;
    
    ArrayList<Drawable> mDrawables = null;
    
    ViewGroup mHostView;
    
    View mRequestingView;
    
    ViewOverlay mViewOverlay;
    
    static {
      try {
        sInvalidateChildInParentFastMethod = ViewGroup.class.getDeclaredMethod("invalidateChildInParentFast", new Class[] { int.class, int.class, Rect.class });
      } catch (NoSuchMethodException noSuchMethodException) {}
    }
    
    OverlayViewGroup(Context param1Context, ViewGroup param1ViewGroup, View param1View, ViewOverlay param1ViewOverlay) {
      super(param1Context);
      this.mHostView = param1ViewGroup;
      this.mRequestingView = param1View;
      setRight(param1ViewGroup.getWidth());
      setBottom(param1ViewGroup.getHeight());
      param1ViewGroup.addView((View)this);
      this.mViewOverlay = param1ViewOverlay;
    }
    
    private void getOffset(int[] param1ArrayOfint) {
      int[] arrayOfInt1 = new int[2];
      int[] arrayOfInt2 = new int[2];
      ViewGroup viewGroup = (ViewGroup)getParent();
      this.mHostView.getLocationOnScreen(arrayOfInt1);
      this.mRequestingView.getLocationOnScreen(arrayOfInt2);
      param1ArrayOfint[0] = arrayOfInt2[0] - arrayOfInt1[0];
      param1ArrayOfint[1] = arrayOfInt2[1] - arrayOfInt1[1];
    }
    
    public void add(Drawable param1Drawable) {
      if (this.mDrawables == null)
        this.mDrawables = new ArrayList<Drawable>(); 
      if (!this.mDrawables.contains(param1Drawable)) {
        this.mDrawables.add(param1Drawable);
        invalidate(param1Drawable.getBounds());
        param1Drawable.setCallback((Drawable.Callback)this);
      } 
    }
    
    public void add(View param1View) {
      if (param1View.getParent() instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)param1View.getParent();
        if (viewGroup != this.mHostView && viewGroup.getParent() != null) {
          int[] arrayOfInt1 = new int[2];
          int[] arrayOfInt2 = new int[2];
          viewGroup.getLocationOnScreen(arrayOfInt1);
          this.mHostView.getLocationOnScreen(arrayOfInt2);
          ViewCompat.offsetLeftAndRight(param1View, arrayOfInt1[0] - arrayOfInt2[0]);
          ViewCompat.offsetTopAndBottom(param1View, arrayOfInt1[1] - arrayOfInt2[1]);
        } 
        viewGroup.removeView(param1View);
        if (param1View.getParent() != null)
          viewGroup.removeView(param1View); 
      } 
      addView(param1View, getChildCount() - 1);
    }
    
    public void clear() {
      removeAllViews();
      if (this.mDrawables != null)
        this.mDrawables.clear(); 
    }
    
    protected void dispatchDraw(Canvas param1Canvas) {
      int i = 0;
      int[] arrayOfInt1 = new int[2];
      int[] arrayOfInt2 = new int[2];
      ViewGroup viewGroup = (ViewGroup)getParent();
      this.mHostView.getLocationOnScreen(arrayOfInt1);
      this.mRequestingView.getLocationOnScreen(arrayOfInt2);
      param1Canvas.translate((arrayOfInt2[0] - arrayOfInt1[0]), (arrayOfInt2[1] - arrayOfInt1[1]));
      param1Canvas.clipRect(new Rect(0, 0, this.mRequestingView.getWidth(), this.mRequestingView.getHeight()));
      super.dispatchDraw(param1Canvas);
      if (this.mDrawables != null)
        i = this.mDrawables.size(); 
      for (byte b = 0; b < i; b++)
        ((Drawable)this.mDrawables.get(b)).draw(param1Canvas); 
    }
    
    public boolean dispatchTouchEvent(MotionEvent param1MotionEvent) {
      return false;
    }
    
    public void invalidateChildFast(View param1View, Rect param1Rect) {
      if (this.mHostView != null) {
        int i = param1View.getLeft();
        int j = param1View.getTop();
        int[] arrayOfInt = new int[2];
        getOffset(arrayOfInt);
        param1Rect.offset(arrayOfInt[0] + i, arrayOfInt[1] + j);
        this.mHostView.invalidate(param1Rect);
      } 
    }
    
    public ViewParent invalidateChildInParent(int[] param1ArrayOfint, Rect param1Rect) {
      if (this.mHostView != null) {
        param1Rect.offset(param1ArrayOfint[0], param1ArrayOfint[1]);
        if (this.mHostView instanceof ViewGroup) {
          param1ArrayOfint[0] = 0;
          param1ArrayOfint[1] = 0;
          int[] arrayOfInt = new int[2];
          getOffset(arrayOfInt);
          param1Rect.offset(arrayOfInt[0], arrayOfInt[1]);
          return super.invalidateChildInParent(param1ArrayOfint, param1Rect);
        } 
        invalidate(param1Rect);
      } 
      return null;
    }
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    protected ViewParent invalidateChildInParentFast(int param1Int1, int param1Int2, Rect param1Rect) {
      if (this.mHostView instanceof ViewGroup && sInvalidateChildInParentFastMethod != null)
        try {
          getOffset(new int[2]);
          sInvalidateChildInParentFastMethod.invoke(this.mHostView, new Object[] { Integer.valueOf(param1Int1), Integer.valueOf(param1Int2), param1Rect });
        } catch (IllegalAccessException illegalAccessException) {
          illegalAccessException.printStackTrace();
        } catch (InvocationTargetException invocationTargetException) {
          invocationTargetException.printStackTrace();
        }  
      return null;
    }
    
    public void invalidateDrawable(Drawable param1Drawable) {
      invalidate(param1Drawable.getBounds());
    }
    
    boolean isEmpty() {
      return (getChildCount() == 0 && (this.mDrawables == null || this.mDrawables.size() == 0));
    }
    
    protected void onLayout(boolean param1Boolean, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void remove(Drawable param1Drawable) {
      if (this.mDrawables != null) {
        this.mDrawables.remove(param1Drawable);
        invalidate(param1Drawable.getBounds());
        param1Drawable.setCallback(null);
      } 
    }
    
    public void remove(View param1View) {
      removeView(param1View);
      if (isEmpty())
        this.mHostView.removeView((View)this); 
    }
    
    protected boolean verifyDrawable(Drawable param1Drawable) {
      return (super.verifyDrawable(param1Drawable) || (this.mDrawables != null && this.mDrawables.contains(param1Drawable)));
    }
    
    static class TouchInterceptor extends View {
      TouchInterceptor(Context param2Context) {
        super(param2Context);
      }
    }
  }
  
  static class TouchInterceptor extends View {
    TouchInterceptor(Context param1Context) {
      super(param1Context);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/ViewOverlay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */