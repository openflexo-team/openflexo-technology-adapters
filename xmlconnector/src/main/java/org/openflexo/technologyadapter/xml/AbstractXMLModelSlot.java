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

import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.editionaction.AddXMLIndividual;
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
@ImplementationClass(AbstractXMLModelSlot.AbstractXMLModelSlotImpl.class)
public interface AbstractXMLModelSlot< T extends AbstractXMLURIProcessor> extends ModelSlot<XMLModel> {

	@PropertyIdentifier(type = List.class)
	public static final String URI_PROCESSORS_LIST_KEY = "uriProcessorsList";

	@Getter(value = URI_PROCESSORS_LIST_KEY, cardinality = Cardinality.LIST)
	@XMLElement
	public List<T> getUriProcessorsList();

	@Setter(URI_PROCESSORS_LIST_KEY)
	public void setUriProcessorsList(List<T> uriProcessorsList);

	@Adder(URI_PROCESSORS_LIST_KEY)
	public void addToUriProcessorsList(T aUriProcessorsList);

	@Remover(URI_PROCESSORS_LIST_KEY)
	public void removeFromUriProcessorsList(T aUriProcessorsList);

	public T createURIProcessor();

	public T retrieveURIProcessorForType(XMLType aXmlType);

	public static abstract class AbstractXMLModelSlotImpl< T extends AbstractXMLURIProcessor> implements AbstractXMLModelSlot<T> {

		private static final Logger logger = Logger.getLogger(AbstractXMLModelSlot.class.getPackage().getName());

		/* Used to process URIs for XML Objects */
		private List<T> uriProcessors;
		private Hashtable<String,  T> uriProcessorsMap;


		public AbstractXMLModelSlotImpl(){
			super();
			if (uriProcessorsMap == null) {
				uriProcessorsMap = new Hashtable<String, T>();
			}
			if (uriProcessors == null) {
				uriProcessors = new ArrayList<T>();
			}
		}

		@Override
		public Type getType() {
			// TODO Auto-generated method stub
			return null;
		}



		/*=====================================================================================
		 * URI Accessors
		 */
		// TODO Manage the fact that URI May Change


		@Override
		public T retrieveURIProcessorForType(XMLType aXmlType) {

			logger.info("SEARCHING for an uriProcessor for " + aXmlType.getURI());

			T mapParams = uriProcessorsMap.get(aXmlType.getURI());

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

		public void setUriProcessors(List<T> uriProcessingParameters) {
			this.uriProcessors = uriProcessingParameters;
		}

		public List<T> getUriProcessors() {
			return uriProcessors;
		}

		public void updateURIMapForProcessor(T xsuriProc) {
			String uri = xsuriProc._getTypeURI();
			if (uri != null) {
				for (String k : uriProcessorsMap.keySet()) {
					T p = uriProcessorsMap.get(k);
					if (p.equals(xsuriProc)) {
						uriProcessorsMap.remove(k);
					}
				}
				uriProcessorsMap.put(uri, xsuriProc);
			}
		}

		public void addToUriProcessors(T xsuriProc) {
			xsuriProc.setModelSlot(this);
			uriProcessors.add(xsuriProc);
			uriProcessorsMap.put(xsuriProc._getTypeURI().toString(), xsuriProc);
		}

		public void removeFromUriProcessors(T xsuriProc) {
			String uri = xsuriProc._getTypeURI();
			if (uri != null) {
				for (String k : uriProcessorsMap.keySet()) {
					T p = uriProcessorsMap.get(k);
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
		public List<T> getUriProcessorsList() {
			return uriProcessors;
		}

		@Override
		public void setUriProcessorsList(List<T> uriProcList) {
			for (T xsuriProc : uriProcList) {
				addToUriProcessorsList(xsuriProc);
			}
		}

		@Override
		public void addToUriProcessorsList(T xsuriProc) {
			addToUriProcessors(xsuriProc);
		}

		@Override
		public void removeFromUriProcessorsList(T xsuriProc) {
			removeFromUriProcessors(xsuriProc);
		}


	}

}
