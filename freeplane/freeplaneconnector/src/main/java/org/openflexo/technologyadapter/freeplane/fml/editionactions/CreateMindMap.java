package org.openflexo.technologyadapter.freeplane.fml.editionactions;


import org.openflexo.foundation.viewpoint.editionaction.AssignableAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.CreateMindMap.CreateMindMapImpl;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

@ImplementationClass(value = CreateMindMapImpl.class)
public interface CreateMindMap extends AssignableAction<IFreeplaneModelSlot, IFreeplaneMap> {

	public static abstract class CreateMindMapImpl extends AssignableActionImpl<IFreeplaneModelSlot, IFreeplaneMap> implements CreateMindMap {

	}
}
