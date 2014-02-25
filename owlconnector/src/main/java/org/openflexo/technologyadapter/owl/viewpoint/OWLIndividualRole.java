package org.openflexo.technologyadapter.owl.viewpoint;

import org.openflexo.foundation.viewpoint.IndividualRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;

@ModelEntity
@ImplementationClass(OWLIndividualRole.OWLIndividualRoleImpl.class)
@XMLElement
public interface OWLIndividualRole extends IndividualRole<OWLIndividual> {

	public static abstract class OWLIndividualRoleImpl extends IndividualRoleImpl<OWLIndividual> implements
			OWLIndividualRole {

		public OWLIndividualRoleImpl() {
			super();
		}

	}
}
