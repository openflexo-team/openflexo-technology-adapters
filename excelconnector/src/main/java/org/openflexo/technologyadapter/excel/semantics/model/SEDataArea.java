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

package org.openflexo.technologyadapter.excel.semantics.model;

import java.util.ArrayList;
import java.util.Collection;

import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.semantics.fml.SEDataAreaRole;

/**
 * Run-time concept referencing all {@link SEFlexoConceptInstance} encoded in a data area in an excel workbook<br>
 * Model concept is {@link SEDataAreaRole}
 * 
 */
@SuppressWarnings("serial")
public class SEDataArea<FCI extends SEFlexoConceptInstance> extends ArrayList<FCI> {

	private SEDataAreaRole role;
	private ExcelCellRange cellRange;

	public SEDataArea(SEDataAreaRole role, ExcelCellRange cellRange, Collection<FCI> instances) {
		this.role = role;
		this.cellRange = cellRange;
		addAll(instances);
	}

	public ExcelCellRange getCellRange() {
		return cellRange;
	}

	public SEDataAreaRole getRole() {
		return role;
	}

	public FlexoConcept getFlexoConceptType() {
		return getRole().getFlexoConceptType();
	}
}
