/*
 * FindBugs - Find bugs in Java programs
 * Copyright (C) 2003-2005 University of Maryland
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package edu.umd.cs.findbugs;

import java.io.IOException;

import edu.umd.cs.findbugs.xml.XMLAttributeList;
import edu.umd.cs.findbugs.xml.XMLOutput;
import edu.umd.cs.findbugs.xml.XMLWriteable;

/**
 * Name/value metadata pair that may be attached to a BugInstance.
 * These are different from BugAnnotations because they are not
 * meant to be shown directly to the user.
 * 
 * @author David Hovemeyer
 */
public class BugProperty implements XMLWriteable {
	private String name;
	private String value;
	private BugProperty next;
	
	/**
	 * Constructor.
	 * 
	 * @param name  name of property
	 * @param value value of property
	 */
	BugProperty(String name, String value) {
		this.name = name.intern();
		this.value = value;
	}
	
	/**
	 * Get name of property.
	 * 
	 * @return name of property
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get value of property.
	 * 
	 * @return value of property
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Set value of property.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Set next property in list.
	 * 
	 * @param next next property in list
	 */
	void setNext(BugProperty next) {
		this.next = next;
	}
	
	/**
	 * Get next property in list.
	 * 
	 * @return next property in list
	 */
	BugProperty getNext() {
		return next;
	}

	/* (non-Javadoc)
	 * @see edu.umd.cs.findbugs.xml.XMLWriteable#writeXML(edu.umd.cs.findbugs.xml.XMLOutput)
	 */
	public void writeXML(XMLOutput xmlOutput) throws IOException {
		xmlOutput.openCloseTag(
				"Property",
				new XMLAttributeList()
					.addAttribute("name", getName())
					.addAttribute("value", getValue())
		);
	}
}
