/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Oslcconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.oslc.model.core;

import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;

@ModelEntity
@ImplementationClass(OSLCQueryCapability.OSLCQueryCapabilityImpl.class)
@XMLElement(xmlTag = "OSLCQueryCapability")
public interface OSLCQueryCapability extends OSLCResource {

	public static final String OSLC_QUERY_CAPABILITY_KEY = "OSLCQueryCapability";

	public static final String OSLC_SERVICE_KEY = "OSLCService";

	@Getter(value = OSLC_QUERY_CAPABILITY_KEY, ignoreType = true)
	public QueryCapability getOSLCQueryCapability();

	@Setter(OSLC_QUERY_CAPABILITY_KEY)
	public void setOSLCQueryCapability(QueryCapability queryCapability);

	@Getter(value = OSLC_SERVICE_KEY, inverse = OSLCService.QUERY_CAPABILITIES_KEY)
	public OSLCService getOSLCService();

	@Setter(OSLC_SERVICE_KEY)
	public void setOSLCService(OSLCService oslcService);

	public static abstract class OSLCQueryCapabilityImpl extends OSLCResourceImpl implements OSLCQueryCapability {
		@Override
		public String getUri() {
			if (getOSLCQueryCapability().getAbout() != null) {
				return getOSLCQueryCapability().getAbout().toString();
			}
			else {
				return getOSLCQueryCapability().getQueryBase().toString();
			}

		}

	}

}
