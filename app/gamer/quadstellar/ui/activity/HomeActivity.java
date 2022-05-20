package app.gamer.quadstellar.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.observer.Observer;
import app.gamer.quadstellar.observer.Subject;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.ui.fragment.HomeFragment;

public class HomeActivity extends BaseActivity implements Observer {
  private HomeFragment mHomeFragment;
  
  private void initViewPager() {
    if (this.mHomeFragment == null)
      this.mHomeFragment = new HomeFragment(); 
    getSupportFragmentManager().beginTransaction().replace(2131756410, (Fragment)this.mHomeFragment).commit();
  }
  
  protected void init() {
    App.getInstance().getLanguageSubject().attach(this);
    initViewPager();
  }
  
  public void onBackPressed() {
    exit();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903079);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    App.getInstance().getLanguageSubject().detach(this);
    this.mHomeFragment.destroy();
  }
  
  public void update(Subject paramSubject, Object... paramVarArgs) {
    if (paramSubject instanceof app.gamer.quadstellar.observer.LanguageSubject);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/activity/HomeActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */