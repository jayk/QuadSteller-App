package app.gamer.quadstellar.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class SetingDialog extends Dialog {
  private static final String TAG = "SetingDialog";
  
  public SetingDialog(Context paramContext) {
    super(paramContext);
  }
  
  public SetingDialog(Context paramContext, int paramInt) {
    super(paramContext, paramInt);
  }
  
  protected SetingDialog(Context paramContext, boolean paramBoolean, DialogInterface.OnCancelListener paramOnCancelListener) {
    super(paramContext, paramBoolean, paramOnCancelListener);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/SetingDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */