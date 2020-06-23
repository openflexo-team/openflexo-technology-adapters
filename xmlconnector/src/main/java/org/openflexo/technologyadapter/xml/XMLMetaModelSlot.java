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
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.fml.XMLIndividualRole;
import org.openflexo.technologyadapter.xml.fml.XMLTypeRole;
import org.openflexo.technologyadapter.xml.fml.editionaction.AddXMLType;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;

/**
 * 
 * An XML ModelSlot used to edit an XML MetaModel, that can ben serialized as an XSD document...
 * 
 * @author Luka Le Roux, Sylvain Guerin, Christophe Guychard
 * 
 */

@DeclareFlexoRoles({ XMLTypeRole.class })
@DeclareEditionActions({ AddXMLType.class })
@DeclareFetchRequests({})
@ModelEntity
@ImplementationClass(XMLMetaModelSlot.XMLMetaModelSlotImpl.class)
@XMLElement
@FML("XMLMetaModelSlot")
public interface XMLMetaModelSlot extends FreeModelSlot<XMLMetaModel> {

	public static abstract class XMLMetaModelSlotImpl extends FreeModelSlotImpl<XMLMetaModel> implements XMLMetaModelSlot {

		static final Logger logger = Logger.getLogger(XMLMetaModelSlot.class.getPackage().getName());

		@Override
		public Class<XMLTechnologyAdapter> getTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (XMLTypeRole.class.isAssignableFrom(patternRoleClass)) {
				return "XMLType";
			}
			else if (XMLIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "XMLIndividual";
			}
			return null;
		}

		@Override
		public Type getType() {
			return XMLModel.class;
		}

		@Override
		public XMLTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (XMLTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

	}
}
