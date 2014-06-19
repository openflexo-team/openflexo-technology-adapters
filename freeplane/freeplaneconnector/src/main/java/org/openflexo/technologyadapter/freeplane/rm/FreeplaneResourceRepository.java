package org.openflexo.technologyadapter.freeplane.rm;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterFileResourceRepository;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public class FreeplaneResourceRepository extends TechnologyAdapterFileResourceRepository<IFreeplaneResource, FreeplaneTechnologyAdapter, IFreeplaneMap> {

    private static final String DEFAULT_BASE_URI = "http://www.openflexo.org/FreeplaneTechnologyAdapter/Maps";

    public FreeplaneResourceRepository(final FreeplaneTechnologyAdapter technologyAdapter, final FlexoResourceCenter<?> resourceCenter) {
        super(technologyAdapter, resourceCenter);
    }

    @Override
    public String getDefaultBaseURI() {
        return DEFAULT_BASE_URI;
    }

}
