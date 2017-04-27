/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.diagram;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoBehaviours;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramNavigationScheme;
import org.openflexo.technologyadapter.diagram.fml.DiagramRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.fml.editionaction.CreateDiagram;
import org.openflexo.technologyadapter.diagram.fml.editionaction.GraphicalAction;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramType;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResourceFactory;

/**
 * Implementation of the ModelSlot class for the Openflexo built-in diagram technology adapter<br>
 * 
 * We modelize here the access to a {@link Diagram} conform to a given {@link DiagramSpecification}.
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ ShapeRole.class, ConnectorRole.class, DiagramRole.class })
@DeclareFlexoBehaviours({ DropScheme.class, LinkScheme.class, DiagramNavigationScheme.class })
@DeclareEditionActions({ CreateDiagram.class, AddShape.class, AddConnector.class, GraphicalAction.class })
@DeclareFetchRequests({})
@ModelEntity
@ImplementationClass(TypedDiagramModelSlot.TypedDiagramModelSlotImpl.class)
@XMLElement
@FML("TypedDiagramModelSlot")
public interface TypedDiagramModelSlot extends TypeAwareModelSlot<Diagram, DiagramSpecification>, DiagramModelSlot {

	@PropertyIdentifier(type = DrawingGraphicalRepresentation.class)
	public static final String GRAPHICAL_REPRESENTATION_KEY = "graphicalRepresentation";

	@PropertyIdentifier(type = List.class)
	public static final String PALETTE_ELEMENTS_BINDING_KEY = "paletteElementBindings";

	@Getter(value = GRAPHICAL_REPRESENTATION_KEY)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	@XMLElement
	public DrawingGraphicalRepresentation getGraphicalRepresentation();

	@Setter(GRAPHICAL_REPRESENTATION_KEY)
	public void setGraphicalRepresentation(DrawingGraphicalRepresentation graphicalRepresentation);

	@Getter(
			value = PALETTE_ELEMENTS_BINDING_KEY,
			cardinality = Cardinality.LIST,
			inverse = FMLDiagramPaletteElementBinding.DIAGRAM_MODEL_SLOT_KEY)
	@XMLElement
	public List<FMLDiagramPaletteElementBinding> getPaletteElementBindings();

	@Setter(PALETTE_ELEMENTS_BINDING_KEY)
	public void setPaletteElementBindings(List<FMLDiagramPaletteElementBinding> paletteElementBindings);

	@Adder(PALETTE_ELEMENTS_BINDING_KEY)
	public void addToPaletteElementBindings(FMLDiagramPaletteElementBinding paletteElementBinding);

	@Remover(PALETTE_ELEMENTS_BINDING_KEY)
	public void removeFromPaletteElementBindings(FMLDiagramPaletteElementBinding paletteElementBinding);

	public FMLDiagramPaletteElementBinding getPaletteElementBinding(DiagramPaletteElement paletteElement);

	public List<FMLDiagramPaletteElementBinding> getPaletteElementBindings(DiagramPaletteElement paletteElement);

	public FMLDiagramPaletteElementBinding addFMLDiagramPaletteElementBinding();

	public DiagramSpecification getDiagramSpecification();

	public static abstract class TypedDiagramModelSlotImpl extends TypeAwareModelSlotImpl<Diagram, DiagramSpecification>
			implements TypedDiagramModelSlot {

		private static final Logger logger = Logger.getLogger(TypedDiagramModelSlot.class.getPackage().getName());

		// private List<FMLDiagramPaletteElementBinding> paletteElementBindings;

		/*
		 * public TypedDiagramModelSlotImpl(VirtualModel virtualModel,
		 * DiagramSpecification diagramSpecification, DiagramTechnologyAdapter
		 * adapter) { this(virtualModel, adapter);
		 * setMetaModelResource(diagramSpecification.getResource()); }
		 */

		/*
		 * public DiagramModelSlot(ViewPointBuilder builder) { super(builder); }
		 */

		// private DiagramType diagramType;

		@Override
		public String getStringRepresentation() {
			return "TypedDiagramModelSlot";
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
		public FMLDiagramPaletteElementBinding getPaletteElementBinding(DiagramPaletteElement paletteElement) {
			for (FMLDiagramPaletteElementBinding binding : getPaletteElementBindings()) {
				if (binding.getPaletteElement() == paletteElement) {
					return binding;
				}
			}
			return null;
		}

		@Override
		public List<FMLDiagramPaletteElementBinding> getPaletteElementBindings(DiagramPaletteElement paletteElement) {
			ArrayList<FMLDiagramPaletteElementBinding> bindings = new ArrayList<FMLDiagramPaletteElementBinding>();
			for (FMLDiagramPaletteElementBinding binding : getPaletteElementBindings()) {
				if (binding.getPaletteElement() == paletteElement) {
					bindings.add(binding);
				}
			}
			return bindings;
		}

		@Override
		public TypedDiagramModelSlotInstanceConfiguration createConfiguration(FlexoConceptInstance fci, FlexoResourceCenter<?> rc) {
			return new TypedDiagramModelSlotInstanceConfiguration(this, fci, rc);
		}

		@Override
		public DiagramResource createProjectSpecificEmptyModel(FlexoResourceCenter<?> rc, String diagramName, String relativePath,
				String diagramUri, FlexoMetaModelResource<Diagram, DiagramSpecification, ?> metaModelResource) {

			DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService()
					.getTechnologyAdapter(DiagramTechnologyAdapter.class);
			DiagramResourceFactory factory = getModelSlotTechnologyAdapter().getDiagramResourceFactory();

			Object serializationArtefact = diagramTA.retrieveResourceSerializationArtefact(rc, diagramName, relativePath,
					DiagramResourceFactory.DIAGRAM_SUFFIX);

			DiagramResource newDiagramResource;
			try {
				newDiagramResource = factory.makeResource(serializationArtefact, (FlexoResourceCenter) rc,
						diagramTA.getTechnologyContextManager(), diagramName, diagramUri, true);
				newDiagramResource.setMetaModelResource((FlexoMetaModelResource) metaModelResource);
				return newDiagramResource;
			} catch (SaveResourceException e) {
				e.printStackTrace();
			} catch (ModelDefinitionException e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		public DiagramResource createSharedEmptyModel(FlexoResourceCenter<?> rc, String relativePath, String diagramName, String diagramUri,
				FlexoMetaModelResource<Diagram, DiagramSpecification, ?> metaModelResource) {

			DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService()
					.getTechnologyAdapter(DiagramTechnologyAdapter.class);
			DiagramResourceFactory factory = getModelSlotTechnologyAdapter().getDiagramResourceFactory();
			String artefactName = diagramName.endsWith(DiagramResourceFactory.DIAGRAM_SUFFIX) ? diagramName
					: diagramName + DiagramResourceFactory.DIAGRAM_SUFFIX;

			Object serializationArtefact = ((FlexoResourceCenter) rc).createEntry(relativePath + File.separator + artefactName,
					rc.getBaseArtefact());

			DiagramResource newDiagramResource;
			try {
				newDiagramResource = diagramTA.getDiagramResourceFactory().makeResource(serializationArtefact, (FlexoResourceCenter) rc,
						diagramTA.getTechnologyContextManager(), diagramName, diagramUri, true);
				newDiagramResource.setMetaModelResource((FlexoMetaModelResource) metaModelResource);
				return newDiagramResource;
			} catch (SaveResourceException e) {
				e.printStackTrace();
			} catch (ModelDefinitionException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public String getURIForObject(
				TypeAwareModelSlotInstance<Diagram, DiagramSpecification, ? extends TypeAwareModelSlot<Diagram, DiagramSpecification>> msInstance,
				Object o) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(
				TypeAwareModelSlotInstance<Diagram, DiagramSpecification, ? extends TypeAwareModelSlot<Diagram, DiagramSpecification>> msInstance,
				String objectURI) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isStrictMetaModelling() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Type getType() {
			return DiagramType.getDiagramType(getDiagramSpecification());

			// Vincent: update this when we will clarify what is a diagram specification and diagram
			// Answer from Sylvain: is that ok for you ?
			/*if (diagramType == null) {
				diagramType = DiagramType.getDiagramType(getDiagramSpecification());
			}
			return diagramType;*/
		}

		@Override
		public String getTypeDescription() {
			return "Diagram Specification";
		}

		@Override
		public FMLDiagramPaletteElementBinding addFMLDiagramPaletteElementBinding() {
			FMLDiagramPaletteElementBinding newBinding = getFMLModelFactory().newInstance(FMLDiagramPaletteElementBinding.class);
			addToPaletteElementBindings(newBinding);
			return newBinding;
		}

		@Override
		public DiagramSpecification getDiagramSpecification() {
			if (getMetaModelResource() != null) {
				return getMetaModelResource().getMetaModelData();
			}
			return null;
		}

		/**
		 * Overrides super implementation by providing default graphical representations
		 */
		/*
		@Override
		public <PR extends FlexoRole<?>> PR makeFlexoRole(Class<PR> flexoRoleClass) {
			PR returned = super.makeFlexoRole(flexoRoleClass);
			if (ShapeRole.class.isAssignableFrom(flexoRoleClass)) {
				ShapeRole shapeRole = (ShapeRole) returned;
				ShapeGraphicalRepresentation gr = getFMLModelFactory().makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
				gr.setWidth(80);
				gr.setHeight(50);
				gr.setX(20);
				gr.setY(20);
				gr.setIsFloatingLabel(false);
				shapeRole.setGraphicalRepresentation(gr);
			}
			if (ConnectorRole.class.isAssignableFrom(flexoRoleClass)) {
				ConnectorRole connectorRole = (ConnectorRole) returned;
				connectorRole.setGraphicalRepresentation(getFMLModelFactory().makeConnectorGraphicalRepresentation(ConnectorType.LINE));
			}
			return returned;
		}
		*/

		// TODO
		/*	@Override
			public List<FMLDiagramPaletteElementBinding> getPaletteElementBindings() {
				return paletteElementBindings;
			}
		
			@Override
			public void setPaletteElementBindings(List<FMLDiagramPaletteElementBinding> paletteElementBindings) {
				this.paletteElementBindings = paletteElementBindings;
			}
		
			@Override
			public void addToPaletteElementBindings(FMLDiagramPaletteElementBinding paletteElementBinding) {
				paletteElementBindings.add(paletteElementBinding);
			}
		
			@Override
			public void removeFromPaletteElementBindings(FMLDiagramPaletteElementBinding paletteElementBinding) {
				paletteElementBindings.remove(paletteElementBinding);
			}*/

	}
}
