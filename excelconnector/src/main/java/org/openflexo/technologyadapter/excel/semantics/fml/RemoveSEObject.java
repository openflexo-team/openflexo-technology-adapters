/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.semantics.fml;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.DeletionScheme;
import org.openflexo.foundation.fml.editionaction.AssignableAction;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.semantics.model.SEDataArea;
import org.openflexo.technologyadapter.excel.semantics.model.SEFlexoConceptInstance;

/**
 * Used (typically in the {@link DeletionScheme} of a {@link SEFlexoConcept}) to remove instance from {@link SEDataArea} (will remove the
 * row from excel workbook)
 * 
 * @author sylvain
 *
 * @param <T>
 */
@ModelEntity
@ImplementationClass(RemoveSEObject.RemoveSEObjectImpl.class)
@XMLElement
public interface RemoveSEObject extends AssignableAction<SEFlexoConceptInstance> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String OBJECT_KEY = "object";
	@PropertyIdentifier(type = DataBinding.class)
	String DATA_AREA_KEY = "dataArea";

	@Getter(value = OBJECT_KEY)
	@XMLAttribute
	public DataBinding<SEFlexoConceptInstance> getObjectToRemove();

	@Setter(OBJECT_KEY)
	public void setObjectToRemove(DataBinding<SEFlexoConceptInstance> object);

	@Getter(value = DATA_AREA_KEY)
	@XMLAttribute
	public DataBinding<SEDataArea<?>> getDataArea();

	@Setter(DATA_AREA_KEY)
	public void setDataArea(DataBinding<SEDataArea<?>> dataArea);

	public static abstract class RemoveSEObjectImpl extends AssignableActionImpl<SEFlexoConceptInstance> implements RemoveSEObject {

		private static final Logger logger = Logger.getLogger(RemoveSEObject.class.getPackage().getName());

		private DataBinding<SEFlexoConceptInstance> objectToRemove;

		private DataBinding<SEDataArea<?>> dataArea;

		@Override
		public DataBinding<SEDataArea<?>> getDataArea() {
			if (dataArea == null) {
				dataArea = new DataBinding<>(this, SEDataArea.class, BindingDefinitionType.GET);
				dataArea.setBindingName("dataArea");
				dataArea.setMandatory(true);
			}
			return dataArea;
		}

		@Override
		public void setDataArea(DataBinding<SEDataArea<?>> dataArea) {
			if (dataArea != null) {
				dataArea.setOwner(this);
				dataArea.setBindingName("dataArea");
				dataArea.setDeclaredType(SEDataArea.class);
				dataArea.setBindingDefinitionType(BindingDefinitionType.GET);
				dataArea.setMandatory(true);
			}
			this.dataArea = dataArea;
		}

		@Override
		public DataBinding<SEFlexoConceptInstance> getObjectToRemove() {
			if (objectToRemove == null) {
				objectToRemove = new DataBinding<>(this, SEFlexoConceptInstance.class, BindingDefinitionType.GET);
				objectToRemove.setBindingName("objectToRemove");
			}
			return objectToRemove;
		}

		@Override
		public void setObjectToRemove(DataBinding<SEFlexoConceptInstance> objectToRemove) {
			if (objectToRemove != null) {
				objectToRemove.setOwner(this);
				objectToRemove.setBindingName("objectToRemove");
				objectToRemove.setDeclaredType(SEFlexoConceptInstance.class);
				objectToRemove.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.objectToRemove = objectToRemove;
			notifiedBindingChanged(objectToRemove);
		}

		@SuppressWarnings("unchecked")
		@Override
		public SEFlexoConceptInstance execute(RunTimeEvaluationContext evaluationContext) {

			SEFlexoConceptInstance objectToDelete = null;
			SEDataArea<SEFlexoConceptInstance> dataArea = null;
			try {
				objectToDelete = getObjectToRemove().getBindingValue(evaluationContext);
				dataArea = (SEDataArea<SEFlexoConceptInstance>) getDataArea().getBindingValue(evaluationContext);
			} catch (TypeMismatchException e1) {
				e1.printStackTrace();
			} catch (NullReferenceException e1) {
				e1.printStackTrace();
			} catch (ReflectiveOperationException e1) {
				e1.printStackTrace();
			}

			if (objectToDelete == null) {
				return null;
			}
			try {
				logger.info("Remove object " + objectToDelete + " from " + dataArea);
				if (dataArea != null) {
					dataArea.removeFlexoConceptInstance(objectToDelete);
				}
			} catch (Exception e) {
				logger.warning("Unexpected exception occured during deletion: " + e.getMessage());
				e.printStackTrace();
			}
			return objectToDelete;
		}

		@Override
		public Type getAssignableType() {
			return SEFlexoConceptInstance.class;
		}
	}

	@DefineValidationRule
	public static class ObjectToRemoveBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<RemoveSEObject> {
		public ObjectToRemoveBindingIsRequiredAndMustBeValid() {
			super("'object_to_delete'_binding_is_not_valid", RemoveSEObject.class);
		}

		@Override
		public DataBinding<?> getBinding(RemoveSEObject object) {
			return object.getObjectToRemove();
		}
	}

	@DefineValidationRule
	public static class DataAreaBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<RemoveSEObject> {
		public DataAreaBindingIsRequiredAndMustBeValid() {
			super("'data_area'_binding_is_not_valid", RemoveSEObject.class);
		}

		@Override
		public DataBinding<?> getBinding(RemoveSEObject object) {
			return object.getDataArea();
		}
	}

}
