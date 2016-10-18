/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.fml.annotations.DeclareResourceTypes;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.technologyadapter.xml.fml.binding.XMLBindingFactory;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelFactory;
import org.openflexo.technologyadapter.xml.rm.XMLFileResourceFactory;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResourceFactory;

/**
 * 
 * @author sylvain, luka, Christophe
 * 
 */

@DeclareModelSlots({ FreeXMLModelSlot.class, XMLModelSlot.class, XMLMetaModelSlot.class })
@DeclareRepositoryType({ XMLModelRepository.class, XSDMetaModelRepository.class })
@DeclareResourceTypes({ XSDMetaModelResourceFactory.class, XMLFileResourceFactory.class })
public class XMLTechnologyAdapter extends TechnologyAdapter {

	protected static final Logger logger = Logger.getLogger(XMLTechnologyAdapter.class.getPackage().getName());

	private static final String TAName = "XML technology adapter";

	private XMLModelFactory xmlModelFactory = null;

	private static final XMLBindingFactory BINDING_FACTORY = new XMLBindingFactory();

	protected HashMap<String, XMLMetaModel> privateMetamodels = null;

	public XMLTechnologyAdapter() {
		super();
		xmlModelFactory = new XMLModelFactory();
		privateMetamodels = new HashMap<String, XMLMetaModel>();
	}

	@Override
	public String getName() {
		return TAName;
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/XMLTechnologyAdapter";
	}

	/**
	 * Return flag indicating if supplied file represents a valid XSD schema
	 * 
	 * @param aMetaModelFile
	 * @return
	 */
	/*public boolean isValidMetaModelFile(File aMetaModelFile) {
		// TODO: also check that file is valid and maps a valid XSD schema
	
		return aMetaModelFile.isFile() && aMetaModelFile.getName().endsWith(".xsd");
	}*/

	/*public boolean isValidModelFile(File aModelFile, FlexoResource<XMLMetaModel> metaModelResource,
			TechnologyContextManager technologyContextManager) {
	
		if (isValidModelFile(aModelFile)) {
	
			XMLRootElementInfo rootInfo = null;
			try {
	
				// FIXME : maybe the RootElement info should be stored with File Resource ?
				// to avoid looping through all XSD MM each time you have an xml file to test
	
				rootInfo = REreader.readRootElement(aModelFile);
	
				String schemaURI = rootInfo.getAttribute("targetNamespace");
	
				String mmURI = metaModelResource.getURI();
				if (schemaURI != null && mmURI != null) {
					if (schemaURI.equals(mmURI)) {
						logger.info("Found a conformant XML Model File [" + schemaURI + "]" + aModelFile.getAbsolutePath());
						return !schemaURI.isEmpty();
					}
				}
	
			} catch (IOException e) {
				logger.warning("Unable to parse Root Node for XML File, discarding it : " + e.getLocalizedMessage());
				return false;
			}
			return true;
		}
	
		return false;
	
	}*/

	/*public boolean isValidModelFile(File aModelFile) {
		if (aModelFile.exists() && aModelFile.getName().endsWith(XML_EXTENSION))
			return true;
		else
			return false;
	}*/

	public String retrieveModelURI(File aModelFile, FlexoResource<XMLModel> metaModelResource) {
		return aModelFile.toURI().toString();
	}

	@Override
	public XMLTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {

		return new XMLTechnologyContextManager(this, service);
	}

	@Override
	public XMLTechnologyContextManager getTechnologyContextManager() {
		return (XMLTechnologyContextManager) super.getTechnologyContextManager();
	}

	/*public String getExpectedModelExtension(FlexoResource<?> metaModel) {
		return XML_EXTENSION;
	}
	
	public String getExpectedMetaModelExtension() {
		return XSD_EXTENSION;
	}*/

	/*@Override
	public <I> void performInitializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
	
		XMLModelRepository mRepository = resourceCenter.getRepository(XMLModelRepository.class, this);
		if (mRepository == null) {
			mRepository = createXMLModelRepository(resourceCenter);
		}
	
		XSDMetaModelRepository mmRepository = resourceCenter.getRepository(XSDMetaModelRepository.class, this);
		if (mmRepository == null) {
			mmRepository = createXMLMetaModelRepository(resourceCenter);
		}
	
		// First pass on meta-models only
		Iterator<I> it = resourceCenter.iterator();
		XSDMetaModelResource mmRes = null;
		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				mmRes = tryToLookupMetaModel(resourceCenter, candidateFile);
			}
		}
	
		// Second pass on models
		it = resourceCenter.iterator();
		XMLResource mRes = null;
	
		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				mRes = tryToLookupModel(resourceCenter, candidateFile);
			}
		}
	
	}*/

	/*protected XSDMetaModelResource tryToLookupMetaModel(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
	
		XSDMetaModelRepository mmRepository = resourceCenter.getRepository(XSDMetaModelRepository.class, this);
		XMLTechnologyContextManager xmlContextManager = getTechnologyContextManager();
		XSDMetaModelResource mmRes = null;
	
		if (isValidMetaModelFile(candidateFile)) {
	
			XMLRootElementInfo rootInfo = null;
			String uri = null;
	
			try {
	
				rootInfo = REreader.readRootElement(candidateFile);
	
				uri = rootInfo.getAttribute("targetNamespace");
	
			} catch (IOException e) {
				logger.warning("Unable to parse file: " + candidateFile);
				e.printStackTrace();
				return null;
			}
	
			if (uri != null) {
	
				mmRes = (XSDMetaModelResource) xmlContextManager.getResourceWithURI(uri);
	
				if (mmRes == null) {
	
					mmRes = XSDMetaModelResourceImpl.makeXSDMetaModelResource(candidateFile, uri, xmlContextManager);
					mmRes.initName(candidateFile.getName());
					mmRes.setResourceCenter(resourceCenter);
					mmRes.setServiceManager(getTechnologyAdapterService().getServiceManager());
				}
				else {
					logger.warning("Found another file with an already existing URI: " + uri);
					return null;
				}
	
				// Register Resource
	
				if (mmRes != null) {
					RepositoryFolder<XSDMetaModelResource> folder;
					xmlContextManager.registerResource(mmRes);
					try {
	
						folder = mmRepository.getRepositoryFolder(candidateFile, true);
						if (folder != null) {
							mmRepository.registerResource(mmRes, folder);
						}
						else {
							mmRepository.registerResource(mmRes);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	
					return mmRes;
				}
			}
		}
		return null;
	}*/

	/*protected XMLResource tryToLookupModel(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		XMLTechnologyContextManager xmlContextManager = getTechnologyContextManager();
		XMLModelRepository modelRepository = resourceCenter.getRepository(XMLModelRepository.class, this);
		XSDMetaModelRepository mmRepository = null;
	
		XMLResource mRes = null;
	
		if (isValidModelFile(candidateFile)) {
	
			String mmURI = null;
			try {
				mmURI = XMLFileResourceImpl.getTargetNamespace(candidateFile);
			} catch (IOException e) {
				logger.warning("Unable to parse Root Node for XML File, discarding it (" + candidateFile.getAbsolutePath() + ") : "
						+ e.getLocalizedMessage());
				return null;
			}
	
			mRes = XMLFileResourceImpl.makeXMLFileResource(candidateFile, xmlContextManager, resourceCenter);
	
			if (mRes != null) {
	
				xmlContextManager.registerResource(mRes);
				RepositoryFolder<XMLFileResource> folder;
				try {
					folder = modelRepository.getRepositoryFolder(candidateFile, true);
					if (folder != null) {
						modelRepository.registerResource((XMLFileResource) mRes, folder);
					}
					else {
						modelRepository.registerResource(mRes);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	
				// then find the MetaModel
	
				if (mmURI != null && mmURI.length() > 0) {
	
					XSDMetaModelResource mmRsc = null;
	
					// Looping all resource-centers to find a MetaModel Resource corresponding to this File
	
					// *********************************************************************************
					// TODO
					// If you find something....
					// either it is an XSD or an already registered MM (initialized or not)
					// or it is a non existent (in Memory) MM
					//
					// But how to promote an AdHoc MM to actual MM ?
					// And serialize it as an XSD?
	
					List<FlexoResourceCenter> rscCenters = xmlContextManager.getResourceCenterService().getResourceCenters();
					for (FlexoResourceCenter<?> rscCenter : rscCenters) {
						mmRepository = rscCenter.getRepository(XSDMetaModelRepository.class, this);
						if (mmRepository != null) {
							mmRsc = mmRepository.getResource(mmURI);
							if (mmRsc != null) {
								mRes.setMetaModelResource(mmRsc);
							}
						}
					}
	
					// Found nothing in RC, create a new one in TA MM collection
					// TODO: should be able to save somewhere and share it between several files
					// TODO: should be possible to expose it through TA
	
					if (mmRsc == null) {
						XMLMetaModel mm = privateMetamodels.get(mmURI);
						if (mm == null) {
							mm = XMLMetaModelImpl.getModelFactory().newInstance(XMLMetaModel.class);
							mm.setURI(mmURI);
							mm.setReadOnly(false);
							privateMetamodels.put(mmURI, mm);
							logger.info("Added a MetaModel for Resource in TA private MetaModels: " + mm.getURI());
	
						}
						else {
							logger.info("Found a MetaModel for Resource in TA private MetaModels: " + mm.getURI());
						}
	
						mRes.getModel().setMetaModel(mm);
					}
	
				}
				else {
					// This not done here anymore, as it causes huge impacts when ResourceCenter are loaded.
					// This now should be done using explicit call to attachMetaModel()
					//
					// XMLMetaModel mm = XMLMetaModelImpl.getModelFactory().newInstance(XMLMetaModel.class);
					// mm.setURI(mRes.getURI() + "/Metamodel");
					// mm.setReadOnly(false);
					// mRes.getModel().setMetaModel(mm);
				}
			}
		}
	
		return null;
	}*/

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	/*@Override
	public <I> boolean contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			File candidateFile = (File) contents;
			if (tryToLookupMetaModel(resourceCenter, candidateFile) != null) {
				// This is a meta-model, this one has just been registered
				return true;
			}
			else {
	
				XMLModelRepository modelRepository = resourceCenter.getRepository(XMLModelRepository.class, this);
				// Check if it's not yet registered
				boolean found = false;
				for (XMLFileResource r : modelRepository.getAllResources()) {
					found = found || getFileFlexoIODelegate(r).getFile().equals(candidateFile);
				}
				if (!found) {
					return (tryToLookupModel(resourceCenter, candidateFile) != null);
				}
			}
		}
		return false;
	}
	
	@Override
	public <I> boolean contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public <I> boolean contentsModified(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public <I> boolean contentsRenamed(FlexoResourceCenter<I> resourceCenter, I contents, String oldName, String newName) {
		// TODO Auto-generated method stub
		return false;
	}*/

	/*private FileFlexoIODelegate getFileFlexoIODelegate(FlexoResource resource) {
		return (FileFlexoIODelegate) resource.getFlexoIODelegate();
	}*/

	public <I> XSDMetaModelRepository<I> getXSDMetaModelRepository(FlexoResourceCenter<I> resourceCenter) {
		XSDMetaModelRepository<I> returned = resourceCenter.retrieveRepository(XSDMetaModelRepository.class, this);
		if (returned == null) {
			returned = new XSDMetaModelRepository<I>(this, resourceCenter);
			resourceCenter.registerRepository(returned, XSDMetaModelRepository.class, this);
		}
		return returned;
	}

	public <I> XMLModelRepository<I> getXMLModelRepository(FlexoResourceCenter<I> resourceCenter) {
		XMLModelRepository<I> returned = resourceCenter.retrieveRepository(XMLModelRepository.class, this);
		if (returned == null) {
			returned = new XMLModelRepository<I>(this, resourceCenter);
			resourceCenter.registerRepository(returned, XMLModelRepository.class, this);
		}
		return returned;
	}

	/**
	 * 
	 * Create a XMLModel repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	/*public XSDMetaModelRepository createXMLMetaModelRepository(FlexoResourceCenter<?> resourceCenter) {
		XSDMetaModelRepository returned = new XSDMetaModelRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, XSDMetaModelRepository.class, this);
		return returned;
	}*/

	/**
	 * 
	 * Create a XMLModel repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	/*public XMLModelRepository createXMLModelRepository(FlexoResourceCenter<?> resourceCenter) {
		XMLModelRepository returned = new XMLModelRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, XMLModelRepository.class, this);
		return returned;
	}*/

	public XMLModelFactory getXMLModelFactory() {
		return xmlModelFactory;
	}

	@Override
	public XMLBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	@Override
	public String getIdentifier() {
		return "XML";
	}

	public XMLFileResourceFactory getXMLFileResourceFactory() {
		return getResourceFactory(XMLFileResourceFactory.class);
	}

	public XSDMetaModelResourceFactory getXSDMetaModelResourceFactory() {
		return getResourceFactory(XSDMetaModelResourceFactory.class);
	}

	@Override
	protected <I> void foundFolder(FlexoResourceCenter<I> resourceCenter, I folder) throws IOException {
		super.foundFolder(resourceCenter, folder);
		if (resourceCenter.isDirectory(folder)) {
			getXMLModelRepository(resourceCenter).getRepositoryFolder(folder, true);
			getXSDMetaModelRepository(resourceCenter).getRepositoryFolder(folder, true);
		}
	}

}
