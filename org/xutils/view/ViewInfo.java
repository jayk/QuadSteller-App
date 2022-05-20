package org.xutils.view;

final class ViewInfo {
  public int parentId;
  
  public int value;
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this != paramObject) {
      if (paramObject == null || getClass() != paramObject.getClass())
        return false; 
      paramObject = paramObject;
      if (this.value != ((ViewInfo)paramObject).value)
        return false; 
      if (this.parentId != ((ViewInfo)paramObject).parentId)
        bool = false; 
    } 
    return bool;
  }
  
  public int hashCode() {
    return this.value * 31 + this.parentId;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/view/ViewInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */