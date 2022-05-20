package org.xutils.db.table;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.xutils.common.util.LogUtil;
import org.xutils.db.annotation.Column;
import org.xutils.db.converter.ColumnConverterFactory;

final class TableUtils {
  private static void addColumns2Map(Class<?> paramClass, HashMap<String, ColumnEntity> paramHashMap) {
    if (!Object.class.equals(paramClass)) {
      try {
        for (Field field : paramClass.getDeclaredFields()) {
          int i = field.getModifiers();
          if (!Modifier.isStatic(i) && !Modifier.isTransient(i)) {
            Column column = field.<Column>getAnnotation(Column.class);
            if (column != null && ColumnConverterFactory.isSupportColumnConverter(field.getType())) {
              ColumnEntity columnEntity = new ColumnEntity();
              this(paramClass, field, column);
              if (!paramHashMap.containsKey(columnEntity.getName()))
                paramHashMap.put(columnEntity.getName(), columnEntity); 
            } 
          } 
        } 
      } catch (Throwable throwable) {
        LogUtil.e(throwable.getMessage(), throwable);
        return;
      } 
      addColumns2Map(throwable.getSuperclass(), paramHashMap);
    } 
  }
  
  static LinkedHashMap<String, ColumnEntity> findColumnMap(Class<?> paramClass) {
    // Byte code:
    //   0: ldc org/xutils/db/table/TableUtils
    //   2: monitorenter
    //   3: new java/util/LinkedHashMap
    //   6: astore_1
    //   7: aload_1
    //   8: invokespecial <init> : ()V
    //   11: aload_0
    //   12: aload_1
    //   13: invokestatic addColumns2Map : (Ljava/lang/Class;Ljava/util/HashMap;)V
    //   16: ldc org/xutils/db/table/TableUtils
    //   18: monitorexit
    //   19: aload_1
    //   20: areturn
    //   21: astore_0
    //   22: ldc org/xutils/db/table/TableUtils
    //   24: monitorexit
    //   25: aload_0
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   3	16	21	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/table/TableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */