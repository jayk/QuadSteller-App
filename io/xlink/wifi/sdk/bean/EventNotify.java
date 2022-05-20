package io.xlink.wifi.sdk.bean;

import java.io.Serializable;
import java.util.Arrays;

public class EventNotify implements Serializable {
  public int formId;
  
  public int messageId;
  
  public int messageType;
  
  public byte[] notifyData;
  
  public byte notyfyFlags;
  
  public String toString() {
    return "EventNotify{notyfyFlags=" + this.notyfyFlags + ", formId=" + this.formId + ", messageId=" + this.messageId + ", messageType=" + this.messageType + ", notifyData=" + Arrays.toString(this.notifyData) + '}';
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/bean/EventNotify.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */