package app.lib.lockview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressLint({"NewApi"})
class Tweener {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "Tweener";
  
  private static Animator.AnimatorListener mCleanupListener;
  
  private static HashMap<Object, Tweener> sTweens = new HashMap<Object, Tweener>();
  
  ObjectAnimator animator;
  
  static {
    mCleanupListener = (Animator.AnimatorListener)new AnimatorListenerAdapter() {
        public void onAnimationCancel(Animator param1Animator) {
          Tweener.remove(param1Animator);
        }
        
        public void onAnimationEnd(Animator param1Animator) {
          Tweener.remove(param1Animator);
        }
      };
  }
  
  public Tweener(ObjectAnimator paramObjectAnimator) {
    this.animator = paramObjectAnimator;
  }
  
  private static void remove(Animator paramAnimator) {
    Iterator iterator = sTweens.entrySet().iterator();
    while (iterator.hasNext()) {
      if (((Tweener)((Map.Entry)iterator.next()).getValue()).animator == paramAnimator) {
        iterator.remove();
        break;
      } 
    } 
  }
  
  private static void replace(ArrayList<PropertyValuesHolder> paramArrayList, Object... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      Object object = paramVarArgs[b];
      object = sTweens.get(object);
      if (object != null) {
        ((Tweener)object).animator.cancel();
        if (paramArrayList != null) {
          ((Tweener)object).animator.setValues(paramArrayList.<PropertyValuesHolder>toArray(new PropertyValuesHolder[paramArrayList.size()]));
        } else {
          sTweens.remove(object);
        } 
      } 
    } 
  }
  
  public static void reset() {
    sTweens.clear();
  }
  
  public static Tweener to(Object paramObject, long paramLong, Object... paramVarArgs) {
    ObjectAnimator objectAnimator;
    long l = 0L;
    ValueAnimator.AnimatorUpdateListener animatorUpdateListener = null;
    Animator.AnimatorListener animatorListener = null;
    TimeInterpolator timeInterpolator = null;
    ArrayList<PropertyValuesHolder> arrayList = new ArrayList(paramVarArgs.length / 2);
    byte b = 0;
    while (b < paramVarArgs.length) {
      ValueAnimator.AnimatorUpdateListener animatorUpdateListener1;
      Animator.AnimatorListener animatorListener1;
      TimeInterpolator timeInterpolator1;
      long l1;
      if (!(paramVarArgs[b] instanceof String))
        throw new IllegalArgumentException("Key must be a string: " + paramVarArgs[b]); 
      String str = (String)paramVarArgs[b];
      Object object = paramVarArgs[b + 1];
      if ("simultaneousTween".equals(str)) {
        animatorUpdateListener1 = animatorUpdateListener;
        animatorListener1 = animatorListener;
        timeInterpolator1 = timeInterpolator;
        l1 = l;
      } else if ("ease".equals(str)) {
        timeInterpolator1 = (TimeInterpolator)object;
        l1 = l;
        animatorListener1 = animatorListener;
        animatorUpdateListener1 = animatorUpdateListener;
      } else if ("onUpdate".equals(str) || "onUpdateListener".equals(str)) {
        animatorUpdateListener1 = (ValueAnimator.AnimatorUpdateListener)object;
        l1 = l;
        timeInterpolator1 = timeInterpolator;
        animatorListener1 = animatorListener;
      } else if ("onComplete".equals(str) || "onCompleteListener".equals(str)) {
        animatorListener1 = (Animator.AnimatorListener)object;
        l1 = l;
        timeInterpolator1 = timeInterpolator;
        animatorUpdateListener1 = animatorUpdateListener;
      } else if ("delay".equals(str)) {
        l1 = ((Number)object).longValue();
        timeInterpolator1 = timeInterpolator;
        animatorListener1 = animatorListener;
        animatorUpdateListener1 = animatorUpdateListener;
      } else {
        l1 = l;
        timeInterpolator1 = timeInterpolator;
        animatorListener1 = animatorListener;
        animatorUpdateListener1 = animatorUpdateListener;
        if (!"syncWith".equals(str))
          if (object instanceof float[]) {
            arrayList.add(PropertyValuesHolder.ofFloat(str, new float[] { ((float[])object)[0], ((float[])object)[1] }));
            l1 = l;
            timeInterpolator1 = timeInterpolator;
            animatorListener1 = animatorListener;
            animatorUpdateListener1 = animatorUpdateListener;
          } else if (object instanceof int[]) {
            arrayList.add(PropertyValuesHolder.ofInt(str, new int[] { ((int[])object)[0], ((int[])object)[1] }));
            l1 = l;
            timeInterpolator1 = timeInterpolator;
            animatorListener1 = animatorListener;
            animatorUpdateListener1 = animatorUpdateListener;
          } else if (object instanceof Number) {
            arrayList.add(PropertyValuesHolder.ofFloat(str, new float[] { ((Number)object).floatValue() }));
            l1 = l;
            timeInterpolator1 = timeInterpolator;
            animatorListener1 = animatorListener;
            animatorUpdateListener1 = animatorUpdateListener;
          } else {
            throw new IllegalArgumentException("Bad argument for key \"" + str + "\" with value " + object.getClass());
          }  
      } 
      b += 2;
      l = l1;
      timeInterpolator = timeInterpolator1;
      animatorListener = animatorListener1;
      animatorUpdateListener = animatorUpdateListener1;
    } 
    Tweener tweener = sTweens.get(paramObject);
    if (tweener == null) {
      objectAnimator = ObjectAnimator.ofPropertyValuesHolder(paramObject, arrayList.<PropertyValuesHolder>toArray(new PropertyValuesHolder[arrayList.size()]));
      tweener = new Tweener(objectAnimator);
      sTweens.put(paramObject, tweener);
      paramObject = tweener;
    } else {
      objectAnimator = ((Tweener)sTweens.get(paramObject)).animator;
      replace(arrayList, new Object[] { paramObject });
      paramObject = tweener;
    } 
    if (timeInterpolator != null)
      objectAnimator.setInterpolator(timeInterpolator); 
    objectAnimator.setStartDelay(l);
    objectAnimator.setDuration(paramLong);
    if (animatorUpdateListener != null) {
      objectAnimator.removeAllUpdateListeners();
      objectAnimator.addUpdateListener(animatorUpdateListener);
    } 
    if (animatorListener != null) {
      objectAnimator.removeAllListeners();
      objectAnimator.addListener(animatorListener);
    } 
    objectAnimator.addListener(mCleanupListener);
    return (Tweener)paramObject;
  }
  
  Tweener from(Object paramObject, long paramLong, Object... paramVarArgs) {
    return to(paramObject, paramLong, paramVarArgs);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/lockview/Tweener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */