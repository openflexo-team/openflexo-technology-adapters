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
            System.out.println("#YOLOOOOOOOOOOOOOOOOOOOOOO");
            return new CreateMap(map, maps, flexoEditor);
        }

        @Override
        public boolean isVisibleForSelection(final IFreeplaneMap map, final Vector<IFreeplaneMap> maps) {
            System.out.println("tititaaaaaaaaaaaaaaaaaieetetetetett");
            return map != null;
        }

        @Override
        public boolean isEnabledForSelection(final IFreeplaneMap map, final Vector<IFreeplaneMap> maps) {
            System.out.println("tititeeeeuuuuuuuuuett");
            return map != null && !map.getMapModel().isReadOnly();
        }
    }

    /** ActionType for CreateMap */
    public static final FlexoActionType<CreateMap, IFreeplaneMap, IFreeplaneMap> actionType = new CreateMapActionType("create_map", FlexoActionType.newMenu, FlexoActionType.editGroup,
                                                                                                    FlexoActionType.ADD_ACTION_TYPE);

    public CreateMap(final IFreeplaneMap focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
        System.out.println("tititititititititit");
    }

    static {
        FlexoObjectImpl.addActionForClass(actionType, IFreeplaneMap.class);
    }

    @Override
    protected void doAction(final Object objet) throws FlexoException {
        if (objet instanceof IFreeplaneMap) {
            System.out.println("tititieetetetetett");
        }
    }

}
