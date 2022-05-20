package org.greenrobot.eventbus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Subscribe {
  int priority() default 0;
  
  boolean sticky() default false;
  
  ThreadMode threadMode() default ThreadMode.POSTING;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/Subscribe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */