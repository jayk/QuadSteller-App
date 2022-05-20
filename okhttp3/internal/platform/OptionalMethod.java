package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class OptionalMethod<T> {
  private final String methodName;
  
  private final Class[] methodParams;
  
  private final Class<?> returnType;
  
  OptionalMethod(Class<?> paramClass, String paramString, Class... paramVarArgs) {
    this.returnType = paramClass;
    this.methodName = paramString;
    this.methodParams = paramVarArgs;
  }
  
  private Method getMethod(Class<?> paramClass) {
    Method method = null;
    if (this.methodName != null) {
      Method method1 = getPublicMethod(paramClass, this.methodName, this.methodParams);
      method = method1;
      if (method1 != null) {
        method = method1;
        if (this.returnType != null) {
          method = method1;
          if (!this.returnType.isAssignableFrom(method1.getReturnType()))
            method = null; 
        } 
      } 
    } 
    return method;
  }
  
  private static Method getPublicMethod(Class<?> paramClass, String paramString, Class[] paramArrayOfClass) {
    Method method = null;
    try {
      Method method1 = paramClass.getMethod(paramString, paramArrayOfClass);
      method = method1;
      int i = method1.getModifiers();
      method = method1;
      if ((i & 0x1) == 0)
        method = null; 
    } catch (NoSuchMethodException noSuchMethodException) {}
    return method;
  }
  
  public Object invoke(T paramT, Object... paramVarArgs) throws InvocationTargetException {
    Method method = getMethod(paramT.getClass());
    if (method == null)
      throw new AssertionError("Method " + this.methodName + " not supported for object " + paramT); 
    try {
      return method.invoke(paramT, paramVarArgs);
    } catch (IllegalAccessException illegalAccessException) {
      AssertionError assertionError = new AssertionError("Unexpectedly could not call: " + method);
      assertionError.initCause(illegalAccessException);
      throw assertionError;
    } 
  }
  
  public Object invokeOptional(T paramT, Object... paramVarArgs) throws InvocationTargetException {
    T t1;
    T t2 = null;
    Method method = getMethod(paramT.getClass());
    if (method == null)
      return t2; 
    try {
      paramT = (T)method.invoke(paramT, paramVarArgs);
    } catch (IllegalAccessException illegalAccessException) {
      t1 = t2;
    } 
    return t1;
  }
  
  public Object invokeOptionalWithoutCheckedException(T paramT, Object... paramVarArgs) {
    try {
      return invokeOptional(paramT, paramVarArgs);
    } catch (InvocationTargetException invocationTargetException) {
      Throwable throwable = invocationTargetException.getTargetException();
      if (throwable instanceof RuntimeException)
        throw (RuntimeException)throwable; 
      AssertionError assertionError = new AssertionError("Unexpected exception");
      assertionError.initCause(throwable);
      throw assertionError;
    } 
  }
  
  public Object invokeWithoutCheckedException(T paramT, Object... paramVarArgs) {
    try {
      return invoke(paramT, paramVarArgs);
    } catch (InvocationTargetException invocationTargetException) {
      Throwable throwable = invocationTargetException.getTargetException();
      if (throwable instanceof RuntimeException)
        throw (RuntimeException)throwable; 
      AssertionError assertionError = new AssertionError("Unexpected exception");
      assertionError.initCause(throwable);
      throw assertionError;
    } 
  }
  
  public boolean isSupported(T paramT) {
    return (getMethod(paramT.getClass()) != null);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/platform/OptionalMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */