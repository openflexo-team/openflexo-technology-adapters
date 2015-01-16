/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.excel.fml;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceModelFactory;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.model.ExcelRow;

@ModelEntity
@ImplementationClass(ExcelRowRole.ExcelRowRoleImpl.class)
@XMLElement
@FML("ExcelRowRole")
public interface ExcelRowRole extends FlexoRole<ExcelRow> {

	public static abstract class ExcelRowRoleImpl extends FlexoRoleImpl<ExcelRow> implements ExcelRowRole {

		public ExcelRowRoleImpl() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public Type getType() {
			return ExcelRow.class;
		}

		@Override
		public String getPreciseType() {
			return ExcelRow.class.getSimpleName();
		}

		/*@Override
		public boolean getIsPrimaryRole() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setIsPrimaryRole(boolean isPrimary) {
			// TODO Auto-generated method stub

		}*/

		/**
		 * Encodes the default cloning strategy
		 * 
		 * @return
		 */
		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Reference;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public ActorReference<ExcelRow> makeActorReference(ExcelRow object, FlexoConceptInstance epi) {
			VirtualModelInstanceModelFactory factory = epi.getFactory();
			ExcelActorReference<ExcelRow> returned = factory.newInstance(ExcelActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(epi);
			returned.setModellingElement(object);
			return returned;
		}

	}
}
