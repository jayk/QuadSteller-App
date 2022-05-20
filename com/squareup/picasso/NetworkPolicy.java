package com.squareup.picasso;

public enum NetworkPolicy {
  NO_CACHE(1),
  NO_STORE(2),
  OFFLINE(4);
  
  final int index;
  
  static {
    $VALUES = new NetworkPolicy[] { NO_CACHE, NO_STORE, OFFLINE };
  }
  
  NetworkPolicy(int paramInt1) {
    this.index = paramInt1;
  }
  
  public static boolean isOfflineOnly(int paramInt) {
    return ((OFFLINE.index & paramInt) != 0);
  }
  
  public static boolean shouldReadFromDiskCache(int paramInt) {
    return ((NO_CACHE.index & paramInt) == 0);
  }
  
  public static boolean shouldWriteToDiskCache(int paramInt) {
    return ((NO_STORE.index & paramInt) == 0);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/NetworkPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */