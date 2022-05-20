package com.google.gson;

import com.google.gson.internal.;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive extends JsonElement {
  private static final Class<?>[] PRIMITIVE_TYPES = new Class[] { 
      int.class, long.class, short.class, float.class, double.class, byte.class, boolean.class, char.class, Integer.class, Long.class, 
      Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };
  
  private Object value;
  
  public JsonPrimitive(Boolean paramBoolean) {
    setValue(paramBoolean);
  }
  
  public JsonPrimitive(Character paramCharacter) {
    setValue(paramCharacter);
  }
  
  public JsonPrimitive(Number paramNumber) {
    setValue(paramNumber);
  }
  
  JsonPrimitive(Object paramObject) {
    setValue(paramObject);
  }
  
  public JsonPrimitive(String paramString) {
    setValue(paramString);
  }
  
  private static boolean isIntegral(JsonPrimitive paramJsonPrimitive) {
    boolean bool = false;
    null = bool;
    if (paramJsonPrimitive.value instanceof Number) {
      Number number = (Number)paramJsonPrimitive.value;
      if (!(number instanceof BigInteger) && !(number instanceof Long) && !(number instanceof Integer) && !(number instanceof Short)) {
        null = bool;
        return (number instanceof Byte) ? true : null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  private static boolean isPrimitiveOrString(Object paramObject) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: aload_0
    //   3: instanceof java/lang/String
    //   6: ifeq -> 13
    //   9: iload_1
    //   10: istore_2
    //   11: iload_2
    //   12: ireturn
    //   13: aload_0
    //   14: invokevirtual getClass : ()Ljava/lang/Class;
    //   17: astore_3
    //   18: getstatic com/google/gson/JsonPrimitive.PRIMITIVE_TYPES : [Ljava/lang/Class;
    //   21: astore_0
    //   22: aload_0
    //   23: arraylength
    //   24: istore #4
    //   26: iconst_0
    //   27: istore #5
    //   29: iload #5
    //   31: iload #4
    //   33: if_icmpge -> 55
    //   36: iload_1
    //   37: istore_2
    //   38: aload_0
    //   39: iload #5
    //   41: aaload
    //   42: aload_3
    //   43: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   46: ifne -> 11
    //   49: iinc #5, 1
    //   52: goto -> 29
    //   55: iconst_0
    //   56: istore_2
    //   57: goto -> 11
  }
  
  JsonPrimitive deepCopy() {
    return this;
  }
  
  public boolean equals(Object paramObject) {
    null = true;
    boolean bool = false;
    if (this == paramObject)
      return null; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    if (this.value == null) {
      if (((JsonPrimitive)paramObject).value != null)
        null = false; 
      return null;
    } 
    if (isIntegral(this) && isIntegral((JsonPrimitive)paramObject)) {
      if (getAsNumber().longValue() != paramObject.getAsNumber().longValue())
        null = false; 
      return null;
    } 
    if (this.value instanceof Number && ((JsonPrimitive)paramObject).value instanceof Number) {
      double d1 = getAsNumber().doubleValue();
      double d2 = paramObject.getAsNumber().doubleValue();
      if (d1 != d2) {
        null = bool;
        if (Double.isNaN(d1)) {
          null = bool;
          if (Double.isNaN(d2))
            return true; 
        } 
        return null;
      } 
    } else {
      return this.value.equals(((JsonPrimitive)paramObject).value);
    } 
    return true;
  }
  
  public BigDecimal getAsBigDecimal() {
    return (this.value instanceof BigDecimal) ? (BigDecimal)this.value : new BigDecimal(this.value.toString());
  }
  
  public BigInteger getAsBigInteger() {
    return (this.value instanceof BigInteger) ? (BigInteger)this.value : new BigInteger(this.value.toString());
  }
  
  public boolean getAsBoolean() {
    return isBoolean() ? getAsBooleanWrapper().booleanValue() : Boolean.parseBoolean(getAsString());
  }
  
  Boolean getAsBooleanWrapper() {
    return (Boolean)this.value;
  }
  
  public byte getAsByte() {
    if (isNumber())
      return getAsNumber().byteValue(); 
    return Byte.parseByte(getAsString());
  }
  
  public char getAsCharacter() {
    return getAsString().charAt(0);
  }
  
  public double getAsDouble() {
    return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
  }
  
  public float getAsFloat() {
    return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
  }
  
  public int getAsInt() {
    return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
  }
  
  public long getAsLong() {
    return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
  }
  
  public Number getAsNumber() {
    return (Number)((this.value instanceof String) ? new LazilyParsedNumber((String)this.value) : this.value);
  }
  
  public short getAsShort() {
    if (isNumber())
      return getAsNumber().shortValue(); 
    return Short.parseShort(getAsString());
  }
  
  public String getAsString() {
    return isNumber() ? getAsNumber().toString() : (isBoolean() ? getAsBooleanWrapper().toString() : (String)this.value);
  }
  
  public int hashCode() {
    if (this.value == null)
      return 31; 
    if (isIntegral(this)) {
      long l = getAsNumber().longValue();
      return (int)(l >>> 32L ^ l);
    } 
    if (this.value instanceof Number) {
      long l = Double.doubleToLongBits(getAsNumber().doubleValue());
      return (int)(l >>> 32L ^ l);
    } 
    return this.value.hashCode();
  }
  
  public boolean isBoolean() {
    return this.value instanceof Boolean;
  }
  
  public boolean isNumber() {
    return this.value instanceof Number;
  }
  
  public boolean isString() {
    return this.value instanceof String;
  }
  
  void setValue(Object paramObject) {
    boolean bool;
    if (paramObject instanceof Character) {
      this.value = String.valueOf(((Character)paramObject).charValue());
      return;
    } 
    if (paramObject instanceof Number || isPrimitiveOrString(paramObject)) {
      bool = true;
    } else {
      bool = false;
    } 
    .Gson.Preconditions.checkArgument(bool);
    this.value = paramObject;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/JsonPrimitive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */