package edu.fcps.karel2;

import edu.fcps.karel2.util.*;
import edu.fcps.karel2.xml.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Andy Street, alstreet@vt.edu, 2007
 */

public class WorldBackend {

	private static WorldBackend current = null;
	
	private HashMap<Coordinate, BeeperStack> beepers;
	private ArrayList<Robot> robots;
	private ArrayList<Wall> walls;
	
	private int width = 10;
	private int height = 10;
	
	private Wall xAxisWall = null, yAxisWall = null;
	
	public WorldBackend(String mapName)
	{
		current = this;
		
		beepers = new HashMap<Coordinate, BeeperStack>();
		robots = new ArrayList<Robot>();
		walls = new ArrayList<Wall>();
		
		walls.add(xAxisWall = new Wall(1, 0, width, Display.HORIZONTAL));
		walls.add(yAxisWall = new Wall(0, 1, height, Display.VERTICAL));
		
		parseMap(mapName);	
	}
	public WorldBackend()
	{
		this(null);
	}
	
	void addRobot(Robot r)
	{
		robots.add(r);
	        Display.step();
        }
	
	void removeRobot(Robot r)
	{
		robots.remove(r);
		Display.step();
	}
	
	public void putBeepers(int x, int y, int num)
	{
		Coordinate c = new Coordinate(x, y);
		
		if(num == Display.INFINITY)
		{
			beepers.put(c, new BeeperStack(x, y, num));
			return;
		}
		
		int oldBeepers = 0;
		
		BeeperStack b;
		if((b = beepers.get(c)) != null)
			oldBeepers = b.getBeepers();
		
		if(oldBeepers == Display.INFINITY)
			return;
		
		if(oldBeepers + num < 1)
			beepers.remove(c);
		else
			beepers.put(c, new BeeperStack(x, y, num + oldBeepers));
	}

        public void addWall(Wall w)
        {
            walls.add(w);
        }

	//Objects
	public void addObject_beeper(Attributes a)
	{
		int x = Integer.parseInt(a.get("x"));
		int y = Integer.parseInt(a.get("y"));
		String num = a.get("num");
		
		if(num.equalsIgnoreCase("infinite"))
			putBeepers(x, y, Display.INFINITY);
		else
			putBeepers(x, y, Integer.parseInt(num));
	}
	public void addObject_wall(Attributes a)
	{
		int x = Integer.parseInt(a.get("x"));
		int y = Integer.parseInt(a.get("y"));
		int length = Integer.parseInt(a.get("length"));
		int style = a.get("style").equalsIgnoreCase("horizontal") ? Display.HORIZONTAL : Display.VERTICAL;
		
		addWall(new Wall(x, y, length, style));
	}
	public void addObject_robot(Attributes a)
	{
		
	}
	
	//Properties
	public void loadProperties_defaultSize(Attributes a)
	{
		int w = Integer.parseInt(a.get("width"));
		int h = Integer.parseInt(a.get("height"));
	
		Display.setSize(w, h);
	}
	
	void parseMap(String mapName)
	{
		 Element e = new XMLParser().parse(getInputStreamForMap(mapName));
		 WorldParser.initiateMap(e);
	}
	private InputStream getInputStreamForMap(String fileName)
	{
		FileInputStream f = null;
		
		try {
			if(fileName == null)
				throw new FileNotFoundException();
			
			f = new FileInputStream(new File(fileName));
		}
		catch (FileNotFoundException e)
		{
			if(fileName != null)
				Debug.printWarning("Map " + fileName + " not found, using default map file...");
			
			try {
				InputStream is = getClass().getResourceAsStream(Display.DEFAULT_MAP);
				
				if(is == null)
					throw new FileNotFoundException();
				
				return is;
			}
			catch (Exception g)
			{
				Debug.printError("Default map file not found!  Aborting...");
				System.exit(1);
			}
		}
		
		return f;
	}
	
	HashMap<Coordinate, BeeperStack> getBeepers()
	{
		return beepers;
	}
	ArrayList<Wall> getWalls()
	{
		return walls;
	}
	ArrayList<Robot> getRobots()
	{
		return robots;
	}
	
	boolean checkWall(int x, int y, int style)
	{
		switch(style)
		{
			case Display.HORIZONTAL:
			{
				for(Wall w : walls)
					if(w.getStyle() == style)
						if(w.getY() == y && x >= w.getX() && x < w.getX() + w.getLength())
							return true;
				
				break;
			}
			case Display.VERTICAL:
			default:
			{
				for(Wall w : walls)
					if(w.getStyle() == style)
						if(w.getX() == x && y >= w.getY() && y < w.getY() + w.getLength())
							return true;
						
				break;
			}
		}
		
		return false;
	}
	boolean checkBeepers(int x, int y)
	{
		return beepers.get(new Coordinate(x, y)) != null;
	}
	boolean isNextToARobot(Robot r, int x, int y)
	{
		for(Robot robot : robots)
			if(robot != r && robot.getX() == x && robot.getY() == y)
				return true;

		return false;
	}

	void setSize(int width, int height)
	{
		if(this.width != width)
		{
			this.width = width;
			walls.remove(xAxisWall);
			walls.add(xAxisWall = new Wall(1, 0, width, Display.HORIZONTAL));
		}
		
		if(this.height != height)
		{
			this.height = height;
			walls.remove(yAxisWall);
			walls.add(yAxisWall = new Wall(0, 1, height, Display.VERTICAL));
		}
	}
	public Coordinate getSize()
	{
		return new Coordinate(width, height);
	}
	
	void close()
	{
		current = null;
	}
	
	public static WorldBackend getCurrent()
	{
		return current;
	}
	
}
