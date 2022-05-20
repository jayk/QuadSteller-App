package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import app.gamer.quadstellar.utils.DensityUtils;

public class DragGridView extends GridView {
  private MotionEvent downEv;
  
  public int downX;
  
  public int downY;
  
  private Bitmap dragBitmap;
  
  private View dragImageView = null;
  
  private ViewGroup dragItemView = null;
  
  int dragOffsetX;
  
  int dragOffsetY;
  
  public int dragPosition;
  
  private double dragScale = 1.0D;
  
  private boolean isMoving = false;
  
  public int lastY;
  
  private DragGridViewListener listener;
  
  private int mHorizontalSpacing = 15;
  
  private DragState mState = DragState.TOUCH_DRAG;
  
  private Vibrator mVibrator;
  
  private int win_view_x;
  
  private int win_view_y;
  
  private WindowManager windowManager = null;
  
  private WindowManager.LayoutParams windowParams = null;
  
  public int windowX;
  
  public int windowY;
  
  public DragGridView(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public DragGridView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public DragGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void onDrag(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.dragImageView != null) {
      this.windowParams.alpha = 0.6F;
      this.windowParams.x = paramInt3 - this.win_view_x;
      this.windowParams.y = paramInt4 - this.win_view_y;
      this.windowManager.updateViewLayout(this.dragImageView, (ViewGroup.LayoutParams)this.windowParams);
    } 
  }
  
  private void setTouchDragStart(MotionEvent paramMotionEvent) {
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    this.dragPosition = pointToPosition(i, j);
    if (this.dragPosition != -1) {
      this.dragItemView = (ViewGroup)getChildAt(this.dragPosition - getFirstVisiblePosition());
      this.win_view_x = this.windowX - this.dragItemView.getLeft();
      this.win_view_y = this.windowY - this.dragItemView.getTop();
      this.dragOffsetX = (int)(paramMotionEvent.getRawX() - i);
      this.dragOffsetY = (int)(paramMotionEvent.getRawY() - j);
      this.dragItemView.destroyDrawingCache();
      this.dragItemView.setDrawingCacheEnabled(true);
      this.dragBitmap = Bitmap.createBitmap(this.dragItemView.getDrawingCache());
      startDrag(this.dragBitmap, (int)paramMotionEvent.getRawX(), (int)paramMotionEvent.getRawY());
      this.dragItemView.setVisibility(4);
      this.isMoving = true;
      this.lastY = (int)paramMotionEvent.getRawY();
      requestDisallowInterceptTouchEvent(false);
    } 
  }
  
  private void stopDrag() {
    if (this.dragImageView != null) {
      this.windowManager.removeView(this.dragImageView);
      this.dragImageView = null;
    } 
    if (this.dragItemView != null)
      this.dragItemView.setVisibility(0); 
  }
  
  public boolean getMovingState() {
    return this.isMoving;
  }
  
  public DragState getState() {
    return this.mState;
  }
  
  public void init(Context paramContext) {
    if (!isInEditMode()) {
      this.mVibrator = (Vibrator)paramContext.getSystemService("vibrator");
      this.mHorizontalSpacing = DensityUtils.dip2px(paramContext, this.mHorizontalSpacing);
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 0) {
      this.downX = (int)paramMotionEvent.getX();
      this.downY = (int)paramMotionEvent.getY();
      this.windowX = (int)paramMotionEvent.getX();
      this.windowY = (int)paramMotionEvent.getY();
      this.downEv = paramMotionEvent;
    } 
    return super.onInterceptTouchEvent(paramMotionEvent);
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(536870911, -2147483648));
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool = false;
    if (this.mState == DragState.TOUCH_DRAG) {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      switch (paramMotionEvent.getAction()) {
        default:
          return super.onTouchEvent(paramMotionEvent);
        case 0:
          this.downX = (int)paramMotionEvent.getX();
          this.windowX = (int)paramMotionEvent.getX();
          this.downY = (int)paramMotionEvent.getY();
          this.windowY = (int)paramMotionEvent.getY();
        case 2:
          if (this.dragImageView == null)
            setTouchDragStart(paramMotionEvent); 
          if (this.dragPosition != -1) {
            int k = (int)paramMotionEvent.getRawX();
            int m = (int)paramMotionEvent.getRawY();
            onDrag(i, j, k, m);
            if (this.listener != null && this.windowParams != null && this.dragImageView != null) {
              if (m < this.lastY)
                bool = true; 
              i = this.lastY;
              this.listener.onMoving(this.windowParams.x, this.windowParams.y, bool, m - i);
            } 
            this.lastY = m;
            requestDisallowInterceptTouchEvent(true);
          } 
        case 1:
        case 3:
          break;
      } 
      this.isMoving = false;
      if (this.listener != null && this.windowParams != null && this.dragImageView != null) {
        DragGridViewListener dragGridViewListener = this.listener;
        i = this.windowParams.x;
        int k = this.dragImageView.getWidth() / 2;
        j = this.windowParams.y;
        dragGridViewListener.onDrop(this, k + i, this.dragImageView.getHeight() / 2 + j, getItemAtPosition(this.dragPosition), this.dragBitmap);
      } 
      stopDrag();
      requestDisallowInterceptTouchEvent(false);
    } 
  }
  
  public void setDragGridViewListener(DragGridViewListener paramDragGridViewListener) {
    this.listener = paramDragGridViewListener;
  }
  
  public void setOnItemClickListener(final MotionEvent ev) {
    setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          public boolean onItemLongClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            int i = (int)ev.getX();
            int j = (int)ev.getY();
            DragGridView.this.dragPosition = param1Int;
            if (DragGridView.this.dragPosition != -1) {
              DragGridView.access$002(DragGridView.this, (ViewGroup)DragGridView.this.getChildAt(DragGridView.this.dragPosition - DragGridView.this.getFirstVisiblePosition()));
              DragGridView.access$102(DragGridView.this, DragGridView.this.windowX - DragGridView.this.dragItemView.getLeft());
              DragGridView.access$202(DragGridView.this, DragGridView.this.windowY - DragGridView.this.dragItemView.getTop());
              DragGridView.this.dragOffsetX = (int)(ev.getRawX() - i);
              DragGridView.this.dragOffsetY = (int)(ev.getRawY() - j);
              DragGridView.this.dragItemView.destroyDrawingCache();
              DragGridView.this.dragItemView.setDrawingCacheEnabled(true);
              DragGridView.access$302(DragGridView.this, Bitmap.createBitmap(DragGridView.this.dragItemView.getDrawingCache()));
              DragGridView.this.mVibrator.vibrate(50L);
              DragGridView.this.startDrag(DragGridView.this.dragBitmap, (int)ev.getRawX(), (int)ev.getRawY());
              DragGridView.this.dragItemView.setVisibility(4);
              DragGridView.access$502(DragGridView.this, true);
              DragGridView.this.lastY = (int)ev.getRawY();
              DragGridView.this.requestDisallowInterceptTouchEvent(true);
              return true;
            } 
            return false;
          }
        });
  }
  
  public void setState(DragState paramDragState) {
    this.mState = paramDragState;
  }
  
  public void startDrag(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    stopDrag();
    this.windowParams = new WindowManager.LayoutParams();
    this.windowParams.gravity = 51;
    this.windowParams.x = paramInt1 - this.win_view_x;
    this.windowParams.y = paramInt2 - this.win_view_y;
    this.windowParams.width = (int)(this.dragScale * paramBitmap.getWidth());
    this.windowParams.height = (int)(this.dragScale * paramBitmap.getHeight());
    this.windowParams.flags = 408;
    this.windowParams.format = -3;
    this.windowParams.windowAnimations = 0;
    ImageView imageView = new ImageView(getContext());
    imageView.setImageBitmap(paramBitmap);
    this.windowManager = (WindowManager)getContext().getSystemService("window");
    this.windowManager.addView((View)imageView, (ViewGroup.LayoutParams)this.windowParams);
    this.dragImageView = (View)imageView;
  }
  
  public static interface DragGridViewListener {
    void onDrop(DragGridView param1DragGridView, int param1Int1, int param1Int2, Object param1Object, Bitmap param1Bitmap);
    
    void onMoving(int param1Int1, int param1Int2, boolean param1Boolean, int param1Int3);
  }
  
  public enum DragState {
    LONG_CLICK_DRAG, NONE, TOUCH_DRAG;
    
    static {
      $VALUES = new DragState[] { NONE, LONG_CLICK_DRAG, TOUCH_DRAG };
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/DragGridView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */