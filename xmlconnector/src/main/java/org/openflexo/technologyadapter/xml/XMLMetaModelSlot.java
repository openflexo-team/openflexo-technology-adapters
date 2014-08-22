/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2013-- Openflexo
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

import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclareFetchRequests;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.editionaction.AddXMLType;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.virtualmodel.XMLIndividualRole;
import org.openflexo.technologyadapter.xml.virtualmodel.XMLTypeRole;

/**
 * 
 *   An XML ModelSlot used to edit an XML MetaModel, that can ben serialized as an XSD document...
 *   
 * @author Luka Le Roux, Sylvain Guerin, Christophe Guychard
 * 
 */

@DeclarePatternRoles({ // All pattern roles available through this model slot
	@DeclarePatternRole(FML = "XMLType", flexoRoleClass = XMLTypeRole.class), })
@DeclareEditionActions({ // All edition actions available through this model slot
	@DeclareEditionAction(FML = "AddXSIndividual", editionActionClass = AddXMLType.class)})
@DeclareFetchRequests({ // All requests available through this model slot
})
@ModelEntity
@ImplementationClass(XMLMetaModelSlot.XSDModelSlotImpl.class)
@XMLElement
public interface XMLMetaModelSlot extends FreeModelSlot<XMLMetaModel> {


	public static abstract class XSDModelSlotImpl extends FreeModelSlotImpl<XMLMetaModel> implements XMLMetaModelSlot {

		static final Logger logger = Logger.getLogger(XMLMetaModelSlot.class.getPackage().getName());


		public XSDModelSlotImpl() {
			super();
		}

		@Override
		public Class<XMLTechnologyAdapter> getTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (XMLTypeRole.class.isAssignableFrom(patternRoleClass)) {
				return "XMLType";
			} else if (XMLIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "XMLIndividual";
			}
			return null;
		}

		@Override
		public Type getType() {
			return XMLModel.class;
		}

		@Override
		public XMLTechnologyAdapter getTechnologyAdapter() {
			return (XMLTechnologyAdapter) super.getTechnologyAdapter();
		}

		// FIXME

		/*
		@Override
		public XMLFileResource createProjectSpecificEmptyModel(FlexoProject project, String filename, String modelUri,
				FlexoMetaModelResource<XMLModel, XMLMetaModel, ?> metaModelResource) {
			return getTechnologyAdapter().createNewXMLFile(project, filename, modelUri, metaModelResource);
		}

		@Override
		public XMLFileResource createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri, FlexoMetaModelResource<XMLModel, XMLMetaModel, ?> metaModelResource) {
			return (XMLFileResource) getTechnologyAdapter().createNewXMLFile((FileSystemBasedResourceCenter) resourceCenter,
					relativePath, filename, modelUri, (XSDMetaModelResource) metaModelResource);
		}
		 */

	}
}
