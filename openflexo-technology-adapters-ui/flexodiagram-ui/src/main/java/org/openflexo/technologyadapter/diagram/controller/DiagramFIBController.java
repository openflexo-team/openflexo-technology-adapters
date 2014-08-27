package org.openflexo.technologyadapter.diagram.controller;

import java.util.logging.Logger;

import org.openflexo.fib.model.FIBComponent;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.viewpoint.ActionScheme;
import org.openflexo.foundation.viewpoint.CloningScheme;
import org.openflexo.foundation.viewpoint.CreationScheme;
import org.openflexo.foundation.viewpoint.DeletionScheme;
import org.openflexo.foundation.viewpoint.FlexoBehaviour;
import org.openflexo.foundation.viewpoint.FlexoBehaviourObject;
import org.openflexo.foundation.viewpoint.FlexoBehaviourParameter;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoConceptConstraint;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.foundation.viewpoint.SynchronizationScheme;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.action.AddFlexoConcept;
import org.openflexo.foundation.viewpoint.action.CreateEditionAction;
import org.openflexo.foundation.viewpoint.action.CreateFlexoBehaviour;
import org.openflexo.foundation.viewpoint.action.CreateFlexoRole;
import org.openflexo.foundation.viewpoint.action.CreateModelSlot;
import org.openflexo.foundation.viewpoint.action.DeleteFlexoConcept;
import org.openflexo.foundation.viewpoint.action.DeleteVirtualModel;
import org.openflexo.foundation.viewpoint.action.DuplicateFlexoConcept;
import org.openflexo.foundation.viewpoint.editionaction.EditionAction;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.rm.Resource;
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
