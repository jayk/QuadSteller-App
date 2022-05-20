package org.greenrobot.greendao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface Property {
  String nameInDb() default "";
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/annotation/Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */