package org.openflexo.technologyadapter.diagram.fml;

import java.util.logging.Logger;

import org.openflexo.fge.GRProperty;
import org.openflexo.fge.GraphicalRepresentation;

/**
 * This class represent a graphical feature that is to be associated on a DiagramElement
 * 
 * @author sylvain
 * 
 */
public abstract class GraphicalFeature<T, GR extends GraphicalRepresentation> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GraphicalFeature.class.getPackage().getName());

	private String name;
	private GRProperty<T> parameter;

	public GraphicalFeature(String name, GRProperty<T> parameter) {
		this.name = name;
		this.parameter = parameter;
	}

	public String getName() {
		return name;
	}

	public GRProperty<T> getParameter() {
		return parameter;
	}

	public Class<T> getType() {
		return parameter.getType();
	}

	public abstract void applyToGraphicalRepresentation(GR gr, T value);

	public abstract T retrieveFromGraphicalRepresentation(GR gr);

	@Override
	public String toString() {
		return "GraphicalFeature[" + getName() + "]";
	}
}
