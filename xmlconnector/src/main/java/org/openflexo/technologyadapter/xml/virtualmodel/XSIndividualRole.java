package org.openflexo.technologyadapter.xml.virtualmodel;

import org.openflexo.foundation.viewpoint.IndividualRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.model.XSOntIndividual;

@ModelEntity
@ImplementationClass(XSIndividualRole.XSIndividualRoleImpl.class)
@XMLElement
public interface XSIndividualRole extends IndividualRole<XSOntIndividual> {

	public static abstract class XSIndividualRoleImpl extends IndividualRoleImpl<XSOntIndividual> implements
			XSIndividualRole {

		public XSIndividualRoleImpl() {
			super();
		}

	}
}
