package android.support.graphics.drawable;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

class PathParser {
  private static final String LOGTAG = "PathParser";
  
  private static void addNode(ArrayList<PathDataNode> paramArrayList, char paramChar, float[] paramArrayOffloat) {
    paramArrayList.add(new PathDataNode(paramChar, paramArrayOffloat));
  }
  
  public static boolean canMorph(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iload_2
    //   3: istore_3
    //   4: aload_0
    //   5: ifnull -> 14
    //   8: aload_1
    //   9: ifnonnull -> 16
    //   12: iload_2
    //   13: istore_3
    //   14: iload_3
    //   15: ireturn
    //   16: iload_2
    //   17: istore_3
    //   18: aload_0
    //   19: arraylength
    //   20: aload_1
    //   21: arraylength
    //   22: if_icmpne -> 14
    //   25: iconst_0
    //   26: istore #4
    //   28: iload #4
    //   30: aload_0
    //   31: arraylength
    //   32: if_icmpge -> 81
    //   35: iload_2
    //   36: istore_3
    //   37: aload_0
    //   38: iload #4
    //   40: aaload
    //   41: getfield type : C
    //   44: aload_1
    //   45: iload #4
    //   47: aaload
    //   48: getfield type : C
    //   51: if_icmpne -> 14
    //   54: iload_2
    //   55: istore_3
    //   56: aload_0
    //   57: iload #4
    //   59: aaload
    //   60: getfield params : [F
    //   63: arraylength
    //   64: aload_1
    //   65: iload #4
    //   67: aaload
    //   68: getfield params : [F
    //   71: arraylength
    //   72: if_icmpne -> 14
    //   75: iinc #4, 1
    //   78: goto -> 28
    //   81: iconst_1
    //   82: istore_3
    //   83: goto -> 14
  }
  
  static float[] copyOfRange(float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    if (paramInt1 > paramInt2)
      throw new IllegalArgumentException(); 
    int i = paramArrayOffloat.length;
    if (paramInt1 < 0 || paramInt1 > i)
      throw new ArrayIndexOutOfBoundsException(); 
    paramInt2 -= paramInt1;
    i = Math.min(paramInt2, i - paramInt1);
    float[] arrayOfFloat = new float[paramInt2];
    System.arraycopy(paramArrayOffloat, paramInt1, arrayOfFloat, 0, i);
    return arrayOfFloat;
  }
  
  public static PathDataNode[] createNodesFromPathData(String paramString) {
    if (paramString == null)
      return null; 
    int i = 0;
    int j = 1;
    ArrayList<PathDataNode> arrayList = new ArrayList();
    while (j < paramString.length()) {
      j = nextStart(paramString, j);
      String str = paramString.substring(i, j).trim();
      if (str.length() > 0) {
        float[] arrayOfFloat = getFloats(str);
        addNode(arrayList, str.charAt(0), arrayOfFloat);
      } 
      i = j;
      j++;
    } 
    if (j - i == 1 && i < paramString.length())
      addNode(arrayList, paramString.charAt(i), new float[0]); 
    return arrayList.<PathDataNode>toArray(new PathDataNode[arrayList.size()]);
  }
  
  public static Path createPathFromPathData(String paramString) {
    Path path = new Path();
    PathDataNode[] arrayOfPathDataNode = createNodesFromPathData(paramString);
    if (arrayOfPathDataNode != null)
      try {
        PathDataNode.nodesToPath(arrayOfPathDataNode, path);
        return path;
      } catch (RuntimeException runtimeException) {
        throw new RuntimeException("Error in parsing " + paramString, runtimeException);
      }  
    return null;
  }
  
  public static PathDataNode[] deepCopyNodes(PathDataNode[] paramArrayOfPathDataNode) {
    if (paramArrayOfPathDataNode == null)
      return null; 
    PathDataNode[] arrayOfPathDataNode = new PathDataNode[paramArrayOfPathDataNode.length];
    byte b = 0;
    while (true) {
      PathDataNode[] arrayOfPathDataNode1 = arrayOfPathDataNode;
      if (b < paramArrayOfPathDataNode.length) {
        arrayOfPathDataNode[b] = new PathDataNode(paramArrayOfPathDataNode[b]);
        b++;
        continue;
      } 
      return arrayOfPathDataNode1;
    } 
  }
  
  private static void extract(String paramString, int paramInt, ExtractFloatResult paramExtractFloatResult) {
    // Byte code:
    //   0: iload_1
    //   1: istore_3
    //   2: iconst_0
    //   3: istore #4
    //   5: aload_2
    //   6: iconst_0
    //   7: putfield mEndWithNegOrDot : Z
    //   10: iconst_0
    //   11: istore #5
    //   13: iconst_0
    //   14: istore #6
    //   16: iload_3
    //   17: aload_0
    //   18: invokevirtual length : ()I
    //   21: if_icmpge -> 109
    //   24: iconst_0
    //   25: istore #7
    //   27: aload_0
    //   28: iload_3
    //   29: invokevirtual charAt : (I)C
    //   32: lookupswitch default -> 92, 32 -> 115, 44 -> 115, 45 -> 129, 46 -> 182, 69 -> 220, 101 -> 220
    //   92: iload #5
    //   94: istore #8
    //   96: iload #7
    //   98: istore #9
    //   100: iload #4
    //   102: istore #10
    //   104: iload #10
    //   106: ifeq -> 234
    //   109: aload_2
    //   110: iload_3
    //   111: putfield mEndPosition : I
    //   114: return
    //   115: iconst_1
    //   116: istore #10
    //   118: iload #7
    //   120: istore #9
    //   122: iload #5
    //   124: istore #8
    //   126: goto -> 104
    //   129: iload #4
    //   131: istore #10
    //   133: iload #7
    //   135: istore #9
    //   137: iload #5
    //   139: istore #8
    //   141: iload_3
    //   142: iload_1
    //   143: if_icmpeq -> 104
    //   146: iload #4
    //   148: istore #10
    //   150: iload #7
    //   152: istore #9
    //   154: iload #5
    //   156: istore #8
    //   158: iload #6
    //   160: ifne -> 104
    //   163: iconst_1
    //   164: istore #10
    //   166: aload_2
    //   167: iconst_1
    //   168: putfield mEndWithNegOrDot : Z
    //   171: iload #7
    //   173: istore #9
    //   175: iload #5
    //   177: istore #8
    //   179: goto -> 104
    //   182: iload #5
    //   184: ifne -> 201
    //   187: iconst_1
    //   188: istore #8
    //   190: iload #4
    //   192: istore #10
    //   194: iload #7
    //   196: istore #9
    //   198: goto -> 104
    //   201: iconst_1
    //   202: istore #10
    //   204: aload_2
    //   205: iconst_1
    //   206: putfield mEndWithNegOrDot : Z
    //   209: iload #7
    //   211: istore #9
    //   213: iload #5
    //   215: istore #8
    //   217: goto -> 104
    //   220: iconst_1
    //   221: istore #9
    //   223: iload #4
    //   225: istore #10
    //   227: iload #5
    //   229: istore #8
    //   231: goto -> 104
    //   234: iinc #3, 1
    //   237: iload #10
    //   239: istore #4
    //   241: iload #9
    //   243: istore #6
    //   245: iload #8
    //   247: istore #5
    //   249: goto -> 16
  }
  
  private static float[] getFloats(String paramString) {
    float[] arrayOfFloat;
    int j;
    int i = 1;
    if (paramString.charAt(0) == 'z') {
      j = 1;
    } else {
      j = 0;
    } 
    if (paramString.charAt(0) != 'Z')
      i = 0; 
    if ((j | i) != 0)
      return new float[0]; 
    try {
      float[] arrayOfFloat1 = new float[paramString.length()];
      i = 1;
      ExtractFloatResult extractFloatResult = new ExtractFloatResult();
      this();
      int k = paramString.length();
      j = 0;
      while (i < k) {
        extract(paramString, i, extractFloatResult);
        int m = extractFloatResult.mEndPosition;
        if (i < m) {
          int n = j + 1;
          arrayOfFloat1[j] = Float.parseFloat(paramString.substring(i, m));
          j = n;
        } 
        if (extractFloatResult.mEndWithNegOrDot) {
          i = m;
          continue;
        } 
        i = m + 1;
      } 
      arrayOfFloat1 = copyOfRange(arrayOfFloat1, 0, j);
      arrayOfFloat = arrayOfFloat1;
    } catch (NumberFormatException numberFormatException) {
      throw new RuntimeException("error in parsing \"" + arrayOfFloat + "\"", numberFormatException);
    } 
    return arrayOfFloat;
  }
  
  private static int nextStart(String paramString, int paramInt) {
    while (true) {
      if (paramInt < paramString.length()) {
        char c = paramString.charAt(paramInt);
        if (((c - 65) * (c - 90) > 0 && (c - 97) * (c - 122) > 0) || c == 'e' || c == 'E') {
          paramInt++;
          continue;
        } 
      } 
      return paramInt;
    } 
  }
  
  public static void updateNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    for (byte b = 0; b < paramArrayOfPathDataNode2.length; b++) {
      (paramArrayOfPathDataNode1[b]).type = (char)(paramArrayOfPathDataNode2[b]).type;
      for (byte b1 = 0; b1 < (paramArrayOfPathDataNode2[b]).params.length; b1++)
        (paramArrayOfPathDataNode1[b]).params[b1] = (paramArrayOfPathDataNode2[b]).params[b1]; 
    } 
  }
  
  private static class ExtractFloatResult {
    int mEndPosition;
    
    boolean mEndWithNegOrDot;
  }
  
  public static class PathDataNode {
    float[] params;
    
    char type;
    
    PathDataNode(char param1Char, float[] param1ArrayOffloat) {
      this.type = (char)param1Char;
      this.params = param1ArrayOffloat;
    }
    
    PathDataNode(PathDataNode param1PathDataNode) {
      this.type = (char)param1PathDataNode.type;
      this.params = PathParser.copyOfRange(param1PathDataNode.params, 0, param1PathDataNode.params.length);
    }
    
    private static void addCommand(Path param1Path, float[] param1ArrayOffloat1, char param1Char1, char param1Char2, float[] param1ArrayOffloat2) {
      int i;
      float f7;
      float f8;
      float f9;
      byte b = 2;
      float f1 = param1ArrayOffloat1[0];
      float f2 = param1ArrayOffloat1[1];
      float f3 = param1ArrayOffloat1[2];
      float f4 = param1ArrayOffloat1[3];
      float f5 = param1ArrayOffloat1[4];
      float f6 = param1ArrayOffloat1[5];
      switch (param1Char2) {
        default:
          i = 0;
          f7 = f6;
          f6 = f5;
          f8 = f4;
          f9 = f3;
          while (true) {
            if (i < param1ArrayOffloat2.length) {
              boolean bool1;
              boolean bool2;
              switch (param1Char2) {
                default:
                  f4 = f7;
                  f5 = f8;
                  f3 = f9;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'm':
                  f1 += param1ArrayOffloat2[i + 0];
                  f2 += param1ArrayOffloat2[i + 1];
                  if (i > 0) {
                    param1Path.rLineTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
                    f3 = f9;
                    f5 = f8;
                    f4 = f7;
                  } else {
                    param1Path.rMoveTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
                    f6 = f1;
                    f4 = f2;
                    f3 = f9;
                    f5 = f8;
                  } 
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'M':
                  f1 = param1ArrayOffloat2[i + 0];
                  f2 = param1ArrayOffloat2[i + 1];
                  if (i > 0) {
                    param1Path.lineTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
                    f3 = f9;
                    f5 = f8;
                    f4 = f7;
                  } else {
                    param1Path.moveTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
                    f6 = f1;
                    f4 = f2;
                    f3 = f9;
                    f5 = f8;
                  } 
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'l':
                  param1Path.rLineTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
                  f1 += param1ArrayOffloat2[i + 0];
                  f2 += param1ArrayOffloat2[i + 1];
                  f3 = f9;
                  f5 = f8;
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'L':
                  param1Path.lineTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
                  f1 = param1ArrayOffloat2[i + 0];
                  f2 = param1ArrayOffloat2[i + 1];
                  f3 = f9;
                  f5 = f8;
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'h':
                  param1Path.rLineTo(param1ArrayOffloat2[i + 0], 0.0F);
                  f1 += param1ArrayOffloat2[i + 0];
                  f3 = f9;
                  f5 = f8;
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'H':
                  param1Path.lineTo(param1ArrayOffloat2[i + 0], f2);
                  f1 = param1ArrayOffloat2[i + 0];
                  f3 = f9;
                  f5 = f8;
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'v':
                  param1Path.rLineTo(0.0F, param1ArrayOffloat2[i + 0]);
                  f2 += param1ArrayOffloat2[i + 0];
                  f3 = f9;
                  f5 = f8;
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'V':
                  param1Path.lineTo(f1, param1ArrayOffloat2[i + 0]);
                  f2 = param1ArrayOffloat2[i + 0];
                  f3 = f9;
                  f5 = f8;
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'c':
                  param1Path.rCubicTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3], param1ArrayOffloat2[i + 4], param1ArrayOffloat2[i + 5]);
                  f3 = f1 + param1ArrayOffloat2[i + 2];
                  f5 = f2 + param1ArrayOffloat2[i + 3];
                  f1 += param1ArrayOffloat2[i + 4];
                  f2 += param1ArrayOffloat2[i + 5];
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'C':
                  param1Path.cubicTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3], param1ArrayOffloat2[i + 4], param1ArrayOffloat2[i + 5]);
                  f1 = param1ArrayOffloat2[i + 4];
                  f2 = param1ArrayOffloat2[i + 5];
                  f3 = param1ArrayOffloat2[i + 2];
                  f5 = param1ArrayOffloat2[i + 3];
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 's':
                  f5 = 0.0F;
                  f3 = 0.0F;
                  if (param1Char1 == 'c' || param1Char1 == 's' || param1Char1 == 'C' || param1Char1 == 'S') {
                    f5 = f1 - f9;
                    f3 = f2 - f8;
                  } 
                  param1Path.rCubicTo(f5, f3, param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3]);
                  f3 = f1 + param1ArrayOffloat2[i + 0];
                  f5 = f2 + param1ArrayOffloat2[i + 1];
                  f1 += param1ArrayOffloat2[i + 2];
                  f2 += param1ArrayOffloat2[i + 3];
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'S':
                  f3 = f1;
                  f5 = f2;
                  if (param1Char1 == 'c' || param1Char1 == 's' || param1Char1 == 'C' || param1Char1 == 'S') {
                    f3 = 2.0F * f1 - f9;
                    f5 = 2.0F * f2 - f8;
                  } 
                  param1Path.cubicTo(f3, f5, param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3]);
                  f3 = param1ArrayOffloat2[i + 0];
                  f5 = param1ArrayOffloat2[i + 1];
                  f1 = param1ArrayOffloat2[i + 2];
                  f2 = param1ArrayOffloat2[i + 3];
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'q':
                  param1Path.rQuadTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3]);
                  f3 = f1 + param1ArrayOffloat2[i + 0];
                  f5 = f2 + param1ArrayOffloat2[i + 1];
                  f1 += param1ArrayOffloat2[i + 2];
                  f2 += param1ArrayOffloat2[i + 3];
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'Q':
                  param1Path.quadTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3]);
                  f3 = param1ArrayOffloat2[i + 0];
                  f5 = param1ArrayOffloat2[i + 1];
                  f1 = param1ArrayOffloat2[i + 2];
                  f2 = param1ArrayOffloat2[i + 3];
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 't':
                  f5 = 0.0F;
                  f3 = 0.0F;
                  if (param1Char1 == 'q' || param1Char1 == 't' || param1Char1 == 'Q' || param1Char1 == 'T') {
                    f5 = f1 - f9;
                    f3 = f2 - f8;
                  } 
                  param1Path.rQuadTo(f5, f3, param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
                  f4 = f1 + f5;
                  f5 = f2 + f3;
                  f1 += param1ArrayOffloat2[i + 0];
                  f2 += param1ArrayOffloat2[i + 1];
                  f3 = f4;
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'T':
                  f5 = f1;
                  f3 = f2;
                  if (param1Char1 == 'q' || param1Char1 == 't' || param1Char1 == 'Q' || param1Char1 == 'T') {
                    f5 = 2.0F * f1 - f9;
                    f3 = 2.0F * f2 - f8;
                  } 
                  param1Path.quadTo(f5, f3, param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
                  f1 = f3;
                  f8 = param1ArrayOffloat2[i + 0];
                  f2 = param1ArrayOffloat2[i + 1];
                  f3 = f5;
                  f5 = f1;
                  f4 = f7;
                  f1 = f8;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'a':
                  f8 = param1ArrayOffloat2[i + 5];
                  f4 = param1ArrayOffloat2[i + 6];
                  f3 = param1ArrayOffloat2[i + 0];
                  f5 = param1ArrayOffloat2[i + 1];
                  f9 = param1ArrayOffloat2[i + 2];
                  if (param1ArrayOffloat2[i + 3] != 0.0F) {
                    bool1 = true;
                  } else {
                    bool1 = false;
                  } 
                  if (param1ArrayOffloat2[i + 4] != 0.0F) {
                    bool2 = true;
                  } else {
                    bool2 = false;
                  } 
                  drawArc(param1Path, f1, f2, f8 + f1, f4 + f2, f3, f5, f9, bool1, bool2);
                  f1 += param1ArrayOffloat2[i + 5];
                  f2 += param1ArrayOffloat2[i + 6];
                  f3 = f1;
                  f5 = f2;
                  f4 = f7;
                  param1Char1 = param1Char2;
                  i += b;
                  f9 = f3;
                  f8 = f5;
                  f7 = f4;
                  continue;
                case 'A':
                  break;
              } 
              f3 = param1ArrayOffloat2[i + 5];
              f8 = param1ArrayOffloat2[i + 6];
              f5 = param1ArrayOffloat2[i + 0];
              f4 = param1ArrayOffloat2[i + 1];
              f9 = param1ArrayOffloat2[i + 2];
              if (param1ArrayOffloat2[i + 3] != 0.0F) {
                bool1 = true;
              } else {
                bool1 = false;
              } 
              if (param1ArrayOffloat2[i + 4] != 0.0F) {
                bool2 = true;
              } else {
                bool2 = false;
              } 
              drawArc(param1Path, f1, f2, f3, f8, f5, f4, f9, bool1, bool2);
              f1 = param1ArrayOffloat2[i + 5];
              f2 = param1ArrayOffloat2[i + 6];
              f3 = f1;
              f5 = f2;
              f4 = f7;
            } else {
              break;
            } 
            param1Char1 = param1Char2;
            i += b;
            f9 = f3;
            f8 = f5;
            f7 = f4;
          } 
          break;
        case 'Z':
        case 'z':
          param1Path.close();
          f1 = f5;
          f2 = f6;
          f3 = f5;
          f4 = f6;
          param1Path.moveTo(f1, f2);
        case 'L':
        case 'M':
        case 'T':
        case 'l':
        case 'm':
        case 't':
          b = 2;
        case 'H':
        case 'V':
        case 'h':
        case 'v':
          b = 1;
        case 'C':
        case 'c':
          b = 6;
        case 'Q':
        case 'S':
        case 'q':
        case 's':
          b = 4;
        case 'A':
        case 'a':
          b = 7;
      } 
      param1ArrayOffloat1[0] = f1;
      param1ArrayOffloat1[1] = f2;
      param1ArrayOffloat1[2] = f9;
      param1ArrayOffloat1[3] = f8;
      param1ArrayOffloat1[4] = f6;
      param1ArrayOffloat1[5] = f7;
    }
    
    private static void arcToBezier(Path param1Path, double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6, double param1Double7, double param1Double8, double param1Double9) {
      int i = (int)Math.ceil(Math.abs(4.0D * param1Double9 / Math.PI));
      double d1 = Math.cos(param1Double7);
      double d2 = Math.sin(param1Double7);
      param1Double7 = Math.cos(param1Double8);
      double d3 = Math.sin(param1Double8);
      double d4 = -param1Double3 * d1 * d3 - param1Double4 * d2 * param1Double7;
      d3 = -param1Double3 * d2 * d3 + param1Double4 * d1 * param1Double7;
      double d5 = param1Double9 / i;
      byte b = 0;
      param1Double7 = param1Double6;
      param1Double6 = param1Double5;
      param1Double9 = param1Double8;
      param1Double8 = d3;
      param1Double5 = d4;
      while (b < i) {
        double d6 = param1Double9 + d5;
        double d7 = Math.sin(d6);
        double d8 = Math.cos(d6);
        double d9 = param1Double3 * d1 * d8 + param1Double1 - param1Double4 * d2 * d7;
        d3 = param1Double3 * d2 * d8 + param1Double2 + param1Double4 * d1 * d7;
        d4 = -param1Double3 * d1 * d7 - param1Double4 * d2 * d8;
        d8 = -param1Double3 * d2 * d7 + param1Double4 * d1 * d8;
        d7 = Math.tan((d6 - param1Double9) / 2.0D);
        param1Double9 = Math.sin(d6 - param1Double9) * (Math.sqrt(4.0D + 3.0D * d7 * d7) - 1.0D) / 3.0D;
        param1Path.rCubicTo((float)(param1Double6 + param1Double9 * param1Double5) - (float)param1Double6, (float)(param1Double7 + param1Double9 * param1Double8) - (float)param1Double7, (float)(d9 - param1Double9 * d4) - (float)param1Double6, (float)(d3 - param1Double9 * d8) - (float)param1Double7, (float)d9 - (float)param1Double6, (float)d3 - (float)param1Double7);
        param1Double9 = d6;
        param1Double6 = d9;
        param1Double7 = d3;
        param1Double5 = d4;
        param1Double8 = d8;
        b++;
      } 
    }
    
    private static void drawArc(Path param1Path, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, boolean param1Boolean1, boolean param1Boolean2) {
      double d1 = Math.toRadians(param1Float7);
      double d2 = Math.cos(d1);
      double d3 = Math.sin(d1);
      double d4 = (param1Float1 * d2 + param1Float2 * d3) / param1Float5;
      double d5 = (-param1Float1 * d3 + param1Float2 * d2) / param1Float6;
      double d6 = (param1Float3 * d2 + param1Float4 * d3) / param1Float5;
      double d7 = (-param1Float3 * d3 + param1Float4 * d2) / param1Float6;
      double d8 = d4 - d6;
      double d9 = d5 - d7;
      double d10 = (d4 + d6) / 2.0D;
      double d11 = (d5 + d7) / 2.0D;
      double d12 = d8 * d8 + d9 * d9;
      if (d12 == 0.0D) {
        Log.w("PathParser", " Points are coincident");
        return;
      } 
      double d13 = 1.0D / d12 - 0.25D;
      if (d13 < 0.0D) {
        Log.w("PathParser", "Points are too far apart " + d12);
        float f = (float)(Math.sqrt(d12) / 1.99999D);
        drawArc(param1Path, param1Float1, param1Float2, param1Float3, param1Float4, param1Float5 * f, param1Float6 * f, param1Float7, param1Boolean1, param1Boolean2);
        return;
      } 
      d13 = Math.sqrt(d13);
      d8 = d13 * d8;
      d9 = d13 * d9;
      if (param1Boolean1 == param1Boolean2) {
        d10 -= d9;
        d11 += d8;
      } else {
        d10 += d9;
        d11 -= d8;
      } 
      d4 = Math.atan2(d5 - d11, d4 - d10);
      d6 = Math.atan2(d7 - d11, d6 - d10) - d4;
      if (d6 >= 0.0D) {
        param1Boolean1 = true;
      } else {
        param1Boolean1 = false;
      } 
      d7 = d6;
      if (param1Boolean2 != param1Boolean1)
        if (d6 > 0.0D) {
          d7 = d6 - 6.283185307179586D;
        } else {
          d7 = d6 + 6.283185307179586D;
        }  
      d10 *= param1Float5;
      d11 *= param1Float6;
      arcToBezier(param1Path, d10 * d2 - d11 * d3, d10 * d3 + d11 * d2, param1Float5, param1Float6, param1Float1, param1Float2, d1, d4, d7);
    }
    
    public static void nodesToPath(PathDataNode[] param1ArrayOfPathDataNode, Path param1Path) {
      float[] arrayOfFloat = new float[6];
      char c1 = 'm';
      byte b = 0;
      char c2;
      for (c2 = c1; b < param1ArrayOfPathDataNode.length; c2 = c1) {
        addCommand(param1Path, arrayOfFloat, c2, (param1ArrayOfPathDataNode[b]).type, (param1ArrayOfPathDataNode[b]).params);
        c1 = (param1ArrayOfPathDataNode[b]).type;
        b++;
      } 
    }
    
    public void interpolatePathDataNode(PathDataNode param1PathDataNode1, PathDataNode param1PathDataNode2, float param1Float) {
      for (byte b = 0; b < param1PathDataNode1.params.length; b++)
        this.params[b] = param1PathDataNode1.params[b] * (1.0F - param1Float) + param1PathDataNode2.params[b] * param1Float; 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/graphics/drawable/PathParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */