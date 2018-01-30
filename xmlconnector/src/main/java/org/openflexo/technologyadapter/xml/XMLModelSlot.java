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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.xml.XMLURIProcessor.XMLURIProcessorImpl;
import org.openflexo.technologyadapter.xml.fml.XMLActorReference;
import org.openflexo.technologyadapter.xml.fml.XMLIndividualRole;
import org.openflexo.technologyadapter.xml.fml.editionaction.AddXMLIndividual;
import org.openflexo.technologyadapter.xml.fml.editionaction.CreateXMLFileResource;
import org.openflexo.technologyadapter.xml.fml.editionaction.GetXMLDocumentRoot;
import org.openflexo.technologyadapter.xml.fml.editionaction.SetXMLDocumentRoot;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.rm.XMLFileResource;
import org.openflexo.technologyadapter.xml.rm.XMLFileResourceFactory;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;

/**
 * 
 * An XML ModelSlot used to edit an XML document conformant to a (XSD) MetaModel
 *
 * @author xtof
 * 
 */
@DeclareFlexoRoles({ XMLIndividualRole.class })
@DeclareActorReferences({ XMLActorReference.class })
@DeclareEditionActions({ CreateXMLFileResource.class, AddXMLIndividual.class, GetXMLDocumentRoot.class, SetXMLDocumentRoot.class })
@ModelEntity
@XMLElement
@ImplementationClass(XMLModelSlot.XMLModelSlotImpl.class)
@Imports({ @Import(XMLURIProcessor.class), })
@FML("XMLModelSlot")
public interface XMLModelSlot extends TypeAwareModelSlot<XMLModel, XMLMetaModel>, AbstractXMLModelSlot<XMLURIProcessor> {

	@PropertyIdentifier(type = XMLMetaModel.class)
	public static final String METAMODEL = "metamodel";

	@Getter(value = METAMODEL)
	public XMLMetaModel getMetamodel();

	// public static abstract class XMLModelSlotImpl extends AbstractXMLModelSlot.AbstractXMLModelSlotImpl<XMLURIProcessor> implements
	// XMLModelSlot {
	// TODO : check for multiple inheritance issues in PAMELA
	public static abstract class XMLModelSlotImpl extends TypeAwareModelSlotImpl<XMLModel, XMLMetaModel> implements XMLModelSlot {

		private static final Logger logger = Logger.getLogger(XMLModelSlot.class.getPackage().getName());

		/* Used to process URIs for XML Objects */
		private List<XMLURIProcessor> uriProcessors;
		private Hashtable<String, XMLURIProcessor> uriProcessorsMap;

		public XMLModelSlotImpl() {
			super();
			if (uriProcessorsMap == null) {
				uriProcessorsMap = new Hashtable<>();
			}
			if (uriProcessors == null) {
				uriProcessors = new ArrayList<>();
			}
		}

		@Override
		public Class<? extends TechnologyAdapter> getTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}

		@Override
		public XMLTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (XMLTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public XMLURIProcessor createURIProcessor() {
			XMLURIProcessor xsuriProc = getFMLModelFactory().newInstance(XMLURIProcessor.class);
			xsuriProc.setModelSlot(this);
			uriProcessors.add(xsuriProc);
			return xsuriProc;
		}

		/*=====================================================================================
		 * URI Accessors
		 */
		// TODO Manage the fact that URI May Change

		@Override
		public String getURIForObject(XMLModel model, Object o) {

			if (o instanceof XMLIndividual) {
				XMLURIProcessor p = retrieveURIProcessorForType(((XMLIndividual) o).getType());
				if (p != null) {
					return p.getURIForObject(model, (XMLObject) o);
				}
				else {
					logger.warning("Unable to calculate URI as I have no XMLURIProcessor");
				}
			}
			else if (o instanceof XMLType) {
				return ((XMLType) o).getURI();
			}

			return null;
		}

		@Override
		public Object retrieveObjectWithURI(XMLModel model, String objectURI) {

			String typeUri = XMLURIProcessorImpl.retrieveTypeURI(model, objectURI);
			XMLURIProcessor mapParams = uriProcessorsMap.get(XMLURIProcessorImpl.retrieveTypeURI(model, objectURI));
			if (mapParams == null) {
				// Look for a processor in superClasses
				XMLType aType = model.getMetaModel().getTypeFromURI(typeUri);
				mapParams = retrieveURIProcessorForType(aType);
			}

			if (mapParams != null) {
				try {
					return mapParams.retrieveObjectWithURI(model, objectURI);
				} catch (DuplicateURIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		public XMLURIProcessor retrieveURIProcessorForType(XMLType aXmlType) {

			logger.info("SEARCHING for an uriProcessor for " + aXmlType.getURI());

			XMLURIProcessor mapParams = uriProcessorsMap.get(aXmlType.getURI());

			if (mapParams == null) {
				XMLType s = aXmlType.getSuperType();
				if (mapParams == null) {
					logger.info("SEARCHING for an uriProcessor for " + s.getURI());
					mapParams = retrieveURIProcessorForType(s);
				}

				if (mapParams != null) {
					logger.info("UPDATING the MapUriProcessors for an uriProcessor for " + aXmlType.getURI());
					uriProcessorsMap.put(aXmlType.getURI(), mapParams);
				}
			}
			return mapParams;
		}

		// ==========================================================================
		// ============================== uriProcessors Map ===================
		// ==========================================================================

		public void setUriProcessors(List<XMLURIProcessor> uriProcessingParameters) {
			this.uriProcessors = uriProcessingParameters;
		}

		public void updateURIMapForProcessor(XMLURIProcessor xmluriProc) {
			String uri = xmluriProc.getTypeURI();
			if (uri != null) {
				for (String k : uriProcessorsMap.keySet()) {
					XMLURIProcessor p = uriProcessorsMap.get(k);
					if (p.equals(xmluriProc)) {
						uriProcessorsMap.remove(k);
					}
				}
				uriProcessorsMap.put(uri, xmluriProc);
			}
		}

		public void addToUriProcessors(XMLURIProcessor xmluriProc) {
			xmluriProc.setModelSlot(this);
			uriProcessors.add(xmluriProc);
			uriProcessorsMap.put(xmluriProc.getTypeURI().toString(), xmluriProc);
		}

		public void removeFromUriProcessors(XMLURIProcessor xmluriProc) {
			String uri = xmluriProc.getTypeURI();
			if (uri != null) {
				for (String k : uriProcessorsMap.keySet()) {
					XMLURIProcessor p = uriProcessorsMap.get(k);
					if (p.equals(xmluriProc)) {
						uriProcessorsMap.remove(k);
					}
				}
				uriProcessors.remove(xmluriProc);
				xmluriProc.reset();
			}
		}

		// Do not use this since not efficient, used in deserialization only
		@Override
		public List<XMLURIProcessor> getUriProcessorsList() {
			return uriProcessors;
		}

		@Override
		public void setUriProcessorsList(List<XMLURIProcessor> uriProcList) {
			for (XMLURIProcessor xmluriProc : uriProcList) {
				addToUriProcessorsList(xmluriProc);
			}
		}

		@Override
		public void addToUriProcessorsList(XMLURIProcessor xmluriProc) {
			addToUriProcessors(xmluriProc);
		}

		@Override
		public void removeFromUriProcessorsList(XMLURIProcessor xmluriProc) {
			removeFromUriProcessors(xmluriProc);
		}

		@Override
		@Getter(value = METAMODEL)
		public XMLMetaModel getMetamodel() {
			FlexoMetaModelResource<XMLModel, XMLMetaModel, ?> mmRes = this.getMetaModelResource();
			if (mmRes != null) {
				return mmRes.getMetaModelData();
			}
			else
				return null;
		}

		@Override
		public XMLFileResource createProjectSpecificEmptyModel(FlexoResourceCenter<?> rc, String filename, String relativePath,
				String modelUri, FlexoMetaModelResource<XMLModel, XMLMetaModel, ?> metaModelResource) {

			XMLTechnologyAdapter xmlTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);
			XMLFileResourceFactory factory = getModelSlotTechnologyAdapter().getXMLFileResourceFactory();

			Object serializationArtefact = xmlTA.retrieveResourceSerializationArtefact(rc, filename, relativePath,
					XMLFileResourceFactory.XML_EXTENSION);

			XMLFileResource newXMLFileResource;
			try {
				newXMLFileResource = factory.makeResource(serializationArtefact, (FlexoResourceCenter) rc, filename, modelUri, true);
				newXMLFileResource.setMetaModelResource((FlexoMetaModelResource) metaModelResource);
				return newXMLFileResource;
			} catch (SaveResourceException e) {
				e.printStackTrace();
			} catch (ModelDefinitionException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public XMLFileResource createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri, FlexoMetaModelResource<XMLModel, XMLMetaModel, ?> metaModelResource) {

			// Unused XMLFileResource returned = null;

			if (resourceCenter instanceof FileSystemBasedResourceCenter) {
				File xmlFile = new File(((FileSystemBasedResourceCenter) resourceCenter).getRootDirectory(),
						relativePath + System.getProperty("file.separator") + filename);

				modelUri = xmlFile.toURI().toString();

				XMLModelRepository<?> modelRepository = getModelSlotTechnologyAdapter().getXMLModelRepository(resourceCenter);

				try {
					return createEmptyXMLFileResource(xmlFile, modelRepository, (XSDMetaModelResource) metaModelResource);
				} catch (SaveResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ModelDefinitionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;

		}

		private XMLFileResource createEmptyXMLFileResource(File xmlFile, XMLModelRepository modelRepository,
				XSDMetaModelResource metaModelResource) throws SaveResourceException, ModelDefinitionException {

			XMLTechnologyAdapter ta = getModelSlotTechnologyAdapter();
			XMLFileResourceFactory xmlFileResourceFactory = ta.getXMLFileResourceFactory();

			XMLFileResource returned = xmlFileResourceFactory.makeResource(xmlFile, modelRepository.getResourceCenter(), true);

			// XMLFileResource returned = XMLFileResourceImpl.makeXMLFileResource(xmlFile,
			// (XMLTechnologyContextManager) this.getModelSlotTechnologyAdapter().getTechnologyContextManager(),
			// modelRepository.getResourceCenter());

			RepositoryFolder<XMLFileResource, ?> folder;
			try {
				folder = modelRepository.getParentRepositoryFolder(xmlFile, true);
				if (folder != null) {
					modelRepository.registerResource(returned, folder);
				}
				else {
					modelRepository.registerResource(returned);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			if (metaModelResource != null && returned != null) {
				returned.setMetaModelResource(metaModelResource);
				returned.getModel().setMetaModel(metaModelResource.getMetaModelData());
			}

			return returned;

		}

		@Override
		public Type getType() {
			return XMLModelSlot.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> flexoRoleClass) {
			return flexoRoleClass.getSimpleName();
		}

		@Override
		public String getTypeDescription() {
			return "xml";
		}
	}

}
