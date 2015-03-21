/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.fml.FMLTechnologyContextManager;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.technologyadapter.emf.metamodel.io.MMFromJarsInDirIODelegate;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;

public class EMFTechnologyContextManager extends TechnologyContextManager<EMFTechnologyAdapter> {

	protected static final Logger logger = Logger.getLogger(EMFTechnologyContextManager.class.getPackage().getName());

	/** Stores all known metamodels where key is the URI of metamodel */
	protected Map<String, EMFMetaModelResource> metamodels = new HashMap<String, EMFMetaModelResource>();
	/** Stores all known models where key is the URI of model */
	protected Map<String, EMFModelResource> models = new HashMap<String, EMFModelResource>();

	/** Stores a reference to EMF Registry instance in order to register every MM available */

	protected Resource.Factory.Registry EMFRegistry = Resource.Factory.Registry.INSTANCE;
	protected Map<String, Object> EMFExtensionToFactoryMap;

	public EMFTechnologyContextManager(EMFTechnologyAdapter adapter, FlexoResourceCenterService resourceCenterService) {
		super(adapter, resourceCenterService);
		EMFExtensionToFactoryMap = EMFRegistry.getExtensionToFactoryMap();
	}

	@Override
	public EMFTechnologyAdapter getTechnologyAdapter() {
		return (EMFTechnologyAdapter) super.getTechnologyAdapter();
	}

	public EMFModelResource getModel(File modelFile) {
		return models.get(modelFile);
	}

	/**
	 * Called when a new meta model was registered, notify the {@link TechnologyContextManager}
	 * 
	 * @param newModel
	 */
	public void registerMetaModel(EMFMetaModelResource newMetaModelResource) {
		String mmURI = newMetaModelResource.getURI();
		EMFMetaModelResource existingMM = metamodels.get(mmURI);
		if (existingMM == null)	{
			registerResource(newMetaModelResource);
			metamodels.put(mmURI, newMetaModelResource);
			EMFExtensionToFactoryMap.put(newMetaModelResource.getModelFileExtension(), newMetaModelResource.getEMFResourceFactory());
		}
		else {
			// TODO : xtof, manage duplicate URIs
			logger.warning(" There already exists a MM with that URI => I will not register this one!");
		}
	}

	public List<String> getAllMetaModelURIs(){
		return new ArrayList<String>(metamodels.keySet());
	}


	public EMFMetaModelResource getMetaModelResourceByURI(String uri){
		return metamodels.get(uri);
	}

	/**
	 * Called when a new model was registered, notify the {@link TechnologyContextManager}
	 * 
	 * @param newModel
	 */
	public void registerModel(EMFModelResource newModelResource) {
		registerResource(newModelResource);
		models.put(newModelResource.getURI(), newModelResource);
	}

}
