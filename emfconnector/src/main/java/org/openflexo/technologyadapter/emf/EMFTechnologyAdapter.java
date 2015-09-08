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
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.emf.fml.binding.EMFBindingFactory;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.metamodel.io.MMFromClasspathIODelegate;
import org.openflexo.technologyadapter.emf.metamodel.io.MMFromClasspathIODelegate.MMFromClasspathIODelegateImpl;
import org.openflexo.technologyadapter.emf.metamodel.io.MMFromJarsInDirIODelegate;
import org.openflexo.technologyadapter.emf.metamodel.io.MMFromJarsInDirIODelegate.MMFromJarsInDirIODelegateImpl;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResourceImpl;
import org.openflexo.technologyadapter.emf.rm.XtextEMFMetaModelResource;

/**
 * This class defines and implements the EMF technology adapter
 * 
 * @author sylvain
 * 
 */
@DeclareModelSlots({ EMFModelSlot.class, /*,EMFMetaModelSlot.class*/
		UMLEMFModelSlot.class /* EMFUMLModelSlot.class */ })
@DeclareRepositoryType({ EMFMetaModelRepository.class, EMFModelRepository.class })
public class EMFTechnologyAdapter extends TechnologyAdapter {

	protected static final Logger logger = Logger.getLogger(EMFTechnologyAdapter.class.getPackage().getName());

	private static final EMFBindingFactory BINDING_FACTORY = new EMFBindingFactory();

	// Static references to ECORE properties

	public static String ECORE_MM_NAME = "Ecore Metamodel";
	public static String ECORE_MM_URI = "http://www.eclipse.org/emf/2002/Ecore";
	private static String ECORE_MM_EXT = "ecore";
	private static String ECORE_MM_PKGCLSNAME = EcorePackageImpl.class.getName();
	private static String ECORE_MM_FACTORYCLSNAME = EcoreResourceFactoryImpl.class.getName();
	private EMFMetaModelResource ecoreMetaModelResource = null;

	// Static references to UML properties

	public static String UML_MM_NAME = "UML Metamodel";
	public static String UML_MM_URI = "http://www.eclipse.org/uml2/5.0.0/UML";
	private static String UML_MM_EXT = "uml";
	private static String UML_MM_PKGCLSNAME = UMLPackage.class.getName();
	private static String UML_MM_FACTORYCLSNAME = UMLResourceFactoryImpl.class.getName();
	private EMFMetaModelResource umlMetaModelResource = null;

	/**
	 * 
	 * Constructor.
	 * 
	 * @throws TechnologyAdapterInitializationException
	 */
	public EMFTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.technologyadapter.TechnologyAdapter#getName()
	 */
	@Override
	public String getName() {
		return "EMF technology adapter";
	}

	/**
	 * Initialize the supplied resource center with the technology<br>
	 * ResourceCenter is scanned, ResourceRepositories are created and new technology-specific resources are build and registered.
	 * 
	 * @param resourceCenter
	 */
	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {

		EMFTechnologyContextManager technologyContextManager = (EMFTechnologyContextManager) getTechnologyAdapterService()
				.getTechnologyContextManager(this);

		// A single MM Repository for all ResourceCenters

		EMFMetaModelRepository mmRepository = resourceCenter.getRepository(EMFMetaModelRepository.class, this);
		if (mmRepository == null) {
			try {
				mmRepository = createMetaModelRepository(resourceCenter);
			} catch (ModelDefinitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		EMFModelRepository modelRepository = resourceCenter.getRepository(EMFModelRepository.class, this);
		if (modelRepository == null) {
			modelRepository = createModelRepository(resourceCenter);
		}

		// First pass on meta-models only
		Iterator<I> it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				EMFMetaModelResource mmRes = tryToLookupMetaModel(resourceCenter, candidateFile);
			}
		}

		// Second pass on models
		it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				EMFModelResource mRes = tryToLookupModel(resourceCenter, candidateFile);
			}
		}

		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	protected EMFMetaModelResource tryToLookupMetaModel(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		EMFTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidMetaModelFile(candidateFile)) {
			EMFMetaModelResource mmRes = retrieveMetaModelResource(candidateFile);
			EMFMetaModelRepository mmRepo = resourceCenter.getRepository(EMFMetaModelRepository.class, this);
			if (mmRes != null) {
				RepositoryFolder<EMFMetaModelResource> folder;
				try {
					folder = mmRepo.getRepositoryFolder(candidateFile, true);
					if (folder != null) {
						mmRepo.registerResource(mmRes, folder);
					}
					else
						mmRepo.registerResource(mmRes);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(mmRes, resourceCenter);
				return mmRes;
			}
		}
		return null;
	}

	protected EMFModelResource tryToLookupModel(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		EMFTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		EMFMetaModelRepository mmRepository = resourceCenter.getRepository(EMFMetaModelRepository.class, this);
		EMFModelRepository modelRepository = resourceCenter.getRepository(EMFModelRepository.class, this);

		for (EMFMetaModelResource mmRes : technologyContextManager.getAllMetaModelResources()) {
			if (isValidModelFile(candidateFile, mmRes)) {
				EMFModelResource mRes = retrieveModelResource(candidateFile, mmRes);
				if (mRes != null) {
					RepositoryFolder<EMFModelResource> folder;
					try {
						folder = modelRepository.getRepositoryFolder(candidateFile, true);
						modelRepository.registerResource(mRes, folder);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					referenceResource(mRes, resourceCenter);
					return mRes;
				}
			}
		}
		return null;
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	@Override
	public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			File candidateFile = (File) contents;
			if (tryToLookupMetaModel(resourceCenter, candidateFile) != null) {
				// This is a meta-model, this one has just been registered
			}
			else {
				tryToLookupModel(resourceCenter, candidateFile);
			}
		}
	}

	@Override
	public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			System.out
					.println("File DELETED " + ((File) contents).getName() + " in " + ((File) contents).getParentFile().getAbsolutePath());
		}
	}

	/**
	 * 
	 * Create a metamodel repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 * @throws ModelDefinitionException
	 * 
	 * @see org.openflexo.foundation.technologyadapter.TechnologyAdapter#createMetaModelRepository(org.openflexo.foundation.resource.FlexoResourceCenter)
	 */
	public EMFMetaModelRepository createMetaModelRepository(FlexoResourceCenter<?> resourceCenter) throws ModelDefinitionException {

		EMFMetaModelRepository mmRepository = new EMFMetaModelRepository(this, resourceCenter);

		// Register Global MetaModels if ever we had a ResourceCenter configured

		registerClasspathMetaModels();

		resourceCenter.registerRepository(mmRepository, EMFMetaModelRepository.class, this);

		return mmRepository;
	}

	/**
	 * Registers the Metamodel that are provided by this technology adapter
	 */
	private void registerClasspathMetaModels() throws ModelDefinitionException {

		ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,
				MMFromClasspathIODelegate.class, EMFMetaModelResource.class, XtextEMFMetaModelResource.class));

		if (ecoreMetaModelResource == null) {
			// register ecore MM once for all resource centers
			MMFromClasspathIODelegate iodelegate = MMFromClasspathIODelegateImpl.makeMMFromClasspathIODelegate(factory);
			ecoreMetaModelResource = factory.newInstance(EMFMetaModelResource.class);
			ecoreMetaModelResource.setFlexoIODelegate(iodelegate);
			iodelegate.setFlexoResource(ecoreMetaModelResource);
			ecoreMetaModelResource.setTechnologyAdapter(this);
			ecoreMetaModelResource.setURI(ECORE_MM_URI);
			ecoreMetaModelResource.initName(ECORE_MM_NAME);
			ecoreMetaModelResource.setModelFileExtension(ECORE_MM_EXT);
			ecoreMetaModelResource.setPackageClassName(ECORE_MM_PKGCLSNAME);
			ecoreMetaModelResource.setResourceFactoryClassName(ECORE_MM_FACTORYCLSNAME);
			ecoreMetaModelResource.setServiceManager(getTechnologyAdapterService().getServiceManager());
			getTechnologyContextManager().registerMetaModel(ecoreMetaModelResource);
		}

		if (umlMetaModelResource == null) {
			// register ecore MM once for all resource centers
			MMFromClasspathIODelegate iodelegate = MMFromClasspathIODelegateImpl.makeMMFromClasspathIODelegate(factory);
			umlMetaModelResource = factory.newInstance(EMFMetaModelResource.class);
			umlMetaModelResource.setFlexoIODelegate(iodelegate);
			iodelegate.setFlexoResource(umlMetaModelResource);
			umlMetaModelResource.setTechnologyAdapter(this);
			umlMetaModelResource.setURI(UML_MM_URI);
			umlMetaModelResource.initName(UML_MM_NAME);
			umlMetaModelResource.setModelFileExtension(UML_MM_EXT);
			umlMetaModelResource.setPackageClassName(UML_MM_PKGCLSNAME);
			umlMetaModelResource.setResourceFactoryClassName(UML_MM_FACTORYCLSNAME);
			umlMetaModelResource.setServiceManager(getTechnologyAdapterService().getServiceManager());
			getTechnologyContextManager().registerMetaModel(umlMetaModelResource);
		}

	}

	/**
	 * 
	 * Create a model repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 * @see org.openflexo.foundation.technologyadapter.TechnologyAdapter#createModelRepository(org.openflexo.foundation.resource.FlexoResourceCenter)
	 */
	public EMFModelRepository createModelRepository(FlexoResourceCenter<?> resourceCenter) {
		EMFModelRepository returned = new EMFModelRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, EMFModelRepository.class, this);
		return returned;
	}

	/**
	 * Return flag indicating if supplied file represents a valid metamodel
	 * 
	 * @param aMetaModelFile
	 * @param technologyContextManager
	 * 
	 * @return
	 */
	public boolean isValidMetaModelFile(File aMetaModelFile) {
		return MMFromJarsInDirIODelegateImpl.isValidMetaModelFile(aMetaModelFile);
	}

	/**
	 * 
	 * Instantiate new meta model resource stored in supplied meta model file
	 * 
	 * @see org.openflexo.foundation.technologyadapter.TechnologyAdapter#retrieveMetaModelResource(java.io.File,
	 *      org.openflexo.foundation.resource.FlexoResourceCenter)
	 */
	public EMFMetaModelResource retrieveMetaModelResource(final File aMetaModelFile) {

		EMFMetaModelResource metaModelResource = null;

		ModelFactory factory;
		try {

			factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(MMFromJarsInDirIODelegate.class,
					EMFMetaModelResource.class, XtextEMFMetaModelResource.class));

			MMFromJarsInDirIODelegate iodelegate = MMFromJarsInDirIODelegateImpl.makeMMFromJarsInDirIODelegate(aMetaModelFile, factory);

			metaModelResource = iodelegate.retrieveMetaModelResource(aMetaModelFile, factory, this.getTechnologyContextManager());

			metaModelResource.setTechnologyAdapter(this);
			metaModelResource.setFlexoIODelegate(iodelegate);
			metaModelResource.setServiceManager(getTechnologyAdapterService().getServiceManager());

			return metaModelResource;

		} catch (ModelDefinitionException e) {
			logger.warning("Unable to Create EMF Metamodel from directory: " + aMetaModelFile);
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Return flag indicating if supplied file represents a valid model conform to supplied meta-model
	 * 
	 * @param aModelFile
	 * @param metaModelResource
	 * @param technologyContextManager
	 * @return
	 */
	public boolean isValidModelFile(File aModelFile, EMFMetaModelResource metaModelResource) {
		boolean valid = false;
		if (aModelFile.exists()) {
			// TODO syntaxic check and conformity to XMI
			if (aModelFile.getName().endsWith("." + metaModelResource.getModelFileExtension())) {
				if (aModelFile.isFile()) {
					valid = true;
				}
			}
		}
		return valid;
	}

	/**
	 * Retrieve and return URI for supplied model file
	 * 
	 * @param aModelFile
	 * @param technologyContextManager
	 * @return
	 */
	public String retrieveModelURI(File aModelFile, EMFMetaModelResource metaModelResource) {
		return retrieveModelResource(aModelFile, metaModelResource).getURI();
	}

	/**
	 * Instantiate new model resource stored in supplied model file, given the conformant metamodel<br>
	 * We assert here that model resource is conform to supplied metamodel, ie we will not try to lookup the metamodel but take the one
	 * which was supplied
	 * 
	 */
	public EMFModelResource retrieveModelResource(File aModelFile, FlexoResource<EMFMetaModel> metaModelResource) {
		EMFModelResource emfModelResource = null;

		emfModelResource = getTechnologyContextManager().getModel(aModelFile);

		if (emfModelResource == null) {
			// TODO refactor with ioDelegates
			emfModelResource = EMFModelResourceImpl.retrieveEMFModelResource(aModelFile, (EMFMetaModelResource) metaModelResource,
					getTechnologyContextManager());
		}

		return emfModelResource;
	}

	/**
	 * Create empty EMFModel.
	 * 
	 * @param project
	 * @param filename
	 * @param modelUri
	 * @param metaModelResource
	 * @param technologyContextManager
	 * @return
	 */
	public EMFModelResource createNewEMFModel(FlexoProject project, String filename, String modelUri,
			EMFMetaModelResource metaModelResource) {
		File modelFile = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);
		return createNewEMFModel(modelFile, modelUri, metaModelResource);
	}

	/**
	 * Create empty EMFModel.
	 * 
	 * @param modelFilePath
	 * @param metaModelResource
	 * @param technologyContextManager
	 * @return
	 */
	public EMFModelResource createNewEMFModel(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			String modelUri, EMFMetaModelResource metaModelResource) {
		File modelDirectory = new File(resourceCenter.getRootDirectory(), relativePath);
		File modelFile = new File(modelDirectory, filename);
		return createNewEMFModel(modelFile, modelUri, metaModelResource);
	}

	/**
	 * Create empty EMFModel.
	 * 
	 * @param modelFile
	 * @param modelUri
	 * @param metaModelResource
	 * @param technologyContextManager
	 * @return
	 */
	public EMFModelResource createNewEMFModel(File modelFile, String modelUri, EMFMetaModelResource metaModelResource) {
		EMFMetaModelResource emfMetaModelResource = metaModelResource;
		EMFModelResource emfModelResource = EMFModelResourceImpl.makeEMFModelResource(modelUri, modelFile, emfMetaModelResource,
				getTechnologyContextManager());
		getTechnologyContextManager().registerModel(emfModelResource);
		try {
			emfModelResource.save(null);
		} catch (SaveResourceException e) {
			e.printStackTrace();
		}
		System.out.println(
				"Created empty model " + modelFile.getAbsolutePath() + " as " + modelUri + " conform to " + metaModelResource.getURI());
		return emfModelResource;
	}

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.technologyadapter.TechnologyAdapter#createNewModelSlot(org.openflexo.foundation.fml.ViewPoint)
	 */
	@Override
	public EMFTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new EMFTechnologyContextManager(this, service);
	}

	@Override
	public EMFTechnologyContextManager getTechnologyContextManager() {
		return (EMFTechnologyContextManager) super.getTechnologyContextManager();
	}

	@Override
	public EMFBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	public String getExpectedModelExtension(EMFMetaModelResource metaModelResource) {
		if (metaModelResource != null) {
			return "." + metaModelResource.getModelFileExtension();
		}
		return null;
	}

	@Override
	public String getIdentifier() {
		return "EMF";
	}

}
