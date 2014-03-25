#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.rm;

import org.openflexo.foundation.resource.FlexoFileResource;
import org.openflexo.foundation.technologyadapter.FlexoModelResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.${rootArtifactId}.${technologyPrefix}TechnologyAdapter;
import org.openflexo.technologyadapter.${rootArtifactId}.${technologyPrefix}TechnologyContextManager;
import org.openflexo.technologyadapter.${rootArtifactId}.model.${technologyPrefix}MetaModel;
import org.openflexo.technologyadapter.${rootArtifactId}.model.${technologyPrefix}Model;

@ModelEntity
@ImplementationClass(${technologyPrefix}ModelResourceImpl.class)
public abstract interface ${technologyPrefix}ModelResource extends FlexoFileResource<${technologyPrefix}Model>, FlexoModelResource<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ${technologyPrefix}TechnologyAdapter>
{
  public static final String TECHNOLOGY_CONTEXT_MANAGER = "technologyContextManager";

  @Getter(value="technologyContextManager", ignoreType=true)
  public abstract ${technologyPrefix}TechnologyContextManager getTechnologyContextManager();

  @Setter("technologyContextManager")
  public abstract void setTechnologyContextManager(${technologyPrefix}TechnologyContextManager param${technologyPrefix}TechnologyContextManager);
}

