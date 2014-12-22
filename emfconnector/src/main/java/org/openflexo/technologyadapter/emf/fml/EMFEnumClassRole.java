package org.openflexo.technologyadapter.emf.fml;

import org.openflexo.foundation.fml.ClassRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.metamodel.EMFEnumClass;

@ModelEntity
@ImplementationClass(EMFEnumClassRole.EMFEnumClassRoleImpl.class)
@XMLElement
public interface EMFEnumClassRole extends ClassRole<EMFEnumClass> {

	public static abstract class EMFEnumClassRoleImpl extends ClassRoleImpl<EMFEnumClass> implements EMFEnumClassRole {

		public EMFEnumClassRoleImpl() {
			super();
		}

	}
}
