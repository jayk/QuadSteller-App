package butterknife;

import android.support.annotation.StringRes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface BindString {
  @StringRes
  int value();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/butterknife/BindString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */