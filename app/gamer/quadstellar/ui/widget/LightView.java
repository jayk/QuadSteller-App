package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import app.gamer.quadstellar.mode.EFDeviceLight;

public class LightView extends ImageView {
  private EFDeviceLight entity;
  
  public LightView(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public LightView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public LightView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void init(Context paramContext) {
    if (isInEditMode());
  }
  
  public EFDeviceLight getEntity() {
    return this.entity;
  }
  
  public void setEntity(EFDeviceLight paramEFDeviceLight) {
    this.entity = paramEFDeviceLight;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/LightView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */