package butterknife;

import android.support.annotation.AnimRes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface BindAnim {
  @AnimRes
  int value();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/butterknife/BindAnim.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */