/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.fml;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.ontology.fml.IndividualRole;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;

@ModelEntity
@ImplementationClass(EMFObjectIndividualRole.EMFObjectIndividualRoleImpl.class)
@XMLElement
@FML("EMFClassClassRole")
public interface EMFObjectIndividualRole extends IndividualRole<EMFObjectIndividual> {

	public static abstract class EMFObjectIndividualRoleImpl extends IndividualRoleImpl<EMFObjectIndividual>
			implements EMFObjectIndividualRole {

		public EMFObjectIndividualRoleImpl() {
			super();
		}

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return EMFTechnologyAdapter.class;
		}

	}
}
