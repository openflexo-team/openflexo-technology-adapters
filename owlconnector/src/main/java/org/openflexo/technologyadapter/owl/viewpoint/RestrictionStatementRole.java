package org.openflexo.technologyadapter.owl.viewpoint;

import java.lang.reflect.Type;

import org.openflexo.foundation.view.ActorReference;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;

// TODO: Rewrite this
@Deprecated
@ModelEntity
@ImplementationClass(RestrictionStatementRole.RestrictionStatementRoleImpl.class)
@XMLElement
public interface RestrictionStatementRole extends StatementRole {

	public static abstract class RestrictionStatementRoleImpl extends StatementRoleImpl implements
			RestrictionStatementRole {

		public RestrictionStatementRoleImpl() {
			super();
		}

		@Override
		public Type getType() {
			return null;
		}

		@Override
		public String getPreciseType() {
			return FlexoLocalization.localizedForKey("restriction_statement");
		}

		@Override
		public ActorReference makeActorReference(Object object, FlexoConceptInstance epi) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
