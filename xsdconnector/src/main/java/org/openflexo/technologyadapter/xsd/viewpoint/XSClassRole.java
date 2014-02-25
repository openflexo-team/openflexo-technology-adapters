package org.openflexo.technologyadapter.xsd.viewpoint;

import org.openflexo.foundation.viewpoint.ClassRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xsd.metamodel.XSOntClass;

@ModelEntity
@ImplementationClass(XSClassRole.XSClassRoleImpl.class)
@XMLElement
public interface XSClassRole extends ClassRole<XSOntClass> {

	public static abstract class XSClassRoleImpl extends ClassRoleImpl<XSOntClass> implements XSClassRole {

		public XSClassRoleImpl() {
			super();
		}

	}
}
