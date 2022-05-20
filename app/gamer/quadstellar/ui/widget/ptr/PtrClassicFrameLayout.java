package app.gamer.quadstellar.ui.widget.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class PtrClassicFrameLayout extends PtrFrameLayout {
  private PtrClassicDefaultHeader mPtrClassicHeader;
  
  public PtrClassicFrameLayout(Context paramContext) {
    super(paramContext);
    initViews();
  }
  
  public PtrClassicFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initViews();
  }
  
  public PtrClassicFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initViews();
  }
  
  private void initViews() {
    this.mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
    setHeaderView((View)this.mPtrClassicHeader);
    addPtrUIHandler(this.mPtrClassicHeader);
  }
  
  public PtrClassicDefaultHeader getHeader() {
    return this.mPtrClassicHeader;
  }
  
  public void setLastUpdateTimeKey(String paramString) {
    if (this.mPtrClassicHeader != null)
      this.mPtrClassicHeader.setLastUpdateTimeKey(paramString); 
  }
  
  public void setLastUpdateTimeRelateObject(Object paramObject) {
    if (this.mPtrClassicHeader != null)
      this.mPtrClassicHeader.setLastUpdateTimeRelateObject(paramObject); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrClassicFrameLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */