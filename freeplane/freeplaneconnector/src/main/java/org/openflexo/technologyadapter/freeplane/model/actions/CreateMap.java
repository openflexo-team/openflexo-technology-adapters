package org.openflexo.technologyadapter.freeplane.model.actions;

import java.util.Vector;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.ActionGroup;
import org.openflexo.foundation.action.ActionMenu;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;

@ModelEntity
public class CreateMap extends FlexoAction<CreateMap, IFreeplaneMap, IFreeplaneMap> {

    /**
     * Avoid declaration in affectation for readability.
     * 
     * @author eloubout
     * 
     */
    private static final class CreateMapActionType extends FlexoActionType<CreateMap, IFreeplaneMap, IFreeplaneMap> {

        protected CreateMapActionType(final String actionName, final ActionMenu actionMenu, final ActionGroup actionGroup, final int actionCategory) {
            super(actionName, actionMenu, actionGroup, actionCategory);
        }

        @Override
        public CreateMap makeNewAction(final IFreeplaneMap map, final Vector<IFreeplaneMap> maps, final FlexoEditor flexoEditor) {
            return new CreateMap(map, maps, flexoEditor);
        }

        @Override
        public boolean isVisibleForSelection(final IFreeplaneMap map, final Vector<IFreeplaneMap> maps) {
            return map != null;
        }

        @Override
        public boolean isEnabledForSelection(final IFreeplaneMap map, final Vector<IFreeplaneMap> maps) {
            return map != null && !map.getMapModel().isReadOnly();
        }
    }

    /** ActionType for CreateMap */
    public static FlexoActionType<CreateMap, IFreeplaneMap, IFreeplaneMap> actionType = new CreateMapActionType("create_data_property", FlexoActionType.newMenu, FlexoActionType.defaultGroup,
                                                                                              FlexoActionType.ADD_ACTION_TYPE);

    public CreateMap(final IFreeplaneMap focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    static {
        FlexoObjectImpl.addActionForClass(actionType, FreeplaneMapImpl.class);
    }

    @Override
    protected void doAction(final Object objet) throws FlexoException {
        if (objet instanceof IFreeplaneMap) {
            System.out.println("DO NOTHING MOUHAHAHAHAHAHA");
        }
    }

}
