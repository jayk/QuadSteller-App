package app.gamer.quadstellar.newdevices.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

public class PlayPauseButton extends View {
  private static final int SPEED = 1;
  
  private static final double SQRT_3 = Math.sqrt(3.0D);
  
  private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
      public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
        PlayPauseButton.this.invalidate();
      }
    };
  
  private int mBackgroundColor = -16777216;
  
  private ValueAnimator mCenterEdgeAnimator;
  
  private ValueAnimator mLeftEdgeAnimator;
  
  private Path mLeftPath;
  
  private OnControlStatusChangeListener mListener;
  
  private Paint mPaint;
  
  private boolean mPlayed;
  
  private final Point mPoint = new Point();
  
  private ValueAnimator mRightEdgeAnimator;
  
  private Path mRightPath;
  
  public PlayPauseButton(Context paramContext) {
    this(paramContext, null, 0);
  }
  
  public PlayPauseButton(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PlayPauseButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  private void initView() {
    setUpPaint();
    setUpPath();
    setUpAnimator();
  }
  
  private void setUpAnimator() {
    if (this.mPlayed) {
      this.mCenterEdgeAnimator = ValueAnimator.ofFloat(new float[] { 1.0F, 1.0F });
      this.mLeftEdgeAnimator = ValueAnimator.ofFloat(new float[] { (float)(SQRT_3 * -0.20000000298023224D), (float)(SQRT_3 * -0.20000000298023224D) });
      this.mRightEdgeAnimator = ValueAnimator.ofFloat(new float[] { 1.0F, 1.0F });
    } else {
      this.mCenterEdgeAnimator = ValueAnimator.ofFloat(new float[] { 0.5F, 0.5F });
      this.mLeftEdgeAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 0.0F });
      this.mRightEdgeAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 0.0F });
    } 
    this.mCenterEdgeAnimator.start();
    this.mLeftEdgeAnimator.start();
    this.mRightEdgeAnimator.start();
  }
  
  private void setUpPaint() {
    this.mPaint = new Paint();
    this.mPaint.setColor(this.mBackgroundColor);
    this.mPaint.setAntiAlias(true);
    this.mPaint.setStyle(Paint.Style.FILL);
  }
  
  private void setUpPath() {
    this.mLeftPath = new Path();
    this.mRightPath = new Path();
  }
  
  public boolean isPlayed() {
    return this.mPlayed;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    this.mPoint.setHeight(paramCanvas.getHeight());
    this.mPoint.setWidth(paramCanvas.getWidth());
    this.mLeftPath.reset();
    this.mRightPath.reset();
    this.mLeftPath.moveTo(this.mPoint.getX(SQRT_3 * -0.5D), this.mPoint.getY(1.0F));
    this.mLeftPath.lineTo(this.mPoint.getY(((Float)this.mLeftEdgeAnimator.getAnimatedValue()).floatValue()) + 0.7F, this.mPoint.getY(((Float)this.mCenterEdgeAnimator.getAnimatedValue()).floatValue()));
    this.mLeftPath.lineTo(this.mPoint.getY(((Float)this.mLeftEdgeAnimator.getAnimatedValue()).floatValue()) + 0.7F, this.mPoint.getY(((Float)this.mCenterEdgeAnimator.getAnimatedValue()).floatValue() * -1.0F));
    this.mLeftPath.lineTo(this.mPoint.getX(SQRT_3 * -0.5D), this.mPoint.getY(-1.0F));
    this.mRightPath.moveTo(this.mPoint.getY(((Float)this.mLeftEdgeAnimator.getAnimatedValue()).floatValue() * -1.0F), this.mPoint.getY(((Float)this.mCenterEdgeAnimator.getAnimatedValue()).floatValue()));
    this.mRightPath.lineTo(this.mPoint.getX(SQRT_3 * 0.5D), this.mPoint.getY(((Float)this.mRightEdgeAnimator.getAnimatedValue()).floatValue()));
    this.mRightPath.lineTo(this.mPoint.getX(SQRT_3 * 0.5D), this.mPoint.getY(((Float)this.mRightEdgeAnimator.getAnimatedValue()).floatValue() * -1.0F));
    this.mRightPath.lineTo(this.mPoint.getY(((Float)this.mLeftEdgeAnimator.getAnimatedValue()).floatValue() * -1.0F), this.mPoint.getY(((Float)this.mCenterEdgeAnimator.getAnimatedValue()).floatValue() * -1.0F));
    paramCanvas.drawPath(this.mLeftPath, this.mPaint);
    paramCanvas.drawPath(this.mRightPath, this.mPaint);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    setPlayed(savedState.played);
    setUpAnimator();
    invalidate();
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.played = isPlayed();
    return (Parcelable)savedState;
  }
  
  public void setBackground(Drawable paramDrawable) {}
  
  public void setColor(int paramInt) {
    this.mBackgroundColor = paramInt;
    this.mPaint.setColor(this.mBackgroundColor);
    invalidate();
  }
  
  public void setOnControlStatusChangeListener(OnControlStatusChangeListener paramOnControlStatusChangeListener) {
    this.mListener = paramOnControlStatusChangeListener;
  }
  
  public void setPlayed(boolean paramBoolean) {
    if (this.mPlayed != paramBoolean) {
      this.mPlayed = paramBoolean;
      invalidate();
    } 
  }
  
  public void startAnimation() {
    this.mCenterEdgeAnimator = ValueAnimator.ofFloat(new float[] { 1.0F, 0.5F });
    this.mCenterEdgeAnimator.setDuration(100L);
    this.mCenterEdgeAnimator.addUpdateListener(this.mAnimatorUpdateListener);
    this.mLeftEdgeAnimator = ValueAnimator.ofFloat(new float[] { (float)(-0.2D * SQRT_3), 0.0F });
    this.mLeftEdgeAnimator.setDuration(100L);
    this.mLeftEdgeAnimator.addUpdateListener(this.mAnimatorUpdateListener);
    this.mRightEdgeAnimator = ValueAnimator.ofFloat(new float[] { 1.0F, 0.0F });
    this.mRightEdgeAnimator.setDuration(150L);
    this.mRightEdgeAnimator.addUpdateListener(this.mAnimatorUpdateListener);
    if (!this.mPlayed) {
      this.mCenterEdgeAnimator.start();
      this.mLeftEdgeAnimator.start();
      this.mRightEdgeAnimator.start();
      return;
    } 
    this.mCenterEdgeAnimator.reverse();
    this.mLeftEdgeAnimator.reverse();
    this.mRightEdgeAnimator.reverse();
  }
  
  public static interface OnControlStatusChangeListener {
    void onStatusChange(View param1View, boolean param1Boolean);
  }
  
  static class Point {
    private int height;
    
    private int width;
    
    public float getX(double param1Double) {
      return getX((float)param1Double);
    }
    
    public float getX(float param1Float) {
      return (this.width / 2) * (1.0F + param1Float);
    }
    
    public float getY(double param1Double) {
      return getY((float)param1Double);
    }
    
    public float getY(float param1Float) {
      return (this.height / 2) * (1.0F + param1Float);
    }
    
    public void setHeight(int param1Int) {
      this.height = param1Int;
    }
    
    public void setWidth(int param1Int) {
      this.width = param1Int;
    }
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public PlayPauseButton.SavedState createFromParcel(Parcel param2Parcel) {
          return new PlayPauseButton.SavedState(param2Parcel);
        }
        
        public PlayPauseButton.SavedState[] newArray(int param2Int) {
          return new PlayPauseButton.SavedState[param2Int];
        }
      };
    
    boolean played;
    
    private SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      this.played = ((Boolean)param1Parcel.readValue(null)).booleanValue();
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(@NonNull Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeValue(Boolean.valueOf(this.played));
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public PlayPauseButton.SavedState createFromParcel(Parcel param1Parcel) {
      return new PlayPauseButton.SavedState(param1Parcel);
    }
    
    public PlayPauseButton.SavedState[] newArray(int param1Int) {
      return new PlayPauseButton.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/PlayPauseButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */