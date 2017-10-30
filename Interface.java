//class to allow interaction from the command line
//initiates instance of database and can then load from
//file, create new tables/ records and ammend them
//
//contains long functions as needed to check against 
//user input
//no testing on class as tested by its use at command line
//also mostly calls methods that are tested in their own
//classes.

public class Interface  {

  private void initiate()  {
    
    Screen s = new Screen();
    Database d = new Database("Data");
    s.printNL("Database");
    options(s, d);

  }
  
  //provides access to methods as database level
  //loop continues until "quit" entered. When quit
  //entered writes back database to files
  private void options(Screen s, Database d)  {

    boolean quit = false;
    String name;

    showOptions(s);

    while(quit == false)  {
      String str = s.getInput();     
      if(str.equals("load table")) {
        name = choice(s);
        if(d.loadFile(name) == false) 
          s.printNL("unknown file - or table reliant on "+
          "foreign key");
      }     
      else if(str.equals("create table"))  {
        if(create(d, s) == false) s.printNL("Unable to create"+
        " table");
      }      
      else if(str.equals("show tables"))  {
        if(d.getNoTablesInDB() == 0)  s.printNL("no Tables"+
        " currently in DB");
        else s.printStrArr(d.getTableNames());
      }    
      else if(str.equals("show number"))  {
        s.printNL(Integer.toString(d.getNoTablesInDB()));
      }     
      else if(str.equals("select table"))  {
        if(select(d, s) == false) s.printNL("unable to select"+
        " table");
      }
      else if(str.equals("delete table"))  {
        name = choice(s);
        if(d.deleteTable(name) == false)
          s.printNL("unable to delete: Table not in DB or"+
          " is relied on for a foreign key");
      }
      else if(str.equals("help")) showOptions(s);
      else if(str.equals("quit")) {
        writeBackTables(d);
        quit = true;
      }   
      else s.printNL("unknown command");       
    }

  }
  
  //called multiple times for user choice
  private String choice(Screen s)  {
  
    s.printNL("enter name");
    String name = s.getInput();
    return name;
    
  }
  
  //creates new table. If successful will write to file
  private boolean create(Database d, Screen s)  {
  
    Tables t = new Tables();
    String name = choice(s);
    int cols, key;
   
    s.printNL("Enter number of columns"); 
    try  {
      cols = Integer.parseInt(s.getInput());  
      s.printNL("Enter columns position of primary key");
      key = Integer.parseInt(s.getInput());
      if(key > cols) return false;
    } catch(Exception e)  {
      return false;
    }
   
    if(checkForeignKey(d, t, s) == false)  {
      s.printNL("cannot create table with FK for table"
      +" that doesnt exist");
      return false;
    }
    s.printNL("Enter column names divided by comma");
    String colNames = s.getInput();
    String[] col = colNames.split(",");
    
    if(col.length != cols) return false;
    t.createTable(name, cols, key, col);
    
    if(t != null)  {
      if(enterRows(t, s) == false)  {
        s.printNL("unable to create rows");
      }
    }
    
    if((t == null) || (d.insertTable(t) == false))
      return false;
      
    if(d.writeFile(t.getTableName()) == false) return false;

    return true;   
  
  }
  
  //checks that if setting a foreign key then the table 
  //it applies to already exists.
  private boolean checkForeignKey(Database d,Tables t, Screen s)  {
  
    s.printNL("is there a foreign key? Y/ N");
    String str = s.getInput();
    int FK = 0;
    
    if(str.equals("Y"))  {
      s.print("Enter column position of foreignKey");
      str = s.getInput();
      try{
        FK = Integer.parseInt(str);
      }
      catch(Exception e) { return false; }
      s.print("enter name of table this is FK for:");
      str = s.getInput();
      if(d.inDatabase(str) == false) return false;
      t.setFK(FK, str);
      return true;
    }
    
    return true;
  
  }
  
  //enters new rows into table
  private boolean enterRows(Tables t, Screen s) {
  
    boolean finished = false;
    
    while(finished == false)  {
    
      s.printNL("Enter record with field divided by commas"+
      ": enter done to finish");
      String fields = s.getInput();
      
      if(fields.equals("done")) finished = true;
     
      else  {      
        String[] field = fields.split(",");
        if(field.length != t.getAttributesNum()) return false;
        if(t.insertRecord(field) == false) return false;
      }
      
    }
    
    return true;
    
  }    
  
  //provides access to table level interactions
  //loop continues until return entered
  private boolean select(Database d, Screen s)  {
    
    boolean Return = false;  
    String name = choice(s);
    Tables t = d.getTable(name);
    if(t == null) return false;
    
    selectOptions(s); 
    
    while(Return ==  false)  {
      String str = s.getInput(); 
        
      if(str.equals("show table field")) 
        s.printStrArr(d.getColumns(d.getTable(name)));        
      else if(str.equals("show full table")) 
        s.printTable(d.getTable(name).returnAllTable());
      else if(str.equals("add new record"))  {
        if(enterRows(t, s) == false)
          s.printNL("unable to enter records");
      }   
      else if(str.equals("delete record"))  {
        if(delete(s, t) == false)
          s.printNL("unable to delete: either doesnt exist"+
          "or is foreign key for another table");
      }    
      else if(str.equals("update record"))  {
        if(update(s, t) == false)
          s.printNL("unable to update");
      }    
      else if(str.equals("add columns")) {
        if(addColumns(s, t) == false) 
          s.printNL("unable to add columns");        
      }  
      else if(str.equals("select record")) {
        s.printNL("enter record to select");
        str = s.getInput();
        s.printRecord(t.selectRecord(str));
      } 
      else if(str.equals("return")) {
        Return = true;
      }
      else if(str.equals("help")) selectOptions(s);
      else s.printNL("unknown command");     
    }
  
    return true;
    
  }
  
  //adds columns to table selected
  private boolean addColumns(Screen s, Tables t)  {
  
    s.printNL("enter new attribute name");
    String str = s.getInput();
    s.printNL("enter new position as number");
    String temp = s.getInput();
    
    int a = 0;
    try  {
      a = Integer.parseInt(temp);
    }
    catch (Exception e){ return false; }
    
    if(t.alterTable(str, a) == false) 
      return false;
    
    return true;
    
  }
  
  //allows update to single row or multiple
  private boolean update(Screen s, Tables t)  {
  
    s.printNL("enter field to update");
    String field = s.getInput();    
    s.printNL("enter new value");
    String update = s.getInput();
    s.printNL("update single or mulitple records? enter S or M");
    String str = s.getInput();
    
    if(str.equals("S"))  {
      s.printNL("choose record to update");
      str = s.getInput();
      if(t.updateRecord(field, update, str) == false)
        return false;
      return true;
    }
    
    else if(str.equals("M"))  {
      s.printNL("enter start of update");
      str = s.getInput();
      int start = 0, end = 0;
      try  {
        start = Integer.parseInt(str);
        s.printNL("enter position of end of delete selection");
        str = s.getInput();
        end = Integer.parseInt(str);
      }
      catch(Exception e) {return false;}
      
      if(t.updateMultiRecords(field,update,start, end) == false)
        return false;
     
      return true;
    }
    
    else return false;
      
  }
  
  //deletes a row or multiple rows
  private boolean delete(Screen s, Tables t)  {
  
    s.printNL("delete multiple or single? Enter M or S");
    String str = s.getInput();
    
    if(str.equals("S")) {
      s.printNL("choose record to delete");
      str = s.getInput();
      if(t.deleteRecord(str) == false)  return false;
      return true;
    }
    
    else if(str.equals("M"))  {
      s.printNL("enter position of start of deletion");
      str = s.getInput();
      int start = 0, end = 0;
      try  {
        start = Integer.parseInt(str);
        s.printNL("enter position of end of delete selection");
        str = s.getInput();
        end = Integer.parseInt(str);
      }
      catch(Exception e) {return false;}
      
      if(t.deleteMultiRecords(start, end) == false)
        return false;
        
      return true;
    }
    
    else return false;
  
  }
  
  //select options that can be called by entering "help"
  private void selectOptions(Screen s)  {
    
    s.printNL("\n-show table field\n-show full table\n"+
    "-add new record\n-delete record\n-update record\n"+
    "-add columns\n-select record\n-return"+
    "\n-help\n");
  
  }
  

      
  //presents options. can be called with "help" instruction
  private void showOptions(Screen s)  {
  
    s.printNL("\noptions:\n-load table\n-create table\n"+
    "-show tables\n-show number\n-select table\n"+
    "-delete table\n-help\n-quit\n"); 
    
  }
  
    
  //saves all tables to file.
  private void writeBackTables(Database d)  {
  
    String[] names = d.getTableNames();
    for(String n : names) 
      d.writeFile(n);
      
  }

  public static void main(String[] args)  {
     
    Interface i = new Interface();
    i.initiate();
    
  }


}
