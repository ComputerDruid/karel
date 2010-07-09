   //Torbert, 4/16/2002, FCPS
   package edu.fcps;
   public class IO
   {
   	//standard console IO
      public static InReader in = new InReader();
      public static OutWriter out = new OutWriter();
   
   	//text file IO
      public static InReader infile = new InReader();
      public static OutWriter outfile = new OutWriter();
   
   	//private constructor means no instantiation
      private IO()
      {
      }
   }