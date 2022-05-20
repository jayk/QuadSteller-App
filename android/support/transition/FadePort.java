package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class FadePort extends VisibilityPort {
  private static boolean DBG = false;
  
  public static final int IN = 1;
  
  private static final String LOG_TAG = "Fade";
  
  public static final int OUT = 2;
  
  private static final String PROPNAME_SCREEN_X = "android:fade:screenX";
  
  private static final String PROPNAME_SCREEN_Y = "android:fade:screenY";
  
  private int mFadingMode;
  
  public FadePort() {
    this(3);
  }
  
  public FadePort(int paramInt) {
    this.mFadingMode = paramInt;
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    int[] arrayOfInt = new int[2];
    paramTransitionValues.view.getLocationOnScreen(arrayOfInt);
    paramTransitionValues.values.put("android:fade:screenX", Integer.valueOf(arrayOfInt[0]));
    paramTransitionValues.values.put("android:fade:screenY", Integer.valueOf(arrayOfInt[1]));
  }
  
  private Animator createAnimation(View paramView, float paramFloat1, float paramFloat2, AnimatorListenerAdapter paramAnimatorListenerAdapter) {
    View view = null;
    if (paramFloat1 == paramFloat2) {
      paramView = view;
      if (paramAnimatorListenerAdapter != null) {
        paramAnimatorListenerAdapter.onAnimationEnd(null);
        paramView = view;
      } 
      return (Animator)paramView;
    } 
    ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(paramView, "alpha", new float[] { paramFloat1, paramFloat2 });
    if (DBG)
      Log.d("Fade", "Created animator " + objectAnimator2); 
    ObjectAnimator objectAnimator1 = objectAnimator2;
    if (paramAnimatorListenerAdapter != null) {
      objectAnimator2.addListener((Animator.AnimatorListener)paramAnimatorListenerAdapter);
      objectAnimator1 = objectAnimator2;
    } 
    return (Animator)objectAnimator1;
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    super.captureStartValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    ViewGroup viewGroup = null;
    paramViewGroup = viewGroup;
    if ((this.mFadingMode & 0x1) == 1) {
      if (paramTransitionValues2 == null)
        return (Animator)viewGroup; 
    } else {
      return (Animator)paramViewGroup;
    } 
    final View endView = paramTransitionValues2.view;
    if (DBG) {
      if (paramTransitionValues1 != null) {
        View view1 = paramTransitionValues1.view;
      } else {
        paramViewGroup = null;
      } 
      Log.d("Fade", "Fade.onAppear: startView, startVis, endView, endVis = " + paramViewGroup + ", " + paramInt1 + ", " + view + ", " + paramInt2);
    } 
    view.setAlpha(0.0F);
    addListener(new TransitionPort.TransitionListenerAdapter() {
          boolean mCanceled = false;
          
          float mPausedAlpha;
          
          public void onTransitionCancel(TransitionPort param1TransitionPort) {
            endView.setAlpha(1.0F);
            this.mCanceled = true;
          }
          
          public void onTransitionEnd(TransitionPort param1TransitionPort) {
            if (!this.mCanceled)
              endView.setAlpha(1.0F); 
          }
          
          public void onTransitionPause(TransitionPort param1TransitionPort) {
            this.mPausedAlpha = endView.getAlpha();
            endView.setAlpha(1.0F);
          }
          
          public void onTransitionResume(TransitionPort param1TransitionPort) {
            endView.setAlpha(this.mPausedAlpha);
          }
        });
    return createAnimation(view, 0.0F, 1.0F, (AnimatorListenerAdapter)null);
  }
  
  public Animator onDisappear(final ViewGroup finalSceneRoot, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, final int finalVisibility) {
    final String finalView;
    String str2;
    final String finalOverlayView;
    final View finalViewToKeep;
    if ((this.mFadingMode & 0x2) != 2)
      return null; 
    TransitionValues transitionValues = null;
    if (paramTransitionValues1 != null) {
      str2 = (String)paramTransitionValues1.view;
    } else {
      str2 = null;
    } 
    if (paramTransitionValues2 != null) {
      View view = paramTransitionValues2.view;
    } else {
      paramTransitionValues2 = null;
    } 
    if (DBG)
      Log.d("Fade", "Fade.onDisappear: startView, startVis, endView, endVis = " + str2 + ", " + paramInt1 + ", " + paramTransitionValues2 + ", " + finalVisibility); 
    String str3 = null;
    View view1 = null;
    if (paramTransitionValues2 == null || paramTransitionValues2.getParent() == null) {
      if (paramTransitionValues2 != null) {
        TransitionValues transitionValues1 = paramTransitionValues2;
        view2 = view1;
      } else {
        str4 = str3;
        paramTransitionValues2 = transitionValues;
        view2 = view1;
        if (str2 != null)
          if (str2.getParent() == null) {
            str4 = str2;
            str1 = str2;
            view2 = view1;
          } else {
            str4 = str3;
            paramTransitionValues2 = transitionValues;
            view2 = view1;
            if (str2.getParent() instanceof View) {
              str4 = str3;
              paramTransitionValues2 = transitionValues;
              view2 = view1;
              if (str2.getParent().getParent() == null) {
                paramInt1 = ((View)str2.getParent()).getId();
                str4 = str3;
                paramTransitionValues2 = transitionValues;
                view2 = view1;
                if (paramInt1 != -1) {
                  str4 = str3;
                  paramTransitionValues2 = transitionValues;
                  view2 = view1;
                  if (finalSceneRoot.findViewById(paramInt1) != null) {
                    str4 = str3;
                    paramTransitionValues2 = transitionValues;
                    view2 = view1;
                    if (this.mCanRemoveViews) {
                      str4 = str2;
                      str1 = str2;
                      view2 = view1;
                    } 
                  } 
                } 
              } 
            } 
          }  
      } 
    } else if (finalVisibility == 4) {
      view2 = (View)str1;
      str4 = str3;
    } else if (str2 == str1) {
      view2 = (View)str1;
      str4 = str3;
    } else {
      str1 = str2;
      str4 = str1;
      view2 = view1;
    } 
    if (str4 != null) {
      int i = ((Integer)paramTransitionValues1.values.get("android:fade:screenX")).intValue();
      paramInt1 = ((Integer)paramTransitionValues1.values.get("android:fade:screenY")).intValue();
      int[] arrayOfInt = new int[2];
      finalSceneRoot.getLocationOnScreen(arrayOfInt);
      ViewCompat.offsetLeftAndRight((View)str4, i - arrayOfInt[0] - str4.getLeft());
      ViewCompat.offsetTopAndBottom((View)str4, paramInt1 - arrayOfInt[1] - str4.getTop());
      ViewGroupOverlay.createFrom(finalSceneRoot).add((View)str4);
      return createAnimation((View)str1, 1.0F, 0.0F, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator param1Animator) {
              finalView.setAlpha(1.0F);
              if (finalViewToKeep != null)
                finalViewToKeep.setVisibility(finalVisibility); 
              if (finalOverlayView != null)
                ViewGroupOverlay.createFrom(finalSceneRoot).remove(finalOverlayView); 
            }
          });
    } 
    if (view2 != null) {
      view2.setVisibility(0);
      return createAnimation((View)str1, 1.0F, 0.0F, new AnimatorListenerAdapter() {
            boolean mCanceled = false;
            
            float mPausedAlpha = -1.0F;
            
            public void onAnimationCancel(Animator param1Animator) {
              this.mCanceled = true;
              if (this.mPausedAlpha >= 0.0F)
                finalView.setAlpha(this.mPausedAlpha); 
            }
            
            public void onAnimationEnd(Animator param1Animator) {
              if (!this.mCanceled)
                finalView.setAlpha(1.0F); 
              if (finalViewToKeep != null && !this.mCanceled)
                finalViewToKeep.setVisibility(finalVisibility); 
              if (finalOverlayView != null)
                ViewGroupOverlay.createFrom(finalSceneRoot).add(finalOverlayView); 
            }
          });
    } 
    return null;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/FadePort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */