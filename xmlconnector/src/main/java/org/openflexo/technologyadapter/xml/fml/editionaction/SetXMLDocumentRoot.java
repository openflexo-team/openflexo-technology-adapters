/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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
