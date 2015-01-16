package org.openflexo.technologyadapter.owl.fml;

import org.openflexo.foundation.fml.OntologicObjectRole;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.owl.model.OWLStatement;

@ModelEntity(isAbstract = true)
@ImplementationClass(StatementRole.StatementRoleImpl.class)
@FML("StatementRole")
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
