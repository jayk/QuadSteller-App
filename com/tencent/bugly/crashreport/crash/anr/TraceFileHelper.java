package com.tencent.bugly.crashreport.crash.anr;

import com.tencent.bugly.proguard.x;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TraceFileHelper {
  private static String a(BufferedReader paramBufferedReader) throws IOException {
    // Byte code:
    //   0: new java/lang/StringBuffer
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: iconst_0
    //   9: istore_2
    //   10: iload_2
    //   11: iconst_3
    //   12: if_icmpge -> 58
    //   15: aload_0
    //   16: invokevirtual readLine : ()Ljava/lang/String;
    //   19: astore_3
    //   20: aload_3
    //   21: ifnonnull -> 28
    //   24: aconst_null
    //   25: astore_0
    //   26: aload_0
    //   27: areturn
    //   28: aload_1
    //   29: new java/lang/StringBuilder
    //   32: dup
    //   33: invokespecial <init> : ()V
    //   36: aload_3
    //   37: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: ldc '\\n'
    //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: invokevirtual toString : ()Ljava/lang/String;
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   51: pop
    //   52: iinc #2, 1
    //   55: goto -> 10
    //   58: aload_1
    //   59: invokevirtual toString : ()Ljava/lang/String;
    //   62: astore_0
    //   63: goto -> 26
  }
  
  private static Object[] a(BufferedReader paramBufferedReader, Pattern... paramVarArgs) throws IOException {
    Object[] arrayOfObject1 = null;
    Object[] arrayOfObject2 = arrayOfObject1;
    if (paramBufferedReader != null) {
      if (paramVarArgs == null)
        return arrayOfObject1; 
    } else {
      return arrayOfObject2;
    } 
    while (true) {
      String str = paramBufferedReader.readLine();
      arrayOfObject2 = arrayOfObject1;
      if (str != null) {
        int i = paramVarArgs.length;
        for (byte b = 0; b < i; b++) {
          Pattern pattern = paramVarArgs[b];
          if (pattern.matcher(str).matches()) {
            arrayOfObject2 = new Object[2];
            arrayOfObject2[0] = pattern;
            arrayOfObject2[1] = str;
            return arrayOfObject2;
          } 
        } 
        continue;
      } 
      // Byte code: goto -> 14
    } 
  }
  
  private static String b(BufferedReader paramBufferedReader) throws IOException {
    StringBuffer stringBuffer = new StringBuffer();
    while (true) {
      String str = paramBufferedReader.readLine();
      if (str != null && str.trim().length() > 0) {
        stringBuffer.append(str + "\n");
        continue;
      } 
      break;
    } 
    return stringBuffer.toString();
  }
  
  public static a readFirstDumpInfo(String paramString, boolean paramBoolean) {
    String str = null;
    if (paramString == null) {
      x.e("path:%s", new Object[] { paramString });
      return (a)str;
    } 
    a a = new a();
    readTraceFile(paramString, new b(a, paramBoolean) {
          public final boolean a(long param1Long) {
            x.c("process end %d", new Object[] { Long.valueOf(param1Long) });
            return false;
          }
          
          public final boolean a(long param1Long1, long param1Long2, String param1String) {
            boolean bool = false;
            x.c("new process %s", new Object[] { param1String });
            this.a.a = param1Long1;
            this.a.b = param1String;
            this.a.c = param1Long2;
            if (this.b)
              bool = true; 
            return bool;
          }
          
          public final boolean a(String param1String1, int param1Int, String param1String2, String param1String3) {
            x.c("new thread %s", new Object[] { param1String1 });
            if (this.a.d == null)
              this.a.d = (Map)new HashMap<String, String>(); 
            this.a.d.put(param1String1, new String[] { param1String2, param1String3, param1Int });
            return true;
          }
        });
    if (a.a > 0L && a.c > 0L && a.b != null)
      return a; 
    x.e("first dump error %s", new Object[] { a.a + " " + a.c + " " + a.b });
    return (a)str;
  }
  
  public static a readTargetDumpInfo(String paramString1, String paramString2, boolean paramBoolean) {
    if (paramString1 == null || paramString2 == null)
      return null; 
    a a = new a();
    readTraceFile(paramString2, new b(a, paramBoolean) {
          public final boolean a(long param1Long) {
            boolean bool = false;
            x.c("process end %d", new Object[] { Long.valueOf(param1Long) });
            if (this.a.a <= 0L || this.a.c <= 0L || this.a.b == null)
              bool = true; 
            return bool;
          }
          
          public final boolean a(long param1Long1, long param1Long2, String param1String) {
            boolean bool = true;
            x.c("new process %s", new Object[] { param1String });
            if (param1String.equals(param1String)) {
              this.a.a = param1Long1;
              this.a.b = param1String;
              this.a.c = param1Long2;
              if (!this.b)
                bool = false; 
            } 
            return bool;
          }
          
          public final boolean a(String param1String1, int param1Int, String param1String2, String param1String3) {
            x.c("new thread %s", new Object[] { param1String1 });
            if (this.a.a > 0L && this.a.c > 0L && this.a.b != null) {
              if (this.a.d == null)
                this.a.d = (Map)new HashMap<String, String>(); 
              this.a.d.put(param1String1, new String[] { param1String2, param1String3, param1Int });
            } 
            return true;
          }
        });
    if (a.a > 0L && a.c > 0L) {
      a a1 = a;
      return (a.b == null) ? null : a1;
    } 
    return null;
  }
  
  public static void readTraceFile(String paramString, b paramb) {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 8
    //   4: aload_1
    //   5: ifnonnull -> 9
    //   8: return
    //   9: new java/io/File
    //   12: dup
    //   13: aload_0
    //   14: invokespecial <init> : (Ljava/lang/String;)V
    //   17: astore_2
    //   18: aload_2
    //   19: invokevirtual exists : ()Z
    //   22: ifeq -> 8
    //   25: aload_2
    //   26: invokevirtual lastModified : ()J
    //   29: pop2
    //   30: aload_2
    //   31: invokevirtual length : ()J
    //   34: pop2
    //   35: aconst_null
    //   36: astore_0
    //   37: new java/io/BufferedReader
    //   40: astore_3
    //   41: new java/io/FileReader
    //   44: astore #4
    //   46: aload #4
    //   48: aload_2
    //   49: invokespecial <init> : (Ljava/io/File;)V
    //   52: aload_3
    //   53: aload #4
    //   55: invokespecial <init> : (Ljava/io/Reader;)V
    //   58: ldc '-{5}\spid\s\d+\sat\s\d+-\d+-\d+\s\d{2}:\d{2}:\d{2}\s-{5}'
    //   60: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   63: astore_2
    //   64: ldc '-{5}\send\s\d+\s-{5}'
    //   66: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   69: astore #4
    //   71: ldc 'Cmd\sline:\s(\S+)'
    //   73: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   76: astore #5
    //   78: ldc '".+"\s(daemon\s){0,1}prio=\d+\stid=\d+\s.*'
    //   80: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   83: astore_0
    //   84: new java/text/SimpleDateFormat
    //   87: astore #6
    //   89: aload #6
    //   91: ldc 'yyyy-MM-dd HH:mm:ss'
    //   93: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   96: invokespecial <init> : (Ljava/lang/String;Ljava/util/Locale;)V
    //   99: aload_3
    //   100: iconst_1
    //   101: anewarray java/util/regex/Pattern
    //   104: dup
    //   105: iconst_0
    //   106: aload_2
    //   107: aastore
    //   108: invokestatic a : (Ljava/io/BufferedReader;[Ljava/util/regex/Pattern;)[Ljava/lang/Object;
    //   111: astore #7
    //   113: aload #7
    //   115: ifnull -> 589
    //   118: aload #7
    //   120: iconst_1
    //   121: aaload
    //   122: invokevirtual toString : ()Ljava/lang/String;
    //   125: ldc '\s'
    //   127: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   130: astore #7
    //   132: aload #7
    //   134: iconst_2
    //   135: aaload
    //   136: invokestatic parseLong : (Ljava/lang/String;)J
    //   139: lstore #8
    //   141: new java/lang/StringBuilder
    //   144: astore #10
    //   146: aload #10
    //   148: invokespecial <init> : ()V
    //   151: aload #6
    //   153: aload #10
    //   155: aload #7
    //   157: iconst_4
    //   158: aaload
    //   159: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: ldc ' '
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: aload #7
    //   169: iconst_5
    //   170: aaload
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: invokevirtual toString : ()Ljava/lang/String;
    //   177: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   180: invokevirtual getTime : ()J
    //   183: lstore #11
    //   185: aload_3
    //   186: iconst_1
    //   187: anewarray java/util/regex/Pattern
    //   190: dup
    //   191: iconst_0
    //   192: aload #5
    //   194: aastore
    //   195: invokestatic a : (Ljava/io/BufferedReader;[Ljava/util/regex/Pattern;)[Ljava/lang/Object;
    //   198: astore #7
    //   200: aload #7
    //   202: ifnonnull -> 227
    //   205: aload_3
    //   206: invokevirtual close : ()V
    //   209: goto -> 8
    //   212: astore_0
    //   213: aload_0
    //   214: invokestatic a : (Ljava/lang/Throwable;)Z
    //   217: ifne -> 8
    //   220: aload_0
    //   221: invokevirtual printStackTrace : ()V
    //   224: goto -> 8
    //   227: aload #5
    //   229: aload #7
    //   231: iconst_1
    //   232: aaload
    //   233: invokevirtual toString : ()Ljava/lang/String;
    //   236: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   239: astore #7
    //   241: aload #7
    //   243: invokevirtual find : ()Z
    //   246: pop
    //   247: aload #7
    //   249: iconst_1
    //   250: invokevirtual group : (I)Ljava/lang/String;
    //   253: pop
    //   254: aload_1
    //   255: lload #8
    //   257: lload #11
    //   259: aload #7
    //   261: iconst_1
    //   262: invokevirtual group : (I)Ljava/lang/String;
    //   265: invokeinterface a : (JJLjava/lang/String;)Z
    //   270: istore #13
    //   272: iload #13
    //   274: ifne -> 299
    //   277: aload_3
    //   278: invokevirtual close : ()V
    //   281: goto -> 8
    //   284: astore_0
    //   285: aload_0
    //   286: invokestatic a : (Ljava/lang/Throwable;)Z
    //   289: ifne -> 8
    //   292: aload_0
    //   293: invokevirtual printStackTrace : ()V
    //   296: goto -> 8
    //   299: aload_3
    //   300: iconst_2
    //   301: anewarray java/util/regex/Pattern
    //   304: dup
    //   305: iconst_0
    //   306: aload_0
    //   307: aastore
    //   308: dup
    //   309: iconst_1
    //   310: aload #4
    //   312: aastore
    //   313: invokestatic a : (Ljava/io/BufferedReader;[Ljava/util/regex/Pattern;)[Ljava/lang/Object;
    //   316: astore #7
    //   318: aload #7
    //   320: ifnull -> 99
    //   323: aload #7
    //   325: iconst_0
    //   326: aaload
    //   327: aload_0
    //   328: if_acmpne -> 537
    //   331: aload #7
    //   333: iconst_1
    //   334: aaload
    //   335: invokevirtual toString : ()Ljava/lang/String;
    //   338: astore #10
    //   340: ldc '".+"'
    //   342: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   345: aload #10
    //   347: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   350: astore #7
    //   352: aload #7
    //   354: invokevirtual find : ()Z
    //   357: pop
    //   358: aload #7
    //   360: invokevirtual group : ()Ljava/lang/String;
    //   363: astore #7
    //   365: aload #7
    //   367: iconst_1
    //   368: aload #7
    //   370: invokevirtual length : ()I
    //   373: iconst_1
    //   374: isub
    //   375: invokevirtual substring : (II)Ljava/lang/String;
    //   378: astore #7
    //   380: aload #10
    //   382: ldc 'NATIVE'
    //   384: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   387: pop
    //   388: ldc 'tid=\d+'
    //   390: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   393: aload #10
    //   395: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   398: astore #10
    //   400: aload #10
    //   402: invokevirtual find : ()Z
    //   405: pop
    //   406: aload #10
    //   408: invokevirtual group : ()Ljava/lang/String;
    //   411: astore #10
    //   413: aload_1
    //   414: aload #7
    //   416: aload #10
    //   418: aload #10
    //   420: ldc '='
    //   422: invokevirtual indexOf : (Ljava/lang/String;)I
    //   425: iconst_1
    //   426: iadd
    //   427: invokevirtual substring : (I)Ljava/lang/String;
    //   430: invokestatic parseInt : (Ljava/lang/String;)I
    //   433: aload_3
    //   434: invokestatic a : (Ljava/io/BufferedReader;)Ljava/lang/String;
    //   437: aload_3
    //   438: invokestatic b : (Ljava/io/BufferedReader;)Ljava/lang/String;
    //   441: invokeinterface a : (Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)Z
    //   446: pop
    //   447: goto -> 299
    //   450: astore_1
    //   451: aload_3
    //   452: astore_0
    //   453: aload_1
    //   454: invokestatic a : (Ljava/lang/Throwable;)Z
    //   457: ifne -> 464
    //   460: aload_1
    //   461: invokevirtual printStackTrace : ()V
    //   464: aload_1
    //   465: invokevirtual getClass : ()Ljava/lang/Class;
    //   468: invokevirtual getName : ()Ljava/lang/String;
    //   471: astore_3
    //   472: new java/lang/StringBuilder
    //   475: astore #4
    //   477: aload #4
    //   479: invokespecial <init> : ()V
    //   482: ldc 'trace open fail:%s : %s'
    //   484: iconst_2
    //   485: anewarray java/lang/Object
    //   488: dup
    //   489: iconst_0
    //   490: aload_3
    //   491: aastore
    //   492: dup
    //   493: iconst_1
    //   494: aload #4
    //   496: aload_1
    //   497: invokevirtual getMessage : ()Ljava/lang/String;
    //   500: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   503: invokevirtual toString : ()Ljava/lang/String;
    //   506: aastore
    //   507: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   510: pop
    //   511: aload_0
    //   512: ifnull -> 8
    //   515: aload_0
    //   516: invokevirtual close : ()V
    //   519: goto -> 8
    //   522: astore_0
    //   523: aload_0
    //   524: invokestatic a : (Ljava/lang/Throwable;)Z
    //   527: ifne -> 8
    //   530: aload_0
    //   531: invokevirtual printStackTrace : ()V
    //   534: goto -> 8
    //   537: aload_1
    //   538: aload #7
    //   540: iconst_1
    //   541: aaload
    //   542: invokevirtual toString : ()Ljava/lang/String;
    //   545: ldc '\s'
    //   547: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   550: iconst_2
    //   551: aaload
    //   552: invokestatic parseLong : (Ljava/lang/String;)J
    //   555: invokeinterface a : (J)Z
    //   560: istore #13
    //   562: iload #13
    //   564: ifne -> 99
    //   567: aload_3
    //   568: invokevirtual close : ()V
    //   571: goto -> 8
    //   574: astore_0
    //   575: aload_0
    //   576: invokestatic a : (Ljava/lang/Throwable;)Z
    //   579: ifne -> 8
    //   582: aload_0
    //   583: invokevirtual printStackTrace : ()V
    //   586: goto -> 8
    //   589: aload_3
    //   590: invokevirtual close : ()V
    //   593: goto -> 8
    //   596: astore_0
    //   597: aload_0
    //   598: invokestatic a : (Ljava/lang/Throwable;)Z
    //   601: ifne -> 8
    //   604: aload_0
    //   605: invokevirtual printStackTrace : ()V
    //   608: goto -> 8
    //   611: astore_0
    //   612: aconst_null
    //   613: astore_1
    //   614: aload_1
    //   615: ifnull -> 622
    //   618: aload_1
    //   619: invokevirtual close : ()V
    //   622: aload_0
    //   623: athrow
    //   624: astore_1
    //   625: aload_1
    //   626: invokestatic a : (Ljava/lang/Throwable;)Z
    //   629: ifne -> 622
    //   632: aload_1
    //   633: invokevirtual printStackTrace : ()V
    //   636: goto -> 622
    //   639: astore_0
    //   640: aload_3
    //   641: astore_1
    //   642: goto -> 614
    //   645: astore_1
    //   646: aload_0
    //   647: astore_3
    //   648: aload_1
    //   649: astore_0
    //   650: aload_3
    //   651: astore_1
    //   652: goto -> 614
    //   655: astore_1
    //   656: goto -> 453
    // Exception table:
    //   from	to	target	type
    //   37	58	655	java/lang/Exception
    //   37	58	611	finally
    //   58	99	450	java/lang/Exception
    //   58	99	639	finally
    //   99	113	450	java/lang/Exception
    //   99	113	639	finally
    //   118	200	450	java/lang/Exception
    //   118	200	639	finally
    //   205	209	212	java/io/IOException
    //   227	272	450	java/lang/Exception
    //   227	272	639	finally
    //   277	281	284	java/io/IOException
    //   299	318	450	java/lang/Exception
    //   299	318	639	finally
    //   331	447	450	java/lang/Exception
    //   331	447	639	finally
    //   453	464	645	finally
    //   464	511	645	finally
    //   515	519	522	java/io/IOException
    //   537	562	450	java/lang/Exception
    //   537	562	639	finally
    //   567	571	574	java/io/IOException
    //   589	593	596	java/io/IOException
    //   618	622	624	java/io/IOException
  }
  
  public static final class a {
    public long a;
    
    public String b;
    
    public long c;
    
    public Map<String, String[]> d;
  }
  
  public static interface b {
    boolean a(long param1Long);
    
    boolean a(long param1Long1, long param1Long2, String param1String);
    
    boolean a(String param1String1, int param1Int, String param1String2, String param1String3);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/anr/TraceFileHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */