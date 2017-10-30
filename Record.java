//Simple class for each record in DB. Each field stored as 
//a string in an array. 

public class Record  {
  private String[] data;

  //constructor to set new string array to specific length
  //ensures that size is greater than 0 before setting
  Record (int i)  {
  
    if(i > 0)  {
      data = new String[i];
    }
    
  }

  //returns amount of strings in record
  public int recordLength()  {
  
    int i = data.length;
    return i;
    
  }

  //gets specific field
  public String getField(int i)  {
  
    if(i > data.length || i < 0) return "field doesnt exist";
    String str = data[i];
    return str;
    
  }

  //sets specific field
  public boolean setField(String s, int i)  {
  
    if(i < data.length && i >= 0)  {
      data[i] = s;
      return true;
    }
    return false;
    
  }
  
  //adds new fields - for use when table size is alterated
  //new column set to "null" - can then be ammended by 
  //set field method
  public void addField(int i)  {
  
    String[] temp = new String[data.length+1];
    
    for(int j = 0, l = 0; j < temp.length; j++)  {
      if(j == i)  temp[i] = "null";
      else  temp[j] = data[l++];
    }
    
    data = temp;
    
  }
 
  private void test()  {
  
    setField("pete", 0);
    setField("20/03/16", 1);
    assert(getField(0).equals("pete"));
    assert(getField(1).equals("20/03/16"));
    assert(getField(-1).equals("field doesnt exist"));
    assert(getField(4).equals("field doesnt exist"));
    assert(recordLength() == 2);
    Record r2 = new Record(4);
    assert(r2.setField("one", 0) == true);
    assert(r2.setField("two", 1) == true);
    assert(r2.setField("three", 2) == true);
    assert(r2.setField("four", 3) == true);
    assert(r2.setField("five", 4) == false);
    assert(r2.setField("minus one", -1) == false);
    assert(r2.recordLength() == 4);
    assert(r2.getField(0).equals("one"));
    assert(r2.getField(1).equals("two"));
    assert(r2.getField(2).equals("three"));
    assert(r2.getField(3).equals("four"));
    r2.addField(4);
    assert(r2.getField(4).equals("null"));
    r2.addField(0);
    assert(r2.recordLength() == 6);
    assert(r2.getField(0).equals("null"));
    Record r3 = new Record(-1);
    assert(r3.data == null);
    Record r4 = new Record(0);
    assert(r4.data == null);
    
  }

  public static void main(String[] args)  {
    boolean testing = false;
    assert(testing = true);  
    if(testing)  {
      Record r = new Record(2);
      r.test();
    }
    
  }

}

  
