package org.openflexo.technologyadapter.xml;

import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlotInstanceConfiguration;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;

public class XMLModelSlotInstanceConfiguration extends TypeAwareModelSlotInstanceConfiguration<XMLModel, XMLMetaModel, XMLModelSlot> {

	protected XMLModelSlotInstanceConfiguration(XMLModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
		setModelUri(getAction().getFocusedObject().getProject().getURI() + "/Models/myXMLFile");
		setRelativePath("/");
		setFilename("myXMLFile"
				+ ((XMLTechnologyAdapter) getModelSlot().getModelSlotTechnologyAdapter()).getExpectedModelExtension(getModelSlot()
						.getMetaModelResource()));
	}

	/*@Override
	public void setOption(org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		if (option == DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel) {
			modelUri = getAction().getFocusedObject().getProject().getURI() + "/Models/myXMLFile";
			relativePath = "/";
			filename = "myXMLFile"
					+ ((XMLTechnologyAdapter) getModelSlot().getTechnologyAdapter()).getExpectedModelExtension(getModelSlot()
							.getMetaModelResource());
		} else if (option == DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel) {
			modelUri = "ResourceCenter/Models/";
			relativePath = "/";
			filename = "myXMLFile"
					+ getModelSlot().getTechnologyAdapter().getExpectedModelExtension(
							(XSDMetaModelResource) getModelSlot().getMetaModelResource());
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
		String expectedSuffix = ((XMLTechnologyAdapter) getModelSlot().getModelSlotTechnologyAdapter()).getExpectedModelExtension(getModelSlot()
				.getMetaModelResource());
		if (!getFilename().endsWith(expectedSuffix)) {
			setErrorMessage(FlexoLocalization.localizedForKey("file_name_should_end_with_right_suffix" + " : " + expectedSuffix));
			return false;
		}
		return true;
	}
}
