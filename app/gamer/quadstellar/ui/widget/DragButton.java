package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DragButton extends RelativeLayout implements View.OnTouchListener {
  private static int THRESHOLD = 5;
  
  private static int initX;
  
  private int createScreenLocation;
  
  private TextView createScreenTv;
  
  int downX = 0;
  
  int downY = 0;
  
  private int dragValue = 0;
  
  private boolean isMoving = false;
  
  private Context mContext;
  
  private EdragAxis mDragAxis;
  
  private DragButtonListener mListener;
  
  private TextView mainScreenTv;
  
  private int marginLeft = 0;
  
  private int marginTop = 0;
  
  private int parentWidth = 0;
  
  private ViewGroup root;
  
  private int startX = 0;
  
  private int startY = 0;
  
  private boolean xAxisLocked = false;
  
  private int xDelta;
  
  private boolean yAxisLocked = false;
  
  public DragButton(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    if (!isInEditMode()) {
      this.marginLeft = (int)paramContext.getResources().getDimension(2131362247);
      this.marginTop = (int)paramContext.getResources().getDimension(2131361969);
      initControls();
    } 
  }
  
  private void centerButtonWithAnimation() {
    Animation animation = new Animation() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)DragButton.this.mainScreenTv.getLayoutParams();
        
        int oriX = this.layoutParams.leftMargin;
        
        int oriY = this.layoutParams.topMargin;
        
        protected void applyTransformation(float param1Float, Transformation param1Transformation) {
          RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DragButton.this.mainScreenTv.getLayoutParams());
          layoutParams.leftMargin = this.oriX - (int)((this.oriX - DragButton.this.startX) * param1Float);
          layoutParams.topMargin = this.oriY - (int)((this.oriY - DragButton.this.startY) * param1Float);
          DragButton.this.mainScreenTv.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        }
      };
    animation.setDuration(350L);
    this.mainScreenTv.startAnimation(animation);
  }
  
  private void initControls() {
    this.root = (ViewGroup)((LayoutInflater)this.mContext.getSystemService("layout_inflater")).inflate(2130903186, (ViewGroup)this, true);
    this.mainScreenTv = (TextView)this.root.findViewById(2131756728);
    this.mainScreenTv.setOnTouchListener(this);
    this.createScreenTv = (TextView)this.root.findViewById(2131756729);
    this.mDragAxis = EdragAxis.EdragAxis_XY;
  }
  
  public boolean getMovingState() {
    return this.isMoving;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    this.parentWidth = View.MeasureSpec.getSize(paramInt1);
    this.startX = this.marginLeft;
    this.startY = this.marginTop;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
    int[] arrayOfInt;
    RelativeLayout.LayoutParams layoutParams;
    int i = (int)paramMotionEvent.getRawX();
    this.downX = i;
    this.downY = (int)paramMotionEvent.getRawY();
    switch (paramMotionEvent.getActionMasked()) {
      default:
        return true;
      case 0:
        arrayOfInt = new int[2];
        this.createScreenTv.getLocationInWindow(arrayOfInt);
        this.createScreenLocation = arrayOfInt[0];
        layoutParams = new RelativeLayout.LayoutParams(this.mainScreenTv.getLayoutParams());
        layoutParams.leftMargin = this.startX;
        layoutParams.topMargin = this.startY;
        paramView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.xDelta = i - layoutParams.leftMargin;
        initX = i;
        requestDisallowInterceptTouchEvent(true);
      case 1:
        this.isMoving = false;
        centerButtonWithAnimation();
        this.dragValue = 0;
        if (this.mListener != null && !this.xAxisLocked && !this.yAxisLocked)
          this.mListener.onDragButtonClick(); 
        setDragAxis(this.mDragAxis);
        requestDisallowInterceptTouchEvent(false);
      case 3:
      case 4:
        this.isMoving = false;
        centerButtonWithAnimation();
        this.dragValue = 0;
        setDragAxis(this.mDragAxis);
      case 2:
        break;
    } 
    int j = i - initX;
    this.isMoving = true;
    if (j > 0 && j > THRESHOLD && !this.yAxisLocked) {
      if (!this.xAxisLocked)
        this.xAxisLocked = true; 
      layoutParams = (RelativeLayout.LayoutParams)paramView.getLayoutParams();
      if (layoutParams.leftMargin < this.parentWidth - paramView.getWidth()) {
        layoutParams.leftMargin = i - this.xDelta;
        paramView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        j = (i - initX) / paramView.getWidth() / 2;
        if (i > this.createScreenLocation && this.mListener != null)
          this.mListener.onDragButtonRight(i); 
        if (j > this.dragValue && this.mListener != null)
          this.dragValue++; 
      } 
    } 
    requestDisallowInterceptTouchEvent(true);
  }
  
  public void reloadText() {
    this.mainScreenTv.setText(2131296825);
    this.createScreenTv.setText(2131296554);
    requestLayout();
  }
  
  public void setDragAxis(EdragAxis paramEdragAxis) {
    this.mDragAxis = paramEdragAxis;
    if (this.mDragAxis == EdragAxis.EdragAxis_X) {
      this.xAxisLocked = false;
      return;
    } 
    if (this.mDragAxis == EdragAxis.EdragAxis_Y) {
      this.yAxisLocked = false;
      return;
    } 
    if (this.mDragAxis == EdragAxis.EdragAxis_XY) {
      this.xAxisLocked = false;
      this.yAxisLocked = false;
    } 
  }
  
  public void setOnDragButtonListener(DragButtonListener paramDragButtonListener) {
    this.mListener = paramDragButtonListener;
  }
  
  public static interface DragButtonListener {
    void onDragButtonClick();
    
    void onDragButtonRight(int param1Int);
  }
  
  enum EdragAxis {
    EdragAxis_X, EdragAxis_XY, EdragAxis_Y;
    
    static {
      $VALUES = new EdragAxis[] { EdragAxis_X, EdragAxis_Y, EdragAxis_XY };
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/DragButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */