package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.math.BigDecimal;

public final class LazilyParsedNumber extends Number {
  private final String value;
  
  public LazilyParsedNumber(String paramString) {
    this.value = paramString;
  }
  
  private Object writeReplace() throws ObjectStreamException {
    return new BigDecimal(this.value);
  }
  
  public double doubleValue() {
    return Double.parseDouble(this.value);
  }
  
  public float floatValue() {
    return Float.parseFloat(this.value);
  }
  
  public int intValue() {
    int i;
    try {
      i = Integer.parseInt(this.value);
    } catch (NumberFormatException numberFormatException) {}
    return i;
  }
  
  public long longValue() {
    long l;
    try {
      l = Long.parseLong(this.value);
    } catch (NumberFormatException numberFormatException) {
      l = (new BigDecimal(this.value)).longValue();
    } 
    return l;
  }
  
  public String toString() {
    return this.value;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/LazilyParsedNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */