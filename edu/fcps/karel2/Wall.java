package edu.fcps.karel2;

import edu.fcps.karel2.util.*;

import java.awt.*;

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
 * A Wall is a barrier that is rendered on the WorldPanel.  A Wall appears between two lanes on the
 * map and causes the program to end when a Robot walks into it.
 *
 * For vertical walls, the x coordinate corresponds to the vertical lane in front of which the Wall
 * will render, and the y coordinate corresponds to the horizontal lane on which the first segment
 * of the Wall will center on.
 *
 * For horizontal walls, the x coordinate corresponds to the vertical lane on which the first segment
 * of the wall will center on, and the y coordinate corresponds to the horizontal lane in front of
 * which the Wall will render.
 *
 * If the Wall is of length greater than 1, the Wall will extend outwards towards x-infinity or
 * y-infinity, depending on its orientation.
 */

public class Wall extends Item {

	private final int WALL_WIDTH = 7;
	
	private int style;
	private int length;
	
	/**
         * Constructs a vertical Wall of length 1
         * @param x the x coordinate of the Wall
         * @param y the y coordinate of the Wall
         */
        public Wall(int x, int y)
	{
		this(x, y, Display.VERTICAL);
	}

        /**
         * Constructs a Wall of length 1 with the specified style
         * @param x the x coordinate of the Wall
         * @param y the y coordinate of the Wall
         * @param style the orientation of the wall (Display.VERTICAL or Display.HORIZONTAL)
         */
	public Wall(int x, int y, int style)
	{
		this(x, y, 1, style);
	}
        
        /**
         * Constructs a Wall of the specified length with the specified style
         * @param x the x coordinate of the Wall
         * @param y the y coordinate of the Wall
         * @param length the length of the Wall
         * @param style the orientation of the wall (Display.VERTICAL or Display.HORIZONTAL)
         */
	public Wall(int x, int y, int length, int style)
	{
		super(x, y);
		
		this.length = length;
		
		if(style == Display.VERTICAL || style == Display.HORIZONTAL)
			this.style = style;
		else
		{
			Debug.printWarning("Invalid wall style: " + style + "...  Using VERTICAL.");
			this.style = Display.VERTICAL;
		}
	}

        /**
         * Returns the length of the Wall
         * @return the length of the Wall
         */
	public int getLength()
	{
		return length;
	}

        /**
         * Returns the style of the Wall
         * @return the orientation of the wall (Display.VERTICAL or Display.HORIZONTAL)
         */
	public int getStyle()
	{
		return style;
	}
	
        /**
         * Renders the Wall at the Coordinate specified using the passed Graphics object.
         * @param g the Graphics with which to render the Wall
         * @param c the Coordinate (in pixels) at which to render the Wall
         */
	public void render(Graphics g, Coordinate c)
	{
		g.setColor(Color.black);
		
		int width = WorldPanel.getCurrent().getXBlockLength();
		int height = WorldPanel.getCurrent().getYBlockLength();
		
		switch(style)
		{
			case Display.VERTICAL:
			{
				g.fillRect(c.x + width / 2 - (WALL_WIDTH - 1) / 2, c.y - height * length + height / 2 , WALL_WIDTH, height * length + 1);
				break;
			}
			case Display.HORIZONTAL:
			{
				g.fillRect(c.x - width / 2, c.y - height / 2 - (WALL_WIDTH - 1) / 2, width * length + 1, WALL_WIDTH);
				break;
			}
		}
	}
	
	public String toString()
	{
		return "Wall { x : " + getX() + ", y: " + getY() + " , length: " + length + ", style: " + style + " }";
	}
	
}
