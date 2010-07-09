//Torbert, e-mail: mr@torbert.com, website: www.mr.torbert.com
//3.28.2003
//Previous editions by Charles Brewer, CMU Graphics, C++, Summer 1999
//by Mary Johnson, Ian Hagemann, Tom Dixon, Summer 1998

   package edu.fcps;

   import javax.swing.*;
   import java.awt.*;
   import java.awt.event.*;
   import java.awt.image.BufferedImage;
   public abstract class Turtle
   {      
      private static final int WIDTH = 600, HEIGHT = 600;
   
      private double heading, xPos, yPos;
      private boolean penIsDown;
      private Color turtleColor;
      private float thickness;
   
      private static BufferedImage buffer1, buffer2;
      private static Graphics graphics1, graphics2;
      private static boolean crawlOff;
      private static double crawlSpeed;
      private static Turtle[] list;
   
      static
      {
         buffer1 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
         graphics1 = buffer1.getGraphics();
         buffer2 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
         graphics2 = buffer2.getGraphics();
         crawlOff = false;
         crawlSpeed = 0.005;
         list = new Turtle[0];
         clear(Color.green);
      }
   
   	//*************************constructors*************************************
      public Turtle (double x, double y, double heading) 
      {
         xPos = x;
         yPos = y;
         this.heading = heading;
         turtleColor = Color.black;
         penIsDown = true;
         thickness = 3.0f;
      
         Turtle[] temp = new Turtle[list.length + 1];
         for(int z = 0; z < list.length; z++)
            temp[z] = list[z];
         temp[temp.length - 1] = this;
         list = temp;
      }  
      public Turtle()
      {
         this(WIDTH / 2, HEIGHT / 2, 90.0);
      }
   
   	//*************************static methods********************************
      public static void setCrawl(boolean b)
      {
         crawlOff = !b;
      }
      public static void setSpeed(int x)
      {
         if(x < 1 || x > 10)
            return;
         crawlOff = false;
         crawlSpeed = 0.05 / x;
      }
      public static void clear(Color c) 
      {
         graphics1.setColor(c);
         graphics1.fillRect(0, 0, WIDTH, HEIGHT); 
         graphics2.drawImage(buffer1, 0, 0, WIDTH, HEIGHT, null);
      }             
      public static void clear() 
      {
         clear(Color.white);
      }
      private static void drawTurtle(Graphics g, double x, double y, double h, Color c)
      {
         int body = 8, head = 4, feet = 2;
         g.setColor(c);
      
      	//body
         g.fillOval((int)(0.5 + x - body), (int)(0.5 + y - body), body * 2, body * 2);
      
      	//head
         g.fillOval((int)(0.5 + x + body * Math.cos(h * Math.PI / 180)) - head,
                      (int)(0.5 + y - body * Math.sin(h * Math.PI/ 180)) - head, 
                   head * 2, head * 2);
      
      	//feet
         g.fillOval((int)(0.5 + x + body * Math.cos((h - 45) * Math.PI / 180)) - feet,
                      (int)(0.5 + y - body * Math.sin((h - 45) * Math.PI/ 180)) - feet, 
                   feet * 2, feet * 2);
      
         g.fillOval((int)(0.5 + x + body * Math.cos((h - 135) * Math.PI / 180)) - feet,
                      (int)(0.5 + y - body * Math.sin((h - 135) * Math.PI/ 180)) - feet, 
                   feet * 2, feet * 2);
      
         g.fillOval((int)(0.5 + x + body * Math.cos((h + 45) * Math.PI / 180)) - feet,
                      (int)(0.5 + y - body * Math.sin((h + 45) * Math.PI/ 180)) - feet, 
                   feet * 2, feet * 2);
      
         g.fillOval((int)(0.5 + x + body * Math.cos((h + 135) * Math.PI / 180)) - feet,
                      (int)(0.5 + y - body * Math.sin((h + 135) * Math.PI/ 180)) - feet, 
                   feet * 2, feet * 2);
      }
   
   	//*********************instance methods**********************************
      private void pause()
      {
         try
         {
            Thread.sleep((int)(crawlSpeed * 1000));
         }
            catch(Exception e)
            {
            }
      }
   //      public String toString()
   //      {
   //         return "Turtle @ (" + xPos + ", " + yPos + ") heading " + heading;
   //      }
      public void setThickness(int x)
      {
         thickness = (float)x;
      }
      public void forward(double amount) 
      {
         double dx = amount * Math.cos(heading * Math.PI / 180);
         double dy = amount * Math.sin(heading * Math.PI / 180);
      
         double temp1 = xPos + dx;
         double temp2 = yPos - dy;
      
         ((Graphics2D)graphics1).setStroke(new BasicStroke(thickness));
      
         double oldX = xPos;
         double oldY = yPos;
      
         if(crawlOff)
         {
            xPos = xPos + dx;
            yPos = yPos - dy;
            if(penIsDown)
            {
               graphics1.setColor(turtleColor);
               graphics1.drawLine((int)(0.5 + oldX), (int)(0.5 + oldY),
                                    (int)(0.5 + xPos), (int)(0.5 + yPos));
               graphics2.drawImage(buffer1, 0, 0, WIDTH, HEIGHT, null);
            }
         }
         else
         {
            double track = 3.0, sum = 0;
            boolean done = false;
            while(!done)
            {
               xPos = oldX + track * Math.cos(heading * Math.PI / 180);
               yPos = oldY - track * Math.sin(heading * Math.PI / 180);
               if(penIsDown)
               {
                  graphics1.setColor(turtleColor);
                  graphics1.drawLine((int)(0.5 + oldX), (int)(0.5 + oldY),
                                       (int)(0.5 + xPos), (int)(0.5 + yPos));
               }
               graphics2.drawImage(buffer1, 0, 0, WIDTH, HEIGHT, null);
               pause();
               oldX = xPos;
               oldY = yPos;
               if(sum + 2 * track >= amount)
                  done = true;
               sum += track;
            }
            if(penIsDown)
            {
               graphics1.setColor(turtleColor);
               graphics1.drawLine((int)(0.5 + xPos), (int)(0.5 + yPos),
                                    (int)(0.5 + temp1), (int)(0.5 + temp2));
            }
            graphics2.drawImage(buffer1, 0, 0, WIDTH, HEIGHT, null);
            pause();
            xPos = temp1;
            yPos = temp2;
            graphics2.drawImage(buffer1, 0, 0, WIDTH, HEIGHT, null);
            pause();
         }
      }
      public void back(double amount) 
      {
         forward(-1 * amount);
      }
      public void turnRight(double degrees)
      {
         if(crawlOff)
            heading = heading - degrees;
         else
         {
            double interval = 5.0;
            if(degrees < 0)
               interval = -interval;
            double temp = heading - degrees;
            while(Math.abs(heading - temp) > Math.abs(interval))
            {
               heading = heading - interval;
               graphics2.drawImage(buffer1, 0, 0, WIDTH, HEIGHT, null);
               pause();
            }
            heading = temp;
            graphics2.drawImage(buffer1, 0, 0, WIDTH, HEIGHT, null);
            pause();
         }
      }
      public void turnLeft(double degrees)
      {
         turnRight(-1 * degrees);
      }
      public Color getColor()
      {
         return turtleColor;
      }
      public void setColor(Color c) 
      {
         turtleColor = c;
         graphics1.setColor(turtleColor);
      }
      public void setColor(int n) 
      {
         if(n < 0 || n > 12)
            return;
         Color k[] = {Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray,
            Color.green, Color.lightGray, Color.magenta, Color.orange, Color.pink,
            Color.red, Color.white, Color.yellow};
         turtleColor = k[n];
         graphics1.setColor(turtleColor);
      }
      public void setPenDown(boolean x) 
      {
         penIsDown = x;
      }
      public abstract void drawShape();
   	//**************************************************************
      public static Image getImage()
      {
         if(!crawlOff)
            for(int x = 0; x < list.length; x++)
            {
               drawTurtle(graphics2, list[x].xPos, list[x].yPos, 
                         list[x].heading, list[x].turtleColor);
            }
         return buffer2;
         //g.drawImage(buffer2, 0, 0, WIDTH, HEIGHT, null);
      }
      public static JPanel createPanel()
      {
         return
               new JPanel()
               {
                  private Timer timer;
                  {
                     timer = new Timer(10, 
                                         new ActionListener()
                                         {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                               repaint();
                                            }
                                         }
                                      );
                     timer.start();
                  }
                  public void paint(Graphics g)
                  {
                     g.drawImage(Turtle.getImage(), 0, 0, getWidth(), getHeight(), null);
                  }
               }
            ;
      }
      public static void createFrame()
      {
         JFrame frame = new JFrame("Turtle");
         frame.setSize(400, 400);
         frame.setLocation(200, 100);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.getContentPane().add( 
                                 new JPanel()
                                 {
                                    private Timer timer;
                                    {
                                       timer = new Timer(10, 
                                                           new ActionListener()
                                                           {
                                                              public void actionPerformed(ActionEvent e)
                                                              {
                                                                 paintImmediately(0, 0, getWidth(), getHeight());
                                                              }
                                                           }
                                                        );
                                       timer.start();
                                    }
                                    public void paint(Graphics g)
                                    {
                                       g.drawImage(Turtle.getImage(), 0, 0, getWidth(), getHeight(), null);
                                    }
                                 }
                              );
         frame.setVisible(true);
      }
   }