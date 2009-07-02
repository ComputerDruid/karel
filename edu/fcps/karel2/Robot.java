   package edu.fcps.karel2;

   import edu.fcps.karel2.util.*;

   import java.awt.*;
   import javax.swing.*;

/**
 * @author Andy Street, alstreet@vt.edu, 2007
 */

    public class Robot extends Item {
   
      private int beepers;
      private int direction;
   
       public Robot()
      {
         this(1, 1, Display.EAST, 0);
      }
       public Robot(int x, int y, int dir, int beepers)
      {
         super(x, y);
      
         if(WorldBackend.getCurrent() == null)
         {
            Display.openDefaultWorld();
         }
      
         this.x = x;
         this.y = y;
      
         if(beepers < 0 && beepers != Display.INFINITY)
         {
            Debug.printWarning("Invalid amount of beepers: " + beepers + "...  Setting to 0...");
            beepers = 0;
         }
      
         direction = Display.validateDirection(dir);
         this.beepers = beepers;
      
         WorldBackend.getCurrent().addRobot(this);
      }
   
       public int getDirection()
      {
         return direction;
      }
       public int getBeepers()
      {
         return beepers;
      }
   
       public synchronized void move()
      {
         if(Display.isDead()) 
            return;
      
         boolean clear = frontIsClear();
      
         if(!clear)
         {
            Coordinate c = getWallCoordinate(direction);
         
            switch(direction)
            {
               case Display.NORTH:
               case Display.SOUTH:
                  Display.die("Tried to walk through a horizontal wall at (" + c.x + ", " + c.y + ")!");
                  break;
               case Display.EAST:
               case Display.WEST:
               default:
                  Display.die("Tried to walk through a vertical wall at (" + c.x + ", " + c.y + ")!");
            }
         
            return;
         }
      
         switch(direction)
         {
            case Display.NORTH:
               y++;
               break;
            case Display.EAST:
               x++;
               break;
            case Display.SOUTH:
               y--;
               break;
            case Display.WEST:
               x--;
               break;
         }
      
         Display.step();
      }
       public void turnLeft()
      {
         if(Display.isDead()) 
            return;
      
         direction = Display.validateDirection(direction + 1);
      
         Display.step();
      }
       void turnRight()
      {
         if(Display.isDead()) 
            return;
      
         direction = Display.validateDirection(direction - 1);
      
         Display.step();
      }
   
       public void putBeeper()
      {
         if(Display.isDead()) 
            return;
      
         if(beepers < 1 && beepers != Display.INFINITY)
         {
            Display.die("Trying to put non-existent beepers!");
            return;
         }
      
         if(beepers != Display.INFINITY)
            beepers--;
      
         WorldBackend.getCurrent().putBeepers(x, y, 1);
      
         Display.step();
      }
       public void pickBeeper()
      {
         if(Display.isDead()) 
            return;
      
         if(!WorldBackend.getCurrent().checkBeepers(x, y))
         {
            Display.die("Trying to pick non-existent beepers!");
            return;
         }
      
         if(beepers != Display.INFINITY)
            beepers++;
      
         WorldBackend.getCurrent().putBeepers(x, y, -1);
      
         Display.step();
      }
       public boolean hasBeepers()
      {
         return beepers > 0 || beepers == Display.INFINITY;
      }
       public boolean frontIsClear()
      {
         return isClear(direction);
      }
       public boolean rightIsClear()
      {
         return isClear(Display.validateDirection(direction - 1));
      }
       public boolean leftIsClear()
      {
         return isClear(Display.validateDirection(direction + 1));
      }
   
       public boolean nextToABeeper()
      {
         return WorldBackend.getCurrent().checkBeepers(x, y);
      }
   
       public boolean nextToARobot()
      {
         return WorldBackend.getCurrent().isNextToARobot(this, x, y);
      }
   
       public boolean facingNorth()
      {
         return direction == Display.NORTH;
      }
       public boolean facingSouth()
      {
         return direction == Display.SOUTH;
      }
       public boolean facingEast()
      {
         return direction == Display.EAST;
      }
       public boolean facingWest()
      {
         return direction == Display.WEST;
      }
   
       private boolean isClear(int direction)
      {
         Coordinate c = getWallCoordinate(direction);
      
         switch(direction)
         {
            case Display.NORTH:
            case Display.SOUTH:
               return !WorldBackend.getCurrent().checkWall(c.x, c.y, Display.HORIZONTAL);
            case Display.EAST:
            case Display.WEST:
            default:
               return !WorldBackend.getCurrent().checkWall(c.x, c.y, Display.VERTICAL);
         }
      }
   
       public void explode()
      {
         WorldBackend.getCurrent().removeRobot(this);
      }
   
       private Coordinate getWallCoordinate(int dir) //This returns the coordinate of where the wall directly in front of the robot would be
      {
         int xc = x, yc = y;
      
         switch(dir)
         {
            case Display.NORTH: //Check in front, not behind
            case Display.EAST: //Check in front, not behind
               break;
            case Display.SOUTH: //Checking behind current location
               yc--;
               break;
            case Display.WEST: //Checking behind current location
               xc--;
               break;
         }
      
         return new Coordinate(xc, yc);
      }
   
       public void render(Graphics g, Coordinate c)
      {
         ImageIcon i = Display.getKarelImage(direction);
         g.drawImage(i.getImage(), c.x - i.getIconWidth() / 2, c.y - i.getIconHeight() / 2, null);
      }
   }
