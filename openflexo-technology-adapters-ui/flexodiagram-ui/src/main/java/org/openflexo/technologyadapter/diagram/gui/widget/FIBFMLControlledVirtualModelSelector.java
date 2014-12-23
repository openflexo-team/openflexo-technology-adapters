/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.diagram.gui.widget;

import java.util.logging.Logger;

import org.openflexo.fml.controller.widget.FIBVirtualModelSelector;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.rm.Resource;
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
	protected boolean isAcceptableValue(Object o) {
		if (getDiagramSpecification() == null) {
			return super.isAcceptableValue(o);
		} else {
			if (super.isAcceptableValue(o)) {
				if(o instanceof VirtualModelResource){
					return hasDiagramSpecification((VirtualModelResource)o);
				}
			}
			return false;
		}
	}

	public boolean hasDiagramSpecification(VirtualModelResource virtualModelResource){
		if(virtualModelResource!=null){
			VirtualModel virtualModel = virtualModelResource.getVirtualModel();
			if(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)
					&& FMLControlledDiagramVirtualModelNature.hasDiagramSpecification(virtualModel,  getDiagramSpecification())){
				return true;
			}
		}
		return false;
	}
	
}