/*
 * (c) Copyright 2014 -  Openflexo
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
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;

/**
 * Widget allowing to select an XMLProperty in an XMLType.<br>
 * 
 * This widget provides many configuration options:
 * <ul>
 * <li>context: required, defines XMLType to browse</li>
  * <li>selectObjectProperties, indicated if object properties should be retrieved</li>
 * <li>selectDataProperties, indicated if data properties should be retrieved</li>
 * </ul>
 * 
 * @author xtpf, based on sguerin's code
 * 
 */
@SuppressWarnings("serial")
public class FIBXMLPropertySelector extends FIBFlexoObjectSelector<XMLProperty> {

	static final Logger logger = Logger.getLogger(FIBXMLPropertySelector.class.getPackage().getName());

	public static final Resource FIB_FILE_NAME = ResourceLocator.locateResource("Fib/widgets/FIBXMLPropertySelector.fib");

	private XMLType context;
	private boolean selectObjectProperties = true;
	private boolean selectDataProperties = true;


	public FIBXMLPropertySelector(XMLProperty editedObject) {
		super(editedObject);
	}

	@Override
	public void delete() {
		super.delete();
		context = null;
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE_NAME;
	}
	
	@Override
	public Class<XMLProperty> getRepresentedType() {
		return XMLProperty.class;
	}

	@Override
	public String renderedString(XMLProperty editedObject) {
		if (editedObject != null) {
			return editedObject.getName();
		}
		return "";
	}

	public XMLType getContext() {
		return context;
	}

	@CustomComponentParameter(name = "context", type = CustomComponentParameter.Type.MANDATORY)
	public void setContext(XMLType context) {
		this.context = context;
	}

	public String getContextOntologyURI() {
		if (getContext() != null) {
			return getContext().getURI();
		}
		return null;
	}
	
	
	public boolean getSelectObjectProperties() {
		return selectObjectProperties;
	}

	@CustomComponentParameter(name = "selectObjectProperties", type = CustomComponentParameter.Type.OPTIONAL)
	public void setSelectObjectProperties(boolean selectObjectProperties) {
		this.selectObjectProperties = selectObjectProperties;
	}

	public boolean getSelectDataProperties() {
		return selectDataProperties;
	}

	@CustomComponentParameter(name = "selectDataProperties", type = CustomComponentParameter.Type.OPTIONAL)
	public void setSelectDataProperties(boolean selectDataProperties) {
		this.selectDataProperties = selectDataProperties;
	}

	
	private XMLModelSlot modelSlot;

	public XMLModelSlot getModelSlot() {
		return modelSlot;
	}

	public void setModelSlot(XMLModelSlot modelSlot) {
		this.modelSlot = modelSlot;
	}

	/**
	 * Return a metamodel adressed by a model slot
	 * 
	 * @return
	 */
	public FlexoMetaModel getAdressedFlexoMetaModel() {
		if (modelSlot instanceof TypeAwareModelSlot) {
			TypeAwareModelSlot typeAwareModelSlot = modelSlot;
			return typeAwareModelSlot.getMetaModelResource().getMetaModelData();
		}
		return null;
	}

}
