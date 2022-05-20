package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import app.gamer.quadstellar.mode.EFDeviceLight;
import java.util.List;

public class AllLightView extends ImageView {
  private int lastColor;
  
  private List<EFDeviceLight> lightList;
  
  public AllLightView(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public AllLightView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public AllLightView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void init(Context paramContext) {
    if (isInEditMode());
  }
  
  public int getLastColor() {
    return this.lastColor;
  }
  
  public List<EFDeviceLight> getLightList() {
    return this.lightList;
  }
  
  public void setLastColor(int paramInt) {
    this.lastColor = paramInt;
  }
  
  public void setLightList(List<EFDeviceLight> paramList) {
    this.lightList = paramList;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/AllLightView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */