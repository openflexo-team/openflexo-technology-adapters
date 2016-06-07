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

package org.openflexo.technologyadapter.gina.model;

import java.lang.reflect.Type;

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.foundation.fml.binding.FlexoConceptBindingFactory;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.model.FIBVariable;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot.VariableAssignment;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent.GINAFIBComponentImpl;
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
@XMLElement
public interface GINAFIBComponent
		extends FlexoObject, TechnologyObject<GINATechnologyAdapter>, ResourceData<GINAFIBComponent>, BindingEvaluationContext {

	// public static final String RESOURCE_KEY = "resource";
	public static final String COMPONENT_KEY = "component";
	public static final String TECHNOLOGY_ADAPTER_KEY = "technologyAdapter";

	@Getter(COMPONENT_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public FIBComponent getComponent();

	@Setter(COMPONENT_KEY)
	public void setComponent(FIBComponent aComponent);

	@Override
	// @Getter(value = RESOURCE_KEY)
	public GINAFIBComponentResource getResource();

	// @Setter(value = RESOURCE_KEY)
	@Override
	public void setResource(FlexoResource<GINAFIBComponent> aResource);

	@Override
	@Getter(value = TECHNOLOGY_ADAPTER_KEY, ignoreType = true)
	public GINATechnologyAdapter getTechnologyAdapter();

	@Setter(value = TECHNOLOGY_ADAPTER_KEY)
	public void setTechnologyAdapter(GINATechnologyAdapter technologyAdapter);

	/**
	 * Ensure that the whole binding context (BindingFactory and BindingModel) is bound to an {@link AbstractVirtualModel} using the
	 * specifications given by a {@link FIBComponentModelSlot}
	 * 
	 * @param virtualModel
	 * @param modelSlot
	 */
	public void bindTo(AbstractVirtualModel<?> virtualModel, FIBComponentModelSlot modelSlot);

	public abstract static class GINAFIBComponentImpl extends FlexoObjectImpl implements GINAFIBComponent {

		@Override
		public GINATechnologyAdapter getTechnologyAdapter() {
			if (getResource() != null) {
				return getResource().getTechnologyAdapter();
			}
			return null;
		}

		/**
		 * Ensure that the whole binding context (BindingFactory and BindingModel) is bound to an {@link AbstractVirtualModel} using the
		 * specifications given by a {@link FIBComponentModelSlot}
		 * 
		 * @param virtualModel
		 * @param modelSlot
		 */
		@Override
		public void bindTo(AbstractVirtualModel<?> virtualModel, FIBComponentModelSlot modelSlot) {
			System.out.println("******* bindTo " + virtualModel + " using " + modelSlot);

			if (getComponent() == null) {
				return;
			}

			getComponent().setBindingFactory(new FlexoConceptBindingFactory(virtualModel.getViewPoint()));

			for (VariableAssignment variableAssign : modelSlot.getAssignments()) {
				FIBVariable<?> returned = getComponent().getVariable(variableAssign.getVariable());

				System.out.println("On s'occupe de la variable " + returned);

				if (returned == null) {
					returned = getComponent().getModelFactory().newFIBVariable(getComponent(), variableAssign.getVariable());
				}
				DataBinding<?> value = variableAssign.getValue();

				System.out.println("value = " + value + " valid=" + value.isValid() + " reason:" + value.invalidBindingReason());

				if (value != null && value.isSet() && value.isValid()) {
					Type analyzedType = value.getAnalyzedType();
					// returned.setType(value.getAnalyzedType());

					System.out.println("analyzedType=" + analyzedType);
					System.out.println("returned.getType()=" + returned.getType());

					if (TypeUtils.isTypeAssignableFrom(analyzedType, returned.getType())) {
						// Type is conform, does nothing
						System.out.println("On fait rien");
					}
					else /*if (!TypeUtils.isTypeAssignableFrom(returned.getType(), analyzedType))*/ {
						returned.setType(analyzedType);
						// We force type of variable to be type analyzed from binding
						System.out.println("****** On passe le type de " + variableAssign.getVariable() + " de " + returned.getType()
								+ " a " + analyzedType);
					}
					// returned.setType(analyzedType);
				}
			}

			// Bacause type may have changed, we have to revalidate all bindings of the component
			recursivelyRevalidateBindings(getComponent());

		}

		private void recursivelyRevalidateBindings(FIBComponent c) {

			c.revalidateBindings();
			if (c instanceof FIBContainer) {
				for (FIBComponent c2 : ((FIBContainer) c).getSubComponents()) {
					recursivelyRevalidateBindings(c2);
				}
			}
		}

	}
}
