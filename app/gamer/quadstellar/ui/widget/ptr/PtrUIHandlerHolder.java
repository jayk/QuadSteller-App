package app.gamer.quadstellar.ui.widget.ptr;

class PtrUIHandlerHolder implements PtrUIHandler {
  private PtrUIHandler mHandler;
  
  private PtrUIHandlerHolder mNext;
  
  public static void addHandler(PtrUIHandlerHolder paramPtrUIHandlerHolder, PtrUIHandler paramPtrUIHandler) {
    if (paramPtrUIHandler != null && paramPtrUIHandlerHolder != null) {
      if (paramPtrUIHandlerHolder.mHandler == null) {
        paramPtrUIHandlerHolder.mHandler = paramPtrUIHandler;
        return;
      } 
      while (true) {
        if (!paramPtrUIHandlerHolder.contains(paramPtrUIHandler)) {
          if (paramPtrUIHandlerHolder.mNext == null) {
            PtrUIHandlerHolder ptrUIHandlerHolder = new PtrUIHandlerHolder();
            ptrUIHandlerHolder.mHandler = paramPtrUIHandler;
            paramPtrUIHandlerHolder.mNext = ptrUIHandlerHolder;
            return;
          } 
          paramPtrUIHandlerHolder = paramPtrUIHandlerHolder.mNext;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private boolean contains(PtrUIHandler paramPtrUIHandler) {
    return (this.mHandler != null && this.mHandler == paramPtrUIHandler);
  }
  
  public static PtrUIHandlerHolder create() {
    return new PtrUIHandlerHolder();
  }
  
  private PtrUIHandler getHandler() {
    return this.mHandler;
  }
  
  public static PtrUIHandlerHolder removeHandler(PtrUIHandlerHolder paramPtrUIHandlerHolder, PtrUIHandler paramPtrUIHandler) {
    PtrUIHandlerHolder ptrUIHandlerHolder1 = paramPtrUIHandlerHolder;
    if (paramPtrUIHandlerHolder != null) {
      ptrUIHandlerHolder1 = paramPtrUIHandlerHolder;
      if (paramPtrUIHandler != null) {
        if (paramPtrUIHandlerHolder.mHandler == null)
          return paramPtrUIHandlerHolder; 
      } else {
        return ptrUIHandlerHolder1;
      } 
    } else {
      return ptrUIHandlerHolder1;
    } 
    ptrUIHandlerHolder1 = paramPtrUIHandlerHolder;
    PtrUIHandlerHolder ptrUIHandlerHolder2 = null;
    PtrUIHandlerHolder ptrUIHandlerHolder3 = paramPtrUIHandlerHolder;
    while (true) {
      PtrUIHandlerHolder ptrUIHandlerHolder;
      if (ptrUIHandlerHolder1.contains(paramPtrUIHandler)) {
        if (ptrUIHandlerHolder2 == null) {
          ptrUIHandlerHolder = ptrUIHandlerHolder1.mNext;
          ptrUIHandlerHolder1.mNext = null;
          paramPtrUIHandlerHolder = ptrUIHandlerHolder;
        } else {
          ptrUIHandlerHolder2.mNext = ptrUIHandlerHolder1.mNext;
          ptrUIHandlerHolder1.mNext = null;
          paramPtrUIHandlerHolder = ptrUIHandlerHolder2.mNext;
          ptrUIHandlerHolder = ptrUIHandlerHolder3;
        } 
      } else {
        ptrUIHandlerHolder2 = ptrUIHandlerHolder1;
        paramPtrUIHandlerHolder = ptrUIHandlerHolder1.mNext;
        ptrUIHandlerHolder = ptrUIHandlerHolder3;
      } 
      ptrUIHandlerHolder1 = paramPtrUIHandlerHolder;
      ptrUIHandlerHolder3 = ptrUIHandlerHolder;
      if (paramPtrUIHandlerHolder == null) {
        ptrUIHandlerHolder1 = ptrUIHandlerHolder;
        if (ptrUIHandlerHolder == null)
          ptrUIHandlerHolder1 = new PtrUIHandlerHolder(); 
        return ptrUIHandlerHolder1;
      } 
    } 
  }
  
  public boolean hasHandler() {
    return (this.mHandler != null);
  }
  
  public void onUIPositionChange(PtrFrameLayout paramPtrFrameLayout, boolean paramBoolean, byte paramByte, PtrIndicator paramPtrIndicator) {
    PtrUIHandler ptrUIHandler;
    PtrUIHandlerHolder ptrUIHandlerHolder = this;
    do {
      ptrUIHandler = ptrUIHandlerHolder.getHandler();
      if (ptrUIHandler != null)
        ptrUIHandler.onUIPositionChange(paramPtrFrameLayout, paramBoolean, paramByte, paramPtrIndicator); 
      ptrUIHandler = ptrUIHandlerHolder.mNext;
      PtrUIHandler ptrUIHandler1 = ptrUIHandler;
    } while (ptrUIHandler != null);
  }
  
  public void onUIRefreshBegin(PtrFrameLayout paramPtrFrameLayout) {
    PtrUIHandler ptrUIHandler;
    PtrUIHandlerHolder ptrUIHandlerHolder = this;
    do {
      ptrUIHandler = ptrUIHandlerHolder.getHandler();
      if (ptrUIHandler != null)
        ptrUIHandler.onUIRefreshBegin(paramPtrFrameLayout); 
      ptrUIHandler = ptrUIHandlerHolder.mNext;
      PtrUIHandler ptrUIHandler1 = ptrUIHandler;
    } while (ptrUIHandler != null);
  }
  
  public void onUIRefreshComplete(PtrFrameLayout paramPtrFrameLayout) {
    PtrUIHandler ptrUIHandler;
    PtrUIHandlerHolder ptrUIHandlerHolder = this;
    do {
      ptrUIHandler = ptrUIHandlerHolder.getHandler();
      if (ptrUIHandler != null)
        ptrUIHandler.onUIRefreshComplete(paramPtrFrameLayout); 
      ptrUIHandler = ptrUIHandlerHolder.mNext;
      PtrUIHandler ptrUIHandler1 = ptrUIHandler;
    } while (ptrUIHandler != null);
  }
  
  public void onUIRefreshPrepare(PtrFrameLayout paramPtrFrameLayout) {
    if (!hasHandler())
      return; 
    PtrUIHandlerHolder ptrUIHandlerHolder = this;
    while (true) {
      PtrUIHandler ptrUIHandler2 = ptrUIHandlerHolder.getHandler();
      if (ptrUIHandler2 != null)
        ptrUIHandler2.onUIRefreshPrepare(paramPtrFrameLayout); 
      ptrUIHandler2 = ptrUIHandlerHolder.mNext;
      PtrUIHandler ptrUIHandler1 = ptrUIHandler2;
      if (ptrUIHandler2 == null)
        return; 
    } 
  }
  
  public void onUIReset(PtrFrameLayout paramPtrFrameLayout) {
    PtrUIHandler ptrUIHandler;
    PtrUIHandlerHolder ptrUIHandlerHolder = this;
    do {
      ptrUIHandler = ptrUIHandlerHolder.getHandler();
      if (ptrUIHandler != null)
        ptrUIHandler.onUIReset(paramPtrFrameLayout); 
      ptrUIHandler = ptrUIHandlerHolder.mNext;
      PtrUIHandler ptrUIHandler1 = ptrUIHandler;
    } while (ptrUIHandler != null);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrUIHandlerHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */