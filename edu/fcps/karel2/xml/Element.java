package edu.fcps.karel2.xml;

import java.util.*;
import org.xml.sax.ext.*;

/**
 * NCXMLElements are what the XMLParser turns the String based XML tree it receives from the ClientConnector into.  With the help
 * of Apache's J-Xerces2 implementation, the Parser will turn the content XML tree of the message into a XMLElement tree for easier
 * use, while turning the header the main HashMap contained in a XMLData object.  An XMLElement contains a Attributes2 object
 * [see the Java 1.6 API] and an ArrayList of subobjects that can be accessed by getSubObjects().  An Object in the subObjects can
 * either be a String or another XMLElement.  Order is preserved with the original tree.  For example, for the content of the 
 * message element in:
 * <code>
 * <pre>  &lt;content&gt;
 *     &lt;message&gt;
 *       This &lt;bold&gt;is&lt;/bold&gt; cool!
 *     &lt;/message&gt;
 *   &lt;/content&gt;</pre>
 * </code>
 * would be transformed to {"This ", XMLElement [bold], " cool!"}, whose elements would be accessed via getSubObjects().get(0),
 * getSubObjects().get(1), and getSubObjects().get(2) respectively.  If you're not transmitting a message where order matters [this
 * would be a majority of the time], you can just use getElement() which will get the first XMLElement whose tag name matches
 * the String passed to it.
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

public class Element
{
	static final long serialVersionUID = 0L;

	String name = null;
	ArrayList subObjects = null;
	ArrayList<Element> elements = null;
	Attributes attrs = null;
	
	/**
	 * The defualt constructor used by the XMLParser.
	 * @param nameTag the name of the tag
	 * @param a the attributes associated with the element
	 */
	public Element(String nameTag, Attributes a)
	{
		name = nameTag;
		attrs = a;
		
		subObjects = new ArrayList();
		elements = new ArrayList<Element>();
	}
	
	/**
	 * The defualt constructor.
	 * @param nameTag the name of the tag
	 */
	public Element(String nameTag)
	{
		this(nameTag, new Attributes());
	}
	
	/**
	 * The defualt constructor used by the XMLParser.
	 * @param nameTag the name of the tag
	 * @param a the attributes associated with the element
	 */
	public Element(Element parent, String nameTag, Attributes a)
	{
		this(nameTag, a);
		
		parent.addElement(this);
	}
	
	/**
	 * The defualt constructor.
	 * @param nameTag the name of the tag
	 * @param parent the parent element
	 */
	public Element(Element parent, String nameTag)
	{
		this(parent, nameTag, new Attributes());
	}
	
	public void addText(Object o)
	{
		String s = o.toString();
		
		int size = subObjects.size();
		if(size != 0)
		{
			if(subObjects.get(size - 1) instanceof String)
				s = (String)(subObjects.remove(size - 1)) + s;
		}
		
		subObjects.add(s);
	}
	
	/**
	 * Adds a child Element to the Element.
	 * @param element the Element to add
	 */
	public void addElement(Element element)
	{
		subObjects.add(element);
		elements.add(element);
	}
	
	/**
	 * Returns an ArrayList of the subobjects [Strings and Elements] within the element.
	 */
	public ArrayList getSubObjects()
	{
		return subObjects;
	}
	
	/**
	 * Returns an ArrayList just the sub-Elements of this Element
	 * @return the child Elements of this Element
	 */
	public ArrayList<Element> getElements()
	{
		return elements;
	}
	
	/**
	 * Returns the name of the tag.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the atributes associated with the element.
	 */
	public Attributes getAttributes()
	{
		return attrs;
	}
	
	/**
	 * Searches the subelements of this element for an XMLElement with the specified tag name.
	 * @param name the name to search for
	 * @return the first matching element or null if there are no matches
	 */
	public Element get(String name)
	{
		for(Element e : elements)
			if(e.getName().equals(name))
				return e;
		
		return null;
	}
	
	/**
	 * Returns an ArrayList<Element> of all the elements that match the given name
	 * @return a possibly empty ArrayList<Element> of matches
	 */
	public ArrayList<Element> getElements(String name)
	{
		ArrayList<Element> res = new ArrayList<Element>();
		
		for(Element e : elements)
			if(e.getName().equals(name))
				res.add(e);
		
		return res;
	}
	
	public String text()
	{
		if(subObjects.size() == 1)
			if(subObjects.get(0) instanceof String)
				return (String)(subObjects.get(0));
		
		return null;
	}
	
	public String toString()
	{
		String s = "<" + name;
		
		for(int i = 0; i < attrs.getLength(); i++)
			s += " " + attrs.getLocalName(i) + "=\"" + attrs.getValue(i) + "\"";
		
		if(subObjects.size() == 0)
			return s + " />";
		else
			s += ">";
		
		for(Object o : subObjects)
			s += o.toString();
		
		return s + "</" + name + ">";
	}
}
