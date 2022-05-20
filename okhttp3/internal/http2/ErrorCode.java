package okhttp3.internal.http2;

public enum ErrorCode {
  CANCEL,
  FLOW_CONTROL_ERROR,
  INTERNAL_ERROR,
  NO_ERROR(0),
  PROTOCOL_ERROR(1),
  REFUSED_STREAM(1);
  
  public final int httpCode;
  
  static {
    INTERNAL_ERROR = new ErrorCode("INTERNAL_ERROR", 2, 2);
    FLOW_CONTROL_ERROR = new ErrorCode("FLOW_CONTROL_ERROR", 3, 3);
    REFUSED_STREAM = new ErrorCode("REFUSED_STREAM", 4, 7);
    CANCEL = new ErrorCode("CANCEL", 5, 8);
    $VALUES = new ErrorCode[] { NO_ERROR, PROTOCOL_ERROR, INTERNAL_ERROR, FLOW_CONTROL_ERROR, REFUSED_STREAM, CANCEL };
  }
  
  ErrorCode(int paramInt1) {
    this.httpCode = paramInt1;
  }
  
  public static ErrorCode fromHttp2(int paramInt) {
    // Byte code:
    //   0: invokestatic values : ()[Lokhttp3/internal/http2/ErrorCode;
    //   3: astore_1
    //   4: aload_1
    //   5: arraylength
    //   6: istore_2
    //   7: iconst_0
    //   8: istore_3
    //   9: iload_3
    //   10: iload_2
    //   11: if_icmpge -> 37
    //   14: aload_1
    //   15: iload_3
    //   16: aaload
    //   17: astore #4
    //   19: aload #4
    //   21: getfield httpCode : I
    //   24: iload_0
    //   25: if_icmpne -> 31
    //   28: aload #4
    //   30: areturn
    //   31: iinc #3, 1
    //   34: goto -> 9
    //   37: aconst_null
    //   38: astore #4
    //   40: goto -> 28
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/ErrorCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */