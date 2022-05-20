package app.gamer.quadstellar.newdevices;

import app.gamer.quadstellar.domain.DeviceOnlineEvent;
import app.gamer.quadstellar.domain.DeviceStateEvent;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.net.DeviceManager;
import app.gamer.quadstellar.utils.ByteUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DeviceOnLineTimeTask {
  private static DeviceOnLineTimeTask mTimeTask;
  
  private TimerTask connectTask;
  
  private String macDress;
  
  private Timer timer;
  
  private DeviceOnLineTimeTask() {
    EventBus.getDefault().register(this);
  }
  
  public static DeviceOnLineTimeTask getInsten() {
    // Byte code:
    //   0: getstatic app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask.mTimeTask : Lapp/gamer/quadstellar/newdevices/DeviceOnLineTimeTask;
    //   3: ifnonnull -> 30
    //   6: ldc app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask
    //   8: monitorenter
    //   9: getstatic app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask.mTimeTask : Lapp/gamer/quadstellar/newdevices/DeviceOnLineTimeTask;
    //   12: ifnonnull -> 27
    //   15: new app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask
    //   18: astore_0
    //   19: aload_0
    //   20: invokespecial <init> : ()V
    //   23: aload_0
    //   24: putstatic app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask.mTimeTask : Lapp/gamer/quadstellar/newdevices/DeviceOnLineTimeTask;
    //   27: ldc app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask
    //   29: monitorexit
    //   30: getstatic app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask.mTimeTask : Lapp/gamer/quadstellar/newdevices/DeviceOnLineTimeTask;
    //   33: areturn
    //   34: astore_0
    //   35: ldc app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask
    //   37: monitorexit
    //   38: aload_0
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   9	27	34	finally
    //   27	30	34	finally
    //   35	38	34	finally
  }
  
  private void initData(final String macDress) {
    if (this.timer == null)
      this.timer = new Timer(); 
    if (this.connectTask == null) {
      this.connectTask = new TimerTask() {
          public void run() {
            ArrayList<ControlDevice> arrayList = DeviceManager.getInstance().getDevices();
            long l = System.currentTimeMillis();
            HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
            while (System.currentTimeMillis() - l < 10000L)
              hashMap.put(macDress, macDress); 
            if (arrayList != null && arrayList.size() > 0)
              for (byte b = 0; b < arrayList.size(); b++) {
                DeviceStateEvent deviceStateEvent = new DeviceStateEvent();
                deviceStateEvent.setPosition(b);
                ControlDevice controlDevice = arrayList.get(b);
                if (!hashMap.containsKey(controlDevice.getMacAddress())) {
                  deviceStateEvent.setOnline(false);
                  if (controlDevice.isOnline())
                    EventBus.getDefault().post(deviceStateEvent); 
                  controlDevice.setOnline(false);
                } else {
                  deviceStateEvent.setOnline(true);
                  if (!controlDevice.isOnline())
                    EventBus.getDefault().post(deviceStateEvent); 
                  controlDevice.setOnline(true);
                } 
              }  
          }
        };
      if (this.timer != null && this.connectTask != null)
        this.timer.schedule(this.connectTask, 0L, 2000L); 
    } 
  }
  
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(DeviceOnlineEvent paramDeviceOnlineEvent) {
    if (paramDeviceOnlineEvent.getDatas() != null)
      this.macDress = ByteUtils.bytesToHexString(ByteUtils.arrayCopyBytes(paramDeviceOnlineEvent.getDatas(), 2, 6)); 
  }
  
  public void startTimeTask() {
    initData(this.macDress);
  }
  
  public void stopTimeTask() {
    if (this.timer != null) {
      this.timer.cancel();
      this.timer = null;
    } 
    if (this.connectTask != null) {
      this.connectTask.cancel();
      this.connectTask = null;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/DeviceOnLineTimeTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */