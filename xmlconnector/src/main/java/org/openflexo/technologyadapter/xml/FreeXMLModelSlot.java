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

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.fml.XMLIndividualRole;
import org.openflexo.technologyadapter.xml.fml.editionaction.AddXMLIndividual;
import org.openflexo.technologyadapter.xml.fml.editionaction.CreateXMLFileResource;
import org.openflexo.technologyadapter.xml.fml.editionaction.GetXMLDocumentRoot;
import org.openflexo.technologyadapter.xml.fml.editionaction.SetXMLDocumentRoot;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLModel;

/**
 * 
 * An XML ModelSlot used to edit freely an XML document and simultaneously produce the corresponding MetaModel
 * 
 * @author xtof
 * 
 */
@DeclareFlexoRoles({ XMLIndividualRole.class })
@DeclareEditionActions({ CreateXMLFileResource.class, AddXMLIndividual.class, GetXMLDocumentRoot.class, SetXMLDocumentRoot.class })
@ModelEntity
@XMLElement
@ImplementationClass(FreeXMLModelSlot.FreeXMLModelSlotImpl.class)
@Imports({ @Import(FreeXMLURIProcessor.class), })
@FML("FreeXMLModelSlot")
public interface FreeXMLModelSlot extends FreeModelSlot<XMLModel>, AbstractXMLModelSlot<FreeXMLURIProcessor> {

	// public static abstract class FreeXMLModelSlotImpl extends AbstractXMLModelSlot.AbstractXMLModelSlotImpl<FreeXMLURIProcessor>
	// implements FreeXMLModelSlot {
	public static abstract class FreeXMLModelSlotImpl extends FreeModelSlotImpl<XMLModel> implements FreeXMLModelSlot {

		private static final Logger logger = Logger.getLogger(FreeXMLModelSlot.class.getPackage().getName());

		/* Used to process URIs for XML Objects */
		private List<FreeXMLURIProcessor> uriProcessors;
		private Hashtable<String, FreeXMLURIProcessor> uriProcessorsMap;

		public FreeXMLModelSlotImpl() {
			super();
		}

		@Override
		public Class<? extends TechnologyAdapter> getTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}

		@Override
		public FreeXMLURIProcessor createURIProcessor() {
			FreeXMLURIProcessor xsuriProc = getFMLModelFactory().newInstance(FreeXMLURIProcessor.class);
			xsuriProc.setModelSlot(this);
			this.addToUriProcessorsList(xsuriProc);
			return xsuriProc;
		}

		/*=====================================================================================
		 * URI Accessors
		 */
		// TODO Manage the fact that URI May Change

		@Override
		public FreeXMLURIProcessor retrieveURIProcessorForType(XMLType aXmlType) {

			logger.info("SEARCHING for an uriProcessor for " + aXmlType.getURI());

			FreeXMLURIProcessor mapParams = uriProcessorsMap.get(aXmlType.getURI());

			if (mapParams == null) {
				XMLType s = aXmlType.getSuperType();
				// on ne cherche que le premier...
				logger.info("SEARCHING for an uriProcessor for " + s.getURI());
				mapParams = retrieveURIProcessorForType(s);

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

		public void setUriProcessors(List<FreeXMLURIProcessor> uriProcessingParameters) {
			this.uriProcessors = uriProcessingParameters;
		}

		public List<FreeXMLURIProcessor> getUriProcessors() {
			return uriProcessors;
		}

		public void updateURIMapForProcessor(FreeXMLURIProcessor xmluriProc) {
			String uri = xmluriProc.getTypeURI();
			if (uri != null) {
				for (String k : uriProcessorsMap.keySet()) {
					FreeXMLURIProcessor p = uriProcessorsMap.get(k);
					if (p.equals(xmluriProc)) {
						uriProcessorsMap.remove(k);
					}
				}
				uriProcessorsMap.put(uri, xmluriProc);
			}
		}

		public void addToUriProcessors(FreeXMLURIProcessor xmluriProc) {
			xmluriProc.setModelSlot(this);
			uriProcessors.add(xmluriProc);
			uriProcessorsMap.put(xmluriProc.getTypeURI().toString(), xmluriProc);
		}

		public void removeFromUriProcessors(FreeXMLURIProcessor xmluriProc) {
			String uri = xmluriProc.getTypeURI();
			if (uri != null) {
				for (String k : uriProcessorsMap.keySet()) {
					FreeXMLURIProcessor p = uriProcessorsMap.get(k);
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
		public List<FreeXMLURIProcessor> getUriProcessorsList() {
			return uriProcessors;
		}

		@Override
		public void setUriProcessorsList(List<FreeXMLURIProcessor> uriProcList) {
			for (FreeXMLURIProcessor xmluriProc : uriProcList) {
				addToUriProcessorsList(xmluriProc);
			}
		}

		@Override
		public void addToUriProcessorsList(FreeXMLURIProcessor xmluriProc) {
			addToUriProcessors(xmluriProc);
		}

		@Override
		public void removeFromUriProcessorsList(FreeXMLURIProcessor xmluriProc) {
			removeFromUriProcessors(xmluriProc);
		}

		@Override
		public Type getType() {
			return XMLModel.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> flexoRoleClass) {
			return flexoRoleClass.getSimpleName();
		}

	}

}
