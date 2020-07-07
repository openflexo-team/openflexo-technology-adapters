/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.gina.fml;

import java.io.FileNotFoundException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.FMLCompilationUnit;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelNature;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;

/**
 * Define the "FML-controlled FIBComponent" nature of a {@link VirtualModel}<br>
 * 
 * A {@link FMLControlledFIBVirtualModelNature} might be seen as an interpretation of a given {@link FMLRTVirtualModelInstance}
 * 
 * @author sylvain
 * 
 */
public class FMLControlledFIBVirtualModelNature implements VirtualModelNature {

	public static FMLControlledFIBVirtualModelNature INSTANCE = new FMLControlledFIBVirtualModelNature();

	// Prevent external instantiation
	private FMLControlledFIBVirtualModelNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModel} might be interpreted as a FML-controlled FIBComponent
	 */
	@Override
	public boolean hasNature(VirtualModel virtualModel) {

		// VirtualModel should have one and only one FIBComponentModelSlot
		if (virtualModel.getModelSlots(FIBComponentModelSlot.class).size() == 0) {
			return false;
		}

		FIBComponentModelSlot fibMS = virtualModel.getModelSlots(FIBComponentModelSlot.class).get(0);

		// The unique FIBComponentModelSlot should have one template resource
		if (fibMS.getTemplateResource() == null) {
			return false;
		}

		return true;
	}

	public static FIBComponentModelSlot getFIBComponentModelSlot(FMLCompilationUnit compilationUnit) {
		return INSTANCE._getFIBComponentModelSlot(compilationUnit.getVirtualModel());
	}

	public static GINAFIBComponent getFIBComponent(FMLCompilationUnit compilationUnit) {
		FIBComponentModelSlot modelSlot = getFIBComponentModelSlot(compilationUnit);
		if (modelSlot != null && modelSlot.getTemplateResource() != null) {
			try {
				return modelSlot.getTemplateResource().getResourceData();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				e.printStackTrace();
			} catch (FlexoException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static FIBComponentModelSlot _getFIBComponentModelSlot(VirtualModel virtualModel) {
		if (virtualModel != null && virtualModel.getModelSlots(FIBComponentModelSlot.class).size() >= 1) {
			return virtualModel.getModelSlots(FIBComponentModelSlot.class).get(0);
		}
		return null;
	}
}
