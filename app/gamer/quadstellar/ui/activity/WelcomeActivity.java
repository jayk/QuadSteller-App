package app.gamer.quadstellar.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.newdevices.activity.ScripActivity;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

public class WelcomeActivity extends BaseActivity {
  int count = 5;
  
  @SuppressLint({"HandlerLeak"})
  Handler handler = new Handler() {
      public void handleMessage(Message param1Message) {
        super.handleMessage(param1Message);
        if (param1Message.what == 0) {
          WelcomeActivity.this.textView.setText(WelcomeActivity.this.getString(2131296311) + "\n" + WelcomeActivity.this.count + "s");
          WelcomeActivity welcomeActivity = WelcomeActivity.this;
          welcomeActivity.count--;
          WelcomeActivity.this.handler.sendEmptyMessageDelayed(0, 1000L);
          if (WelcomeActivity.this.count == 0) {
            if (!PreferenceHelper.readBoolean("box_fist_install", false)) {
              WelcomeActivity.this.skipActivity((Activity)WelcomeActivity.this, ScripActivity.class);
              PreferenceHelper.write("box_fist_install", true);
              return;
            } 
          } else {
            return;
          } 
        } else {
          return;
        } 
        WelcomeActivity.this.skipActivity((Activity)WelcomeActivity.this, HomeActivity.class);
      }
    };
  
  private ImageView imageView;
  
  private RelativeLayout relativeLayout;
  
  private TextView textView;
  
  protected void init() {
    Log.d("WelcomeActivity", App.uniqueId);
    PreferenceHelper.write("user_phone", App.uniqueId);
    this.textView = (TextView)findViewById(2131756523);
    this.relativeLayout = (RelativeLayout)findViewById(2131756522);
    this.imageView = (ImageView)findViewById(2131756521);
    Picasso.with((Context)this).load("http://www.gamerstorm.com/apploading/images/OT159.jpg").placeholder((Drawable)new ColorDrawable(Color.parseColor("#000000"))).error((Drawable)new ColorDrawable(Color.parseColor("#000000"))).into(this.imageView);
    this.handler.sendEmptyMessage(0);
    this.relativeLayout.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (!PreferenceHelper.readBoolean("box_fist_install", false)) {
              WelcomeActivity.this.skipActivity((Activity)WelcomeActivity.this, ScripActivity.class);
              PreferenceHelper.write("box_fist_install", true);
              return;
            } 
            WelcomeActivity.this.skipActivity((Activity)WelcomeActivity.this, HomeActivity.class);
          }
        });
  }
  
  public void onCreate(Bundle paramBundle) {
    setContentView(2130903108);
    super.onCreate(paramBundle);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    this.handler.removeCallbacksAndMessages(null);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/activity/WelcomeActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */