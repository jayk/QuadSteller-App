package app.gamer.quadstellar.newdevices.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.gamer.quadstellar.ui.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Help_Activity extends BaseActivity {
  @BindView(2131756401)
  Button btHelp;
  
  private TextView mback;
  
  protected void init() {
    this.mback = (TextView)findViewById(2131756399);
    this.mback.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Help_Activity.this.finish();
          }
        });
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903115);
    ButterKnife.bind((Activity)this);
  }
  
  @OnClick({2131756401})
  public void onViewClicked() {
    startActivity(new Intent((Context)this, ScripActivity.class));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/activity/Help_Activity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */