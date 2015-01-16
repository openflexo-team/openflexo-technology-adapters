package org.openflexo.technologyadapter.diagram;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoBehaviours;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramNavigationScheme;
import org.openflexo.technologyadapter.diagram.fml.DiagramRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddDiagram;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.fml.editionaction.GraphicalAction;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;

/**
 * Implementation of the ModelSlot class for the Openflexo built-in diagram technology adapter<br>
 * 
 * We modelize here the access to a free {@link Diagram} (no conformance to any {@link DiagramSpecification}).
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ ShapeRole.class, ConnectorRole.class, DiagramRole.class })
@DeclareFlexoBehaviours({ DropScheme.class, LinkScheme.class, DiagramNavigationScheme.class })
@DeclareEditionActions({ AddDiagram.class, AddShape.class, AddConnector.class, GraphicalAction.class })
@DeclareFetchRequests({})
@ModelEntity
@ImplementationClass(FreeDiagramModelSlot.FreeDiagramModelSlotImpl.class)
@XMLElement
@FML("FreeDiagramModelSlot")
public interface FreeDiagramModelSlot extends FreeModelSlot<Diagram>, DiagramModelSlot {

	public abstract class FreeDiagramModelSlotImpl extends FreeModelSlotImpl<Diagram> implements FreeDiagramModelSlot {

		private static final Logger logger = Logger.getLogger(FreeDiagramModelSlot.class.getPackage().getName());

		@Override
		public String getStringRepresentation() {
			return "FreeDiagramModelSlot";
		}

		@Override
		public Class<DiagramTechnologyAdapter> getTechnologyAdapterClass() {
			return DiagramTechnologyAdapter.class;
		}

		@Override
		public DiagramTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (DiagramTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public boolean getIsRequired() {
			return true;
		}

		@Override
		public FreeDiagramModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new FreeDiagramModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public DiagramResource createProjectSpecificEmptyResource(View view, String filename, String modelUri) {
			return null;
		}

		@Override
		public DiagramResource createSharedEmptyResource(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri) {
			return null;
		}

		@Override
		public String getURIForObject(FreeModelSlotInstance<Diagram, ? extends FreeModelSlot<Diagram>> msInstance, Object o) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(FreeModelSlotInstance<Diagram, ? extends FreeModelSlot<Diagram>> msInstance, String objectURI) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Type getType() {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
