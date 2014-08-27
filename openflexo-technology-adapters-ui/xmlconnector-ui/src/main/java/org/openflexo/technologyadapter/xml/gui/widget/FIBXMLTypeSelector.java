/*
 * (c) Copyright 2014- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.xml.gui.widget;

import java.util.logging.Logger;

import org.openflexo.components.widget.FIBFlexoObjectSelector;
import org.openflexo.foundation.technologyadapter.InformationSpace;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;

/**
 * Widget allowing to select an XMLType<br>
 * 
 * This widget provides many configuration options:
 * <ul>
 * <li>context: required, defines metamodel(XSD) context</li>
 * </ul>
 * 
 * @author xtof, adapted from sguerin's code
 * 
 */
@SuppressWarnings("serial")
public class FIBXMLTypeSelector extends FIBFlexoObjectSelector<XMLType> {
	static final Logger logger = Logger.getLogger(FIBXMLTypeSelector.class.getPackage().getName());

	public static final Resource FIB_FILE =  ResourceLocator.locateResource("Fib/widgets/FIBXMLTypeSelector.fib");

	private InformationSpace informationSpace;
	private XMLMetaModel context;

	public FIBXMLTypeSelector(XMLType editedObject) {
		super(editedObject);
	}

	@Override
	public void delete() {
		super.delete();
		context = null;
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	@Override
	public Class<XMLType> getRepresentedType() {
		return XMLType.class;
	}


	@Override
	public String renderedString(XMLType editedObject) {
		if (editedObject != null) {
			return editedObject.getName();
		}
		return "";
	}

	public String getContextMetamodelURI() {
		if (getContext() != null) {
			return getContext().getURI();
		}
		return null;
	}


	public XMLMetaModel getContext() {
		return context;
	}

	@CustomComponentParameter(name = "context", type = CustomComponentParameter.Type.MANDATORY)
	public void setContext(XMLMetaModel context) {
		this.context = context;
	}


	

}
