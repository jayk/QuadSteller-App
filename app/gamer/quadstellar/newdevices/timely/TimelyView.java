package app.gamer.quadstellar.newdevices.timely;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import app.gamer.quadstellar.R;
import app.gamer.quadstellar.newdevices.timely.animation.TimelyEvaluator;
import app.gamer.quadstellar.newdevices.timely.model.NumberUtils;

public class TimelyView extends View {
  private static final Property<TimelyView, float[][]> CONTROL_POINTS_PROPERTY = new Property<TimelyView, float[][]>(float[][].class, "controlPoints") {
      public float[][] get(TimelyView param1TimelyView) {
        return param1TimelyView.getControlPoints();
      }
      
      public void set(TimelyView param1TimelyView, float[][] param1ArrayOffloat) {
        param1TimelyView.setControlPoints(param1ArrayOffloat);
      }
    };
  
  private static final float RATIO = 1.0F;
  
  private float[][] controlPoints = (float[][])null;
  
  private Paint mPaint = null;
  
  private Path mPath = null;
  
  private int textColor;
  
  public TimelyView(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public TimelyView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    this.textColor = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TimelyView).getColor(0, -16777216);
    init();
  }
  
  public TimelyView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private void init() {
    this.mPaint = new Paint();
    this.mPaint.setAntiAlias(true);
    this.mPaint.setColor(this.textColor);
    this.mPaint.setStrokeWidth(5.0F);
    this.mPaint.setStyle(Paint.Style.STROKE);
    this.mPath = new Path();
  }
  
  public ObjectAnimator animate(int paramInt) {
    float[][] arrayOfFloat1 = NumberUtils.getControlPointsFor(-1);
    float[][] arrayOfFloat2 = NumberUtils.getControlPointsFor(paramInt);
    return ObjectAnimator.ofObject(this, CONTROL_POINTS_PROPERTY, (TypeEvaluator)new TimelyEvaluator(), (Object[])new float[][][] { arrayOfFloat1, arrayOfFloat2 });
  }
  
  public ObjectAnimator animate(int paramInt1, int paramInt2) {
    float[][] arrayOfFloat1 = NumberUtils.getControlPointsFor(paramInt1);
    float[][] arrayOfFloat2 = NumberUtils.getControlPointsFor(paramInt2);
    return ObjectAnimator.ofObject(this, CONTROL_POINTS_PROPERTY, (TypeEvaluator)new TimelyEvaluator(), (Object[])new float[][][] { arrayOfFloat1, arrayOfFloat2 });
  }
  
  public float[][] getControlPoints() {
    return this.controlPoints;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.controlPoints != null) {
      float f;
      int i = this.controlPoints.length;
      int j = getMeasuredHeight();
      int k = getMeasuredWidth();
      if (j > k) {
        f = k;
      } else {
        f = j;
      } 
      this.mPath.reset();
      this.mPath.moveTo(this.controlPoints[0][0] * f, this.controlPoints[0][1] * f);
      for (j = 1; j < i; j += 3)
        this.mPath.cubicTo(this.controlPoints[j][0] * f, this.controlPoints[j][1] * f, this.controlPoints[j + 1][0] * f, this.controlPoints[j + 1][1] * f, this.controlPoints[j + 2][0] * f, this.controlPoints[j + 2][1] * f); 
      paramCanvas.drawPath(this.mPath, this.mPaint);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    paramInt1 = getMeasuredWidth();
    paramInt2 = getMeasuredHeight();
    int i = paramInt1 - getPaddingLeft() - getPaddingRight();
    int j = (int)((paramInt2 - getPaddingTop() - getPaddingBottom()) * 1.0F);
    int k = (int)(i / 1.0F);
    if (i > j) {
      paramInt1 = getPaddingLeft() + j + getPaddingRight();
    } else {
      paramInt2 = getPaddingTop() + k + getPaddingBottom();
    } 
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public void setControlPoints(float[][] paramArrayOffloat) {
    this.controlPoints = paramArrayOffloat;
    invalidate();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/TimelyView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */