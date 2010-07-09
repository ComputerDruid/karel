//Torbert, 4/16/2002, FCPS
//modified from EasyWriter by Litvin and TextIO by Eck
   package edu.fcps;
   import java.io.*;
   public class OutWriter
   {
      public static final int LEFT = 1;
      public static final int CENTER = 2;
      public static final int RIGHT = 3;
   
      private String myFileName;
      private PrintWriter myOutFile;
      private char myFillChar = ' ';
      private int myJustification = RIGHT;
      public OutWriter() 
      {
         setConsoleWriter();
      }
      public OutWriter(String fileName)
      {
         open(fileName);
      }
      public OutWriter(String fileName, boolean x) //append mode
      {
         open(fileName, x);
      }
      public void open(String fileName) 
      {
         myFileName = fileName;
         try
         {
            myOutFile = new PrintWriter(new FileWriter(fileName), true);
         }
            catch (IOException e)
            {
               e.printStackTrace();
            }
      }
      public void open(String fileName, boolean x) //append mode
      {
         myFileName = fileName;
         try 
         {
            myOutFile = new PrintWriter(new FileWriter(fileName, x), true);
         }
            catch (IOException e)
            {
               e.printStackTrace();
            }
      }
      public void close()
      {
         if (myFileName == null)
            return;
         myOutFile.close();
         setConsoleWriter();
      }
      private void setConsoleWriter()
      {
         myFileName = null;
         myOutFile = new PrintWriter(System.out, true);
      }
      public void fill(char x)
      {
         myFillChar = x;
      }
      public void fill(String x)
      {
         if(x != null)
            myFillChar = x.charAt(0);
      }
      public void justify(int x)
      {
         if(1 <= x && x <= 3)
            myJustification = x;
      }
   
   	//methods for strings
      public void print(String s) 
      {
         myOutFile.print(s);
         myOutFile.flush();
      }
      public void println(String s) 
      {
         myOutFile.println(s);
      }
      public void put(String str, int n)
      {
         int x;
         switch(myJustification)
         {
            case LEFT:
               print(str);
               for(x = str.length(); x < n; x++)
                  print("" + myFillChar);
               break;
            case CENTER:
               int temp = n - str.length();
               for(x = 0; x < temp / 2; x++)
                  print("" + myFillChar);
               print(str);
               for(; x < temp; x++)
                  print("" + myFillChar);
               break;
            case RIGHT:
               for(x = str.length(); x < n; x++)
                  print("" + myFillChar);
               print(str);
               break;
         }
      }
      public void putln(String str, int n)
      {
         put(str, n);
         println();
      }
   
   	//overloaded methods for Object, char, int, double, boolean
      public void print(Object obj)
      {
         print("" + obj);
      }
      public void print(char x)
      {
         print("" + x);
      }
      public void print(int x) 
      {
         print("" + x);
      }
      public void print(double x)
      {
         print("" + x);
      }
      public void print(boolean x)
      {
         print("" + x);
      }
      public void println()
      {
         println("");
      }
      public void println(Object obj)
      {
         println("" + obj);
      }
      public void println(char x)
      {
         println("" + x);
      }
      public void println(int x)
      {
         println("" + x);
      }
      public void println(double x)
      {
         println("" + x);
      }
      public void println(boolean x)
      {
         println("" + x);
      }
      public void put(char x, int n)
      {
         put("" + x, n);
      }
      public void put(int x, int n) 
      {
         put("" + x, n);
      }
      public void put(double x, int n)
      {
         put("" + x, n);
      }
      public void put(boolean x, int n)
      {
         put("" + x, n);
      }
      public void putln(char x, int n)
      {
         putln("" + x, n);
      }
      public void putln(int x, int n)
      {
         putln("" + x, n);
      }
      public void putln(double x, int n)
      {
         putln("" + x, n);
      }
      public void putln(boolean x, int n)
      {
         putln("" + x, n);
      }
   }