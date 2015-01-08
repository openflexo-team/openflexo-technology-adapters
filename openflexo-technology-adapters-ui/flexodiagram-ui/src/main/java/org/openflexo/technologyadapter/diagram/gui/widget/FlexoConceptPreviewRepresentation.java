/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.diagram.gui.widget;

import java.awt.Color;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.GRBinding.ConnectorGRBinding;
import org.openflexo.fge.GRBinding.DrawingGRBinding;
import org.openflexo.fge.GRBinding.ShapeGRBinding;
import org.openflexo.fge.GRProvider.ConnectorGRProvider;
import org.openflexo.fge.GRProvider.DrawingGRProvider;
import org.openflexo.fge.GRProvider.ShapeGRProvider;
import org.openflexo.fge.GRStructureVisitor;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.connectors.ConnectorSpecification.ConnectorType;
import org.openflexo.fge.impl.DrawingImpl;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;

public class FlexoConceptPreviewRepresentation extends DrawingImpl<FlexoConcept> implements FlexoConceptPreviewConstants {

	private static final Logger logger = Logger.getLogger(FlexoConceptPreviewRepresentation.class.getPackage().getName());

	// private FlexoConceptPreviewShemaGR graphicalRepresentation;

	// private Boolean ignoreNotifications = true;

	// private Hashtable<FlexoRole, FlexoConceptPreviewShapeGR> shapesGR;
	// private Hashtable<FlexoRole, FlexoConceptPreviewConnectorGR> connectorsGR;

	/*static FGEModelFactory PREVIEW_FACTORY;

	static {
		try {
			PREVIEW_FACTORY = new FGEModelFactoryImpl();
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
	}*/

	private final Hashtable<FlexoRole, ConnectorFromArtifact> fromArtifacts;
	private final Hashtable<FlexoRole, ConnectorToArtifact> toArtifacts;

	public FlexoConceptPreviewRepresentation(FlexoConcept model) {
		super(model, model.getVirtualModelFactory(), PersistenceMode.UniqueGraphicalRepresentations);
		// Sylvain: commented this because not movable nor rezizable shapes
		// setEditable(false);

		factory = model.getVirtualModelFactory();

		fromArtifacts = new Hashtable<FlexoRole, ConnectorFromArtifact>();
		toArtifacts = new Hashtable<FlexoRole, ConnectorToArtifact>();
	}

	private final FMLModelFactory factory;

	@Override
	public void init() {

		final DrawingGRBinding<FlexoConcept> drawingBinding = bindDrawing(FlexoConcept.class, "flexoConcept",
				new DrawingGRProvider<FlexoConcept>() {
					@Override
					public DrawingGraphicalRepresentation provideGR(FlexoConcept drawable, FGEModelFactory factory) {
						DrawingGraphicalRepresentation returned = factory.makeDrawingGraphicalRepresentation();
						returned.setWidth(WIDTH);
						returned.setHeight(HEIGHT);
						returned.setBackgroundColor(BACKGROUND_COLOR);
						returned.setDrawWorkingArea(false);
						return returned;
					}
				});
		final ShapeGRBinding<ShapeRole> shapeBinding = bindShape(ShapeRole.class, "shapePatternRole", new ShapeGRProvider<ShapeRole>() {
			@Override
			public ShapeGraphicalRepresentation provideGR(ShapeRole drawable, FGEModelFactory factory) {
				if (drawable.getGraphicalRepresentation() == null) {
					drawable.setGraphicalRepresentation(makeDefaultShapeGR());
				}
				return drawable.getGraphicalRepresentation();
			}
		});
		final ConnectorGRBinding<ConnectorRole> connectorBinding = bindConnector(ConnectorRole.class, "connector", shapeBinding,
				shapeBinding, new ConnectorGRProvider<ConnectorRole>() {
					@Override
					public ConnectorGraphicalRepresentation provideGR(ConnectorRole drawable, FGEModelFactory factory) {
						if (drawable.getGraphicalRepresentation() == null) {
							drawable.setGraphicalRepresentation(makeDefaultConnectorGR());
						}
						return drawable.getGraphicalRepresentation();
					}
				});
		final ShapeGRBinding<ConnectorFromArtifact> fromArtefactBinding = bindShape(ConnectorFromArtifact.class, "fromArtifact",
				new ShapeGRProvider<ConnectorFromArtifact>() {
					@Override
					public ShapeGraphicalRepresentation provideGR(ConnectorFromArtifact drawable, FGEModelFactory factory) {
						return makeFromArtefactGR();
					}

				});
		final ShapeGRBinding<ConnectorToArtifact> toArtefactBinding = bindShape(ConnectorToArtifact.class, "toArtifact",
				new ShapeGRProvider<ConnectorToArtifact>() {
					@Override
					public ShapeGraphicalRepresentation provideGR(ConnectorToArtifact drawable, FGEModelFactory factory) {
						return makeToArtefactGR();
					}

				});

		drawingBinding.addToWalkers(new GRStructureVisitor<FlexoConcept>() {

			@Override
			public void visit(FlexoConcept flexoConcept) {

				for (FlexoRole<?> role : flexoConcept.getFlexoRoles()) {
					if (role instanceof ShapeRole) {
						if (((ShapeRole) role).getParentShapeAsDefinedInAction()) {
							drawShape(shapeBinding, (ShapeRole) role, getFlexoConcept());
							// System.out.println("Add shape " + role.getRoleName() + " under FlexoConcept");
						} 
					} else if (role instanceof ConnectorRole) {
						ConnectorRole connectorRole = (ConnectorRole) role;
						ShapeGRBinding fromBinding;
						ShapeGRBinding toBinding;
						Object fromDrawable;
						Object toDrawable;
						if (connectorRole.getStartShapeRole() == null) {
							drawShape(fromArtefactBinding, getFromArtifact(connectorRole), getFlexoConcept());
							fromBinding = fromArtefactBinding;
							fromDrawable = getFromArtifact(connectorRole);
							// System.out.println("Add From artifact under FlexoConcept");
						} else {
							fromBinding = shapeBinding;
							fromDrawable = connectorRole.getStartShapeRole();
						}
						if (connectorRole.getEndShapeRole() == null) {
							drawShape(toArtefactBinding, getToArtifact(connectorRole), getFlexoConcept());
							// System.out.println("Add To artifact under FlexoConcept");
							toBinding = toArtefactBinding;
							toDrawable = getToArtifact(connectorRole);
						} else {
							toBinding = shapeBinding;
							toDrawable = connectorRole.getEndShapeRole();
						}
						// System.out.println("Add connector " + role.getPatternRoleName() + " under FlexoConcept");
						drawConnector(connectorBinding, connectorRole, fromBinding, fromDrawable, toBinding, toDrawable);
					}
				}

			}
		});

		shapeBinding.addToWalkers(new GRStructureVisitor<ShapeRole>() {

			@Override
			public void visit(ShapeRole parentRole) {

				for (FlexoRole<?> role : parentRole.getFlexoConcept().getFlexoRoles()) {
					if (role instanceof ShapeRole) {
						if (((ShapeRole) role).getParentShapeRole() == parentRole) {
							drawShape(shapeBinding, (ShapeRole) role, parentRole);
							//System.out.println("Add shape " + role.getRoleName() + " under " + parentRole.getRoleName());
						}
					}
				}

			}
		});

		shapeBinding.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.exampleLabel"), true);
		connectorBinding.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.exampleLabel"), true);

	}

	@Override
	public void delete() {

		/*if (graphicalRepresentation != null) {
			graphicalRepresentation.delete();
		}*/
		/*if (getFlexoConcept() != null) {
			getFlexoConcept().deleteObserver(this);
		}*/
		/*for (FlexoRole role : getFlexoConcept().getPatternRoles()) {
			role.deleteObserver(this);
		}*/
		super.delete();
	}

	public FlexoConcept getFlexoConcept() {
		return getModel();
	}

	protected ConnectorFromArtifact getFromArtifact(ConnectorRole connector) {
		ConnectorFromArtifact returned = fromArtifacts.get(connector);
		if (returned == null) {
			returned = new ConnectorFromArtifact(connector);
			fromArtifacts.put(connector, returned);
		}
		return returned;
	}

	protected ConnectorToArtifact getToArtifact(ConnectorRole connector) {
		ConnectorToArtifact returned = toArtifacts.get(connector);
		if (returned == null) {
			returned = new ConnectorToArtifact(connector);
			toArtifacts.put(connector, returned);
		}
		return returned;
	}

	protected class ConnectorFromArtifact {

		private final ConnectorRole connector;

		protected ConnectorFromArtifact(ConnectorRole aConnector) {
			connector = aConnector;
		}

	}

	protected class ConnectorToArtifact {

		private final ConnectorRole connector;

		protected ConnectorToArtifact(ConnectorRole aConnector) {
			connector = aConnector;
		}

	}

	private ShapeGraphicalRepresentation makeFromArtefactGR() {
		ShapeGraphicalRepresentation returned = factory.makeShapeGraphicalRepresentation(ShapeType.CIRCLE);
		returned.setX(80);
		returned.setY(80);
		returned.setWidth(20);
		returned.setHeight(20);
		returned.setForeground(factory.makeForegroundStyle(new Color(255, 204, 0)));
		returned.setBackground(factory.makeColoredBackground(new Color(255, 255, 204)));
		returned.setIsFocusable(true);
		returned.setIsSelectable(false);
		return returned;
	}

	private ShapeGraphicalRepresentation makeToArtefactGR() {
		ShapeGraphicalRepresentation returned = factory.makeShapeGraphicalRepresentation(ShapeType.CIRCLE);
		returned.setX(350);
		returned.setY(80);
		returned.setWidth(20);
		returned.setHeight(20);
		returned.setForeground(factory.makeForegroundStyle(new Color(255, 204, 0)));
		returned.setBackground(factory.makeColoredBackground(new Color(255, 255, 204)));
		returned.setIsFocusable(true);
		returned.setIsSelectable(false);
		return returned;
	}

	private ShapeGraphicalRepresentation makeDefaultShapeGR() {
		ShapeGraphicalRepresentation returned = factory.makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
		returned.setTextStyle(factory.makeTextStyle(DEFAULT_SHAPE_TEXT_COLOR, DEFAULT_FONT));
		returned.setX((WIDTH - DEFAULT_SHAPE_WIDTH) / 2);
		returned.setY((HEIGHT - DEFAULT_SHAPE_HEIGHT) / 2);
		returned.setWidth(DEFAULT_SHAPE_WIDTH);
		returned.setHeight(DEFAULT_SHAPE_HEIGHT);
		returned.setBackground(factory.makeColoredBackground(DEFAULT_SHAPE_BACKGROUND_COLOR));
		returned.setIsFloatingLabel(false);
		return returned;
	}

	private ConnectorGraphicalRepresentation makeDefaultConnectorGR() {
		ConnectorGraphicalRepresentation returned = factory.makeConnectorGraphicalRepresentation(ConnectorType.LINE);
		returned.setTextStyle(factory.makeTextStyle(DEFAULT_SHAPE_TEXT_COLOR, DEFAULT_FONT));
		return returned;
	}

}
