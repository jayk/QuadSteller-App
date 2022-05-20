package okhttp3.internal.http2;

import java.util.Arrays;

public final class Settings {
  static final int COUNT = 10;
  
  static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
  
  static final int ENABLE_PUSH = 2;
  
  static final int HEADER_TABLE_SIZE = 1;
  
  static final int INITIAL_WINDOW_SIZE = 7;
  
  static final int MAX_CONCURRENT_STREAMS = 4;
  
  static final int MAX_FRAME_SIZE = 5;
  
  static final int MAX_HEADER_LIST_SIZE = 6;
  
  private int set;
  
  private final int[] values = new int[10];
  
  void clear() {
    this.set = 0;
    Arrays.fill(this.values, 0);
  }
  
  int get(int paramInt) {
    return this.values[paramInt];
  }
  
  boolean getEnablePush(boolean paramBoolean) {
    boolean bool2;
    boolean bool1 = true;
    if ((this.set & 0x4) != 0) {
      bool2 = this.values[2];
    } else if (paramBoolean) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    return (bool2 == true) ? bool1 : false;
  }
  
  int getHeaderTableSize() {
    return ((this.set & 0x2) != 0) ? this.values[1] : -1;
  }
  
  int getInitialWindowSize() {
    return ((this.set & 0x80) != 0) ? this.values[7] : 65535;
  }
  
  int getMaxConcurrentStreams(int paramInt) {
    if ((this.set & 0x10) != 0)
      paramInt = this.values[4]; 
    return paramInt;
  }
  
  int getMaxFrameSize(int paramInt) {
    if ((this.set & 0x20) != 0)
      paramInt = this.values[5]; 
    return paramInt;
  }
  
  int getMaxHeaderListSize(int paramInt) {
    if ((this.set & 0x40) != 0)
      paramInt = this.values[6]; 
    return paramInt;
  }
  
  boolean isSet(int paramInt) {
    boolean bool = true;
    if ((this.set & 1 << paramInt) == 0)
      bool = false; 
    return bool;
  }
  
  void merge(Settings paramSettings) {
    for (byte b = 0; b < 10; b++) {
      if (paramSettings.isSet(b))
        set(b, paramSettings.get(b)); 
    } 
  }
  
  Settings set(int paramInt1, int paramInt2) {
    if (paramInt1 < this.values.length) {
      this.set |= 1 << paramInt1;
      this.values[paramInt1] = paramInt2;
    } 
    return this;
  }
  
  int size() {
    return Integer.bitCount(this.set);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/Settings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */