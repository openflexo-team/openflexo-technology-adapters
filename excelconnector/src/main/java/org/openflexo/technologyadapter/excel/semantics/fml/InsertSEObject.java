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

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.rt.FMLExecutionException;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.editionaction.AbstractAddFlexoConceptInstance;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.semantics.model.SEDataArea;
import org.openflexo.technologyadapter.excel.semantics.model.SEFlexoConceptInstance;
import org.openflexo.technologyadapter.excel.semantics.model.SEVirtualModelInstance;

/**
 * Insert a new object as a new row in excel workbook, according to a {@link SEDataAreaRole}
 * 
 * @author sylvain
 */
@ModelEntity
@ImplementationClass(InsertSEObject.InsertSEObjectImpl.class)
@XMLElement
public interface InsertSEObject extends AbstractAddFlexoConceptInstance<SEFlexoConceptInstance, SEVirtualModelInstance> {

	@PropertyIdentifier(type = DataBinding.class)
	String DATA_AREA_KEY = "dataArea";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String ROW_INDEX_KEY = "rowIndex";

	@Getter(value = DATA_AREA_KEY)
	@XMLAttribute
	public DataBinding<SEDataArea<?>> getDataArea();

	@Setter(DATA_AREA_KEY)
	public void setDataArea(DataBinding<SEDataArea<?>> dataArea);

	@Getter(value = ROW_INDEX_KEY)
	@XMLAttribute
	public DataBinding<Integer> getRowIndex();

	@Setter(ROW_INDEX_KEY)
	public void setRowIndex(DataBinding<Integer> rowIndex);

	public static abstract class InsertSEObjectImpl
			extends AbstractAddFlexoConceptInstanceImpl<SEFlexoConceptInstance, SEVirtualModelInstance> implements InsertSEObject {

		private static final Logger logger = Logger.getLogger(InsertSEObject.class.getPackage().getName());

		private DataBinding<SEDataArea<?>> dataArea;
		private DataBinding<Integer> rowIndex;

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
		protected Class<? extends FlexoConcept> getDynamicFlexoConceptTypeType() {
			return FlexoConcept.class;
		}

		@Override
		public FlexoConcept getFlexoConceptType() {
			if (getDataArea().isValid()) {
				Type itemType = TypeUtils.getTypeArgument(getDataArea().getAnalyzedType(), SEDataArea.class, 0);
				if (itemType instanceof FlexoConceptInstanceType) {
					return ((FlexoConceptInstanceType) itemType).getFlexoConcept();
				}
			}
			return super.getFlexoConceptType();
		}

		@Override
		public void notifiedBindingChanged(DataBinding<?> dataBinding) {
			super.notifiedBindingChanged(dataBinding);
			if (dataBinding == dataArea) {
				getPropertyChangeSupport().firePropertyChange(FLEXO_CONCEPT_TYPE_KEY, null, getFlexoConceptType());
				getPropertyChangeSupport().firePropertyChange("availableCreationSchemes", null, getAvailableCreationSchemes());
			}
		}

		@Override
		public DataBinding<Integer> getRowIndex() {
			if (rowIndex == null) {
				rowIndex = new DataBinding<>(this, Integer.class, DataBinding.BindingDefinitionType.GET);
				rowIndex.setBindingName("rowIndex");
			}
			return rowIndex;
		}

		@Override
		public void setRowIndex(DataBinding<Integer> rowIndex) {
			if (rowIndex != null) {
				rowIndex.setOwner(this);
				rowIndex.setDeclaredType(Integer.class);
				rowIndex.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				rowIndex.setBindingName("rowIndex");
			}
			this.rowIndex = rowIndex;
		}

		public SEDataArea<?> getDataArea(BindingEvaluationContext evaluationContext) {
			if (getDataArea().isValid()) {
				try {
					return getDataArea().getBindingValue(evaluationContext);
				} catch (TypeMismatchException e) {
					e.printStackTrace();
				} catch (NullReferenceException e) {
					e.printStackTrace();
				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		public Integer getRowIndex(BindingEvaluationContext evaluationContext) {
			if (getRowIndex().isValid()) {
				try {
					return getRowIndex().getBindingValue(evaluationContext);
				} catch (TypeMismatchException e) {
					e.printStackTrace();
				} catch (NullReferenceException e) {
					e.printStackTrace();
				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public SEFlexoConceptInstance execute(RunTimeEvaluationContext evaluationContext) throws FMLExecutionException {
			SEVirtualModelInstance vmi = getVirtualModelInstance(evaluationContext);

			// System.out.println("InsertSEObject for receiver " + getReceiver() + " = " + vmi + " concept=" + getFlexoConceptType());

			SEFlexoConceptInstance returned = super.execute(evaluationContext);

			SEDataArea<?> dataArea = getDataArea(evaluationContext);
			vmi.getPropertyChangeSupport().firePropertyChange(dataArea.getDataAreaRole().getName(), null, dataArea);

			return returned;

		}

		@Override
		protected SEFlexoConceptInstance makeNewFlexoConceptInstance(RunTimeEvaluationContext evaluationContext)
				throws FMLExecutionException {

			SEDataArea<?> dataArea = getDataArea(evaluationContext);
			Integer index = getRowIndex(evaluationContext);

			return dataArea.insertFlexoConceptInstanceAtIndex(index);

			/*int insertedRowIndex;
			if (index != null && index >= 0) {
				insertedRowIndex = dataArea.getCellRange().getTopLeftCell().getRowIndex() + index;
			}
			else {
				// Last row
				insertedRowIndex = dataArea.getCellRange().getBottomRightCell().getRowIndex() + 1;
			}
			
			ExcelSheet sheet = dataArea.getCellRange().getExcelSheet();
			ExcelRow excelRow = sheet.insertRowAt(insertedRowIndex);
			
			int startColIndex = dataArea.getCellRange().getTopLeftCell().getColumnIndex();
			int endColIndex = dataArea.getCellRange().getBottomRightCell().getColumnIndex();
			for (int i = startColIndex; i <= endColIndex; i++) {
				ExcelCell cell = excelRow.createCellAt(i);
			}
			
			System.out.println("La row qu'on vient de creer c'est: " + excelRow);
			System.out.println("avec " + excelRow.getRow());
			
			FlexoConceptInstance container = null;
			SEVirtualModelInstance vmi = getVirtualModelInstance(evaluationContext);
			
			if (getFlexoConceptType().getContainerFlexoConcept() != null) {
				container = getContainer(evaluationContext);
				if (container == null) {
					logger.warning("null container while creating new concept " + getFlexoConceptType());
					return null;
				}
			}
			
			System.out.println("Hop, on vient construire le SEFlexoConceptInstance");
			SEFlexoConceptInstance returned = vmi.getFlexoConceptInstance(excelRow.getRow(), container, dataArea.getDataAreaRole());
			
			return returned;*/
		}

		@Override
		public TechnologyAdapter getModelSlotTechnologyAdapter() {
			if (getServiceManager() != null) {
				return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(ExcelTechnologyAdapter.class);
			}
			return super.getModelSlotTechnologyAdapter();
		}

		@Override
		public Class<SEVirtualModelInstance> getVirtualModelInstanceClass() {
			return SEVirtualModelInstance.class;
		}
	}

	@DefineValidationRule
	public static class DataAreaBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<InsertSEObject> {
		public DataAreaBindingIsRequiredAndMustBeValid() {
			super("'data_area'_binding_is_not_valid", InsertSEObject.class);
		}

		@Override
		public DataBinding<?> getBinding(InsertSEObject object) {
			return object.getDataArea();
		}

	}

}
