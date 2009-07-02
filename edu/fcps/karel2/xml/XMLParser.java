package edu.fcps.karel2.xml;

import edu.fcps.karel2.util.*;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.Locator;
import org.xml.sax.ext.Attributes2Impl;
import org.apache.xerces.parsers.SAXParser;

/**
 * The XMLParser implements ContentHandler to use the SAXParser contained in Apache's J-Xerces2 implementation.  The parser uses
 * callbacks to construct an XMLElement representing the base of the
 * content XML tree.  See http://netchat.tjhsst.edu/trac/netchat/wiki/NCP for more information.
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

public class XMLParser implements ContentHandler
{
	private SAXParser parser;
	private ArrayList<Element> elements = null;
	private Element rootElement = null;
	
	/**
	 * The default constructor.
	 */
	public XMLParser()
	{
		Debug.println("Initializing base parser objects...", 3);
		
		Debug.println("Creating parser...", 3);
		parser = new SAXParser();
		
		Debug.println("Setting Handler...", 3);
		parser.setContentHandler(this);
	}
	
	/**
	 * Initiates message parsing, reading the XML document from the supplied Reader.
	 * @param r the reader that buffers the message
	 */
	public Element parse(Reader r)
	{
		return parse(new InputSource(r));
	}
	
	/**
	 * Initiates message parsing, reading the XML document from the supplied InputStream.
	 * @param is the InputStream that buffers the message
	 */
	public Element parse(InputStream is)
	{
		return parse(new InputSource(is));
	}

	/**
	 * Initiates message parsing, reading the XML document from the supplied InputSource.
	 * @param is the InputSource that buffers the message
	 */
	public Element parse(InputSource is)
	{
		try{
			parser.parse(is);
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
		return rootElement;
	}
	
	public void startDocument()
	{
		Debug.println("Initializing parser objects...", 4);
		elements = new ArrayList<Element>();
		
		Debug.println("Incoming XML message, starting parser...", 4);
	}
	public void endDocument()
	{
		
	}
	public void processingInstruction(String target, String data)
	{
	}
	public void startPrefixMapping(String prefix, String uri)
	{
	}
	public void endPrefixMapping(String prefix)
	{
	}
	public void startElement(String namespaceURI, String localName, String rawName, Attributes as)
	{	
		if(Debug.debugLevel() >= 5)
		{
			Debug.println("Start of element received:", 5);
			Debug.println("  Namespace URI: " + namespaceURI, 5);
			Debug.println("  Local Name: " + localName, 5);
			Debug.println("  Raw Name: " + rawName, 5);
			Debug.println("  Attributes:", 5);
			for(int i = 0; i < as.getLength(); i++)
			{
				Debug.println("    Namespace URI: " + as.getURI(i), 5);
				Debug.println("    Type: " + as.getType(i), 5);
				Debug.println("    Qualified (prefixed) Name: " + as.getQName(i), 5);
				Debug.println("    Local Name: " + as.getLocalName(i), 5);
				Debug.println("    Value: " + as.getValue(i), 5);
			}
		}
		
		edu.fcps.karel2.xml.Attributes a = new edu.fcps.karel2.xml.Attributes();
		a.setAttributes(as);

		Element e = new Element(localName, a);

		if(rootElement == null)
			rootElement = e;
		else
			elements.get(elements.size() - 1).addElement(e);
		
		elements.add(e);
		
		Debug.println("End of start of element.", 5);
	}
	public void endElement(String namespaceURI, String localName, String rawName)
	{
		if(Debug.debugLevel() >= 5)
		{
			Debug.println("End of element received:", 5);
			Debug.println("  Namespace URI: " + namespaceURI, 5);
			Debug.println("  Local Name: " + localName, 5);
			Debug.println("  Raw Name: " + rawName, 5);
			Debug.println("End of end of element.", 5);
		}
		
		int elemLen = elements.size();
		
		if(elemLen > 0)
			elements.remove(elemLen - 1);
	}
	public void characters(char[] ch, int s, int len)
	{
		String content = new String(ch, s, len);
		
		if(content.length() != 0)
		{
			if(Debug.debugLevel() >= 5)
			{
				Debug.println("Characters received:", 5);
				Debug.println("  Characters: " + content, 5);
				Debug.println("End of characters.", 5);
				Debug.println("Adding content...", 5);
			}
			
			elements.get(elements.size() - 1).addText(content);
		}
	}
	
	public void error(SAXParseException e) //We use the default error and fatal error handlers
	{
	}
	public void fatalError(SAXParseException e)
	{
	}
	
	public void setDocumentLocator(Locator loc)
	{
		
	}
	public void ignorableWhitespace(char[] ch, int s, int len)
	{
		if(Debug.debugLevel() >= 5)
		{
			Debug.println("Ignorable whitespace:", 5);
			Debug.println("  Characters: " + new String(ch, s, len), 5);
			Debug.println("End of ignorable whitespace.", 5);
		}
	}
	public void skippedEntity(String entName)
	{
		Debug.println("Skipped entity: " + entName, 1);
	}
}

