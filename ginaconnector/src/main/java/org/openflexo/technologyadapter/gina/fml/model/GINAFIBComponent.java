/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.gina.fml.model;

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.fml.model.GINAFIBComponent.GINAFIBComponentImpl;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;

/**
 * A {@link FlexoObject} (an object of model federation infrastructure) that references a {@link FIBComponent}<br>
 * (Access layer between Openflexo-Core and GINA framework)
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(GINAFIBComponentImpl.class)
public interface GINAFIBComponent
		extends FlexoObject, TechnologyObject<GINATechnologyAdapter>, ResourceData<GINAFIBComponent>, BindingEvaluationContext {

	public static final String RESOURCE_KEY = "resource";
	public static final String TECHNOLOGY_ADAPTER_KEY = "technologyAdapter";

	@Override
	@Getter(value = RESOURCE_KEY)
	@XMLAttribute
	public GINAFIBComponentResource getResource();

	@Setter(value = RESOURCE_KEY)
	public void setResource(GINAFIBComponentResource aResource);

	@Override
	@Getter(value = TECHNOLOGY_ADAPTER_KEY, ignoreType = true)
	public GINATechnologyAdapter getTechnologyAdapter();

	@Setter(value = TECHNOLOGY_ADAPTER_KEY)
	public void setTechnologyAdapter(GINATechnologyAdapter technologyAdapter);

	public abstract static class GINAFIBComponentImpl extends FlexoObjectImpl implements GINAFIBComponent {

		private GINATechnologyAdapter technologyAdapter;

		@Override
		public GINATechnologyAdapter getTechnologyAdapter() {
			return technologyAdapter;
		}

		@Override
		public void setTechnologyAdapter(GINATechnologyAdapter technologyAdapter) {
			this.technologyAdapter = technologyAdapter;
		}

	}
}
