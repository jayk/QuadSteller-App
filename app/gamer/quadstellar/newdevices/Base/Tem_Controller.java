package app.gamer.quadstellar.newdevices.Base;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.controler.DataControler;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.RefreshTempDataEvent;
import app.gamer.quadstellar.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.zhy.android.percent.support.PercentRelativeLayout;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Tem_Controller extends TabController implements View.OnClickListener {
  Button controllerTemBtClose;
  
  Button controllerTemBtContent;
  
  Button controllerTemBtFlourOpen;
  
  Button controllerTemBtFlourOpen2;
  
  Button controllerTemBtOpen;
  
  ImageView controllerTemIv;
  
  private boolean isControlAll;
  
  PercentRelativeLayout llLocalControl;
  
  private Context mContext;
  
  View vLightControlFive;
  
  View vLightControlFour;
  
  View vLightControlOne;
  
  View vLightControlThree;
  
  View vLightControlTwo;
  
  public Tem_Controller(Context paramContext, boolean paramBoolean) {
    super(paramContext, paramBoolean);
    boolean bool;
    this.mContext = paramContext;
    StringBuilder stringBuilder = (new StringBuilder()).append("Context为空吗?");
    if (this.mContext == null) {
      bool = true;
    } else {
      bool = false;
    } 
    LogUtil.e("崩溃测试", stringBuilder.append(bool).toString());
    this.isControlAll = paramBoolean;
    EventBus.getDefault().register(this);
    initData();
  }
  
  private void initData() {
    Picasso.with(this.mContext).load(2130838310).into(this.controllerTemIv);
  }
  
  private void showTempText(double paramDouble) {
    double d = 9.0D * paramDouble / 5.0D;
    if (paramDouble < 38.0D && paramDouble >= 36.0D) {
      this.controllerTemBtContent.setBackgroundColor(Color.parseColor("#4169e1"));
    } else if (paramDouble >= 38.0D) {
      this.controllerTemBtContent.setBackgroundColor(-65536);
    } 
    this.controllerTemBtContent.setText(paramDouble + "℃/" + (d + 32.0D) + "f");
  }
  
  protected View initContentView(Context paramContext) {
    View view = View.inflate(paramContext, 2130903128, null);
    this.vLightControlFive = view.findViewById(2131756591);
    this.vLightControlFour = view.findViewById(2131756592);
    this.vLightControlThree = view.findViewById(2131756595);
    this.vLightControlTwo = view.findViewById(2131756593);
    this.vLightControlOne = view.findViewById(2131756594);
    this.controllerTemIv = (ImageView)view.findViewById(2131756609);
    this.llLocalControl = (PercentRelativeLayout)view.findViewById(2131756590);
    this.controllerTemBtContent = (Button)view.findViewById(2131756610);
    this.controllerTemBtOpen = (Button)view.findViewById(2131756614);
    this.controllerTemBtClose = (Button)view.findViewById(2131756615);
    this.controllerTemBtFlourOpen = (Button)view.findViewById(2131756612);
    this.controllerTemBtFlourOpen2 = (Button)view.findViewById(2131756613);
    this.controllerTemBtOpen.setOnClickListener(this);
    this.controllerTemBtClose.setOnClickListener(this);
    this.controllerTemBtFlourOpen.setOnClickListener(this);
    this.controllerTemBtFlourOpen2.setOnClickListener(this);
    StringBuilder stringBuilder = (new StringBuilder()).append("initContentView中Context为空吗?");
    if (this.mContext == null) {
      boolean bool1 = true;
      LogUtil.e("崩溃测试", stringBuilder.append(bool1).toString());
      return view;
    } 
    boolean bool = false;
    LogUtil.e("崩溃测试", stringBuilder.append(bool).toString());
    return view;
  }
  
  public void onClick(View paramView) {
    FatherDeviceInfo fatherDeviceInfo;
    if (this.isControlAll) {
      fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
    } else {
      fatherDeviceInfo = App.getInstance().getCurrentDevice();
    } 
    switch (paramView.getId()) {
      default:
        return;
      case 2131756614:
        fatherDeviceInfo.setDoorToggleState(1);
        FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
        DataControler.getInstance().sendTempData(fatherDeviceInfo, 1, this.isControlAll);
      case 2131756615:
        fatherDeviceInfo.setDoorToggleState(0);
        FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
        DataControler.getInstance().sendTempData(fatherDeviceInfo, 0, this.isControlAll);
      case 2131756612:
        fatherDeviceInfo.setDoorToggleState(0);
        FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
        DataControler.getInstance().sendTempData(fatherDeviceInfo, 2, this.isControlAll);
      case 2131756613:
        break;
    } 
    fatherDeviceInfo.setDoorToggleState(0);
    FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
    DataControler.getInstance().sendTempData(fatherDeviceInfo, 3, this.isControlAll);
  }
  
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(RefreshTempDataEvent paramRefreshTempDataEvent) {
    showTempText(Double.parseDouble(paramRefreshTempDataEvent.getTemp()));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Base/Tem_Controller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */