package org.openflexo.technologyadapter.excel.fml.editionaction;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.editionaction.TechnologySpecificAction;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.SemanticsExcelModelSlot;
import org.openflexo.technologyadapter.excel.model.semantics.BusinessConceptInstance;

@FIBPanel("Fib/AddBusinessConceptInstancePanel.fib")
@ModelEntity
@ImplementationClass(AddBusinessConceptInstance.AddBusinessConceptInstanceImpl.class)
@XMLElement
public interface AddBusinessConceptInstance extends TechnologySpecificAction<SemanticsExcelModelSlot, BusinessConceptInstance> {

	public static abstract class AddBusinessConceptInstanceImpl extends
			TechnologySpecificActionImpl<SemanticsExcelModelSlot, BusinessConceptInstance> implements AddBusinessConceptInstance {

		@Override
		public Type getAssignableType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BusinessConceptInstance execute(FlexoBehaviourAction action) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
