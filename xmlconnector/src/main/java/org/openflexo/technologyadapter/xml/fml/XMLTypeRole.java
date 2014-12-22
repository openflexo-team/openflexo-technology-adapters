package org.openflexo.technologyadapter.xml.fml;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;

@ModelEntity
@ImplementationClass(XMLTypeRole.XMLTypeRoleImpl.class)
@XMLElement
public interface XMLTypeRole extends FlexoRole<XMLType>,TechnologyObject<XMLTechnologyAdapter> {

	public static abstract class XMLTypeRoleImpl extends FlexoRoleImpl<XMLType> implements XMLTypeRole {

		public XMLTypeRoleImpl() {
			super();
		}

	}
}
