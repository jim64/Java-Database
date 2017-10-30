// Simple class to print to screen
// has no need to know of other classesjust prints a single
// string or an array of strings.

import java.util.Scanner;

public class Screen  {
 
    private int colWidth[];
    
    
    //simple wrapper for println functions
    public void print(String s)  {
      System.out.print(s);    
    }
    
    public void printNL(String s)  {
      System.out.println(s);
    }
    
    //prints string to screen. Adjusts spacing by column
    //width to ensure consistency on page
    public void printRecord(String...s)  {

      int cnt = 0;
      int i;
           
      for(i = 0; i < s.length; i++)  {   
        print(s[i]);
        
        while(cnt+s[i].length() < colWidth[i]+1)  {
          print(" ");
          cnt++;
        }
        print("|");
        cnt = 0;
      }
      print("\n");
      printLines();
      
    }
    
    //prints both table name and full table with correct 
    //spacing
    public void printFullTable(String name, String[][]s) {
    
      print(name);
      printNL("");
      printTable(s);
    
    }
    
    //prints table without name
    public void printTable(String[][] s)  {
    
      setColWidth(s);    
     
      for(int i = 0; i < s.length; i++)  {
        if(i == 1) {
          printNL("");
          printLines();
        }
        printRecord(s[i]);
      }
      print("\n\n");
      
    }
    
    //sets each column width to ensure any length of string
    //will fit
    private void setColWidth(String[][]s)  {
 
      colWidth = new int[s[0].length];
      int cnt = 0;
      
      for(int i = 0; i < s.length; i++)  {    
        for(int j = 0; j < s[i].length; j++)  {
          if(s[i][j].length() > colWidth[j])  {
            colWidth[j] = s[i][j].length();
            cnt++;
          }
        }
      }

    }
    
    //prints simple array. Differs from printing record
    //as not designed to print record and, therefore, no 
    //need to use columnwidth for formatting
    public void printStrArr(String...str)   {
    
      for(String s : str)
        print(s+ " ");
      printNL("");
      
    }
    
    //prints - and | to produce formatting
    private void printLines()  {
    
      int cnt = 0;
      
      for(int i = 0; i < colWidth.length; i++)  {
        while(cnt <= colWidth[i])  {
          print("-");
          cnt++;
        }
        print("|");
        cnt = 0;
      }
      printNL("");
      
    }
    
    //gets user input
    public String getInput()  {
    
      Scanner input = new Scanner(System.in);
      print(">> ");
      String text = input.nextLine();
     
      return text;
      
    }
    
    private void test()  {
    
      String[][] s = {{"1","2","3"},{"4","5","6"},
                      {"7","8","9"}};
      printFullTable("test",s);
      String input = getInput();
      print(input);  
      printNL("");
      assert(colWidth[0] == 1);
      assert(colWidth[1] == 1);
      assert(colWidth[2] == 1);
      printRecord(s[0]);
      printTable(s);
    
    }
      
    //testing prints to screen and takes input from screen
    //to ensure functionality.
    
    public static void main(String[] args)  {
      boolean testing = false;
      assert(testing = true);
      if(testing)  {
        Screen s = new Screen();
        s.test();
      }

    }

}
