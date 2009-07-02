package edu.fcps.karel2.util;

/**
 * Debug provides an easy way for all components of the client to output various levels of debug information, from full to error,
 * only having to change the debug level the client is run in.  All debug output from the framework and the default modules are
 * passed through this class instead of directly through standard out.  To use this class, just take the statement you would
 * normally have using System.out.println, and pass it to Debug.println, along with an extra argument for the debug level it's
 * associated with.  For example:
 * <code>System.out.println("Failed to load module " + modName + "...");</code> becomes
 * <code>Debug.println("Failed to load module " + modName + "...", 1);</code>
 * The "1" means that if the current client debug level is 1 or less, the statement will be put out.  Otherwise, it will be
 * ignored.  The debug levels are:<br>
 * <pre>    0: No debug except for errors [default]
 *    1: Minimal Debug
 *    2: Maximum Debug
 *    3: Uber XML Debug</pre>
 * <p>The client debug level can either be hard coded in or set at runtime via the command line arg "-d".
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

public class Debug
{
	private static int debugLevel = 0;
	private static int maxDebugLevel = 5;

	private static boolean printToStd = true;
	
	/**
	 * Sets the debug level.
	 * @param level the level to set to <br>
	 * 0: No debug except for errors [default] <br>
	 * 1: Minimal Debug <br>
	 * 2: Maximum Debug <br>
	 * 3: Uber XML Debug
	 */
	public static void setDebug(int level)
	{
		if(level >= 0 && level <= maxDebugLevel)
			debugLevel = level;
		else
		{
			System.out.println("Invalid debug level specified (0-" + maxDebugLevel + ").");
			System.exit(-1);
		}
	}
	
	/** 
	 * If true, print debug messages to System.out (default), otherwise, print to System.err
	 * @param bool whether or not to print ot System.out
	 */
	public static void setPrintToStd(boolean bool)
	{
		printToStd = bool;
	}
	
	/**
	 * Prints the debug statement if the level if less than or equal to the current debug level.
	 * @param s the String to print out
	 * @param level the debug level associated with the message
	 */
	public static void println(String s, int level)
	{
		if(level <= debugLevel)
			if(printToStd)
				System.out.println("[DEBUG " + level + "] " + s);
			else
				System.err.println("[DEBUG " + level + "] " + s);
	}
	
	/**
	 * Prints an error statement
	 */
	public static void printError(String s)
	{
		System.out.println("[ERROR] " + s);
	}
	
	/**
	 * Prints a warning statement
	 */
	public static void printWarning(String s)
	{
		System.out.println("[WARNING] " + s);
	}
	
	/**
	 * Prints something tagged as important
	 */
	public static void printTagged(String s)
	{
		System.out.println("[ !! ] " + s);
	}
	
	/**
	 * Returns the debug level
	 */
	public static int debugLevel()
	{
		return debugLevel;
	}
}
