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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.openflexo.foundation.ontology.technologyadapter.FlexoOntologyTechnologyContextManager;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;

public class EMFTechnologyContextManager extends FlexoOntologyTechnologyContextManager<EMFTechnologyAdapter> {

	protected static final Logger logger = Logger.getLogger(EMFTechnologyContextManager.class.getPackage().getName());

	/** Stores all known metamodels where key is the URI of metamodel */
	protected Map<String, EMFMetaModelResource> metamodels = new HashMap<String, EMFMetaModelResource>();
	/** Stores all known metamodels where key is the URI of profiles (UML) */
	protected Map<String, EMFMetaModelResource> profiles = new HashMap<String, EMFMetaModelResource>();
	/** Stores all known models where key is the URI of model */
	protected Map<String, EMFModelResource> models = new HashMap<String, EMFModelResource>();

	/** Stores a reference to EMF Registry instance in order to register every MM available */

	protected Resource.Factory.Registry EMFRscFactoryRegistry = Resource.Factory.Registry.INSTANCE;
	protected EPackage.Registry EMFPackageRegistry = EPackage.Registry.INSTANCE;
	protected Map<String, Object> EMFExtensionToFactoryMap;

	public EMFTechnologyContextManager(EMFTechnologyAdapter adapter, FlexoResourceCenterService resourceCenterService) {
		super(adapter, resourceCenterService);
		EMFExtensionToFactoryMap = EMFRscFactoryRegistry.getExtensionToFactoryMap();
		// This enables working with UML Models
		UMLResourcesUtil.initGlobalRegistries();
	}

	@Override
	public EMFTechnologyAdapter getTechnologyAdapter() {
		return super.getTechnologyAdapter();
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
		if (existingMM == null) {
			registerResource(newMetaModelResource);
			metamodels.put(mmURI, newMetaModelResource);
			EMFExtensionToFactoryMap.put(newMetaModelResource.getModelFileExtension(), newMetaModelResource.getEMFResourceFactory());
			EPackage ePackage = newMetaModelResource.getPackage();
			System.out.println("******* Register " + newMetaModelResource + " rc=" + newMetaModelResource.getResourceCenter());
			if (!EMFPackageRegistry.containsKey(mmURI) && ePackage != null) {
				EMFPackageRegistry.put(mmURI, ePackage);
			}
			else {
				logger.warning("EMF MEtamodel already exists in registry : " + newMetaModelResource.getURI());
			}
		}
		else {
			// TODO : xtof, manage duplicate URIs
			logger.warning(" There already exists a MM with that URI => I will not register this one!");
		}
	}

	/**
	 * Called when a new profile was registered, notify the {@link TechnologyContextManager}
	 * 
	 * @param newModel
	 */
	public void registerProfile(EMFMetaModelResource newMetaModelResource) {
		String mmURI = newMetaModelResource.getURI();
		EMFMetaModelResource existingMM = profiles.get(mmURI);
		if (existingMM == null) {
			registerResource(newMetaModelResource);
			profiles.put(mmURI, newMetaModelResource);
			EPackage ePackage = newMetaModelResource.getPackage();
			if (!EMFPackageRegistry.containsKey(mmURI) && ePackage != null) {
				EMFPackageRegistry.put(newMetaModelResource.getURI(), ePackage);
				EMFPackageRegistry.put(ePackage.getNsPrefix(), ePackage);
			}
			else {
				logger.warning("UML Profile already exists in registry : " + newMetaModelResource.getURI());
			}
		}
		else {
			// TODO : xtof, manage duplicate URIs
			logger.warning(" There already exists a MM with that URI => I will not register this one!");
		}
	}

	/** Accessors for Profile Collection */

	public Set<String> getAllProfileURIs() {
		return profiles.keySet();
	}

	public Collection<EMFMetaModelResource> getAllProfileResources() {
		return Collections.unmodifiableCollection(profiles.values());
	}

	public EMFMetaModelResource geProfileResourceByURI(String uri) {
		return profiles.get(uri);
	}

	/** Accessors for MetaModel Collection */

	public Set<String> getAllMetaModelURIs() {
		return metamodels.keySet();
	}

	public Collection<EMFMetaModelResource> getAllMetaModelResources() {
		return Collections.unmodifiableCollection(metamodels.values());
	}

	public EMFMetaModelResource getMetaModelResourceByURI(String uri) {
		return metamodels.get(uri);
	}

	// TODO: maybe it does not need/have to be a EMFMetaModelResource
	public EMFMetaModelResource getProfileResourceByURI(String uri) {
		return profiles.get(uri);
	}

	/**
	 * Called when a new model is registered, notify the {@link TechnologyContextManager}
	 * 
	 * @param newModel
	 */
	public void registerModel(EMFModelResource newModelResource) {
		registerResource(newModelResource);
		models.put(newModelResource.getURI(), newModelResource);
	}

}
