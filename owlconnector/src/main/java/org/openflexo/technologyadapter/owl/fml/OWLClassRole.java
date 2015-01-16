package org.openflexo.technologyadapter.owl.fml;

import org.openflexo.foundation.fml.ClassRole;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLClass;

@ModelEntity
@ImplementationClass(OWLClassRole.OWLClassRoleImpl.class)
@XMLElement
@FML("OWLClassRole")
public interface OWLClassRole extends ClassRole<OWLClass> {

	public static abstract class OWLClassRoleImpl extends ClassRoleImpl<OWLClass> implements OWLClassRole {

		public OWLClassRoleImpl() {
			super();
		}

	}
}
