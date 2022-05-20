package org.xutils.db.sqlite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WhereBuilder {
  private final List<String> whereItems = new ArrayList<String>();
  
  private void appendCondition(String paramString1, String paramString2, String paramString3, Object paramObject) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #5
    //   9: aload_0
    //   10: getfield whereItems : Ljava/util/List;
    //   13: invokeinterface size : ()I
    //   18: ifle -> 29
    //   21: aload #5
    //   23: ldc ' '
    //   25: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload_1
    //   30: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   33: ifne -> 48
    //   36: aload #5
    //   38: aload_1
    //   39: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: ldc ' '
    //   44: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: pop
    //   48: aload #5
    //   50: ldc '"'
    //   52: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: aload_2
    //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: ldc '"'
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: pop
    //   65: ldc '!='
    //   67: aload_3
    //   68: invokevirtual equals : (Ljava/lang/Object;)Z
    //   71: ifeq -> 115
    //   74: ldc '<>'
    //   76: astore_1
    //   77: aload #4
    //   79: ifnonnull -> 172
    //   82: ldc '='
    //   84: aload_1
    //   85: invokevirtual equals : (Ljava/lang/Object;)Z
    //   88: ifeq -> 132
    //   91: aload #5
    //   93: ldc ' IS NULL'
    //   95: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: aload_0
    //   100: getfield whereItems : Ljava/util/List;
    //   103: aload #5
    //   105: invokevirtual toString : ()Ljava/lang/String;
    //   108: invokeinterface add : (Ljava/lang/Object;)Z
    //   113: pop
    //   114: return
    //   115: aload_3
    //   116: astore_1
    //   117: ldc '=='
    //   119: aload_3
    //   120: invokevirtual equals : (Ljava/lang/Object;)Z
    //   123: ifeq -> 77
    //   126: ldc '='
    //   128: astore_1
    //   129: goto -> 77
    //   132: ldc '<>'
    //   134: aload_1
    //   135: invokevirtual equals : (Ljava/lang/Object;)Z
    //   138: ifeq -> 152
    //   141: aload #5
    //   143: ldc ' IS NOT NULL'
    //   145: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: goto -> 99
    //   152: aload #5
    //   154: ldc ' '
    //   156: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: aload_1
    //   160: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: ldc ' NULL'
    //   165: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: pop
    //   169: goto -> 99
    //   172: aload #5
    //   174: ldc ' '
    //   176: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: aload_1
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: ldc ' '
    //   185: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: pop
    //   189: ldc 'IN'
    //   191: aload_1
    //   192: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   195: ifeq -> 436
    //   198: aconst_null
    //   199: astore_1
    //   200: aload #4
    //   202: instanceof java/lang/Iterable
    //   205: ifeq -> 325
    //   208: aload #4
    //   210: checkcast java/lang/Iterable
    //   213: astore_1
    //   214: aload_1
    //   215: ifnull -> 426
    //   218: new java/lang/StringBuilder
    //   221: dup
    //   222: ldc '('
    //   224: invokespecial <init> : (Ljava/lang/String;)V
    //   227: astore_3
    //   228: aload_1
    //   229: invokeinterface iterator : ()Ljava/util/Iterator;
    //   234: astore #4
    //   236: aload #4
    //   238: invokeinterface hasNext : ()Z
    //   243: ifeq -> 395
    //   246: aload #4
    //   248: invokeinterface next : ()Ljava/lang/Object;
    //   253: invokestatic convert2DbValueIfNeeded : (Ljava/lang/Object;)Ljava/lang/Object;
    //   256: astore_1
    //   257: getstatic org/xutils/db/sqlite/ColumnDbType.TEXT : Lorg/xutils/db/sqlite/ColumnDbType;
    //   260: aload_1
    //   261: invokevirtual getClass : ()Ljava/lang/Class;
    //   264: invokestatic getDbColumnType : (Ljava/lang/Class;)Lorg/xutils/db/sqlite/ColumnDbType;
    //   267: invokevirtual equals : (Ljava/lang/Object;)Z
    //   270: ifeq -> 386
    //   273: aload_1
    //   274: invokevirtual toString : ()Ljava/lang/String;
    //   277: astore_2
    //   278: aload_2
    //   279: astore_1
    //   280: aload_2
    //   281: bipush #39
    //   283: invokevirtual indexOf : (I)I
    //   286: iconst_m1
    //   287: if_icmpeq -> 299
    //   290: aload_2
    //   291: ldc '''
    //   293: ldc ''''
    //   295: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   298: astore_1
    //   299: aload_3
    //   300: ldc '''
    //   302: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   305: aload_1
    //   306: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   309: ldc '''
    //   311: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   314: pop
    //   315: aload_3
    //   316: ldc ','
    //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   321: pop
    //   322: goto -> 236
    //   325: aload #4
    //   327: invokevirtual getClass : ()Ljava/lang/Class;
    //   330: invokevirtual isArray : ()Z
    //   333: ifeq -> 214
    //   336: aload #4
    //   338: invokestatic getLength : (Ljava/lang/Object;)I
    //   341: istore #6
    //   343: new java/util/ArrayList
    //   346: dup
    //   347: iload #6
    //   349: invokespecial <init> : (I)V
    //   352: astore_1
    //   353: iconst_0
    //   354: istore #7
    //   356: iload #7
    //   358: iload #6
    //   360: if_icmpge -> 383
    //   363: aload_1
    //   364: aload #4
    //   366: iload #7
    //   368: invokestatic get : (Ljava/lang/Object;I)Ljava/lang/Object;
    //   371: invokeinterface add : (Ljava/lang/Object;)Z
    //   376: pop
    //   377: iinc #7, 1
    //   380: goto -> 356
    //   383: goto -> 214
    //   386: aload_3
    //   387: aload_1
    //   388: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   391: pop
    //   392: goto -> 315
    //   395: aload_3
    //   396: aload_3
    //   397: invokevirtual length : ()I
    //   400: iconst_1
    //   401: isub
    //   402: invokevirtual deleteCharAt : (I)Ljava/lang/StringBuilder;
    //   405: pop
    //   406: aload_3
    //   407: ldc ')'
    //   409: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   412: pop
    //   413: aload #5
    //   415: aload_3
    //   416: invokevirtual toString : ()Ljava/lang/String;
    //   419: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   422: pop
    //   423: goto -> 99
    //   426: new java/lang/IllegalArgumentException
    //   429: dup
    //   430: ldc 'value must be an Array or an Iterable.'
    //   432: invokespecial <init> : (Ljava/lang/String;)V
    //   435: athrow
    //   436: ldc 'BETWEEN'
    //   438: aload_1
    //   439: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   442: ifeq -> 743
    //   445: aconst_null
    //   446: astore_1
    //   447: aload #4
    //   449: instanceof java/lang/Iterable
    //   452: ifeq -> 491
    //   455: aload #4
    //   457: checkcast java/lang/Iterable
    //   460: astore_1
    //   461: aload_1
    //   462: ifnull -> 733
    //   465: aload_1
    //   466: invokeinterface iterator : ()Ljava/util/Iterator;
    //   471: astore_2
    //   472: aload_2
    //   473: invokeinterface hasNext : ()Z
    //   478: ifne -> 552
    //   481: new java/lang/IllegalArgumentException
    //   484: dup
    //   485: ldc 'value must have tow items.'
    //   487: invokespecial <init> : (Ljava/lang/String;)V
    //   490: athrow
    //   491: aload #4
    //   493: invokevirtual getClass : ()Ljava/lang/Class;
    //   496: invokevirtual isArray : ()Z
    //   499: ifeq -> 461
    //   502: aload #4
    //   504: invokestatic getLength : (Ljava/lang/Object;)I
    //   507: istore #6
    //   509: new java/util/ArrayList
    //   512: dup
    //   513: iload #6
    //   515: invokespecial <init> : (I)V
    //   518: astore_1
    //   519: iconst_0
    //   520: istore #7
    //   522: iload #7
    //   524: iload #6
    //   526: if_icmpge -> 549
    //   529: aload_1
    //   530: aload #4
    //   532: iload #7
    //   534: invokestatic get : (Ljava/lang/Object;I)Ljava/lang/Object;
    //   537: invokeinterface add : (Ljava/lang/Object;)Z
    //   542: pop
    //   543: iinc #7, 1
    //   546: goto -> 522
    //   549: goto -> 461
    //   552: aload_2
    //   553: invokeinterface next : ()Ljava/lang/Object;
    //   558: astore_1
    //   559: aload_2
    //   560: invokeinterface hasNext : ()Z
    //   565: ifne -> 578
    //   568: new java/lang/IllegalArgumentException
    //   571: dup
    //   572: ldc 'value must have tow items.'
    //   574: invokespecial <init> : (Ljava/lang/String;)V
    //   577: athrow
    //   578: aload_2
    //   579: invokeinterface next : ()Ljava/lang/Object;
    //   584: astore_2
    //   585: aload_1
    //   586: invokestatic convert2DbValueIfNeeded : (Ljava/lang/Object;)Ljava/lang/Object;
    //   589: astore_1
    //   590: aload_2
    //   591: invokestatic convert2DbValueIfNeeded : (Ljava/lang/Object;)Ljava/lang/Object;
    //   594: astore_3
    //   595: getstatic org/xutils/db/sqlite/ColumnDbType.TEXT : Lorg/xutils/db/sqlite/ColumnDbType;
    //   598: aload_1
    //   599: invokevirtual getClass : ()Ljava/lang/Class;
    //   602: invokestatic getDbColumnType : (Ljava/lang/Class;)Lorg/xutils/db/sqlite/ColumnDbType;
    //   605: invokevirtual equals : (Ljava/lang/Object;)Z
    //   608: ifeq -> 708
    //   611: aload_1
    //   612: invokevirtual toString : ()Ljava/lang/String;
    //   615: astore_2
    //   616: aload_2
    //   617: astore_1
    //   618: aload_2
    //   619: bipush #39
    //   621: invokevirtual indexOf : (I)I
    //   624: iconst_m1
    //   625: if_icmpeq -> 637
    //   628: aload_2
    //   629: ldc '''
    //   631: ldc ''''
    //   633: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   636: astore_1
    //   637: aload_3
    //   638: invokevirtual toString : ()Ljava/lang/String;
    //   641: astore_3
    //   642: aload_3
    //   643: astore_2
    //   644: aload_3
    //   645: bipush #39
    //   647: invokevirtual indexOf : (I)I
    //   650: iconst_m1
    //   651: if_icmpeq -> 663
    //   654: aload_3
    //   655: ldc '''
    //   657: ldc ''''
    //   659: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   662: astore_2
    //   663: aload #5
    //   665: ldc '''
    //   667: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   670: aload_1
    //   671: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   674: ldc '''
    //   676: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   679: pop
    //   680: aload #5
    //   682: ldc ' AND '
    //   684: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   687: pop
    //   688: aload #5
    //   690: ldc '''
    //   692: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   695: aload_2
    //   696: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   699: ldc '''
    //   701: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   704: pop
    //   705: goto -> 99
    //   708: aload #5
    //   710: aload_1
    //   711: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   714: pop
    //   715: aload #5
    //   717: ldc ' AND '
    //   719: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   722: pop
    //   723: aload #5
    //   725: aload_3
    //   726: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   729: pop
    //   730: goto -> 99
    //   733: new java/lang/IllegalArgumentException
    //   736: dup
    //   737: ldc 'value must be an Array or an Iterable.'
    //   739: invokespecial <init> : (Ljava/lang/String;)V
    //   742: athrow
    //   743: aload #4
    //   745: invokestatic convert2DbValueIfNeeded : (Ljava/lang/Object;)Ljava/lang/Object;
    //   748: astore_1
    //   749: getstatic org/xutils/db/sqlite/ColumnDbType.TEXT : Lorg/xutils/db/sqlite/ColumnDbType;
    //   752: aload_1
    //   753: invokevirtual getClass : ()Ljava/lang/Class;
    //   756: invokestatic getDbColumnType : (Ljava/lang/Class;)Lorg/xutils/db/sqlite/ColumnDbType;
    //   759: invokevirtual equals : (Ljava/lang/Object;)Z
    //   762: ifeq -> 811
    //   765: aload_1
    //   766: invokevirtual toString : ()Ljava/lang/String;
    //   769: astore_2
    //   770: aload_2
    //   771: astore_1
    //   772: aload_2
    //   773: bipush #39
    //   775: invokevirtual indexOf : (I)I
    //   778: iconst_m1
    //   779: if_icmpeq -> 791
    //   782: aload_2
    //   783: ldc '''
    //   785: ldc ''''
    //   787: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   790: astore_1
    //   791: aload #5
    //   793: ldc '''
    //   795: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   798: aload_1
    //   799: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   802: ldc '''
    //   804: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   807: pop
    //   808: goto -> 99
    //   811: aload #5
    //   813: aload_1
    //   814: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   817: pop
    //   818: goto -> 99
  }
  
  public static WhereBuilder b() {
    return new WhereBuilder();
  }
  
  public static WhereBuilder b(String paramString1, String paramString2, Object paramObject) {
    WhereBuilder whereBuilder = new WhereBuilder();
    whereBuilder.appendCondition(null, paramString1, paramString2, paramObject);
    return whereBuilder;
  }
  
  public WhereBuilder and(String paramString1, String paramString2, Object paramObject) {
    if (this.whereItems.size() == 0) {
      String str1 = null;
      appendCondition(str1, paramString1, paramString2, paramObject);
      return this;
    } 
    String str = "AND";
    appendCondition(str, paramString1, paramString2, paramObject);
    return this;
  }
  
  public WhereBuilder and(WhereBuilder paramWhereBuilder) {
    if (this.whereItems.size() == 0) {
      String str1 = " ";
      return expr(str1 + "(" + paramWhereBuilder.toString() + ")");
    } 
    String str = "AND ";
    return expr(str + "(" + paramWhereBuilder.toString() + ")");
  }
  
  public WhereBuilder expr(String paramString) {
    this.whereItems.add(" " + paramString);
    return this;
  }
  
  public int getWhereItemSize() {
    return this.whereItems.size();
  }
  
  public WhereBuilder or(String paramString1, String paramString2, Object paramObject) {
    if (this.whereItems.size() == 0) {
      String str1 = null;
      appendCondition(str1, paramString1, paramString2, paramObject);
      return this;
    } 
    String str = "OR";
    appendCondition(str, paramString1, paramString2, paramObject);
    return this;
  }
  
  public WhereBuilder or(WhereBuilder paramWhereBuilder) {
    if (this.whereItems.size() == 0) {
      String str1 = " ";
      return expr(str1 + "(" + paramWhereBuilder.toString() + ")");
    } 
    String str = "OR ";
    return expr(str + "(" + paramWhereBuilder.toString() + ")");
  }
  
  public String toString() {
    if (this.whereItems.size() == 0)
      return ""; 
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<String> iterator = this.whereItems.iterator();
    while (iterator.hasNext())
      stringBuilder.append(iterator.next()); 
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/sqlite/WhereBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */