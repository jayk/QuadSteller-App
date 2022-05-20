package app.lib.lockview;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;

@SuppressLint({"NewApi"})
class Ease {
  private static final float DOMAIN = 1.0F;
  
  private static final float DURATION = 1.0F;
  
  private static final float START = 0.0F;
  
  static class Cubic {
    public static final TimeInterpolator easeIn = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 1.0F;
          return 1.0F * param2Float * param2Float * param2Float + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeInOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 0.5F;
          if (param2Float < 1.0F)
            return 0.5F * param2Float * param2Float * param2Float + 0.0F; 
          param2Float -= 2.0F;
          return (param2Float * param2Float * param2Float + 2.0F) * 0.5F + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float = param2Float / 1.0F - 1.0F;
          return (param2Float * param2Float * param2Float + 1.0F) * 1.0F + 0.0F;
        }
      };
    
    static {
    
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 1.0F;
      return 1.0F * param1Float * param1Float * param1Float + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float = param1Float / 1.0F - 1.0F;
      return (param1Float * param1Float * param1Float + 1.0F) * 1.0F + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 0.5F;
      if (param1Float < 1.0F)
        return 0.5F * param1Float * param1Float * param1Float + 0.0F; 
      param1Float -= 2.0F;
      return (param1Float * param1Float * param1Float + 2.0F) * 0.5F + 0.0F;
    }
  }
  
  static class Linear {
    public static final TimeInterpolator easeNone = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          return param2Float;
        }
      };
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      return param1Float;
    }
  }
  
  static class Quad {
    public static final TimeInterpolator easeIn = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 1.0F;
          return 1.0F * param2Float * param2Float + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeInOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 0.5F;
          if (param2Float < 1.0F)
            return 0.5F * param2Float * param2Float + 0.0F; 
          param2Float--;
          return -0.5F * ((param2Float - 2.0F) * param2Float - 1.0F) + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 1.0F;
          return -1.0F * param2Float * (param2Float - 2.0F) + 0.0F;
        }
      };
    
    static {
    
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 1.0F;
      return 1.0F * param1Float * param1Float + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 1.0F;
      return -1.0F * param1Float * (param1Float - 2.0F) + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 0.5F;
      if (param1Float < 1.0F)
        return 0.5F * param1Float * param1Float + 0.0F; 
      param1Float--;
      return -0.5F * ((param1Float - 2.0F) * param1Float - 1.0F) + 0.0F;
    }
  }
  
  static class Quart {
    public static final TimeInterpolator easeIn = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 1.0F;
          return 1.0F * param2Float * param2Float * param2Float * param2Float + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeInOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 0.5F;
          if (param2Float < 1.0F)
            return 0.5F * param2Float * param2Float * param2Float * param2Float + 0.0F; 
          param2Float -= 2.0F;
          return -0.5F * (param2Float * param2Float * param2Float * param2Float - 2.0F) + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float = param2Float / 1.0F - 1.0F;
          return -1.0F * (param2Float * param2Float * param2Float * param2Float - 1.0F) + 0.0F;
        }
      };
    
    static {
    
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 1.0F;
      return 1.0F * param1Float * param1Float * param1Float * param1Float + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float = param1Float / 1.0F - 1.0F;
      return -1.0F * (param1Float * param1Float * param1Float * param1Float - 1.0F) + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 0.5F;
      if (param1Float < 1.0F)
        return 0.5F * param1Float * param1Float * param1Float * param1Float + 0.0F; 
      param1Float -= 2.0F;
      return -0.5F * (param1Float * param1Float * param1Float * param1Float - 2.0F) + 0.0F;
    }
  }
  
  static class Quint {
    public static final TimeInterpolator easeIn = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 1.0F;
          return 1.0F * param2Float * param2Float * param2Float * param2Float * param2Float + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeInOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float /= 0.5F;
          if (param2Float < 1.0F)
            return 0.5F * param2Float * param2Float * param2Float * param2Float * param2Float + 0.0F; 
          param2Float -= 2.0F;
          return (param2Float * param2Float * param2Float * param2Float * param2Float + 2.0F) * 0.5F + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          param2Float = param2Float / 1.0F - 1.0F;
          return (param2Float * param2Float * param2Float * param2Float * param2Float + 1.0F) * 1.0F + 0.0F;
        }
      };
    
    static {
    
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 1.0F;
      return 1.0F * param1Float * param1Float * param1Float * param1Float * param1Float + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float = param1Float / 1.0F - 1.0F;
      return (param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F) * 1.0F + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      param1Float /= 0.5F;
      if (param1Float < 1.0F)
        return 0.5F * param1Float * param1Float * param1Float * param1Float * param1Float + 0.0F; 
      param1Float -= 2.0F;
      return (param1Float * param1Float * param1Float * param1Float * param1Float + 2.0F) * 0.5F + 0.0F;
    }
  }
  
  static class Sine {
    public static final TimeInterpolator easeIn = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          return -1.0F * (float)Math.cos((param2Float / 1.0F) * 1.5707963267948966D) + 1.0F + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeInOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          return -0.5F * ((float)Math.cos(Math.PI * param2Float / 1.0D) - 1.0F) + 0.0F;
        }
      };
    
    public static final TimeInterpolator easeOut = new TimeInterpolator() {
        public float getInterpolation(float param2Float) {
          return (float)Math.sin((param2Float / 1.0F) * 1.5707963267948966D) * 1.0F + 0.0F;
        }
      };
    
    static {
    
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      return -1.0F * (float)Math.cos((param1Float / 1.0F) * 1.5707963267948966D) + 1.0F + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      return (float)Math.sin((param1Float / 1.0F) * 1.5707963267948966D) * 1.0F + 0.0F;
    }
  }
  
  static final class null implements TimeInterpolator {
    public float getInterpolation(float param1Float) {
      return -0.5F * ((float)Math.cos(Math.PI * param1Float / 1.0D) - 1.0F) + 0.0F;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/lockview/Ease.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */