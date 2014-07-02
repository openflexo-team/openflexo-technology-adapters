package org.openflexo.technologyadapter.freeplane.fml;

import org.openflexo.foundation.viewpoint.NavigationScheme;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.freeplane.fml.FreeplaneNavigationScheme.FreeplaneNavigationSchemeImpl;

@ModelEntity
@ImplementationClass(value = FreeplaneNavigationSchemeImpl.class)
public interface FreeplaneNavigationScheme extends NavigationScheme {

    public abstract class FreeplaneNavigationSchemeImpl extends NavigationSchemeImpl implements FreeplaneNavigationScheme {

        public FreeplaneNavigationSchemeImpl() {
            super();
        }
    }
}
