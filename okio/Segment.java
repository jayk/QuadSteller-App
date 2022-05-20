package okio;

import javax.annotation.Nullable;

final class Segment {
  static final int SHARE_MINIMUM = 1024;
  
  static final int SIZE = 8192;
  
  final byte[] data = new byte[8192];
  
  int limit;
  
  Segment next;
  
  boolean owner;
  
  int pos;
  
  Segment prev;
  
  boolean shared;
  
  Segment() {
    this.owner = true;
    this.shared = false;
  }
  
  Segment(Segment paramSegment) {
    this(paramSegment.data, paramSegment.pos, paramSegment.limit);
    paramSegment.shared = true;
  }
  
  Segment(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.pos = paramInt1;
    this.limit = paramInt2;
    this.owner = false;
    this.shared = true;
  }
  
  public void compact() {
    if (this.prev == this)
      throw new IllegalStateException(); 
    if (this.prev.owner) {
      int k;
      int i = this.limit - this.pos;
      int j = this.prev.limit;
      if (this.prev.shared) {
        k = 0;
      } else {
        k = this.prev.pos;
      } 
      if (i <= 8192 - j + k) {
        writeTo(this.prev, i);
        pop();
        SegmentPool.recycle(this);
      } 
    } 
  }
  
  @Nullable
  public Segment pop() {
    if (this.next != this) {
      Segment segment1 = this.next;
      this.prev.next = this.next;
      this.next.prev = this.prev;
      this.next = null;
      this.prev = null;
      return segment1;
    } 
    Segment segment = null;
    this.prev.next = this.next;
    this.next.prev = this.prev;
    this.next = null;
    this.prev = null;
    return segment;
  }
  
  public Segment push(Segment paramSegment) {
    paramSegment.prev = this;
    paramSegment.next = this.next;
    this.next.prev = paramSegment;
    this.next = paramSegment;
    return paramSegment;
  }
  
  public Segment split(int paramInt) {
    if (paramInt <= 0 || paramInt > this.limit - this.pos)
      throw new IllegalArgumentException(); 
    if (paramInt >= 1024) {
      Segment segment1 = new Segment(this);
      segment1.limit = segment1.pos + paramInt;
      this.pos += paramInt;
      this.prev.push(segment1);
      return segment1;
    } 
    Segment segment = SegmentPool.take();
    System.arraycopy(this.data, this.pos, segment.data, 0, paramInt);
    segment.limit = segment.pos + paramInt;
    this.pos += paramInt;
    this.prev.push(segment);
    return segment;
  }
  
  public void writeTo(Segment paramSegment, int paramInt) {
    if (!paramSegment.owner)
      throw new IllegalArgumentException(); 
    if (paramSegment.limit + paramInt > 8192) {
      if (paramSegment.shared)
        throw new IllegalArgumentException(); 
      if (paramSegment.limit + paramInt - paramSegment.pos > 8192)
        throw new IllegalArgumentException(); 
      System.arraycopy(paramSegment.data, paramSegment.pos, paramSegment.data, 0, paramSegment.limit - paramSegment.pos);
      paramSegment.limit -= paramSegment.pos;
      paramSegment.pos = 0;
    } 
    System.arraycopy(this.data, this.pos, paramSegment.data, paramSegment.limit, paramInt);
    paramSegment.limit += paramInt;
    this.pos += paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/Segment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */