/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.xml.gui.widget;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.openflexo.components.widget.FIBFlexoObjectSelector;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
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
 * @author xtof, based on sguerin's code
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
		performFireModelUpdated();
	}

	private boolean modelWillBeUpdated = false;
	
	private void performFireModelUpdated() {
		if (modelWillBeUpdated) {
			return;
		} else {
			modelWillBeUpdated = true;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					getPropertyChangeSupport().firePropertyChange("context", null, context);
					modelWillBeUpdated = false;
				}
			});
		}
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

	

}
