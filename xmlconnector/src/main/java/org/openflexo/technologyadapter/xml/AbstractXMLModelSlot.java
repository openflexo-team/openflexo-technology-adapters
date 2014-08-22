/*
 *
 * (c) Copyright 2014- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.xml;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.FlexoModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLURIProcessor.XSURIProcessorImpl;
import org.openflexo.technologyadapter.xml.editionaction.AddXMLIndividual;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.virtualmodel.XMLIndividualRole;

/**
 * 
 *   An XML ModelSlot used to edit an XML document conformant to a (XSD) MetaModel
 *
 * @author xtof
 * 
 */
@DeclarePatternRoles({ @DeclarePatternRole(flexoRoleClass = XMLIndividualRole.class, FML = "XMLIndividual"), // Instances
})
@DeclareEditionActions({ @DeclareEditionAction(editionActionClass = AddXMLIndividual.class, FML = "AddXMLIndividual"), // Add
	// instance
})
@ModelEntity
@XMLElement
@ImplementationClass(AbstractXMLModelSlot.AbstractXMLModelSlotImpl.class)
public interface AbstractXMLModelSlot extends TypeAwareModelSlot<XMLModel, XMLMetaModel> {

	@PropertyIdentifier(type = List.class)
	public static final String URI_PROCESSORS_LIST_KEY = "uriProcessorsList";

	@Getter(value = URI_PROCESSORS_LIST_KEY, cardinality = Cardinality.LIST)
	@XMLElement
	public List<XMLURIProcessor> getUriProcessorsList();

	@Setter(URI_PROCESSORS_LIST_KEY)
	public void setUriProcessorsList(List<XMLURIProcessor> uriProcessorsList);

	@Adder(URI_PROCESSORS_LIST_KEY)
	public void addToUriProcessorsList(XMLURIProcessor aUriProcessorsList);

	@Remover(URI_PROCESSORS_LIST_KEY)
	public void removeFromUriProcessorsList(XMLURIProcessor aUriProcessorsList);


	public XMLURIProcessor createURIProcessor();


	public static abstract class AbstractXMLModelSlotImpl extends TypeAwareModelSlotImpl<XMLModel, XMLMetaModel> implements AbstractXMLModelSlot {

		private static final Logger logger = Logger.getLogger(AbstractXMLModelSlot.class.getPackage().getName());

		/* Used to process URIs for XML Objects */
		private List<XMLURIProcessor> uriProcessors;
		private Hashtable<String, XMLURIProcessor> uriProcessorsMap;


		public AbstractXMLModelSlotImpl(){
			super();
			if (uriProcessorsMap == null) {
				uriProcessorsMap = new Hashtable<String, XMLURIProcessor>();
			}
			if (uriProcessors == null) {
				uriProcessors = new ArrayList<XMLURIProcessor>();
			}
		}

		@Override
		public Type getType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class<? extends TechnologyAdapter> getTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}

		@Override
		public FlexoModelResource<XMLModel, XMLMetaModel, ?> createProjectSpecificEmptyModel(FlexoProject project, String filename,
				String modelUri, FlexoMetaModelResource<XMLModel, XMLMetaModel, ?> metaModelResource) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FlexoModelResource<XMLModel, XMLMetaModel, ?> createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath,
				String filename, String modelUri, FlexoMetaModelResource<XMLModel, XMLMetaModel, ?> metaModelResource) {
			// TODO Auto-generated method stub
			return null;
		}

		/*=====================================================================================
		 * URI Accessors
		 */
		// TODO Manage the fact that URI May Change

		@Override
		public String getURIForObject(
				TypeAwareModelSlotInstance<XMLModel, XMLMetaModel, ? extends TypeAwareModelSlot<XMLModel, XMLMetaModel>> msInstance,
						Object o) {
			XMLIndividual xsO = (XMLIndividual) o;

			XMLType lClass = (xsO.getType());
			XMLURIProcessor mapParams = retrieveURIProcessorForType(lClass);

			if (mapParams != null) {
				return mapParams.getURIForObject(msInstance, xsO);
			} else {
				logger.warning("XSDModelSlot: unable to get the URIProcessor for element of type: "
						+ ((XMLType) xsO.getType()).getName());
				return null;
			}

		}

		public XMLURIProcessor retrieveURIProcessorForType(XMLType aXmlType) {

			logger.info("SEARCHING for an uriProcessor for " + aXmlType.getURI());

			XMLURIProcessor mapParams = uriProcessorsMap.get(aXmlType.getURI());

			if (mapParams == null) {
				XMLType s = aXmlType.getSuperType();
				if (mapParams == null) {
					// on ne cherche que le premier...
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

		@Override
		public Object retrieveObjectWithURI(
				TypeAwareModelSlotInstance<XMLModel, XMLMetaModel, ? extends TypeAwareModelSlot<XMLModel, XMLMetaModel>> msInstance,
						String objectURI) {
			String typeUri = XSURIProcessorImpl.retrieveTypeURI(msInstance, objectURI);
			XMLModel model = msInstance.getModel();
			XMLURIProcessor mapParams = uriProcessorsMap.get(XSURIProcessorImpl.retrieveTypeURI(msInstance, objectURI));
			if (mapParams == null) {
				// Look for a processor in superClasses
				XMLType aClass = model.getMetaModel().getTypeFromURI(typeUri);
				mapParams = retrieveURIProcessorForType(aClass);
			}

			if (mapParams != null) {
				try {
					return mapParams.retrieveObjectWithURI(msInstance, objectURI);
				} catch (DuplicateURIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		// ==========================================================================
		// ============================== uriProcessors Map ===================
		// ==========================================================================

		public void setUriProcessors(List<XMLURIProcessor> uriProcessingParameters) {
			this.uriProcessors = uriProcessingParameters;
		}

		public List<XMLURIProcessor> getUriProcessors() {
			return uriProcessors;
		}

		@Override
		public XMLURIProcessor createURIProcessor() {
			XMLURIProcessor xsuriProc = getVirtualModelFactory().newInstance(XMLURIProcessor.class);
			xsuriProc.setModelSlot(this);
			uriProcessors.add(xsuriProc);
			return xsuriProc;
		}

		public void updateURIMapForProcessor(XMLURIProcessor xsuriProc) {
			String uri = xsuriProc._getTypeURI();
			if (uri != null) {
				for (String k : uriProcessorsMap.keySet()) {
					XMLURIProcessor p = uriProcessorsMap.get(k);
					if (p.equals(xsuriProc)) {
						uriProcessorsMap.remove(k);
					}
				}
				uriProcessorsMap.put(uri, xsuriProc);
			}
		}

		public void addToUriProcessors(XMLURIProcessor xsuriProc) {
			xsuriProc.setModelSlot(this);
			uriProcessors.add(xsuriProc);
			uriProcessorsMap.put(xsuriProc._getTypeURI().toString(), xsuriProc);
		}

		public void removeFromUriProcessors(XMLURIProcessor xsuriProc) {
			String uri = xsuriProc._getTypeURI();
			if (uri != null) {
				for (String k : uriProcessorsMap.keySet()) {
					XMLURIProcessor p = uriProcessorsMap.get(k);
					if (p.equals(xsuriProc)) {
						uriProcessorsMap.remove(k);
					}
				}
				uriProcessors.remove(xsuriProc);
				xsuriProc.reset();
			}
		}

		// Do not use this since not efficient, used in deserialization only
		@Override
		public List<XMLURIProcessor> getUriProcessorsList() {
			return uriProcessors;
		}

		@Override
		public void setUriProcessorsList(List<XMLURIProcessor> uriProcList) {
			for (XMLURIProcessor xsuriProc : uriProcList) {
				addToUriProcessorsList(xsuriProc);
			}
		}

		@Override
		public void addToUriProcessorsList(XMLURIProcessor xsuriProc) {
			addToUriProcessors(xsuriProc);
		}

		@Override
		public void removeFromUriProcessorsList(XMLURIProcessor xsuriProc) {
			removeFromUriProcessors(xsuriProc);
		}


		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public XMLModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new XMLModelSlotInstanceConfiguration(this, action);
		}


		@Override
		public boolean isStrictMetaModelling() {
			// TODO Auto-generated method stub
			return false;
		}

	}

}