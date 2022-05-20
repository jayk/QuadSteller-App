package com.blankj.utilcode.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class TimeConstants {
  public static final int DAY = 86400000;
  
  public static final int HOUR = 3600000;
  
  public static final int MIN = 60000;
  
  public static final int MSEC = 1;
  
  public static final int SEC = 1000;
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Unit {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/constant/TimeConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */