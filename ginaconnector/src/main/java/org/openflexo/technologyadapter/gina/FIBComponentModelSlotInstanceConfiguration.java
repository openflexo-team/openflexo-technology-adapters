/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.gina;

import java.io.FileNotFoundException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;

public class FIBComponentModelSlotInstanceConfiguration
		extends FreeModelSlotInstanceConfiguration<GINAFIBComponent, FIBComponentModelSlot> {

	public static enum FIBComponentModelSlotInstanceConfigurationOption implements ModelSlotInstanceConfigurationOption {

		/**
		 * Use the component given as template in model slot, do not modify it
		 */
		ReadOnlyUseFIBComponent,
		/**
		 * Use the component given as template in model slot, and allow to dynamically modify it
		 */
		ReadWriteFIBComponent;

		@Override
		public String getDescriptionKey() {
			return name() + "_description";
		}
	}

	protected FIBComponentModelSlotInstanceConfiguration(FIBComponentModelSlot ms, AbstractVirtualModelInstance<?, ?> virtualModelInstance,
			FlexoProject project) {
		super(ms, virtualModelInstance, project);
	}

	@Override
	protected void initDefaultOptions() {
		options.add(FIBComponentModelSlotInstanceConfigurationOption.ReadOnlyUseFIBComponent);
		options.add(FIBComponentModelSlotInstanceConfigurationOption.ReadWriteFIBComponent);
		setOption(FIBComponentModelSlotInstanceConfigurationOption.ReadOnlyUseFIBComponent);
	}

	@Override
	public void setOption(ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		// TODO : add specific options here
	}

	@Override
	protected FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot> configureModelSlotInstance(
			FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot> msInstance) {

		/*System.out.println("C'est parti pour configurer le modelSlot");
		System.out.println("option=" + getOption());
		System.out.println("modelSlot=" + getModelSlot());
		Thread.dumpStack();
		System.out.println("getModelSlot().getTemplateResource()=" + getModelSlot().getTemplateResource());*/

		try {
			if (getOption() == FIBComponentModelSlotInstanceConfigurationOption.ReadOnlyUseFIBComponent) {
				// In this case, we will use the FIBComponent as read-only
				// The accessed ResourceData will be the template FIBComponent
				msInstance.setAccessedResourceData(getModelSlot().getTemplateResource().getResourceData(null));
				// System.out.println("rd=" + msInstance.getAccessedResourceData());
			} else if (getOption() == FIBComponentModelSlotInstanceConfigurationOption.ReadWriteFIBComponent) {
				// In this case, we have to manage a copy of template
				/*resource = createProjectSpecificEmptyResource(msInstance, getModelSlot());
				if (getResource() != null) {
					RD resourceData = getResource().getResourceData(null);
					if (resourceData != null) {
						msInstance.setAccessedResourceData(resourceData);
					}
					else {
						msInstance.setResource(getResource());
					}
				}
				else {
					logger.warning("Could not create ProjectSpecificEmtpyResource for model slot " + getModelSlot());
				}*/
				System.out.println("ReadWriteFIBComponent for FIBComponentModelSlot : Not implemented yet " + getModelSlot());
			}
			return msInstance;
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
		return null;
	}

	@Override
	public boolean isValidConfiguration() {
		if (getOption() == FIBComponentModelSlotInstanceConfigurationOption.ReadOnlyUseFIBComponent) {
			return true;
		} else if (getOption() == FIBComponentModelSlotInstanceConfigurationOption.ReadWriteFIBComponent) {
			setErrorMessage(FlexoLocalization.localizedForKey("not_implemented_yet"));
			return false;
		}
		return false;
	}

}
