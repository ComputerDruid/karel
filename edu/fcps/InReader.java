//Torbert, 7/23/2002, FCPS
//modified from EasyReader by Litvin and TextIO by Eck
   package edu.fcps;
   import java.io.*;
   import java.util.StringTokenizer;
   public class InReader
   {
      private String myFileName, current, next;
      private StringTokenizer buffer;
      private BufferedReader myInFile;
      private boolean isEOF, fileNotFound, nonToken, console;
      public InReader()
      {
         setConsoleReader();
      }
      public InReader(String fileName)
      {
         open(fileName);
      }
      public void open(String fileName)
      {
         open(fileName, false);
      }
      public void open(String fileName, boolean x)
      {
         myFileName = fileName;
         isEOF = false;
         fileNotFound = false;
         console = false;
         nonToken = x;
         try 
         {
            myInFile = new BufferedReader(new FileReader(fileName));
         }
            catch (FileNotFoundException e) 
            {
               setConsoleReader();
               return;
            }
         initNext();
      }
      private void initNext()
      {
         if(nonToken)
         {
            try 
            {
               next = myInFile.readLine();
            }
               catch (IOException e) 
               {
                  e.printStackTrace();
               }
         }
         else
         {
            readNext();
         }
         if(next == null)
         {
            setConsoleReader();
         }
      }
      public void close()
      {
         if (myFileName == null)
            return;
         try
         {
            myInFile.close();
         }
            catch (IOException e)
            {
               e.printStackTrace();
            }
         setConsoleReader();
      }
      private void setConsoleReader()
      {
         myFileName = null;
         current = null;
         next = null;
         buffer = null;
         myInFile = new BufferedReader(new InputStreamReader(System.in));
         isEOF = true;
         fileNotFound = true;
         nonToken = false;
         console = true;
      }
      public boolean eof()
      {
         return isEOF;
      }
      public boolean fileNotFound()
      {
         return fileNotFound;
      }
      private void readNext() //exclusively for token
      {
         current = next;
         if(current != null)
            buffer = new StringTokenizer(current);
         StringTokenizer temp = null;
         do
         {
            try 
            {
               next = myInFile.readLine();
            }
               catch (IOException e) 
               {
                  e.printStackTrace();
               }
            if(next != null)
               temp = new StringTokenizer(next);
         }
         while(!(next == null || temp.hasMoreTokens()));
      }
      public String readString() //exclusively for token
      {
         if(console)
         {
            if (buffer == null || !buffer.hasMoreTokens())
               try
               {
                  buffer = new StringTokenizer(myInFile.readLine());
                  return buffer.nextToken();
               }
                  catch (IOException e)
                  {
                     e.printStackTrace();
                  }
            else
               return buffer.nextToken();
            return null;
         }
         if(nonToken || isEOF)
            return null;
         if(buffer == null || !buffer.hasMoreTokens())
         {
            readNext();
         }
         String answer = buffer.nextToken();
         if(!buffer.hasMoreTokens() && next == null)
         {
            isEOF = true;
         }
         return answer;
      }
      public int readInteger() //exclusively for token
      {
         if(console)
         {
            if (buffer == null || !buffer.hasMoreTokens())
               try
               {
                  buffer = new StringTokenizer(myInFile.readLine());
                  return Integer.parseInt(buffer.nextToken());
               }
                  catch (IOException e)
                  {
                     e.printStackTrace();
                  }
                  catch (NumberFormatException f)
                  {
                     f.printStackTrace();
                  }
            else
               try 
               {
                  return Integer.parseInt(buffer.nextToken());
               }
                  catch (NumberFormatException f)
                  {
                     f.printStackTrace();
                  }
            return 0;
         }
         if(nonToken || isEOF)
            return 0;
         String answer = readString();
         try 
         {
            return Integer.parseInt(answer);
         }
            catch (NumberFormatException e)
            {
               e.printStackTrace();
            }
         return 0;
      }
      public double readDouble() //exclusively for token
      {
         if(console)
         {
            if (buffer == null || !buffer.hasMoreTokens())
               try
               {
                  buffer = new StringTokenizer(myInFile.readLine());
                  return Double.parseDouble(buffer.nextToken());
               }
                  catch (IOException e)
                  {
                     e.printStackTrace();
                  }
                  catch (NumberFormatException f)
                  {
                     f.printStackTrace();
                  }
            try 
            {
               return Double.parseDouble(buffer.nextToken());
            }
               catch (NumberFormatException f)
               {
                  f.printStackTrace();
               }
            return 0.0;
         }
         if(nonToken || isEOF)
            return 0.0;
         String answer = readString();
         try 
         {
            return Double.parseDouble(answer);
         }
            catch (NumberFormatException e)
            {
               e.printStackTrace();
            }
         return 0.0;
      }
      public char readCharacter() //exclusively for token
      {
         if(console)
         {
            if (buffer == null || !buffer.hasMoreTokens())
               try
               {
                  buffer = new StringTokenizer(myInFile.readLine());
                  return buffer.nextToken().charAt(0);
               }
                  catch (IOException e)
                  {
                     e.printStackTrace();
                  }
                  catch (IndexOutOfBoundsException f)
                  {
                     f.printStackTrace();
                  }
            try 
            {
               return buffer.nextToken().charAt(0);
            }
               catch (IndexOutOfBoundsException f)
               {
                  f.printStackTrace();
               }
            return (char)0;
         }
         if(nonToken || isEOF)
            return (char)0;
         String answer = readString();
         try
         {
            return answer.charAt(0);
         }
            catch (IndexOutOfBoundsException e) //paranoid
            {
               e.printStackTrace();
            }
         return (char)0;
      }
      public String readLine() //exclusively for NON-token
      {
         if(console)
         {
            try 
            {
               return myInFile.readLine();
            }
               catch (IOException e) 
               {
                  e.printStackTrace();
               }
            return null;
         }
         if(!nonToken || isEOF)
            return null;
         if(current == null)
         {
            current = next;
            try 
            {
               next = myInFile.readLine();
            }
               catch (IOException e) 
               {
                  e.printStackTrace();
               }
         }
         String answer = current + '\n';
         current = null;
         if(next == null)
         {
            isEOF = true;
         }
         return answer;
      }
      public char read() //exclusively for NON-token
      {
         if(console)
         {
            try 
            {
               return myInFile.readLine().charAt(0);
            }
               catch (IOException e) 
               {
                  e.printStackTrace();
               }
               catch (IndexOutOfBoundsException f)
               {
                  f.printStackTrace();
               }
            return (char)0;
         }
         if(!nonToken || isEOF)
            return (char)0;
         if(current == null)
         {
            current = next;
            try 
            {
               next = myInFile.readLine();
            }
               catch (IOException e) 
               {
                  e.printStackTrace();
               }
         }
         char answer = (char)0;
         if(current.length() == 0)
         {
            answer = '\n';
            current = null;
         }
         else
         {
            try
            {
               answer = current.charAt(0);
            }
               catch (IndexOutOfBoundsException e) //paranoid
               {
                  e.printStackTrace();
               }
            current = current.substring(1);
         }
         if(next == null && current == null)
         {
            isEOF = true;
         }
         return answer;
      }
   }