package org.xutils.view;

import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import org.xutils.ViewInjector;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public final class ViewInjectorImpl implements ViewInjector {
  private static final HashSet<Class<?>> IGNORED = new HashSet<Class<?>>();
  
  private static volatile ViewInjectorImpl instance;
  
  private static final Object lock = new Object();
  
  private static ContentView findContentView(Class<?> paramClass) {
    if (paramClass == null || IGNORED.contains(paramClass))
      return null; 
    ContentView contentView2 = paramClass.<ContentView>getAnnotation(ContentView.class);
    ContentView contentView1 = contentView2;
    if (contentView2 == null)
      contentView1 = findContentView(paramClass.getSuperclass()); 
    return contentView1;
  }
  
  private static void injectObject(Object paramObject, Class<?> paramClass, ViewFinder paramViewFinder) {
    if (paramClass != null && !IGNORED.contains(paramClass)) {
      injectObject(paramObject, paramClass.getSuperclass(), paramViewFinder);
      Field[] arrayOfField = paramClass.getDeclaredFields();
      if (arrayOfField != null && arrayOfField.length > 0) {
        int i = arrayOfField.length;
        for (byte b = 0; b < i; b++) {
          Field field = arrayOfField[b];
          Class<?> clazz = field.getType();
          if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && !clazz.isPrimitive() && !clazz.isArray()) {
            ViewInject viewInject = field.<ViewInject>getAnnotation(ViewInject.class);
            if (viewInject != null)
              try {
                View view = paramViewFinder.findViewById(viewInject.value(), viewInject.parentId());
                if (view != null) {
                  field.setAccessible(true);
                  field.set(paramObject, view);
                } else {
                  RuntimeException runtimeException = new RuntimeException();
                  StringBuilder stringBuilder = new StringBuilder();
                  this();
                  this(stringBuilder.append("Invalid @ViewInject for ").append(paramClass.getSimpleName()).append(".").append(field.getName()).toString());
                  throw runtimeException;
                } 
              } catch (Throwable throwable) {
                LogUtil.e(throwable.getMessage(), throwable);
              }  
          } 
        } 
      } 
      Method[] arrayOfMethod = paramClass.getDeclaredMethods();
      if (arrayOfMethod != null && arrayOfMethod.length > 0) {
        int i = arrayOfMethod.length;
        byte b = 0;
        label61: while (true) {
          if (b < i) {
            Method method = arrayOfMethod[b];
            if (!Modifier.isStatic(method.getModifiers())) {
              if (!Modifier.isPrivate(method.getModifiers()))
                continue; 
              Event event = method.<Event>getAnnotation(Event.class);
              if (event != null) {
                try {
                  int j;
                  int[] arrayOfInt2 = event.value();
                  int[] arrayOfInt1 = event.parentId();
                  if (arrayOfInt1 == null) {
                    j = 0;
                  } else {
                    j = arrayOfInt1.length;
                  } 
                  byte b1 = 0;
                  while (true) {
                    if (b1 < arrayOfInt2.length) {
                      int k = arrayOfInt2[b1];
                      if (k > 0) {
                        ViewInfo viewInfo = new ViewInfo();
                        this();
                        viewInfo.value = k;
                        if (j > b1) {
                          k = arrayOfInt1[b1];
                        } else {
                          k = 0;
                        } 
                        viewInfo.parentId = k;
                        method.setAccessible(true);
                        EventListenerManager.addEventMethod(paramViewFinder, viewInfo, event, paramObject, method);
                      } 
                      b1++;
                      continue;
                    } 
                    b++;
                    continue label61;
                  } 
                } catch (Throwable throwable) {
                  LogUtil.e(throwable.getMessage(), throwable);
                  continue;
                } 
                break;
              } 
              continue;
            } 
            continue;
          } 
          return;
        } 
      } 
    } 
  }
  
  public static void registerInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null) {
          ViewInjectorImpl viewInjectorImpl = new ViewInjectorImpl();
          this();
          instance = viewInjectorImpl;
        } 
        x.Ext.setViewInjector(instance);
        return;
      }  
    x.Ext.setViewInjector(instance);
  }
  
  public View inject(Object paramObject, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup) {
    View view2;
    View view1 = null;
    Class<?> clazz = paramObject.getClass();
    try {
      ContentView contentView = findContentView(clazz);
      view2 = view1;
      if (contentView != null) {
        int i = contentView.value();
        view2 = view1;
        if (i > 0)
          view2 = paramLayoutInflater.inflate(i, paramViewGroup, false); 
      } 
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
      view2 = view1;
    } 
    injectObject(paramObject, clazz, new ViewFinder(view2));
    return view2;
  }
  
  public void inject(Activity paramActivity) {
    Class<?> clazz = paramActivity.getClass();
    try {
      ContentView contentView = findContentView(clazz);
      if (contentView != null) {
        int i = contentView.value();
        if (i > 0)
          clazz.getMethod("setContentView", new Class[] { int.class }).invoke(paramActivity, new Object[] { Integer.valueOf(i) }); 
      } 
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
    injectObject(paramActivity, clazz, new ViewFinder(paramActivity));
  }
  
  public void inject(View paramView) {
    injectObject(paramView, paramView.getClass(), new ViewFinder(paramView));
  }
  
  public void inject(Object paramObject, View paramView) {
    injectObject(paramObject, paramObject.getClass(), new ViewFinder(paramView));
  }
  
  static {
    IGNORED.add(Object.class);
    IGNORED.add(Activity.class);
    IGNORED.add(Fragment.class);
    try {
      IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
      IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
    } catch (Throwable throwable) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/view/ViewInjectorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */