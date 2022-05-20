package app.gamer.quadstellar.mode;

public class EFAlarm extends EntityBase {
  private static final long serialVersionUID = 76092185134099394L;
  
  private int day;
  
  private int hour;
  
  private int minute;
  
  private int month;
  
  private String name;
  
  public static long getSerialversionuid() {
    return 76092185134099394L;
  }
  
  public int compareTo(Object paramObject) {
    return 0;
  }
  
  public int getDay() {
    return this.day;
  }
  
  public int getHour() {
    return this.hour;
  }
  
  public int getMinute() {
    return this.minute;
  }
  
  public int getMonth() {
    return this.month;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getTimeText() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(String.format("%1$02d-%2$02d", new Object[] { Integer.valueOf(this.month), Integer.valueOf(this.day) }));
    stringBuilder.append(" ");
    stringBuilder.append(String.format("%1$02d:%2$02d", new Object[] { Integer.valueOf(this.hour), Integer.valueOf(this.minute) }));
    stringBuilder.append("(");
    stringBuilder.append(this.name);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void setDay(int paramInt) {
    this.day = paramInt;
  }
  
  public void setHour(int paramInt) {
    this.hour = paramInt;
  }
  
  public void setMinute(int paramInt) {
    this.minute = paramInt;
  }
  
  public void setMonth(int paramInt) {
    this.month = paramInt;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFAlarm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */