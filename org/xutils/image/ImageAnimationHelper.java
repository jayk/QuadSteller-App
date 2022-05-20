package org.xutils.image;

import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import java.lang.reflect.Method;
import org.xutils.common.util.LogUtil;

public final class ImageAnimationHelper {
  private static final Method cloneMethod;
  
  static {
    Method method;
    try {
      method = Animation.class.getDeclaredMethod("clone", new Class[0]);
      method.setAccessible(true);
    } catch (Throwable throwable) {
      method = null;
      LogUtil.w(throwable.getMessage(), throwable);
    } 
    cloneMethod = method;
  }
  
  public static void animationDisplay(ImageView paramImageView, Drawable paramDrawable, Animation paramAnimation) {
    paramImageView.setImageDrawable(paramDrawable);
    if (cloneMethod != null && paramAnimation != null) {
      try {
        paramImageView.startAnimation((Animation)cloneMethod.invoke(paramAnimation, new Object[0]));
      } catch (Throwable throwable) {
        paramImageView.startAnimation(paramAnimation);
      } 
      return;
    } 
    paramImageView.startAnimation(paramAnimation);
  }
  
  public static void fadeInDisplay(ImageView paramImageView, Drawable paramDrawable) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
    alphaAnimation.setDuration(300L);
    alphaAnimation.setInterpolator((Interpolator)new DecelerateInterpolator());
    paramImageView.setImageDrawable(paramDrawable);
    paramImageView.startAnimation((Animation)alphaAnimation);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/ImageAnimationHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */