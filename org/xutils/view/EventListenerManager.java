package org.xutils.view;

import android.text.TextUtils;
import android.view.View;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.xutils.common.util.DoubleKeyValueMap;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;

final class EventListenerManager {
  private static final HashSet<String> AVOID_QUICK_EVENT_SET = new HashSet<String>(2);
  
  private static final long QUICK_EVENT_TIME_SPAN = 300L;
  
  private static final DoubleKeyValueMap<ViewInfo, Class<?>, Object> listenerCache = new DoubleKeyValueMap();
  
  public static void addEventMethod(ViewFinder paramViewFinder, ViewInfo paramViewInfo, Event paramEvent, Object paramObject, Method paramMethod) {
    try {
      View view = paramViewFinder.findViewByInfo(paramViewInfo);
      if (view != null) {
        Class clazz = paramEvent.type();
        String str2 = paramEvent.setter();
        String str1 = str2;
        if (TextUtils.isEmpty(str2)) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          str1 = stringBuilder.append("set").append(clazz.getSimpleName()).toString();
        } 
        str2 = paramEvent.method();
        boolean bool = false;
        Object object = listenerCache.get(paramViewInfo, clazz);
        if (object != null) {
          DynamicHandler dynamicHandler = (DynamicHandler)Proxy.getInvocationHandler(object);
          boolean bool1 = paramObject.equals(dynamicHandler.getHandler());
          bool = bool1;
          if (bool1) {
            dynamicHandler.addMethod(str2, paramMethod);
            bool = bool1;
          } 
        } 
        if (!bool) {
          object = new DynamicHandler();
          super(paramObject);
          object.addMethod(str2, paramMethod);
          object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, (InvocationHandler)object);
          listenerCache.put(paramViewInfo, clazz, object);
        } 
        view.getClass().getMethod(str1, new Class[] { clazz }).invoke(view, new Object[] { object });
      } 
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  static {
    AVOID_QUICK_EVENT_SET.add("onClick");
    AVOID_QUICK_EVENT_SET.add("onItemClick");
  }
  
  public static class DynamicHandler implements InvocationHandler {
    private static long lastClickTime = 0L;
    
    private WeakReference<Object> handlerRef;
    
    private final HashMap<String, Method> methodMap = new HashMap<String, Method>(1);
    
    public DynamicHandler(Object param1Object) {
      this.handlerRef = new WeakReference(param1Object);
    }
    
    public void addMethod(String param1String, Method param1Method) {
      this.methodMap.put(param1String, param1Method);
    }
    
    public Object getHandler() {
      return this.handlerRef.get();
    }
    
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      Object object = this.handlerRef.get();
      if (object != null) {
        String str = param1Method.getName();
        if ("toString".equals(str))
          return DynamicHandler.class.getSimpleName(); 
        param1Method = this.methodMap.get(str);
        param1Object = param1Method;
        if (param1Method == null) {
          param1Object = param1Method;
          if (this.methodMap.size() == 1) {
            Iterator<Map.Entry> iterator = this.methodMap.entrySet().iterator();
            param1Object = param1Method;
            if (iterator.hasNext()) {
              Map.Entry entry = iterator.next();
              param1Object = param1Method;
              if (TextUtils.isEmpty((CharSequence)entry.getKey()))
                param1Object = entry.getValue(); 
            } 
          } 
        } 
        if (param1Object != null) {
          if (EventListenerManager.AVOID_QUICK_EVENT_SET.contains(str)) {
            long l = System.currentTimeMillis() - lastClickTime;
            if (l < 300L) {
              LogUtil.d("onClick cancelled: " + l);
              return null;
            } 
            lastClickTime = System.currentTimeMillis();
          } 
          try {
            Object object1 = param1Object.invoke(object, param1ArrayOfObject);
            param1Object = object1;
          } catch (Throwable throwable) {
            throw new RuntimeException("invoke method error:" + object.getClass().getName() + "#" + param1Object.getName(), throwable);
          } 
          return param1Object;
        } 
        LogUtil.w("method not impl: " + str + "(" + object.getClass().getSimpleName() + ")");
      } 
      return null;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/view/EventListenerManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */