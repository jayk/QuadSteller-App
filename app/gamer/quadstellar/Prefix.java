package app.gamer.quadstellar;

public class Prefix {
  public static String ALL_FF;
  
  public static final byte[] ALL_MAC;
  
  public static byte[] BIND_DEVICES = new byte[] { 1, 98, 105, 110, 100, 17 };
  
  public static byte[] BROACAST_DATA;
  
  public static byte[] CHANGE_COLOR_DEVICES;
  
  public static final byte[] COLOR_OFF;
  
  public static final byte[] COLOR_ON;
  
  public static byte[] CONTROLLER_FAN;
  
  public static byte[] CONTROL_TYPE;
  
  public static byte[] CTL_WIFI_LIGHT;
  
  public static byte[] CTL_ZIG_OUTLET;
  
  public static byte[] DEL_ZIG_OUTLET;
  
  public static byte[] GET_WIFI_LIGHT;
  
  public static final byte[] HEART_DATA = new byte[] { -16, 0, 100 };
  
  public static byte[] OC_TYPE;
  
  public static final byte[] RESTORE_FACTORY;
  
  public static byte[] SCAN_WIFI_DEVICE;
  
  public static byte[] SCAN_WIFI_DEVICE_1;
  
  public static byte[] TMR_ZIG_OUTLET;
  
  public static byte[] WIFI_FAN;
  
  static {
    ALL_MAC = new byte[] { -1, -1, -1, -1, -1, -1 };
    CHANGE_COLOR_DEVICES = new byte[] { 2, 99, 116, 108, 17 };
    BROACAST_DATA = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1 };
    CTL_ZIG_OUTLET = CHANGE_COLOR_DEVICES;
    TMR_ZIG_OUTLET = new byte[] { 15, 116, 109, 114 };
    DEL_ZIG_OUTLET = new byte[] { 17, 100, 101, 108 };
    SCAN_WIFI_DEVICE = new byte[] { 1, 119, 98, 105, 110, 100 };
    SCAN_WIFI_DEVICE_1 = new byte[] { 1, 4, 98, 105, 110, 100 };
    GET_WIFI_LIGHT = new byte[] { 12, 117, 112, 100, 97, 116, 101, 17 };
    CONTROLLER_FAN = new byte[] { 13, 102, 97, 110, 17 };
    CTL_WIFI_LIGHT = new byte[] { 2, 99, 116, 108, 17 };
    CONTROL_TYPE = new byte[] { 2, 0, 100 };
    OC_TYPE = new byte[] { 12, 0, 100 };
    WIFI_FAN = new byte[] { 6, 102, 119, 117, 112, 17 };
    ALL_FF = "ffffffffffffffffffffffffffffffff";
    COLOR_ON = new byte[] { 0, 0, 0 };
    COLOR_OFF = new byte[] { -1, -1, -1 };
    RESTORE_FACTORY = new byte[] { 15, 0, 100 };
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/Prefix.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */