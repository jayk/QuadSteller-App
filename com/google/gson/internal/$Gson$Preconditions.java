package com.google.gson.internal;

public final class $Gson$Preconditions {
  public static void checkArgument(boolean paramBoolean) {
    if (!paramBoolean)
      throw new IllegalArgumentException(); 
  }
  
  public static <T> T checkNotNull(T paramT) {
    if (paramT == null)
      throw new NullPointerException(); 
    return paramT;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/$Gson$Preconditions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */