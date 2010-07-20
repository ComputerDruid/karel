package edu.fcps.karel2;

import edu.fcps.karel2.util.*;

import java.awt.*;
import javax.swing.*;

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
 * Display houses most of the static constants used in object creation and
 * rendering, Karel file locations and images, and the speed at which the
 * WorldPanel updates.  The Display.step() method is responsible for the
 * animation of the Panel.
 * @author Andy Street, alstreet@vt.edu, 2007
 * @author Craig Saperstein, cmsaperstein@gmail.com, 2010
 */

public class Display {

	/**
	 * Map loaded if no other map is specified.
	 */
	public static final String DEFAULT_MAP = "/default.map";

	/**
	 * Defined direction for east (right).
	 */
	public static final int EAST = 0;
	/**
	 * Defined direction for north (up).
	 */
	public static final int NORTH = 1;
	/**
	 * Defined direction for west (left).
	 */
	public static final int WEST = 2;
	/**
	 * Defined direction for south (down).
	 */
	public static final int SOUTH = 3;

	/**
	 * Default width of the display window.
	 */
	public static int FRAME_WIDTH = 550;
	/**
	 * Default height of the display window
	 */
	public static int FRAME_HEIGHT = 550;

	//Walls
	/**
	 * Define for a vertical wall.
	 */
	public static final int VERTICAL = 1;
	/**
	 * Define for a horizontal wall.
	 */
	public static final int HORIZONTAL = 2;

	/**
	 * The internal number used to identify an infinite number of beepers.
	 */
	public static final int INFINITY = -2;

	/**
	 * The maximum allowed speed.
	 */
	private static final int MAX_SPEED = 10;

	/**
	 * Internally used to pause before the first paint.
	 */
	private static boolean firstStep = true;

	/**
	 * The font to write numbers on the beepers.
	 */
	private static Font beeperFont = null;
	/**
	 * The name of the font to write numbers on the beepers.
	 */
	private static String beeperFontName = "monospaced";
	/**
	 * The font size to write numbers on the beepers.
	 */
	private static int beeperFontSize = 10;

	/**
	 * Array to hold all the directions.
	 */
	private static final int[] directions = { NORTH,
	                                        EAST,
	                                        SOUTH,
	                                        WEST
	                                        };

	/**
	 * Location of the image of karel facing north.
	 */
	private static final String nkarelLocation = "/icons/kareln.gif";
	/**
	 * Location of the image of karel facing east.
	 */
	private static final String ekarelLocation = "/icons/karele.gif";
	/**
	 * Location of the image of karel facing south.
	 */
	private static final String skarelLocation = "/icons/karels.gif";
	/**
	 * Location of the image of karel facing west.
	 */
	private static final String wkarelLocation = "/icons/karelw.gif";

	/**
	 * Image icon where the north-facing karel is loaed.
	 */
	private static ImageIcon nkarel = null;
	/**
	 * Image icon where the east-facing karel is loaded.
	 */
	private static ImageIcon ekarel = null;
	/**
	 * Image icon where the south-facing karel is loaded.
	 */
	private static ImageIcon skarel = null;
	/**
	 * Image icon where the west-facing karel is loaded.
	 */
	private static ImageIcon wkarel = null;

	/**
	 * Default world speed.
	 */
	private static int speed = 5;

	/**
	 * Tracks whether or not the program has crashed.
	 */
	private static boolean isDead = false;

	/**
	 * Closes the current world if there is one, then creates a new WorldFrame
	 * with the specified map file.
	 * @param mapName the path to the map file to be loaded
	 */
	public static void openWorld(String mapName) {
		closeWorld();
		new WorldFrame(new WorldBackend(mapName));
	}

	/**
	 * Closes the current world if there is one, then creates a new WorldFrame
	 * with the default map.
	 */
	public static void openDefaultWorld() {
		closeWorld();
		new WorldFrame(new WorldBackend());
	}

	/**
	 * Placea beeper at some coordinate x,y
	 * @param x the x coordinate of the desired placement
	 * @param y the y coordinate of the desired placement 
	 */
	
	public static void placeBeeper(int x, int y)
	{
		if (WorldBackend.getCurrent() == null) {
			Display.openDefaultWorld();
		}

		if (isDead())
			return;
		WorldBackend.getCurrent().putBeepers(x, y, 1);
		WorldPanel.getCurrent().repaint();
	}
	
	/**
	 * If a WorldFrame has been previously created, its close method is called,
	 * closing the associated WorldFrame and WorldBackend before disposing of
	 * the current WorldFrame.
	 */
	private static void closeWorld() {
		if (WorldFrame.getCurrent() != null)
			WorldFrame.getCurrent().close();
	}

	/**
	 * Sets the speed at which the Display updates.
	 * @param s the requested speed of the Display.  If it is greater than the
	 * max speed, the speed is set to the max speed
	 */
	public static void setSpeed(int s) {
		if (s > MAX_SPEED || s < 1) {
			Debug.printWarning("Trying to set speed greater than maximum ("
			                   + MAX_SPEED + ")!  Setting to max instead...");
			speed = MAX_SPEED;
			return;
		}

		speed = s;
	}

	/**
	 * Returns the speed at which the Display updates.
	 */
	public static int getSpeed() {
		return speed;
	}

	/**
	 * Returns the font with which to render beepers.
	 */
	static Font getBeeperFont() {
		if (beeperFont == null)
			beeperFont = new Font(beeperFontName, Font.PLAIN, beeperFontSize);

		return beeperFont;
	}

	/**
	 * Takes a possible invalid direction and returns a valid one via modding
	 * @param dir a possibly invalid direction
	 * @return a valid direction
	 */
	public static int validateDirection(int dir) {
		for (int i = 0; i < directions.length; i++)
			if (dir == directions[i])
				return dir;

		return ((dir % 4) + 4) % 4; //This is in case it's negative
	}

	/**
	 * Returns the Karel ImageIcon associated with the specified direction
	 */
	static ImageIcon getKarelImage(int direction) {
		switch (direction) {
			case NORTH: {
					if (nkarel == null)
						nkarel = new ImageIcon(Display.class.getResource(nkarelLocation));

					return nkarel;
				}
			case EAST: {
					if (ekarel == null)
						ekarel = new ImageIcon(Display.class.getResource(ekarelLocation));

					return ekarel;
				}
			case SOUTH: {
					if (skarel == null)
						skarel = new ImageIcon(Display.class.getResource(skarelLocation));

					return skarel;
				}
			case WEST: {
					if (wkarel == null)
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
	public static void setSize(int x, int y) {
		if (WorldBackend.getCurrent() == null)
			openDefaultWorld();

		WorldBackend.getCurrent().setSize(x, y);
	}

	/**
	 * Repaints the WorldPanel, then pauses the Thread for a period of time
	 * based on the current Display speed.
	 */
	static void step() {
		if (firstStep) {
			firstStep = false;
			pause();
		}

		WorldPanel.getCurrent().repaint();
		pause();
	}

	/**
	 * Sleeps the Thread for a period of time based on the current Display speed.
	 */
	public static void pause() {
		try {
			Thread.sleep(30 * (MAX_SPEED - speed + 1));
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Outputs the reason why the Display cannot continue to update, calls
	 * hang(), then exits when hang() returns.
	 */
	static void die(String reason) {
		isDead = true;
		Debug.printError(reason);
		hang();
		System.exit(0);
	}

	/**
	 * Blocks the Thread until input is given to System.in.
	 */
	private static void hang() { //A bit hacky, but it works
		try {
			System.in.read();
		}
		catch (Exception e) { }
	}

	/**
	 * Returns whether or not the Display is currently dead
	 * (no longer able to update).
	 */
	public static boolean isDead() {
		return isDead;
	}
}
