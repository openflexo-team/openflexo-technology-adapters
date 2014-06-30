package org.openflexo.technologyadapter.freeplane.model.impl;

import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public abstract class FreeplaneMapImpl extends FlexoObjectImpl implements IFreeplaneMap {

    private FreeplaneTechnologyAdapter technologyAdapter;

    public FreeplaneMapImpl(final FreeplaneTechnologyAdapter technologyAdapter) {
        this.technologyAdapter = technologyAdapter;
    }

    public FreeplaneMapImpl() {
    }

    public void setTechnologyAdapter(final FreeplaneTechnologyAdapter technologyAdapter) {
        this.technologyAdapter = technologyAdapter;
    }

    /* (non-Javadoc)
     * @see org.openflexo.foundation.technologyadapter.TechnologyObject#getTechnologyAdapter()
     */
    @Override
    public FreeplaneTechnologyAdapter getTechnologyAdapter() {
        return this.technologyAdapter;
    }

}
