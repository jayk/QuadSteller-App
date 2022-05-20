package app.gamer.quadstellar.domain;

public class JavaBeanTest {
  private boolean isSelect;
  
  private String name;
  
  public JavaBeanTest(String paramString, boolean paramBoolean) {
    this.name = paramString;
    this.isSelect = paramBoolean;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isSelect() {
    return this.isSelect;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setSelect(boolean paramBoolean) {
    this.isSelect = paramBoolean;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/JavaBeanTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */