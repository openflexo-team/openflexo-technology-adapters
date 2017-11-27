/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.semantics.fml;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.PropertyCardinality;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.semantics.model.SEFlexoConceptInstance;

/**
 * A role specific to Semantics/Excel technology (SEModelSlot) representing a data area in an excel sheet, which is reflected by a list of
 * {@link SEFlexoConceptInstance}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(SEDataAreaRole.SEDataAreaRoleImpl.class)
@XMLElement
public interface SEDataAreaRole extends FlexoConceptInstanceRole {

	@PropertyIdentifier(type = ExcelCellRange.class)
	String CELL_RANGE_KEY = "cellRange";

	@Getter(CELL_RANGE_KEY)
	@XMLAttribute
	public ExcelCellRange getCellRange();

	@Setter(CELL_RANGE_KEY)
	public void setCellRange(ExcelCellRange dataRange);

	public ExcelWorkbook getExcelWorkbook();

	public static abstract class SEDataAreaRoleImpl extends FlexoConceptInstanceRoleImpl implements SEDataAreaRole {

		private static final Logger logger = Logger.getLogger(SEDataAreaRoleImpl.class.getPackage().getName());

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return ExcelTechnologyAdapter.class;
		}

		@Override
		public PropertyCardinality getCardinality() {
			return PropertyCardinality.ZeroMany;
		}

		@Override
		public ExcelWorkbook getExcelWorkbook() {
			if (getFlexoConcept() instanceof SEVirtualModel
					&& ((SEVirtualModel) getFlexoConcept()).getTemplateExcelWorkbookResource() != null) {
				return ((SEVirtualModel) getFlexoConcept()).getTemplateExcelWorkbookResource().getExcelWorkbook();
			}
			if (getFlexoConcept() instanceof SEFlexoConcept
					&& ((SEFlexoConcept) getFlexoConcept()).getTemplateExcelWorkbookResource() != null) {
				return ((SEFlexoConcept) getFlexoConcept()).getTemplateExcelWorkbookResource().getExcelWorkbook();
			}
			return null;
		}

	}
}
