package android.support.v4.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback {
  private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
  
  private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
  
  private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
  
  private static int MAX_IMAGE_SIZE = 1048576;
  
  private Matrix mTempMatrix;
  
  private static Bitmap createDrawableBitmap(Drawable paramDrawable) {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    if (i <= 0 || j <= 0)
      return null; 
    float f = Math.min(1.0F, MAX_IMAGE_SIZE / (i * j));
    if (paramDrawable instanceof BitmapDrawable && f == 1.0F)
      return ((BitmapDrawable)paramDrawable).getBitmap(); 
    i = (int)(i * f);
    int k = (int)(j * f);
    Bitmap bitmap = Bitmap.createBitmap(i, k, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Rect rect = paramDrawable.getBounds();
    j = rect.left;
    int m = rect.top;
    int n = rect.right;
    int i1 = rect.bottom;
    paramDrawable.setBounds(0, 0, i, k);
    paramDrawable.draw(canvas);
    paramDrawable.setBounds(j, m, n, i1);
    return bitmap;
  }
  
  public Parcelable onCaptureSharedElementSnapshot(View paramView, Matrix paramMatrix, RectF paramRectF) {
    Bundle bundle1;
    Bundle bundle2;
    float[] arrayOfFloat;
    if (paramView instanceof ImageView) {
      ImageView imageView = (ImageView)paramView;
      Drawable drawable1 = imageView.getDrawable();
      Drawable drawable2 = imageView.getBackground();
      if (drawable1 != null && drawable2 == null) {
        Bitmap bitmap = createDrawableBitmap(drawable1);
        if (bitmap != null) {
          bundle2 = new Bundle();
          bundle2.putParcelable("sharedElement:snapshot:bitmap", (Parcelable)bitmap);
          bundle2.putString("sharedElement:snapshot:imageScaleType", imageView.getScaleType().toString());
          bundle1 = bundle2;
          if (imageView.getScaleType() == ImageView.ScaleType.MATRIX) {
            Matrix matrix = imageView.getImageMatrix();
            arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            bundle2.putFloatArray("sharedElement:snapshot:imageMatrix", arrayOfFloat);
            bundle1 = bundle2;
          } 
          return (Parcelable)bundle1;
        } 
      } 
    } 
    int i = Math.round(arrayOfFloat.width());
    int j = Math.round(arrayOfFloat.height());
    Bitmap bitmap2 = null;
    Bitmap bitmap1 = bitmap2;
    if (i > 0) {
      bitmap1 = bitmap2;
      if (j > 0) {
        float f = Math.min(1.0F, MAX_IMAGE_SIZE / (i * j));
        i = (int)(i * f);
        j = (int)(j * f);
        if (this.mTempMatrix == null)
          this.mTempMatrix = new Matrix(); 
        this.mTempMatrix.set((Matrix)bundle2);
        this.mTempMatrix.postTranslate(-((RectF)arrayOfFloat).left, -((RectF)arrayOfFloat).top);
        this.mTempMatrix.postScale(f, f);
        bitmap1 = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        canvas.concat(this.mTempMatrix);
        bundle1.draw(canvas);
      } 
    } 
    return (Parcelable)bitmap1;
  }
  
  public View onCreateSnapshotView(Context paramContext, Parcelable paramParcelable) {
    ImageView imageView1;
    ImageView imageView2;
    Bitmap bitmap = null;
    if (paramParcelable instanceof Bundle) {
      Bundle bundle = (Bundle)paramParcelable;
      bitmap = (Bitmap)bundle.getParcelable("sharedElement:snapshot:bitmap");
      if (bitmap == null)
        return null; 
      imageView1 = new ImageView(paramContext);
      null = imageView1;
      imageView1.setImageBitmap(bitmap);
      imageView1.setScaleType(ImageView.ScaleType.valueOf(bundle.getString("sharedElement:snapshot:imageScaleType")));
      imageView2 = null;
      if (imageView1.getScaleType() == ImageView.ScaleType.MATRIX) {
        float[] arrayOfFloat = bundle.getFloatArray("sharedElement:snapshot:imageMatrix");
        Matrix matrix = new Matrix();
        matrix.setValues(arrayOfFloat);
        imageView1.setImageMatrix(matrix);
        ImageView imageView = null;
      } 
    } else if (imageView1 instanceof Bitmap) {
      Bitmap bitmap1 = (Bitmap)imageView1;
      imageView2 = new ImageView((Context)null);
      imageView2.setImageBitmap(bitmap1);
    } 
    return (View)imageView2;
  }
  
  public void onMapSharedElements(List<String> paramList, Map<String, View> paramMap) {}
  
  public void onRejectSharedElements(List<View> paramList) {}
  
  public void onSharedElementEnd(List<String> paramList, List<View> paramList1, List<View> paramList2) {}
  
  public void onSharedElementStart(List<String> paramList, List<View> paramList1, List<View> paramList2) {}
  
  public void onSharedElementsArrived(List<String> paramList, List<View> paramList1, OnSharedElementsReadyListener paramOnSharedElementsReadyListener) {
    paramOnSharedElementsReadyListener.onSharedElementsReady();
  }
  
  public static interface OnSharedElementsReadyListener {
    void onSharedElementsReady();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/app/SharedElementCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */