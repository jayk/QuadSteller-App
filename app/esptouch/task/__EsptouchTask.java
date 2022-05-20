package app.esptouch.task;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import app.esptouch.EsptouchResult;
import app.esptouch.IEsptouchListener;
import app.esptouch.IEsptouchResult;
import app.esptouch.protocol.EsptouchGenerator;
import app.esptouch.udp.UDPSocketClient;
import app.esptouch.udp.UDPSocketServer;
import app.esptouch.util.ByteUtil;
import app.esptouch.util.EspNetUtil;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class __EsptouchTask implements __IEsptouchTask {
  private static final int ONE_DATA_LEN = 3;
  
  private static final String TAG = "EsptouchTask";
  
  private final String mApBssid;
  
  private final String mApPassword;
  
  private final String mApSsid;
  
  private volatile Map<String, Integer> mBssidTaskSucCountMap;
  
  private final Context mContext;
  
  private IEsptouchListener mEsptouchListener;
  
  private volatile List<IEsptouchResult> mEsptouchResultList;
  
  private AtomicBoolean mIsCancelled;
  
  private volatile boolean mIsExecuted = false;
  
  private volatile boolean mIsInterrupt = false;
  
  private final boolean mIsSsidHidden;
  
  private volatile boolean mIsSuc = false;
  
  private IEsptouchTaskParameter mParameter;
  
  private final UDPSocketClient mSocketClient;
  
  private final UDPSocketServer mSocketServer;
  
  public __EsptouchTask(String paramString1, String paramString2, String paramString3, Context paramContext, IEsptouchTaskParameter paramIEsptouchTaskParameter, boolean paramBoolean) {
    if (TextUtils.isEmpty(paramString1))
      throw new IllegalArgumentException("the apSsid should be null or empty"); 
    String str = paramString3;
    if (paramString3 == null)
      str = ""; 
    this.mContext = paramContext;
    this.mApSsid = paramString1;
    this.mApBssid = paramString2;
    this.mApPassword = str;
    this.mIsCancelled = new AtomicBoolean(false);
    this.mSocketClient = new UDPSocketClient();
    this.mParameter = paramIEsptouchTaskParameter;
    this.mSocketServer = new UDPSocketServer(this.mParameter.getPortListening(), this.mParameter.getWaitUdpTotalMillisecond(), paramContext);
    this.mIsSsidHidden = paramBoolean;
    this.mEsptouchResultList = new ArrayList<IEsptouchResult>();
    this.mBssidTaskSucCountMap = new HashMap<String, Integer>();
  }
  
  private void __checkTaskValid() {
    if (this.mIsExecuted)
      throw new IllegalStateException("the Esptouch task could be executed only once"); 
    this.mIsExecuted = true;
  }
  
  private boolean __execute(IEsptouchGenerator paramIEsptouchGenerator) {
    long l1 = System.currentTimeMillis();
    long l2 = l1;
    long l3 = l2 - this.mParameter.getTimeoutTotalCodeMillisecond();
    byte[][] arrayOfByte2 = paramIEsptouchGenerator.getGCBytes2();
    byte[][] arrayOfByte1 = paramIEsptouchGenerator.getDCBytes2();
    int i = 0;
    while (true) {
      if (!this.mIsInterrupt) {
        if (l2 - l3 >= this.mParameter.getTimeoutTotalCodeMillisecond()) {
          Log.d("EsptouchTask", "send gc code ");
          while (!this.mIsInterrupt && System.currentTimeMillis() - l2 < this.mParameter.getTimeoutGuideCodeMillisecond()) {
            this.mSocketClient.sendData(arrayOfByte2, this.mParameter.getTargetHostname(), this.mParameter.getTargetPort(), this.mParameter.getIntervalGuideCodeMillisecond());
            if (System.currentTimeMillis() - l1 > this.mParameter.getWaitUdpSendingMillisecond())
              break; 
          } 
          l3 = l2;
        } else {
          this.mSocketClient.sendData(arrayOfByte1, i, 3, this.mParameter.getTargetHostname(), this.mParameter.getTargetPort(), this.mParameter.getIntervalDataCodeMillisecond());
          i = (i + 3) % arrayOfByte1.length;
        } 
        long l = System.currentTimeMillis();
        l2 = l;
        if (l - l1 > this.mParameter.getWaitUdpSendingMillisecond())
          return this.mIsSuc; 
        continue;
      } 
      return this.mIsSuc;
    } 
  }
  
  private List<IEsptouchResult> __getEsptouchResultList() {
    synchronized (this.mEsptouchResultList) {
      if (this.mEsptouchResultList.isEmpty()) {
        EsptouchResult esptouchResult = new EsptouchResult();
        this(false, null, null);
        esptouchResult.setIsCancelled(this.mIsCancelled.get());
        this.mEsptouchResultList.add(esptouchResult);
      } 
      return this.mEsptouchResultList;
    } 
  }
  
  private void __interrupt() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mIsInterrupt : Z
    //   6: ifne -> 34
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield mIsInterrupt : Z
    //   14: aload_0
    //   15: getfield mSocketClient : Lapp/esptouch/udp/UDPSocketClient;
    //   18: invokevirtual interrupt : ()V
    //   21: aload_0
    //   22: getfield mSocketServer : Lapp/esptouch/udp/UDPSocketServer;
    //   25: invokevirtual interrupt : ()V
    //   28: invokestatic currentThread : ()Ljava/lang/Thread;
    //   31: invokevirtual interrupt : ()V
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	37	finally
  }
  
  private void __listenAsyn(final int expectDataLen) {
    (new Thread() {
        public void run() {
          long l = System.currentTimeMillis();
          byte b = (byte)((ByteUtil.getBytesByString(__EsptouchTask.this.mApSsid + __EsptouchTask.this.mApPassword)).length + 9);
          while (true) {
            boolean bool;
            if (__EsptouchTask.this.mEsptouchResultList.size() < __EsptouchTask.this.mParameter.getExpectTaskResultCount() && !__EsptouchTask.this.mIsInterrupt) {
              int i;
              byte[] arrayOfByte = __EsptouchTask.this.mSocketServer.receiveSpecLenBytes(expectDataLen);
              if (arrayOfByte != null) {
                i = arrayOfByte[0];
              } else {
                i = -1;
              } 
              if (i == b) {
                long l1 = System.currentTimeMillis();
                i = (int)(__EsptouchTask.this.mParameter.getWaitUdpTotalMillisecond() - l1 - l);
                if (i >= 0) {
                  __EsptouchTask.this.mSocketServer.setSoTimeout(i);
                  if (arrayOfByte != null) {
                    String str = ByteUtil.parseBssid(arrayOfByte, __EsptouchTask.this.mParameter.getEsptouchResultOneLen(), __EsptouchTask.this.mParameter.getEsptouchResultMacLen());
                    InetAddress inetAddress = EspNetUtil.parseInetAddr(arrayOfByte, __EsptouchTask.this.mParameter.getEsptouchResultOneLen() + __EsptouchTask.this.mParameter.getEsptouchResultMacLen(), __EsptouchTask.this.mParameter.getEsptouchResultIpLen());
                    __EsptouchTask.this.__putEsptouchResult(true, str, inetAddress);
                  } 
                  continue;
                } 
              } else {
                continue;
              } 
            } 
            __EsptouchTask __EsptouchTask1 = __EsptouchTask.this;
            if (__EsptouchTask.this.mEsptouchResultList.size() >= __EsptouchTask.this.mParameter.getExpectTaskResultCount()) {
              bool = true;
            } else {
              bool = false;
            } 
            __EsptouchTask.access$702(__EsptouchTask1, bool);
            __EsptouchTask.this.__interrupt();
            Log.d("EsptouchTask", "__listenAsyn() finish");
            return;
          } 
        }
      }).start();
  }
  
  private void __putEsptouchResult(boolean paramBoolean, String paramString, InetAddress paramInetAddress) {
    synchronized (this.mEsptouchResultList) {
      StringBuilder stringBuilder1;
      boolean bool1;
      Integer integer1 = this.mBssidTaskSucCountMap.get(paramString);
      Integer integer2 = integer1;
      if (integer1 == null)
        integer2 = Integer.valueOf(0); 
      integer2 = Integer.valueOf(integer2.intValue() + 1);
      StringBuilder stringBuilder2 = new StringBuilder();
      this();
      Log.d("EsptouchTask", stringBuilder2.append("__putEsptouchResult(): count = ").append(integer2).toString());
      this.mBssidTaskSucCountMap.put(paramString, integer2);
      if (integer2.intValue() >= this.mParameter.getThresholdSucBroadcastCount()) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (!bool1) {
        stringBuilder1 = new StringBuilder();
        this();
        Log.d("EsptouchTask", stringBuilder1.append("__putEsptouchResult(): count = ").append(integer2).append(", isn't enough").toString());
        return;
      } 
      boolean bool2 = false;
      Iterator<IEsptouchResult> iterator = this.mEsptouchResultList.iterator();
      while (true) {
        bool1 = bool2;
        if (iterator.hasNext()) {
          if (((IEsptouchResult)iterator.next()).getBssid().equals(stringBuilder1)) {
            bool1 = true;
            break;
          } 
          continue;
        } 
        break;
      } 
      if (!bool1) {
        Log.d("EsptouchTask", "__putEsptouchResult(): put one more result");
        EsptouchResult esptouchResult = new EsptouchResult();
        this(paramBoolean, (String)stringBuilder1, paramInetAddress);
        this.mEsptouchResultList.add(esptouchResult);
        if (this.mEsptouchListener != null)
          this.mEsptouchListener.onEsptouchResultAdded((IEsptouchResult)esptouchResult); 
      } 
      return;
    } 
  }
  
  public IEsptouchResult executeForResult() throws RuntimeException {
    return executeForResults(1).get(0);
  }
  
  public List<IEsptouchResult> executeForResults(int paramInt) throws RuntimeException {
    __checkTaskValid();
    this.mParameter.setExpectTaskResultCount(paramInt);
    Log.d("EsptouchTask", "execute()");
    if (Looper.myLooper() == Looper.getMainLooper())
      throw new RuntimeException("Don't call the esptouch Task at Main(UI) thread directly."); 
    InetAddress inetAddress = EspNetUtil.getLocalInetAddress(this.mContext);
    Log.i("EsptouchTask", "localInetAddress: " + inetAddress);
    EsptouchGenerator esptouchGenerator = new EsptouchGenerator(this.mApSsid, this.mApBssid, this.mApPassword, inetAddress, this.mIsSsidHidden);
    __listenAsyn(this.mParameter.getEsptouchResultTotalLen());
    for (paramInt = 0; paramInt < this.mParameter.getTotalRepeatTime(); paramInt++) {
      if (__execute((IEsptouchGenerator)esptouchGenerator))
        return __getEsptouchResultList(); 
    } 
    if (!this.mIsInterrupt) {
      try {
        Thread.sleep(this.mParameter.getWaitUdpReceivingMillisecond());
        __interrupt();
        List<IEsptouchResult> list1 = __getEsptouchResultList();
      } catch (InterruptedException interruptedException) {}
      return (List<IEsptouchResult>)interruptedException;
    } 
    List<IEsptouchResult> list = __getEsptouchResultList();
  }
  
  public void interrupt() {
    Log.d("EsptouchTask", "interrupt()");
    this.mIsCancelled.set(true);
    __interrupt();
  }
  
  public boolean isCancelled() {
    return this.mIsCancelled.get();
  }
  
  public void setEsptouchListener(IEsptouchListener paramIEsptouchListener) {
    this.mEsptouchListener = paramIEsptouchListener;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/task/__EsptouchTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */