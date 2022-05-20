package app.esptouch.task;

public interface IEsptouchTaskParameter {
  int getEsptouchResultIpLen();
  
  int getEsptouchResultMacLen();
  
  int getEsptouchResultOneLen();
  
  int getEsptouchResultTotalLen();
  
  int getExpectTaskResultCount();
  
  long getIntervalDataCodeMillisecond();
  
  long getIntervalGuideCodeMillisecond();
  
  int getPortListening();
  
  String getTargetHostname();
  
  int getTargetPort();
  
  int getThresholdSucBroadcastCount();
  
  long getTimeoutDataCodeMillisecond();
  
  long getTimeoutGuideCodeMillisecond();
  
  long getTimeoutTotalCodeMillisecond();
  
  int getTotalRepeatTime();
  
  int getWaitUdpReceivingMillisecond();
  
  int getWaitUdpSendingMillisecond();
  
  int getWaitUdpTotalMillisecond();
  
  void setExpectTaskResultCount(int paramInt);
  
  void setWaitUdpTotalMillisecond(int paramInt);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/task/IEsptouchTaskParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */