package org.greenrobot.greendao.converter;

public interface PropertyConverter<P, D> {
  D convertToDatabaseValue(P paramP);
  
  P convertToEntityProperty(D paramD);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/converter/PropertyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */