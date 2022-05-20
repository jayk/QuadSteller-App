package com.squareup.picasso;

public interface Callback {
  void onError();
  
  void onSuccess();
  
  public static class EmptyCallback implements Callback {
    public void onError() {}
    
    public void onSuccess() {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/Callback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */