package okio;

public final class Utf8 {
  public static long size(String paramString) {
    return size(paramString, 0, paramString.length());
  }
  
  public static long size(String paramString, int paramInt1, int paramInt2) {
    if (paramString == null)
      throw new IllegalArgumentException("string == null"); 
    if (paramInt1 < 0)
      throw new IllegalArgumentException("beginIndex < 0: " + paramInt1); 
    if (paramInt2 < paramInt1)
      throw new IllegalArgumentException("endIndex < beginIndex: " + paramInt2 + " < " + paramInt1); 
    if (paramInt2 > paramString.length())
      throw new IllegalArgumentException("endIndex > string.length: " + paramInt2 + " > " + paramString.length()); 
    long l = 0L;
    while (paramInt1 < paramInt2) {
      byte b;
      char c = paramString.charAt(paramInt1);
      if (c < '') {
        l++;
        paramInt1++;
        continue;
      } 
      if (c < 'ࠀ') {
        l += 2L;
        paramInt1++;
        continue;
      } 
      if (c < '?' || c > '?') {
        l += 3L;
        paramInt1++;
        continue;
      } 
      if (paramInt1 + 1 < paramInt2) {
        b = paramString.charAt(paramInt1 + 1);
      } else {
        b = 0;
      } 
      if (c > '?' || b < '?' || b > '?') {
        l++;
        paramInt1++;
        continue;
      } 
      l += 4L;
      paramInt1 += 2;
    } 
    return l;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/Utf8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */