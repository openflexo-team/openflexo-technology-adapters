package org.openflexo.technologyadapter.xml;

import org.openflexo.foundation.technologyadapter.TypeAwareModelSlotInstanceConfiguration;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;

public class XMLModelSlotInstanceConfiguration extends TypeAwareModelSlotInstanceConfiguration<XMLModel, XMLMetaModel, XMLModelSlot> {

	protected XMLModelSlotInstanceConfiguration(XMLModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
	}

	@Override
	public void setOption(org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		if (option == DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel) {
			modelUri = getAction().getFocusedObject().getProject().getURI() + "/Models/myXMLFile";
			relativePath = "/";
			filename = "myXMLFile" + ((XMLTechnologyAdapter) getModelSlot().getTechnologyAdapter()).getExpectedModelExtension(getModelSlot().getMetaModelResource());
		} /*else if (option == DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel) {
			modelUri = "ResourceCenter/Models/";
			relativePath = "/";
			filename = "myXMLFile"
					+ getModelSlot().getTechnologyAdapter().getExpectedModelExtension(
							(XSDMetaModelResource) getModelSlot().getMetaModelResource());
			}*/
	}

	@Override
	public boolean isURIEditable() {
		return false;
	}

}
