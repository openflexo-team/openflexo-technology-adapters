package org.openflexo.technologyadapter.diagram;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;

public class FreeDiagramModelSlotInstanceConfiguration extends FreeModelSlotInstanceConfiguration<Diagram, FreeDiagramModelSlot> {

	private static final Logger logger = Logger.getLogger(ModelSlotInstanceConfiguration.class.getPackage().getName());

	protected List<ModelSlotInstanceConfigurationOption> options;

	protected FlexoResourceCenter<?> resourceCenter;
	protected DiagramResource diagramResource;

	protected FreeDiagramModelSlotInstanceConfiguration(FreeDiagramModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
		setResourceUri(getAction().getFocusedObject().getProject().getURI() + "/Diagrams/myDiagram");
		setRelativePath("/");
		setFilename("myDiagram.diagram");
	}

	@Override
	protected boolean checkValidFileName() {
		if (!super.checkValidFileName()) {
			return false;
		}
		if (!getFilename().endsWith(DiagramResource.DIAGRAM_SUFFIX)) {
			setErrorMessage(FlexoLocalization.localizedForKey("file_name_should_end_with_.diagram_suffix"));
			return false;
		}
		return true;
	}

}
