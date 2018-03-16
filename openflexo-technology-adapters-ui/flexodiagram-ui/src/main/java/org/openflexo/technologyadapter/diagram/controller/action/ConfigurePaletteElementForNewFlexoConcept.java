package org.openflexo.technologyadapter.diagram.controller.action;

import java.util.List;
import java.util.logging.Logger;
import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.diana.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoAction.PostProcessing;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.action.CreatePaletteElementFromFlexoConcept;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.PropertyChangedSupportDefaultImplementation;

/**
 * This step is used to define some properties to be created for new {@link FlexoConcept}
 * 
 * @author sylvain
 *
 */
@FIBPanel("Fib/Wizard/CreateFMLElement/ConfigurePaletteElementForNewFlexoConcept.fib")
public abstract class ConfigurePaletteElementForNewFlexoConcept<A extends FlexoAction<A, T1, T2>, T1 extends FlexoObject, T2 extends FlexoObject>
		extends WizardStep implements PostProcessing {

	private static final Logger logger = Logger.getLogger(ConfigurePaletteElementForNewFlexoConcept.class.getPackage().getName());

	private final FlexoWizard wizard;
	private final A action;

	/**
	 * @param createFMLControlledDiagramFlexoConceptWizard
	 */
	ConfigurePaletteElementForNewFlexoConcept(A action, FlexoWizard wizard) {
		this.wizard = wizard;
		this.action = action;
	}

	private boolean putToPalette = true;
	private DiagramPalette diagramPalette;
	private String paletteElementName;

	public ApplicationContext getServiceManager() {
		return wizard.getController().getApplicationContext();
	}

	@Override
	public A getAction() {
		return action;
	}

	@Override
	public String getTitle() {
		return getAction().getLocales().localizedForKey("configure_palette_element");
	}

	@Override
	public boolean isValid() {
		/*	if (getPalette() == null) {
				setIssueMessage(noPaletteSelectedMessage(), IssueMessageType.ERROR);
				return false;
			}
		
			if (StringUtils.isEmpty(getNewElementName())) {
				setIssueMessage(noNameMessage(), IssueMessageType.ERROR);
				return false;
			}
		
			if (getPalette().getPaletteElement(getNewElementName()) != null) {
				setIssueMessage(duplicatedPaletteElement(), IssueMessageType.ERROR);
				return false;
			}
		
			if (getConfigureFMLControls()) {
				if (getVirtualModel() == null) {
					setIssueMessage(noVirtualModelSelectedMessage(), IssueMessageType.ERROR);
					return false;
				}
				if (getFlexoConcept() == null) {
					setIssueMessage(noFlexoConceptSelectedMessage(), IssueMessageType.ERROR);
					return false;
				}
		
				if (getDropScheme() == null) {
					setIssueMessage(noDropSchemeSelectedMessage(), IssueMessageType.ERROR);
					return false;
				}
		
			}*/

		return true;
	}

	public boolean getPutToPalette() {
		return putToPalette;
	}

	public void setPutToPalette(boolean putToPalette) {
		if (putToPalette != this.putToPalette) {
			this.putToPalette = putToPalette;
			getPropertyChangeSupport().firePropertyChange("putToPalette", !putToPalette, putToPalette);
			checkValidity();
		}
	}

	public abstract VirtualModel getVirtualModel();/* {
													if (getFlexoConcept() != null && getFlexoConcept().getOwningVirtualModel() instanceof VirtualModel) {
													return (VirtualModel) getFlexoConcept().getOwningVirtualModel();
													}
													return null;
													}*/

	/*if (getAction() instanceof AbstractCreateFlexoConcept) {
	return (VirtualModel) ((AbstractCreateFlexoConcept<?, ?, ?>) getAction()).getFocusedObject();
	}
	else if (getAction() instanceof DeclareShapeInFlexoConcept) {
	return ((DeclareShapeInFlexoConcept) getAction()).getVirtualModel();
	}
	else {
	logger.warning("Unexpected action " + getAction());
	return null;
	}*/

	public DiagramSpecification getDiagramSpecification() {
		if (getVirtualModel() != null && getVirtualModel().hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
			return FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(getVirtualModel()).getDiagramSpecification();
		}
		return null;
	}

	public DiagramPalette getPalette() {
		return diagramPalette;
	}

	public void setPalette(DiagramPalette palette) {
		if (palette != getPalette()) {
			DiagramPalette oldValue = getPalette();
			diagramPalette = palette;
			getPropertyChangeSupport().firePropertyChange("palette", oldValue, palette);
			checkValidity();
		}
	}

	public String getPaletteElementName() {
		if (paletteElementName == null) {
			return getDefaultPaletteElementName();
		}
		return paletteElementName;
	}

	public void setPaletteElementName(String paletteElement) {
		if ((paletteElement == null && this.paletteElementName != null)
				|| (paletteElement != null && !paletteElement.equals(this.paletteElementName))) {
			String oldValue = this.paletteElementName;
			this.paletteElementName = paletteElement;
			getPropertyChangeSupport().firePropertyChange("paletteElement", oldValue, paletteElement);
			checkValidity();
		}
	}

	private boolean takeScreenshotForTopLevelElement;
	private boolean overrideDefaultGraphicalRepresentations;

	public boolean takeScreenshotForTopLevelElement() {
		return takeScreenshotForTopLevelElement;
	}

	public void setTakeScreenshotForTopLevelElement(boolean takeScreenshotForTopLevelElement) {
		if (takeScreenshotForTopLevelElement != takeScreenshotForTopLevelElement()) {
			this.takeScreenshotForTopLevelElement = takeScreenshotForTopLevelElement;
			getPropertyChangeSupport().firePropertyChange("takeScreenshotForTopLevelElement", !takeScreenshotForTopLevelElement,
					takeScreenshotForTopLevelElement);
			if (takeScreenshotForTopLevelElement) {
				getScreenshot();
			}
			checkValidity();
		}
	}

	public boolean overrideDefaultGraphicalRepresentations() {
		return overrideDefaultGraphicalRepresentations;
	}

	public void setOverrideDefaultGraphicalRepresentations(boolean overrideDefaultGraphicalRepresentations) {
		if (overrideDefaultGraphicalRepresentations != overrideDefaultGraphicalRepresentations()) {
			this.overrideDefaultGraphicalRepresentations = overrideDefaultGraphicalRepresentations;
			getPropertyChangeSupport().firePropertyChange("overrideDefaultGraphicalRepresentations",
					!overrideDefaultGraphicalRepresentations, overrideDefaultGraphicalRepresentations);
			checkValidity();
		}
	}

	public VirtualModelResource getVirtualModelResource() {
		if (getVirtualModel() != null && getVirtualModel().getResource() instanceof VirtualModelResource) {
			return (VirtualModelResource) getVirtualModel().getResource();
		}
		return null;
	}

	public abstract FlexoConcept getFlexoConcept();

	public abstract String getFlexoConceptName();

	public abstract DropScheme getDropScheme();

	public abstract String getDropSchemeName();

	public abstract String getDefaultPaletteElementName();

	@Override
	public void run() {
		if (getPutToPalette()) {
			CreatePaletteElementFromFlexoConcept action = CreatePaletteElementFromFlexoConcept.actionType
					.makeNewEmbeddedAction(getFlexoConcept(), null, getAction());
			action.setPalette(getPalette());
			action.setVirtualModel(getVirtualModel());
			action.setFlexoConcept(getFlexoConcept());
			action.setDropScheme(getDropScheme());
			action.setNewElementName(getPaletteElementName());
			action.setConfigureFMLControls(true);
			action.setTakeScreenshotForTopLevelElement(takeScreenshotForTopLevelElement());
			if (takeScreenshotForTopLevelElement()) {
				action.setScreenshot(getScreenshot());
			}
			action.setOverrideDefaultGraphicalRepresentations(overrideDefaultGraphicalRepresentations());
			if (overrideDefaultGraphicalRepresentations()) {
				// TODO: handle graphical entries
			}

			if (action.isValid()) {
				action.doAction();
			}
			else {
				logger.warning("Invalid action: " + action);
			}

		}
	}

	/*@Override
	protected void updateDiagramElementsEntries() {
		super.updateDiagramElementsEntries();
	
		for (GraphicalElementRole<?, ?> elementRole : getFocusedObject().getAccessibleProperties(GraphicalElementRole.class)) {
			diagramElementEntries.add(new RoleEntry(elementRole));
		}
	}*/

	private ScreenshotImage<DiagramShape> screenshot;
	private int imageWidth, imageHeight;

	public ScreenshotImage<DiagramShape> getScreenshot() {
		if (screenshot == null) {
			screenshot = makeScreenshot();
			getPropertyChangeSupport().firePropertyChange("screenshot", null, screenshot);
			if (screenshot != null) {
				imageWidth = screenshot.image.getWidth(null);
				getPropertyChangeSupport().firePropertyChange("imageWidth", null, imageWidth);
				imageHeight = screenshot.image.getHeight(null);
				getPropertyChangeSupport().firePropertyChange("imageHeight", null, imageHeight);
			}
		}
		return screenshot;
	}

	public abstract ScreenshotImage<DiagramShape> makeScreenshot();

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	/*@Override
	public ShapeGraphicalRepresentation makePaletteElementGraphicalRepresentation() {
		// TODO improve this
		if (getFlexoConcept() != null && getFlexoConcept().getAccessibleProperties(ShapeRole.class).size() > 0) {
			return getFlexoConcept().getAccessibleProperties(ShapeRole.class).get(0).getGraphicalRepresentation();
		}
		return null;
	}*/

	/*public RoleEntry getEntry(GraphicalElementRole<?, ?> role) {
		for (GraphicalElementEntry e : diagramElementEntries) {
			if (((RoleEntry) e).getFlexoRole() == role) {
				return (RoleEntry) e;
			}
		}
		return null;
	}*/

	public abstract List<? extends GraphicalElementEntry> getGraphicalElementEntries();

	public static abstract class GraphicalElementEntry extends PropertyChangedSupportDefaultImplementation {
		private boolean selectThis = true;
		private String elementName;
		// private GraphicalElementRole<?, ?> flexoRole;

		public GraphicalElementEntry(String elementName) {
			super();
			this.elementName = elementName;
			// this.selectThis = isMainEntry();
		}

		public String getElementName() {
			return elementName;
		}

		public void setElementName(String elementName) {
			if ((elementName == null && this.elementName != null) || (elementName != null && !elementName.equals(this.elementName))) {
				String oldValue = this.elementName;
				this.elementName = elementName;
				getPropertyChangeSupport().firePropertyChange("elementName", oldValue, elementName);
			}
		}

		/*public GraphicalElementRole<?, ?> getFlexoRole() {
			return flexoRole;
		}
		
		public void setFlexoRole(GraphicalElementRole<?, ?> flexoRole) {
			if ((flexoRole == null && this.flexoRole != null) || (flexoRole != null && !flexoRole.equals(this.flexoRole))) {
				GraphicalElementRole<?, ?> oldValue = this.flexoRole;
				this.flexoRole = flexoRole;
				getPropertyChangeSupport().firePropertyChange("flexoRole", oldValue, flexoRole);
			}
		}*/

		// public abstract boolean isMainEntry();

		public boolean getSelectThis() {
			return selectThis;
		}

		public void setSelectThis(boolean aFlag) {
			selectThis = aFlag;
		}

		public abstract GraphicalElementEntry getParentEntry();

		// public abstract List<? extends GraphicalElementRole<?, ?>> getAvailableFlexoRoles();
	}

	/*public class RoleEntry extends GraphicalElementEntry {
		private boolean selectThis = true;
	
		public RoleEntry(GraphicalElementRole<?, ?> role) {
			super(role.getRoleName());
			setFlexoRole(role);
		}
	
		@Override
		public boolean isMainEntry() {
			return true;
		}
	
		@Override
		public boolean getSelectThis() {
			return selectThis;
		}
	
		@Override
		public void setSelectThis(boolean aFlag) {
			selectThis = aFlag;
		}
	
		@Override
		public RoleEntry getParentEntry() {
			if (getFlexoRole() instanceof ShapeRole) {
				return getEntry(((ShapeRole) getFlexoRole()).getParentShapeRole());
			}
			return null;
		}
	
		@Override
		public List<? extends GraphicalElementRole<?, ?>> getAvailableFlexoRoles() {
			if (getFlexoConcept() != null) {
				return (List) getFlexoConcept().getAccessibleProperties(GraphicalElementRole.class);
			}
			return null;
		}
	}*/

}
