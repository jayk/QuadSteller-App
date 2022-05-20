package app.esptouch.task;

public class EsptouchTaskParameter implements IEsptouchTaskParameter {
  private static int _datagramCount = 0;
  
  private int mEsptouchResultIpLen = 4;
  
  private int mEsptouchResultMacLen = 6;
  
  private int mEsptouchResultOneLen = 1;
  
  private int mEsptouchResultTotalLen = 11;
  
  private int mExpectTaskResultCount = 1;
  
  private long mIntervalDataCodeMillisecond = 10L;
  
  private long mIntervalGuideCodeMillisecond = 10L;
  
  private int mPortListening = 18266;
  
  private int mTargetPort = 7001;
  
  private int mThresholdSucBroadcastCount = 1;
  
  private long mTimeoutDataCodeMillisecond = 4000L;
  
  private long mTimeoutGuideCodeMillisecond = 2000L;
  
  private int mTotalRepeatTime = 1;
  
  private int mWaitUdpReceivingMilliseond = 15000;
  
  private int mWaitUdpSendingMillisecond = 45000;
  
  private static int __getNextDatagramCount() {
    int i = _datagramCount;
    _datagramCount = i + 1;
    return i % 100 + 1;
  }
  
  public int getEsptouchResultIpLen() {
    return this.mEsptouchResultIpLen;
  }
  
  public int getEsptouchResultMacLen() {
    return this.mEsptouchResultMacLen;
  }
  
  public int getEsptouchResultOneLen() {
    return this.mEsptouchResultOneLen;
  }
  
  public int getEsptouchResultTotalLen() {
    return this.mEsptouchResultTotalLen;
  }
  
  public int getExpectTaskResultCount() {
    return this.mExpectTaskResultCount;
  }
  
  public long getIntervalDataCodeMillisecond() {
    return this.mIntervalDataCodeMillisecond;
  }
  
  public long getIntervalGuideCodeMillisecond() {
    return this.mIntervalGuideCodeMillisecond;
  }
  
  public int getPortListening() {
    return this.mPortListening;
  }
  
  public String getTargetHostname() {
    int i = __getNextDatagramCount();
    return "234." + i + "." + i + "." + i;
  }
  
  public int getTargetPort() {
    return this.mTargetPort;
  }
  
  public int getThresholdSucBroadcastCount() {
    return this.mThresholdSucBroadcastCount;
  }
  
  public long getTimeoutDataCodeMillisecond() {
    return this.mTimeoutDataCodeMillisecond;
  }
  
  public long getTimeoutGuideCodeMillisecond() {
    return this.mTimeoutGuideCodeMillisecond;
  }
  
  public long getTimeoutTotalCodeMillisecond() {
    return this.mTimeoutGuideCodeMillisecond + this.mTimeoutDataCodeMillisecond;
  }
  
  public int getTotalRepeatTime() {
    return this.mTotalRepeatTime;
  }
  
  public int getWaitUdpReceivingMillisecond() {
    return this.mWaitUdpReceivingMilliseond;
  }
  
  public int getWaitUdpSendingMillisecond() {
    return this.mWaitUdpSendingMillisecond;
  }
  
  public int getWaitUdpTotalMillisecond() {
    return this.mWaitUdpReceivingMilliseond + this.mWaitUdpSendingMillisecond;
  }
  
  public void setExpectTaskResultCount(int paramInt) {
    this.mExpectTaskResultCount = paramInt;
  }
  
  public void setWaitUdpTotalMillisecond(int paramInt) {
    if (paramInt < this.mWaitUdpReceivingMilliseond + getTimeoutTotalCodeMillisecond())
      throw new IllegalArgumentException("waitUdpTotalMillisecod is invalid, it is less than mWaitUdpReceivingMilliseond + getTimeoutTotalCodeMillisecond()"); 
    this.mWaitUdpSendingMillisecond = paramInt - this.mWaitUdpReceivingMilliseond;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/task/EsptouchTaskParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */