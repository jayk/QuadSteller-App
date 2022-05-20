package okhttp3.internal.ws;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;

final class WebSocketReader {
  boolean closed;
  
  long frameBytesRead;
  
  final FrameCallback frameCallback;
  
  long frameLength;
  
  final boolean isClient;
  
  boolean isControlFrame;
  
  boolean isFinalFrame;
  
  boolean isMasked;
  
  final byte[] maskBuffer = new byte[8192];
  
  final byte[] maskKey = new byte[4];
  
  int opcode;
  
  final BufferedSource source;
  
  WebSocketReader(boolean paramBoolean, BufferedSource paramBufferedSource, FrameCallback paramFrameCallback) {
    if (paramBufferedSource == null)
      throw new NullPointerException("source == null"); 
    if (paramFrameCallback == null)
      throw new NullPointerException("frameCallback == null"); 
    this.isClient = paramBoolean;
    this.source = paramBufferedSource;
    this.frameCallback = paramFrameCallback;
  }
  
  private void readControlFrame() throws IOException {
    // Byte code:
    //   0: new okio/Buffer
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: aload_0
    //   9: getfield frameBytesRead : J
    //   12: aload_0
    //   13: getfield frameLength : J
    //   16: lcmp
    //   17: ifge -> 41
    //   20: aload_0
    //   21: getfield isClient : Z
    //   24: ifeq -> 144
    //   27: aload_0
    //   28: getfield source : Lokio/BufferedSource;
    //   31: aload_1
    //   32: aload_0
    //   33: getfield frameLength : J
    //   36: invokeinterface readFully : (Lokio/Buffer;J)V
    //   41: aload_0
    //   42: getfield opcode : I
    //   45: tableswitch default -> 72, 8 -> 235, 9 -> 205, 10 -> 219
    //   72: new java/net/ProtocolException
    //   75: dup
    //   76: new java/lang/StringBuilder
    //   79: dup
    //   80: invokespecial <init> : ()V
    //   83: ldc 'Unknown control opcode: '
    //   85: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: aload_0
    //   89: getfield opcode : I
    //   92: invokestatic toHexString : (I)Ljava/lang/String;
    //   95: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: invokevirtual toString : ()Ljava/lang/String;
    //   101: invokespecial <init> : (Ljava/lang/String;)V
    //   104: athrow
    //   105: aload_0
    //   106: getfield maskBuffer : [B
    //   109: iload_2
    //   110: i2l
    //   111: aload_0
    //   112: getfield maskKey : [B
    //   115: aload_0
    //   116: getfield frameBytesRead : J
    //   119: invokestatic toggleMask : ([BJ[BJ)V
    //   122: aload_1
    //   123: aload_0
    //   124: getfield maskBuffer : [B
    //   127: iconst_0
    //   128: iload_2
    //   129: invokevirtual write : ([BII)Lokio/Buffer;
    //   132: pop
    //   133: aload_0
    //   134: aload_0
    //   135: getfield frameBytesRead : J
    //   138: iload_2
    //   139: i2l
    //   140: ladd
    //   141: putfield frameBytesRead : J
    //   144: aload_0
    //   145: getfield frameBytesRead : J
    //   148: aload_0
    //   149: getfield frameLength : J
    //   152: lcmp
    //   153: ifge -> 41
    //   156: aload_0
    //   157: getfield frameLength : J
    //   160: aload_0
    //   161: getfield frameBytesRead : J
    //   164: lsub
    //   165: aload_0
    //   166: getfield maskBuffer : [B
    //   169: arraylength
    //   170: i2l
    //   171: invokestatic min : (JJ)J
    //   174: l2i
    //   175: istore_2
    //   176: aload_0
    //   177: getfield source : Lokio/BufferedSource;
    //   180: aload_0
    //   181: getfield maskBuffer : [B
    //   184: iconst_0
    //   185: iload_2
    //   186: invokeinterface read : ([BII)I
    //   191: istore_2
    //   192: iload_2
    //   193: iconst_m1
    //   194: if_icmpne -> 105
    //   197: new java/io/EOFException
    //   200: dup
    //   201: invokespecial <init> : ()V
    //   204: athrow
    //   205: aload_0
    //   206: getfield frameCallback : Lokhttp3/internal/ws/WebSocketReader$FrameCallback;
    //   209: aload_1
    //   210: invokevirtual readByteString : ()Lokio/ByteString;
    //   213: invokeinterface onReadPing : (Lokio/ByteString;)V
    //   218: return
    //   219: aload_0
    //   220: getfield frameCallback : Lokhttp3/internal/ws/WebSocketReader$FrameCallback;
    //   223: aload_1
    //   224: invokevirtual readByteString : ()Lokio/ByteString;
    //   227: invokeinterface onReadPong : (Lokio/ByteString;)V
    //   232: goto -> 218
    //   235: sipush #1005
    //   238: istore_2
    //   239: ldc ''
    //   241: astore_3
    //   242: aload_1
    //   243: invokevirtual size : ()J
    //   246: lstore #4
    //   248: lload #4
    //   250: lconst_1
    //   251: lcmp
    //   252: ifne -> 265
    //   255: new java/net/ProtocolException
    //   258: dup
    //   259: ldc 'Malformed close payload length of 1.'
    //   261: invokespecial <init> : (Ljava/lang/String;)V
    //   264: athrow
    //   265: lload #4
    //   267: lconst_0
    //   268: lcmp
    //   269: ifeq -> 300
    //   272: aload_1
    //   273: invokevirtual readShort : ()S
    //   276: istore_2
    //   277: aload_1
    //   278: invokevirtual readUtf8 : ()Ljava/lang/String;
    //   281: astore_3
    //   282: iload_2
    //   283: invokestatic closeCodeExceptionMessage : (I)Ljava/lang/String;
    //   286: astore_1
    //   287: aload_1
    //   288: ifnull -> 300
    //   291: new java/net/ProtocolException
    //   294: dup
    //   295: aload_1
    //   296: invokespecial <init> : (Ljava/lang/String;)V
    //   299: athrow
    //   300: aload_0
    //   301: getfield frameCallback : Lokhttp3/internal/ws/WebSocketReader$FrameCallback;
    //   304: iload_2
    //   305: aload_3
    //   306: invokeinterface onReadClose : (ILjava/lang/String;)V
    //   311: aload_0
    //   312: iconst_1
    //   313: putfield closed : Z
    //   316: goto -> 218
  }
  
  private void readHeader() throws IOException {
    int j;
    boolean bool2;
    boolean bool3;
    boolean bool1 = true;
    if (this.closed)
      throw new IOException("closed"); 
    long l = this.source.timeout().timeoutNanos();
    this.source.timeout().clearTimeout();
    try {
      i = this.source.readByte();
      j = i & 0xFF;
      this.source.timeout().timeout(l, TimeUnit.NANOSECONDS);
      this.opcode = j & 0xF;
      if ((j & 0x80) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.isFinalFrame = bool2;
      if ((j & 0x8) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.isControlFrame = bool2;
    } finally {
      this.source.timeout().timeout(l, TimeUnit.NANOSECONDS);
    } 
    if ((j & 0x40) != 0) {
      i = 1;
    } else {
      i = 0;
    } 
    if ((j & 0x20) != 0) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    if ((j & 0x10) != 0) {
      j = 1;
    } else {
      j = 0;
    } 
    if (i || bool3 || j != 0)
      throw new ProtocolException("Reserved flags are unsupported."); 
    int i = this.source.readByte() & 0xFF;
    if ((i & 0x80) != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    this.isMasked = bool2;
    if (this.isMasked == this.isClient) {
      if (this.isClient) {
        String str1 = "Server-sent frames must not be masked.";
        throw new ProtocolException(str1);
      } 
      String str = "Client-sent frames must be masked.";
      throw new ProtocolException(str);
    } 
    this.frameLength = (i & 0x7F);
    if (this.frameLength == 126L) {
      this.frameLength = this.source.readShort() & 0xFFFFL;
    } else if (this.frameLength == 127L) {
      this.frameLength = this.source.readLong();
      if (this.frameLength < 0L)
        throw new ProtocolException("Frame length 0x" + Long.toHexString(this.frameLength) + " > 0x7FFFFFFFFFFFFFFF"); 
    } 
    this.frameBytesRead = 0L;
    if (this.isControlFrame && this.frameLength > 125L)
      throw new ProtocolException("Control frame must be less than 125B."); 
    if (this.isMasked)
      this.source.readFully(this.maskKey); 
  }
  
  private void readMessage(Buffer paramBuffer) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield closed : Z
    //   4: ifeq -> 17
    //   7: new java/io/IOException
    //   10: dup
    //   11: ldc 'closed'
    //   13: invokespecial <init> : (Ljava/lang/String;)V
    //   16: athrow
    //   17: aload_0
    //   18: getfield frameBytesRead : J
    //   21: aload_0
    //   22: getfield frameLength : J
    //   25: lcmp
    //   26: ifne -> 97
    //   29: aload_0
    //   30: getfield isFinalFrame : Z
    //   33: ifeq -> 37
    //   36: return
    //   37: aload_0
    //   38: invokevirtual readUntilNonControlFrame : ()V
    //   41: aload_0
    //   42: getfield opcode : I
    //   45: ifeq -> 81
    //   48: new java/net/ProtocolException
    //   51: dup
    //   52: new java/lang/StringBuilder
    //   55: dup
    //   56: invokespecial <init> : ()V
    //   59: ldc 'Expected continuation opcode. Got: '
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: aload_0
    //   65: getfield opcode : I
    //   68: invokestatic toHexString : (I)Ljava/lang/String;
    //   71: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: invokevirtual toString : ()Ljava/lang/String;
    //   77: invokespecial <init> : (Ljava/lang/String;)V
    //   80: athrow
    //   81: aload_0
    //   82: getfield isFinalFrame : Z
    //   85: ifeq -> 97
    //   88: aload_0
    //   89: getfield frameLength : J
    //   92: lconst_0
    //   93: lcmp
    //   94: ifeq -> 36
    //   97: aload_0
    //   98: getfield frameLength : J
    //   101: aload_0
    //   102: getfield frameBytesRead : J
    //   105: lsub
    //   106: lstore_2
    //   107: aload_0
    //   108: getfield isMasked : Z
    //   111: ifeq -> 200
    //   114: lload_2
    //   115: aload_0
    //   116: getfield maskBuffer : [B
    //   119: arraylength
    //   120: i2l
    //   121: invokestatic min : (JJ)J
    //   124: lstore_2
    //   125: aload_0
    //   126: getfield source : Lokio/BufferedSource;
    //   129: aload_0
    //   130: getfield maskBuffer : [B
    //   133: iconst_0
    //   134: lload_2
    //   135: l2i
    //   136: invokeinterface read : ([BII)I
    //   141: i2l
    //   142: lstore_2
    //   143: lload_2
    //   144: ldc2_w -1
    //   147: lcmp
    //   148: ifne -> 159
    //   151: new java/io/EOFException
    //   154: dup
    //   155: invokespecial <init> : ()V
    //   158: athrow
    //   159: aload_0
    //   160: getfield maskBuffer : [B
    //   163: lload_2
    //   164: aload_0
    //   165: getfield maskKey : [B
    //   168: aload_0
    //   169: getfield frameBytesRead : J
    //   172: invokestatic toggleMask : ([BJ[BJ)V
    //   175: aload_1
    //   176: aload_0
    //   177: getfield maskBuffer : [B
    //   180: iconst_0
    //   181: lload_2
    //   182: l2i
    //   183: invokevirtual write : ([BII)Lokio/Buffer;
    //   186: pop
    //   187: aload_0
    //   188: aload_0
    //   189: getfield frameBytesRead : J
    //   192: lload_2
    //   193: ladd
    //   194: putfield frameBytesRead : J
    //   197: goto -> 0
    //   200: aload_0
    //   201: getfield source : Lokio/BufferedSource;
    //   204: aload_1
    //   205: lload_2
    //   206: invokeinterface read : (Lokio/Buffer;J)J
    //   211: lstore #4
    //   213: lload #4
    //   215: lstore_2
    //   216: lload #4
    //   218: ldc2_w -1
    //   221: lcmp
    //   222: ifne -> 187
    //   225: new java/io/EOFException
    //   228: dup
    //   229: invokespecial <init> : ()V
    //   232: athrow
  }
  
  private void readMessageFrame() throws IOException {
    int i = this.opcode;
    if (i != 1 && i != 2)
      throw new ProtocolException("Unknown opcode: " + Integer.toHexString(i)); 
    Buffer buffer = new Buffer();
    readMessage(buffer);
    if (i == 1) {
      this.frameCallback.onReadMessage(buffer.readUtf8());
      return;
    } 
    this.frameCallback.onReadMessage(buffer.readByteString());
  }
  
  void processNextFrame() throws IOException {
    readHeader();
    if (this.isControlFrame) {
      readControlFrame();
      return;
    } 
    readMessageFrame();
  }
  
  void readUntilNonControlFrame() throws IOException {
    while (true) {
      if (!this.closed) {
        readHeader();
        if (this.isControlFrame) {
          readControlFrame();
          continue;
        } 
      } 
      return;
    } 
  }
  
  public static interface FrameCallback {
    void onReadClose(int param1Int, String param1String);
    
    void onReadMessage(String param1String) throws IOException;
    
    void onReadMessage(ByteString param1ByteString) throws IOException;
    
    void onReadPing(ByteString param1ByteString);
    
    void onReadPong(ByteString param1ByteString);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/ws/WebSocketReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */