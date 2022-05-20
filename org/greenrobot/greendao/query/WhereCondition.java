package org.greenrobot.greendao.query;

import java.util.List;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;

public interface WhereCondition {
  void appendTo(StringBuilder paramStringBuilder, String paramString);
  
  void appendValuesTo(List<Object> paramList);
  
  public static abstract class AbstractCondition implements WhereCondition {
    protected final boolean hasSingleValue;
    
    protected final Object value;
    
    protected final Object[] values;
    
    public AbstractCondition() {
      this.hasSingleValue = false;
      this.value = null;
      this.values = null;
    }
    
    public AbstractCondition(Object param1Object) {
      this.value = param1Object;
      this.hasSingleValue = true;
      this.values = null;
    }
    
    public AbstractCondition(Object[] param1ArrayOfObject) {
      this.value = null;
      this.hasSingleValue = false;
      this.values = param1ArrayOfObject;
    }
    
    public void appendValuesTo(List<Object> param1List) {
      if (this.hasSingleValue) {
        param1List.add(this.value);
        return;
      } 
      if (this.values != null) {
        Object[] arrayOfObject = this.values;
        int i = arrayOfObject.length;
        byte b = 0;
        while (true) {
          if (b < i) {
            param1List.add(arrayOfObject[b]);
            b++;
            continue;
          } 
          return;
        } 
      } 
    }
  }
  
  public static class PropertyCondition extends AbstractCondition {
    public final String op;
    
    public final Property property;
    
    public PropertyCondition(Property param1Property, String param1String) {
      this.property = param1Property;
      this.op = param1String;
    }
    
    public PropertyCondition(Property param1Property, String param1String, Object param1Object) {
      super(checkValueForType(param1Property, param1Object));
      this.property = param1Property;
      this.op = param1String;
    }
    
    public PropertyCondition(Property param1Property, String param1String, Object[] param1ArrayOfObject) {
      super(checkValuesForType(param1Property, param1ArrayOfObject));
      this.property = param1Property;
      this.op = param1String;
    }
    
    private static Object checkValueForType(Property param1Property, Object param1Object) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: aload_1
      //   3: ifnull -> 26
      //   6: aload_1
      //   7: invokevirtual getClass : ()Ljava/lang/Class;
      //   10: invokevirtual isArray : ()Z
      //   13: ifeq -> 26
      //   16: new org/greenrobot/greendao/DaoException
      //   19: dup
      //   20: ldc 'Illegal value: found array, but simple object required'
      //   22: invokespecial <init> : (Ljava/lang/String;)V
      //   25: athrow
      //   26: aload_0
      //   27: getfield type : Ljava/lang/Class;
      //   30: ldc java/util/Date
      //   32: if_acmpne -> 91
      //   35: aload_1
      //   36: instanceof java/util/Date
      //   39: ifeq -> 55
      //   42: aload_1
      //   43: checkcast java/util/Date
      //   46: invokevirtual getTime : ()J
      //   49: invokestatic valueOf : (J)Ljava/lang/Long;
      //   52: astore_3
      //   53: aload_3
      //   54: areturn
      //   55: aload_1
      //   56: astore_3
      //   57: aload_1
      //   58: instanceof java/lang/Long
      //   61: ifne -> 53
      //   64: new org/greenrobot/greendao/DaoException
      //   67: dup
      //   68: new java/lang/StringBuilder
      //   71: dup
      //   72: invokespecial <init> : ()V
      //   75: ldc 'Illegal date value: expected java.util.Date or Long for value '
      //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   80: aload_1
      //   81: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   84: invokevirtual toString : ()Ljava/lang/String;
      //   87: invokespecial <init> : (Ljava/lang/String;)V
      //   90: athrow
      //   91: aload_0
      //   92: getfield type : Ljava/lang/Class;
      //   95: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
      //   98: if_acmpeq -> 112
      //   101: aload_1
      //   102: astore_3
      //   103: aload_0
      //   104: getfield type : Ljava/lang/Class;
      //   107: ldc java/lang/Boolean
      //   109: if_acmpne -> 53
      //   112: aload_1
      //   113: instanceof java/lang/Boolean
      //   116: ifeq -> 139
      //   119: aload_1
      //   120: checkcast java/lang/Boolean
      //   123: invokevirtual booleanValue : ()Z
      //   126: ifeq -> 131
      //   129: iconst_1
      //   130: istore_2
      //   131: iload_2
      //   132: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   135: astore_3
      //   136: goto -> 53
      //   139: aload_1
      //   140: instanceof java/lang/Number
      //   143: ifeq -> 194
      //   146: aload_1
      //   147: checkcast java/lang/Number
      //   150: invokevirtual intValue : ()I
      //   153: istore_2
      //   154: aload_1
      //   155: astore_3
      //   156: iload_2
      //   157: ifeq -> 53
      //   160: aload_1
      //   161: astore_3
      //   162: iload_2
      //   163: iconst_1
      //   164: if_icmpeq -> 53
      //   167: new org/greenrobot/greendao/DaoException
      //   170: dup
      //   171: new java/lang/StringBuilder
      //   174: dup
      //   175: invokespecial <init> : ()V
      //   178: ldc 'Illegal boolean value: numbers must be 0 or 1, but was '
      //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   183: aload_1
      //   184: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   187: invokevirtual toString : ()Ljava/lang/String;
      //   190: invokespecial <init> : (Ljava/lang/String;)V
      //   193: athrow
      //   194: aload_1
      //   195: astore_3
      //   196: aload_1
      //   197: instanceof java/lang/String
      //   200: ifeq -> 53
      //   203: aload_1
      //   204: checkcast java/lang/String
      //   207: astore_0
      //   208: ldc 'TRUE'
      //   210: aload_0
      //   211: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   214: ifeq -> 225
      //   217: iconst_1
      //   218: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   221: astore_3
      //   222: goto -> 53
      //   225: ldc 'FALSE'
      //   227: aload_0
      //   228: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   231: ifeq -> 242
      //   234: iconst_0
      //   235: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   238: astore_3
      //   239: goto -> 53
      //   242: new org/greenrobot/greendao/DaoException
      //   245: dup
      //   246: new java/lang/StringBuilder
      //   249: dup
      //   250: invokespecial <init> : ()V
      //   253: ldc 'Illegal boolean value: Strings must be "TRUE" or "FALSE" (case insensitive), but was '
      //   255: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   258: aload_1
      //   259: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   262: invokevirtual toString : ()Ljava/lang/String;
      //   265: invokespecial <init> : (Ljava/lang/String;)V
      //   268: athrow
    }
    
    private static Object[] checkValuesForType(Property param1Property, Object[] param1ArrayOfObject) {
      for (byte b = 0; b < param1ArrayOfObject.length; b++)
        param1ArrayOfObject[b] = checkValueForType(param1Property, param1ArrayOfObject[b]); 
      return param1ArrayOfObject;
    }
    
    public void appendTo(StringBuilder param1StringBuilder, String param1String) {
      SqlUtils.appendProperty(param1StringBuilder, param1String, this.property).append(this.op);
    }
  }
  
  public static class StringCondition extends AbstractCondition {
    protected final String string;
    
    public StringCondition(String param1String) {
      this.string = param1String;
    }
    
    public StringCondition(String param1String, Object param1Object) {
      super(param1Object);
      this.string = param1String;
    }
    
    public StringCondition(String param1String, Object... param1VarArgs) {
      super(param1VarArgs);
      this.string = param1String;
    }
    
    public void appendTo(StringBuilder param1StringBuilder, String param1String) {
      param1StringBuilder.append(this.string);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/WhereCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */