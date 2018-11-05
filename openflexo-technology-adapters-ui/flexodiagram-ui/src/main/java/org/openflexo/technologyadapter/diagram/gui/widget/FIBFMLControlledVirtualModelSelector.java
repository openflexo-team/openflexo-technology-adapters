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

package org.openflexo.technologyadapter.diagram.gui.widget;

import java.util.logging.Logger;

import org.openflexo.fml.controller.widget.FIBVirtualModelSelector;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;

/**
 * Widget allowing to select a virtual model with a FML control
 * 
 * @author vincent
 * 
 */
@SuppressWarnings("serial")
public class FIBFMLControlledVirtualModelSelector extends FIBVirtualModelSelector {

	static final Logger logger = Logger.getLogger(FIBFMLControlledVirtualModelSelector.class.getPackage().getName());

	public static Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FMLControlledVirtualModelSelector.fib");

	public FIBFMLControlledVirtualModelSelector(VirtualModelResource editedObject) {
		super(editedObject);
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	private DiagramSpecification diagramSpecification;

	public DiagramSpecification getDiagramSpecification() {
		return diagramSpecification;
	}

	@CustomComponentParameter(name = "diagramSpecification", type = CustomComponentParameter.Type.MANDATORY)
	public void setDiagramSpecification(DiagramSpecification diagramSpecification) {
		this.diagramSpecification = diagramSpecification;
	}

	@Override
	public boolean isAcceptableValue(Object o) {
		if (super.isAcceptableValue(o) && o instanceof VirtualModelResource) {
			if (getDiagramSpecification() == null) {
				return isFMLControlledVirtualModel((VirtualModelResource) o);
			}
			else {
				return hasDiagramSpecification((VirtualModelResource) o);
			}
		}
		return false;
	}

	public boolean hasDiagramSpecification(VirtualModelResource virtualModelResource) {
		if (virtualModelResource != null) {
			VirtualModel virtualModel = virtualModelResource.getVirtualModel();
			if (virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)
					&& FMLControlledDiagramVirtualModelNature.hasDiagramSpecification(virtualModel, getDiagramSpecification())) {
				return true;
			}
		}
		return false;
	}

	public boolean isFMLControlledVirtualModel(VirtualModelResource virtualModelResource) {
		if (virtualModelResource != null) {
			VirtualModel virtualModel = virtualModelResource.getVirtualModel();
			if (virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
				return true;
			}
		}
		return false;
	}

}
