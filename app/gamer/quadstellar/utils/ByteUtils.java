package app.gamer.quadstellar.utils;

import android.os.Bundle;
import android.util.Log;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.mode.EFAlarm;
import app.gamer.quadstellar.mode.EFBridge;
import app.gamer.quadstellar.mode.EFCamera;
import app.gamer.quadstellar.mode.EFDeviceCurtains;
import app.gamer.quadstellar.mode.EFDeviceDoorLock;
import app.gamer.quadstellar.mode.EFDeviceLight;
import app.gamer.quadstellar.mode.EFDeviceMusic;
import app.gamer.quadstellar.mode.EFDeviceOutlet;
import app.gamer.quadstellar.mode.EFDeviceSwitch;
import app.gamer.quadstellar.mode.EFDeviceYaoKong;
import app.gamer.quadstellar.mode.EFPowerStripTimer;
import app.gamer.quadstellar.mode.EFTimer;
import app.gamer.quadstellar.mode.EntityBase;
import app.gamer.quadstellar.mode.LinkageMode;
import app.gamer.quadstellar.mode.dao.DaoUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xutils.db.sqlite.WhereBuilder;

public class ByteUtils {
  public static byte[] append(int paramInt, byte[]... paramVarArgs) {
    byte[] arrayOfByte = new byte[paramInt];
    paramInt = 0;
    for (byte b = 0; b < paramVarArgs.length; b++) {
      System.arraycopy(paramVarArgs[b], 0, arrayOfByte, paramInt, (paramVarArgs[b]).length);
      paramInt += (paramVarArgs[b]).length;
    } 
    return arrayOfByte;
  }
  
  public static byte[] appendAuto(byte[]... paramVarArgs) {
    int i = 0;
    int j = paramVarArgs.length;
    byte b = 0;
    while (b < j) {
      byte[] arrayOfByte1 = paramVarArgs[b];
      int m = i;
      if (arrayOfByte1 != null)
        m = i + arrayOfByte1.length; 
      b++;
      i = m;
    } 
    byte[] arrayOfByte = new byte[i];
    int k = 0;
    for (b = 0; b < paramVarArgs.length; b++) {
      if (paramVarArgs[b] != null) {
        System.arraycopy(paramVarArgs[b], 0, arrayOfByte, k, (paramVarArgs[b]).length);
        k += (paramVarArgs[b]).length;
      } 
    } 
    return arrayOfByte;
  }
  
  public static byte[] arrayCopyBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public static byte[] arrayCopyBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
    byte[] arrayOfByte = new byte[paramInt3];
    System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public static String bytesToHexString(byte[] paramArrayOfbyte) {
    StringBuffer stringBuffer = new StringBuffer();
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      String str = Integer.toHexString(paramArrayOfbyte[b] & 0xFF).toUpperCase();
      if (str.length() == 1)
        stringBuffer.append('0'); 
      stringBuffer.append(str);
    } 
    return stringBuffer.toString().toUpperCase();
  }
  
  public static int bytesToInt(byte[] paramArrayOfbyte, int paramInt) {
    return paramArrayOfbyte[paramInt] & 0xFF | (paramArrayOfbyte[paramInt + 1] & 0xFF) << 8 | (paramArrayOfbyte[paramInt + 2] & 0xFF) << 16 | (paramArrayOfbyte[paramInt + 3] & 0xFF) << 24;
  }
  
  public static int chaeckMatchResult(byte[] paramArrayOfbyte) {
    byte b = 1;
    String str2 = new String(arrayCopyBytes(paramArrayOfbyte, 1, 13));
    String str3 = new String(arrayCopyBytes(paramArrayOfbyte, 1, 10));
    String str1 = new String(arrayCopyBytes(paramArrayOfbyte, 1, 8));
    if ("match success".equals(str2))
      return 2; 
    if (!"phonenum".equals(str1.trim())) {
      if ("match fail".equals(str3))
        return 0; 
      b = -1;
    } 
    return b;
  }
  
  private static byte charToByte(char paramChar) {
    return (byte)"0123456789ABCDEF".indexOf(paramChar);
  }
  
  public static boolean checkEnd(byte[] paramArrayOfbyte) {
    return "end".equals(new String(arrayCopyBytes(paramArrayOfbyte, 29, 3)));
  }
  
  public static ArrayList<EFAlarm> decodeAlarmInfo(ControlDevice paramControlDevice, byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length != 36 || paramControlDevice == null)
      return null; 
    ArrayList<EFAlarm> arrayList = new ArrayList();
    byte b = 0;
    while (true) {
      String str;
      ArrayList<EFAlarm> arrayList1 = arrayList;
      if (b < 3) {
        int i = b * 12;
        arrayList1 = arrayList;
        if ((paramArrayOfbyte[i] & 0xFF) != 255) {
          str = getAlarmDeviceName(paramControlDevice, bytesToHexString(arrayCopyBytes(paramArrayOfbyte, i, 8)));
          EFAlarm eFAlarm = new EFAlarm();
          eFAlarm.setName(str);
          eFAlarm.setMonth(paramArrayOfbyte[i + 8] & 0xFF);
          eFAlarm.setDay(paramArrayOfbyte[i + 9] & 0xFF);
          eFAlarm.setHour(paramArrayOfbyte[i + 10] & 0xFF);
          eFAlarm.setMinute(paramArrayOfbyte[i + 11] & 0xFF);
          arrayList.add(eFAlarm);
          b++;
          continue;
        } 
      } 
      return (ArrayList<EFAlarm>)str;
    } 
  }
  
  public static List<EFDeviceLight> decodeLampStatus(byte[] paramArrayOfbyte) {
    if (!(new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone")))
      return null; 
    ArrayList<EFDeviceLight> arrayList = new ArrayList();
    byte b = 1;
    while (true) {
      EFDeviceLight eFDeviceLight;
      ArrayList<EFDeviceLight> arrayList1 = arrayList;
      if (b < 6) {
        byte[] arrayOfByte = arrayCopyBytes(paramArrayOfbyte, (b + 1) * 10, 10);
        arrayList1 = arrayList;
        if (bytesToInt(arrayOfByte, 2) != 0) {
          eFDeviceLight = new EFDeviceLight();
          eFDeviceLight.setDeviceType(arrayOfByte[0] & 0xFF);
          eFDeviceLight.setDeviceState(arrayOfByte[1] & 0xFF);
          eFDeviceLight.setDeviceMac(bytesToHexString(arrayCopyBytes(arrayOfByte, 2, 8)));
          arrayList.add(eFDeviceLight);
          b++;
          continue;
        } 
      } 
      return (List<EFDeviceLight>)eFDeviceLight;
    } 
  }
  
  public static double decodePowerData(byte[] paramArrayOfbyte, String paramString) {
    return !(new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone")) ? 0.0D : (((paramArrayOfbyte[20] & 0xFF) != 9) ? 0.0D : (!bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 21, 6)).equals(paramString) ? 0.0D : (((paramArrayOfbyte[27] << 8) + (paramArrayOfbyte[28] & 0xFF)) + ((paramArrayOfbyte[29] << 8) + (paramArrayOfbyte[30] & 0xFF)) % 10000.0D / 10000.0D)));
  }
  
  public static EFPowerStripTimer decodePowerStripTimer(byte[] paramArrayOfbyte) {
    boolean bool;
    if (!(new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone")))
      return null; 
    if ((paramArrayOfbyte[20] & 0xFF) != 9)
      return null; 
    EFPowerStripTimer eFPowerStripTimer = new EFPowerStripTimer();
    eFPowerStripTimer.setNumber(paramArrayOfbyte[27] & 0xFF);
    if (!"no".equals((new String(arrayCopyBytes(paramArrayOfbyte, 28, 2))).trim())) {
      eFPowerStripTimer.setOpenHour(paramArrayOfbyte[28] & 0xFF);
      eFPowerStripTimer.setOpenMinute(paramArrayOfbyte[29] & 0xFF);
      eFPowerStripTimer.setOpenEdited(true);
      eFPowerStripTimer.setType(1);
    } else {
      eFPowerStripTimer.setOpenEdited(false);
    } 
    if (!"no".equals((new String(arrayCopyBytes(paramArrayOfbyte, 30, 2))).trim())) {
      eFPowerStripTimer.setCloseHour(paramArrayOfbyte[30] & 0xFF);
      eFPowerStripTimer.setCloseMinute(paramArrayOfbyte[31] & 0xFF);
      eFPowerStripTimer.setCloseEdited(true);
      eFPowerStripTimer.setType(2);
    } else {
      eFPowerStripTimer.setCloseEdited(false);
    } 
    byte b = paramArrayOfbyte[32];
    if (b == Byte.MIN_VALUE) {
      eFPowerStripTimer.setRepeatType(10);
    } else if (b == Byte.MAX_VALUE) {
      eFPowerStripTimer.setRepeatType(11);
    } else if (b == 31) {
      eFPowerStripTimer.setRepeatType(12);
    } else if (b == 96) {
      eFPowerStripTimer.setRepeatType(13);
    } else {
      eFPowerStripTimer.setRepeatType(14);
      eFPowerStripTimer.setCustomList(eFPowerStripTimer.convertToList(b));
    } 
    int i = paramArrayOfbyte[33] & 0xFF;
    int j = i >> 7;
    if (j == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    eFPowerStripTimer.setState(bool);
    eFPowerStripTimer.setControlType(i - j * 128);
    return eFPowerStripTimer;
  }
  
  public static EFDeviceOutlet decodeSigOutlet(byte[] paramArrayOfbyte) {
    EFDeviceOutlet eFDeviceOutlet = null;
    if (!(new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone")))
      return null; 
    int i = paramArrayOfbyte[20] & 0xFF;
    if (i == 2) {
      eFDeviceOutlet = new EFDeviceOutlet();
      eFDeviceOutlet.setDeviceType(i);
      eFDeviceOutlet.setDeviceState(paramArrayOfbyte[21] & 0xFF);
      eFDeviceOutlet.setDeviceMac(bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 8)));
      return eFDeviceOutlet;
    } 
    if (i == 4 || i == 9) {
      if ((paramArrayOfbyte[32] & 0xFF) == 255);
      eFDeviceOutlet = new EFDeviceOutlet();
      eFDeviceOutlet.setDeviceType(i);
      eFDeviceOutlet.setDeviceState(paramArrayOfbyte[21] & 0xFF);
      eFDeviceOutlet.setDeviceMac(bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 6)));
      eFDeviceOutlet.setLock(paramArrayOfbyte[28] & 0xFF);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramArrayOfbyte[29] & 0xFF);
      stringBuilder.append(".");
      stringBuilder.append(paramArrayOfbyte[30] & 0xFF);
      stringBuilder.append(".");
      stringBuilder.append(paramArrayOfbyte[31] & 0xFF);
      eFDeviceOutlet.setFirmwareVersion(stringBuilder.toString());
    } 
    return eFDeviceOutlet;
  }
  
  public static EFDeviceLight decodeSigWifiLamp(byte[] paramArrayOfbyte) {
    EFDeviceLight eFDeviceLight = null;
    int i = paramArrayOfbyte[20] & 0xFF;
    if (i == 5 && (new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone"))) {
      if ((paramArrayOfbyte[32] & 0xFF) == 255);
      eFDeviceLight = new EFDeviceLight();
      eFDeviceLight.setDeviceType(i);
      eFDeviceLight.setDeviceState(paramArrayOfbyte[21] & 0xFF);
      eFDeviceLight.setDeviceMac(bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 6)));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramArrayOfbyte[28] & 0xFF);
      stringBuilder.append(".");
      stringBuilder.append(paramArrayOfbyte[29] & 0xFF);
      stringBuilder.append(".");
      stringBuilder.append(paramArrayOfbyte[30] & 0xFF);
      eFDeviceLight.setFirmwareVersion(stringBuilder.toString());
    } 
    return eFDeviceLight;
  }
  
  public static EFDeviceCurtains decodeSigZigCurtain(byte[] paramArrayOfbyte) {
    EFDeviceCurtains eFDeviceCurtains = null;
    int i = paramArrayOfbyte[20] & 0xFF;
    if (i == 7 && (new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone"))) {
      eFDeviceCurtains = new EFDeviceCurtains();
      eFDeviceCurtains.setDeviceType(i);
      i = paramArrayOfbyte[21] & 0xFF;
      if (i >= 128) {
        eFDeviceCurtains.setDeviceState(i - 128);
        eFDeviceCurtains.setDirection(1);
      } else {
        eFDeviceCurtains.setDeviceState(i);
        eFDeviceCurtains.setDirection(0);
      } 
      eFDeviceCurtains.setDeviceMac(bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 8)));
    } 
    return eFDeviceCurtains;
  }
  
  public static EFDeviceDoorLock decodeSigZigDoorLock(byte[] paramArrayOfbyte) {
    EFDeviceDoorLock eFDeviceDoorLock = null;
    int i = paramArrayOfbyte[20] & 0xFF;
    if (i == 14 && (new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone"))) {
      eFDeviceDoorLock = new EFDeviceDoorLock();
      eFDeviceDoorLock.setDeviceType(i);
      i = paramArrayOfbyte[21] & 0xFF;
      if (i == 0) {
        eFDeviceDoorLock.setDeviceState(0);
      } else {
        eFDeviceDoorLock.setDeviceState(i);
      } 
      eFDeviceDoorLock.setDeviceMac(bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 8)));
    } 
    return eFDeviceDoorLock;
  }
  
  public static EFDeviceLight decodeSigZigLamp(byte[] paramArrayOfbyte) {
    EFDeviceLight eFDeviceLight = null;
    int i = paramArrayOfbyte[20] & 0xFF;
    if (i == 1 && (new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone"))) {
      eFDeviceLight = new EFDeviceLight();
      eFDeviceLight.setDeviceType(i);
      eFDeviceLight.setDeviceState(paramArrayOfbyte[21] & 0xFF);
      eFDeviceLight.setDeviceMac(bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 8)));
    } 
    return eFDeviceLight;
  }
  
  public static EFDeviceSwitch decodeSigZigSwitch(byte[] paramArrayOfbyte) {
    EFDeviceSwitch eFDeviceSwitch = null;
    int i = paramArrayOfbyte[20] & 0xFF;
    if ((i == 3 || i == 10 || i == 11) && (new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone"))) {
      eFDeviceSwitch = new EFDeviceSwitch();
      eFDeviceSwitch.setDeviceType(i);
      eFDeviceSwitch.setDeviceState(paramArrayOfbyte[21] & 0xFF);
      eFDeviceSwitch.setDeviceMac(bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 8)));
    } 
    return eFDeviceSwitch;
  }
  
  public static EFTimer decodeTimer(byte[] paramArrayOfbyte) {
    boolean bool;
    if (!(new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone")))
      return null; 
    int i = paramArrayOfbyte[20] & 0xFF;
    if (i != 4 && i != 5)
      return null; 
    EFTimer eFTimer = new EFTimer();
    eFTimer.setNumber(paramArrayOfbyte[27] & 0xFF);
    if (!"no".equals((new String(arrayCopyBytes(paramArrayOfbyte, 28, 2))).trim())) {
      eFTimer.setOpenHour(paramArrayOfbyte[28] & 0xFF);
      eFTimer.setOpenMinute(paramArrayOfbyte[29] & 0xFF);
      eFTimer.setOpenEdited(true);
      eFTimer.setType(1);
    } else {
      eFTimer.setOpenEdited(false);
    } 
    if (!"no".equals((new String(arrayCopyBytes(paramArrayOfbyte, 30, 2))).trim())) {
      eFTimer.setCloseHour(paramArrayOfbyte[30] & 0xFF);
      eFTimer.setCloseMinute(paramArrayOfbyte[31] & 0xFF);
      eFTimer.setCloseEdited(true);
      eFTimer.setType(2);
    } else {
      eFTimer.setCloseEdited(false);
    } 
    byte b = paramArrayOfbyte[32];
    if (b == Byte.MIN_VALUE) {
      eFTimer.setRepeatType(10);
    } else if (b == Byte.MAX_VALUE) {
      eFTimer.setRepeatType(11);
    } else if (b == 31) {
      eFTimer.setRepeatType(12);
    } else if (b == 96) {
      eFTimer.setRepeatType(13);
    } else {
      eFTimer.setRepeatType(14);
      eFTimer.setCustomList(eFTimer.convertToList(b));
    } 
    if ((paramArrayOfbyte[33] & 0xFF) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    eFTimer.setState(bool);
    return eFTimer;
  }
  
  public static Bundle decodeWifiDevice(byte[] paramArrayOfbyte) {
    if (!(new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone")))
      return null; 
    int i = paramArrayOfbyte[20] & 0xFF;
    Bundle bundle2 = new Bundle();
    bundle2.putInt("deviceType", i);
    if (i == 4 || i == 9) {
      String str = bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 6));
      EFDeviceOutlet eFDeviceOutlet = new EFDeviceOutlet();
      eFDeviceOutlet.setDeviceType(i);
      eFDeviceOutlet.setDeviceState(paramArrayOfbyte[21] & 0xFF);
      eFDeviceOutlet.setDeviceMac(str);
      eFDeviceOutlet.setParentMac(str);
      eFDeviceOutlet.setLock(paramArrayOfbyte[28] & 0xFF);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramArrayOfbyte[29] & 0xFF);
      stringBuilder.append(".");
      stringBuilder.append(paramArrayOfbyte[30] & 0xFF);
      stringBuilder.append(".");
      stringBuilder.append(paramArrayOfbyte[31] & 0xFF);
      eFDeviceOutlet.setFirmwareVersion(stringBuilder.toString());
      updateOutlet(eFDeviceOutlet);
      updateWifiMacs(str, i);
      bundle2.putString("deviceMac", str);
      return bundle2;
    } 
    if (i == 5) {
      String str = bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 22, 6));
      EFDeviceLight eFDeviceLight = new EFDeviceLight();
      eFDeviceLight.setDeviceType(i);
      eFDeviceLight.setDeviceState(paramArrayOfbyte[21] & 0xFF);
      eFDeviceLight.setDeviceMac(str);
      eFDeviceLight.setParentMac(str);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramArrayOfbyte[28] & 0xFF);
      stringBuilder.append(".");
      stringBuilder.append(paramArrayOfbyte[29] & 0xFF);
      stringBuilder.append(".");
      stringBuilder.append(paramArrayOfbyte[30] & 0xFF);
      eFDeviceLight.setFirmwareVersion(stringBuilder.toString());
      updateLight(eFDeviceLight);
      updateWifiMacs(str, i);
      bundle2.putString("deviceMac", str);
      return bundle2;
    } 
    Bundle bundle1 = bundle2;
    if (i == 6) {
      String str = bytesToHexString(arrayCopyBytes(paramArrayOfbyte, 21, 6));
      updateTeleMacs(str);
      bundle2.putString("deviceMac", str);
      bundle1 = bundle2;
    } 
    return bundle1;
  }
  
  public static boolean decodeZigDevice(byte[] paramArrayOfbyte) {
    if (!doCheckCode(paramArrayOfbyte))
      return false; 
    if (!(new String(arrayCopyBytes(paramArrayOfbyte, 0, 20))).trim().equals(PreferenceHelper.readString("user_phone")))
      return false; 
    String str = App.getInstance().getCurrentDeviceMac();
    byte b = 1;
    while (true) {
      if (b < 6) {
        byte[] arrayOfByte = arrayCopyBytes(paramArrayOfbyte, (b + 1) * 10, 10);
        if (bytesToInt(arrayOfByte, 2) != 0) {
          EFDeviceLight eFDeviceLight;
          EFDeviceOutlet eFDeviceOutlet;
          EFDeviceSwitch eFDeviceSwitch;
          EFDeviceCurtains eFDeviceCurtains;
          EFDeviceDoorLock eFDeviceDoorLock;
          int i = arrayOfByte[0] & 0xFF;
          switch (i) {
            case 1:
              eFDeviceLight = new EFDeviceLight();
              eFDeviceLight.setDeviceType(i);
              eFDeviceLight.setDeviceState(arrayOfByte[1] & 0xFF);
              eFDeviceLight.setDeviceMac(bytesToHexString(arrayCopyBytes(arrayOfByte, 2, 8)));
              eFDeviceLight.setParentMac(str);
              updateLight(eFDeviceLight);
              b++;
              break;
            case 2:
              eFDeviceOutlet = new EFDeviceOutlet();
              eFDeviceOutlet.setDeviceType(i);
              eFDeviceOutlet.setDeviceState(arrayOfByte[1] & 0xFF);
              eFDeviceOutlet.setDeviceMac(bytesToHexString(arrayCopyBytes(arrayOfByte, 2, 8)));
              eFDeviceOutlet.setParentMac(str);
              updateOutlet(eFDeviceOutlet);
              b++;
              break;
            case 3:
            case 10:
            case 11:
              eFDeviceSwitch = new EFDeviceSwitch();
              eFDeviceSwitch.setDeviceType(i);
              eFDeviceSwitch.setDeviceState(arrayOfByte[1] & 0xFF);
              eFDeviceSwitch.setDeviceMac(bytesToHexString(arrayCopyBytes(arrayOfByte, 2, 8)));
              eFDeviceSwitch.setParentMac(str);
              updateSwitch(eFDeviceSwitch);
              b++;
              break;
            case 7:
              eFDeviceCurtains = new EFDeviceCurtains();
              eFDeviceCurtains.setDeviceType(i);
              eFDeviceCurtains.setDeviceState(arrayOfByte[1] & 0xFF);
              eFDeviceCurtains.setDeviceMac(bytesToHexString(arrayCopyBytes(arrayOfByte, 2, 8)));
              eFDeviceCurtains.setParentMac(str);
              updateCurtain(eFDeviceCurtains);
              b++;
              break;
            case 14:
              eFDeviceDoorLock = new EFDeviceDoorLock();
              eFDeviceDoorLock.setDeviceType(i);
              eFDeviceDoorLock.setDeviceState(arrayOfByte[1] & 0xFF);
              eFDeviceDoorLock.setDeviceMac(bytesToHexString(arrayCopyBytes(arrayOfByte, 2, 8)));
              eFDeviceDoorLock.setParentMac(str);
              updateDoorLock(eFDeviceDoorLock);
              b++;
              break;
          } 
          continue;
        } 
      } 
      return true;
    } 
  }
  
  public static boolean doCheckCode(byte[] paramArrayOfbyte) {
    boolean bool = true;
    byte[] arrayOfByte = getCheckCode(paramArrayOfbyte);
    int[] arrayOfInt = new int[paramArrayOfbyte.length];
    int i;
    for (i = 0; i < arrayOfInt.length; i++)
      arrayOfInt[i] = paramArrayOfbyte[i] & 0xFF; 
    long l = 0L;
    for (i = 0; i < arrayOfInt.length - 2; i += 2)
      l += ((arrayOfInt[i] << 8) + arrayOfInt[i + 1] >> 1 ^ 0x8408); 
    i = (int)(0xFFFFL & l ^ l >> 16L);
    arrayOfInt[paramArrayOfbyte.length - 2] = (byte)(i >> 8 & 0xFF);
    arrayOfInt[paramArrayOfbyte.length - 1] = (byte)(i & 0xFF);
    if ((byte)arrayOfInt[paramArrayOfbyte.length - 1] != arrayOfByte[1] || (byte)(arrayOfInt[paramArrayOfbyte.length - 2] & 0xFF) != arrayOfByte[0])
      bool = false; 
    return bool;
  }
  
  private static String getAlarmDeviceName(ControlDevice paramControlDevice, String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: invokevirtual getType : ()I
    //   6: tableswitch default -> 32, 0 -> 95, 1 -> 51, 2 -> 73
    //   32: aload_2
    //   33: astore_0
    //   34: aload_0
    //   35: ifnull -> 171
    //   38: aload_0
    //   39: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   42: astore_0
    //   43: aload_0
    //   44: ifnonnull -> 168
    //   47: aload_1
    //   48: astore_0
    //   49: aload_0
    //   50: areturn
    //   51: ldc app/gamer/quadstellar/mode/EFDeviceLight
    //   53: ldc_w 'deviceMac'
    //   56: ldc_w '='
    //   59: aload_1
    //   60: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   63: invokestatic queryOne : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Lapp/gamer/quadstellar/mode/EntityBase;
    //   66: checkcast app/gamer/quadstellar/mode/EFDevice
    //   69: astore_0
    //   70: goto -> 34
    //   73: ldc app/gamer/quadstellar/mode/EFDeviceOutlet
    //   75: ldc_w 'deviceMac'
    //   78: ldc_w '='
    //   81: aload_1
    //   82: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   85: invokestatic queryOne : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Lapp/gamer/quadstellar/mode/EntityBase;
    //   88: checkcast app/gamer/quadstellar/mode/EFDevice
    //   91: astore_0
    //   92: goto -> 34
    //   95: ldc app/gamer/quadstellar/mode/EFDeviceLight
    //   97: ldc_w 'deviceMac'
    //   100: ldc_w '='
    //   103: aload_1
    //   104: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   107: invokestatic queryOne : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Lapp/gamer/quadstellar/mode/EntityBase;
    //   110: checkcast app/gamer/quadstellar/mode/EFDevice
    //   113: astore_2
    //   114: aload_2
    //   115: astore_0
    //   116: aload_2
    //   117: ifnonnull -> 34
    //   120: ldc app/gamer/quadstellar/mode/EFDeviceOutlet
    //   122: ldc_w 'deviceMac'
    //   125: ldc_w '='
    //   128: aload_1
    //   129: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   132: invokestatic queryOne : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Lapp/gamer/quadstellar/mode/EntityBase;
    //   135: checkcast app/gamer/quadstellar/mode/EFDevice
    //   138: astore_2
    //   139: aload_2
    //   140: astore_0
    //   141: aload_2
    //   142: ifnonnull -> 34
    //   145: ldc_w app/gamer/quadstellar/mode/EFDeviceSwitch
    //   148: ldc_w 'deviceMac'
    //   151: ldc_w '='
    //   154: aload_1
    //   155: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/xutils/db/sqlite/WhereBuilder;
    //   158: invokestatic queryOne : (Ljava/lang/Class;Lorg/xutils/db/sqlite/WhereBuilder;)Lapp/gamer/quadstellar/mode/EntityBase;
    //   161: checkcast app/gamer/quadstellar/mode/EFDevice
    //   164: astore_0
    //   165: goto -> 34
    //   168: goto -> 49
    //   171: aload_1
    //   172: astore_0
    //   173: goto -> 49
  }
  
  public static byte[] getCheckCode(byte[] paramArrayOfbyte) {
    return arrayCopyBytes(paramArrayOfbyte, 78, 2);
  }
  
  public static String getDeviecIP(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramArrayOfbyte[0] & 0xFF);
    stringBuilder.append('.');
    stringBuilder.append(paramArrayOfbyte[1] & 0xFF);
    stringBuilder.append('.');
    stringBuilder.append(paramArrayOfbyte[2] & 0xFF);
    stringBuilder.append('.');
    stringBuilder.append(paramArrayOfbyte[3] & 0xFF);
    return stringBuilder.toString();
  }
  
  public static byte[] getPhonenumberBytes() {
    byte[] arrayOfByte1 = hexStringToBytes(PreferenceHelper.readString("user_phone", ""));
    byte[] arrayOfByte2 = new byte[20];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    return arrayOfByte2;
  }
  
  public static byte[] getPhonenumberBytes(String paramString) {
    byte[] arrayOfByte1 = hexStringToBytes(paramString);
    byte[] arrayOfByte2 = new byte[20];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    return arrayOfByte2;
  }
  
  public static byte[] getPswBytes(String paramString) {
    byte[] arrayOfByte2 = paramString.getBytes();
    byte[] arrayOfByte1 = new byte[11];
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, arrayOfByte2.length);
    return arrayOfByte1;
  }
  
  public static byte[] getSystemTimeBytes(Long paramLong) {
    return new byte[] { (byte)(int)(paramLong.longValue() >> 4L), (byte)(int)(paramLong.longValue() >> 12L), (byte)(int)(paramLong.longValue() >> 20L), (byte)(int)(paramLong.longValue() >> 28L) };
  }
  
  public static byte[] getUUIDBytes(String paramString) {
    byte[] arrayOfByte1 = paramString.getBytes();
    byte[] arrayOfByte2 = new byte[20];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    return arrayOfByte2;
  }
  
  public static byte[] hexStringToBytes(String paramString) {
    if (paramString == null || paramString.equals(""))
      return null; 
    paramString = paramString.toUpperCase();
    int i = paramString.length() / 2;
    char[] arrayOfChar = paramString.toCharArray();
    byte[] arrayOfByte = new byte[i];
    byte b = 0;
    while (true) {
      byte[] arrayOfByte1 = arrayOfByte;
      if (b < i) {
        int j = b * 2;
        arrayOfByte[b] = (byte)(byte)(charToByte(arrayOfChar[j]) << 4 | charToByte(arrayOfChar[j + 1]));
        b++;
        continue;
      } 
      return arrayOfByte1;
    } 
  }
  
  public static byte[] int2OneByte(int paramInt) {
    return new byte[] { (byte)(paramInt & 0xFF) };
  }
  
  public static byte[] intToRGB(int paramInt1, int paramInt2, int paramInt3) {
    return new byte[] { (byte)(255 - paramInt1 & 0xFF), (byte)(255 - paramInt2 & 0xFF), (byte)(255 - paramInt3 & 0xFF) };
  }
  
  public static boolean isSameTime(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    boolean bool = true;
    if (paramArrayOfbyte1[0] != paramArrayOfbyte2[0] || paramArrayOfbyte1[1] != paramArrayOfbyte2[1] || paramArrayOfbyte1[2] != paramArrayOfbyte2[2] || paramArrayOfbyte1[3] != paramArrayOfbyte2[3])
      bool = false; 
    return bool;
  }
  
  public static byte[] sendUDPHeartData() {
    return append(50, new byte[][] { Prefix.HEART_DATA, getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), getPhonenumberBytes(), Prefix.ALL_MAC, { 2 } });
  }
  
  public static String subPhoneNumber(byte[] paramArrayOfbyte) {
    return new String(arrayCopyBytes(paramArrayOfbyte, 9, 20));
  }
  
  public static byte[] sysCopy(List<byte[]> paramList) {
    int i = 0;
    Iterator<byte> iterator = paramList.iterator();
    while (iterator.hasNext())
      i += ((byte[])iterator.next()).length; 
    byte[] arrayOfByte = new byte[i];
    i = 0;
    for (byte[] arrayOfByte1 : paramList) {
      System.arraycopy(arrayOfByte1, 0, arrayOfByte, i, arrayOfByte1.length);
      i += arrayOfByte1.length;
    } 
    return arrayOfByte;
  }
  
  public static void updateBridge(EFBridge paramEFBridge) {
    if (paramEFBridge != null) {
      EFBridge eFBridge = (EFBridge)DaoUtil.queryOne(EFBridge.class, WhereBuilder.b("deviceMac", "=", paramEFBridge.getDeviceMac()));
      if (eFBridge != null) {
        eFBridge.setDeviceState(paramEFBridge.getDeviceState());
        eFBridge.setParentMac(paramEFBridge.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFBridge);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFBridge);
    } 
  }
  
  public static void updateCamera(EFCamera paramEFCamera) {
    if (paramEFCamera != null) {
      EFCamera eFCamera = (EFCamera)DaoUtil.queryOne(EFCamera.class, WhereBuilder.b("deviceMac", "=", paramEFCamera.getDeviceMac()));
      if (eFCamera != null) {
        eFCamera.setDeviceState(paramEFCamera.getDeviceState());
        eFCamera.setParentMac(paramEFCamera.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFCamera);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFCamera);
    } 
  }
  
  private static void updateCompositeMacs(String paramString) {
    String str = PreferenceHelper.readString("key_composite_device", "");
    if (!str.contains(paramString)) {
      StringBuilder stringBuilder;
      if (!"".equals(str)) {
        stringBuilder = new StringBuilder(str);
        stringBuilder.append(",");
      } else {
        stringBuilder = new StringBuilder();
      } 
      stringBuilder.append(paramString);
      PreferenceHelper.write("key_composite_device", stringBuilder.toString());
    } 
  }
  
  public static void updateCurtain(EFDeviceCurtains paramEFDeviceCurtains) {
    if (paramEFDeviceCurtains != null) {
      EFDeviceCurtains eFDeviceCurtains = (EFDeviceCurtains)DaoUtil.queryOne(EFDeviceCurtains.class, WhereBuilder.b("deviceMac", "=", paramEFDeviceCurtains.getDeviceMac()));
      if (eFDeviceCurtains != null) {
        eFDeviceCurtains.setDeviceState(paramEFDeviceCurtains.getDeviceState());
        eFDeviceCurtains.setParentMac(paramEFDeviceCurtains.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFDeviceCurtains);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFDeviceCurtains);
    } 
  }
  
  public static void updateDoorLock(EFDeviceDoorLock paramEFDeviceDoorLock) {
    if (paramEFDeviceDoorLock != null) {
      EFDeviceDoorLock eFDeviceDoorLock = (EFDeviceDoorLock)DaoUtil.queryOne(EFDeviceDoorLock.class, WhereBuilder.b("deviceMac", "=", paramEFDeviceDoorLock.getDeviceMac()));
      if (eFDeviceDoorLock != null) {
        eFDeviceDoorLock.setDeviceState(paramEFDeviceDoorLock.getDeviceState());
        eFDeviceDoorLock.setParentMac(paramEFDeviceDoorLock.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFDeviceDoorLock);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFDeviceDoorLock);
    } 
  }
  
  public static void updateLight(EFDeviceLight paramEFDeviceLight) {
    if (paramEFDeviceLight != null) {
      EFDeviceLight eFDeviceLight = (EFDeviceLight)DaoUtil.queryOne(EFDeviceLight.class, WhereBuilder.b("deviceMac", "=", paramEFDeviceLight.getDeviceMac()));
      if (eFDeviceLight != null) {
        eFDeviceLight.setDeviceState(paramEFDeviceLight.getDeviceState());
        eFDeviceLight.setParentMac(paramEFDeviceLight.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFDeviceLight);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFDeviceLight);
    } 
  }
  
  public static void updateLinkageMode(LinkageMode paramLinkageMode) {
    if (paramLinkageMode != null) {
      LinkageMode linkageMode = (LinkageMode)DaoUtil.queryOne(LinkageMode.class, WhereBuilder.b("modeName", "=", paramLinkageMode.getModeName()));
      if (linkageMode != null) {
        linkageMode.setModeName(paramLinkageMode.getModeName());
        linkageMode.setPicPath(paramLinkageMode.getPicPath());
        linkageMode.setDeviceList(paramLinkageMode.getDeviceList());
        DaoUtil.saveOrUpdate((EntityBase)linkageMode);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramLinkageMode);
    } 
  }
  
  public static void updateLinkageModeByID(LinkageMode paramLinkageMode, int paramInt) {
    if (paramLinkageMode != null) {
      LinkageMode linkageMode = (LinkageMode)DaoUtil.queryOne(LinkageMode.class, WhereBuilder.b("id", "=", Integer.valueOf(paramInt)));
      if (linkageMode != null) {
        linkageMode.setModeName(paramLinkageMode.getModeName());
        linkageMode.setPicPath(paramLinkageMode.getPicPath());
        linkageMode.setDeviceList(paramLinkageMode.getDeviceList());
        DaoUtil.saveOrUpdate((EntityBase)linkageMode);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramLinkageMode);
    } 
  }
  
  public static void updateMusic(EFDeviceMusic paramEFDeviceMusic) {
    if (paramEFDeviceMusic != null) {
      Log.e("music", "updateMusic: " + paramEFDeviceMusic.getDeviceMac());
      EFDeviceMusic eFDeviceMusic = (EFDeviceMusic)DaoUtil.queryOne(EFDeviceMusic.class, WhereBuilder.b("deviceMac", "=", paramEFDeviceMusic.getDeviceMac()));
      if (eFDeviceMusic != null) {
        eFDeviceMusic.setDeviceState(paramEFDeviceMusic.getDeviceState());
        eFDeviceMusic.setParentMac(paramEFDeviceMusic.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFDeviceMusic);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFDeviceMusic);
    } 
  }
  
  public static void updateOutlet(EFDeviceOutlet paramEFDeviceOutlet) {
    if (paramEFDeviceOutlet != null) {
      EFDeviceOutlet eFDeviceOutlet = (EFDeviceOutlet)DaoUtil.queryOne(EFDeviceOutlet.class, WhereBuilder.b("deviceMac", "=", paramEFDeviceOutlet.getDeviceMac()));
      if (eFDeviceOutlet != null) {
        eFDeviceOutlet.setDeviceState(paramEFDeviceOutlet.getDeviceState());
        eFDeviceOutlet.setFirmwareVersion(paramEFDeviceOutlet.getFirmwareVersion());
        eFDeviceOutlet.setParentMac(paramEFDeviceOutlet.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFDeviceOutlet);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFDeviceOutlet);
    } 
  }
  
  public static void updateSwitch(EFDeviceSwitch paramEFDeviceSwitch) {
    if (paramEFDeviceSwitch != null) {
      EFDeviceSwitch eFDeviceSwitch = (EFDeviceSwitch)DaoUtil.queryOne(EFDeviceSwitch.class, WhereBuilder.b("deviceMac", "=", paramEFDeviceSwitch.getDeviceMac()));
      if (eFDeviceSwitch != null) {
        eFDeviceSwitch.setDeviceState(paramEFDeviceSwitch.getDeviceState());
        eFDeviceSwitch.setFirmwareVersion(paramEFDeviceSwitch.getFirmwareVersion());
        eFDeviceSwitch.setParentMac(paramEFDeviceSwitch.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFDeviceSwitch);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFDeviceSwitch);
    } 
  }
  
  public static void updateTele(EFDeviceYaoKong paramEFDeviceYaoKong) {
    if (paramEFDeviceYaoKong != null) {
      EFDeviceYaoKong eFDeviceYaoKong = (EFDeviceYaoKong)DaoUtil.queryOne(EFDeviceYaoKong.class, WhereBuilder.b("deviceMac", "=", paramEFDeviceYaoKong.getDeviceMac()));
      if (eFDeviceYaoKong != null) {
        eFDeviceYaoKong.setDeviceState(paramEFDeviceYaoKong.getDeviceState());
        eFDeviceYaoKong.setParentMac(paramEFDeviceYaoKong.getParentMac());
        DaoUtil.saveOrUpdate((EntityBase)eFDeviceYaoKong);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFDeviceYaoKong);
    } 
  }
  
  private static void updateTeleMacs(String paramString) {
    String str = PreferenceHelper.readString("key_tele_device", "");
    if (!str.contains(paramString)) {
      StringBuilder stringBuilder;
      if (!"".equals(str)) {
        stringBuilder = new StringBuilder(str);
        stringBuilder.append(",");
      } else {
        stringBuilder = new StringBuilder();
      } 
      stringBuilder.append(paramString);
      PreferenceHelper.write("key_tele_device", stringBuilder.toString());
    } 
  }
  
  private static void updateWifiMacs(String paramString, int paramInt) {
    if (paramInt == 4) {
      String str = PreferenceHelper.readString("key_wifi_outlet", "");
      if (str == null || !str.contains(paramString)) {
        StringBuilder stringBuilder;
        if (!"".equals(str)) {
          stringBuilder = new StringBuilder(str);
          stringBuilder.append(",");
        } else {
          stringBuilder = new StringBuilder();
        } 
        stringBuilder.append(paramString);
        PreferenceHelper.write("key_wifi_outlet", stringBuilder.toString());
      } 
      return;
    } 
    if (paramInt == 9) {
      String str = PreferenceHelper.readString("key_wifi_power_strip", "");
      if (str == null || !str.contains(paramString)) {
        StringBuilder stringBuilder;
        if (!"".equals(str)) {
          stringBuilder = new StringBuilder(str);
          stringBuilder.append(",");
        } else {
          stringBuilder = new StringBuilder();
        } 
        stringBuilder.append(paramString);
        PreferenceHelper.write("key_wifi_power_strip", stringBuilder.toString());
      } 
      return;
    } 
    if (paramInt == 5) {
      String str = PreferenceHelper.readString("key_wifi_light", "");
      if (str == null || !str.contains(paramString)) {
        StringBuilder stringBuilder;
        if (!"".equals(str)) {
          stringBuilder = new StringBuilder(str);
          stringBuilder.append(",");
        } else {
          stringBuilder = new StringBuilder();
        } 
        stringBuilder.append(paramString);
        PreferenceHelper.write("key_wifi_light", stringBuilder.toString());
      } 
    } 
  }
  
  public static void updateYaoKong(EFDeviceYaoKong paramEFDeviceYaoKong) {
    if (paramEFDeviceYaoKong != null) {
      Log.e("music", "updateMusic: " + paramEFDeviceYaoKong.getDeviceMac());
      EFDeviceYaoKong eFDeviceYaoKong = (EFDeviceYaoKong)DaoUtil.queryOne(EFDeviceYaoKong.class, WhereBuilder.b("deviceMac", "=", paramEFDeviceYaoKong.getDeviceMac()));
      if (eFDeviceYaoKong != null) {
        eFDeviceYaoKong.setDeviceState(paramEFDeviceYaoKong.getDeviceState());
        eFDeviceYaoKong.setParentMac(paramEFDeviceYaoKong.getParentMac());
        eFDeviceYaoKong.setPosition1(paramEFDeviceYaoKong.getPosition1());
        eFDeviceYaoKong.setYaokongList(paramEFDeviceYaoKong.getYaokongList());
        DaoUtil.saveOrUpdate((EntityBase)eFDeviceYaoKong);
        return;
      } 
      DaoUtil.saveOrUpdate((EntityBase)paramEFDeviceYaoKong);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/ByteUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */