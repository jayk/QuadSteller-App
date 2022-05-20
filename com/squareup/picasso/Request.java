package com.squareup.picasso;

import android.graphics.Bitmap;
import android.net.Uri;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class Request {
  private static final long TOO_LONG_LOG = TimeUnit.SECONDS.toNanos(5L);
  
  public final boolean centerCrop;
  
  public final boolean centerInside;
  
  public final Bitmap.Config config;
  
  public final boolean hasRotationPivot;
  
  int id;
  
  int networkPolicy;
  
  public final boolean onlyScaleDown;
  
  public final Picasso.Priority priority;
  
  public final int resourceId;
  
  public final float rotationDegrees;
  
  public final float rotationPivotX;
  
  public final float rotationPivotY;
  
  public final String stableKey;
  
  long started;
  
  public final int targetHeight;
  
  public final int targetWidth;
  
  public final List<Transformation> transformations;
  
  public final Uri uri;
  
  private Request(Uri paramUri, int paramInt1, String paramString, List<Transformation> paramList, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean4, Bitmap.Config paramConfig, Picasso.Priority paramPriority) {
    this.uri = paramUri;
    this.resourceId = paramInt1;
    this.stableKey = paramString;
    if (paramList == null) {
      this.transformations = null;
    } else {
      this.transformations = Collections.unmodifiableList(paramList);
    } 
    this.targetWidth = paramInt2;
    this.targetHeight = paramInt3;
    this.centerCrop = paramBoolean1;
    this.centerInside = paramBoolean2;
    this.onlyScaleDown = paramBoolean3;
    this.rotationDegrees = paramFloat1;
    this.rotationPivotX = paramFloat2;
    this.rotationPivotY = paramFloat3;
    this.hasRotationPivot = paramBoolean4;
    this.config = paramConfig;
    this.priority = paramPriority;
  }
  
  public Builder buildUpon() {
    return new Builder(this);
  }
  
  String getName() {
    return (this.uri != null) ? String.valueOf(this.uri.getPath()) : Integer.toHexString(this.resourceId);
  }
  
  boolean hasCustomTransformations() {
    return (this.transformations != null);
  }
  
  public boolean hasSize() {
    return (this.targetWidth != 0 || this.targetHeight != 0);
  }
  
  String logId() {
    long l = System.nanoTime() - this.started;
    return (l > TOO_LONG_LOG) ? (plainId() + '+' + TimeUnit.NANOSECONDS.toSeconds(l) + 's') : (plainId() + '+' + TimeUnit.NANOSECONDS.toMillis(l) + "ms");
  }
  
  boolean needsMatrixTransform() {
    return (hasSize() || this.rotationDegrees != 0.0F);
  }
  
  boolean needsTransformation() {
    return (needsMatrixTransform() || hasCustomTransformations());
  }
  
  String plainId() {
    return "[R" + this.id + ']';
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Request{");
    if (this.resourceId > 0) {
      stringBuilder.append(this.resourceId);
    } else {
      stringBuilder.append(this.uri);
    } 
    if (this.transformations != null && !this.transformations.isEmpty())
      for (Transformation transformation : this.transformations)
        stringBuilder.append(' ').append(transformation.key());  
    if (this.stableKey != null)
      stringBuilder.append(" stableKey(").append(this.stableKey).append(')'); 
    if (this.targetWidth > 0)
      stringBuilder.append(" resize(").append(this.targetWidth).append(',').append(this.targetHeight).append(')'); 
    if (this.centerCrop)
      stringBuilder.append(" centerCrop"); 
    if (this.centerInside)
      stringBuilder.append(" centerInside"); 
    if (this.rotationDegrees != 0.0F) {
      stringBuilder.append(" rotation(").append(this.rotationDegrees);
      if (this.hasRotationPivot)
        stringBuilder.append(" @ ").append(this.rotationPivotX).append(',').append(this.rotationPivotY); 
      stringBuilder.append(')');
    } 
    if (this.config != null)
      stringBuilder.append(' ').append(this.config); 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public static final class Builder {
    private boolean centerCrop;
    
    private boolean centerInside;
    
    private Bitmap.Config config;
    
    private boolean hasRotationPivot;
    
    private boolean onlyScaleDown;
    
    private Picasso.Priority priority;
    
    private int resourceId;
    
    private float rotationDegrees;
    
    private float rotationPivotX;
    
    private float rotationPivotY;
    
    private String stableKey;
    
    private int targetHeight;
    
    private int targetWidth;
    
    private List<Transformation> transformations;
    
    private Uri uri;
    
    public Builder(int param1Int) {
      setResourceId(param1Int);
    }
    
    public Builder(Uri param1Uri) {
      setUri(param1Uri);
    }
    
    Builder(Uri param1Uri, int param1Int, Bitmap.Config param1Config) {
      this.uri = param1Uri;
      this.resourceId = param1Int;
      this.config = param1Config;
    }
    
    private Builder(Request param1Request) {
      this.uri = param1Request.uri;
      this.resourceId = param1Request.resourceId;
      this.stableKey = param1Request.stableKey;
      this.targetWidth = param1Request.targetWidth;
      this.targetHeight = param1Request.targetHeight;
      this.centerCrop = param1Request.centerCrop;
      this.centerInside = param1Request.centerInside;
      this.rotationDegrees = param1Request.rotationDegrees;
      this.rotationPivotX = param1Request.rotationPivotX;
      this.rotationPivotY = param1Request.rotationPivotY;
      this.hasRotationPivot = param1Request.hasRotationPivot;
      this.onlyScaleDown = param1Request.onlyScaleDown;
      if (param1Request.transformations != null)
        this.transformations = new ArrayList<Transformation>(param1Request.transformations); 
      this.config = param1Request.config;
      this.priority = param1Request.priority;
    }
    
    public Request build() {
      if (this.centerInside && this.centerCrop)
        throw new IllegalStateException("Center crop and center inside can not be used together."); 
      if (this.centerCrop && this.targetWidth == 0 && this.targetHeight == 0)
        throw new IllegalStateException("Center crop requires calling resize with positive width and height."); 
      if (this.centerInside && this.targetWidth == 0 && this.targetHeight == 0)
        throw new IllegalStateException("Center inside requires calling resize with positive width and height."); 
      if (this.priority == null)
        this.priority = Picasso.Priority.NORMAL; 
      return new Request(this.uri, this.resourceId, this.stableKey, this.transformations, this.targetWidth, this.targetHeight, this.centerCrop, this.centerInside, this.onlyScaleDown, this.rotationDegrees, this.rotationPivotX, this.rotationPivotY, this.hasRotationPivot, this.config, this.priority);
    }
    
    public Builder centerCrop() {
      if (this.centerInside)
        throw new IllegalStateException("Center crop can not be used after calling centerInside"); 
      this.centerCrop = true;
      return this;
    }
    
    public Builder centerInside() {
      if (this.centerCrop)
        throw new IllegalStateException("Center inside can not be used after calling centerCrop"); 
      this.centerInside = true;
      return this;
    }
    
    public Builder clearCenterCrop() {
      this.centerCrop = false;
      return this;
    }
    
    public Builder clearCenterInside() {
      this.centerInside = false;
      return this;
    }
    
    public Builder clearOnlyScaleDown() {
      this.onlyScaleDown = false;
      return this;
    }
    
    public Builder clearResize() {
      this.targetWidth = 0;
      this.targetHeight = 0;
      this.centerCrop = false;
      this.centerInside = false;
      return this;
    }
    
    public Builder clearRotation() {
      this.rotationDegrees = 0.0F;
      this.rotationPivotX = 0.0F;
      this.rotationPivotY = 0.0F;
      this.hasRotationPivot = false;
      return this;
    }
    
    public Builder config(Bitmap.Config param1Config) {
      this.config = param1Config;
      return this;
    }
    
    boolean hasImage() {
      return (this.uri != null || this.resourceId != 0);
    }
    
    boolean hasPriority() {
      return (this.priority != null);
    }
    
    boolean hasSize() {
      return (this.targetWidth != 0 || this.targetHeight != 0);
    }
    
    public Builder onlyScaleDown() {
      if (this.targetHeight == 0 && this.targetWidth == 0)
        throw new IllegalStateException("onlyScaleDown can not be applied without resize"); 
      this.onlyScaleDown = true;
      return this;
    }
    
    public Builder priority(Picasso.Priority param1Priority) {
      if (param1Priority == null)
        throw new IllegalArgumentException("Priority invalid."); 
      if (this.priority != null)
        throw new IllegalStateException("Priority already set."); 
      this.priority = param1Priority;
      return this;
    }
    
    public Builder resize(int param1Int1, int param1Int2) {
      if (param1Int1 < 0)
        throw new IllegalArgumentException("Width must be positive number or 0."); 
      if (param1Int2 < 0)
        throw new IllegalArgumentException("Height must be positive number or 0."); 
      if (param1Int2 == 0 && param1Int1 == 0)
        throw new IllegalArgumentException("At least one dimension has to be positive number."); 
      this.targetWidth = param1Int1;
      this.targetHeight = param1Int2;
      return this;
    }
    
    public Builder rotate(float param1Float) {
      this.rotationDegrees = param1Float;
      return this;
    }
    
    public Builder rotate(float param1Float1, float param1Float2, float param1Float3) {
      this.rotationDegrees = param1Float1;
      this.rotationPivotX = param1Float2;
      this.rotationPivotY = param1Float3;
      this.hasRotationPivot = true;
      return this;
    }
    
    public Builder setResourceId(int param1Int) {
      if (param1Int == 0)
        throw new IllegalArgumentException("Image resource ID may not be 0."); 
      this.resourceId = param1Int;
      this.uri = null;
      return this;
    }
    
    public Builder setUri(Uri param1Uri) {
      if (param1Uri == null)
        throw new IllegalArgumentException("Image URI may not be null."); 
      this.uri = param1Uri;
      this.resourceId = 0;
      return this;
    }
    
    public Builder stableKey(String param1String) {
      this.stableKey = param1String;
      return this;
    }
    
    public Builder transform(Transformation param1Transformation) {
      if (param1Transformation == null)
        throw new IllegalArgumentException("Transformation must not be null."); 
      if (param1Transformation.key() == null)
        throw new IllegalArgumentException("Transformation key must not be null."); 
      if (this.transformations == null)
        this.transformations = new ArrayList<Transformation>(2); 
      this.transformations.add(param1Transformation);
      return this;
    }
    
    public Builder transform(List<? extends Transformation> param1List) {
      if (param1List == null)
        throw new IllegalArgumentException("Transformation list must not be null."); 
      byte b = 0;
      int i = param1List.size();
      while (b < i) {
        transform(param1List.get(b));
        b++;
      } 
      return this;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/Request.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */