package app.gamer.quadstellar.ui.widget.ptr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import app.gamer.quadstellar.R;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PtrClassicDefaultHeader extends FrameLayout implements PtrUIHandler {
  private static final String KEY_SharedPreferences = "cube_ptr_classic_last_update";
  
  private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  private RotateAnimation mFlipAnimation;
  
  private TextView mLastUpdateTextView;
  
  private long mLastUpdateTime = -1L;
  
  private String mLastUpdateTimeKey;
  
  private LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();
  
  private View mProgressBar;
  
  private RotateAnimation mReverseFlipAnimation;
  
  private int mRotateAniTime = 150;
  
  private View mRotateView;
  
  private boolean mShouldShowLastUpdate;
  
  private TextView mTitleTextView;
  
  public PtrClassicDefaultHeader(Context paramContext) {
    super(paramContext);
    initViews((AttributeSet)null);
  }
  
  public PtrClassicDefaultHeader(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initViews(paramAttributeSet);
  }
  
  public PtrClassicDefaultHeader(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initViews(paramAttributeSet);
  }
  
  private void buildAnimation() {
    this.mFlipAnimation = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
    this.mFlipAnimation.setInterpolator((Interpolator)new LinearInterpolator());
    this.mFlipAnimation.setDuration(this.mRotateAniTime);
    this.mFlipAnimation.setFillAfter(true);
    this.mReverseFlipAnimation = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
    this.mReverseFlipAnimation.setInterpolator((Interpolator)new LinearInterpolator());
    this.mReverseFlipAnimation.setDuration(this.mRotateAniTime);
    this.mReverseFlipAnimation.setFillAfter(true);
  }
  
  private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout paramPtrFrameLayout) {
    this.mTitleTextView.setVisibility(0);
    if (paramPtrFrameLayout.isPullToRefresh()) {
      this.mTitleTextView.setText(getResources().getString(2131296561));
      return;
    } 
    this.mTitleTextView.setText(getResources().getString(2131296560));
  }
  
  private void crossRotateLineFromTopUnderTouch(PtrFrameLayout paramPtrFrameLayout) {
    if (!paramPtrFrameLayout.isPullToRefresh()) {
      this.mTitleTextView.setVisibility(0);
      this.mTitleTextView.setText(2131296564);
    } 
  }
  
  private String getLastUpdateTime() {
    String str1 = null;
    if (this.mLastUpdateTime == -1L && !TextUtils.isEmpty(this.mLastUpdateTimeKey))
      this.mLastUpdateTime = getContext().getSharedPreferences("cube_ptr_classic_last_update", 0).getLong(this.mLastUpdateTimeKey, -1L); 
    if (this.mLastUpdateTime == -1L)
      return str1; 
    long l = (new Date()).getTime() - this.mLastUpdateTime;
    int i = (int)(l / 1000L);
    String str2 = str1;
    if (l >= 0L) {
      str2 = str1;
      if (i > 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getContext().getString(2131296558));
        if (i < 60) {
          stringBuilder.append(i + getContext().getString(2131296565));
        } else {
          i /= 60;
          if (i > 60) {
            i /= 60;
            if (i > 24) {
              Date date = new Date(this.mLastUpdateTime);
              stringBuilder.append(sDataFormat.format(date));
            } else {
              stringBuilder.append(i + getContext().getString(2131296557));
            } 
          } else {
            stringBuilder.append(i + getContext().getString(2131296559));
          } 
        } 
        str2 = stringBuilder.toString();
      } 
    } 
    return str2;
  }
  
  private void hideRotateView() {
    this.mRotateView.clearAnimation();
    this.mRotateView.setVisibility(4);
  }
  
  private void resetView() {
    hideRotateView();
    this.mProgressBar.setVisibility(4);
  }
  
  private void tryUpdateLastUpdateTime() {
    if (TextUtils.isEmpty(this.mLastUpdateTimeKey) || !this.mShouldShowLastUpdate) {
      this.mLastUpdateTextView.setVisibility(8);
      return;
    } 
    String str = getLastUpdateTime();
    if (TextUtils.isEmpty(str)) {
      this.mLastUpdateTextView.setVisibility(8);
      return;
    } 
    this.mLastUpdateTextView.setVisibility(0);
    this.mLastUpdateTextView.setText(str);
  }
  
  protected void initViews(AttributeSet paramAttributeSet) {
    TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.PtrClassicHeader, 0, 0);
    if (typedArray != null)
      this.mRotateAniTime = typedArray.getInt(0, this.mRotateAniTime); 
    buildAnimation();
    View view = LayoutInflater.from(getContext()).inflate(2130903129, (ViewGroup)this);
    this.mRotateView = view.findViewById(2131756619);
    this.mTitleTextView = (TextView)view.findViewById(2131756617);
    this.mLastUpdateTextView = (TextView)view.findViewById(2131756618);
    this.mProgressBar = view.findViewById(2131756620);
    resetView();
  }
  
  public void onUIPositionChange(PtrFrameLayout paramPtrFrameLayout, boolean paramBoolean, byte paramByte, PtrIndicator paramPtrIndicator) {
    int i = paramPtrFrameLayout.getOffsetToRefresh();
    int j = paramPtrIndicator.getCurrentPosY();
    int k = paramPtrIndicator.getLastPosY();
    if (j < i && k >= i) {
      if (paramBoolean && paramByte == 2) {
        crossRotateLineFromBottomUnderTouch(paramPtrFrameLayout);
        if (this.mRotateView != null) {
          this.mRotateView.clearAnimation();
          this.mRotateView.startAnimation((Animation)this.mReverseFlipAnimation);
        } 
      } 
      return;
    } 
    if (j > i && k <= i && paramBoolean && paramByte == 2) {
      crossRotateLineFromTopUnderTouch(paramPtrFrameLayout);
      if (this.mRotateView != null) {
        this.mRotateView.clearAnimation();
        this.mRotateView.startAnimation((Animation)this.mFlipAnimation);
      } 
    } 
  }
  
  public void onUIRefreshBegin(PtrFrameLayout paramPtrFrameLayout) {
    this.mShouldShowLastUpdate = false;
    hideRotateView();
    this.mProgressBar.setVisibility(0);
    this.mTitleTextView.setVisibility(0);
    this.mTitleTextView.setText(2131296563);
    tryUpdateLastUpdateTime();
    this.mLastUpdateTimeUpdater.stop();
  }
  
  public void onUIRefreshComplete(PtrFrameLayout paramPtrFrameLayout) {
    hideRotateView();
    this.mProgressBar.setVisibility(4);
    this.mTitleTextView.setVisibility(0);
    this.mTitleTextView.setText(getResources().getString(2131296562));
    SharedPreferences sharedPreferences = getContext().getSharedPreferences("cube_ptr_classic_last_update", 0);
    if (!TextUtils.isEmpty(this.mLastUpdateTimeKey)) {
      this.mLastUpdateTime = (new Date()).getTime();
      sharedPreferences.edit().putLong(this.mLastUpdateTimeKey, this.mLastUpdateTime).commit();
    } 
  }
  
  public void onUIRefreshPrepare(PtrFrameLayout paramPtrFrameLayout) {
    this.mShouldShowLastUpdate = true;
    tryUpdateLastUpdateTime();
    this.mLastUpdateTimeUpdater.start();
    this.mProgressBar.setVisibility(4);
    this.mRotateView.setVisibility(0);
    this.mTitleTextView.setVisibility(0);
    if (paramPtrFrameLayout.isPullToRefresh()) {
      this.mTitleTextView.setText(getResources().getString(2131296561));
      return;
    } 
    this.mTitleTextView.setText(getResources().getString(2131296560));
  }
  
  public void onUIReset(PtrFrameLayout paramPtrFrameLayout) {
    resetView();
    this.mShouldShowLastUpdate = true;
    tryUpdateLastUpdateTime();
  }
  
  public void setLastUpdateTimeKey(String paramString) {
    if (!TextUtils.isEmpty(paramString))
      this.mLastUpdateTimeKey = paramString; 
  }
  
  public void setLastUpdateTimeRelateObject(Object paramObject) {
    setLastUpdateTimeKey(paramObject.getClass().getName());
  }
  
  public void setRotateAniTime(int paramInt) {
    if (paramInt != this.mRotateAniTime && paramInt != 0) {
      this.mRotateAniTime = paramInt;
      buildAnimation();
    } 
  }
  
  private class LastUpdateTimeUpdater implements Runnable {
    private boolean mRunning = false;
    
    private LastUpdateTimeUpdater() {}
    
    private void start() {
      if (!TextUtils.isEmpty(PtrClassicDefaultHeader.this.mLastUpdateTimeKey)) {
        this.mRunning = true;
        run();
      } 
    }
    
    private void stop() {
      this.mRunning = false;
      PtrClassicDefaultHeader.this.removeCallbacks(this);
    }
    
    public void run() {
      PtrClassicDefaultHeader.this.tryUpdateLastUpdateTime();
      if (this.mRunning)
        PtrClassicDefaultHeader.this.postDelayed(this, 1000L); 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrClassicDefaultHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */