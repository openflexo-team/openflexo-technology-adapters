package org.openflexo.technologyadapter.diagram.controller;

import java.util.logging.Logger;

import org.openflexo.fib.model.FIBComponent;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateExampleDiagram;
import org.openflexo.technologyadapter.diagram.fml.action.DeleteDiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.action.DeleteDiagram;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;

public class DiagramFIBController extends FlexoFIBController {

	protected static final Logger logger = FlexoLogger.getLogger(DiagramFIBController.class.getPackage().getName());

	public DiagramFIBController(FIBComponent component) {
		super(component);
	}

	public DiagramFIBController(FIBComponent component, FlexoController controller) {
		super(component, controller);
	}
	
	public Diagram createExampleDiagram(DiagramSpecification diagramSpecification) {
		CreateExampleDiagram createExampleDiagram = CreateExampleDiagram.actionType.makeNewAction(diagramSpecification, null, getEditor());
		createExampleDiagram.doAction();
		return createExampleDiagram.getNewDiagram();
	}

	public void deleteExampleDiagram(Diagram diagram) {
		DeleteDiagram deleteDiagram = DeleteDiagram.actionType.makeNewAction(diagram, null, getEditor());
		deleteDiagram.doAction();
	}
	
	public DiagramPalette createDiagramPalette(DiagramSpecification diagramSpecification) {
		CreateDiagramPalette createDiagramPalette = CreateDiagramPalette.actionType.makeNewAction(diagramSpecification, null, getEditor());
		createDiagramPalette.doAction();
		return createDiagramPalette.getNewPalette();
	}

	public void deleteDiagramPalette(DiagramPalette diagramPalette) {
		DeleteDiagramPalette deleteDiagramPalette = DeleteDiagramPalette.actionType.makeNewAction(diagramPalette, null, getEditor());
		deleteDiagramPalette.doAction();
	}
}
