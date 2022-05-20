package app.gamer.quadstellar.newdevices.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import app.gamer.quadstellar.R;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LoopView extends View {
  int change;
  
  int conterTextColor;
  
  Context context;
  
  int firstLineY;
  
  private GestureDetector gestureDetector;
  
  int halfCircumference;
  
  Handler handler;
  
  int indicatorTextColor;
  
  int initPosition;
  
  public boolean isInit;
  
  boolean isLoop;
  
  ArrayList<String> items;
  
  int itemsVisible;
  
  float lineSpacingMultiplier;
  
  ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
  
  private ScheduledFuture<?> mFuture;
  
  private int mOffset = 0;
  
  int maxTextHeight;
  
  int maxTextWidth;
  
  int measuredHeight;
  
  int measuredWidth;
  
  OnItemSelectedListener onItemSelectedListener;
  
  int outerTextColor;
  
  int paddingLeft = 0;
  
  int paddingRight = 0;
  
  Paint paintCenterText;
  
  Paint paintIndicator;
  
  Paint paintOuterText;
  
  int preCurrentIndex;
  
  private float previousY;
  
  int radius;
  
  private float scaleX = 1.05F;
  
  int secondLineY;
  
  private int selectedItem;
  
  long startTime = 0L;
  
  private Rect tempRect = new Rect();
  
  int textSize;
  
  int totalScrollY;
  
  public LoopView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LoopView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.LoopView);
    this.conterTextColor = typedArray.getInt(0, -5263441);
    this.outerTextColor = typedArray.getInt(1, -13553359);
    this.indicatorTextColor = typedArray.getInt(2, -3815995);
    typedArray.recycle();
    initLoopView(paramContext);
  }
  
  private int getTextX(String paramString, Paint paramPaint, Rect paramRect) {
    paramPaint.getTextBounds(paramString, 0, paramString.length(), paramRect);
    int i = (int)(paramRect.width() * this.scaleX);
    return (this.measuredWidth - i) / 2;
  }
  
  private void initLoopView(Context paramContext) {
    this.context = paramContext;
    this.handler = new MessageHandler(this);
    this.gestureDetector = new GestureDetector(paramContext, (GestureDetector.OnGestureListener)new LoopViewGestureListener(this));
    this.gestureDetector.setIsLongpressEnabled(false);
    this.lineSpacingMultiplier = 2.0F;
    this.isLoop = true;
    this.itemsVisible = 9;
    this.textSize = 0;
    this.totalScrollY = 0;
    this.initPosition = -1;
    initPaints();
    setTextSize(16.0F);
  }
  
  private void initPaints() {
    this.paintOuterText = new Paint();
    this.paintOuterText.setColor(this.conterTextColor);
    this.paintOuterText.setAntiAlias(true);
    this.paintOuterText.setTypeface(Typeface.MONOSPACE);
    this.paintOuterText.setTextSize(this.textSize);
    this.paintCenterText = new Paint();
    this.paintCenterText.setColor(this.outerTextColor);
    this.paintCenterText.setAntiAlias(true);
    this.paintCenterText.setTextScaleX(this.scaleX);
    this.paintCenterText.setTypeface(Typeface.MONOSPACE);
    this.paintCenterText.setTextSize(this.textSize);
    this.paintIndicator = new Paint();
    this.paintIndicator.setColor(this.indicatorTextColor);
    this.paintIndicator.setAntiAlias(true);
    if (Build.VERSION.SDK_INT >= 11)
      setLayerType(1, null); 
  }
  
  private void measureTextWidthHeight() {
    for (byte b = 0; b < this.items.size(); b++) {
      String str = this.items.get(b);
      this.paintCenterText.getTextBounds(str, 0, str.length(), this.tempRect);
      int i = this.tempRect.width();
      if (i > this.maxTextWidth)
        this.maxTextWidth = (int)(i * this.scaleX); 
      this.paintCenterText.getTextBounds("星期", 0, 2, this.tempRect);
      i = this.tempRect.height();
      if (i > this.maxTextHeight)
        this.maxTextHeight = i; 
    } 
  }
  
  private void remeasure() {
    if (this.items != null) {
      measureTextWidthHeight();
      this.halfCircumference = (int)(this.maxTextHeight * this.lineSpacingMultiplier * (this.itemsVisible - 1));
      this.measuredHeight = (int)((this.halfCircumference * 2) / Math.PI);
      this.radius = (int)(this.halfCircumference / Math.PI);
      this.measuredWidth = this.maxTextWidth + this.paddingLeft + this.paddingRight;
      this.firstLineY = (int)((this.measuredHeight - this.lineSpacingMultiplier * this.maxTextHeight) / 2.0F);
      this.secondLineY = (int)((this.measuredHeight + this.lineSpacingMultiplier * this.maxTextHeight) / 2.0F);
      if (this.initPosition == -1)
        if (this.isLoop) {
          this.initPosition = (this.items.size() + 1) / 2;
        } else {
          this.initPosition = 0;
        }  
      this.preCurrentIndex = this.initPosition;
      Log.d("LoopView", "preCurrentIndex:" + this.preCurrentIndex + "initPosition" + this.initPosition);
    } 
  }
  
  public void cancelFuture() {
    if (this.mFuture != null && !this.mFuture.isCancelled()) {
      this.mFuture.cancel(true);
      this.mFuture = null;
    } 
  }
  
  public ArrayList<String> getData() {
    return this.items;
  }
  
  public int getPaddingLeft() {
    return this.paddingLeft;
  }
  
  public int getPaddingRight() {
    return this.paddingRight;
  }
  
  public final int getSelectedItem() {
    return this.selectedItem;
  }
  
  public int listSize() {
    return (this.items != null) ? this.items.size() : 0;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.items != null) {
      String[] arrayOfString = new String[this.itemsVisible];
      if (this.isInit) {
        this.change = 0;
        this.isInit = false;
      } else {
        this.change = (int)(this.totalScrollY / this.lineSpacingMultiplier * this.maxTextHeight);
      } 
      this.preCurrentIndex = this.initPosition + this.change % this.items.size();
      Log.d("LoopView", "change:" + this.change);
      Log.d("LoopView", "preCu===rentIndex:" + this.preCurrentIndex);
      if (!this.isLoop) {
        if (this.preCurrentIndex < 0)
          this.preCurrentIndex = 0; 
        if (this.preCurrentIndex > this.items.size() - 1)
          this.preCurrentIndex = this.items.size() - 1; 
      } else {
        if (this.preCurrentIndex < 0)
          this.preCurrentIndex = this.items.size() + this.preCurrentIndex; 
        if (this.preCurrentIndex > this.items.size() - 1)
          this.preCurrentIndex -= this.items.size(); 
      } 
      int i = (int)(this.totalScrollY % this.lineSpacingMultiplier * this.maxTextHeight);
      int j;
      for (j = 0; j < this.itemsVisible; j++) {
        int k = this.preCurrentIndex - this.itemsVisible / 2 - j;
        if (this.isLoop) {
          int m;
          while (true) {
            m = k;
            if (k < 0) {
              k += this.items.size();
              continue;
            } 
            break;
          } 
          while (m > this.items.size() - 1)
            m -= this.items.size(); 
          arrayOfString[j] = this.items.get(m);
        } else if (k < 0) {
          arrayOfString[j] = "";
        } else if (k > this.items.size() - 1) {
          arrayOfString[j] = "";
        } else {
          arrayOfString[j] = this.items.get(k);
        } 
      } 
      paramCanvas.drawLine(0.0F, this.firstLineY, this.measuredWidth, this.firstLineY, this.paintIndicator);
      paramCanvas.drawLine(0.0F, this.secondLineY, this.measuredWidth, this.secondLineY, this.paintIndicator);
      byte b = 0;
      while (true) {
        if (b < this.itemsVisible) {
          paramCanvas.save();
          float f1 = this.maxTextHeight * this.lineSpacingMultiplier;
          double d = (b * f1 - i) * Math.PI / this.halfCircumference;
          float f2 = (float)(90.0D - d / Math.PI * 180.0D);
          if (f2 >= 90.0F || f2 <= -90.0F) {
            paramCanvas.restore();
          } else {
            j = (int)(this.radius - Math.cos(d) * this.radius - Math.sin(d) * this.maxTextHeight / 2.0D);
            paramCanvas.translate(0.0F, j);
            paramCanvas.scale(1.0F, (float)Math.sin(d));
            if (j <= this.firstLineY && this.maxTextHeight + j >= this.firstLineY) {
              paramCanvas.save();
              paramCanvas.clipRect(0, 0, this.measuredWidth, this.firstLineY - j);
              paramCanvas.drawText(arrayOfString[b], getTextX(arrayOfString[b], this.paintOuterText, this.tempRect), this.maxTextHeight, this.paintOuterText);
              paramCanvas.restore();
              paramCanvas.save();
              paramCanvas.clipRect(0, this.firstLineY - j, this.measuredWidth, (int)f1);
              paramCanvas.drawText(arrayOfString[b], getTextX(arrayOfString[b], this.paintCenterText, this.tempRect), this.maxTextHeight, this.paintCenterText);
              paramCanvas.restore();
            } else if (j <= this.secondLineY && this.maxTextHeight + j >= this.secondLineY) {
              paramCanvas.save();
              paramCanvas.clipRect(0, 0, this.measuredWidth, this.secondLineY - j);
              paramCanvas.drawText(arrayOfString[b], getTextX(arrayOfString[b], this.paintCenterText, this.tempRect), this.maxTextHeight, this.paintCenterText);
              paramCanvas.restore();
              paramCanvas.save();
              paramCanvas.clipRect(0, this.secondLineY - j, this.measuredWidth, (int)f1);
              paramCanvas.drawText(arrayOfString[b], getTextX(arrayOfString[b], this.paintOuterText, this.tempRect), this.maxTextHeight, this.paintOuterText);
              paramCanvas.restore();
            } else if (j >= this.firstLineY && this.maxTextHeight + j <= this.secondLineY) {
              paramCanvas.clipRect(0, 0, this.measuredWidth, (int)f1);
              paramCanvas.drawText(arrayOfString[b], getTextX(arrayOfString[b], this.paintCenterText, this.tempRect), this.maxTextHeight, this.paintCenterText);
              this.selectedItem = this.items.indexOf(arrayOfString[b]);
            } else {
              paramCanvas.clipRect(0, 0, this.measuredWidth, (int)f1);
              paramCanvas.drawText(arrayOfString[b], getTextX(arrayOfString[b], this.paintOuterText, this.tempRect), this.maxTextHeight, this.paintOuterText);
            } 
            paramCanvas.restore();
          } 
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  protected final void onItemSelected() {
    if (this.onItemSelectedListener != null)
      postDelayed(new OnItemSelectedRunnable(this), 200L); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    remeasure();
    setMeasuredDimension(this.measuredWidth, this.measuredHeight);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f2;
    float f3;
    boolean bool = this.gestureDetector.onTouchEvent(paramMotionEvent);
    float f1 = this.lineSpacingMultiplier * this.maxTextHeight;
    switch (paramMotionEvent.getAction()) {
      default:
        if (!bool) {
          float f = paramMotionEvent.getY();
          double d1 = Math.acos(((this.radius - f) / this.radius));
          double d2 = this.radius;
          int i = (int)(((f1 / 2.0F) + d1 * d2) / f1);
          f = this.totalScrollY;
          this.mOffset = (int)((i - this.itemsVisible / 2) * f1 - (f % f1 + f1) % f1);
          if (System.currentTimeMillis() - this.startTime > 120L) {
            smoothScroll(ACTION.DAGGLE);
            invalidate();
            return true;
          } 
          break;
        } 
        invalidate();
        return true;
      case 0:
        this.startTime = System.currentTimeMillis();
        cancelFuture();
        this.previousY = paramMotionEvent.getRawY();
        invalidate();
        return true;
      case 2:
        f2 = this.previousY;
        f3 = paramMotionEvent.getRawY();
        this.previousY = paramMotionEvent.getRawY();
        this.totalScrollY = (int)(this.totalScrollY + f2 - f3);
        if (!this.isLoop) {
          f2 = -this.initPosition * f1;
          f1 = (this.items.size() - 1 - this.initPosition) * f1;
          if (this.totalScrollY < f2) {
            this.totalScrollY = (int)f2;
            invalidate();
            return true;
          } 
          if (this.totalScrollY > f1)
            this.totalScrollY = (int)f1; 
        } 
        invalidate();
        return true;
    } 
    smoothScroll(ACTION.CLICK);
    invalidate();
    return true;
  }
  
  protected final void scrollBy(float paramFloat) {
    cancelFuture();
    this.mFuture = this.mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, paramFloat), 0L, 10L, TimeUnit.MILLISECONDS);
  }
  
  public void setConterTextColor(int paramInt) {
    this.conterTextColor = paramInt;
  }
  
  public void setContextSzie(float paramFloat) {
    if (paramFloat > 0.0F) {
      this.textSize = (int)((this.context.getResources().getDisplayMetrics()).density * paramFloat);
      this.paintCenterText.setTextSize(this.textSize);
    } 
  }
  
  public void setIndicatorTextColor(int paramInt) {
    this.indicatorTextColor = paramInt;
  }
  
  public final void setInitPosition(int paramInt) {
    if (paramInt < 0) {
      this.initPosition = 0;
      return;
    } 
    if (this.items != null && this.items.size() - 1 >= paramInt)
      this.initPosition = paramInt; 
  }
  
  public final void setItems(ArrayList<String> paramArrayList) {
    this.items = paramArrayList;
    remeasure();
    invalidate();
  }
  
  public final void setListener(OnItemSelectedListener paramOnItemSelectedListener) {
    this.onItemSelectedListener = paramOnItemSelectedListener;
  }
  
  public final void setNotLoop() {
    this.isLoop = false;
  }
  
  public void setOuterTextColor(int paramInt) {
    this.outerTextColor = paramInt;
  }
  
  public void setSelectedItem(int paramInt) {
    this.initPosition -= paramInt;
  }
  
  public final void setTextSize(float paramFloat) {
    if (paramFloat > 0.0F) {
      this.textSize = (int)((this.context.getResources().getDisplayMetrics()).density * paramFloat);
      this.paintOuterText.setTextSize(this.textSize);
    } 
  }
  
  public void setViewPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.paddingLeft = paramInt1;
    this.paddingRight = paramInt3;
  }
  
  void smoothScroll(ACTION paramACTION) {
    cancelFuture();
    if (paramACTION == ACTION.FLING || paramACTION == ACTION.DAGGLE) {
      float f = this.lineSpacingMultiplier * this.maxTextHeight;
      this.mOffset = (int)((this.totalScrollY % f + f) % f);
      if (this.mOffset > f / 2.0F) {
        this.mOffset = (int)(f - this.mOffset);
      } else {
        this.mOffset = -this.mOffset;
      } 
    } 
    this.mFuture = this.mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, this.mOffset), 0L, 10L, TimeUnit.MILLISECONDS);
  }
  
  public enum ACTION {
    CLICK, DAGGLE, FLING;
    
    static {
      $VALUES = new ACTION[] { CLICK, FLING, DAGGLE };
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/LoopView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */