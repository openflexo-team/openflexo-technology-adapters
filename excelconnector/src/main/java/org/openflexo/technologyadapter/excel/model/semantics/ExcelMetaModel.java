/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

package org.openflexo.technologyadapter.excel.model.semantics;

import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelObject;
public class ExcelMetaModel extends ExcelObject implements FlexoMetaModel<ExcelMetaModel> {

	public ExcelMetaModel(ExcelTechnologyAdapter adapter) {
		super(adapter);
	}

	@Override
	public org.openflexo.foundation.resource.FlexoResource<ExcelMetaModel> getResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResource(org.openflexo.foundation.resource.FlexoResource<ExcelMetaModel> resource) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsReadOnly(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getObject(String objectURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


}
