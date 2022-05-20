package okhttp3;

import java.net.InetAddress;
import java.util.List;

abstract class EventListener {
  public static final EventListener NONE = new EventListener() {
    
    };
  
  static Factory factory(final EventListener listener) {
    return new Factory() {
        public EventListener create(Call param1Call) {
          return listener;
        }
      };
  }
  
  public void connectEnd(Call paramCall, InetAddress paramInetAddress, int paramInt, String paramString, Throwable paramThrowable) {}
  
  public void connectStart(Call paramCall, InetAddress paramInetAddress, int paramInt) {}
  
  public void dnsEnd(Call paramCall, String paramString, List<InetAddress> paramList, Throwable paramThrowable) {}
  
  public void dnsStart(Call paramCall, String paramString) {}
  
  public void fetchEnd(Call paramCall, Throwable paramThrowable) {}
  
  public void fetchStart(Call paramCall) {}
  
  public void requestBodyEnd(Call paramCall, Throwable paramThrowable) {}
  
  public void requestBodyStart(Call paramCall) {}
  
  public void requestHeadersEnd(Call paramCall, Throwable paramThrowable) {}
  
  public void requestHeadersStart(Call paramCall) {}
  
  public void responseBodyEnd(Call paramCall, Throwable paramThrowable) {}
  
  public void responseBodyStart(Call paramCall) {}
  
  public void responseHeadersEnd(Call paramCall, Throwable paramThrowable) {}
  
  public void responseHeadersStart(Call paramCall) {}
  
  public void secureConnectEnd(Call paramCall, Handshake paramHandshake, Throwable paramThrowable) {}
  
  public void secureConnectStart(Call paramCall) {}
  
  public static interface Factory {
    EventListener create(Call param1Call);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/EventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */