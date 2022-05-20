package app.gamer.quadstellar.ui.widget.ptr.header;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import java.util.Random;

public class StoreHouseBarItem extends Animation {
  public int index;
  
  private PointF mCEndPoint;
  
  private PointF mCStartPoint;
  
  private float mFromAlpha = 1.0F;
  
  private final Paint mPaint = new Paint();
  
  private float mToAlpha = 0.4F;
  
  public PointF midPoint;
  
  public float translationX;
  
  public StoreHouseBarItem(int paramInt1, PointF paramPointF1, PointF paramPointF2, int paramInt2, int paramInt3) {
    this.index = paramInt1;
    this.midPoint = new PointF((paramPointF1.x + paramPointF2.x) / 2.0F, (paramPointF1.y + paramPointF2.y) / 2.0F);
    this.mCStartPoint = new PointF(paramPointF1.x - this.midPoint.x, paramPointF1.y - this.midPoint.y);
    this.mCEndPoint = new PointF(paramPointF2.x - this.midPoint.x, paramPointF2.y - this.midPoint.y);
    setColor(paramInt2);
    setLineWidth(paramInt3);
    this.mPaint.setAntiAlias(true);
    this.mPaint.setStyle(Paint.Style.STROKE);
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation) {
    float f = this.mFromAlpha;
    setAlpha(f + (this.mToAlpha - f) * paramFloat);
  }
  
  public void draw(Canvas paramCanvas) {
    paramCanvas.drawLine(this.mCStartPoint.x, this.mCStartPoint.y, this.mCEndPoint.x, this.mCEndPoint.y, this.mPaint);
  }
  
  public void resetPosition(int paramInt) {
    this.translationX = (-(new Random()).nextInt(paramInt) + paramInt);
  }
  
  public void setAlpha(float paramFloat) {
    this.mPaint.setAlpha((int)(255.0F * paramFloat));
  }
  
  public void setColor(int paramInt) {
    this.mPaint.setColor(paramInt);
  }
  
  public void setLineWidth(int paramInt) {
    this.mPaint.setStrokeWidth(paramInt);
  }
  
  public void start(float paramFloat1, float paramFloat2) {
    this.mFromAlpha = paramFloat1;
    this.mToAlpha = paramFloat2;
    start();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/header/StoreHouseBarItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */