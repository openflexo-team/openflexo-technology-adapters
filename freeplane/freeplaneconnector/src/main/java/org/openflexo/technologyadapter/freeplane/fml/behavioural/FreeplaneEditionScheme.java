package org.openflexo.technologyadapter.freeplane.fml.behavioural;

import org.openflexo.foundation.viewpoint.TechnologySpecificEditionScheme;
import org.openflexo.model.annotations.Implementation;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.fml.behavioural.FreeplaneEditionScheme.FreeplaneBehaviourSchemeImpl;

@ModelEntity(isAbstract = true)
@ImplementationClass(value = FreeplaneBehaviourSchemeImpl.class)
public interface FreeplaneEditionScheme extends TechnologySpecificEditionScheme {

	public static final String PARENT_NODE = "parentNode";

	@Implementation
	public abstract class FreeplaneBehaviourSchemeImpl implements FreeplaneEditionScheme {

		@Override
		public FreeplaneTechnologyAdapter getTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);
		}
    }
}
