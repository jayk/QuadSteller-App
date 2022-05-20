package app.gamer.quadstellar.controler;

import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.db.FanDeviceInfoDB;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.db.LightDeviceInfoDB;
import app.gamer.quadstellar.domain.FanDeviceInfo;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.LightDeviceInfo;
import app.gamer.quadstellar.net.ThreadManger;
import app.gamer.quadstellar.net.UdpClient;
import app.gamer.quadstellar.newdevices.Base.TabController;
import app.gamer.quadstellar.utils.ByteUtils;
import java.util.List;

public class DataControler {
  private static DataControler mDataControler;
  
  private float[] mHSVColor = new float[3];
  
  public static DataControler getInstance() {
    // Byte code:
    //   0: ldc app/gamer/quadstellar/controler/DataControler
    //   2: monitorenter
    //   3: getstatic app/gamer/quadstellar/controler/DataControler.mDataControler : Lapp/gamer/quadstellar/controler/DataControler;
    //   6: ifnonnull -> 33
    //   9: ldc app/gamer/quadstellar/controler/DataControler
    //   11: monitorenter
    //   12: getstatic app/gamer/quadstellar/controler/DataControler.mDataControler : Lapp/gamer/quadstellar/controler/DataControler;
    //   15: ifnonnull -> 30
    //   18: new app/gamer/quadstellar/controler/DataControler
    //   21: astore_0
    //   22: aload_0
    //   23: invokespecial <init> : ()V
    //   26: aload_0
    //   27: putstatic app/gamer/quadstellar/controler/DataControler.mDataControler : Lapp/gamer/quadstellar/controler/DataControler;
    //   30: ldc app/gamer/quadstellar/controler/DataControler
    //   32: monitorexit
    //   33: ldc app/gamer/quadstellar/controler/DataControler
    //   35: monitorexit
    //   36: getstatic app/gamer/quadstellar/controler/DataControler.mDataControler : Lapp/gamer/quadstellar/controler/DataControler;
    //   39: areturn
    //   40: astore_0
    //   41: ldc app/gamer/quadstellar/controler/DataControler
    //   43: monitorexit
    //   44: aload_0
    //   45: athrow
    //   46: astore_0
    //   47: ldc app/gamer/quadstellar/controler/DataControler
    //   49: monitorexit
    //   50: aload_0
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   3	12	46	finally
    //   12	30	40	finally
    //   30	33	40	finally
    //   33	36	46	finally
    //   41	44	40	finally
    //   44	46	46	finally
    //   47	50	46	finally
  }
  
  public void sendData(final FatherDeviceInfo fatherDeviceInfo, boolean paramBoolean, final byte[] data) {
    final byte[] date = ByteUtils.appendAuto(new byte[][] { Prefix.CONTROL_TYPE, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), ByteUtils.getPhonenumberBytes(), ByteUtils.hexStringToBytes(fatherDeviceInfo.getMacAdrass()), data });
    if (paramBoolean) {
      ThreadManger.execute(new Runnable() {
            public void run() {
              for (FatherDeviceInfo fatherDeviceInfo : FatherDeviceInfoDB.getInstance().queryUserList()) {
                byte[] arrayOfByte = ByteUtils.appendAuto(new byte[][] { Prefix.CONTROL_TYPE, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), ByteUtils.getPhonenumberBytes(), ByteUtils.hexStringToBytes(fatherDeviceInfo.getMacAdrass()), this.val$data });
                UdpClient.getInstance().sendData(7075, fatherDeviceInfo.getDeviceIP(), arrayOfByte, null);
              } 
              for (byte b = 0; b < 2; b++)
                UdpClient.getInstance().sendData(7075, fatherDeviceInfo.getDeviceIP(), date, null); 
            }
          });
      return;
    } 
    UdpClient.getInstance().sendData(7075, fatherDeviceInfo.getDeviceIP(), arrayOfByte, null);
  }
  
  public void sendFanData(FatherDeviceInfo paramFatherDeviceInfo, boolean paramBoolean) {
    byte b = 3;
    byte[] arrayOfByte = new byte[8];
    arrayOfByte[0] = (byte)(byte)arrayOfByte.length;
    arrayOfByte[1] = (byte)2;
    arrayOfByte[2] = (byte)1;
    arrayOfByte[3] = (byte)(byte)TabController.fanContorlType;
    FanDeviceInfo fanDeviceInfo = FanDeviceInfoDB.getInstance().queryUserList(paramFatherDeviceInfo.getMacAdrass(), TabController.fanContorlType);
    if (fanDeviceInfo.getSonFanMode() != 3)
      b = 1; 
    arrayOfByte[4] = (byte)b;
    arrayOfByte[5] = (byte)(byte)fanDeviceInfo.getSonFanMode();
    arrayOfByte[6] = (byte)(byte)fanDeviceInfo.getSetSonFanSpeed();
    sendData(paramFatherDeviceInfo, paramBoolean, arrayOfByte);
  }
  
  public void sendLightData(FatherDeviceInfo paramFatherDeviceInfo, boolean paramBoolean1, boolean paramBoolean2, int paramInt) {
    int i;
    if (paramBoolean2 && (paramFatherDeviceInfo.getControlTag() == 8 || paramInt == -1)) {
      i = 65;
    } else {
      i = 15;
    } 
    byte[] arrayOfByte = new byte[i];
    arrayOfByte[0] = (byte)(byte)arrayOfByte.length;
    arrayOfByte[1] = (byte)1;
    arrayOfByte[2] = (byte)(byte)paramFatherDeviceInfo.getToggleState();
    if (paramBoolean2) {
      i = paramFatherDeviceInfo.getPartLightMode();
    } else {
      i = paramFatherDeviceInfo.getLigtMode();
    } 
    arrayOfByte[3] = (byte)(byte)i;
    if (paramFatherDeviceInfo.getLigtMode() == 20) {
      sendData(paramFatherDeviceInfo, paramBoolean1, arrayOfByte);
      return;
    } 
    if (paramBoolean2 && paramFatherDeviceInfo.getControlTag() == 4 && paramInt != -1) {
      i = 1;
    } else {
      i = 255;
    } 
    arrayOfByte[4] = (byte)(byte)i;
    if (paramBoolean2 && paramFatherDeviceInfo.getControlTag() == 4 && paramInt != -1) {
      i = paramInt;
    } else {
      i = 255;
    } 
    arrayOfByte[5] = (byte)(byte)i;
    if (paramInt == -1) {
      arrayOfByte[6] = (byte)46;
    } else if (paramFatherDeviceInfo.getControlTag() == 4 || paramFatherDeviceInfo.getControlTag() == 8) {
      arrayOfByte[6] = (byte)12;
    } else {
      arrayOfByte[6] = (byte)(byte)paramFatherDeviceInfo.getControlTag();
    } 
    arrayOfByte[7] = (byte)(byte)paramFatherDeviceInfo.getToggleState();
    arrayOfByte[8] = (byte)0;
    if (paramBoolean2 && paramInt != 255) {
      if (paramInt == -1 || paramFatherDeviceInfo.getControlTag() == 8) {
        arrayOfByte[4] = (byte)6;
        List list = LightDeviceInfoDB.getInstance().queryUserList(paramFatherDeviceInfo.getMacAdrass());
        for (i = 0; i < list.size(); i++) {
          byte b;
          int j = i * 10;
          arrayOfByte[j + 5] = (byte)(byte)i;
          if (paramInt == -1) {
            b = 46;
          } else {
            b = 12;
          } 
          arrayOfByte[j + 6] = (byte)(byte)b;
          arrayOfByte[j + 7] = (byte)(byte)paramFatherDeviceInfo.getToggleState();
          LightDeviceInfo lightDeviceInfo = LightDeviceInfoDB.getInstance().queryUserList(paramFatherDeviceInfo.getMacAdrass(), i);
          arrayOfByte[j + 9] = (byte)(byte)(lightDeviceInfo.getColorR() * paramFatherDeviceInfo.getPartLightBrigtness() / 100);
          arrayOfByte[j + 10] = (byte)(byte)(lightDeviceInfo.getColorG() * paramFatherDeviceInfo.getPartLightBrigtness() / 100);
          arrayOfByte[j + 11] = (byte)(byte)(lightDeviceInfo.getColorB() * paramFatherDeviceInfo.getPartLightBrigtness() / 100);
          arrayOfByte[j + 12] = (byte)(byte)paramFatherDeviceInfo.getPartLightBrigtness();
          arrayOfByte[j + 13] = (byte)(byte)paramFatherDeviceInfo.getColorFineness();
          arrayOfByte[j + 14] = (byte)(byte)paramFatherDeviceInfo.getPartLightSpeed();
        } 
      } else {
        LightDeviceInfo lightDeviceInfo = LightDeviceInfoDB.getInstance().queryUserList(paramFatherDeviceInfo.getMacAdrass(), paramInt);
        arrayOfByte[9] = (byte)(byte)(lightDeviceInfo.getColorR() * paramFatherDeviceInfo.getPartLightBrigtness() / 100);
        arrayOfByte[10] = (byte)(byte)(lightDeviceInfo.getColorG() * paramFatherDeviceInfo.getPartLightBrigtness() / 100);
        arrayOfByte[11] = (byte)(byte)(lightDeviceInfo.getColorB() * paramFatherDeviceInfo.getPartLightBrigtness() / 100);
        arrayOfByte[12] = (byte)(byte)paramFatherDeviceInfo.getPartLightBrigtness();
        arrayOfByte[13] = (byte)(byte)paramFatherDeviceInfo.getColorFineness();
        arrayOfByte[14] = (byte)(byte)paramFatherDeviceInfo.getPartLightSpeed();
      } 
    } else {
      arrayOfByte[9] = (byte)(byte)(paramFatherDeviceInfo.getColorR() * paramFatherDeviceInfo.getColorBrightness() / 100);
      arrayOfByte[10] = (byte)(byte)(paramFatherDeviceInfo.getColorG() * paramFatherDeviceInfo.getColorBrightness() / 100);
      arrayOfByte[11] = (byte)(byte)(paramFatherDeviceInfo.getColorB() * paramFatherDeviceInfo.getColorBrightness() / 100);
      arrayOfByte[12] = (byte)(byte)paramFatherDeviceInfo.getColorBrightness();
      arrayOfByte[13] = (byte)(byte)paramFatherDeviceInfo.getColorFineness();
      arrayOfByte[14] = (byte)(byte)paramFatherDeviceInfo.getColorSpeed();
    } 
    sendData(paramFatherDeviceInfo, paramBoolean1, arrayOfByte);
  }
  
  public void sendTempData(FatherDeviceInfo paramFatherDeviceInfo, int paramInt, boolean paramBoolean) {
    byte[] arrayOfByte = new byte[8];
    arrayOfByte[0] = (byte)(byte)arrayOfByte.length;
    arrayOfByte[1] = (byte)3;
    arrayOfByte[2] = (byte)1;
    arrayOfByte[3] = (byte)(byte)paramInt;
    sendData(paramFatherDeviceInfo, paramBoolean, arrayOfByte);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/controler/DataControler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */