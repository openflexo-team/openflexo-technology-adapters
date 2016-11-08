package org.openflexo.technologyadapter.diagram.controller.action;

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoAction.PostProcessing;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.AbstractCreateFlexoConcept;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.action.CreatePaletteElementFromFlexoConcept;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareShapeInFlexoConcept;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;

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
	private String paletteElement;

	public ApplicationContext getServiceManager() {
		return wizard.getController().getApplicationContext();
	}

	@Override
	public FlexoAction<?, ?, ?> getAction() {
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

	public VirtualModel getVirtualModel() {
		if (getAction() instanceof AbstractCreateFlexoConcept) {
			return (VirtualModel) ((AbstractCreateFlexoConcept<?, ?, ?>) getAction()).getFocusedObject();
		}
		else if (getAction() instanceof DeclareShapeInFlexoConcept) {
			return ((DeclareShapeInFlexoConcept) getAction()).getVirtualModel();
		}
		else {
			logger.warning("Unexpected action " + getAction());
			return null;
		}
	}

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

	public String getPaletteElement() {
		return paletteElement;
	}

	public void setPaletteElement(String paletteElement) {
		if ((paletteElement == null && this.paletteElement != null)
				|| (paletteElement != null && !paletteElement.equals(this.paletteElement))) {
			String oldValue = this.paletteElement;
			this.paletteElement = paletteElement;
			getPropertyChangeSupport().firePropertyChange("paletteElement", oldValue, paletteElement);
			checkValidity();
		}
	}

	public abstract FlexoConcept getFlexoConcept();

	public abstract DropScheme getDropScheme();

	@Override
	public void run() {
		System.out.println("Et hop je m'occupe de la palette ");
		if (getPutToPalette()) {
			System.out.println("On met dans la palette");

			CreatePaletteElementFromFlexoConcept action = CreatePaletteElementFromFlexoConcept.actionType
					.makeNewEmbeddedAction(getFlexoConcept(), null, getAction());
			action.setDropScheme(getDropScheme());

		}
	}

}
