package com.blankj.utilcode.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MemoryConstants {
  public static final int BYTE = 1;
  
  public static final int GB = 1073741824;
  
  public static final int KB = 1024;
  
  public static final int MB = 1048576;
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Unit {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/constant/MemoryConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */