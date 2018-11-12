/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Powerpointconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Slide;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.pamela.annotations.Implementation;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

@ModelEntity
public interface PowerpointModelSlot extends FreeModelSlot<PowerpointSlideshow> {

	@Implementation
	public abstract class PowerpointModelSlotImpl implements PowerpointModelSlot {

		private static final Logger logger = Logger.getLogger(PowerpointModelSlot.class.getPackage().getName());

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (Slide.class.isAssignableFrom(patternRoleClass)) {
				return "slide";
			}
			else if (AutoShape.class.isAssignableFrom(patternRoleClass)) {
				return "shape";
			}
			logger.warning("Unexpected pattern property: " + patternRoleClass.getName());
			return null;
		}

		@Override
		public String getTypeDescription() {
			return "Powerpoint Slide Show";
		};

		@Override
		public Type getType() {
			return PowerpointSlideshow.class;
		}

		@Override
		public String getURIForObject(PowerpointSlideshow resourceData, Object o) {
			// TODO
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(PowerpointSlideshow resourceData, String objectURI) {
			// TODO
			return null;
		}

	}

}
