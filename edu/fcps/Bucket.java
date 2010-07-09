   //Torbert, e-mail: mr@torbert.com, website: www.mr.torbert.com
	//version 3.26.2003
	//Previous edition by Shane Torbert for C++ w/ CMU Graphics
	//Original by Mary Johnson

   package edu.fcps;

   import javax.swing.*;
   import java.awt.Color;
   import java.awt.Font;
   import java.awt.*;
   import java.awt.event.*;
   import java.awt.image.BufferedImage;
   public class Bucket
   {           
   	//class constants
      private static final int maxCapacity = 8, maxBuckets = 3;
   
   	//static fields
      private static BufferedImage buffer;
      private static double diff;
      private static int numBuckets, totalWater;
      private static boolean useTotal, shownWin;
      private static boolean[] doneList;
      private static Bucket[] jugList;
      private static int commands;
   
   	//instance fields
      private int capacity;
      private double water;
   
   	//static initializer - for static fields
      static
      {
         buffer = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
      
         diff = 1.0;
         numBuckets = totalWater = 0;
         useTotal = shownWin = false;
         doneList = new boolean[maxCapacity * maxBuckets];
         jugList = new Bucket[maxBuckets];
         commands = 0;
      }
   
   	//constructor (dynamic initializer - for dynamic fields)
      public Bucket(int numGals)
      {
         capacity = numGals;
         water = 0.0;
         jugList[numBuckets] = this;
         numBuckets++;
         if (capacity > maxCapacity)
            capacity = maxCapacity;
      }
      public Bucket() //default constructor
      {
         this(0);
      }
   
   	//static methods
      public static void useTotal(boolean b)
      {
         useTotal = b;
      }
      public static void setSpeed(int x)
      {
         if(x < 1 || x > 10)
            return;
         diff = x * 0.1;
      }
   
   	//instance methods
      private void pause()
      {
         try
         {
            Thread.sleep(100);
         }
            catch(Exception e)
            {
            }
      }
      public void fill()
      {
         commands++;
         int temp = capacity - (int)water;
         while(water + diff / 100 < capacity)
         {
            water += diff;
            pause();
            if(water + diff / 100 > capacity)
            {
               water = (double)capacity;
               pause();
            }
         }
         water = (double)capacity;
         totalWater += temp;
         if(useTotal)
            doneList[totalWater] = true;
         else
            doneList[(int)water] = true;
      }
      public void spill()
      {
         commands++;
         int temp = (int)water;
         while(water > diff / 100)
         {
            water -= diff;
            pause();
         }
         water = 0.0;
         totalWater -= temp;
         if(useTotal)
            doneList[totalWater] = true;
      }
      public void pourInto(Bucket dest)
      {
         commands++;
         double w1 = water, w2 = dest.water;
         int temp = dest.capacity - (int)dest.water;
         if(temp > (int)water)
            temp = (int)water;
         while(water > diff/100 && dest.water + diff/100 < dest.capacity)
         {
            dest.water += diff;
            water -= diff;
            pause();
         }
         water = w1 - (double)temp;
         dest.water = w2 + (double)temp;
         if(!useTotal)
         {
            doneList[(int)water] = true;
            doneList[(int)dest.water] = true;
         }
      }
   
      public static Image getImage()
      {
         Graphics g = buffer.getGraphics();
      
         	//draw table and room
         g.setColor(Color.gray);
         g.fillRect(0, 375, 600, 25);
         g.setColor(Color.green);
         g.fillRect(0, 0, 600, 375);
         g.setColor(Color.black);
         g.fillRect(25, 300, 550, 50);
         g.fillRect(50, 350, 25, 50);
         g.fillRect(525, 350, 25, 50);
      
         	//show score
         g.setFont(new Font("Arial", Font.PLAIN, 10));
         int count = 1, max = 0;
         if(useTotal)
            for(int index = 0; index < numBuckets; index++)
               max += jugList[index].capacity;
         else
            for(int index = 0; index < numBuckets; index++)
               if(jugList[index].capacity > max)
                  max = jugList[index].capacity;
         boolean win = true;
         if(max == 0)
            win = false;
         for(int x = 60;
            x < 550 - 500 / doneList.length; 
            x += 500 / doneList.length)
         {
            if(count > max)
               g.setColor(Color.gray);
            else if(doneList[count])
               g.setColor(Color.red);
            else
            {
               g.setColor(Color.white);
               win = false;
            }
            g.drawString("" + count, x, 50);
            count++;
         }
      
         	//draw jugs
         g.setFont(new Font("Courier", Font.PLAIN, 14));
         int space, start, end, scale;
         space = (600 - 50) / maxBuckets;
         for(int index = 0; index < numBuckets; index++)
         {
            start = 50 + index * space + 10;
            end = (index + 1) * space - 10;
            scale = 200 / maxCapacity;
            g.setColor(Color.black);
            for(int loop = 1; loop <= jugList[index].capacity; loop++)
            {
               g.drawString("" + loop, start - 20, 
                           300 - (int)(scale * (loop - 0.5)));
            }
            g.setColor(Color.white);
            g.fillRect(start, 300 - scale * jugList[index].capacity,
                      end - start, scale * jugList[index].capacity);
            g.setColor(Color.black);
            g.drawRect(start, 300 - scale * jugList[index].capacity,
                      end - start, scale * jugList[index].capacity);
            g.setColor(Color.blue);
            if(301 - (int)(scale * jugList[index].water) < 
               300 - scale * jugList[index].capacity)
            {
               g.fillRect(start + 1, 301 - scale * jugList[index].capacity,
                         end - start - 1, scale * jugList[index].capacity - 1);
            }
            else
               g.fillRect(start + 1, 301 - (int)(scale * jugList[index].water),
                         end - start - 1, (int)(scale * jugList[index].water) - 1);
         }
      
         if(win)
         {
            if(!shownWin)
            {
               shownWin = true;
               for(int z = 1; z <= 7; z++)
               {
                  if(z % 2 == 1)
                     g.setColor(Color.yellow.brighter());
                  else
                     g.setColor(Color.black);
                  g.setFont(new Font("Serif", Font.BOLD, 25));
                  g.drawString("Well Done - " + commands + " commands", 160, 330);
                  return buffer;
                  //gg.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
               /*
                  int y = 500;
                  if(z == 1)
                     y = 750;
                  try
                  {
                     Thread.sleep(y);
                  }
                     catch (InterruptedException oops)
                     {
                     }
               		*/
               }
            }
            else
            {
               g.setColor(Color.yellow.brighter());
               g.setFont(new Font("Serif", Font.BOLD, 25));
               g.drawString("Well Done - " + commands + " commands", 160, 330);
               return buffer;
                  //gg.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
            }
         }
         else
            return buffer;
               //gg.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
         return null;
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
                     g.drawImage(Bucket.getImage(), 0, 0, getWidth(), getHeight(), null);
                  }
               }
            ;
      }
      public static void createFrame()
      {
         JFrame frame = new JFrame("Bucket");
         frame.setSize(600, 400);
         frame.setLocation(100, 100);
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
                                       g.drawImage(Bucket.getImage(), 0, 0, getWidth(), getHeight(), null);
                                    }
                                 }
                              );
         frame.setVisible(true);
      }
   }