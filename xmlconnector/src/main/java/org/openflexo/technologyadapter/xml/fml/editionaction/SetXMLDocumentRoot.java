/*
 * (c) Copyright 2012-2013 Openflexo
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
package org.openflexo.technologyadapter.xml.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.fml.XMLActorReference;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;

@ModelEntity
@ImplementationClass(SetXMLDocumentRoot.SetXMLDocumentRootImpl.class)
@XMLElement
@Imports({ @Import(XMLActorReference.class), })
@FML("SetXMLDocumentRoot")
public interface SetXMLDocumentRoot extends XMLAction<XMLModelSlot, XMLIndividual> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String PARAMETER_KEY = "parameter";

	@Getter(value = PARAMETER_KEY)
	@XMLAttribute
	public DataBinding<Object> getParameter();

	@Setter(value = PARAMETER_KEY)
	public void setParameter(DataBinding<Object> param);

	public static abstract class SetXMLDocumentRootImpl extends TechnologySpecificActionImpl<XMLModelSlot, XMLIndividual> implements
			SetXMLDocumentRoot {

		private static final Logger logger = Logger.getLogger(SetXMLDocumentRoot.class.getPackage().getName());

		private DataBinding<Object> parameter;

		@Override
		public DataBinding<Object> getParameter() {

			if (parameter == null) {
				parameter = new DataBinding<Object>(this, Object.class, DataBinding.BindingDefinitionType.GET);
				parameter.setBindingName("parameter");
			}
			return parameter;

		}

		@Override
		public void setParameter(DataBinding<Object> paramIndivBinding) {

			if (paramIndivBinding != null) {
				paramIndivBinding.setOwner(this);
				paramIndivBinding.setDeclaredType(Object.class);
				paramIndivBinding.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				paramIndivBinding.setBindingName("parameter");
			}

			this.parameter = paramIndivBinding;

		}

		@Override
		public void notifiedBindingChanged(DataBinding<?> dataBinding) {
			if (dataBinding == getParameter()) {
			}
			super.notifiedBindingChanged(dataBinding);
		}

		@Override
		public XMLIndividual execute(FlexoBehaviourAction action) {

			ModelSlotInstance<XMLModelSlot, XMLModel> modelSlotInstance = (ModelSlotInstance<XMLModelSlot, XMLModel>) getModelSlotInstance(action);
			XMLModel model = modelSlotInstance.getAccessedResourceData();
			XMLModelSlot modelSlot = modelSlotInstance.getModelSlot();

			XMLIndividual rootIndiv = null;

			try {
				Object o = getParameter().getBindingValue(action);
				if (o instanceof XMLIndividual) {
					rootIndiv = (XMLIndividual) o;
				} else {
					logger.warning("Invalid value in Binding :" + getParameter().getUnparsedBinding());
				}
			} catch (TypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullReferenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (rootIndiv != null) {
				model.setRoot(rootIndiv);
			}

			return rootIndiv;
		}

		@Override
		public Type getAssignableType() {
			return XMLIndividual.class;
		}
	}
}
