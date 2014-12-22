package org.openflexo.technologyadapter.emf;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fmlrt.ModelSlotInstance;
import org.openflexo.foundation.fmlrt.View;
import org.openflexo.foundation.fmlrt.VirtualModelInstance;
import org.openflexo.foundation.fmlrt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fmlrt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;

public class EMFMetaModelSlotInstanceConfiguration extends ModelSlotInstanceConfiguration<EMFMetaModelSlot, EMFMetaModel> {

	private static final Logger logger = Logger.getLogger(EMFMetaModelSlotInstanceConfiguration.class.getPackage().getName());

	protected List<ModelSlotInstanceConfigurationOption> options;

	protected EMFMetaModelSlotInstanceConfiguration(EMFMetaModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
		options = new ArrayList<ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption>();
		options.add(DefaultModelSlotInstanceConfigurationOption.SelectExistingMetaModel);
	}

	@Override
	public List<org.openflexo.foundation.fmlrt.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption> getAvailableOptions() {
		return options;
	}

	@Override
	public ModelSlotInstance<EMFMetaModelSlot, EMFMetaModel> createModelSlotInstance(VirtualModelInstance msInstance, View view) {
		logger.warning("Please implement me !!!!");
		return null;
	}

	@Override
	public TechnologyAdapterResource<EMFMetaModel, ?> getResource() {
		logger.warning("Please implement me !!!!");
		return null;
	}

}
