package org.freeplane.main.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.fixture.FrameFixture;
import org.freeplane.core.ui.ShowSelectionAsRectangleAction;
import org.freeplane.core.ui.components.FreeplaneToolBar;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.ui.ribbon.RibbonBuilder;
import org.freeplane.core.util.Compat;
import org.freeplane.core.util.FileUtils;
import org.freeplane.core.util.LogUtils;
import org.freeplane.features.attribute.ModelessAttributeController;
import org.freeplane.features.filter.FilterController;
import org.freeplane.features.filter.NextNodeAction;
import org.freeplane.features.filter.NextPresentationItemAction;
import org.freeplane.features.format.FormatController;
import org.freeplane.features.format.ScannerController;
import org.freeplane.features.help.HelpController;
import org.freeplane.features.icon.IconController;
import org.freeplane.features.link.LinkController;
import org.freeplane.features.map.MapController;
import org.freeplane.features.map.MapController.Direction;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.mindmapmode.DeleteNodeAction;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.freeplane.features.mode.QuitAction;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.features.print.PrintController;
import org.freeplane.features.styles.LogicalStyleFilterController;
import org.freeplane.features.styles.MapViewLayout;
import org.freeplane.features.text.TextController;
import org.freeplane.features.time.TimeController;
import org.freeplane.features.ui.FrameController;
import org.freeplane.features.url.FreeplaneUriConverter;
import org.freeplane.features.url.UrlManager;
import org.freeplane.main.browsemode.BModeControllerFactory;
import org.freeplane.main.filemode.FModeControllerFactory;
import org.freeplane.main.mindmapmode.MModeControllerFactory;
import org.freeplane.n3.nanoxml.XMLException;
import org.freeplane.n3.nanoxml.XMLParseException;
import org.freeplane.view.swing.features.nodehistory.NodeHistory;
import org.freeplane.view.swing.map.MapView;
import org.freeplane.view.swing.map.ViewLayoutTypeAction;
import org.freeplane.view.swing.map.mindmapmode.MMapViewController;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;

/**
 * Most controllers initialization come from FreeplaneGUIStarter. Each concerned
 * method is commented for that.<br>
 * <br>
 * The purpose of this class is to provide a singleton to allow display of
 * freeplane mind map in Openflexo.<br>
 * Public access by getInstance()
 * 
 * @author eloubout
 * 
 */
public class BasicFreeplaneAdapter {

    private static final Logger                LOGGER   = Logger.getLogger(BasicFreeplaneAdapter.class.getName());

    private static final BasicFreeplaneAdapter INSTANCE = new BasicFreeplaneAdapter();

    /**
     * Public accessor to instance of this class.
     * 
     * @return current instance. Will never return null.
     */
    public static BasicFreeplaneAdapter getInstance() {
        return INSTANCE;
    }

    /**
     * Private constructor, that init what is needed for further use.
     */
    private BasicFreeplaneAdapter() {
        try {
            this.init();
        } catch (final Exception e) {
            final String msg = "";
            LOGGER.log(Level.SEVERE, msg, e);
        }
    }

    private ApplicationResourceController applicationResourceController;

    private MMapViewController            mapViewController;

    private ApplicationViewController     viewController;

    private FrameFixture                  window;

    private JPanel                        mapView;

    private JToolBar                      toolBar;

    /**
     * Generic Exception is thrown for readability of try catch of callers.
     * 
     * @param args
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    private void init() throws Exception {
        final String oldHandler = System.getProperty("java.protocol.handler.pkgs");
        String newHandler = "org.freeplane.main.application.protocols";
        if (oldHandler != null) {
            newHandler = oldHandler + '|' + newHandler;
        }
        System.setProperty("java.protocol.handler.pkgs", newHandler);

        this.applicationResourceController = new ApplicationResourceController();
        this.applicationResourceController.setProperty("menu.ribbons", "false");
        final Controller controller = this.createController();

        this.createModeControllers(controller);

        FilterController.getController(controller).loadDefaultConditions();
        final Set<String> emptySet = Collections.emptySet();
        MenuInitializer.buildMenus(controller, emptySet);

    }

    /**
     * Use FEST to get Map panel by name. Not a good thing to have. Need to
     * change that.
     * 
     * @throws Exception
     */
    private void initFrame() throws Exception {
        final Controller controller = Controller.getCurrentController();
        final ModeController modeController = controller.getModeController(MModeController.MODENAME);
        controller.selectModeForBuild(modeController);

        this.viewController.init(Controller.getCurrentController());
        final JFrame frame = (JFrame) this.viewController.getFrame();
        this.window = new FrameFixture(frame);
        this.updateRghtClicks();
    }

    /**
     * Code got from freeplane sources.
     * 
     * @param controller
     * @param name
     * @throws FileNotFoundException
     * @throws XMLParseException
     * @throws MalformedURLException
     * @throws IOException
     * @throws URISyntaxException
     * @throws XMLException
     */
    public void loadMap(final Controller controller, final String... names) throws FileNotFoundException, XMLParseException,
            MalformedURLException, IOException, URISyntaxException, XMLException {
        controller.selectMode(MModeController.MODENAME);

        for (final String name : names) {
            String fileArgument = name;
            if (!FileUtils.isAbsolutePath(fileArgument)) {
                fileArgument = System.getProperty("user.dir") + System.getProperty("file.separator") + fileArgument;
            }
            final URL url = Compat.fileToUrl(new File(fileArgument));
            if (url.getPath().toLowerCase().endsWith(org.freeplane.features.url.UrlManager.FREEPLANE_FILE_EXTENSION)) {
                final MModeController modeController = (MModeController) controller.getModeController();
                final MapController mapController = modeController.getMapController();
                mapController.openMapSelectReferencedNode(url);
            }
        }
    }

    public MapModel loadMapFromFile(final File file) {

        final Controller controller = Controller.getCurrentController();
        final MModeController modeController = (MModeController) controller.getModeController(MModeController.MODENAME);
        controller.selectModeForBuild(modeController);
        controller.selectMode(MModeController.MODENAME);
        this.loadMaps(controller, file.getAbsolutePath());
        return Controller.getCurrentController().getMap();

    }

    /**
     * Part from Freeplane code
     * 
     * @param controller
     * @param args
     */
    private void loadMaps(final Controller controller, final String... args) {
        controller.selectMode(MModeController.MODENAME);
        for (final String arg : args) {
            final String fileArgument = arg;
            try {
                final URL url;
                if (fileArgument.startsWith(UrlManager.FREEPLANE_SCHEME + ':')) {
                    final String fixedUri = new FreeplaneUriConverter()
                            .fixPartiallyDecodedFreeplaneUriComingFromInternetExplorer(fileArgument);
                    LinkController.getController().loadURI(new URI(fixedUri));
                }
                else {
                    url = Compat.fileToUrl(new File(fileArgument));
                    if (url.getPath().toLowerCase().endsWith(org.freeplane.features.url.UrlManager.FREEPLANE_FILE_EXTENSION)) {
                        final MModeController modeController = (MModeController) controller.getModeController();
                        final MapController mapController = modeController.getMapController();
                        mapController.openMapSelectReferencedNode(url);
                    }
                }
            } catch (final Exception ex) {
                LOGGER.log(Level.SEVERE, "error while loading map", ex);
            }
        }
    }

    public void loadMap(final String... names) throws FileNotFoundException, XMLParseException, MalformedURLException, IOException,
            URISyntaxException, XMLException {
        this.loadMap(Controller.getCurrentController(), names);
    }

    public JPanel getSelectedMapView() {
        final Controller controller = Controller.getCurrentController();
        final ModeController modeController = controller.getModeController(MModeController.MODENAME);
        controller.selectModeForBuild(modeController);

        this.viewController.init(Controller.getCurrentController());
        return (JPanel) this.mapViewController.getMapViewComponent();
    }

    private void updateRghtClicks() {
        final Controller controller = Controller.getCurrentController();
        final ModeController modeController = controller.getModeController(MModeController.MODENAME);
        controller.selectModeForBuild(modeController);
        // Suppression of confirm on delete, was bugged
        modeController.removeAction("DeleteAction");
        modeController.addAction(new DeleteNodeAction());

        final JMenuItem it = new JMenuItem(modeController.getAction("NewChildAction"));
        final JMenuItem it2 = new JMenuItem(modeController.getAction("NewSiblingAction"));
        final JMenuItem it3 = new JMenuItem(modeController.getAction("NewPreviousSiblingAction"));
        final JMenuItem it4 = new JMenuItem(modeController.getAction("DeleteAction"));

        if (modeController.getUserInputListenerFactory().getNodePopupMenu() != null) {
            modeController.getUserInputListenerFactory().getNodePopupMenu().add(it, 0);
            modeController.getUserInputListenerFactory().getNodePopupMenu().add(it2, 1);
            modeController.getUserInputListenerFactory().getNodePopupMenu().add(it3, 2);
            modeController.getUserInputListenerFactory().getNodePopupMenu().add(it4, 3);
        }

        // Some magic numbers in the following code. Was done by looking effect
        // on right clicks.
        // NodePopupCleanUp
        final JPopupMenu nodePopupMenu = modeController.getUserInputListenerFactory().getNodePopupMenu();
        if (nodePopupMenu != null) {
            nodePopupMenu.remove(10);
            nodePopupMenu.remove(10);
            for (int i = 11; i++ < 26;) {
                nodePopupMenu.remove(11);
            }
            nodePopupMenu.remove(13);
            nodePopupMenu.remove(14);
            nodePopupMenu.remove(14);
        }
        // MapPopup cleanUp
        final JPopupMenu mapPopupMenu = modeController.getUserInputListenerFactory().getMapPopup();
        if (mapPopupMenu != null) {
            for (int i = 0; i++ < 16;) {
                mapPopupMenu.remove(0);
            }
        }
    }

    /**
     * Comme from freeplane code
     * 
     * @return
     */
    private Controller createController() {
        try {
            final Controller controller = new Controller(this.applicationResourceController);
            Controller.setCurrentController(controller);
            Compat.macAppChanges();
            controller.addAction(new QuitAction());
            this.applicationResourceController.init();
            final String lookandfeel = System.getProperty("lookandfeel", this.applicationResourceController.getProperty("lookandfeel"));
            FrameController.setLookAndFeel(lookandfeel);
            final JFrame frame;
            if (UITools.useRibbonsMenu()) {
                frame = new JRibbonFrame("Freeplane");
                IconInitializer.initIcons(this.applicationResourceController);
            }
            else {
                frame = new JFrame("Freeplane");
            }
            frame.setName(UITools.MAIN_FREEPLANE_FRAME);
            this.mapViewController = new MMapViewController(controller);
            this.viewController = new NonNPEApplicationViewController(controller, this.mapViewController, frame);
            System.setSecurityManager(new FreeplaneSecurityManager());
            this.mapViewController.addMapViewChangeListener(this.applicationResourceController.getLastOpenedList());
            FilterController.install();
            PrintController.install();
            FormatController.install(new FormatController());
            final ScannerController scannerController = new ScannerController();
            ScannerController.install(scannerController);
            scannerController.addParsersForStandardFormats();
            ModelessAttributeController.install();
            TextController.install();
            TimeController.install();
            LinkController.install();
            IconController.install();
            HelpController.install();
            controller.addAction(new UpdateCheckAction());
            controller.addAction(new NextNodeAction(Direction.FORWARD));
            controller.addAction(new NextNodeAction(Direction.BACK));
            controller.addAction(new NextNodeAction(Direction.FORWARD_N_FOLD));
            controller.addAction(new NextNodeAction(Direction.BACK_N_FOLD));
            controller.addAction(new NextPresentationItemAction());
            controller.addAction(new ShowSelectionAsRectangleAction());
            controller.addAction(new ViewLayoutTypeAction(MapViewLayout.OUTLINE));
            FilterController.getCurrentFilterController().getConditionFactory()
                    .addConditionController(70, new LogicalStyleFilterController());
            MapController.install();

            NodeHistory.install(controller);
            return controller;
        } catch (final Exception e) {
            LogUtils.severe(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * From Freeplane code.
     * 
     * @param controller
     */
    private void createModeControllers(final Controller controller) {
        MModeControllerFactory.createModeController();
        final ModeController mindMapModeController = controller.getModeController(MModeController.MODENAME);
        mindMapModeController.getMapController().addMapChangeListener(this.applicationResourceController.getLastOpenedList());
        final LastOpenedMapsRibbonContributorFactory lastOpenedMapsRibbonContributorFactory = this.applicationResourceController
                .getLastOpenedList().getLastOpenedMapsRibbonContributorFactory();
        final RibbonBuilder menuBuilder = mindMapModeController.getUserInputListenerFactory().getMenuBuilder(RibbonBuilder.class);
        menuBuilder.registerContributorFactory("lastOpenedMaps", lastOpenedMapsRibbonContributorFactory);
        mindMapModeController.addMenuContributor(FilterController.getController(controller).getMenuContributor());
        if (!UITools.useRibbonsMenu()) {
            BModeControllerFactory.createModeController();
            FModeControllerFactory.createModeController();
        }
    }

    /**
     * Get the selected map title
     * 
     * @return String
     */
    public String getMapName() {
        return Controller.getCurrentController().getMap().getTitle();
    }

    /**
     * Get map view of freeplane editor
     * 
     * @return Component, if exception is raised return a new JPanel instead
     */
    public JComponent getMapView() {
        try {
            if (this.window == null) {
                this.initFrame();
            }
            if (this.mapView == null) {
                this.mapView = this.window.panel(new MapViewMatcher(JPanel.class)).component();
            }
            return this.mapView;
        } catch (final Exception e) {
            final String msg = "Error while getting Freeplane component";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return new JPanel();
    }

    /**
     * Get icon toolbar for freeplane nodes
     * 
     * @return JToolBar, or null if Exception is raised.
     */
    public JToolBar getIconToolar() {
        try {
            if (this.window == null) {
                this.initFrame();
            }
            if (this.toolBar == null) {
                this.toolBar = this.window.toolBar(new IconToolBarMatcher(JToolBar.class)).component();
            }
            return this.toolBar;
        } catch (final Exception e) {
            final String msg = "Error while getting Freeplane component";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    /**
     * Extension of FEST Matcher to find the map panel of selected map in
     * freeplane gui.
     * 
     */
    private class MapViewMatcher extends GenericTypeMatcher<JPanel> {

        public MapViewMatcher(final Class<JPanel> supportedType) {
            super(supportedType);
        }

        @Override
        protected boolean isMatching(final JPanel component) {
            return component instanceof MapView && component.getName().equalsIgnoreCase(BasicFreeplaneAdapter.this.getMapName());
        }

    }

    /** Extension of FEST Matcher to find the icon toolbar */
    private class IconToolBarMatcher extends GenericTypeMatcher<JToolBar> {

        public IconToolBarMatcher(final Class<JToolBar> supportedType) {
            super(supportedType);
        }

        @Override
        protected boolean isMatching(final JToolBar component) {
            return component instanceof FreeplaneToolBar && "icon_toolbar".equals(component.getName());
        }
    }
}
