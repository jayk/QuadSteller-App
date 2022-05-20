package app.gamer.quadstellar.ui;

import app.gamer.quadstellar.mode.LinkageControl;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseLinkageActivity extends BaseActivity {
  protected boolean isFrist = true;
  
  protected List<LinkageControl> mDatas = new ArrayList<LinkageControl>();
  
  protected List<LinkageControl> LinkageDeviceList() {
    // Byte code:
    //   0: ldc app/gamer/quadstellar/mode/EFDeviceMusic
    //   2: ldc 'sceneId'
    //   4: ldc '='
    //   6: iconst_0
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   13: invokestatic getAllList : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Ljava/util/List;
    //   16: astore_1
    //   17: aload_1
    //   18: ifnonnull -> 110
    //   21: ldc app/gamer/quadstellar/mode/EFDeviceLight
    //   23: ldc 'sceneId'
    //   25: ldc '='
    //   27: iconst_0
    //   28: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   31: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   34: invokestatic getAllList : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Ljava/util/List;
    //   37: astore_1
    //   38: aload_1
    //   39: ifnonnull -> 182
    //   42: ldc app/gamer/quadstellar/mode/EFDeviceOutlet
    //   44: ldc 'sceneId'
    //   46: ldc '='
    //   48: iconst_0
    //   49: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   52: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   55: invokestatic getAllList : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Ljava/util/List;
    //   58: astore_1
    //   59: aload_1
    //   60: ifnonnull -> 458
    //   63: ldc app/gamer/quadstellar/mode/EFDeviceSwitch
    //   65: ldc 'sceneId'
    //   67: ldc '='
    //   69: iconst_0
    //   70: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   73: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   76: invokestatic getAllList : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Ljava/util/List;
    //   79: astore_2
    //   80: aload_2
    //   81: ifnonnull -> 726
    //   84: ldc app/gamer/quadstellar/mode/EFDeviceCurtains
    //   86: ldc 'sceneId'
    //   88: ldc '='
    //   90: iconst_0
    //   91: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   94: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   97: invokestatic getAllList : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Ljava/util/List;
    //   100: astore_1
    //   101: aload_1
    //   102: ifnonnull -> 1002
    //   105: aload_0
    //   106: getfield mDatas : Ljava/util/List;
    //   109: areturn
    //   110: iconst_0
    //   111: istore_3
    //   112: iload_3
    //   113: aload_1
    //   114: invokeinterface size : ()I
    //   119: if_icmpge -> 21
    //   122: aload_1
    //   123: iload_3
    //   124: invokeinterface get : (I)Ljava/lang/Object;
    //   129: checkcast app/gamer/quadstellar/mode/EFDeviceMusic
    //   132: astore #4
    //   134: bipush #15
    //   136: aload #4
    //   138: invokevirtual getDeviceType : ()I
    //   141: if_icmpne -> 176
    //   144: aload_0
    //   145: getfield mDatas : Ljava/util/List;
    //   148: new app/gamer/quadstellar/mode/LinkageControl
    //   151: dup
    //   152: aconst_null
    //   153: iconst_0
    //   154: bipush #15
    //   156: aload #4
    //   158: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   161: aload_0
    //   162: ldc 2131296863
    //   164: invokevirtual getString : (I)Ljava/lang/String;
    //   167: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   170: invokeinterface add : (Ljava/lang/Object;)Z
    //   175: pop
    //   176: iinc #3, 1
    //   179: goto -> 112
    //   182: iconst_0
    //   183: istore_3
    //   184: iload_3
    //   185: aload_1
    //   186: invokeinterface size : ()I
    //   191: if_icmpge -> 42
    //   194: aload_1
    //   195: iload_3
    //   196: invokeinterface get : (I)Ljava/lang/Object;
    //   201: checkcast app/gamer/quadstellar/mode/EFDeviceLight
    //   204: astore_2
    //   205: aload_2
    //   206: invokevirtual getDeviceType : ()I
    //   209: istore #5
    //   211: iconst_5
    //   212: iload #5
    //   214: if_icmpne -> 369
    //   217: aload_2
    //   218: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   221: astore #4
    //   223: ldc 'LinkageDeviceList'
    //   225: new java/lang/StringBuilder
    //   228: dup
    //   229: invokespecial <init> : ()V
    //   232: ldc 'LinkageDeviceList: '
    //   234: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: aload #4
    //   239: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: invokevirtual toString : ()Ljava/lang/String;
    //   245: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   248: pop
    //   249: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   252: aload_2
    //   253: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   256: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   259: astore #6
    //   261: aload_2
    //   262: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   265: ifnonnull -> 360
    //   268: ldc 'NULL'
    //   270: astore #4
    //   272: ldc 'LinkageDeviceList'
    //   274: new java/lang/StringBuilder
    //   277: dup
    //   278: invokespecial <init> : ()V
    //   281: ldc 'LinkageDeviceList: mDatas ---'
    //   283: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   286: aload_0
    //   287: getfield mDatas : Ljava/util/List;
    //   290: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   293: invokevirtual toString : ()Ljava/lang/String;
    //   296: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   299: pop
    //   300: ldc 'LinkageDeviceList'
    //   302: new java/lang/StringBuilder
    //   305: dup
    //   306: invokespecial <init> : ()V
    //   309: ldc 'LinkageDeviceList: '
    //   311: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   314: aload #6
    //   316: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   319: invokevirtual toString : ()Ljava/lang/String;
    //   322: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   325: pop
    //   326: aload_0
    //   327: getfield mDatas : Ljava/util/List;
    //   330: new app/gamer/quadstellar/mode/LinkageControl
    //   333: dup
    //   334: aload #6
    //   336: iconst_0
    //   337: iconst_0
    //   338: aload #6
    //   340: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   343: aload #4
    //   345: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   348: invokeinterface add : (Ljava/lang/Object;)Z
    //   353: pop
    //   354: iinc #3, 1
    //   357: goto -> 184
    //   360: aload_2
    //   361: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   364: astore #4
    //   366: goto -> 272
    //   369: iconst_1
    //   370: iload #5
    //   372: if_icmpne -> 354
    //   375: aload_0
    //   376: getfield isFrist : Z
    //   379: ifeq -> 354
    //   382: aload_2
    //   383: ifnull -> 354
    //   386: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   389: aload_2
    //   390: invokevirtual getParentMac : ()Ljava/lang/String;
    //   393: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   396: astore #6
    //   398: aload #6
    //   400: ifnull -> 354
    //   403: aload_2
    //   404: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   407: ifnonnull -> 449
    //   410: ldc 'NULL'
    //   412: astore #4
    //   414: aload_0
    //   415: getfield mDatas : Ljava/util/List;
    //   418: new app/gamer/quadstellar/mode/LinkageControl
    //   421: dup
    //   422: aload #6
    //   424: iconst_0
    //   425: iconst_1
    //   426: aload_2
    //   427: invokevirtual getParentMac : ()Ljava/lang/String;
    //   430: aload #4
    //   432: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   435: invokeinterface add : (Ljava/lang/Object;)Z
    //   440: pop
    //   441: aload_0
    //   442: iconst_0
    //   443: putfield isFrist : Z
    //   446: goto -> 354
    //   449: aload_2
    //   450: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   453: astore #4
    //   455: goto -> 414
    //   458: iconst_0
    //   459: istore_3
    //   460: iload_3
    //   461: aload_1
    //   462: invokeinterface size : ()I
    //   467: if_icmpge -> 63
    //   470: aload_1
    //   471: iload_3
    //   472: invokeinterface get : (I)Ljava/lang/Object;
    //   477: checkcast app/gamer/quadstellar/mode/EFDeviceOutlet
    //   480: astore_2
    //   481: aload_2
    //   482: invokevirtual getDeviceType : ()I
    //   485: istore #5
    //   487: iconst_2
    //   488: iload #5
    //   490: if_icmpne -> 587
    //   493: aload_2
    //   494: ifnonnull -> 503
    //   497: iinc #3, 1
    //   500: goto -> 460
    //   503: aload_1
    //   504: iload_3
    //   505: invokeinterface get : (I)Ljava/lang/Object;
    //   510: checkcast app/gamer/quadstellar/mode/EFDeviceOutlet
    //   513: invokevirtual getDeviceType : ()I
    //   516: iconst_2
    //   517: if_icmpne -> 497
    //   520: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   523: aload_2
    //   524: invokevirtual getParentMac : ()Ljava/lang/String;
    //   527: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   530: astore #6
    //   532: aload #6
    //   534: ifnull -> 497
    //   537: aload_2
    //   538: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   541: ifnonnull -> 578
    //   544: ldc 'NULL'
    //   546: astore #4
    //   548: aload_0
    //   549: getfield mDatas : Ljava/util/List;
    //   552: new app/gamer/quadstellar/mode/LinkageControl
    //   555: dup
    //   556: aload #6
    //   558: iconst_0
    //   559: iconst_2
    //   560: aload_2
    //   561: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   564: aload #4
    //   566: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   569: invokeinterface add : (Ljava/lang/Object;)Z
    //   574: pop
    //   575: goto -> 497
    //   578: aload_2
    //   579: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   582: astore #4
    //   584: goto -> 548
    //   587: iconst_4
    //   588: iload #5
    //   590: if_icmpne -> 656
    //   593: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   596: aload_2
    //   597: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   600: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   603: astore #6
    //   605: aload_2
    //   606: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   609: ifnonnull -> 647
    //   612: ldc 'NULL'
    //   614: astore #4
    //   616: aload_0
    //   617: getfield mDatas : Ljava/util/List;
    //   620: new app/gamer/quadstellar/mode/LinkageControl
    //   623: dup
    //   624: aload #6
    //   626: iconst_0
    //   627: iconst_0
    //   628: aload #6
    //   630: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   633: aload #4
    //   635: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   638: invokeinterface add : (Ljava/lang/Object;)Z
    //   643: pop
    //   644: goto -> 497
    //   647: aload_2
    //   648: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   651: astore #4
    //   653: goto -> 616
    //   656: bipush #9
    //   658: iload #5
    //   660: if_icmpne -> 497
    //   663: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   666: aload_2
    //   667: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   670: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   673: astore #6
    //   675: aload_2
    //   676: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   679: ifnonnull -> 717
    //   682: ldc 'NULL'
    //   684: astore #4
    //   686: aload_0
    //   687: getfield mDatas : Ljava/util/List;
    //   690: new app/gamer/quadstellar/mode/LinkageControl
    //   693: dup
    //   694: aload #6
    //   696: iconst_0
    //   697: iconst_0
    //   698: aload #6
    //   700: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   703: aload #4
    //   705: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   708: invokeinterface add : (Ljava/lang/Object;)Z
    //   713: pop
    //   714: goto -> 497
    //   717: aload_2
    //   718: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   721: astore #4
    //   723: goto -> 686
    //   726: iconst_0
    //   727: istore_3
    //   728: iload_3
    //   729: aload_2
    //   730: invokeinterface size : ()I
    //   735: if_icmpge -> 84
    //   738: aload_2
    //   739: iload_3
    //   740: invokeinterface get : (I)Ljava/lang/Object;
    //   745: checkcast app/gamer/quadstellar/mode/EFDeviceSwitch
    //   748: astore_1
    //   749: aload_1
    //   750: invokevirtual getDeviceType : ()I
    //   753: istore #5
    //   755: iconst_3
    //   756: iload #5
    //   758: if_icmpeq -> 775
    //   761: bipush #10
    //   763: iload #5
    //   765: if_icmpeq -> 775
    //   768: bipush #11
    //   770: iload #5
    //   772: if_icmpne -> 779
    //   775: aload_2
    //   776: ifnonnull -> 785
    //   779: iinc #3, 1
    //   782: goto -> 728
    //   785: iconst_3
    //   786: iload #5
    //   788: if_icmpne -> 846
    //   791: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   794: aload_1
    //   795: invokevirtual getParentMac : ()Ljava/lang/String;
    //   798: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   801: astore #6
    //   803: aload #6
    //   805: ifnull -> 846
    //   808: aload_1
    //   809: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   812: ifnonnull -> 975
    //   815: ldc 'NULL'
    //   817: astore #4
    //   819: aload_0
    //   820: getfield mDatas : Ljava/util/List;
    //   823: new app/gamer/quadstellar/mode/LinkageControl
    //   826: dup
    //   827: aload #6
    //   829: iconst_0
    //   830: iconst_3
    //   831: aload_1
    //   832: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   835: aload #4
    //   837: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   840: invokeinterface add : (Ljava/lang/Object;)Z
    //   845: pop
    //   846: bipush #10
    //   848: iload #5
    //   850: if_icmpne -> 909
    //   853: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   856: aload_1
    //   857: invokevirtual getParentMac : ()Ljava/lang/String;
    //   860: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   863: astore #6
    //   865: aload #6
    //   867: ifnull -> 909
    //   870: aload_1
    //   871: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   874: ifnonnull -> 984
    //   877: ldc 'NULL'
    //   879: astore #4
    //   881: aload_0
    //   882: getfield mDatas : Ljava/util/List;
    //   885: new app/gamer/quadstellar/mode/LinkageControl
    //   888: dup
    //   889: aload #6
    //   891: iconst_0
    //   892: bipush #10
    //   894: aload_1
    //   895: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   898: aload #4
    //   900: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   903: invokeinterface add : (Ljava/lang/Object;)Z
    //   908: pop
    //   909: bipush #11
    //   911: iload #5
    //   913: if_icmpne -> 779
    //   916: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   919: aload_1
    //   920: invokevirtual getParentMac : ()Ljava/lang/String;
    //   923: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   926: astore #6
    //   928: aload #6
    //   930: ifnull -> 779
    //   933: aload_1
    //   934: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   937: ifnonnull -> 993
    //   940: ldc 'NULL'
    //   942: astore #4
    //   944: aload_0
    //   945: getfield mDatas : Ljava/util/List;
    //   948: new app/gamer/quadstellar/mode/LinkageControl
    //   951: dup
    //   952: aload #6
    //   954: iconst_0
    //   955: bipush #11
    //   957: aload_1
    //   958: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   961: aload #4
    //   963: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   966: invokeinterface add : (Ljava/lang/Object;)Z
    //   971: pop
    //   972: goto -> 779
    //   975: aload_1
    //   976: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   979: astore #4
    //   981: goto -> 819
    //   984: aload_1
    //   985: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   988: astore #4
    //   990: goto -> 881
    //   993: aload_1
    //   994: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   997: astore #4
    //   999: goto -> 944
    //   1002: iconst_0
    //   1003: istore_3
    //   1004: iload_3
    //   1005: aload_1
    //   1006: invokeinterface size : ()I
    //   1011: if_icmpge -> 105
    //   1014: aload_1
    //   1015: iload_3
    //   1016: invokeinterface get : (I)Ljava/lang/Object;
    //   1021: checkcast app/gamer/quadstellar/mode/EFDeviceCurtains
    //   1024: astore_2
    //   1025: bipush #7
    //   1027: aload_2
    //   1028: invokevirtual getDeviceType : ()I
    //   1031: if_icmpne -> 1038
    //   1034: aload_2
    //   1035: ifnonnull -> 1044
    //   1038: iinc #3, 1
    //   1041: goto -> 1004
    //   1044: aload_1
    //   1045: iload_3
    //   1046: invokeinterface get : (I)Ljava/lang/Object;
    //   1051: checkcast app/gamer/quadstellar/mode/EFDeviceCurtains
    //   1054: invokevirtual getDeviceType : ()I
    //   1057: bipush #7
    //   1059: if_icmpne -> 1038
    //   1062: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   1065: aload_2
    //   1066: invokevirtual getParentMac : ()Ljava/lang/String;
    //   1069: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   1072: astore #6
    //   1074: aload #6
    //   1076: ifnull -> 1038
    //   1079: aload_2
    //   1080: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   1083: ifnonnull -> 1121
    //   1086: ldc 'NULL'
    //   1088: astore #4
    //   1090: aload_0
    //   1091: getfield mDatas : Ljava/util/List;
    //   1094: new app/gamer/quadstellar/mode/LinkageControl
    //   1097: dup
    //   1098: aload #6
    //   1100: iconst_0
    //   1101: bipush #7
    //   1103: aload_2
    //   1104: invokevirtual getDeviceMac : ()Ljava/lang/String;
    //   1107: aload #4
    //   1109: invokespecial <init> : (Lapp/gamer/quadstellar/mode/ControlDevice;ZILjava/lang/String;Ljava/lang/String;)V
    //   1112: invokeinterface add : (Ljava/lang/Object;)Z
    //   1117: pop
    //   1118: goto -> 1038
    //   1121: aload_2
    //   1122: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   1125: astore #4
    //   1127: goto -> 1090
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/BaseLinkageActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */