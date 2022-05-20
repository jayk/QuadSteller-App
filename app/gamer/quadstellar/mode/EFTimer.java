package app.gamer.quadstellar.mode;

import android.content.res.Resources;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.utils.ByteUtils;
import app.gamer.quadstellar.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class EFTimer extends EntityBase {
  public static final int CUSTOM = 14;
  
  public static final int EVERY_DAY = 11;
  
  public static final int ONCE = 10;
  
  public static final int TIMER_CLOSE = 2;
  
  public static final int TIMER_DEFAULT = -1;
  
  public static final int TIMER_OPEN = 1;
  
  public static final int WEEKEND = 13;
  
  public static final int WORKING_DAY = 12;
  
  private static final long serialVersionUID = 1811505252756260454L;
  
  protected int closeHour;
  
  protected int closeMinute;
  
  protected List<Integer> customList;
  
  protected boolean isCloseEdited = false;
  
  protected boolean isOpenEdited = false;
  
  protected int number;
  
  protected int openHour;
  
  protected int openMinute;
  
  protected int repeatType = 10;
  
  protected boolean state = true;
  
  protected int type = -1;
  
  private byte convertToByte() {
    int i = 0;
    for (byte b = 0; b < this.customList.size(); b++)
      i |= 1 << ((Integer)this.customList.get(b)).intValue(); 
    return (byte)(i & 0xFF);
  }
  
  public static long getSerialversionuid() {
    return 1811505252756260454L;
  }
  
  public int compareTo(Object paramObject) {
    return 0;
  }
  
  public List<Integer> convertToList(byte paramByte) {
    ArrayList<Integer> arrayList = new ArrayList();
    for (byte b = 0; b < 7; b++) {
      if ((1 << b & paramByte) == 1 << b)
        arrayList.add(Integer.valueOf(b)); 
    } 
    return arrayList;
  }
  
  public String getAllText(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.type == 1) {
      stringBuilder.append(String.format("%1$s%2$02d:%3$02d", new Object[] { App.getAppResources().getString(2131297500) + ":", Integer.valueOf(this.openHour), Integer.valueOf(this.openMinute) }));
      LogUtil.e("11111", "allText.toString() ===" + stringBuilder.toString());
      return stringBuilder.toString();
    } 
    if (this.type == 2)
      stringBuilder.append(String.format("%1$s%2$02d:%3$02d", new Object[] { App.getAppResources().getString(2131297499) + ":", Integer.valueOf(this.closeHour), Integer.valueOf(this.closeMinute) })); 
    LogUtil.e("11111", "allText.toString() ===" + stringBuilder.toString());
    return stringBuilder.toString();
  }
  
  public int getCloseHour() {
    return this.closeHour;
  }
  
  public int getCloseMinute() {
    return this.closeMinute;
  }
  
  public String getCloseTimeText() {
    return String.format("%1$02d:%2$02d", new Object[] { Integer.valueOf(this.closeHour), Integer.valueOf(this.closeMinute) });
  }
  
  public List<Integer> getCustomList() {
    return this.customList;
  }
  
  public String getCustomText() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.customList == null)
      return ""; 
    String[] arrayOfString = App.getAppResources().getStringArray(2131689504);
    for (byte b = 0; b < this.customList.size(); b++) {
      stringBuilder.append(arrayOfString[((Integer)this.customList.get(b)).intValue()]);
      if (b != this.customList.size() - 1)
        stringBuilder.append(","); 
    } 
    return stringBuilder.toString();
  }
  
  public int getNumber() {
    return this.number;
  }
  
  public int getOpenHour() {
    return this.openHour;
  }
  
  public int getOpenMinute() {
    return this.openMinute;
  }
  
  public String getOpenTimeText() {
    return String.format("%1$02d:%2$02d", new Object[] { Integer.valueOf(this.openHour), Integer.valueOf(this.openMinute) });
  }
  
  protected byte[] getRepeatData() {
    byte[] arrayOfByte = new byte[1];
    switch (this.repeatType) {
      default:
        return arrayOfByte;
      case 10:
        arrayOfByte[0] = (byte)Byte.MIN_VALUE;
      case 11:
        arrayOfByte[0] = (byte)Byte.MAX_VALUE;
      case 12:
        arrayOfByte[0] = (byte)31;
      case 13:
        arrayOfByte[0] = (byte)96;
      case 14:
        break;
    } 
    arrayOfByte[0] = convertToByte();
  }
  
  public String getRepeatText() {
    String str = "";
    Resources resources = App.getAppResources();
    switch (this.repeatType) {
      default:
        return str;
      case 10:
        str = resources.getString(2131296930);
      case 11:
        str = resources.getString(2131296671);
      case 12:
        str = resources.getString(2131297589);
      case 13:
        str = resources.getString(2131297575);
      case 14:
        break;
    } 
    str = getCustomText();
  }
  
  public int getRepeatType() {
    return this.repeatType;
  }
  
  public boolean getState() {
    return this.state;
  }
  
  public byte[] getTimerData() {
    byte[] arrayOfByte1 = new byte[4];
    arrayOfByte1[2] = (byte)110;
    arrayOfByte1[3] = (byte)111;
    if (this.type == 1) {
      arrayOfByte1[0] = (byte)(byte)(this.openHour & 0xFF);
      arrayOfByte1[1] = (byte)(byte)(this.openMinute & 0xFF);
      arrayOfByte1[2] = (byte)110;
      arrayOfByte1[3] = (byte)111;
    } else if (this.type == 2) {
      arrayOfByte1[0] = (byte)110;
      arrayOfByte1[1] = (byte)111;
      arrayOfByte1[2] = (byte)(byte)(this.closeHour & 0xFF);
      arrayOfByte1[3] = (byte)(byte)(this.closeMinute & 0xFF);
    } 
    byte[] arrayOfByte2 = getRepeatData();
    if (this.state) {
      byte[] arrayOfByte = ByteUtils.int2OneByte(1);
      return ByteUtils.append(7, new byte[][] { ByteUtils.int2OneByte(this.number), arrayOfByte1, arrayOfByte2, arrayOfByte });
    } 
    byte[] arrayOfByte3 = ByteUtils.int2OneByte(0);
    return ByteUtils.append(7, new byte[][] { ByteUtils.int2OneByte(this.number), arrayOfByte1, arrayOfByte2, arrayOfByte3 });
  }
  
  public int getType() {
    return this.type;
  }
  
  public byte[] getZigTimerData() {
    byte[] arrayOfByte = new byte[4];
    if (this.type == 1) {
      arrayOfByte[0] = (byte)(byte)(this.openHour & 0xFF);
      arrayOfByte[1] = (byte)(byte)(this.openMinute & 0xFF);
      arrayOfByte[2] = (byte)110;
      arrayOfByte[3] = (byte)111;
      return ByteUtils.append(5, new byte[][] { arrayOfByte, getRepeatData() });
    } 
    if (this.type == 2) {
      arrayOfByte[0] = (byte)110;
      arrayOfByte[1] = (byte)111;
      arrayOfByte[2] = (byte)(byte)(this.closeHour & 0xFF);
      arrayOfByte[3] = (byte)(byte)(this.closeMinute & 0xFF);
    } 
    return ByteUtils.append(5, new byte[][] { arrayOfByte, getRepeatData() });
  }
  
  public boolean isCloseEdited() {
    return this.isCloseEdited;
  }
  
  public boolean isOpenEdited() {
    return this.isOpenEdited;
  }
  
  public boolean notEdit() {
    return (this.type == -1);
  }
  
  public void setCloseEdited(boolean paramBoolean) {
    this.isCloseEdited = paramBoolean;
  }
  
  public void setCloseHour(int paramInt) {
    this.closeHour = paramInt;
  }
  
  public void setCloseMinute(int paramInt) {
    this.closeMinute = paramInt;
  }
  
  public void setCustomList(List<Integer> paramList) {
    this.customList = paramList;
  }
  
  public void setNumber(int paramInt) {
    this.number = paramInt;
  }
  
  public void setOpenEdited(boolean paramBoolean) {
    this.isOpenEdited = paramBoolean;
  }
  
  public void setOpenHour(int paramInt) {
    this.openHour = paramInt;
  }
  
  public void setOpenMinute(int paramInt) {
    this.openMinute = paramInt;
  }
  
  public void setRepeatType(int paramInt) {
    this.repeatType = paramInt;
  }
  
  public void setState(boolean paramBoolean) {
    this.state = paramBoolean;
  }
  
  public void setType(int paramInt) {
    this.type = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */