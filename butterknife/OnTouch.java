package butterknife;

import android.support.annotation.IdRes;
import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(method = {@ListenerMethod(defaultReturn = "false", name = "onTouch", parameters = {"android.view.View", "android.view.MotionEvent"}, returnType = "boolean")}, setter = "setOnTouchListener", targetType = "android.view.View", type = "android.view.View.OnTouchListener")
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface OnTouch {
  @IdRes
  int[] value() default {-1};
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/butterknife/OnTouch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */