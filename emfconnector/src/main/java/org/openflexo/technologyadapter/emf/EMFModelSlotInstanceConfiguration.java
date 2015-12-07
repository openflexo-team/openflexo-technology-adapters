/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf;

import org.openflexo.foundation.fml.rt.action.AbstractCreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlotInstanceConfiguration;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;

public class EMFModelSlotInstanceConfiguration extends TypeAwareModelSlotInstanceConfiguration<EMFModel, EMFMetaModel, EMFModelSlot> {

	protected EMFModelSlotInstanceConfiguration(EMFModelSlot ms, AbstractCreateVirtualModelInstance<?, ?, ?, ?> action) {
		super(ms, action);
		setModelUri(getAction().getProject().getURI() + "/Models/myEMFModel");
		setRelativePath("/");
		setFilename("myEMFModel" + getModelSlot().getModelSlotTechnologyAdapter()
				.getExpectedModelExtension((EMFMetaModelResource) getModelSlot().getMetaModelResource()));
	}

	/*@Override
	public void setOption(org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		if (option == DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel) {
			modelUri = getAction().getFocusedObject().getProject().getURI() + "/Models/myEMFModel";
			relativePath = "/";
			filename = "myEMFModel"
					+ getModelSlot().getTechnologyAdapter().getExpectedModelExtension(
							(EMFMetaModelResource) getModelSlot().getMetaModelResource());
		} else if (option == DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel) {
			modelUri = "ResourceCenter/Models/";
			relativePath = "/";
			filename = "myEMFModel"
					+ getModelSlot().getTechnologyAdapter().getExpectedModelExtension(
							(EMFMetaModelResource) getModelSlot().getMetaModelResource());
			}
	}*/

	@Override
	public boolean isURIEditable() {
		return false;
	}

	@Override
	protected boolean checkValidFileName() {
		if (!super.checkValidFileName()) {
			return false;
		}
		String expectedSuffix = getModelSlot().getModelSlotTechnologyAdapter()
				.getExpectedModelExtension((EMFMetaModelResource) getModelSlot().getMetaModelResource());
		if (!getFilename().endsWith(expectedSuffix)) {
			setErrorMessage(FlexoLocalization.localizedForKey("file_name_should_end_with_right_suffix" + " : " + expectedSuffix));
			return false;
		}
		return true;
	}

}
