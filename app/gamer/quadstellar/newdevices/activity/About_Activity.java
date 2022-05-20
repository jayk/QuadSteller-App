package app.gamer.quadstellar.newdevices.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.ui.widget.RatioLayout;

public class About_Activity extends BaseActivity {
  private static final String TAG = "About_Activity";
  
  private TextView mAboutcontent1;
  
  private TextView mAboutcontent2;
  
  private TextView mback;
  
  private RatioLayout ratioLayout;
  
  protected void init() {
    this.mAboutcontent1 = (TextView)findViewById(2131756535);
    this.mAboutcontent2 = (TextView)findViewById(2131756537);
    this.mback = (TextView)findViewById(2131756399);
    findViewById(2131756536).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.gamerstorm.com/"));
            About_Activity.this.startActivity(intent);
          }
        });
    this.ratioLayout = (RatioLayout)findViewById(2131756534);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(getWindowManager().getDefaultDisplay().getWidth() * 0.5D), -2);
    layoutParams.gravity = 1;
    this.ratioLayout.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.mAboutcontent1.setText(getString(2131296360));
    this.mAboutcontent2.setText(getString(2131296361));
    this.mback.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            About_Activity.this.finish();
          }
        });
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903114);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/activity/About_Activity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */