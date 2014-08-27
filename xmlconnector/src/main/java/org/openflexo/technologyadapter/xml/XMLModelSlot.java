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

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.FlexoModelResource;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.view.FreeModelSlotInstance;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.XMLElement;
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
@ImplementationClass(XMLModelSlot.XMLModelSlotImpl.class)
public interface XMLModelSlot extends AbstractXMLModelSlot<XMLURIProcessor>, TypeAwareModelSlot<XMLModel, XMLMetaModel>  {

	@PropertyIdentifier(type = XMLMetaModel.class)
	public static final String METAMODEL = "metamodel";
	

	@Getter(value = METAMODEL)
	public XMLMetaModel getMetamodel();
	
	
     public static abstract class XMLModelSlotImpl extends AbstractXMLModelSlot.AbstractXMLModelSlotImpl<XMLURIProcessor> implements XMLModelSlot {
	// public static abstract class XMLModelSlotImpl extends TypeAwareModelSlotImpl<XMLModel, XMLMetaModel> implements XMLModelSlot {

        @Override
		public TechnologyAdapterResource<XMLModel, ?> createProjectSpecificEmptyResource(View view, String filename, String modelUri) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TechnologyAdapterResource<XMLModel, ?> createSharedEmptyResource(FlexoResourceCenter<?> resourceCenter, String relativePath,
				String filename, String modelUri) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getURIForObject(FreeModelSlotInstance<XMLModel, ? extends FreeModelSlot<XMLModel>> msInstance, Object o) {
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

		@Override
		public Object retrieveObjectWithURI(FreeModelSlotInstance<XMLModel, ? extends FreeModelSlot<XMLModel>> msInstance, String objectURI) {
			// TODO Auto-generated method stub
			return null;
		}

		private static final Logger logger = Logger.getLogger(XMLModelSlot.class.getPackage().getName());

        
        public XMLModelSlotImpl(){
        	super();
        }

        @Override
        public Type getType() {
            // TODO Auto-generated method stub
            return null;
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


		@Override
		public XMLURIProcessor createURIProcessor() {
			XMLURIProcessor xsuriProc = getVirtualModelFactory().newInstance(XMLURIProcessor.class);
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
