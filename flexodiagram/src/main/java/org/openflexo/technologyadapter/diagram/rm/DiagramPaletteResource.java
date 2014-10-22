package org.openflexo.technologyadapter.diagram.rm;

import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.PamelaResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;

@ModelEntity
@ImplementationClass(DiagramPaletteResourceImpl.class)
@XMLElement
public interface DiagramPaletteResource extends PamelaResource<DiagramPalette, DiagramPaletteFactory> ,
TechnologyAdapterResource<DiagramPalette, DiagramTechnologyAdapter>{

	/**
	 * Return diagram palette stored by this resource<br>
	 * Load the resource data when unloaded
	 */
	public DiagramPalette getDiagramPalette();

	/**
	 * Return diagram palette stored by this resource<br>
	 * Do not force load the resource data
	 * 
	 * @return
	 */
	public DiagramPalette getLoadedDiagramPalette();

	@Override
	public DiagramSpecificationResource getContainer();
	
	public static final String DIAGRAM_PALETTE_SUFFIX = ".palette";

}
