package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class ChangeBoundsPort extends TransitionPort {
  private static final String LOG_TAG = "ChangeBounds";
  
  private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
  
  private static final String PROPNAME_PARENT = "android:changeBounds:parent";
  
  private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
  
  private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
  
  private static RectEvaluator sRectEvaluator;
  
  private static final String[] sTransitionProperties = new String[] { "android:changeBounds:bounds", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY" };
  
  boolean mReparent = false;
  
  boolean mResizeClip = false;
  
  int[] tempLocation = new int[2];
  
  static {
    sRectEvaluator = new RectEvaluator();
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    View view = paramTransitionValues.view;
    paramTransitionValues.values.put("android:changeBounds:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
    paramTransitionValues.values.put("android:changeBounds:parent", paramTransitionValues.view.getParent());
    paramTransitionValues.view.getLocationInWindow(this.tempLocation);
    paramTransitionValues.values.put("android:changeBounds:windowX", Integer.valueOf(this.tempLocation[0]));
    paramTransitionValues.values.put("android:changeBounds:windowY", Integer.valueOf(this.tempLocation[1]));
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    // Byte code:
    //   0: aload_2
    //   1: ifnull -> 8
    //   4: aload_3
    //   5: ifnonnull -> 12
    //   8: aconst_null
    //   9: astore_1
    //   10: aload_1
    //   11: areturn
    //   12: aload_2
    //   13: getfield values : Ljava/util/Map;
    //   16: astore #4
    //   18: aload_3
    //   19: getfield values : Ljava/util/Map;
    //   22: astore #5
    //   24: aload #4
    //   26: ldc 'android:changeBounds:parent'
    //   28: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   33: checkcast android/view/ViewGroup
    //   36: astore #4
    //   38: aload #5
    //   40: ldc 'android:changeBounds:parent'
    //   42: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   47: checkcast android/view/ViewGroup
    //   50: astore #6
    //   52: aload #4
    //   54: ifnull -> 62
    //   57: aload #6
    //   59: ifnonnull -> 67
    //   62: aconst_null
    //   63: astore_1
    //   64: goto -> 10
    //   67: aload_3
    //   68: getfield view : Landroid/view/View;
    //   71: astore #5
    //   73: aload #4
    //   75: aload #6
    //   77: if_acmpeq -> 93
    //   80: aload #4
    //   82: invokevirtual getId : ()I
    //   85: aload #6
    //   87: invokevirtual getId : ()I
    //   90: if_icmpne -> 580
    //   93: iconst_1
    //   94: istore #7
    //   96: aload_0
    //   97: getfield mReparent : Z
    //   100: ifeq -> 108
    //   103: iload #7
    //   105: ifeq -> 913
    //   108: aload_2
    //   109: getfield values : Ljava/util/Map;
    //   112: ldc 'android:changeBounds:bounds'
    //   114: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   119: checkcast android/graphics/Rect
    //   122: astore_1
    //   123: aload_3
    //   124: getfield values : Ljava/util/Map;
    //   127: ldc 'android:changeBounds:bounds'
    //   129: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   134: checkcast android/graphics/Rect
    //   137: astore_2
    //   138: aload_1
    //   139: getfield left : I
    //   142: istore #8
    //   144: aload_2
    //   145: getfield left : I
    //   148: istore #9
    //   150: aload_1
    //   151: getfield top : I
    //   154: istore #10
    //   156: aload_2
    //   157: getfield top : I
    //   160: istore #11
    //   162: aload_1
    //   163: getfield right : I
    //   166: istore #12
    //   168: aload_2
    //   169: getfield right : I
    //   172: istore #13
    //   174: aload_1
    //   175: getfield bottom : I
    //   178: istore #14
    //   180: aload_2
    //   181: getfield bottom : I
    //   184: istore #15
    //   186: iload #12
    //   188: iload #8
    //   190: isub
    //   191: istore #16
    //   193: iload #14
    //   195: iload #10
    //   197: isub
    //   198: istore #17
    //   200: iload #13
    //   202: iload #9
    //   204: isub
    //   205: istore #18
    //   207: iload #15
    //   209: iload #11
    //   211: isub
    //   212: istore #19
    //   214: iconst_0
    //   215: istore #20
    //   217: iconst_0
    //   218: istore #21
    //   220: iload #20
    //   222: istore #7
    //   224: iload #16
    //   226: ifeq -> 319
    //   229: iload #20
    //   231: istore #7
    //   233: iload #17
    //   235: ifeq -> 319
    //   238: iload #20
    //   240: istore #7
    //   242: iload #18
    //   244: ifeq -> 319
    //   247: iload #20
    //   249: istore #7
    //   251: iload #19
    //   253: ifeq -> 319
    //   256: iload #8
    //   258: iload #9
    //   260: if_icmpeq -> 268
    //   263: iconst_0
    //   264: iconst_1
    //   265: iadd
    //   266: istore #21
    //   268: iload #21
    //   270: istore #7
    //   272: iload #10
    //   274: iload #11
    //   276: if_icmpeq -> 285
    //   279: iload #21
    //   281: iconst_1
    //   282: iadd
    //   283: istore #7
    //   285: iload #7
    //   287: istore #21
    //   289: iload #12
    //   291: iload #13
    //   293: if_icmpeq -> 302
    //   296: iload #7
    //   298: iconst_1
    //   299: iadd
    //   300: istore #21
    //   302: iload #21
    //   304: istore #7
    //   306: iload #14
    //   308: iload #15
    //   310: if_icmpeq -> 319
    //   313: iload #21
    //   315: iconst_1
    //   316: iadd
    //   317: istore #7
    //   319: iload #7
    //   321: ifle -> 1222
    //   324: aload_0
    //   325: getfield mResizeClip : Z
    //   328: ifne -> 586
    //   331: iload #7
    //   333: anewarray android/animation/PropertyValuesHolder
    //   336: astore_1
    //   337: iload #8
    //   339: iload #9
    //   341: if_icmpeq -> 351
    //   344: aload #5
    //   346: iload #8
    //   348: invokevirtual setLeft : (I)V
    //   351: iload #10
    //   353: iload #11
    //   355: if_icmpeq -> 365
    //   358: aload #5
    //   360: iload #10
    //   362: invokevirtual setTop : (I)V
    //   365: iload #12
    //   367: iload #13
    //   369: if_icmpeq -> 379
    //   372: aload #5
    //   374: iload #12
    //   376: invokevirtual setRight : (I)V
    //   379: iload #14
    //   381: iload #15
    //   383: if_icmpeq -> 393
    //   386: aload #5
    //   388: iload #14
    //   390: invokevirtual setBottom : (I)V
    //   393: iload #8
    //   395: iload #9
    //   397: if_icmpeq -> 1239
    //   400: iconst_0
    //   401: iconst_1
    //   402: iadd
    //   403: istore #21
    //   405: aload_1
    //   406: iconst_0
    //   407: ldc 'left'
    //   409: iconst_2
    //   410: newarray int
    //   412: dup
    //   413: iconst_0
    //   414: iload #8
    //   416: iastore
    //   417: dup
    //   418: iconst_1
    //   419: iload #9
    //   421: iastore
    //   422: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   425: aastore
    //   426: iload #21
    //   428: istore #7
    //   430: iload #10
    //   432: iload #11
    //   434: if_icmpeq -> 465
    //   437: aload_1
    //   438: iload #21
    //   440: ldc 'top'
    //   442: iconst_2
    //   443: newarray int
    //   445: dup
    //   446: iconst_0
    //   447: iload #10
    //   449: iastore
    //   450: dup
    //   451: iconst_1
    //   452: iload #11
    //   454: iastore
    //   455: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   458: aastore
    //   459: iload #21
    //   461: iconst_1
    //   462: iadd
    //   463: istore #7
    //   465: iload #7
    //   467: istore #21
    //   469: iload #12
    //   471: iload #13
    //   473: if_icmpeq -> 504
    //   476: aload_1
    //   477: iload #7
    //   479: ldc 'right'
    //   481: iconst_2
    //   482: newarray int
    //   484: dup
    //   485: iconst_0
    //   486: iload #12
    //   488: iastore
    //   489: dup
    //   490: iconst_1
    //   491: iload #13
    //   493: iastore
    //   494: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   497: aastore
    //   498: iload #7
    //   500: iconst_1
    //   501: iadd
    //   502: istore #21
    //   504: iload #14
    //   506: iload #15
    //   508: if_icmpeq -> 1236
    //   511: aload_1
    //   512: iload #21
    //   514: ldc 'bottom'
    //   516: iconst_2
    //   517: newarray int
    //   519: dup
    //   520: iconst_0
    //   521: iload #14
    //   523: iastore
    //   524: dup
    //   525: iconst_1
    //   526: iload #15
    //   528: iastore
    //   529: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   532: aastore
    //   533: aload #5
    //   535: aload_1
    //   536: invokestatic ofPropertyValuesHolder : (Ljava/lang/Object;[Landroid/animation/PropertyValuesHolder;)Landroid/animation/ObjectAnimator;
    //   539: astore_2
    //   540: aload_2
    //   541: astore_1
    //   542: aload #5
    //   544: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   547: instanceof android/view/ViewGroup
    //   550: ifeq -> 10
    //   553: aload #5
    //   555: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   558: checkcast android/view/ViewGroup
    //   561: astore_1
    //   562: aload_0
    //   563: new android/support/transition/ChangeBoundsPort$1
    //   566: dup
    //   567: aload_0
    //   568: invokespecial <init> : (Landroid/support/transition/ChangeBoundsPort;)V
    //   571: invokevirtual addListener : (Landroid/support/transition/TransitionPort$TransitionListener;)Landroid/support/transition/TransitionPort;
    //   574: pop
    //   575: aload_2
    //   576: astore_1
    //   577: goto -> 10
    //   580: iconst_0
    //   581: istore #7
    //   583: goto -> 96
    //   586: iload #16
    //   588: iload #18
    //   590: if_icmpeq -> 608
    //   593: aload #5
    //   595: iload #16
    //   597: iload #18
    //   599: invokestatic max : (II)I
    //   602: iload #9
    //   604: iadd
    //   605: invokevirtual setRight : (I)V
    //   608: iload #17
    //   610: iload #19
    //   612: if_icmpeq -> 630
    //   615: aload #5
    //   617: iload #17
    //   619: iload #19
    //   621: invokestatic max : (II)I
    //   624: iload #11
    //   626: iadd
    //   627: invokevirtual setBottom : (I)V
    //   630: iload #8
    //   632: iload #9
    //   634: if_icmpeq -> 648
    //   637: aload #5
    //   639: iload #8
    //   641: iload #9
    //   643: isub
    //   644: i2f
    //   645: invokevirtual setTranslationX : (F)V
    //   648: iload #10
    //   650: iload #11
    //   652: if_icmpeq -> 666
    //   655: aload #5
    //   657: iload #10
    //   659: iload #11
    //   661: isub
    //   662: i2f
    //   663: invokevirtual setTranslationY : (F)V
    //   666: iload #9
    //   668: iload #8
    //   670: isub
    //   671: i2f
    //   672: fstore #22
    //   674: iload #11
    //   676: iload #10
    //   678: isub
    //   679: i2f
    //   680: fstore #23
    //   682: iload #18
    //   684: iload #16
    //   686: isub
    //   687: istore #20
    //   689: iload #19
    //   691: iload #17
    //   693: isub
    //   694: istore #14
    //   696: iconst_0
    //   697: istore #21
    //   699: fload #22
    //   701: fconst_0
    //   702: fcmpl
    //   703: ifeq -> 711
    //   706: iconst_0
    //   707: iconst_1
    //   708: iadd
    //   709: istore #21
    //   711: iload #21
    //   713: istore #7
    //   715: fload #23
    //   717: fconst_0
    //   718: fcmpl
    //   719: ifeq -> 728
    //   722: iload #21
    //   724: iconst_1
    //   725: iadd
    //   726: istore #7
    //   728: iload #20
    //   730: ifne -> 742
    //   733: iload #7
    //   735: istore #21
    //   737: iload #14
    //   739: ifeq -> 748
    //   742: iload #7
    //   744: iconst_1
    //   745: iadd
    //   746: istore #21
    //   748: iload #21
    //   750: anewarray android/animation/PropertyValuesHolder
    //   753: astore_1
    //   754: fload #22
    //   756: fconst_0
    //   757: fcmpl
    //   758: ifeq -> 1230
    //   761: iconst_0
    //   762: iconst_1
    //   763: iadd
    //   764: istore #7
    //   766: aload_1
    //   767: iconst_0
    //   768: ldc 'translationX'
    //   770: iconst_2
    //   771: newarray float
    //   773: dup
    //   774: iconst_0
    //   775: aload #5
    //   777: invokevirtual getTranslationX : ()F
    //   780: fastore
    //   781: dup
    //   782: iconst_1
    //   783: fconst_0
    //   784: fastore
    //   785: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   788: aastore
    //   789: fload #23
    //   791: fconst_0
    //   792: fcmpl
    //   793: ifeq -> 1227
    //   796: aload_1
    //   797: iload #7
    //   799: ldc 'translationY'
    //   801: iconst_2
    //   802: newarray float
    //   804: dup
    //   805: iconst_0
    //   806: aload #5
    //   808: invokevirtual getTranslationY : ()F
    //   811: fastore
    //   812: dup
    //   813: iconst_1
    //   814: fconst_0
    //   815: fastore
    //   816: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   819: aastore
    //   820: iload #20
    //   822: ifne -> 830
    //   825: iload #14
    //   827: ifeq -> 858
    //   830: new android/graphics/Rect
    //   833: dup
    //   834: iconst_0
    //   835: iconst_0
    //   836: iload #16
    //   838: iload #17
    //   840: invokespecial <init> : (IIII)V
    //   843: pop
    //   844: new android/graphics/Rect
    //   847: dup
    //   848: iconst_0
    //   849: iconst_0
    //   850: iload #18
    //   852: iload #19
    //   854: invokespecial <init> : (IIII)V
    //   857: pop
    //   858: aload #5
    //   860: aload_1
    //   861: invokestatic ofPropertyValuesHolder : (Ljava/lang/Object;[Landroid/animation/PropertyValuesHolder;)Landroid/animation/ObjectAnimator;
    //   864: astore_1
    //   865: aload #5
    //   867: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   870: instanceof android/view/ViewGroup
    //   873: ifeq -> 898
    //   876: aload #5
    //   878: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   881: checkcast android/view/ViewGroup
    //   884: astore_2
    //   885: aload_0
    //   886: new android/support/transition/ChangeBoundsPort$2
    //   889: dup
    //   890: aload_0
    //   891: invokespecial <init> : (Landroid/support/transition/ChangeBoundsPort;)V
    //   894: invokevirtual addListener : (Landroid/support/transition/TransitionPort$TransitionListener;)Landroid/support/transition/TransitionPort;
    //   897: pop
    //   898: aload_1
    //   899: new android/support/transition/ChangeBoundsPort$3
    //   902: dup
    //   903: aload_0
    //   904: invokespecial <init> : (Landroid/support/transition/ChangeBoundsPort;)V
    //   907: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   910: goto -> 10
    //   913: aload_2
    //   914: getfield values : Ljava/util/Map;
    //   917: ldc 'android:changeBounds:windowX'
    //   919: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   924: checkcast java/lang/Integer
    //   927: invokevirtual intValue : ()I
    //   930: istore #19
    //   932: aload_2
    //   933: getfield values : Ljava/util/Map;
    //   936: ldc 'android:changeBounds:windowY'
    //   938: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   943: checkcast java/lang/Integer
    //   946: invokevirtual intValue : ()I
    //   949: istore #21
    //   951: aload_3
    //   952: getfield values : Ljava/util/Map;
    //   955: ldc 'android:changeBounds:windowX'
    //   957: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   962: checkcast java/lang/Integer
    //   965: invokevirtual intValue : ()I
    //   968: istore #20
    //   970: aload_3
    //   971: getfield values : Ljava/util/Map;
    //   974: ldc 'android:changeBounds:windowY'
    //   976: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   981: checkcast java/lang/Integer
    //   984: invokevirtual intValue : ()I
    //   987: istore #7
    //   989: iload #19
    //   991: iload #20
    //   993: if_icmpne -> 1003
    //   996: iload #21
    //   998: iload #7
    //   1000: if_icmpeq -> 1222
    //   1003: aload_1
    //   1004: aload_0
    //   1005: getfield tempLocation : [I
    //   1008: invokevirtual getLocationInWindow : ([I)V
    //   1011: aload #5
    //   1013: invokevirtual getWidth : ()I
    //   1016: aload #5
    //   1018: invokevirtual getHeight : ()I
    //   1021: getstatic android/graphics/Bitmap$Config.ARGB_8888 : Landroid/graphics/Bitmap$Config;
    //   1024: invokestatic createBitmap : (IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
    //   1027: astore_2
    //   1028: aload #5
    //   1030: new android/graphics/Canvas
    //   1033: dup
    //   1034: aload_2
    //   1035: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   1038: invokevirtual draw : (Landroid/graphics/Canvas;)V
    //   1041: new android/graphics/drawable/BitmapDrawable
    //   1044: dup
    //   1045: aload_2
    //   1046: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   1049: astore_3
    //   1050: aload #5
    //   1052: iconst_4
    //   1053: invokevirtual setVisibility : (I)V
    //   1056: aload_1
    //   1057: invokestatic createFrom : (Landroid/view/View;)Landroid/support/transition/ViewOverlay;
    //   1060: aload_3
    //   1061: invokevirtual add : (Landroid/graphics/drawable/Drawable;)V
    //   1064: new android/graphics/Rect
    //   1067: dup
    //   1068: iload #19
    //   1070: aload_0
    //   1071: getfield tempLocation : [I
    //   1074: iconst_0
    //   1075: iaload
    //   1076: isub
    //   1077: iload #21
    //   1079: aload_0
    //   1080: getfield tempLocation : [I
    //   1083: iconst_1
    //   1084: iaload
    //   1085: isub
    //   1086: iload #19
    //   1088: aload_0
    //   1089: getfield tempLocation : [I
    //   1092: iconst_0
    //   1093: iaload
    //   1094: isub
    //   1095: aload #5
    //   1097: invokevirtual getWidth : ()I
    //   1100: iadd
    //   1101: iload #21
    //   1103: aload_0
    //   1104: getfield tempLocation : [I
    //   1107: iconst_1
    //   1108: iaload
    //   1109: isub
    //   1110: aload #5
    //   1112: invokevirtual getHeight : ()I
    //   1115: iadd
    //   1116: invokespecial <init> : (IIII)V
    //   1119: astore #4
    //   1121: new android/graphics/Rect
    //   1124: dup
    //   1125: iload #20
    //   1127: aload_0
    //   1128: getfield tempLocation : [I
    //   1131: iconst_0
    //   1132: iaload
    //   1133: isub
    //   1134: iload #7
    //   1136: aload_0
    //   1137: getfield tempLocation : [I
    //   1140: iconst_1
    //   1141: iaload
    //   1142: isub
    //   1143: iload #20
    //   1145: aload_0
    //   1146: getfield tempLocation : [I
    //   1149: iconst_0
    //   1150: iaload
    //   1151: isub
    //   1152: aload #5
    //   1154: invokevirtual getWidth : ()I
    //   1157: iadd
    //   1158: iload #7
    //   1160: aload_0
    //   1161: getfield tempLocation : [I
    //   1164: iconst_1
    //   1165: iaload
    //   1166: isub
    //   1167: aload #5
    //   1169: invokevirtual getHeight : ()I
    //   1172: iadd
    //   1173: invokespecial <init> : (IIII)V
    //   1176: astore_2
    //   1177: aload_3
    //   1178: ldc_w 'bounds'
    //   1181: getstatic android/support/transition/ChangeBoundsPort.sRectEvaluator : Landroid/support/transition/RectEvaluator;
    //   1184: iconst_2
    //   1185: anewarray java/lang/Object
    //   1188: dup
    //   1189: iconst_0
    //   1190: aload #4
    //   1192: aastore
    //   1193: dup
    //   1194: iconst_1
    //   1195: aload_2
    //   1196: aastore
    //   1197: invokestatic ofObject : (Ljava/lang/Object;Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/ObjectAnimator;
    //   1200: astore_2
    //   1201: aload_2
    //   1202: new android/support/transition/ChangeBoundsPort$4
    //   1205: dup
    //   1206: aload_0
    //   1207: aload_1
    //   1208: aload_3
    //   1209: aload #5
    //   1211: invokespecial <init> : (Landroid/support/transition/ChangeBoundsPort;Landroid/view/ViewGroup;Landroid/graphics/drawable/BitmapDrawable;Landroid/view/View;)V
    //   1214: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   1217: aload_2
    //   1218: astore_1
    //   1219: goto -> 10
    //   1222: aconst_null
    //   1223: astore_1
    //   1224: goto -> 10
    //   1227: goto -> 820
    //   1230: iconst_0
    //   1231: istore #7
    //   1233: goto -> 789
    //   1236: goto -> 533
    //   1239: iconst_0
    //   1240: istore #21
    //   1242: goto -> 426
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
  
  public void setReparent(boolean paramBoolean) {
    this.mReparent = paramBoolean;
  }
  
  public void setResizeClip(boolean paramBoolean) {
    this.mResizeClip = paramBoolean;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/ChangeBoundsPort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */