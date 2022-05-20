package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public final class ConstructorConstructor {
  private final Map<Type, InstanceCreator<?>> instanceCreators;
  
  public ConstructorConstructor(Map<Type, InstanceCreator<?>> paramMap) {
    this.instanceCreators = paramMap;
  }
  
  private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> paramClass) {
    try {
      Constructor<? super T> constructor = paramClass.getDeclaredConstructor(new Class[0]);
      if (!constructor.isAccessible())
        constructor.setAccessible(true); 
      ObjectConstructor<T> objectConstructor = new ObjectConstructor<T>() {
          public T construct() {
            try {
              return (T)constructor.newInstance((Object[])null);
            } catch (InstantiationException instantiationException) {
              throw new RuntimeException("Failed to invoke " + constructor + " with no args", instantiationException);
            } catch (InvocationTargetException invocationTargetException) {
              throw new RuntimeException("Failed to invoke " + constructor + " with no args", invocationTargetException.getTargetException());
            } catch (IllegalAccessException illegalAccessException) {
              throw new AssertionError(illegalAccessException);
            } 
          }
        };
      super(this, constructor);
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException = null;
    } 
    return (ObjectConstructor<T>)noSuchMethodException;
  }
  
  private <T> ObjectConstructor<T> newDefaultImplementationConstructor(final Type type, Class<? super T> paramClass) {
    return Collection.class.isAssignableFrom(paramClass) ? (SortedSet.class.isAssignableFrom(paramClass) ? new ObjectConstructor<T>() {
        public T construct() {
          return (T)new TreeSet();
        }
      } : (EnumSet.class.isAssignableFrom(paramClass) ? new ObjectConstructor<T>() {
        public T construct() {
          if (type instanceof ParameterizedType) {
            Type type = ((ParameterizedType)type).getActualTypeArguments()[0];
            if (type instanceof Class)
              return (T)EnumSet.noneOf((Class<Enum>)type); 
            throw new JsonIOException("Invalid EnumSet type: " + type.toString());
          } 
          throw new JsonIOException("Invalid EnumSet type: " + type.toString());
        }
      } : (Set.class.isAssignableFrom(paramClass) ? new ObjectConstructor<T>() {
        public T construct() {
          return (T)new LinkedHashSet();
        }
      } : (Queue.class.isAssignableFrom(paramClass) ? new ObjectConstructor<T>() {
        public T construct() {
          return (T)new LinkedList();
        }
      } : new ObjectConstructor<T>() {
        public T construct() {
          return (T)new ArrayList();
        }
      })))) : (Map.class.isAssignableFrom(paramClass) ? (SortedMap.class.isAssignableFrom(paramClass) ? new ObjectConstructor<T>() {
        public T construct() {
          return (T)new TreeMap<Object, Object>();
        }
      } : ((type instanceof ParameterizedType && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType)type).getActualTypeArguments()[0]).getRawType())) ? new ObjectConstructor<T>() {
        public T construct() {
          return (T)new LinkedHashMap<Object, Object>();
        }
      } : new ObjectConstructor<T>() {
        public T construct() {
          return (T)new LinkedTreeMap<Object, Object>();
        }
      })) : null);
  }
  
  private <T> ObjectConstructor<T> newUnsafeAllocator(final Type type, final Class<? super T> rawType) {
    return new ObjectConstructor<T>() {
        private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        
        public T construct() {
          try {
            return (T)this.unsafeAllocator.newInstance(rawType);
          } catch (Exception exception) {
            throw new RuntimeException("Unable to invoke no-args constructor for " + type + ". " + "Register an InstanceCreator with Gson for this type may fix this problem.", exception);
          } 
        }
      };
  }
  
  public <T> ObjectConstructor<T> get(TypeToken<T> paramTypeToken) {
    final Type type = paramTypeToken.getType();
    Class<?> clazz = paramTypeToken.getRawType();
    final InstanceCreator rawTypeCreator = this.instanceCreators.get(type);
    if (instanceCreator != null)
      return new ObjectConstructor<T>() {
          public T construct() {
            return (T)typeCreator.createInstance(type);
          }
        }; 
    instanceCreator = this.instanceCreators.get(clazz);
    if (instanceCreator != null)
      return new ObjectConstructor<T>() {
          public T construct() {
            return (T)rawTypeCreator.createInstance(type);
          }
        }; 
    ObjectConstructor<?> objectConstructor2 = newDefaultConstructor(clazz);
    ObjectConstructor<?> objectConstructor1 = objectConstructor2;
    if (objectConstructor2 == null) {
      objectConstructor1 = newDefaultImplementationConstructor(type, clazz);
      if (objectConstructor1 == null)
        objectConstructor1 = newUnsafeAllocator(type, clazz); 
    } 
    return (ObjectConstructor)objectConstructor1;
  }
  
  public String toString() {
    return this.instanceCreators.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/ConstructorConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */