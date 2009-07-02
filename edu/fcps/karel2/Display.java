package edu.fcps.karel2;

import edu.fcps.karel2.util.*;

import java.awt.*;
import javax.swing.*;

/**
 * @author Andy Street, alstreet@vt.edu, 2007
 */

/*
 * Copyright (C) Andy Street 2007
 *
 * This software is licensed under the GNU Public License v3.
 * See attached file LICENSE.TXT or contact the author for more information.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 3 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Display houses most of the static constants used in object creation and rendering, Karel file
 * locations and images, and the speed at which the WorldPanel updates.  The Display.step() method
 * is responsible for the animation of the Panel.
 */

public class Display {
	
	//	Maps
	public static final String DEFAULT_MAP = "/default.map";
	
	//Directions
	public static final int EAST = 0;
	public static final int NORTH = 1;
	public static final int WEST = 2;
	public static final int SOUTH = 3;
	
	//Frame
	public static int FRAME_WIDTH = 550;
	public static int FRAME_HEIGHT = 550;
	
	//Walls
	public static final int VERTICAL = 1;
	public static final int HORIZONTAL = 2;
	
	//Beepers
	public static final int INFINITY = -2;
	
	private static final int MAX_SPEED = 10;
	
	private static boolean firstStep = true;
	
	private static Font beeperFont = null;
	private static String beeperFontName = "monospaced";
	private static int beeperFontSize = 10;
	
	private static final int[] directions = { NORTH, 
											  EAST,
											  SOUTH,
											  WEST };
	
	private static final String nkarelLocation = "/icons/kareln.gif";
	private static final String ekarelLocation = "/icons/karele.gif";
	private static final String skarelLocation = "/icons/karels.gif";
	private static final String wkarelLocation = "/icons/karelw.gif";
	
	private static ImageIcon nkarel = null;
	private static ImageIcon ekarel = null;
	private static ImageIcon skarel = null;
	private static ImageIcon wkarel = null;
	
	private static int speed = 5;
	
	private static boolean isDead = false;
	
	/**
         * Closes the current world if there is one, then creates a new WorldFrame with the
         * specified map file.
         * @param mapName the path to the map file to be loaded
         */
        public static void openWorld(String mapName)
	{
		closeWorld();
		new WorldFrame(new WorldBackend(mapName));
	}
	
        /**
         * Closes the current world if there is one, then creates a new WorldFrame with the
         * default map.
         */
	public static void openDefaultWorld()
	{
		closeWorld();
		new WorldFrame(new WorldBackend());
	}
	
        /**
         * If a WorldFrame has been previously created, its close method is called, closing the
         * associated WorldFrame and WorldBackend before disposing of the current WorldFrame.
         */
	static void closeWorld()
	{
		if(WorldFrame.getCurrent() != null)
			WorldFrame.getCurrent().close();
	}
	
        /**
         * Sets the speed at which the Display updates.
         * @param s the requested speed of the Display.  If it is greater than the max speed, the speed
         * is set to the max speed
         */
	public static void setSpeed(int s)
	{
		if(s > MAX_SPEED || s < 1)
		{
			Debug.printWarning("Trying to set speed greater than maximum (" + MAX_SPEED + ")!  Setting to max instead...");
			speed = MAX_SPEED;
			return;
		}
		
		speed = s;
	}

        /**
         * Returns the speed at which the Display updates.
         */
	public static int getSpeed()
	{
		return speed;
	}
	
	/**
         * Returns the font with which to render beepers.
         */
        static Font getBeeperFont()
	{
		if(beeperFont == null)
			beeperFont = new Font(beeperFontName, Font.PLAIN, beeperFontSize);
		
		return beeperFont;
	}
	
        /**
         * Takes a possible invalid direction and returns a valid one via modding
         * @param dir a possibly invalid direction
         * @return a valid direction
         */
	public static int validateDirection(int dir)
	{
		for(int i = 0; i < directions.length; i++)
			if(dir == directions[i])
				return dir;
		
		return ((dir % 4) + 4) % 4; //This is in case it's negative
	}
	
        /**
         * Returns the Karel ImageIcon associated with the specified direction
         */
	static ImageIcon getKarelImage(int direction)
	{
		switch(direction)
		{
			case NORTH:
			{
				if(nkarel == null)
					nkarel = new ImageIcon(Display.class.getResource(nkarelLocation));
				
				return nkarel;
			}
			case EAST:
			{
				if(ekarel == null)
					ekarel = new ImageIcon(Display.class.getResource(ekarelLocation));
				
				return ekarel;
			}
			case SOUTH:
			{
				if(skarel == null)
					skarel = new ImageIcon(Display.class.getResource(skarelLocation));
				
				return skarel;
			}
			case WEST:
			{
				if(wkarel == null)
					wkarel = new ImageIcon(Display.class.getResource(wkarelLocation));
				
				return wkarel;
			}
			default:
				Debug.printError("Karel image for direction " + direction + " not found!  Aborting...");
				System.exit(7);
				return null;
		}
	}
	
	/**
	 * The same as calling WorldBackend.setSize(x, y)
	 * @param x
	 * @param y
	 */
	public static void setSize(int x, int y)
	{
		if(WorldBackend.getCurrent() == null)
			openDefaultWorld();
		
		WorldBackend.getCurrent().setSize(x, y);
	}
	
        /**
         * Repaints the WorldPanel, then pauses the Thread for a period of time based on the current
         * Display speed.
         */
	static void step()
	{
		if(firstStep)
		{
			firstStep = false;
			pause();
		}
		
		WorldPanel.getCurrent().repaint();
		pause();
	}
	
        /**
         * Sleeps the Thread for a period of time based on the current Display speed.
         */
	public static void pause()
	{
		try {
			Thread.sleep(30 * (MAX_SPEED - speed + 1));
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
        /**
         * Outputs the reason why the Display cannot continue to update, calls hang(), then exits when
         * hang() returns.
         */
	static void die(String reason)
	{
		isDead = true;
		Debug.printError(reason);
                hang();
                System.exit(0);
	}
	
        /**
         * Blocks the Thread until input is given to System.in.
         */
        private static void hang() //A bit hacky, but it works
        {
            try {
                System.in.read();
            }
            catch (Exception e) { }
        }

        /**
         * Returns whether or not the Display is currently dead (no longer able to update).
         */
        public static boolean isDead()
	{
		return isDead;
	}
}
