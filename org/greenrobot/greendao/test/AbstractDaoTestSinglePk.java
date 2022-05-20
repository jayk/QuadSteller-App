package org.greenrobot.greendao.test;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;

public abstract class AbstractDaoTestSinglePk<D extends AbstractDao<T, K>, T, K> extends AbstractDaoTest<D, T, K> {
  private Property pkColumn;
  
  protected Set<K> usedPks = new HashSet<K>();
  
  public AbstractDaoTestSinglePk(Class<D> paramClass) {
    super(paramClass);
  }
  
  protected boolean checkKeyIsNullable() {
    if (createEntity((K)null) == null) {
      DaoLog.d("Test is not available for entities with non-null keys");
      return false;
    } 
    return true;
  }
  
  protected abstract T createEntity(K paramK);
  
  protected T createEntityWithRandomPk() {
    return createEntity(nextPk());
  }
  
  protected abstract K createRandomPk();
  
  protected K nextPk() {
    for (byte b = 0; b < 100000; b++) {
      K k = createRandomPk();
      if (this.usedPks.add(k))
        return k; 
    } 
    throw new IllegalStateException("Could not find a new PK");
  }
  
  protected Cursor queryWithDummyColumnsInFront(int paramInt, String paramString, K paramK) {
    StringBuilder stringBuilder = new StringBuilder("SELECT ");
    byte b;
    for (b = 0; b < paramInt; b++)
      stringBuilder.append(paramString).append(","); 
    SqlUtils.appendColumns(stringBuilder, "T", this.dao.getAllColumns()).append(" FROM ");
    stringBuilder.append('"').append(this.dao.getTablename()).append('"').append(" T");
    if (paramK != null) {
      stringBuilder.append(" WHERE ");
      assertEquals(1, (this.dao.getPkColumns()).length);
      stringBuilder.append(this.dao.getPkColumns()[0]).append("=");
      DatabaseUtils.appendValueToSql(stringBuilder, paramK);
    } 
    String str = stringBuilder.toString();
    Cursor cursor = this.db.rawQuery(str, null);
    assertTrue(cursor.moveToFirst());
    b = 0;
    while (true) {
      if (b < paramInt) {
        try {
          assertEquals(paramString, cursor.getString(b));
          b++;
        } catch (RuntimeException runtimeException) {
          cursor.close();
          throw runtimeException;
        } 
        continue;
      } 
      if (paramK != null)
        assertEquals(1, cursor.getCount()); 
      return cursor;
    } 
  }
  
  protected void runLoadPkTest(int paramInt) {
    null = nextPk();
    T t = createEntity(null);
    this.dao.insert(t);
    Cursor cursor = queryWithDummyColumnsInFront(paramInt, "42", null);
    try {
      assertEquals(null, this.daoAccess.readKey(cursor, paramInt));
      return;
    } finally {
      cursor.close();
    } 
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    for (Property property : this.daoAccess.getProperties()) {
      if (property.primaryKey) {
        if (this.pkColumn != null)
          throw new RuntimeException("Test does not work with multiple PK columns"); 
        this.pkColumn = property;
      } 
    } 
    if (this.pkColumn == null)
      throw new RuntimeException("Test does not work without a PK column"); 
  }
  
  public void testCount() {
    this.dao.deleteAll();
    assertEquals(0L, this.dao.count());
    this.dao.insert(createEntityWithRandomPk());
    assertEquals(1L, this.dao.count());
    this.dao.insert(createEntityWithRandomPk());
    assertEquals(2L, this.dao.count());
  }
  
  public void testDelete() {
    K k = nextPk();
    this.dao.deleteByKey(k);
    T t = createEntity(k);
    this.dao.insert(t);
    assertNotNull(this.dao.load(k));
    this.dao.deleteByKey(k);
    assertNull(this.dao.load(k));
  }
  
  public void testDeleteAll() {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: iconst_0
    //   9: istore_2
    //   10: iload_2
    //   11: bipush #10
    //   13: if_icmpge -> 33
    //   16: aload_1
    //   17: aload_0
    //   18: invokevirtual createEntityWithRandomPk : ()Ljava/lang/Object;
    //   21: invokeinterface add : (Ljava/lang/Object;)Z
    //   26: pop
    //   27: iinc #2, 1
    //   30: goto -> 10
    //   33: aload_0
    //   34: getfield dao : Lorg/greenrobot/greendao/AbstractDao;
    //   37: aload_1
    //   38: invokevirtual insertInTx : (Ljava/lang/Iterable;)V
    //   41: aload_0
    //   42: getfield dao : Lorg/greenrobot/greendao/AbstractDao;
    //   45: invokevirtual deleteAll : ()V
    //   48: lconst_0
    //   49: aload_0
    //   50: getfield dao : Lorg/greenrobot/greendao/AbstractDao;
    //   53: invokevirtual count : ()J
    //   56: invokestatic assertEquals : (JJ)V
    //   59: aload_1
    //   60: invokeinterface iterator : ()Ljava/util/Iterator;
    //   65: astore_1
    //   66: aload_1
    //   67: invokeinterface hasNext : ()Z
    //   72: ifeq -> 109
    //   75: aload_1
    //   76: invokeinterface next : ()Ljava/lang/Object;
    //   81: astore_3
    //   82: aload_0
    //   83: getfield daoAccess : Lorg/greenrobot/greendao/InternalUnitTestDaoAccess;
    //   86: aload_3
    //   87: invokevirtual getKey : (Ljava/lang/Object;)Ljava/lang/Object;
    //   90: astore_3
    //   91: aload_3
    //   92: invokestatic assertNotNull : (Ljava/lang/Object;)V
    //   95: aload_0
    //   96: getfield dao : Lorg/greenrobot/greendao/AbstractDao;
    //   99: aload_3
    //   100: invokevirtual load : (Ljava/lang/Object;)Ljava/lang/Object;
    //   103: invokestatic assertNull : (Ljava/lang/Object;)V
    //   106: goto -> 66
    //   109: return
  }
  
  public void testDeleteByKeyInTx() {
    arrayList = new ArrayList();
    for (byte b = 0; b < 10; b++)
      arrayList.add(createEntityWithRandomPk()); 
    this.dao.insertInTx(arrayList);
    ArrayList<Object> arrayList1 = new ArrayList();
    arrayList1.add(this.daoAccess.getKey(arrayList.get(0)));
    arrayList1.add(this.daoAccess.getKey(arrayList.get(3)));
    arrayList1.add(this.daoAccess.getKey(arrayList.get(4)));
    arrayList1.add(this.daoAccess.getKey(arrayList.get(8)));
    this.dao.deleteByKeyInTx(arrayList1);
    assertEquals((arrayList.size() - arrayList1.size()), this.dao.count());
    for (ArrayList<T> arrayList : arrayList1) {
      assertNotNull(arrayList);
      assertNull(this.dao.load(arrayList));
    } 
  }
  
  public void testDeleteInTx() {
    arrayList = new ArrayList();
    for (byte b = 0; b < 10; b++)
      arrayList.add(createEntityWithRandomPk()); 
    this.dao.insertInTx(arrayList);
    ArrayList arrayList1 = new ArrayList();
    arrayList1.add(arrayList.get(0));
    arrayList1.add(arrayList.get(3));
    arrayList1.add(arrayList.get(4));
    arrayList1.add(arrayList.get(8));
    this.dao.deleteInTx(arrayList1);
    assertEquals((arrayList.size() - arrayList1.size()), this.dao.count());
    for (ArrayList<T> arrayList : (Iterable<ArrayList<T>>)arrayList1) {
      Object object = this.daoAccess.getKey(arrayList);
      assertNotNull(object);
      assertNull(this.dao.load(object));
    } 
  }
  
  public void testInsertAndLoad() {
    K k = nextPk();
    T t = createEntity(k);
    this.dao.insert(t);
    assertEquals(k, this.daoAccess.getKey(t));
    k = (K)this.dao.load(k);
    assertNotNull(k);
    assertEquals(this.daoAccess.getKey(t), this.daoAccess.getKey(k));
  }
  
  public void testInsertInTx() {
    this.dao.deleteAll();
    ArrayList<T> arrayList = new ArrayList();
    for (byte b = 0; b < 20; b++)
      arrayList.add(createEntityWithRandomPk()); 
    this.dao.insertInTx(arrayList);
    assertEquals(arrayList.size(), this.dao.count());
  }
  
  public void testInsertOrReplaceInTx() {
    this.dao.deleteAll();
    ArrayList<T> arrayList1 = new ArrayList();
    ArrayList<T> arrayList2 = new ArrayList();
    for (byte b = 0; b < 20; b++) {
      T t = createEntityWithRandomPk();
      if (b % 2 == 0)
        arrayList1.add(t); 
      arrayList2.add(t);
    } 
    this.dao.insertOrReplaceInTx(arrayList1);
    this.dao.insertOrReplaceInTx(arrayList2);
    assertEquals(arrayList2.size(), this.dao.count());
  }
  
  public void testInsertOrReplaceTwice() {
    T t = createEntityWithRandomPk();
    long l1 = this.dao.insert(t);
    long l2 = this.dao.insertOrReplace(t);
    if ((this.dao.getPkProperty()).type == Long.class)
      assertEquals(l1, l2); 
  }
  
  public void testInsertTwice() {
    T t = createEntity(nextPk());
    this.dao.insert(t);
    try {
      this.dao.insert(t);
      fail("Inserting twice should not work");
    } catch (SQLException sQLException) {}
  }
  
  public void testLoadAll() {
    this.dao.deleteAll();
    ArrayList<T> arrayList = new ArrayList();
    for (byte b = 0; b < 15; b++)
      arrayList.add(createEntity(nextPk())); 
    this.dao.insertInTx(arrayList);
    List list = this.dao.loadAll();
    assertEquals(arrayList.size(), list.size());
  }
  
  public void testLoadPk() {
    runLoadPkTest(0);
  }
  
  public void testLoadPkWithOffset() {
    runLoadPkTest(10);
  }
  
  public void testQuery() {
    this.dao.insert(createEntityWithRandomPk());
    K k = nextPk();
    this.dao.insert(createEntity(k));
    this.dao.insert(createEntityWithRandomPk());
    String str = "WHERE " + this.dao.getPkColumns()[0] + "=?";
    List list = this.dao.queryRaw(str, new String[] { k.toString() });
    assertEquals(1, list.size());
    assertEquals(k, this.daoAccess.getKey(list.get(0)));
  }
  
  public void testReadWithOffset() {
    null = nextPk();
    T t = createEntity(null);
    this.dao.insert(t);
    Cursor cursor = queryWithDummyColumnsInFront(5, "42", null);
    try {
      Object object = this.daoAccess.readEntity(cursor, 5);
      assertEquals(null, this.daoAccess.getKey(object));
      return;
    } finally {
      cursor.close();
    } 
  }
  
  public void testRowId() {
    boolean bool;
    T t1 = createEntityWithRandomPk();
    T t2 = createEntityWithRandomPk();
    if (this.dao.insert(t1) != this.dao.insert(t2)) {
      bool = true;
    } else {
      bool = false;
    } 
    assertTrue(bool);
  }
  
  public void testSave() {
    if (checkKeyIsNullable()) {
      this.dao.deleteAll();
      T t = createEntity((K)null);
      if (t != null) {
        this.dao.save(t);
        this.dao.save(t);
        assertEquals(1L, this.dao.count());
      } 
    } 
  }
  
  public void testSaveInTx() {
    if (checkKeyIsNullable()) {
      this.dao.deleteAll();
      ArrayList<T> arrayList1 = new ArrayList();
      ArrayList<T> arrayList2 = new ArrayList();
      for (byte b = 0; b < 20; b++) {
        T t = createEntity((K)null);
        if (b % 2 == 0)
          arrayList1.add(t); 
        arrayList2.add(t);
      } 
      this.dao.saveInTx(arrayList1);
      this.dao.saveInTx(arrayList2);
      assertEquals(arrayList2.size(), this.dao.count());
    } 
  }
  
  public void testUpdate() {
    this.dao.deleteAll();
    T t = createEntityWithRandomPk();
    this.dao.insert(t);
    this.dao.update(t);
    assertEquals(1L, this.dao.count());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/test/AbstractDaoTestSinglePk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */