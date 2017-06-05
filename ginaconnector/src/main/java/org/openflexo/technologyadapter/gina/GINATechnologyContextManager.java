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

package org.openflexo.technologyadapter.gina;

import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.gina.FIBLibrary;
import org.openflexo.gina.FIBLibrary.FIBLibraryImpl;

public class GINATechnologyContextManager extends TechnologyContextManager<GINATechnologyAdapter> {

	private FIBLibrary fibLibrary;

	public GINATechnologyContextManager(GINATechnologyAdapter adapter, FlexoResourceCenterService resourceCenterService) {
		super(adapter, resourceCenterService);
		fibLibrary = FIBLibraryImpl.createInstance(resourceCenterService.getServiceManager().getTechnologyAdapterService());
	}

	public FIBLibrary getFIBLibrary() {
		return fibLibrary;
	}
}
