package app.gamer.quadstellar.mode;

public class LightDevices {
  private byte mBrightness;
  
  private byte mColor_B;
  
  private byte mColor_G;
  
  private byte mColor_R;
  
  private byte mDevicesMac;
  
  private byte mFan_High;
  
  private byte mFan_Low;
  
  private byte mFan_Mode;
  
  private byte mFan_Now;
  
  private byte mIsOpen;
  
  private byte mMode;
  
  private byte mPhoneNum;
  
  private byte mSpeed;
  
  private byte mStatus;
  
  private byte mVersion;
  
  public byte[] getBytedate() {
    return new byte[] { this.mIsOpen, this.mMode, this.mColor_R, this.mColor_G, this.mColor_B, this.mBrightness, this.mSpeed };
  }
  
  public byte[] getFanBytedate() {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)this.mFan_Mode;
    arrayOfByte[1] = (byte)this.mFan_Low;
    arrayOfByte[2] = (byte)this.mFan_High;
    return arrayOfByte;
  }
  
  public byte getmBrightness() {
    return this.mBrightness;
  }
  
  public byte getmColor_B() {
    return this.mColor_B;
  }
  
  public byte getmColor_G() {
    return this.mColor_G;
  }
  
  public byte getmColor_R() {
    return this.mColor_R;
  }
  
  public byte getmDevicesMac() {
    return this.mDevicesMac;
  }
  
  public byte getmFan_High() {
    return this.mFan_High;
  }
  
  public byte getmFan_Low() {
    return this.mFan_Low;
  }
  
  public byte getmFan_Mode() {
    return this.mFan_Mode;
  }
  
  public byte getmFan_Now() {
    return this.mFan_Now;
  }
  
  public byte getmIsOpen() {
    return this.mIsOpen;
  }
  
  public byte getmMode() {
    return this.mMode;
  }
  
  public byte getmSpeed() {
    return this.mSpeed;
  }
  
  public byte getmVersion() {
    return this.mVersion;
  }
  
  public void setmBrightness(byte paramByte) {
    this.mBrightness = (byte)paramByte;
  }
  
  public void setmColor_B(byte paramByte) {
    this.mColor_B = (byte)paramByte;
  }
  
  public void setmColor_G(byte paramByte) {
    this.mColor_G = (byte)paramByte;
  }
  
  public void setmColor_R(byte paramByte) {
    this.mColor_R = (byte)paramByte;
  }
  
  public void setmDevicesMac(byte paramByte) {
    this.mDevicesMac = (byte)paramByte;
  }
  
  public void setmFan_High(byte paramByte) {
    this.mFan_High = (byte)paramByte;
  }
  
  public void setmFan_Low(byte paramByte) {
    this.mFan_Low = (byte)paramByte;
  }
  
  public void setmFan_Mode(byte paramByte) {
    this.mFan_Mode = (byte)paramByte;
  }
  
  public void setmFan_Now(byte paramByte) {
    this.mFan_Now = (byte)paramByte;
  }
  
  public void setmIsOpen(byte paramByte) {
    this.mIsOpen = (byte)paramByte;
  }
  
  public void setmMode(byte paramByte) {
    this.mMode = (byte)paramByte;
  }
  
  public void setmSpeed(byte paramByte) {
    this.mSpeed = (byte)paramByte;
  }
  
  public void setmVersion(byte paramByte) {
    this.mVersion = (byte)paramByte;
  }
  
  public String toString() {
    return "LightDevices{mPhoneNum=" + this.mPhoneNum + ", mDevicesMac=" + this.mDevicesMac + ", mVersion=" + this.mVersion + ", mIsOpen=" + this.mIsOpen + ", mMode=" + this.mMode + ", mColor_R=" + this.mColor_R + ", mColor_G=" + this.mColor_G + ", mColor_B=" + this.mColor_B + ", mBrightness=" + this.mBrightness + ", mSpeed=" + this.mSpeed + ", mFan_Mode=" + this.mFan_Mode + ", mFan_Low=" + this.mFan_Low + ", mFan_High=" + this.mFan_High + ", mFan_Now=" + this.mFan_Now + ", mStatus=" + this.mStatus + '}';
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/LightDevices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */