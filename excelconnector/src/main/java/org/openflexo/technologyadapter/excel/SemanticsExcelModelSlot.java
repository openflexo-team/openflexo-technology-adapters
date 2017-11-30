/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
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
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
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

/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.excel;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoBehaviours;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.InferedFMLRTModelSlot;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.semantics.fml.InsertSEObject;
import org.openflexo.technologyadapter.excel.semantics.fml.CreateSEResource;
import org.openflexo.technologyadapter.excel.semantics.fml.SEColumnRole;
import org.openflexo.technologyadapter.excel.semantics.fml.SEDataAreaRole;
import org.openflexo.technologyadapter.excel.semantics.fml.SEInitializer;
import org.openflexo.technologyadapter.excel.semantics.fml.SEReferenceRole;
import org.openflexo.technologyadapter.excel.semantics.fml.SEVirtualModelInstanceType;
import org.openflexo.technologyadapter.excel.semantics.model.SEObjectActorReference;
import org.openflexo.technologyadapter.excel.semantics.model.SEVirtualModelInstance;

/**
 * An implementation of a {@link ModelSlot} providing basic access to a set of data stored in an excel workbook, and reflected as FML
 * instances objects<br>
 * 
 * This {@link ModelSlot} is contract-based, as it is configured with a {@link VirtualModel} modelling data beeing accessed through this
 * {@link ModelSlot}. It means that data stored in database is locally reflected as {@link FlexoConceptInstance}s in a
 * {@link VirtualModelInstance} (instance of contract {@link VirtualModel})
 * 
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@XMLElement
@ImplementationClass(SemanticsExcelModelSlot.SemanticsExcelModelSlotImpl.class)
@DeclareFlexoRoles({ SEColumnRole.class, SEDataAreaRole.class, SEReferenceRole.class })
@DeclareEditionActions({ CreateSEResource.class, InsertSEObject.class })
@DeclareFlexoBehaviours({ SEInitializer.class })
@DeclareActorReferences({ SEObjectActorReference.class })
public interface SemanticsExcelModelSlot extends InferedFMLRTModelSlot<SEVirtualModelInstance, ExcelTechnologyAdapter> {

	abstract class SemanticsExcelModelSlotImpl extends InferedFMLRTModelSlotImpl<SEVirtualModelInstance, ExcelTechnologyAdapter>
			implements SemanticsExcelModelSlot {

		private SEVirtualModelInstanceType type;

		@Override
		public Class<ExcelTechnologyAdapter> getTechnologyAdapterClass() {
			return ExcelTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> flexoRoleClass) {
			return super.defaultFlexoRoleName(flexoRoleClass);
		}

		@Override
		public ExcelTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (ExcelTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public Type getType() {
			if (type == null || type.getVirtualModel() != getAccessedVirtualModel()) {
				type = SEVirtualModelInstanceType.getVirtualModelInstanceType(getAccessedVirtualModel());
			}
			return type;
		}

		@Override
		public void setAccessedVirtualModel(VirtualModel aVirtualModel) {
			if (aVirtualModel != getAccessedVirtualModel()) {
				super.setAccessedVirtualModel(aVirtualModel);
				type = SEVirtualModelInstanceType.getVirtualModelInstanceType(getAccessedVirtualModel());
			}
		}

	}

}
