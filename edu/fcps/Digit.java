   package edu.fcps;
   import edu.fcps.karel2.*;

   public abstract class Digit
   {
      private Robot myLED;
      public Digit(int x, int y)
      {
         myLED = new Robot(x, y, Display.EAST, Display.INFINITY);
      }
      public abstract void display();
      private void threeInARow(boolean on)
      {
         for(int x = 1; x <=3; x++)
         {
            myLED.move();
            if(on)
               myLED.putBeeper();
         }
         myLED.move();
      }
      private void turnRight()
      {
         myLED.turnLeft();
         myLED.turnLeft();
         myLED.turnLeft();
      }
      private void turnLeft()
      {
         myLED.turnLeft();
      }
      private void threeAndTurn(boolean on)
      {
         threeInARow(on);
         turnRight();
      }
      private void move()
      {
         myLED.move();
      }
      public void segment1_On()
      {
         threeAndTurn(true);
      }
      public void segment1_Off()
      {
         threeAndTurn(false); 
      }
      public void segment2_On()
      {
         threeInARow(true);
      }
      public void segment2_Off()
      {
         threeInARow(false);
      }
      public void segment3_On()
      {
         threeAndTurn(true);
      }
      public void segment3_Off()
      {
         threeAndTurn(false);
      }
      public void segment4_On()
      {
         threeAndTurn(true);
      }
      public void segment4_Off()
      {
         threeAndTurn(false);
      }
      public void segment5_On()
      {
         threeInARow(true);
      }
      public void segment5_Off()
      {
         threeInARow(false);
      }
      public void segment6_On()
      {
         threeInARow(true);
         turnLeft();
         turnLeft();
         threeInARow(false);
         turnLeft();
      }
      public void segment6_Off()
      {
         threeInARow(false);
         turnLeft();
         turnLeft();
         threeInARow(false);
         turnLeft();
      }
      public void segment7_On()
      {
         threeInARow(true);
      }
      public void segment7_Off()
      {
         threeInARow(false);
      }
   }
