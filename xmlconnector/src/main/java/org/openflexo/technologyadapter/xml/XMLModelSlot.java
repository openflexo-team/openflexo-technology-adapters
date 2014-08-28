/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.editionaction.AddXMLIndividual;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
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
@ImplementationClass(XMLModelSlot.XMLModelSlotImpl.class)
@Imports({@Import(XMLURIProcessor.class),})
public interface XMLModelSlot extends TypeAwareModelSlot<XMLModel, XMLMetaModel>, AbstractXMLModelSlot<XMLURIProcessor>  {

	@PropertyIdentifier(type = XMLMetaModel.class)
	public static final String METAMODEL = "metamodel";


	@Getter(value = METAMODEL)
	public XMLMetaModel getMetamodel();


	// public static abstract class XMLModelSlotImpl extends AbstractXMLModelSlot.AbstractXMLModelSlotImpl<XMLURIProcessor> implements XMLModelSlot {
	// TODO : check for multiple inheritance issues
	public static abstract class XMLModelSlotImpl extends TypeAwareModelSlotImpl<XMLModel, XMLMetaModel> implements XMLModelSlot {


		private static final Logger logger = Logger.getLogger(XMLModelSlot.class.getPackage().getName());


		/* Used to process URIs for XML Objects */
		private List<XMLURIProcessor> uriProcessors;
		private Hashtable<String,  XMLURIProcessor> uriProcessorsMap;

		public XMLModelSlotImpl(){
			super();
			if (uriProcessorsMap == null) {
				uriProcessorsMap = new Hashtable<String, XMLURIProcessor>();
			}
			if (uriProcessors == null) {
				uriProcessors = new ArrayList<XMLURIProcessor>();
			}
		}

		
		@Override
		public Class<? extends TechnologyAdapter> getTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}
		
		@Override
		public XMLURIProcessor createURIProcessor() {
			XMLURIProcessor xsuriProc = getVirtualModelFactory().newInstance(XMLURIProcessor.class);
			xsuriProc.setModelSlot(this);
			uriProcessors.add(xsuriProc);
			return xsuriProc;
		}


		/*=====================================================================================
		 * URI Accessors
		 */
		// TODO Manage the fact that URI May Change

		@Override
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


		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public XMLModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new XMLModelSlotInstanceConfiguration(this, action);
		}

		@Override
		@Getter(value = METAMODEL)
		public XMLMetaModel getMetamodel(){
			FlexoMetaModelResource<XMLModel, XMLMetaModel, ?> mmRes = this.getMetaModelResource();
			if (mmRes != null ){
				return mmRes.getMetaModelData();
			}
			else return null;
		}

	}

}
