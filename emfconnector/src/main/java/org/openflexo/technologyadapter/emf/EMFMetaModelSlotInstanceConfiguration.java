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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
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
	public List<org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption> getAvailableOptions() {
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
