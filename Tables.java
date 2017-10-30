//class to hold, ammend and access records
//decision not to seperate testing from class as this 
//allows methods to remain private.
//Tables holds a linked has map of records so these can
//be accessed by position inserted and also by string(PK)
//also hold records or whether it has Foreign Keys or
//provides these for elsewhere. 
//Due to the high level of private fields required the class
//needs a lot of differeent get methods to return copies of
//these. 

import java.util.*;

public class Tables {
  private String tableName, FKTable, FKfor;
  private String[] Attributes;
  private Map<String, Record> Table;
  private int key, ForeignKey;
  private boolean FK = false, isFKForOther = false;
   
  //initialises new table. Takes variable length of string 
  //array to allow different sized Tables
  //seperate from a regular constructor to ensure that 
  //instances of table can be created without having to
  //initialise them with data
  public void createTable(String name, int attributes, 
                      int key, String...columnName)  {
    
    this.tableName = name;
    this.Table = new LinkedHashMap<String, Record>();
    Attributes = new String[attributes];
    this.key = key;
    
    System.arraycopy(columnName,0,Attributes,0,attributes);
  
  }
  
  private void testCreate()  {
    
    createTable("T-name", 3, 2, "colOne", "colTwo",
                "colThree");
    assert(Attributes[0] == "colOne");  
    assert(Attributes[1] == "colTwo");
    assert(Attributes[2] == "colThree");
    assert(Attributes.length == 3);
    assert(tableName == "T-name");
    assert(key == 2);
    
  } 
  
  //four methods below return copies of private fields
  //for use outside of class

  public String getTableName()  {
    
    String s = tableName;
    return s;
  
  }
  
  public int getRecordsNum()  {
  
    int temp = Table.size();
    return temp;
  
  }
  
  public int getAttributesNum()  {
      
    int temp = Attributes.length;
    return temp;
  
  }
  
  public String getAttributeName(int num)  {
  
    String temp = Attributes[num];
    return temp;
    
  }
  
  //returns copy of all attribute names
  public String[] getAllAtt()  {
  
    String[] s = new String[Attributes.length];
    
    for(int i = 0; i < Attributes.length; i++)  {
      s[i] = Attributes[i];
    }
    return s;
    
  }
  
  //returns column position of the key for table
  public int getKey()  {
  
    int temp = key;
    return temp;
  
  }
  
 
  //coverted to String array so only strings passed out
  //keeps Files.java non-specific class and allows output 
  //to print all Keys from a table if needed.
  //alternatives would have been to pass the set or as an 
  //array list but this would have left other class more
  //type dependent.
  public String[] getAllKeys()  {
  
    Set<String> keys = Table.keySet();
    String[] keyStr = keys.toArray(new String[keys.size()]);

    return keyStr;
  
  }
  
  //ensures all keys are unique
  private boolean checkKeyUnique(String s)  {

    String[] keys = getAllKeys();
    for(String unique : keys)  {
      if(unique.equals(s)) return false;
    }
    
    return true;
  
  }
  
  //tests all above methods
  private void testGetMethods()  {
  
    Tables t = new Tables();
    t.createTable("test", 3, 1, "1", "2", "3");
    assert(t.getTableName().equals("test"));
    assert(t.getRecordsNum() == 0);
    assert(t.getAttributesNum() == 3);
    assert(t.getAttributeName(0).equals("1"));
    assert(t.getAttributeName(1).equals("2"));
    assert(t.getAttributeName(2).equals("3"));
    assert(t.getKey() == 1);
    assert(t.insertRecord("a","b","c") == true);
    //checks key unique
    assert(t.insertRecord("a","b","c") == false);
    assert(t.checkKeyUnique("d") == true);
    assert(t.checkKeyUnique("b") == false);
    String[] s = t.getAllKeys();
    assert(s.length == 1);
    assert(s[0].equals("b"));  
  
  }
  
  //returns -1 if column name not found for checking when 
  //called
  public int getAttPos(String s)  {
    
    for(int i = 0; i < Attributes.length; i++)  {
      if(Attributes[i].equals(s)) 
        return i;
    }
    
    return -1;
    
  }
  
  private void testAttPos()  {
  
    assert(getAttPos("colOne") == 0);
    assert(getAttPos("colTwo") == 1);
    assert(getAttPos("colThree") == 2);
    assert(getAttPos("colNone") == -1);
    assert(getAttPos("colFour") == -1);
    
  }
    
  
  //sets boolean to show it provides the FK for another 
  //table and provides the name of that table
  public void setIsFK(String name)  {
  
    isFKForOther = true;
    FKfor = name;
    
  }
  
  //return name of table that this table is the foreign key 
  //for
  public String getFKforName()  {
  
    String temp = FKfor;
    return temp;
    
  }
  
  //checks whether this provides a FK for another table
  public boolean checkIsFK()  {
  
    boolean temp = isFKForOther;
    return temp;
  
  }
  
  //checks whether this table has a foreign key 
  public boolean checkFKSet()  {
  
    boolean temp = FK;
    return temp;
      
  }
  
  //sets that this table has a foreign key and the name
  //of the table that provides the FK
  public void setFK(int pos, String table)  {
  
    ForeignKey = pos;
    FK = true;
    FKTable = table;

  }
  
  //gets name of table that provides foreign key
  public String getFKTable()  {
  
    String temp = FKTable;  
    return temp;
  
  }
  
  //gets the column number of any foreign key
  public int getFK()  {
  
    int temp = ForeignKey;
    return temp;
  
  }
  
  //tests above FK methods
  private void testFK()  {
  
    setIsFK("test");
    assert(checkIsFK() == true);
    assert(FKfor.equals("test"));
    setFK(2, "anothertest");
    assert(ForeignKey == 2);
    assert(checkFKSet() == true);
    assert(getFKTable().equals("anothertest"));
    assert(getFK() == 2);
  
  }
  
  //allows change of attribute name
  //ensures that the name being changed is not a 
  //FK for another table
  public boolean changeAttName(String oldAtt, 
                               String newAtt)  {
   
    int i = getAttPos(oldAtt);
    
    if(i == -1) return false;
    Attributes[i] = newAtt;
    
    return true;
    
  }
  
  private void testChangeAttName()  {
    
    assert(changeAttName("colOne", "newName") == true);
    assert(changeAttName("Wrong", "stillWrong") == false);
    assert(getAttPos("newName") == 0); 
    assert(changeAttName("newName", "colOne") == true); 
    
  }
  
  //inserts a record - ensuring it is correct length and 
  //that each field set is completed
  //ensures that key is unique before inserting record
  public boolean insertRecord(String...newData)  {
    
    if(newData.length != Attributes.length)  return false;
    
    Record r = new Record(Attributes.length);
      
    for(int i = 0; i < Attributes.length; i++)  {
      if(r.setField(newData[i], i) != true) return false;
    }
    
    String s = r.getField(key);
    
    if(checkKeyUnique(s))  {
      Table.put(s, r);
      return true;
    }
    
    return false;
    
  }
  
  
  private void testInsertRecord()  {
  
    assert(insertRecord("1", "2", "3") == true);
    assert(getRecordsNum() == 1);
    assert(insertRecord("1", "2", "3", "4") == false);
    assert(getRecordsNum() == 1);
    assert(insertRecord("1", "2", "3") == false);
    assert(getRecordsNum() == 1);
    assert(insertRecord("a1", "a2", "a3") == true);
    assert(getRecordsNum() == 2);
    assert(Table.size() == 2);
  
  }
  
  //returns copy of individual record
  public String[] selectRecord(String key)  {
  
    String[] s = new String[Attributes.length];
    
    for(int i = 0; i < Attributes.length; i++)  {
      s[i] = Table.get(key).getField(i);
    }

    return s;
    
  }
    
  //selects multiple records dependent on when they 
  //were inserted so returning first 30 will return
  //first thirty that were inputted
  public String[][] selectMultiRecords(int first, int last)  {
     
    last = adjustFirstLast(first, last);
    if(first > last) last = first;
 
    String[][] r = new String[(last - first)+1][];
    String[] keys = getAllKeys();
    int i = 0;
 
    while(first <= last)  {
      r[i++] = selectRecord(keys[first++]);
    }
    
    return r;
    
  }
    
  private void testSelect()  {

    //test selectRecord
    String[] r = selectRecord("3");
    assert(r[0].equals("1"));
    assert(r[2].equals("3"));
    r = selectRecord("a3");
    assert(r[1].equals("a2"));
    //tests SelectMultiRecords;   
    String[][] test = selectMultiRecords(0,1);
    assert(test[0][0].equals("1"));
    assert(test[0][1].equals("2"));
    assert(test[0][2].equals("3"));   
    assert(test[1][0].equals("a1"));
    assert(test[1][1].equals("a2"));
    assert(test[1][2].equals("a3"));
    
  }
  
  //returns all records as a 2d string array 
  public String[][] returnAllRecords()  {
    
    String[][] tmp = new String[Table.size()][];
    String[] keys = getAllKeys();
    
    for(int i = 0; i < keys.length; i++)  {
      tmp[i] = selectRecord(keys[i]);
    }
    
    return tmp;
    
  }
  
  //returns full table including columns.
  public String[][] returnAllTable()  {
   
    String[][] cols = new String[1][0];
    String[][] recs = returnAllRecords();
    String[][] tmp = new String[Table.size()+1][];  
    cols[0] = getAllAtt();
    
    System.arraycopy(cols,0,tmp, 0, 1);
    System.arraycopy(recs,0,tmp,1,recs.length);

    return tmp;
    
  }
  
  
  private void testReturnMethods()  {
  
    Tables test = new Tables();
    test.createTable("test", 2, 1, "ONE", "TWO");
    assert(test.insertRecord("this", "that") == true);
    assert(test.insertRecord("andThis", "andThat") == true);
    String[][] a = test.returnAllRecords();
    assert(a[0][0].equals("this"));
    assert(a[0][1].equals("that"));
    assert(a[1][0].equals("andThis"));
    assert(a[1][1].equals("andThat"));
    assert(a.length == 2);
    String[][] b = test.returnAllTable();
    assert(b.length == 3);
    assert(b[0][0].equals("ONE"));
    assert(b[0][1].equals("TWO"));    
    assert(b[1][0].equals("this"));
    assert(b[1][1].equals("that"));
    assert(b[2][0].equals("andThis"));
    assert(b[2][1].equals("andThat"));
  
  }
    
  //delete individual record
  public boolean deleteRecord(String key)  {
  
    if(isFKForOther == true) return false;
  
    if(!Table.containsKey(key)) return false;
   
    Table.remove(key);
    return true;
    
  }
  
  //allows deletion of multiple records
  public boolean deleteMultiRecords(int first, int last)  {
  

    if(testParameters(first, last) == false) return false;     
    last = adjustFirstLast(first, last);
   
    String[] keys = getAllKeys();
    
    while(first <= last)  {
      if(deleteRecord(keys[first++]) == false)
        return false;
    }
    
    return true;
  
  }
  
  //tests parameters for multiple selection methods
  private boolean testParameters(int first, int last)  {
  
    if((Table == null) || (Table.isEmpty())) return false;  
    if((first < 0) || (last < 0)) return false; 
    if(last > Table.size()) return false;
    
    return true;
  
  }
  
  private void testDelete()  {
    
    assert(Table.size() == 2);
    assert(deleteRecord("a3") == true);
    assert(Table.size() == 1);
    assert(deleteRecord("3") == true);
    assert(deleteRecord("3") == false);
    assert(deleteRecord("£") == false);
    assert(Table.size() == 0);
    assert(getRecordsNum() == 0);
    assert(insertRecord("1", "2", "3") == true);
    assert(insertRecord("a1", "a2", "a3") == true);
    assert(getRecordsNum() == 2);
    assert(Table.size() == 2);   
    assert(deleteMultiRecords(0, 1) == true);
    assert(deleteMultiRecords(0, 5) == false);
    assert(deleteMultiRecords(0, 1) == false);
    assert(deleteMultiRecords(0, 2) == false);
    assert(deleteMultiRecords(5, 1) == false);
    assert(deleteMultiRecords(-1, 0) == false);
    assert(Table.size() == 0);
    
  }
    
  
  //adjusts first / last for multiple record use
  //to ensure that failures are accounted for
  private int adjustFirstLast(int first, int last)  {
  
    if(last - first > Table.size()) {
      last = (Table.size() - (first+1));
    }
    
    if(last < 0) last = 0;
 
    return last;
  
  }

  //allows a field of the record to be changed. Ensures that
  //if column is a foreign key it is not updated.
  public boolean updateRecord(String Att, String update,
                              String k)  {
    
    if(Att.equals(Attributes[key])) return false;
    if((isFKForOther == true) && (Att.equals(Attributes[ForeignKey])))
      return false;
      
    int j = getAttPos(Att);
    if((j == -1) || (!Table.containsKey(k)))
      return false;
    
    if(Table.get(k).setField(update, j) == false)
      return false;

    return true;
  }
  
  //ensures that the key field is not updated with the same
  //string so that keys remain unique
  public boolean updateMultiRecords(String Att, 
                    String update, int first, int last)  {
                    
    if(testParameters(first, last) == false) return false; 
    if(Att.equals(Attributes[key])) return false;   
  
    last = adjustFirstLast(first, last);

    String[] keys = getAllKeys();
    
    while(first <= last)  {
      if(updateRecord(Att, update, keys[first++]) == false)
        return false;
    }
    
    return true;
                    
  }


  private void testUpdate()  {
  
    assert(updateRecord("colTwo", "testpass", "a3") == true);
    assert(updateRecord("colOne", "testOne", "a3") == true);
    assert(updateRecord("colThree", "testTwo", "a3") == false);
    assert(updateRecord("wrong", "test", "a3") == false);
    assert(updateRecord("colOne", "test", "d") == false);
    String[] test = selectRecord("a3");
    assert(test[1].equals("testpass"));
    assert(test[0].equals("testOne"));
    assert(updateMultiRecords("colFour", "new", 0, 1) == false);
    Tables empty = new Tables();
    assert(empty.updateMultiRecords("one", "two", 0, 2) == false);
    empty.createTable("empty", 2, 1, "1", "2");
    assert(empty.updateMultiRecords("1", "test", 0, 1) == false);
    assert(empty.insertRecord("test1", "test2") == true);
    assert(empty.insertRecord("test2.1", "test2.2") == true);
    assert(empty.updateMultiRecords("nokey", "test", 0, 1) == false);
    assert(empty.updateMultiRecords("1", "new", 0, 1) == true);
    assert(empty.updateMultiRecords("2", "new", 0, 1) == false);
    assert(empty.updateMultiRecords("1", "newnew", 0, 5) == false);
    assert(empty.updateMultiRecords("1", "new", -1, 1) == false);
    assert(empty.selectRecord("test2.2")[0].equals("new"));
    assert(empty.selectRecord("test2")[0].equals("new"));

  }
  
  //allows a new column to be added to table
  //only allows one column at a time to be added
  //then adjusts all records in table so set new
  //field to "null"
  public boolean alterTable (String newAtt, int pos) {
  
    if(pos > Attributes.length+1) return false;
    if(pos <= key) key += 1;
    if(pos <= ForeignKey) ForeignKey += 1;
    
    String[] update = new String[Attributes.length + 1];
    
    for(int i = 0 ,j = 0; i < Attributes.length + 1; i++)  {
      if(i == pos)  update[i] = newAtt;
      else  {
        update[i] = Attributes[j];
        j++;
      }
    }
    
    Attributes = update;
    
    String[] str = getAllKeys();
    for(String s : str)  
      Table.get(s).addField(pos);
    
    return true;
    
  }
  
  private void testAlterTable()  {
  
    Tables table = new Tables();
    table.createTable("new", 2, 0, "one", "two");
    assert(table.Attributes.length == 2);
    assert(table.alterTable("one.five", 1) == true);
    assert(table.Attributes.length == 3);
    assert(table.Attributes[1].equals("one.five"));
    assert(table.Attributes[2].equals("two"));
    assert(table.alterTable("zero", 0) == true);
    assert(table.alterTable("test", 56) == false);
    assert(table.Attributes.length == 4);
    assert(table.Attributes[0].equals("zero"));
    assert(table.Attributes[1].equals("one"));
    assert(table.Attributes[2].equals("one.five"));
    assert(table.Attributes[3].equals("two"));
  
  }
  
  private void test()  {

    testGetMethods();
    testCreate();
    testAttPos();
    testChangeAttName();
    testInsertRecord();
    testSelect();  
    testAlterTable();
    testUpdate();
    testDelete();
    testReturnMethods();
    testFK();

  }
  
  public static void main(String[] args)  {
    boolean testing = false;
    assert(testing = true);
    if(testing)  {
      Tables t = new Tables();
      t.test();
    }
  }

}
  
