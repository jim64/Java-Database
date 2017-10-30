//class to read and write tables to text files for 
//storage purposes. ¬ character chosen as divider bewteen 
//strings in files due to its low use.
//exceptions to read / write caught as returning false so
//files has no need to output to screen - these can then be 
//dealt with were methdos are called.

import java.io.*;
import java.util.*;


public class Files  {

  //Boolean new allows files to be written over or 
  //added to. Catches exceptions as false so that errors
  //can be passed back to methods that called this
  public boolean writeFile(Tables t, boolean New)  {

    int AttNum = t.getAttributesNum();

    try {
      FileWriter writer = new FileWriter(t.getTableName()
      +".txt", New);
      BufferedWriter bWriter = new BufferedWriter(writer);
      int i; int j;
      
      if(New == false)  
        t = writeCols(t, bWriter, AttNum);
      
      if(writeRecords(t, bWriter, AttNum) == false)
        return false;
      
      bWriter.close();
      writer.close();
    } catch (IOException e)  {
       return false;
    }
    
    return true;
      
  }
  
  //writes rows / records to file
  //¬ chosen as the marker between record fields due to its
  //low common use. This allows commas and new lines to be
  //used within fields
  private boolean writeRecords(Tables t, BufferedWriter b,
                               int AttNum)  {
  
    int i, j;
    String[] keys = t.getAllKeys();
    String[] s;
    
    try  {
      for(j = 0; j < keys.length; j++)  {
        s = t.selectRecord(keys[j]);
        for(i = 0; i < AttNum; i++)  { 
          b.write(s[i]+"¬");
        }
        b.write("\n");
      }
    }
    catch (IOException e)  {
      return false;
    }
    
    return true;
     
  }  
  
  //writes columns to file, ensure primary key is registered
  //at the right place. Catches exceptions by setting table
  //to null so this can be checked against when methods 
  //called from  
  private Tables writeCols(Tables t,BufferedWriter b,
                           int AttNum)  {
   
    int i;
    int key = t.getKey();
    int FK = -1;
    
    if(t.checkFKSet() == true) FK = t.getFK();
    
    try  {
      for(i = 0; i < AttNum; i++)  {              
        b.write(t.getAttributeName(i)+"¬");
        if(i == key) b.write("PK¬");
        if(i == FK)  {  
          b.write("FK¬");
          b.write(t.getFKTable()+"¬");
        }
      }
      b.write("\n");
    }
    catch (IOException e)  {
      t = null;
    }

    return t;

  }
      
  //reads tables from files. Ensures that top row is 
  //processed as columns. As described above exceptions 
  //catched by returning a null table.
    
  public Tables readFile(String s)  {
    
    Tables temp = new Tables();
    boolean columns = true;
     
    try {
      FileReader reader = new FileReader(s+".txt");
      BufferedReader bufferedReader = new BufferedReader(reader);
 
      String line;
 
      while((line = bufferedReader.readLine()) != null) {
        temp = processLine(line, temp, s, columns);
        if(columns == true) columns = false;
      }
      bufferedReader.close();
      reader.close();
 
    } catch (IOException e) {
        temp = null;
    }
      
    return temp;
      
  }
    
    
  //method set to static to allow access to for String
  //without having to make another new version of the string
  //process columns line differently to ensure sets primary
  //key to the right position without putting PK as a string
  //into the table
  private static Tables processLine(String s, Tables t,
                         String name, boolean columns)  {
    
    int PK = -1, FK = -1;
    String temp = null;
 
    if(columns == true)  {
      ArrayList<String> items = new ArrayList<String>
                        (Arrays.asList(s.split("¬"))); 
      PK = (items.indexOf("PK") - 1);
      items.remove("PK");
      if(items.contains("FK"))  {
        FK = (items.indexOf("FK") -1 );
        temp = items.remove(items.indexOf("FK") + 1);
        items.remove("FK");
        
      }
      String[] word = items.toArray(new String[items.size()]);
      t.createTable(name, word.length, PK, word);
      if(FK != -1)  {
        t.setFK(FK, temp);      
      }
    }
    else  {
      String[] words = s.split("¬");
      t.insertRecord(words); 
    }
    
    return t;
  
  }
    
  //methods such as process line not explicitly 
  //tested as they are tested by the calls below to their
  //parent methods. I.e. process line tested by readfile
  //resetFile method resets testfile to original state
  private void test()  {
  
    resetFile();
    Tables t = readFile("testfile");
    assert(t != null);
    assert(t.getRecordsNum() == 3);
    assert(t.getAttributeName(0).equals("one"));
    assert(t.getAttributeName(1).equals("two"));
    assert(t.getAttributeName(2).equals("three"));
    assert(t.getAttributeName(3).equals("four"));
    assert(t.selectRecord("1")[0].equals("1"));
    assert(t.selectRecord("1")[3].equals("4"));
    assert(t.selectRecord("a1")[1].equals("a2"));
    assert(t.selectRecord("a1")[2].equals("a3"));
    assert(t.selectRecord("b1")[3].equals("b4"));
    assert(t.selectRecord("b1")[1].equals("b2"));
    assert(writeFile(t, false) == true);
    //tests that writeFile has overwitten file with same data
    Tables t2 = readFile("testfile");
    assert(t2 != null);
    assert(t2.getRecordsNum() == 3);
    assert(t2.getAttributeName(0).equals("one"));
    assert(t2.getAttributeName(1).equals("two"));
    assert(t2.getAttributeName(2).equals("three"));
    assert(t2.getAttributeName(3).equals("four"));
    assert(t2.selectRecord("1")[0].equals("1"));
    assert(t2.selectRecord("1")[3].equals("4"));
    assert(t2.selectRecord("a1")[1].equals("a2"));
    assert(t2.selectRecord("a1")[2].equals("a3"));
    assert(t2.selectRecord("b1")[3].equals("b4"));
    assert(t2.selectRecord("b1")[1].equals("b2"));
    assert(writeFile(t2, true) == true);
    //tests that writeFile has added to file
    //and table has remained at size 3
    Tables t3 = readFile("testfile");
    assert(t3 != null);
    assert(t3.getRecordsNum() == 3);
    assert(t3.getAttributeName(0).equals("one"));
    assert(t3.getAttributeName(1).equals("two"));
    assert(t3.getAttributeName(2).equals("three"));
    assert(t3.getAttributeName(3).equals("four"));
    assert(t3.selectRecord("1")[0].equals("1"));
    assert(t3.selectRecord("a1")[1].equals("a2"));
    assert(t3.selectRecord("b1")[2].equals("b3"));
    assert(t3.selectRecord("1")[3].equals("4"));
    assert(t3.selectRecord("a1")[2].equals("a3"));
    assert(t3.selectRecord("b1")[1].equals("b2"));
    assert(t3.selectRecord("b1")[0].equals("b1"));
     
  }
   
  //class to adi testing by resetting the testfile back
  //to original state 
  private void resetFile()  {
        
   try  {
    FileWriter writer = new FileWriter("testfile.txt", false);
      
    writer.write("one¬PK¬two¬three¬four\na1¬a2¬a3¬a4\n"+
                   "1¬2¬3¬4\nb1¬b2¬b3¬b4\n");
    writer.close();
      
    } catch (IOException e)  {
        System.out.println("Testing failed. Cannot write"+
        " resetFile");
    }
    
  }
  
  //this is not working - it is returning true but not 
  //actually deleting the file when called from database 
  public boolean deleteFile(String s)  {
  
    File f = new File(s+"txt");

    try {
     f.delete();
    }
    catch(Exception e) {return false;}
    return true;
  
  }
    
  public static void main(String[] args)  {
    boolean testing = false;

    assert(testing = true);
    if(testing)  {
      Files f = new Files();
      f.test();
    }
  }
     
}
