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

import java.io.File;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.powerpoint.fml.PowerpointShapeRole;
import org.openflexo.technologyadapter.powerpoint.fml.PowerpointSlideRole;
import org.openflexo.technologyadapter.powerpoint.fml.editionaction.AddPowerpointShape;
import org.openflexo.technologyadapter.powerpoint.fml.editionaction.AddPowerpointSlide;
import org.openflexo.technologyadapter.powerpoint.fml.editionaction.SelectPowerpointShape;
import org.openflexo.technologyadapter.powerpoint.fml.editionaction.SelectPowerpointSlide;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointObject;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;

/**
 * Implementation of a basic ModelSlot class for the Powerpoint technology adapter<br>
 * 
 * @author Vincent Leildé, Sylvain Guérin
 * 
 */
@DeclareFlexoRoles({ PowerpointSlideRole.class, PowerpointShapeRole.class })
@DeclareEditionActions({ AddPowerpointSlide.class, AddPowerpointShape.class })
@DeclareFetchRequests({ SelectPowerpointSlide.class, SelectPowerpointShape.class })
@ModelEntity
@ImplementationClass(BasicPowerpointModelSlot.BasicPowerpointModelSlotImpl.class)
@XMLElement
@FML("BasicPowerpointModelSlot")
public interface BasicPowerpointModelSlot extends FreeModelSlot<PowerpointSlideshow>, PowerpointModelSlot {

	public static abstract class BasicPowerpointModelSlotImpl extends FreeModelSlotImpl<PowerpointSlideshow>
			implements BasicPowerpointModelSlot {

		private static final Logger logger = Logger.getLogger(BasicPowerpointModelSlot.class.getPackage().getName());

		private BasicPowerpointModelSlotURIProcessor uriProcessor;

		public BasicPowerpointModelSlotURIProcessor getUriProcessor() {
			if (uriProcessor == null && getFMLModelFactory() != null) {
				uriProcessor = getFMLModelFactory().newInstance(BasicPowerpointModelSlotURIProcessor.class);
			}
			return uriProcessor;
		}

		@Override
		public Class<PowerpointTechnologyAdapter> getTechnologyAdapterClass() {
			return PowerpointTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (PowerpointSlideRole.class.isAssignableFrom(patternRoleClass)) {
				return "slide";
			}
			else if (PowerpointShapeRole.class.isAssignableFrom(patternRoleClass)) {
				return "shape";
			}
			return null;
		}

		@Override
		public Type getType() {
			return PowerpointSlideshow.class;
		}

		@Override
		public ModelSlotInstanceConfiguration<BasicPowerpointModelSlot, PowerpointSlideshow> createConfiguration(FlexoConceptInstance fci,
				FlexoResourceCenter<?> rc) {
			return new BasicPowerpointModelSlotInstanceConfiguration(this, fci, rc);
		}

		@Override
		public String getURIForObject(FreeModelSlotInstance<PowerpointSlideshow, ? extends FreeModelSlot<PowerpointSlideshow>> msInstance,
				Object o) {
			PowerpointObject powerpointObject = (PowerpointObject) o;
			return getUriProcessor().getURIForObject(msInstance, powerpointObject);
		}

		@Override
		public Object retrieveObjectWithURI(
				FreeModelSlotInstance<PowerpointSlideshow, ? extends FreeModelSlot<PowerpointSlideshow>> msInstance, String objectURI) {

			try {
				return getUriProcessor().retrieveObjectWithURI(msInstance, objectURI);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public PowerpointTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (PowerpointTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public PowerpointSlideshowResource createProjectSpecificEmptyResource(AbstractVirtualModelInstance<?, ?> view, String filename,
				String modelUri) {
			try {
				return getModelSlotTechnologyAdapter().createNewSlideshow((FlexoResourceCenter<File>) view.getResourceCenter(), filename,
						modelUri);
			} catch (SaveResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModelDefinitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public PowerpointSlideshowResource createSharedEmptyResource(FlexoResourceCenter<?> resourceCenter, String relativePath,
				String filename, String modelUri) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
