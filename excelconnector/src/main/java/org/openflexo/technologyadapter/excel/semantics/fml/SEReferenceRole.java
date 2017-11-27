/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.excel.semantics.fml;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.semantics.model.SEFlexoConceptInstance;

/**
 * A role specific to Semantics/Excel technology (SEModelSlot) allowing to access a referenced object somewhere in an excel workbook, and
 * reflected as a {@link SEFlexoConceptInstance}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(SEReferenceRole.HbnToOneReferenceRoleImpl.class)
@XMLElement
public interface SEReferenceRole extends FlexoConceptInstanceRole {

	@PropertyIdentifier(type = Integer.class)
	String COLUMN_INDEX_KEY = "columnIndex";

	@Getter(COLUMN_INDEX_KEY)
	@XMLAttribute
	public Integer getColumnIndex();

	@Setter(COLUMN_INDEX_KEY)
	public void setColumnIndex(Integer columnIndex);

	/*@PropertyIdentifier(type = String.class)
	String COLUMN_NAME_KEY = "columnName";
	@PropertyIdentifier(type = String.class)
	String FOREIGN_KEY_ATTRIBUTE_NAME_KEY = "foreignKeyAttributeName";
	
	@Getter(COLUMN_NAME_KEY)
	@XMLAttribute
	public String getColumnName();
	
	@Setter(COLUMN_NAME_KEY)
	public void setColumnName(String columnName);
	
	@Getter(FOREIGN_KEY_ATTRIBUTE_NAME_KEY)
	@XMLAttribute
	String getForeignKeyAttributeName();
	
	@Setter(FOREIGN_KEY_ATTRIBUTE_NAME_KEY)
	void setForeignKeyAttributeName(String foreignKeyAttributeName);*/

	public static abstract class HbnToOneReferenceRoleImpl extends FlexoConceptInstanceRoleImpl implements SEReferenceRole {

		private static final Logger logger = Logger.getLogger(HbnToOneReferenceRoleImpl.class.getPackage().getName());

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return ExcelTechnologyAdapter.class;
		}

		/**
		 * Return a boolean indicating if this {@link FlexoRole} handles itself instantiation and management of related ActorReference
		 * 
		 * @return
		 */
		/*@Override
		public boolean supportSelfInstantiation() {
			return true;
		}*/

		/**
		 * Performs self instantiation
		 * 
		 * @param fci
		 * @return
		 */
		/*@Override
		public List<? extends ActorReference<? extends FlexoConceptInstance>> selfInstantiate(FlexoConceptInstance fci) {
			try {
				String url = getUrl().getBindingValue(fci);
				logger.info("Executing REST request " + url);
			
				if (fci.getVirtualModelInstance() instanceof RestVirtualModelInstance) {
					RestVirtualModelInstance vmi = (RestVirtualModelInstance) fci.getVirtualModelInstance();
					FlexoConceptInstance container = vmi;
					if (getContainer().isSet() && getContainer().isValid()) {
						Object object = getContainer().getBindingValue(fci);
						if (object instanceof FlexoConceptInstance) {
							container = (FlexoConceptInstance) object;
						}
					}
					List<RestFlexoConceptInstance> flexoConceptInstances = vmi.getFlexoConceptInstances(url, getPointer(), container,
							getFlexoConceptType());
					List returned = new ArrayList<>();
					for (RestFlexoConceptInstance target : flexoConceptInstances) {
						returned.add(makeActorReference(target, fci));
					}
					return returned;
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
		
			return super.selfInstantiate(fci);
		}*/

	}
}
