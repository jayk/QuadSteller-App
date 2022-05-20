package app.gamer.quadstellar.newdevices.Base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.controler.DataControler;
import app.gamer.quadstellar.db.FanDeviceInfoDB;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.db.LightDeviceInfoDB;
import app.gamer.quadstellar.domain.FanDeviceInfo;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.LightDeviceInfo;
import app.gamer.quadstellar.utils.LogUtil;
import java.util.List;

public abstract class TabController extends BaseController {
  public static final int Fan_Mode = 0;
  
  public static final int Fan_Speed = 1;
  
  public static final int Light_Brightness = 8;
  
  public static final int Light_Color = 4;
  
  public static final int Light_Fineness = 16;
  
  public static final int Light_MODE = 2;
  
  public static final int Light_Speed = 32;
  
  public static final int Light_Toggle = 1;
  
  public static final int Temp_Colsoe = 0;
  
  public static final int Temp_Flour = 2;
  
  public static final int Temp_Flour2 = 3;
  
  public static final int Temp_Open = 1;
  
  public static int fanContorlType;
  
  boolean isContrlAll;
  
  protected FrameLayout mContentContainer;
  
  public TabController(Context paramContext, boolean paramBoolean) {
    super(paramContext);
    this.isContrlAll = paramBoolean;
  }
  
  protected abstract View initContentView(Context paramContext);
  
  protected View initView(Context paramContext) {
    View view = View.inflate(paramContext, 2130903301, null);
    this.mContentContainer = (FrameLayout)view.findViewById(2131756998);
    this.mContentContainer.addView(initContentView(paramContext));
    this.mContentContainer.getOnFocusChangeListener();
    return view;
  }
  
  protected void mControl_Fan_changemode(int paramInt1, int paramInt2, boolean paramBoolean) {
    FatherDeviceInfo fatherDeviceInfo;
    this.isContrlAll = paramBoolean;
    if (paramBoolean) {
      FatherDeviceInfo fatherDeviceInfo1 = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
      List<FatherDeviceInfo> list = FatherDeviceInfoDB.getInstance().queryUserList();
      byte b = 0;
      while (true) {
        fatherDeviceInfo = fatherDeviceInfo1;
        if (b < list.size()) {
          String str;
          FanDeviceInfo fanDeviceInfo = FanDeviceInfoDB.getInstance().queryUserList(((FatherDeviceInfo)list.get(b)).getMacAdrass(), fanContorlType);
          StringBuilder stringBuilder = (new StringBuilder()).append("i===").append(b).append("fandeviceinfo==null");
          if (fanDeviceInfo == null) {
            str = "空";
          } else {
            str = "不是空";
          } 
          LogUtil.e("崩溃测试", stringBuilder.append(str).toString());
          fanDeviceInfo.setSonFanMode(paramInt1);
          if (paramInt1 == 3)
            fanDeviceInfo.setSetSonFanSpeed(paramInt2); 
          FanDeviceInfoDB.getInstance().updateUser(fanDeviceInfo);
          b++;
          continue;
        } 
        break;
      } 
    } else {
      fatherDeviceInfo = App.getInstance().getCurrentDevice();
      FanDeviceInfo fanDeviceInfo = FanDeviceInfoDB.getInstance().queryUserList(fatherDeviceInfo.getMacAdrass(), fanContorlType);
      if (paramInt1 == 3)
        fanDeviceInfo.setSetSonFanSpeed(paramInt2); 
      fanDeviceInfo.setSonFanMode(paramInt1);
      FanDeviceInfoDB.getInstance().updateUser(fanDeviceInfo);
    } 
    DataControler.getInstance().sendFanData(fatherDeviceInfo, paramBoolean);
  }
  
  protected void mControl_Light_Color(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4, int paramInt5) {
    FatherDeviceInfo fatherDeviceInfo;
    this.isContrlAll = paramBoolean1;
    if (paramBoolean1) {
      FatherDeviceInfo fatherDeviceInfo1 = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
      fatherDeviceInfo1.setControlTag(4);
      fatherDeviceInfo1.setColorR(paramInt1);
      fatherDeviceInfo1.setColorG(paramInt2);
      fatherDeviceInfo1.setColorB(paramInt3);
      if (paramInt5 != -1)
        fatherDeviceInfo1.setColorFineness(paramInt5); 
      List<FatherDeviceInfo> list = FatherDeviceInfoDB.getInstance().queryUserList();
      byte b = 0;
      while (true) {
        fatherDeviceInfo = fatherDeviceInfo1;
        if (b < list.size()) {
          if (paramBoolean2) {
            LightDeviceInfo lightDeviceInfo = LightDeviceInfoDB.getInstance().queryUserList(((FatherDeviceInfo)list.get(b)).getMacAdrass(), paramInt4);
            lightDeviceInfo.setColorR(paramInt1);
            lightDeviceInfo.setColorG(paramInt2);
            lightDeviceInfo.setColorB(paramInt3);
            if (paramInt5 != -1)
              lightDeviceInfo.setSonLightFineness(paramInt5); 
            LightDeviceInfoDB.getInstance().updateUser(lightDeviceInfo);
          } else {
            ((FatherDeviceInfo)list.get(b)).setControlTag(4);
            ((FatherDeviceInfo)list.get(b)).setColorR(paramInt1);
            ((FatherDeviceInfo)list.get(b)).setColorG(paramInt2);
            ((FatherDeviceInfo)list.get(b)).setColorB(paramInt3);
            if (paramInt5 != -1)
              ((FatherDeviceInfo)list.get(b)).setColorFineness(paramInt5); 
            FatherDeviceInfoDB.getInstance().updateUser(list.get(b));
          } 
          b++;
          continue;
        } 
        break;
      } 
    } else {
      fatherDeviceInfo = App.getInstance().getCurrentDevice();
      fatherDeviceInfo.setControlTag(4);
      if (!paramBoolean2) {
        fatherDeviceInfo.setColorR(paramInt1);
        fatherDeviceInfo.setColorG(paramInt2);
        fatherDeviceInfo.setColorB(paramInt3);
        if (paramInt5 != -1)
          fatherDeviceInfo.setColorFineness(paramInt5); 
        FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
      } else {
        LightDeviceInfo lightDeviceInfo = LightDeviceInfoDB.getInstance().queryUserList(fatherDeviceInfo.getMacAdrass(), paramInt4);
        lightDeviceInfo.setColorR(paramInt1);
        lightDeviceInfo.setColorG(paramInt2);
        lightDeviceInfo.setColorB(paramInt3);
        if (paramInt5 != -1)
          lightDeviceInfo.setSonLightFineness(paramInt5); 
        LightDeviceInfoDB.getInstance().updateUser(lightDeviceInfo);
      } 
    } 
    DataControler.getInstance().sendLightData(fatherDeviceInfo, paramBoolean1, paramBoolean2, paramInt4);
  }
  
  protected void mControl_Light_Mode(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: iconst_0
    //   4: istore #6
    //   6: aload_0
    //   7: iload_2
    //   8: putfield isContrlAll : Z
    //   11: iload_2
    //   12: ifeq -> 382
    //   15: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   18: getstatic app/gamer/quadstellar/App.macDress : Ljava/lang/String;
    //   21: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   24: astore #7
    //   26: aload #7
    //   28: iload_3
    //   29: invokevirtual setControlTag : (I)V
    //   32: iload_3
    //   33: iconst_2
    //   34: if_icmpne -> 80
    //   37: iload_1
    //   38: bipush #20
    //   40: if_icmpne -> 80
    //   43: aload #7
    //   45: iload_1
    //   46: invokevirtual setLigtMode : (I)V
    //   49: invokestatic getInstance : ()Lapp/gamer/quadstellar/controler/DataControler;
    //   52: astore #8
    //   54: iload #4
    //   56: ifeq -> 73
    //   59: iconst_0
    //   60: istore_1
    //   61: aload #8
    //   63: aload #7
    //   65: iload_2
    //   66: iload #4
    //   68: iload_1
    //   69: invokevirtual sendLightData : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;ZZI)V
    //   72: return
    //   73: sipush #255
    //   76: istore_1
    //   77: goto -> 61
    //   80: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   83: invokevirtual queryUserList : ()Ljava/util/List;
    //   86: astore #9
    //   88: iconst_0
    //   89: istore #6
    //   91: aload #7
    //   93: astore #8
    //   95: iload #6
    //   97: aload #9
    //   99: invokeinterface size : ()I
    //   104: if_icmpge -> 500
    //   107: aload #9
    //   109: iload #6
    //   111: invokeinterface get : (I)Ljava/lang/Object;
    //   116: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   119: iload_3
    //   120: invokevirtual setControlTag : (I)V
    //   123: iload_3
    //   124: lookupswitch default -> 168, 2 -> 192, 8 -> 247, 16 -> 357, 32 -> 302
    //   168: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   171: aload #9
    //   173: iload #6
    //   175: invokeinterface get : (I)Ljava/lang/Object;
    //   180: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   183: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;)V
    //   186: iinc #6, 1
    //   189: goto -> 91
    //   192: iload #4
    //   194: ifeq -> 222
    //   197: aload #9
    //   199: iload #6
    //   201: invokeinterface get : (I)Ljava/lang/Object;
    //   206: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   209: iload_1
    //   210: invokevirtual setPartLightMode : (I)V
    //   213: aload #7
    //   215: iload_1
    //   216: invokevirtual setPartLightMode : (I)V
    //   219: goto -> 168
    //   222: aload #9
    //   224: iload #6
    //   226: invokeinterface get : (I)Ljava/lang/Object;
    //   231: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   234: iload_1
    //   235: invokevirtual setLigtMode : (I)V
    //   238: aload #7
    //   240: iload_1
    //   241: invokevirtual setLigtMode : (I)V
    //   244: goto -> 168
    //   247: iload #4
    //   249: ifne -> 277
    //   252: aload #9
    //   254: iload #6
    //   256: invokeinterface get : (I)Ljava/lang/Object;
    //   261: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   264: iload_1
    //   265: invokevirtual setColorBrightness : (I)V
    //   268: aload #7
    //   270: iload_1
    //   271: invokevirtual setColorBrightness : (I)V
    //   274: goto -> 168
    //   277: aload #9
    //   279: iload #6
    //   281: invokeinterface get : (I)Ljava/lang/Object;
    //   286: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   289: iload_1
    //   290: invokevirtual setPartLightBrigtness : (I)V
    //   293: aload #7
    //   295: iload_1
    //   296: invokevirtual setPartLightBrigtness : (I)V
    //   299: goto -> 168
    //   302: iload #4
    //   304: ifne -> 332
    //   307: aload #9
    //   309: iload #6
    //   311: invokeinterface get : (I)Ljava/lang/Object;
    //   316: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   319: iload_1
    //   320: invokevirtual setColorSpeed : (I)V
    //   323: aload #7
    //   325: iload_1
    //   326: invokevirtual setColorSpeed : (I)V
    //   329: goto -> 168
    //   332: aload #9
    //   334: iload #6
    //   336: invokeinterface get : (I)Ljava/lang/Object;
    //   341: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   344: iload_1
    //   345: invokevirtual setPartLightSpeed : (I)V
    //   348: aload #7
    //   350: iload_1
    //   351: invokevirtual setPartLightSpeed : (I)V
    //   354: goto -> 168
    //   357: aload #9
    //   359: iload #6
    //   361: invokeinterface get : (I)Ljava/lang/Object;
    //   366: checkcast app/gamer/quadstellar/domain/FatherDeviceInfo
    //   369: iload_1
    //   370: invokevirtual setColorFineness : (I)V
    //   373: aload #7
    //   375: iload_1
    //   376: invokevirtual setColorFineness : (I)V
    //   379: goto -> 168
    //   382: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   385: invokevirtual getCurrentDevice : ()Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   388: astore #8
    //   390: aload #8
    //   392: iload_3
    //   393: invokevirtual setControlTag : (I)V
    //   396: iload_3
    //   397: iconst_2
    //   398: if_icmpne -> 447
    //   401: iload_1
    //   402: bipush #20
    //   404: if_icmpne -> 447
    //   407: aload #8
    //   409: iload_1
    //   410: invokevirtual setLigtMode : (I)V
    //   413: invokestatic getInstance : ()Lapp/gamer/quadstellar/controler/DataControler;
    //   416: astore #7
    //   418: iload #4
    //   420: ifeq -> 440
    //   423: iload #6
    //   425: istore_1
    //   426: aload #7
    //   428: aload #8
    //   430: iload_2
    //   431: iload #4
    //   433: iload_1
    //   434: invokevirtual sendLightData : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;ZZI)V
    //   437: goto -> 72
    //   440: sipush #255
    //   443: istore_1
    //   444: goto -> 426
    //   447: iload_3
    //   448: lookupswitch default -> 492, 2 -> 527, 8 -> 550, 16 -> 596, 32 -> 573
    //   492: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   495: aload #8
    //   497: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;)V
    //   500: invokestatic getInstance : ()Lapp/gamer/quadstellar/controler/DataControler;
    //   503: astore #7
    //   505: iload #4
    //   507: ifeq -> 605
    //   510: iload #5
    //   512: istore_1
    //   513: aload #7
    //   515: aload #8
    //   517: iload_2
    //   518: iload #4
    //   520: iload_1
    //   521: invokevirtual sendLightData : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;ZZI)V
    //   524: goto -> 72
    //   527: iload #4
    //   529: ifeq -> 541
    //   532: aload #8
    //   534: iload_1
    //   535: invokevirtual setPartLightMode : (I)V
    //   538: goto -> 492
    //   541: aload #8
    //   543: iload_1
    //   544: invokevirtual setLigtMode : (I)V
    //   547: goto -> 492
    //   550: iload #4
    //   552: ifne -> 564
    //   555: aload #8
    //   557: iload_1
    //   558: invokevirtual setColorBrightness : (I)V
    //   561: goto -> 492
    //   564: aload #8
    //   566: iload_1
    //   567: invokevirtual setPartLightBrigtness : (I)V
    //   570: goto -> 492
    //   573: iload #4
    //   575: ifne -> 587
    //   578: aload #8
    //   580: iload_1
    //   581: invokevirtual setColorSpeed : (I)V
    //   584: goto -> 492
    //   587: aload #8
    //   589: iload_1
    //   590: invokevirtual setPartLightSpeed : (I)V
    //   593: goto -> 492
    //   596: aload #8
    //   598: iload_1
    //   599: invokevirtual setColorFineness : (I)V
    //   602: goto -> 492
    //   605: sipush #255
    //   608: istore_1
    //   609: goto -> 513
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Base/TabController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */