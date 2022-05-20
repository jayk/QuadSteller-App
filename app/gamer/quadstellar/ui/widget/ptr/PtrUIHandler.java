package app.gamer.quadstellar.ui.widget.ptr;

public interface PtrUIHandler {
  void onUIPositionChange(PtrFrameLayout paramPtrFrameLayout, boolean paramBoolean, byte paramByte, PtrIndicator paramPtrIndicator);
  
  void onUIRefreshBegin(PtrFrameLayout paramPtrFrameLayout);
  
  void onUIRefreshComplete(PtrFrameLayout paramPtrFrameLayout);
  
  void onUIRefreshPrepare(PtrFrameLayout paramPtrFrameLayout);
  
  void onUIReset(PtrFrameLayout paramPtrFrameLayout);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrUIHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */