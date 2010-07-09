//Torbert, 6/5/2002, FCPS
//modified 7.24.2003

   package edu.fcps;
   public class Utilities
   {
      static
      {
         System.loadLibrary("edu_fcps_Utilities");
      }
      public static native void cls();
      public static native void getch();
   }