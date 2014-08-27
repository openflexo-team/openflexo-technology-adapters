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

import java.util.logging.Logger;

import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.editionaction.AddXMLIndividual;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
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
public interface XMLModelSlot extends TypeAwareModelSlot<XMLModel, XMLMetaModel>, AbstractXMLModelSlot<XMLURIProcessor>  {

	@PropertyIdentifier(type = XMLMetaModel.class)
	public static final String METAMODEL = "metamodel";


	@Getter(value = METAMODEL)
	public XMLMetaModel getMetamodel();


	// public static abstract class XMLModelSlotImpl extends AbstractXMLModelSlot.AbstractXMLModelSlotImpl<XMLURIProcessor> implements XMLModelSlot {
	// TODO : check for multiple inheritance issues
	public static abstract class XMLModelSlotImpl extends TypeAwareModelSlotImpl<XMLModel, XMLMetaModel> implements XMLModelSlot {


		private static final Logger logger = Logger.getLogger(XMLModelSlot.class.getPackage().getName());

		 private static ModelFactory MF;
		 
		    static{
		    	try {
					MF = new ModelFactory(ModelContextLibrary.getCompoundModelContext(XMLModelSlot.class,
											  										  XMLURIProcessor.class));
				} catch (ModelDefinitionException e) {
					e.printStackTrace();
				}
		    }

		public XMLModelSlotImpl(){
			super();
		}


		@Override
		public Class<? extends TechnologyAdapter> getTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}
		
		@Override
		public XMLURIProcessor createURIProcessor() {
			XMLURIProcessor xsuriProc = MF.newInstance(XMLURIProcessor.class);
			xsuriProc.setModelSlot(this);
			this.addToUriProcessorsList(xsuriProc);
			return xsuriProc;
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
