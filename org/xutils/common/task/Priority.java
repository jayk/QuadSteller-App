package org.xutils.common.task;

public enum Priority {
  BG_LOW, BG_NORMAL, BG_TOP, DEFAULT, UI_LOW, UI_NORMAL, UI_TOP;
  
  static {
    UI_NORMAL = new Priority("UI_NORMAL", 1);
    UI_LOW = new Priority("UI_LOW", 2);
    DEFAULT = new Priority("DEFAULT", 3);
    BG_TOP = new Priority("BG_TOP", 4);
    BG_NORMAL = new Priority("BG_NORMAL", 5);
    BG_LOW = new Priority("BG_LOW", 6);
    $VALUES = new Priority[] { UI_TOP, UI_NORMAL, UI_LOW, DEFAULT, BG_TOP, BG_NORMAL, BG_LOW };
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/task/Priority.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */