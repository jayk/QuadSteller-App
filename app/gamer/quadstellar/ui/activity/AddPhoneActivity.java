package app.gamer.quadstellar.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.utils.StringUtils;
import app.gamer.quadstellar.utils.XlinkUtils;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

public class AddPhoneActivity extends BaseActivity {
  @ViewInject(2131756362)
  private EditText addPhoneEt;
  
  @Event({2131756359})
  private void backOnClick(View paramView) {
    finish();
  }
  
  @Event({2131756363})
  private void finishAddPhoneOnClick(View paramView) {
    String str = this.addPhoneEt.getText().toString().trim();
    if (StringUtils.isEmpty(str)) {
      XlinkUtils.shortTips(getString(2131296654));
      return;
    } 
    Intent intent = new Intent();
    intent.putExtra("phonenumber", str.trim());
    setResult(-1, intent);
    finish();
  }
  
  protected void init() {}
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903071);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/activity/AddPhoneActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */