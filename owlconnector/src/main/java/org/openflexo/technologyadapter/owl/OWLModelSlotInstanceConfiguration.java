/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResource;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResourceFactory;
import org.openflexo.toolbox.StringUtils;

public class OWLModelSlotInstanceConfiguration extends TypeAwareModelSlotInstanceConfiguration<OWLOntology, OWLOntology, OWLModelSlot> {

	private static final Logger logger = Logger.getLogger(TypeAwareModelSlotInstanceConfiguration.class.getPackage().getName());

	protected OWLModelSlotInstanceConfiguration(OWLModelSlot ms, FlexoConceptInstance fci, FlexoResourceCenter<?> rc) {
		super(ms, fci, rc);
		// options.add(DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel);
		if (fci != null) {
			setModelUri(fci.getResourceCenter().getDefaultBaseURI() + "/Models/myOntology");
		}
		setRelativePath("/");
		setFilename("myOntology" + OWLOntologyResourceFactory.OWL_FILE_EXTENSION);
	}

	@Override
	protected TypeAwareModelSlotInstance<OWLOntology, OWLOntology, OWLModelSlot> configureModelSlotInstance(
			TypeAwareModelSlotInstance<OWLOntology, OWLOntology, OWLModelSlot> msInstance, View view) {
		if (getOption() == DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel) {
			modelResource = createSharedEmptyModel(msInstance, getModelSlot());
			if (modelResource != null) {
				msInstance.setAccessedResourceData(getModelResource().getModel());
				msInstance.setModelURI(getModelResource().getURI());
			}
			else {
				logger.warning("Could not create SharedEmptyModel for model slot " + getModelSlot());
			}
			return msInstance;
		}
		else {
			return super.configureModelSlotInstance(msInstance, view);
		}
	}

	private OWLOntologyResource createSharedEmptyModel(TypeAwareModelSlotInstance<OWLOntology, OWLOntology, OWLModelSlot> msInstance,
			OWLModelSlot modelSlot) {
		return (OWLOntologyResource) modelSlot.createSharedEmptyModel(getResourceCenter(), getRelativePath(), getFilename(), getModelUri(),
				modelSlot.getMetaModelResource());
	}

	/*@Override
	public void setOption(org.openflexo.foundation.ontology.fml.rt.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		if (option == DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel) {
			modelUri = getAction().getFocusedObject().getProject().getURI() + "/Models/myOntology";
			relativePath = "/";
			filename = "myOntology" + getModelSlot().getTechnologyAdapter().getExpectedOntologyExtension();
		} else if (option == DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel) {
			modelUri = "ResourceCenter/Models/";
			relativePath = "/";
			filename = "myOntology" + getModelSlot().getTechnologyAdapter().getExpectedOntologyExtension();
		}
	}*/

	@Override
	public boolean isValidConfiguration() {
		if (getOption() == DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel) {
			return getResourceCenter() != null && getResourceCenter() instanceof FileSystemBasedResourceCenter
					&& StringUtils.isNotEmpty(getModelUri()) && StringUtils.isNotEmpty(getRelativePath())
					&& StringUtils.isNotEmpty(getFilename());
		}
		else {
			return super.isValidConfiguration();
		}
	}

	@Override
	public boolean isURIEditable() {
		return true;
	}

	@Override
	protected boolean checkValidFileName() {
		if (!super.checkValidFileName()) {
			return false;
		}
		if (!getFilename().endsWith(OWLOntologyResourceFactory.OWL_FILE_EXTENSION)) {
			setErrorMessage(
					getModelSlot().getModelSlotTechnologyAdapter().getLocales().localizedForKey("file_name_should_end_with_.owl_suffix"));
			return false;
		}
		return true;
	}

}
