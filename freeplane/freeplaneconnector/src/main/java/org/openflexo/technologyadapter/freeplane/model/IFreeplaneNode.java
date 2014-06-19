package org.openflexo.technologyadapter.freeplane.model;

import org.freeplane.features.map.NodeModel;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;

@ModelEntity
public interface IFreeplaneNode extends TechnologyObject<FreeplaneTechnologyAdapter> {

	public static final String	NODE_MODEL_KEY	= "noed_model";

	@Getter(value = NODE_MODEL_KEY)
	public NodeModel getNodeModel();

	@Setter(value = NODE_MODEL_KEY)
	public void setNodeModel(NodeModel model);
}
