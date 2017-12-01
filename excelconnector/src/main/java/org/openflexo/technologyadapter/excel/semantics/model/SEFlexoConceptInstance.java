/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
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
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
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

package org.openflexo.technologyadapter.excel.semantics.model;

import java.util.Date;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstanceModelFactory;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.semantics.fml.SEColumnRole;

/**
 * A Excel-specific {@link FlexoConceptInstance} reflecting a distant object (represented by a row in a workbook) accessible in an
 * {@link SEVirtualModelInstance} through a {@link HbnModelSlot}<br>
 * 
 */
@ModelEntity
@ImplementationClass(SEFlexoConceptInstance.SEFlexoConceptInstanceImpl.class)
@XMLElement
public interface SEFlexoConceptInstance extends FlexoConceptInstance {

	/**
	 * Initialize this {@link SEFlexoConceptInstance} with supplied Hibernate support object, and explicit concept (type)
	 * 
	 * @param hbnMap
	 * @param concept
	 */
	@Initializer
	void initialize(FlexoConcept concept);

	/**
	 * Return {@link Row} support object
	 * 
	 * @return
	 */
	public Row getRowSupportObject();

	/**
	 * Sets {@link Row} support object
	 * 
	 * @return
	 */
	public void setRowSupportObject(Row row);

	/**
	 * Default implementation for {@link SEFlexoConceptInstance}
	 * 
	 * @author sylvain
	 *
	 */
	abstract class SEFlexoConceptInstanceImpl extends FlexoConceptInstanceImpl implements SEFlexoConceptInstance {

		private static final Logger logger = FlexoLogger.getLogger(SEFlexoConceptInstance.class.getPackage().toString());

		// Row support object
		private Row row;

		/**
		 * Initialize this {@link SEFlexoConceptInstance} with supplied Hibernate support object, and explicit concept (type)
		 * 
		 * @param hbnMap
		 * @param concept
		 */
		@Override
		public void initialize(FlexoConcept concept) {
			setFlexoConcept(concept);
		}

		@Override
		public SEVirtualModelInstance getVirtualModelInstance() {
			return (SEVirtualModelInstance) super.getVirtualModelInstance();
		}

		@Override
		public Row getRowSupportObject() {
			return row;
		}

		@Override
		public void setRowSupportObject(Row row) {

			if ((row == null && this.row != null) || (row != null && !row.equals(this.row))) {
				Row oldValue = this.row;
				this.row = row;
				getPropertyChangeSupport().firePropertyChange("rowSupportObject", oldValue, row);
			}
		}

		@Override
		public <T> T getFlexoActor(FlexoRole<T> flexoRole) {
			if (flexoRole instanceof SEColumnRole) {
				SEColumnRole<T> columnRole = (SEColumnRole<T>) flexoRole;
				Cell cell = row.getCell(columnRole.getColumnIndex());
				// System.out.println("cell: " + cell);
				switch (columnRole.getPrimitiveType()) {
					case String:
						return (T) cell.getStringCellValue();
					case Long:
					case Integer:
					case Double:
					case Float:
						return (T) TypeUtils.castTo(cell.getNumericCellValue(), columnRole.getPrimitiveType().getType());
					case Date:
						return (T) cell.getDateCellValue();
					case Boolean:
						return (T) (Boolean) cell.getBooleanCellValue();
					default:
						logger.warning("Unexpected primitive type: " + columnRole.getPrimitiveType());
						return null;
				}
			}
			return super.getFlexoActor(flexoRole);
		}

		@Override
		public <T> void setFlexoActor(T object, FlexoRole<T> flexoRole) {
			if (flexoRole instanceof SEColumnRole) {
				SEColumnRole<T> columnRole = (SEColumnRole<T>) flexoRole;
				Cell cell = row.getCell(columnRole.getColumnIndex());
				// System.out.println("cell: " + cell);
				switch (columnRole.getPrimitiveType()) {
					case String:
						cell.setCellValue((String) object);
						break;
					case Long:
						cell.setCellValue((Long) object);
						break;
					case Integer:
						cell.setCellValue((Integer) object);
						break;
					case Double:
						cell.setCellValue((Double) object);
						break;
					case Float:
						cell.setCellValue((Float) object);
						break;
					case Date:
						cell.setCellValue((Date) object);
						break;
					case Boolean:
						cell.setCellValue((Boolean) object);
						break;
					default:
						logger.warning("Unexpected primitive type: " + columnRole.getPrimitiveType());
						break;
				}
			}
			else {
				super.setFlexoActor(object, flexoRole);
			}
		}

		@Override
		public SEObjectActorReference makeActorReference(FlexoConceptInstanceRole role, FlexoConceptInstance fci) {
			AbstractVirtualModelInstanceModelFactory<?> factory = getFactory();
			SEObjectActorReference returned = factory.newInstance(SEObjectActorReference.class);
			returned.setFlexoRole(role);
			returned.setFlexoConceptInstance(fci);
			returned.setModellingElement(this);
			return returned;
		}

	}
}
