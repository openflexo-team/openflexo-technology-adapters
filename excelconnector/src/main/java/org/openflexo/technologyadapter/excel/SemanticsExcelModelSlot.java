/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.AbstractCreateVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.FlexoModelResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.fml.BusinessConceptInstanceRole;
import org.openflexo.technologyadapter.excel.fml.BusinessConceptTypeRole;
import org.openflexo.technologyadapter.excel.fml.ExcelCellRole;
import org.openflexo.technologyadapter.excel.fml.ExcelRowRole;
import org.openflexo.technologyadapter.excel.fml.ExcelSheetRole;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddBusinessConceptInstance;
import org.openflexo.technologyadapter.excel.model.semantics.ExcelMetaModel;
import org.openflexo.technologyadapter.excel.model.semantics.ExcelModel;

/**
 * Implementation of the ModelSlot class for the Excel technology adapter<br>
 * We assert here that the spreadsheet is interpretated through a ExcelMetaModel, and data are wrapped into BusinessConcepts.
 * 
 * @author Vincent Leildé, Sylvain Guérin
 * 
 */
@DeclareFlexoRoles({ BusinessConceptTypeRole.class, BusinessConceptInstanceRole.class })
@DeclareEditionActions({ AddBusinessConceptInstance.class })
@ModelEntity
@ImplementationClass(SemanticsExcelModelSlot.SemanticsExcelModelSlotImpl.class)
@XMLElement
public interface SemanticsExcelModelSlot extends TypeAwareModelSlot<ExcelModel, ExcelMetaModel> {

	public abstract static class SemanticsExcelModelSlotImpl extends TypeAwareModelSlotImpl<ExcelModel, ExcelMetaModel> implements
			SemanticsExcelModelSlot {

		private static final Logger logger = Logger.getLogger(SemanticsExcelModelSlot.class.getPackage().getName());

		@Override
		public Class<ExcelTechnologyAdapter> getTechnologyAdapterClass() {
			return ExcelTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (ExcelCellRole.class.isAssignableFrom(patternRoleClass)) {
				return "cell";
			} else if (ExcelRowRole.class.isAssignableFrom(patternRoleClass)) {
				return "row";
			} else if (ExcelSheetRole.class.isAssignableFrom(patternRoleClass)) {
				return "sheet";
			}
			return null;
		}

		@Override
		public Type getType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ModelSlotInstanceConfiguration<SemanticsExcelModelSlot, ExcelModel> createConfiguration(AbstractCreateVirtualModelInstance action) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FlexoModelResource<ExcelModel, ExcelMetaModel, ?, ?> createProjectSpecificEmptyModel(FlexoProject project, String filename,
				String modelUri, FlexoMetaModelResource<ExcelModel, ExcelMetaModel, ?> metaModelResource) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FlexoModelResource<ExcelModel, ExcelMetaModel, ?, ?> createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter,
				String relativePath, String filename, String modelUri,
				FlexoMetaModelResource<ExcelModel, ExcelMetaModel, ?> metaModelResource) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getURIForObject(
				TypeAwareModelSlotInstance<ExcelModel, ExcelMetaModel, ? extends TypeAwareModelSlot<ExcelModel, ExcelMetaModel>> msInstance,
				Object o) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(
				TypeAwareModelSlotInstance<ExcelModel, ExcelMetaModel, ? extends TypeAwareModelSlot<ExcelModel, ExcelMetaModel>> msInstance,
				String objectURI) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isStrictMetaModelling() {
			return true;
		}

		@Override
		public String getTypeDescription() {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
