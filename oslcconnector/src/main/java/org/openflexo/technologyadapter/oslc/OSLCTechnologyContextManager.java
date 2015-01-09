/*
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of either : 
 * - GNU General Public License as published by
 * the Free Software Foundation version 3 of the License.
 * - EUPL v1.1 : European Union Public Licence
 * 
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License or EUPL for more details.
 *
 * You should have received a copy of the GNU General Public License or 
 * European Union Public Licence along with OpenFlexo. 
 * If not, see <http://www.gnu.org/licenses/>, or http://ec.europa.eu/idabc/eupl.html
 *
 */

package org.openflexo.technologyadapter.oslc;

import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;

public class OSLCTechnologyContextManager extends TechnologyContextManager<OSLCTechnologyAdapter> {

	public OSLCTechnologyContextManager(OSLCTechnologyAdapter adapter, FlexoResourceCenterService resourceCenterService) {
		super(adapter, resourceCenterService);
	}

	@Override
	public OSLCTechnologyAdapter getTechnologyAdapter() {
		return super.getTechnologyAdapter();
	}
}
