//class to hold multiple tables within a database
//holds tables as a hash table using the table name
//as a key

//dependencies: as top structure knows about table,
//files and screen

import java.util.*;

public class Database  {
  private Map<String, Tables> database;
  private String name;
  
  Database(String name)  {
  
    this.name = name;
    this.database = new HashMap<String, Tables>();
    
  }
  
  //allows meta-data: access to list of table names
  public String[] getTableNames()  {
  
    Set<String> names = database.keySet();
  
    String[] temp = names.toArray(new String[names.size()]);

    return temp;
        
  } 

  
  //checks a table is in the database
  public boolean inDatabase(String name)  {
  
    if(database.containsKey(name)) return true;
    
    return false;
  
  }
  
  //allows meta-data: access to table columns
  public String[] getColumns(Tables t)  {
  
    String[] temp = t.getAllAtt();
    return temp;
  
  }
    
  
  //ensures that foreign key tables are set correctly 
  //if present and insert table to database
  public boolean insertTable(Tables t)  {
  
    if(t.checkFKSet() == true)  {
      Tables temp = database.get(t.getFKTable());
      if(temp == null) return false;
      else {
        temp.setIsFK(t.getTableName());
        database.put(t.getTableName(),t);
        return true;
      }
    }
    else  database.put(t.getTableName(),t);
    
    return true;
    
  }
  
  //returns copy to ensure private fields not accessible
  public Tables getTable(String name)  {
  
    Tables temp;
    
    if(inDatabase(name) == false) temp = null;
    else temp = database.get(name);
    
    return temp;
    
  }
  
  //removes table from database, ensures that not deleted 
  //if foreign key for another table. Tables that provide FK
  //for others must be deleted first
  public boolean deleteTable(String name)  {
  
    if(!database.containsKey(name)) return false;
    
    if(database.get(name).checkIsFK() == true)  {
      if(database.get(database.get(name).getFKforName()) 
         != null)
        return false;
    }
      
    database.remove(name);
    /*
    This is not working so commented out. The boolean is 
    returning as true but not actually deleting file
    Files f = new Files();
    if(f.deleteFile(name) == false) System.out.println("no");
    */
    return true;
    
  }
  
  //return size of database in terms of how many tables
  public int getNoTablesInDB()  {
  
    int temp = database.size();
    return temp;
    
  }
  
  //methods below wrappers to allow files use from 
  //interaction class so interaction only deals with
  //database class.
  public boolean loadFile(String name)  {
  
    Files f = new Files();
    Tables t = f.readFile(name);
    
    if(t == null) return false;
    if(insertTable(t) == false) return false;
    
    return true;
  
  }
  
  public boolean writeFile(String name)  {
  
    Tables t = getTable(name);
    Files f = new Files();
    
    if(f.writeFile(t, false) == false) return false;
    
    return true;
    
  }
  
  
  private void test()  {

    assert(name.equals("new"));
    Tables t = new Tables();
    t.createTable("one", 2, 1, "1", "2");
    assert(insertTable(t) == true);
    assert(database.size() == 1);
    assert(getNoTablesInDB() == 1);
    Tables t2 = new Tables();
    t2.createTable("two", 2, 1, "1", "2");
    assert(insertTable(t2) == true);
    assert(database.size() == 2);
    assert(getNoTablesInDB() == 2);
    Tables t3 = getTable("one");
    assert(database.size() == 2);
    String[] names = getTableNames();
    assert(names.length == 2);
    assert(names[0].equals("one"));
    assert(names[1].equals("two"));
    assert(inDatabase("one") == true);
    assert(inDatabase("three") == false);
    String[] cols = getColumns(t2);
    assert(cols[0].equals("1"));
    assert(cols[1].equals("2"));
    assert(getNoTablesInDB() == 2);
    assert(deleteTable("one") == true);
    assert(deleteTable("one") == false);
    assert(getNoTablesInDB() == 1);
    assert(deleteTable("two") == true);
    assert(deleteTable("two") == false);
    assert(getNoTablesInDB() == 0);
    //test methods below in place to test interactions with
    // screen and files class
    Database d = new Database("Animals and Owners");
    assert(d.loadFile("owners") == true);
    assert(d.loadFile("animals") == true);
    assert(d.writeFile("animals") == true);
    assert(d.writeFile("owners") == true);
    assert(d.deleteTable("owners") == false);
    assert(d.deleteTable("animals") == true);
    assert(d.deleteTable("owners") == true);
    
  }
  
  public static void main(String[] args)  {
    boolean testing = false;
    Database d = new Database("new");
    assert(testing = true);
    if(testing)  {
      d.test();
      
    }   
  } 
  
}
