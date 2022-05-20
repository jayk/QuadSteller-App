package app.gamer.quadstellar.ui.slide;

import android.graphics.Canvas;
import android.view.animation.Interpolator;

public class CanvasTransformerBuilder {
  private static Interpolator lin = new Interpolator() {
      public float getInterpolation(float param1Float) {
        return param1Float;
      }
    };
  
  private SlidingMenu.CanvasTransformer mTrans;
  
  private void initTransformer() {
    if (this.mTrans == null)
      this.mTrans = new SlidingMenu.CanvasTransformer() {
          public void transformCanvas(Canvas param1Canvas, float param1Float) {}
        }; 
  }
  
  public SlidingMenu.CanvasTransformer concatTransformer(final SlidingMenu.CanvasTransformer t) {
    initTransformer();
    this.mTrans = new SlidingMenu.CanvasTransformer() {
        public void transformCanvas(Canvas param1Canvas, float param1Float) {
          CanvasTransformerBuilder.this.mTrans.transformCanvas(param1Canvas, param1Float);
          t.transformCanvas(param1Canvas, param1Float);
        }
      };
    return this.mTrans;
  }
  
  public SlidingMenu.CanvasTransformer rotate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return rotate(paramInt1, paramInt2, paramInt3, paramInt4, lin);
  }
  
  public SlidingMenu.CanvasTransformer rotate(final int openedDeg, final int closedDeg, final int px, final int py, final Interpolator interp) {
    initTransformer();
    this.mTrans = new SlidingMenu.CanvasTransformer() {
        public void transformCanvas(Canvas param1Canvas, float param1Float) {
          CanvasTransformerBuilder.this.mTrans.transformCanvas(param1Canvas, param1Float);
          param1Float = interp.getInterpolation(param1Float);
          param1Canvas.rotate((openedDeg - closedDeg) * param1Float + closedDeg, px, py);
        }
      };
    return this.mTrans;
  }
  
  public SlidingMenu.CanvasTransformer translate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return translate(paramInt1, paramInt2, paramInt3, paramInt4, lin);
  }
  
  public SlidingMenu.CanvasTransformer translate(final int openedX, final int closedX, final int openedY, final int closedY, final Interpolator interp) {
    initTransformer();
    this.mTrans = new SlidingMenu.CanvasTransformer() {
        public void transformCanvas(Canvas param1Canvas, float param1Float) {
          CanvasTransformerBuilder.this.mTrans.transformCanvas(param1Canvas, param1Float);
          param1Float = interp.getInterpolation(param1Float);
          param1Canvas.translate((openedX - closedX) * param1Float + closedX, (openedY - closedY) * param1Float + closedY);
        }
      };
    return this.mTrans;
  }
  
  public SlidingMenu.CanvasTransformer zoom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    return zoom(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, lin);
  }
  
  public SlidingMenu.CanvasTransformer zoom(final int openedX, final int closedX, final int openedY, final int closedY, final int px, final int py, final Interpolator interp) {
    initTransformer();
    this.mTrans = new SlidingMenu.CanvasTransformer() {
        public void transformCanvas(Canvas param1Canvas, float param1Float) {
          CanvasTransformerBuilder.this.mTrans.transformCanvas(param1Canvas, param1Float);
          param1Float = interp.getInterpolation(param1Float);
          param1Canvas.scale((openedX - closedX) * param1Float + closedX, (openedY - closedY) * param1Float + closedY, px, py);
        }
      };
    return this.mTrans;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/slide/CanvasTransformerBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */