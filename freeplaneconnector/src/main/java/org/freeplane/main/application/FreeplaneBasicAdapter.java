package org.freeplane.main.application;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import org.freeplane.core.ui.ShowSelectionAsRectangleAction;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.ui.ribbon.RibbonBuilder;
import org.freeplane.core.util.Compat;
import org.freeplane.core.util.LogUtils;
import org.freeplane.features.attribute.ModelessAttributeController;
import org.freeplane.features.filter.FilterController;
import org.freeplane.features.filter.NextNodeAction;
import org.freeplane.features.filter.NextPresentationItemAction;
import org.freeplane.features.format.FormatController;
import org.freeplane.features.format.ScannerController;
import org.freeplane.features.help.HelpController;
import org.freeplane.features.icon.IconController;
import org.freeplane.features.icon.mindmapmode.MIconController;
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
import org.freeplane.view.swing.features.nodehistory.NodeHistory;
import org.freeplane.view.swing.map.ViewLayoutTypeAction;
import org.freeplane.view.swing.map.mindmapmode.MMapViewController;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;

/**
 * Most controllers initialization come from FreeplaneGUIStarter. Each concerned method is commented for that.<br> <br> The purpose of this
 * class is to provide a singleton to allow display of freeplane mind map in Openflexo.<br> Public access by getInstance()
 *
 * @author eloubout
 */
public class FreeplaneBasicAdapter {

	private static final Logger LOGGER = Logger.getLogger(FreeplaneBasicAdapter.class.getName());

	private static final FreeplaneBasicAdapter INSTANCE = new FreeplaneBasicAdapter();

	/**
	 * Public accessor to instance of this class.
	 *
	 * @return current instance. Will never return null.
	 */
	public static FreeplaneBasicAdapter getInstance() {
		return INSTANCE;
	}

	/**
	 * Private constructor, that init what is needed for further use.
	 */
	private FreeplaneBasicAdapter() {
		final String oldHandler = System.getProperty("java.protocol.handler.pkgs");
		String newHandler = "org.freeplane.main.application.protocols";
		if (oldHandler != null) {
			newHandler = oldHandler + '|' + newHandler;
		}
		System.setProperty("java.protocol.handler.pkgs", newHandler);

		this.applicationResourceController = new ApplicationResourceController();
		final Controller controller = this.createController();

		this.createModeControllers(controller);

		FilterController.getController(controller).loadDefaultConditions();
		final Set<String> emptySet = Collections.emptySet();
		MenuInitializer.buildMenus(controller, emptySet);

		this.updateActions();
	}

	private final ApplicationResourceController applicationResourceController;

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
	 * @param controller   freeplane controller
	 * @param fileArgument file name.
	 */
	private void loadMaps(final Controller controller, final String fileArgument) {
		controller.selectMode(MModeController.MODENAME);
		try {
			final URL url;
			if (fileArgument.startsWith(UrlManager.FREEPLANE_SCHEME + ':')) {
				final String fixedUri = new FreeplaneUriConverter().fixPartiallyDecodedFreeplaneUriComingFromInternetExplorer(fileArgument);
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

	private void updateActions() {
		final Controller controller = Controller.getCurrentController();
		final ModeController modeController = controller.getModeController(MModeController.MODENAME);
		controller.selectModeForBuild(modeController);
		// Suppression of confirm on delete, was bugged
		modeController.removeAction("DeleteAction");
		modeController.addAction(new DeleteNodeAction());

		// MapPopup cleanUp
		final JPopupMenu mapPopupMenu = modeController.getUserInputListenerFactory().getMapPopup();
		if (mapPopupMenu != null) {
			for (int i = 0; i++ < 16; ) {
				mapPopupMenu.remove(0);
			}
		}
	}

	/**
	 * Come from freeplane code. Initialization of Freeplane resources.
	 *
	 * @return Controller created.
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
			MMapViewController mapViewController = new MMapViewController(controller);
			new NonNPEApplicationViewController(controller, mapViewController, frame);
			System.setSecurityManager(new FreeplaneSecurityManager());
			mapViewController.addMapViewChangeListener(this.applicationResourceController.getLastOpenedList());
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
	 * From Freeplane code. Initialization for MindMap editing.
	 *
	 * @param controller freeplane controller
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
	 * @return JComponent, Parent need to be a JViewport
	 */
	public JComponent getMapView() {
		return (JComponent) Controller.getCurrentController().getMapViewManager().getMapViewComponent();
	}

	/**
	 * Get icon toolbar for freeplane nodes
	 *
	 * @return JScrollPane, empty one if exception is raised.
	 */
	public JScrollPane getIconToolbar() {
		final String errorMsg = "Error while retrieving Freeplane IconToolbar";
		try {
			final Field f = MIconController.class.getDeclaredField("iconToolBar");
			f.setAccessible(true);
			final JToolBar bar = (JToolBar) f.get(Controller.getCurrentModeController().getExtension(IconController.class));
			return new JScrollPane(bar);
		} catch (final Exception e) {
			LOGGER.log(Level.SEVERE, errorMsg, e);
		}
		return new JScrollPane();
	}
}
