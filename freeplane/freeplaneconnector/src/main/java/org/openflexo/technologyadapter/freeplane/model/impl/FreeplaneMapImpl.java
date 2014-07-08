package org.openflexo.technologyadapter.freeplane.model.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.MapModel;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

public abstract class FreeplaneMapImpl extends FlexoObjectImpl implements IFreeplaneMap {

	private static final Logger LOGGER = Logger.getLogger(FreeplaneMapImpl.class.getPackage().getName());

	private FreeplaneTechnologyAdapter technologyAdapter;

	private MapModel mapModel;

	public FreeplaneMapImpl(final FreeplaneTechnologyAdapter technologyAdapter) {
		this.technologyAdapter = technologyAdapter;
	}

	public FreeplaneMapImpl() {
	}

	public void setTechnologyAdapter(final FreeplaneTechnologyAdapter technologyAdapter) {
		this.technologyAdapter = technologyAdapter;
	}

	/* (non-Javadoc)
	 * @see org.openflexo.foundation.technologyadapter.TechnologyObject#getTechnologyAdapter()
	 */
	@Override
	public FreeplaneTechnologyAdapter getTechnologyAdapter() {
		return this.technologyAdapter;
	}

	@Override
	@Setter(value = MAP_MODEL_KEY)
	public void setMapModel(final MapModel map) {
		this.mapModel = map;
		try {
			final ModelFactory factory = new ModelFactory(IFreeplaneNode.class);
			final FreeplaneNodeImpl node = (FreeplaneNodeImpl) factory.newInstance(IFreeplaneNode.class);
			node.setTechnologyAdapter(this.getTechnologyAdapter());
			// recursive call inside, don't be preoccupied by children
			// initialization.
			node.setNodeModel(map.getRootNode());
			this.setRootNode(node);
		} catch (final ModelDefinitionException e) {
			final String msg = "Error while setting MapModel of map " + this;
			LOGGER.log(Level.SEVERE, msg, e);
		}
	}

	@Override
	@Getter(value = MAP_MODEL_KEY, ignoreType = true)
	public MapModel getMapModel() {
		return this.mapModel;
	}

	@Override
	public String getUri() {
		return "MAP=" + this.getMapModel().getTitle();
	}
}
