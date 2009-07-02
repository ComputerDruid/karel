package edu.fcps.karel2;

import javax.swing.JFrame;

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
 * WorldFrame houses the WorldPanel and deals with its creation using the WorldBackend passed to the
 * constructor.
 */
public class WorldFrame extends JFrame {
	
	private static WorldFrame current = null;
	
	/**
         * Constructs a WorldFrame associated with the specified WorldBackend.  A WorldPanel is
         * created with the WorldBackend and set as the content pane.
         */
        public WorldFrame(WorldBackend wb)
	{
		super();
		
		current = this;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Display.FRAME_WIDTH, Display.FRAME_HEIGHT);
		setLocation(250, 250);
		setContentPane(new WorldPanel(wb));
		setVisible(true);
	}
	
        /**
         * Closes the WorldBackend and WorldPanel, then disposes the Frame.
         */
	void close()
	{
		WorldBackend.getCurrent().close();
		WorldPanel.getCurrent().close();
		current = null;
		dispose();
	}
	
        /**
         * Returns the current (most recently created) WorldFrame.
         */
	public static WorldFrame getCurrent()
	{
		return current;
	}
}
