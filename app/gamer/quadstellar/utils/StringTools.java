package app.gamer.quadstellar.utils;

import android.content.Context;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.mode.EFScene;
import app.gamer.quadstellar.mode.EntityBase;
import app.gamer.quadstellar.mode.dao.DaoUtil;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;

public class StringTools {
  public static String ADDDEVICESID;
  
  public static final String APPKEY = "F11D40D2CD624F8981A096E88512D094";
  
  public static final int COLOR_FIVE = -16711425;
  
  public static final int COLOR_FOUR = -14483712;
  
  public static final int COLOR_ONE = -65536;
  
  public static final int COLOR_SEVEN = -1;
  
  public static final int COLOR_SIX = -2752257;
  
  public static final int COLOR_THREE = -3840;
  
  public static final int COLOR_TWO = -25088;
  
  public static String CONTROLID;
  
  public static int CONTROLLER_COMPOSITE = 0;
  
  public static byte[] DATANUM;
  
  public static String DEVICES_STATE;
  
  public static int GUID = 0;
  
  public static int ISADD = 0;
  
  public static int ISGETDEVICE = 0;
  
  public static final String LIGHTMODE = "lightmodel";
  
  public static final String MATCH = "6D 61 74 63 68 20";
  
  public static final String MUSICDEVICES = "musicdevices";
  
  public static String SCENEPIC;
  
  public static final String SECRETKEY = "EB7D400B047941DEB49DDED0AD63D342";
  
  public static String TOKEN;
  
  public static final String USERDATAS = "userdates";
  
  public static String ZIGBEEMAC = "";
  
  private static Gson gson;
  
  private static ControlDevice mControlDevices;
  
  static {
    DATANUM = null;
    CONTROLLER_COMPOSITE = 0;
    CONTROLID = "controllid";
    ADDDEVICESID = "";
    DEVICES_STATE = "";
    TOKEN = "";
    SCENEPIC = "assets://scenepic/defult.jpg";
  }
  
  public static ControlDevice getControlDevices() {
    return mControlDevices;
  }
  
  public static String objToJson(Object paramObject) {
    return (new Gson()).toJson(paramObject);
  }
  
  public static void setControlDevices(ControlDevice paramControlDevice) {
    mControlDevices = paramControlDevice;
  }
  
  public static void setDefultScene(Context paramContext) {
    EFScene eFScene = new EFScene();
    eFScene.setSceneName(paramContext.getString(2131296825));
    eFScene.setScenePic("assets://scenepic/sceneimage99.jpg");
    DaoUtil.saveOrUpdate((EntityBase)eFScene);
    List<String> list = Arrays.asList(paramContext.getResources().getStringArray(2131689475));
    for (byte b = 0; b < list.size(); b++) {
      EFScene eFScene1 = new EFScene();
      LogUtil.e("names", "---------" + (String)list.get(b));
      eFScene1.setSceneName(list.get(b));
      eFScene1.setScenePic("assets://scenepic/sceneimage" + (b + 100) + ".jpg");
      DaoUtil.saveOrUpdate((EntityBase)eFScene1);
    } 
  }
  
  public static <T> T toEntityFromJson(String paramString, Class<T> paramClass) {
    return (T)(new Gson()).fromJson(new String(paramString), paramClass);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/StringTools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */