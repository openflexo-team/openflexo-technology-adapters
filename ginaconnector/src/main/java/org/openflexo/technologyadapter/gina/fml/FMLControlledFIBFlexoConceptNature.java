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
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptNature;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;

/**
 * Define the "FML-controlled FIBComponent" nature of a {@link FlexoConcept}<br>
 * 
 * A {@link FMLControlledFIBFlexoConceptNature} might be seen as an interpretation of a given {@link FlexoConcept}
 * 
 * @author sylvain
 * 
 */
public class FMLControlledFIBFlexoConceptNature implements FlexoConceptNature {

	public static FMLControlledFIBFlexoConceptNature INSTANCE = new FMLControlledFIBFlexoConceptNature();

	// Prevent external instantiation
	private FMLControlledFIBFlexoConceptNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModel} might be interpreted as a FML-controlled FIBComponent
	 */
	@Override
	public boolean hasNature(FlexoConcept flexoConcept) {

		// VirtualModel should have one and only one FIBComponentModelSlot
		if (flexoConcept.getDeclaredProperties(FIBComponentModelSlot.class).size() != 1) {
			return false;
		}

		FIBComponentModelSlot fibMS = flexoConcept.getDeclaredProperties(FIBComponentModelSlot.class).get(0);

		// The unique FIBComponentModelSlot should have one template resource
		if (fibMS.getTemplateResource() == null) {
			return false;
		}

		return true;
	}

	public static FIBComponentModelSlot getFIBComponentModelSlot(FlexoConcept flexoConcept) {
		return INSTANCE._getFIBComponentModelSlot(flexoConcept);
	}

	public static GINAFIBComponent getFIBComponent(FlexoConcept flexoConcept) {
		FIBComponentModelSlot modelSlot = getFIBComponentModelSlot(flexoConcept);
		if (modelSlot != null && modelSlot.getTemplateResource() != null) {
			try {
				return modelSlot.getTemplateResource().getResourceData();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FlexoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private static FIBComponentModelSlot _getFIBComponentModelSlot(FlexoConcept flexoConcept) {
		if (flexoConcept != null && flexoConcept.getDeclaredProperties(FIBComponentModelSlot.class).size() == 1) {
			return flexoConcept.getDeclaredProperties(FIBComponentModelSlot.class).get(0);
		}
		return null;
	}
}
