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
 * The WorldPanel is responsible for actually drawing the world, according to the specifications of the
 * associated WorldBackend.
 */
public class WorldPanel extends JPanel {

	private static WorldPanel current = null;
	
	private WorldBackend wb = null;
	
	private final int X_BUFFER = 40, Y_BUFFER = 40;
	private final Color BACKGROUND = Color.white;
	
	private int blockWidth, blockHeight;
	
	/**
         * Constructs a WorldPanel associated with the specified WorldBackend.
         */
        public WorldPanel(WorldBackend wb)
	{
		super();
		
		this.wb = wb;
		current = this;
		
		setBackground(BACKGROUND);
	}
	
        /**
         * Renders the world paths (avenues and streets) using the specified Graphics object.
         */
	private void renderGrid(Graphics g)
	{
		Coordinate worldSize = wb.getSize();
		g.setColor(Color.red);
		
		for(int i = 1; i <= worldSize.x; i++)
		{
			Coordinate c1 = coordinateToPixel(i, 0);
			Coordinate c2 = coordinateToPixel(i, worldSize.y);
			g.drawLine(c1.x, c1.y - blockHeight / 2, c2.x, c2.y);
		}
		
		for(int i = 1; i <= worldSize.y; i++)
		{
			Coordinate c1 = coordinateToPixel(0, i);
			Coordinate c2 = coordinateToPixel(worldSize.x, i);
			g.drawLine(c1.x + blockWidth / 2, c1.y, c2.x, c2.y);
		}
	}

        /**
         * Renders all beepers contained by the world using the specified Graphics object.
         */
	private void renderBeepers(Graphics g)
	{
		for(BeeperStack b : wb.getBeepers().values())
		{
			b.render(g, coordinateToPixel(b.getX(), b.getY()));
		}
	}

        /**
         * Renders all Walls contained by the world using the specified Graphics object.
         */
	private void renderWalls(Graphics g)
	{
		for(Wall w : wb.getWalls())
		{
			w.render(g, coordinateToPixel(w.getX(), w.getY()));
		}
	}

        /**
         * Renders all Robots contained by the world using the specified Graphics object.
         */
	private void renderRobots(Graphics g)
	{
		for(Robot r : wb.getRobots())
		{
			r.render(g, coordinateToPixel(r.getX(), r.getY()));
		}
	}

        /**
         * Renders the world.
         */
	private void renderScene(Graphics g)
	{
		blockWidth = getXBlockLength();
		blockHeight = getYBlockLength();
		
		renderGrid(g);
		renderBeepers(g);
		renderRobots(g);
		renderWalls(g);
	}
	
	/**
	 * Returns the equivelent pixel coordinates of a set of Karel coordinates.
	 * @param x the Karel x coordinate
	 * @param y the Karel y coordinate
	 * @return the transformed pixel coordinates
	 */
	public Coordinate coordinateToPixel(int x, int y)
	{
		Dimension panelSize = getSize(); //In pixels
		Coordinate worldSize = wb.getSize(); //In Cartesian coordinates
		int width = panelSize.width;
		int height = panelSize.height;
		
		//Center within whitespace buffer, find coordinate by scaling, add left hand X_BUFFER, subtract half block width for more centering
		int xc = (int)((width - 2 * X_BUFFER) * ( x * 1.0 / worldSize.x)) + X_BUFFER - blockWidth / 2;
		int yc = (int)((height - 2 * Y_BUFFER) * ((worldSize.y - y) * 1.0 / worldSize.y)) + Y_BUFFER + blockHeight / 2;
		
		return new Coordinate(xc, yc);
	}

        /**
         * Returns whether the specified Coordinate is within the size specifications of the world.
         */
	public boolean isVisible(Coordinate c)
	{
		if(c.x > getSize().width || c.y > getSize().height || c.x < 0 || c.y < 0)
			return false;
		
		return true;
	}
	
	/**
         * Returns the number of pixels between two consecutive vertical paths (avenues).
         */
        protected int getXBlockLength() //This is the distance betweeen 2 streets, the paths running vertically (used for creating walls)
	{
		return (int)((getSize().getWidth() - 2 * X_BUFFER) * (1.0 / wb.getSize().x));
	}

	/**
         * Returns the number of pixels between two consecutive horizontal paths (streets).
         */
        protected int getYBlockLength() //This is the distance betweeen 2 avenues, the paths running horizontally (used for creating walls)
	{
		return (int)((getSize().getHeight() - 2 * Y_BUFFER) * (1.0 / wb.getSize().y));
	}

        /**
         * Paints the world.
         */
	protected synchronized void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		renderScene(g);
	}
	
        /**
         * Disables the WorldPanel.
         */
	void close()
	{
		current = null;
	}
	
        /**
         * Returns the current (most recently created) WorldPanel.
         */
	public static WorldPanel getCurrent()
	{
		return current;
	}
	
}
