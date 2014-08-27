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
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.editionaction.AddXMLIndividual;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.virtualmodel.XMLIndividualRole;

/**
 * 
 * An XML ModelSlot used to edit freely an XML document and simultaneously produce the corresponding MetaModel
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
@ImplementationClass(FreeXMLModelSlot.FreeXMLModelSlotImpl.class)
public interface FreeXMLModelSlot extends AbstractXMLModelSlot {
	
	
    public static abstract class FreeXMLModelSlotImpl extends AbstractXMLModelSlot.AbstractXMLModelSlotImpl implements FreeXMLModelSlot {

        private static final Logger logger = Logger.getLogger(FreeXMLModelSlot.class.getPackage().getName());

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
        public String getURIForObject(
                TypeAwareModelSlotInstance<XMLModel, XMLMetaModel, ? extends TypeAwareModelSlot<XMLModel, XMLMetaModel>> msInstance, Object o) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object retrieveObjectWithURI(
                TypeAwareModelSlotInstance<XMLModel, XMLMetaModel, ? extends TypeAwareModelSlot<XMLModel, XMLMetaModel>> msInstance,
                String objectURI) {
            // TODO Auto-generated method stub
            return null;
        }


        @Override
        public boolean isStrictMetaModelling() {
            // TODO Auto-generated method stub
            return false;
        }


		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public ModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new FreeXMLModelSlotInstanceConfiguration(this, action);
		}

    }

}
