package org.openflexo.technologyadapter.freeplane.rm;

import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyContextManager;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

@ModelEntity
@ImplementationClass(value = FreeplaneResourceImpl.class)
public interface IFreeplaneResource extends TechnologyAdapterResource<IFreeplaneMap, FreeplaneTechnologyAdapter> {

    public static final String TECHNOLOGY_CONTEXT_MANAGER = "technologyContextManager";

    @Getter(value = TECHNOLOGY_CONTEXT_MANAGER, ignoreType = true)
    public FreeplaneTechnologyContextManager getTechnologyContextManager();

    @Setter(TECHNOLOGY_CONTEXT_MANAGER)
    public void setTechnologyContextManager(FreeplaneTechnologyContextManager paramFreeplaneTechnologyContextManager);

    public FileFlexoIODelegate getFileFlexoIODelegate();
}
