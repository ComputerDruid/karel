package edu.fcps.karel2.xml;

import org.xml.sax.ext.Attributes2Impl;

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
 * A simple extension of the sax Attributes2Impl that shortens the name and attributes to be added and
 * removed in a HashMap-like manner.
 */

public class Attributes extends Attributes2Impl {
	public Attributes()
	{
		super();
	}
	public Attributes(org.xml.sax.Attributes a)
	{
		super(a);
	}
	
	/**
         * Adds an attribute pair.
         * @param key the attribute key
         * @param value the attribute value
         */
        public void put(String key, String value)
	{
		addAttribute("", key, key, "CDATA", value);
	}

        /**
         * A synonym for getValue(String).
         * @param key the key for the value you wish to retrieve
         */
	public String get(String key)
	{
		return getValue(key);
	}
}
