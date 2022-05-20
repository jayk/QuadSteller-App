package app.gamer.quadstellar.newdevices.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AvatarImageView extends ImageView {
  public AvatarImageView(Context paramContext) {
    super(paramContext);
  }
  
  public AvatarImageView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public AvatarImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onDraw(Canvas paramCanvas) {
    Drawable drawable = getDrawable();
    if (drawable != null && getWidth() != 0 && getHeight() != 0) {
      Paint paint = new Paint();
      paint.setColor(-12434878);
      paint.setAntiAlias(true);
      PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
      Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
      paramCanvas.saveLayer(0.0F, 0.0F, getWidth(), getHeight(), null, 31);
      paramCanvas.drawCircle((getWidth() / 2), (getHeight() / 2), (getWidth() / 2), paint);
      paint.setXfermode((Xfermode)porterDuffXfermode);
      float f1 = getWidth() / bitmap.getWidth();
      float f2 = getHeight() / bitmap.getHeight();
      Matrix matrix = new Matrix();
      matrix.postScale(f1, f2);
      paramCanvas.drawBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), 0.0F, 0.0F, paint);
      paramCanvas.restore();
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/AvatarImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */