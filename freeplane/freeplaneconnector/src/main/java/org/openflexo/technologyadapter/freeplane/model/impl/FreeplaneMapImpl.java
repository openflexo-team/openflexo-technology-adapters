package org.openflexo.technologyadapter.freeplane.model.impl;

import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public abstract class FreeplaneMapImpl implements IFreeplaneMap {

    private FreeplaneTechnologyAdapter technologyAdapter;

    public FreeplaneMapImpl(final FreeplaneTechnologyAdapter technologyAdapter) {
        this.technologyAdapter = technologyAdapter;
    }

    public FreeplaneMapImpl() {
    }

    public void setTechnologyAdapter(final FreeplaneTechnologyAdapter technologyAdapter) {
        this.technologyAdapter = technologyAdapter;
    }

    @Override
    public FreeplaneTechnologyAdapter getTechnologyAdapter() {
        return this.technologyAdapter;
    }
}
