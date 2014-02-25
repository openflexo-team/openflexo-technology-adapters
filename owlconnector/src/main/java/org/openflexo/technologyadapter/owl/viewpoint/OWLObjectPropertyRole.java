package org.openflexo.technologyadapter.owl.viewpoint;

import org.openflexo.foundation.viewpoint.ObjectPropertyRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;

@ModelEntity
@ImplementationClass(OWLObjectPropertyRole.OWLObjectPropertyRoleImpl.class)
@XMLElement
public interface OWLObjectPropertyRole extends ObjectPropertyRole<OWLObjectProperty> {

	public static abstract class OWLObjectPropertyRoleImpl extends ObjectPropertyRoleImpl<OWLObjectProperty> implements
			OWLObjectPropertyRole {

		public OWLObjectPropertyRoleImpl() {
			super();
		}

	}
}
