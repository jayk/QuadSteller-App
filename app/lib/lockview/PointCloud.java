package app.lib.lockview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.ArrayList;

public class PointCloud {
  private static final int INNER_POINTS = 8;
  
  private static final float MAX_POINT_SIZE = 4.0F;
  
  private static final float MIN_POINT_SIZE = 2.0F;
  
  private static final float PI = 3.1415927F;
  
  private static final String TAG = "PointCloud";
  
  GlowManager glowManager = new GlowManager();
  
  private float mCenterX;
  
  private float mCenterY;
  
  private Drawable mDrawable;
  
  private float mOuterRadius;
  
  private Paint mPaint = new Paint();
  
  private ArrayList<Point> mPointCloud = new ArrayList<Point>();
  
  private float mScale = 1.0F;
  
  WaveManager waveManager = new WaveManager();
  
  public PointCloud(Drawable paramDrawable) {
    this.mPaint.setFilterBitmap(true);
    this.mPaint.setColor(Color.rgb(0, 0, 0));
    this.mPaint.setAntiAlias(true);
    this.mPaint.setDither(true);
    this.mDrawable = paramDrawable;
    if (this.mDrawable != null)
      paramDrawable.setBounds(0, 0, paramDrawable.getIntrinsicWidth(), paramDrawable.getIntrinsicHeight()); 
  }
  
  private static float hypot(float paramFloat1, float paramFloat2) {
    return (float)Math.sqrt((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2));
  }
  
  private float interp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat2 - paramFloat1) * paramFloat3 + paramFloat1;
  }
  
  private static float max(float paramFloat1, float paramFloat2) {
    if (paramFloat1 <= paramFloat2)
      paramFloat1 = paramFloat2; 
    return paramFloat1;
  }
  
  public void draw(Canvas paramCanvas) {
    ArrayList<Point> arrayList = this.mPointCloud;
    paramCanvas.save(1);
    paramCanvas.scale(this.mScale, this.mScale, this.mCenterX, this.mCenterY);
    for (byte b = 0; b < arrayList.size(); b++) {
      Point point = arrayList.get(b);
      float f1 = interp(4.0F, 2.0F, point.radius / this.mOuterRadius);
      float f2 = point.x + this.mCenterX;
      float f3 = point.y + this.mCenterY;
      int i = getAlphaForPoint(point);
      if (i != 0)
        if (this.mDrawable != null) {
          paramCanvas.save(1);
          float f4 = this.mDrawable.getIntrinsicWidth();
          float f5 = this.mDrawable.getIntrinsicHeight();
          f1 /= 4.0F;
          paramCanvas.scale(f1, f1, f2, f3);
          paramCanvas.translate(f2 - f4 * 0.5F, f3 - f5 * 0.5F);
          this.mDrawable.setAlpha(i);
          this.mDrawable.draw(paramCanvas);
          paramCanvas.restore();
        } else {
          this.mPaint.setAlpha(i);
          paramCanvas.drawCircle(f2, f3, f1, this.mPaint);
        }  
    } 
    paramCanvas.restore();
  }
  
  public int getAlphaForPoint(Point paramPoint) {
    float f = hypot(this.glowManager.x - paramPoint.x, this.glowManager.y - paramPoint.y);
    if (f < this.glowManager.radius) {
      f = (float)Math.cos((0.7853982F * f / this.glowManager.radius));
      this.glowManager.alpha;
      max(0.0F, (float)Math.pow(f, 10.0D));
    } 
    f = hypot(paramPoint.x, paramPoint.y) - this.waveManager.radius;
    if (f < this.waveManager.width * 0.5F && f < 0.0F) {
      f = (float)Math.cos((0.7853982F * f / this.waveManager.width));
      this.waveManager.alpha;
      max(0.0F, (float)Math.pow(f, 20.0D));
    } 
    return 0;
  }
  
  public float getScale() {
    return this.mScale;
  }
  
  public void makePointCloud(float paramFloat1, float paramFloat2) {
    if (paramFloat1 == 0.0F) {
      Log.w("PointCloud", "Must specify an inner radius");
      return;
    } 
    this.mOuterRadius = paramFloat2;
    this.mPointCloud.clear();
    paramFloat2 -= paramFloat1;
    float f1 = 6.2831855F * paramFloat1 / 8.0F;
    int i = Math.round(paramFloat2 / f1);
    float f2 = paramFloat2 / i;
    byte b = 0;
    while (true) {
      if (b <= i) {
        int j = (int)(6.2831855F * paramFloat1 / f1);
        paramFloat2 = 1.5707964F;
        float f = 6.2831855F / j;
        for (byte b1 = 0; b1 < j; b1++) {
          float f3 = (float)Math.cos(paramFloat2);
          float f4 = (float)Math.sin(paramFloat2);
          paramFloat2 += f;
          this.mPointCloud.add(new Point(paramFloat1 * f3, paramFloat1 * f4, paramFloat1));
        } 
        b++;
        paramFloat1 += f2;
        continue;
      } 
      return;
    } 
  }
  
  public void setCenter(float paramFloat1, float paramFloat2) {
    this.mCenterX = paramFloat1;
    this.mCenterY = paramFloat2;
  }
  
  public void setScale(float paramFloat) {
    this.mScale = paramFloat;
  }
  
  public class GlowManager {
    private float alpha = 0.0F;
    
    private float radius = 0.0F;
    
    private float x;
    
    private float y;
    
    public float getAlpha() {
      return this.alpha;
    }
    
    public float getRadius() {
      return this.radius;
    }
    
    public float getX() {
      return this.x;
    }
    
    public float getY() {
      return this.y;
    }
    
    public void setAlpha(float param1Float) {
      this.alpha = param1Float;
    }
    
    public void setRadius(float param1Float) {
      this.radius = param1Float;
    }
    
    public void setX(float param1Float) {
      this.x = param1Float;
    }
    
    public void setY(float param1Float) {
      this.y = param1Float;
    }
  }
  
  class Point {
    float radius;
    
    float x;
    
    float y;
    
    public Point(float param1Float1, float param1Float2, float param1Float3) {
      this.x = param1Float1;
      this.y = param1Float2;
      this.radius = param1Float3;
    }
  }
  
  public class WaveManager {
    private float alpha = 0.0F;
    
    private float radius = 50.0F;
    
    private float width = 200.0F;
    
    public float getAlpha() {
      return this.alpha;
    }
    
    public float getRadius() {
      return this.radius;
    }
    
    public void setAlpha(float param1Float) {
      this.alpha = param1Float;
    }
    
    public void setRadius(float param1Float) {
      this.radius = param1Float;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/lockview/PointCloud.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */