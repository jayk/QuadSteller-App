package android.support.design.widget;

class MathUtils {
  static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (paramFloat1 >= paramFloat2) {
      if (paramFloat1 > paramFloat3)
        return paramFloat3; 
      paramFloat2 = paramFloat1;
    } 
    return paramFloat2;
  }
  
  static int constrain(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 >= paramInt2) {
      if (paramInt1 > paramInt3)
        return paramInt3; 
      paramInt2 = paramInt1;
    } 
    return paramInt2;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/MathUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */