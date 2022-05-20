package app.gamer.quadstellar.domain;

public class ColorInfo {
  private int colorFive = -1;
  
  private int colorFour = -14483712;
  
  private int colorOne = -65536;
  
  private int colorThree = -16776961;
  
  private int colorTwo = -256;
  
  private Long id;
  
  private boolean isWhole;
  
  private String macDrass;
  
  public ColorInfo() {}
  
  public ColorInfo(Long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean) {
    this.id = paramLong;
    this.macDrass = paramString;
    this.colorOne = paramInt1;
    this.colorTwo = paramInt2;
    this.colorThree = paramInt3;
    this.colorFour = paramInt4;
    this.colorFive = paramInt5;
    this.isWhole = paramBoolean;
  }
  
  public int getColorFive() {
    return this.colorFive;
  }
  
  public int getColorFour() {
    return this.colorFour;
  }
  
  public int getColorOne() {
    return this.colorOne;
  }
  
  public int getColorThree() {
    return this.colorThree;
  }
  
  public int getColorTwo() {
    return this.colorTwo;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public boolean getIsWhole() {
    return this.isWhole;
  }
  
  public String getMacDrass() {
    return this.macDrass;
  }
  
  public void setColorFive(int paramInt) {
    this.colorFive = paramInt;
  }
  
  public void setColorFour(int paramInt) {
    this.colorFour = paramInt;
  }
  
  public void setColorOne(int paramInt) {
    this.colorOne = paramInt;
  }
  
  public void setColorThree(int paramInt) {
    this.colorThree = paramInt;
  }
  
  public void setColorTwo(int paramInt) {
    this.colorTwo = paramInt;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void setIsWhole(boolean paramBoolean) {
    this.isWhole = paramBoolean;
  }
  
  public void setMacDrass(String paramString) {
    this.macDrass = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/ColorInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */