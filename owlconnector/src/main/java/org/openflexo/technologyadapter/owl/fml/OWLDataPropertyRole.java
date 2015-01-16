package org.openflexo.technologyadapter.owl.fml;

import org.openflexo.foundation.fml.DataPropertyRole;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;

@ModelEntity
@ImplementationClass(OWLDataPropertyRole.OWLDataPropertyRoleImpl.class)
@XMLElement
@FML("OWLDataPropertyRole")
public interface OWLDataPropertyRole extends DataPropertyRole<OWLDataProperty> {

	public static abstract class OWLDataPropertyRoleImpl extends DataPropertyRoleImpl<OWLDataProperty> implements OWLDataPropertyRole {

		public OWLDataPropertyRoleImpl() {
			super();
		}

	}
}
