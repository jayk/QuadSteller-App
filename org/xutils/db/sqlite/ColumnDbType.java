package org.xutils.db.sqlite;

public enum ColumnDbType {
  BLOB,
  INTEGER("INTEGER"),
  REAL("REAL"),
  TEXT("TEXT");
  
  private String value;
  
  static {
    BLOB = new ColumnDbType("BLOB", 3, "BLOB");
    $VALUES = new ColumnDbType[] { INTEGER, REAL, TEXT, BLOB };
  }
  
  ColumnDbType(String paramString1) {
    this.value = paramString1;
  }
  
  public String toString() {
    return this.value;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/sqlite/ColumnDbType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */