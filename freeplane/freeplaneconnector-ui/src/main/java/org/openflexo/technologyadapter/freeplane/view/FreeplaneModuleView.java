package org.openflexo.technologyadapter.freeplane.view;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.freeplane.main.application.BasicFreeplaneAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class FreeplaneModuleView extends JScrollPane implements ModuleView<IFreeplaneMap> {

    /**
     * Generated serial
     */
    private static final long      serialVersionUID = 8443361431050803298L;
    private final FlexoPerspective perspective;
    private final IFreeplaneMap    map;
    private final FlexoController  controller;

    public FreeplaneModuleView(final IFreeplaneMap map, final FlexoController controller, final FlexoPerspective peerspective) {
        super(BasicFreeplaneAdapter.getInstance().getMapView());
        this.controller = controller;
        this.perspective = peerspective;
        this.map = map;

    }

    @Override
    public IFreeplaneMap getRepresentedObject() {
        return this.map;
    }

    @Override
    public void deleteModuleView() {
        this.controller.removeModuleView(this);
    }

    @Override
    public FlexoPerspective getPerspective() {
        return this.perspective;
    }

    @Override
    public void willShow() {
    }

    @Override
    public void willHide() {
    }

    @Override
    public void show(final FlexoController controller, final FlexoPerspective perspective) {
        final JScrollPane jeSuisNelEnSwing = new JScrollPane(BasicFreeplaneAdapter.getInstance().getIconToolar());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                perspective.setTopRightView(jeSuisNelEnSwing);
                controller.getControllerModel().setRightViewVisible(true);
            }
        });
        controller.getControllerModel().setRightViewVisible(true);
    }

    @Override
    public boolean isAutoscrolled() {
        return true;
    }

}
