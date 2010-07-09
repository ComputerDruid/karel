//Torbert, 5.24.2004
//updated 7.29.2004

   package edu.fcps;

   import java.io.*;
   import java.util.*;

/**
The Scanner class provides a simple, Sun-standard mechanism for reading input
from either the keyboard or a text file.
<br><br>
If you are using a tjXXXXx.jar file you can access the Scanner class by importing
edu.fcps.Scanner.
<br><br>
If you are using Java 1.5 you can access the complete Scanner class by importing
java.util.Scanner.  A more detailed API for this version
of the Scanner class is availabe from the official Sun Java API website.
<br><br>
A <i>token</i> is a continuous sequence of non-whitespace characters.  Other
than the nextLine method, the Scanner class processes input data in tokens.  By
default, whitespace is defined by the Character.isWhitespace method.
*/

   public class Scanner
   {
      private BufferedReader br;
      private StringTokenizer st;
      private boolean eof;
      private String line;
      private String token;
      private boolean console;
   
      private Scanner()
      {
         br = null;
         st = new StringTokenizer("");
         eof = false;
         line = null;
         token = null;
         console = false;
      }
		/**
		Constructs a new Scanner that reads from the specified input stream (e.g., System.in).
		*/
      public Scanner(InputStream is)
      {
			this();
         console = is.equals(System.in);
         try
         {
            br = new BufferedReader(new InputStreamReader(is));
         }
            catch(Exception e)
            {
               eof = true;
            }
         if(!eof && !console)
            advance();
      }
		/**
		Constructs a new Scanner that reads from the specified file (e.g., new File("data.txt")).
		
		@throws	FileNotFoundException	You must handle this exception.
		*/		
      public Scanner(File f) throws FileNotFoundException
      {
			this();
         console = false;
         try
         {
            br = new BufferedReader(new FileReader(f));
         }
            catch(FileNotFoundException e1)
            {
               eof = true;
					throw e1;
            }
				catch(Exception e2)
				{
					eof = true;
				}
         if(!eof)
            advance();
      }   
		/**
		Returns true if there is more data to read.
		*/
      public boolean hasNext()
      {
         return !eof;
      }
		/**
		Returns true if the next token to be read is an <code>int</code> value.
		*/
      public boolean hasNextInt()
      {
         if(hasNext())
            try
            {
               Integer.parseInt(token);
               return true;
            }
               catch(Exception e)
               {
                  return false;
               }
         else
            return false;
      }
		/**
		Returns true if the next token to be read is a <code>double</code> value.
		*/
      public boolean hasNextDouble()
      {
         if(hasNext())
            try
            {
               Double.parseDouble(token);
               return true;
            }
               catch(Exception e)
               {
                  return false;
               }
         else
            return false;
      }
		/**
		Reads the next token as an <code>int</code>.
		*/
      public int nextInt()
      {
         return Integer.parseInt(next());
      }
		/**
		Reads the next token as a <code>double</code>.
		*/
      public double nextDouble()
      {
         return Double.parseDouble(next());
      }
      private void advance()
      {
         while(hasNext() && !st.hasMoreTokens())
            try
            {
               line = br.readLine();
               st = new StringTokenizer(line);
            }
               catch(Exception e)
               {
                  eof = true;
               }
         if(eof)
            token = null;
         else
            token = st.nextToken();
      }
		/**
		Reads the next token as a <code>String</code>.
		*/
      public String next()
      {
         if(console)
            advance();
         String ret = token;
         String temp = line;
         if(!console)
            advance();
         if(temp.equals(line))
            line = line.substring(ret.length()).trim();
         return ret;
      }
		/**
		Advances past the current line and returns the input that was skipped.
		*/
      public String nextLine()
      {	
         if(console)
            advance();
         String ret = line;
         st = new StringTokenizer(""); //force advance to readLine
         if(!console)
            advance();
         return ret;
      }
		/**
		Closes this scanner.		
		*/
      public void close()
      {
         try
         {
            br.close();
         }
            catch(Exception e)
            {
            }
         eof = true;
      }
   }
