package org.openflexo.technologyadapter.xml.fml;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;

@ModelEntity
@ImplementationClass(XMLTypeRole.XMLTypeRoleImpl.class)
@XMLElement
public interface XMLTypeRole extends FlexoRole<XMLType> {

	public static abstract class XMLTypeRoleImpl extends FlexoRoleImpl<XMLType> implements XMLTypeRole {

		public XMLTypeRoleImpl() {
			super();
		}

		@Override
		public Type getType() {
			return XMLType.class;
		}

		@Override
		public String getPreciseType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public org.openflexo.foundation.fml.FlexoRole.RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Ignore;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return false;
		}

		@Override
		public ActorReference<XMLType> makeActorReference(XMLType object, FlexoConceptInstance epi) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
