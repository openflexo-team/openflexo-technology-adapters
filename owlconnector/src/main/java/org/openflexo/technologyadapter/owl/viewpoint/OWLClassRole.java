package org.openflexo.technologyadapter.owl.viewpoint;

import org.openflexo.foundation.viewpoint.ClassRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLClass;

@ModelEntity
@ImplementationClass(OWLClassRole.OWLClassRoleImpl.class)
@XMLElement
public interface OWLClassRole extends ClassRole<OWLClass> {

	public static abstract class OWLClassRoleImpl extends ClassRoleImpl<OWLClass> implements OWLClassRole {

		public OWLClassRoleImpl() {
			super();
		}

	}
}
