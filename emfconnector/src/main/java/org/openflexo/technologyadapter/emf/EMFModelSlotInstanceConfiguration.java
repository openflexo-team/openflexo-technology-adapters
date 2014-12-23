package org.openflexo.technologyadapter.emf;

import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlotInstanceConfiguration;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;

public class EMFModelSlotInstanceConfiguration extends TypeAwareModelSlotInstanceConfiguration<EMFModel, EMFMetaModel, EMFModelSlot> {

	protected EMFModelSlotInstanceConfiguration(EMFModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
		setModelUri(getAction().getFocusedObject().getProject().getURI() + "/Models/myEMFModel");
		setRelativePath("/");
		setFilename("myEMFModel"
				+ getModelSlot().getTechnologyAdapter().getExpectedModelExtension(
						(EMFMetaModelResource) getModelSlot().getMetaModelResource()));
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
		String expectedSuffix = getModelSlot().getTechnologyAdapter().getExpectedModelExtension(
				(EMFMetaModelResource) getModelSlot().getMetaModelResource());
		if (!getFilename().endsWith(expectedSuffix)) {
			setErrorMessage(FlexoLocalization.localizedForKey("file_name_should_end_with_right_suffix" + " : " + expectedSuffix));
			return false;
		}
		return true;
	}

}
