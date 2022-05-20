package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import app.gamer.quadstellar.R;
import app.gamer.quadstellar.utils.LightTool;
import app.gamer.quadstellar.utils.TimerUtils;

public class MyTextView extends TextView {
  private int mDrawableSize;
  
  public MyTextView(Context paramContext) {
    this(paramContext, (AttributeSet)null, 0);
    init();
  }
  
  public MyTextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
    init();
  }
  
  public MyTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    Drawable drawable1;
    Drawable drawable2;
    Drawable drawable3 = null;
    Context context = null;
    AttributeSet attributeSet = null;
    Drawable drawable4 = null;
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DrawableState);
    int i = typedArray.getIndexCount();
    paramInt = 0;
    paramContext = context;
    paramAttributeSet = attributeSet;
    while (paramInt < i) {
      int j = typedArray.getIndex(paramInt);
      switch (j) {
        case 0:
          this.mDrawableSize = typedArray.getDimensionPixelSize(0, 40);
          Log.i("MyTextView", "mDrawableSize:" + this.mDrawableSize);
          paramInt++;
          break;
        case 1:
          drawable1 = typedArray.getDrawable(j);
          paramInt++;
          break;
        case 2:
          drawable4 = typedArray.getDrawable(j);
          paramInt++;
          break;
        case 3:
          drawable2 = typedArray.getDrawable(j);
          paramInt++;
          break;
        case 4:
          drawable3 = typedArray.getDrawable(j);
          paramInt++;
          break;
      } 
    } 
    typedArray.recycle();
    setCompoundDrawablesWithIntrinsicBounds(drawable3, drawable1, drawable2, drawable4);
    init();
  }
  
  private void init() {
    setText(LightTool.get2Number(TimerUtils.getCurHour()) + ":" + LightTool.get2Number(TimerUtils.getCurMin()));
  }
  
  public void setCompoundDrawablesWithIntrinsicBounds(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 != null)
      paramDrawable1.setBounds(0, 0, this.mDrawableSize, this.mDrawableSize); 
    if (paramDrawable3 != null)
      paramDrawable3.setBounds(0, 0, this.mDrawableSize, this.mDrawableSize); 
    if (paramDrawable2 != null)
      paramDrawable2.setBounds(0, 0, this.mDrawableSize, this.mDrawableSize); 
    if (paramDrawable4 != null)
      paramDrawable4.setBounds(0, 0, this.mDrawableSize, this.mDrawableSize); 
    setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/MyTextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */