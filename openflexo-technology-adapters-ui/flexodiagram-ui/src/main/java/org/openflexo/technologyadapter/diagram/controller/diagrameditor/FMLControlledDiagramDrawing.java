/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.DataBinding;
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
import org.openflexo.fge.geom.FGEGeometricObject.SimplifiedCardinalDirection;
import org.openflexo.foundation.fml.binding.FlexoConceptBindingFactory;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance.ObjectLookupResult;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This is the abstraction of a drawing representing a {@link Diagram} in controlled mode: the Diagram is edited in a federated mode<br>
 * There is a VirtualModel controlling the edition of the diagram
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramDrawing extends AbstractDiagramDrawing implements DiagramRepresentationConstants {

	private static final Logger logger = Logger.getLogger(FMLControlledDiagramDrawing.class.getPackage().getName());

	private final VirtualModelInstance virtualModelInstance;

	private ModelFactory MODEL_FACTORY = null;

	private final Map<FlexoConceptInstance, List<FMLControlledDiagramElement<?, ?>>> diagramElementsForFlexoConceptInstances;
	private final Map<DiagramShape, FMLControlledDiagramShape> federatedShapes;
	private final Map<DiagramConnector, FMLControlledDiagramConnector> federatedConnectors;

	private ShapeGRBinding<FMLControlledDiagramShape> fmlControlledShapeBinding;
	private ConnectorGRBinding<FMLControlledDiagramConnector> fmlControlledConnectorBinding;

	private final List<FMLControlledDiagramFloatingPalette> floatingPalettes;

	public FMLControlledDiagramDrawing(VirtualModelInstance vmInstance, boolean readOnly) {
		super(FMLControlledDiagramVirtualModelInstanceNature.getDiagram(vmInstance), readOnly);
		this.virtualModelInstance = vmInstance;
		floatingPalettes = new ArrayList<FMLControlledDiagramFloatingPalette>();
		diagramElementsForFlexoConceptInstances = new HashMap<FlexoConceptInstance, List<FMLControlledDiagramElement<?, ?>>>();
		federatedShapes = new HashMap<DiagramShape, FMLControlledDiagramShape>();
		federatedConnectors = new HashMap<DiagramConnector, FMLControlledDiagramConnector>();
		try {
			MODEL_FACTORY = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FMLControlledDiagramShape.class,
					FMLControlledDiagramConnector.class));
			MODEL_FACTORY.setEditingContext(vmInstance.getServiceManager().getEditingContext());
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ((virtualModelInstance != null) && (virtualModelInstance.getVirtualModel() != null)) {
			BindingFactory bindingFactory = new FlexoConceptBindingFactory(virtualModelInstance.getVirtualModel().getViewPoint());
			fmlControlledShapeBinding.setBindingFactory(bindingFactory);
			fmlControlledConnectorBinding.setBindingFactory(bindingFactory);
		}

	}

	@Override
	public void delete() {
		for (FMLControlledDiagramFloatingPalette floatingPalette : floatingPalettes) {
			floatingPalette.delete();
		}
		super.delete();
	}

	public VirtualModelInstance getVirtualModelInstance() {
		return virtualModelInstance;
	}

	@Override
	public Diagram getDiagram() {
		return FMLControlledDiagramVirtualModelInstanceNature.getDiagram(getVirtualModelInstance());
	}

	@Override
	public <O> void invalidateGraphicalObjectsHierarchy(O drawable) {
		super.invalidateGraphicalObjectsHierarchy(drawable);
		clearCache();
	}

	@Override
	public void invalidateGraphicalObjectsHierarchy() {
		super.invalidateGraphicalObjectsHierarchy();
		clearCache();
	}

	ObjectLookupResult getObjectLookupResult(DiagramElement<?> element) {
		return getVirtualModelInstance().lookup(element);
	}

	private void clearCache() {
		// TODO
	}

	// TODO: implements cache to not always try to retrieve FMLControlledDiagramElement when DiagramElement is not federated
	public FMLControlledDiagramShape getFederatedShape(DiagramShape shape) {
		FMLControlledDiagramShape returned = federatedShapes.get(shape);
		if (returned == null) {
			ObjectLookupResult r = getObjectLookupResult(shape);
			if (r != null) {
				returned = MODEL_FACTORY.newInstance(FMLControlledDiagramShape.class);
				federatedShapes.put(shape, returned);
				returned.setDrawing(this);
				returned.setFlexoConceptInstance(r.flexoConceptInstance);
				returned.setRole((ShapeRole) r.property);
				returned.setDiagramElement(shape);
				registerNewFMLControlledDiagramElement(returned);
				// shape.setName(returned.getLabel());
			} else {
				// TODO: perfs issue: when not found it will be costly !!!
			}
		}
		return returned;
	}

	// TODO: implements cache to not always try to retrieve FMLControlledDiagramElement when DiagramElement is not federated
	public FMLControlledDiagramConnector getFederatedConnector(DiagramConnector connector) {
		FMLControlledDiagramConnector returned = federatedConnectors.get(connector);
		if (returned == null) {
			ObjectLookupResult r = getObjectLookupResult(connector);
			if (r != null) {
				returned = MODEL_FACTORY.newInstance(FMLControlledDiagramConnector.class);
				federatedConnectors.put(connector, returned);
				returned.setDrawing(this);
				returned.setFlexoConceptInstance(r.flexoConceptInstance);
				returned.setRole((ConnectorRole) r.property);
				returned.setDiagramElement(connector);
				registerNewFMLControlledDiagramElement(returned);
			} else {
				// TODO: perfs issue: when not found it will be costly !!!
			}
		}
		return returned;
	}

	private void registerNewFMLControlledDiagramElement(FMLControlledDiagramElement<?, ?> fmlControlledDiagramElement) {
		List<FMLControlledDiagramElement<?, ?>> list = diagramElementsForFlexoConceptInstances.get(fmlControlledDiagramElement
				.getFlexoConceptInstance());
		if (list == null) {
			list = new ArrayList<FMLControlledDiagramElement<?, ?>>();
			diagramElementsForFlexoConceptInstances.put(fmlControlledDiagramElement.getFlexoConceptInstance(), list);
		}
		if (!list.contains(fmlControlledDiagramElement)) {
			list.add(fmlControlledDiagramElement);
		}
	}

	/**
	 * Return the list of all {@link FMLControlledDiagramElement} addressing supplied {@link FlexoConceptInstance}
	 * 
	 * @param flexoConceptInstance
	 * @return
	 */
	public List<FMLControlledDiagramElement<?, ?>> getFMLControlledDiagramElements(FlexoConceptInstance flexoConceptInstance) {
		return diagramElementsForFlexoConceptInstances.get(flexoConceptInstance);
	}

	@Override
	public void init() {

		final DrawingGRBinding<Diagram> drawingBinding = bindDrawing(Diagram.class, "drawing", new DrawingGRProvider<Diagram>() {
			@Override
			public DrawingGraphicalRepresentation provideGR(Diagram drawable, FGEModelFactory factory) {
				return retrieveGraphicalRepresentation(drawable, (DiagramFactory) factory);
			}
		});
		final ShapeGRBinding<DiagramShape> shapeBinding = bindShape(DiagramShape.class, "shape", new ShapeGRProvider<DiagramShape>() {
			@Override
			public ShapeGraphicalRepresentation provideGR(DiagramShape drawable, FGEModelFactory factory) {
				return retrieveGraphicalRepresentation(drawable, (DiagramFactory) factory);
			}
		});

		fmlControlledShapeBinding = bindShape(FMLControlledDiagramShape.class, "fmlControlledShape",
				new ShapeGRProvider<FMLControlledDiagramShape>() {
					@Override
					public ShapeGraphicalRepresentation provideGR(FMLControlledDiagramShape drawable, FGEModelFactory factory) {
						if (drawable == null) {
							return null;
						}
						return retrieveGraphicalRepresentation(drawable.getDiagramElement(), (DiagramFactory) factory);
					}

					@Override
					public List<FMLControlledDiagramFloatingPalette> makeControlAreasFor(
							DrawingTreeNode<FMLControlledDiagramShape, ShapeGraphicalRepresentation> dtn) {

						ShapeNode<FMLControlledDiagramShape> node = (ShapeNode<FMLControlledDiagramShape>) dtn;
						List<LinkScheme> availableLinkSchemes = dtn.getDrawable().getAvailableLinkSchemes();

						if (availableLinkSchemes != null && availableLinkSchemes.size() > 0) {

							List<FMLControlledDiagramFloatingPalette> returned = new ArrayList<FMLControlledDiagramFloatingPalette>();

							boolean northDirectionSupported = false;
							boolean eastDirectionSupported = false;
							boolean southDirectionSupported = false;
							boolean westDirectionSupported = false;
							for (LinkScheme ls : availableLinkSchemes) {
								if (ls.getNorthDirectionSupported()) {
									northDirectionSupported = true;
								}
								if (ls.getEastDirectionSupported()) {
									eastDirectionSupported = true;
								}
								if (ls.getSouthDirectionSupported()) {
									southDirectionSupported = true;
								}
								if (ls.getWestDirectionSupported()) {
									westDirectionSupported = true;
								}
							}

							if (northDirectionSupported) {
								returned.add(new FMLControlledDiagramFloatingPalette(node, SimplifiedCardinalDirection.NORTH));
							}
							if (eastDirectionSupported) {
								returned.add(new FMLControlledDiagramFloatingPalette(node, SimplifiedCardinalDirection.EAST));
							}
							if (southDirectionSupported) {
								returned.add(new FMLControlledDiagramFloatingPalette(node, SimplifiedCardinalDirection.SOUTH));
							}
							if (westDirectionSupported) {
								returned.add(new FMLControlledDiagramFloatingPalette(node, SimplifiedCardinalDirection.WEST));
							}

							floatingPalettes.addAll(returned);

							return returned;
						}

						return null;
					}
				});

		final ConnectorGRBinding<DiagramConnector> connectorBinding = bindConnector(DiagramConnector.class, "connector", shapeBinding,
				shapeBinding, new ConnectorGRProvider<DiagramConnector>() {
					@Override
					public ConnectorGraphicalRepresentation provideGR(DiagramConnector drawable, FGEModelFactory factory) {
						return retrieveGraphicalRepresentation(drawable, (DiagramFactory) factory);
					}
				});

		fmlControlledConnectorBinding = bindConnector(FMLControlledDiagramConnector.class, "fmlControlledConnector",
				new ConnectorGRProvider<FMLControlledDiagramConnector>() {
					@Override
					public ConnectorGraphicalRepresentation provideGR(FMLControlledDiagramConnector drawable, FGEModelFactory factory) {
						if (drawable == null) {
							return null;
						}
						return retrieveGraphicalRepresentation(drawable.getDiagramElement(), (DiagramFactory) factory);
					}
				});

		drawingBinding.addToWalkers(new GRStructureVisitor<Diagram>() {

			@Override
			public void visit(Diagram diagram) {

				for (DiagramShape shape : diagram.getShapes()) {
					FMLControlledDiagramShape fmlShape = getFederatedShape(shape);
					if (fmlShape != null) {
						// In this case, the shape is federated in a certain FlexoConceptInstance : we will address this concept instead
						drawShape(fmlControlledShapeBinding, fmlShape, diagram);
					} else {
						// Otherwise, this is a normal shape, we just display the shape
						drawShape(shapeBinding, shape, diagram);
					}
				}
				for (DiagramConnector connector : diagram.getConnectors()) {
					FMLControlledDiagramConnector fmlConnector = getFederatedConnector(connector);
					FMLControlledDiagramShape fmlStartShape = getFederatedShape(connector.getStartShape());
					FMLControlledDiagramShape fmlEndShape = getFederatedShape(connector.getEndShape());
					if (fmlConnector != null) {
						// In this case, the connector is federated in a certain FlexoConceptInstance : we will address this concept instead
						drawConnector(fmlControlledConnectorBinding, fmlConnector,
								fmlStartShape != null ? fmlStartShape : connector.getStartShape(), fmlEndShape != null ? fmlEndShape
										: connector.getEndShape(), diagram);
					} else {
						// Otherwise, this is a normal connector, we just display the connector
						drawConnector(connectorBinding, connector, fmlStartShape != null ? fmlStartShape : connector.getStartShape(),
								fmlEndShape != null ? fmlEndShape : connector.getEndShape(), diagram);
					}
				}
			}
		});

		shapeBinding.addToWalkers(new GRStructureVisitor<DiagramShape>() {
			@Override
			public void visit(DiagramShape aShape) {
				for (DiagramShape shape : aShape.getShapes()) {
					FMLControlledDiagramShape fmlShape = getFederatedShape(shape);
					if (fmlShape != null) {
						// In this case, the shape is federated in a certain FlexoConceptInstance : we will address this concept instead
						drawShape(fmlControlledShapeBinding, fmlShape, aShape);
					} else {
						// Otherwise, this is a normal shape, we just display the shape
						drawShape(shapeBinding, shape, aShape);
					}
				}
				for (DiagramConnector connector : aShape.getConnectors()) {
					FMLControlledDiagramConnector fmlConnector = getFederatedConnector(connector);
					FMLControlledDiagramShape fmlStartShape = getFederatedShape(connector.getStartShape());
					FMLControlledDiagramShape fmlEndShape = getFederatedShape(connector.getEndShape());
					if (fmlConnector != null) {
						// In this case, the connector is federated in a certain FlexoConceptInstance : we will address this concept instead
						drawConnector(fmlControlledConnectorBinding, fmlConnector,
								fmlStartShape != null ? fmlStartShape : connector.getStartShape(), fmlEndShape != null ? fmlEndShape
										: connector.getEndShape(), aShape);
					} else {
						// Otherwise, this is a normal connector, we just display the connector
						drawConnector(connectorBinding, connector, fmlStartShape != null ? fmlStartShape : connector.getStartShape(),
								fmlEndShape != null ? fmlEndShape : connector.getEndShape(), aShape);
					}
				}
			}
		});

		fmlControlledShapeBinding.addToWalkers(new GRStructureVisitor<FMLControlledDiagramShape>() {
			@Override
			public void visit(FMLControlledDiagramShape aShape) {
				if (aShape.getDiagramElement() != null) {
					for (DiagramShape shape : aShape.getDiagramElement().getShapes()) {
						FMLControlledDiagramShape fmlShape = getFederatedShape(shape);
						if (fmlShape != null) {
							// In this case, the shape is federated in a certain FlexoConceptInstance : we will address this concept instead
							drawShape(fmlControlledShapeBinding, fmlShape, aShape);
						} else {
							// Otherwise, this is a normal shape, we just display the shape
							drawShape(shapeBinding, shape, aShape);
						}
					}
					for (DiagramConnector connector : aShape.getDiagramElement().getConnectors()) {
						FMLControlledDiagramConnector fmlConnector = getFederatedConnector(connector);
						FMLControlledDiagramShape fmlStartShape = getFederatedShape(connector.getStartShape());
						FMLControlledDiagramShape fmlEndShape = getFederatedShape(connector.getEndShape());
						if (fmlConnector != null) {
							// In this case, the connector is federated in a certain FlexoConceptInstance : we will address this concept
							// instead
							drawConnector(fmlControlledConnectorBinding, fmlConnector,
									fmlStartShape != null ? fmlStartShape : connector.getStartShape(), fmlEndShape != null ? fmlEndShape
											: connector.getEndShape(), aShape);
						} else {
							// Otherwise, this is a normal connector, we just display the connector
							drawConnector(connectorBinding, connector, fmlStartShape != null ? fmlStartShape : connector.getStartShape(),
									fmlEndShape != null ? fmlEndShape : connector.getEndShape(), aShape);
						}
					}
				}
			}
		});

		// TODO: move this to FME !!!
		fmlControlledShapeBinding.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.label"), true);
		fmlControlledConnectorBinding
				.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.label"), true);

		shapeBinding.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.name"), true);
		connectorBinding.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.name"), true);

	}
	/*protected ShapeGraphicalRepresentation retrieveGraphicalRepresentation(DiagramShape shape, DiagramFactory factory) {
		ShapeGraphicalRepresentation returned = super.retrieveGraphicalRepresentation(shape, factory);
		if (shape != null) {
			ShapeRole patternRole = shape.getPatternRole(vmInstance);
			if (patternRole != null) {
				for (ActionMask mask : shape.getPatternRole(vmInstance).getReferencedMasks()) {
					returned.addToMouseClickControls(new FMLControlledDiagramMouseClickControl(mask, patternRole, vmInstance, factory
							.getEditingContext()));
				}
			}
		}

		return returned;
	}*/

	/*@Override
	protected ConnectorGraphicalRepresentation retrieveGraphicalRepresentation(DiagramConnector connector, DiagramFactory factory) {
		ConnectorGraphicalRepresentation returned = super.retrieveGraphicalRepresentation(connector, factory);

		boolean doubleClickUsed = false;
		if (connector != null) {
			ConnectorRole patternRole = connector.getPatternRole(vmInstance);
			if (patternRole != null) {
				for (ActionMask mask : patternRole.getReferencedMasks()) {
					returned.addToMouseClickControls(new FMLControlledDiagramMouseClickControl(mask, patternRole, vmInstance, factory
							.getEditingContext()));
					doubleClickUsed |= mask == ActionMask.DoubleClick;
				}
			}
		}

		if (!doubleClickUsed) {
			returned.addToMouseClickControls(new MouseClickControlImpl<DiagramEditor>("reset_layout", MouseButton.LEFT, 2,
					new MouseClickControlActionImpl<DiagramEditor>() {

						@Override
						public boolean handleClick(org.openflexo.fge.Drawing.DrawingTreeNode<?, ?> node, DiagramEditor controller,
								MouseControlContext context) {
							if (node instanceof ConnectorNode) {
								((ConnectorNode<?>) node).refreshConnector();
							}
							return true;
						}
					}, false, false, false, false, factory.getEditingContext()));
		}

		return returned;
	}*/

}
