package app.gamer.quadstellar;

public class Config {
  public static final int ADD_TIMER = 1997;
  
  public static final String ALARM = "alarm_";
  
  public static final String APP_ID = "appid_";
  
  public static String APP_Updata_Url;
  
  public static final String AUTH_KEY = "authkey_";
  
  public static final String BACKGROUND_PATH = "bgground_path";
  
  public static final String BROADCAST_CLOUD_DISCONNECT;
  
  public static final String BROADCAST_DEVICE_CHANGED;
  
  public static final String BROADCAST_DEVICE_SYNC;
  
  public static final String BROADCAST_EXIT;
  
  public static final String BROADCAST_LOCAL_DISCONNECT;
  
  public static final String BROADCAST_ON_LOGIN;
  
  public static final String BROADCAST_ON_START;
  
  public static final String BROADCAST_RECVPIPE;
  
  public static final String BROADCAST_RECVPIPE_OC;
  
  public static final String BROADCAST_RECVPIPE_SCAN;
  
  public static final String BROADCAST_SOCKET_STATUS;
  
  public static final String BROADCAST_TIMER_UPDATE;
  
  public static final String CONFIG_FILE_NAME = "config_prefences";
  
  public static String Check_APP_Updata_Version_Url;
  
  public static final String DATA = "data";
  
  public static final String DBNAME = "intelligent.db";
  
  public static final boolean DEBUG = true;
  
  public static final int DELETE_TIMER = 1999;
  
  public static final String DEVICE_MAC = "device-mac";
  
  public static final String EHOME_PREFRENCE = "ehome_prefernce";
  
  public static final int FIFTY_LENGTH = 50;
  
  public static final int FORTY_LENGTH = 50;
  
  public static final String HERAT_TIME = "box_heart_time";
  
  public static final String ISFIST = "app.gamer.quadstellar_isFist";
  
  public static final String KEY = "key";
  
  public static final String KEY_COMPOSITE_DEVICE = "key_composite_device";
  
  public static final String KEY_POWER_STRIP = "key_wifi_power_strip";
  
  public static final String KEY_TELE_DEVICE = "key_tele_device";
  
  public static final String KEY_WIFI_LIGHT = "key_wifi_light";
  
  public static final String KEY_WIFI_OUTLET = "key_wifi_outlet";
  
  public static final String LAMP_BACKGROUND_PATH = "lamp_bgground_path_";
  
  public static final String LANGUAGE = "language";
  
  public static final String LINAKEMODE_INIT = "LinakeMode_init";
  
  public static final String LOGIN_PREFRENCE = "login_prefernce";
  
  public static final String MAIN_BACKGROUND_PATH = "main_bgground_path";
  
  public static final String MODEL = "model_";
  
  public static final int MODIFY_TIMER = 1998;
  
  public static final int ONE_HUNDRED_LENGTH = 100;
  
  public static final String PACKAGE_NAME;
  
  public static final String PREF_PWD = "pref_pwd";
  
  public static final String PREF_USERNAME = "pref_username";
  
  public static final String RECENT_NORMAL_DEVICE = "recent_normal_device";
  
  public static final String RECENT_TELE_DEVICE = "recent_tele_device";
  
  public static final String REQUEST_CODE_KEY = "request_code_key";
  
  public static final String SCENE_TIMER = "scene_timer_";
  
  public static final String SMS_APPKEY = "de495571a6ae";
  
  public static final String SMS_APPSECRET = "f5798f6721bc70055098dbf92318310c";
  
  public static final boolean SQL_DEBUG = true;
  
  public static final String STATUS = "status";
  
  public static final String SUB_BACKGROUND_PATH = "sub_bgground_path";
  
  public static final String TEMPORARILY_SAVE_LIGHT_MSG = "light_msg";
  
  public static final String TIMER = "timer_";
  
  public static final String TYPE = "type";
  
  public static final String USER_PHONE = "user_phone";
  
  public static final String USER_PWD = "user_pwd";
  
  public static final String VERSION_APK = "http://www.lingansmart.cn/APP_VER/eFamily/Android/IntelligentLight.apk";
  
  public static final String VERSION_APP = "http://www.lingansmart.cn/APP_VER/eFamily/Android/version.txt";
  
  public static final String VERSION_URL_1 = "http://www.lingansmart.cn/WM_fwup/SWA1/version.txt";
  
  public static final String VERSION_URL_2 = "http://www.lingansmart.cn/WM_fwup/SWA2/version.txt";
  
  public static final String VERSION_URL_3 = "http://www.lingansmart.cn/WM_fwup/UCC10/version.txt";
  
  public static final String VERSION_URL_4 = "http://www.lingansmart.cn/WM_fwup/UCC11/version.txt";
  
  public static final String VERSION_URL_5 = "http://www.lingansmart.cn/WM_fwup/UCC12/version.txt";
  
  public static final String VERSION_URL_6 = "http://www.lingansmart.cn/WM_fwup/LWE3/version.txt";
  
  public static final String VERSION_URL_7 = "http://www.lingansmart.cn/ESP/FAN/version.txt";
  
  public static String WIFI_PRODUCTID = "5471177bba474be2a92b4eef51cbc9cc";
  
  public static String WIFI_PRODUCTID_LEXIN;
  
  public static String ZIG_PRODUCTID = "bd82ce55a3894ae38761fab3bfcaf94d";
  
  public static final String account = "0000";
  
  public static final String passwrod = "8888";
  
  static {
    WIFI_PRODUCTID_LEXIN = "160fa2afb49eaa00160fa2afb49eaa01";
    PACKAGE_NAME = App.getInstance().getPackageName();
    BROADCAST_ON_START = PACKAGE_NAME + ".onStart";
    BROADCAST_ON_LOGIN = PACKAGE_NAME + ".xlinkonLogin";
    BROADCAST_CLOUD_DISCONNECT = PACKAGE_NAME + ".clouddisconnect";
    BROADCAST_LOCAL_DISCONNECT = PACKAGE_NAME + ".localdisconnect";
    BROADCAST_RECVPIPE = PACKAGE_NAME + ".recv-pipe";
    BROADCAST_RECVPIPE_OC = PACKAGE_NAME + ".recv-pipe-0c";
    BROADCAST_RECVPIPE_SCAN = PACKAGE_NAME + ".recv-pipe-scan";
    BROADCAST_DEVICE_CHANGED = PACKAGE_NAME + ".device-changed";
    BROADCAST_DEVICE_SYNC = PACKAGE_NAME + ".device-sync";
    BROADCAST_EXIT = PACKAGE_NAME + ".exit";
    BROADCAST_TIMER_UPDATE = PACKAGE_NAME + "timer-update";
    BROADCAST_SOCKET_STATUS = PACKAGE_NAME + "socket-status";
    Check_APP_Updata_Version_Url = "http://www.gamerstorm.com/apploading/app/Quadstellar.txt";
    APP_Updata_Url = "http://www.gamerstorm.com/apploading/app/Quadstellar.apk";
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/Config.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */