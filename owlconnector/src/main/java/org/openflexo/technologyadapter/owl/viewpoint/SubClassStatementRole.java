package org.openflexo.technologyadapter.owl.viewpoint;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstanceModelFactory;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.SubClassStatement;

@ModelEntity
@ImplementationClass(SubClassStatementRole.SubClassStatementRoleImpl.class)
@XMLElement
public interface SubClassStatementRole extends StatementRole<SubClassStatement> {

	public static abstract class SubClassStatementRoleImpl extends StatementRoleImpl<SubClassStatement> implements
			SubClassStatementRole {

		static final Logger logger = FlexoLogger.getLogger(SubClassStatementRole.class.getPackage().toString());

		public SubClassStatementRoleImpl() {
			super();
		}

		@Override
		public Type getType() {
			return null;
		}

		@Override
		public String getPreciseType() {
			return FlexoLocalization.localizedForKey("sub_class_statement");
		}

		@Override
		public SubClassStatementActorReference makeActorReference(SubClassStatement object, FlexoConceptInstance epi) {
			VirtualModelInstanceModelFactory factory = epi.getFactory();
			SubClassStatementActorReference returned = factory.newInstance(SubClassStatementActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(epi);
			returned.setModellingElement(object);
			return returned;
		}
	}
}
