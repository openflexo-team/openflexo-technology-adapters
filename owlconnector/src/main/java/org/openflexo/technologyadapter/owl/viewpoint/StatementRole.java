package org.openflexo.technologyadapter.owl.viewpoint;

import org.openflexo.foundation.viewpoint.OntologicObjectRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.owl.model.OWLStatement;

@ModelEntity(isAbstract = true)
@ImplementationClass(StatementRole.StatementRoleImpl.class)
public abstract interface StatementRole<T extends OWLStatement> extends OntologicObjectRole<T> {

	public static abstract class StatementRoleImpl<T extends OWLStatement> extends OntologicObjectRoleImpl<T> implements StatementRole<T> {

		/**
		 * Encodes the default cloning strategy
		 * 
		 * @return
		 */
		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Clone;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return true;
		}

	}
}
