package app.gamer.quadstellar.newdevices.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import app.gamer.quadstellar.ui.BaseActivity;

public class Official_Activity extends BaseActivity {
  private WebView mWeb;
  
  private void initView() {
    WebSettings webSettings = this.mWeb.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setUseWideViewPort(true);
    webSettings.setLoadWithOverviewMode(true);
    webSettings.setBuiltInZoomControls(true);
    webSettings.setSupportZoom(true);
    webSettings.setDisplayZoomControls(false);
    this.mWeb.loadUrl("http://www.deepcool.com/cn");
    this.mWeb.setWebViewClient(new MyNewClient());
  }
  
  protected void init() {
    this.mWeb = (WebView)findViewById(2131756520);
    initView();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903107);
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && this.mWeb.canGoBack()) {
      this.mWeb.goBack();
      return true;
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public class MyNewClient extends WebViewClient {
    public void onPageFinished(WebView param1WebView, String param1String) {
      super.onPageFinished(param1WebView, param1String);
    }
    
    public boolean shouldOverrideUrlLoading(WebView param1WebView, String param1String) {
      param1WebView.loadUrl(param1String);
      return true;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/activity/Official_Activity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */