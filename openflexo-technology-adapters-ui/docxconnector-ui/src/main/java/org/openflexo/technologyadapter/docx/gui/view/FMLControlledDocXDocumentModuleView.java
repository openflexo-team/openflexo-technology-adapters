/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.docx.gui.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.openflexo.components.doc.editorkit.FlexoDocumentEditor;
import org.openflexo.components.doc.editorkit.widget.FIBFlexoDocumentBrowser;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionSource;
import org.openflexo.foundation.doc.FlexoDocObject;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.doc.nature.FMLControlledDocumentVirtualModelInstanceNature;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.controller.DocXAdapterController;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXObject;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelInstanceNature;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.listener.FlexoActionButton;

/**
 * A {@link ModuleView} for a federated document inside a {@link FMLRTVirtualModelInstance}<br>
 * It is stated that the related {@link FMLRTVirtualModelInstance} has the {@link FMLControlledDocumentVirtualModelInstanceNature}
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class FMLControlledDocXDocumentModuleView extends JPanel implements ModuleView<FMLRTVirtualModelInstance>,
		FlexoActionSource, PropertyChangeListener {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FMLControlledDocXDocumentModuleView.class.getPackage().getName());

	private final FMLRTVirtualModelInstance virtualModelInstance;
	private final FlexoPerspective perspective;

	private final FlexoDocumentEditor<DocXDocument, DocXTechnologyAdapter> editor;

	private final FIBFlexoDocumentBrowser browser;
	private final JPanel topPanel;

	public FMLControlledDocXDocumentModuleView(FMLRTVirtualModelInstance virtualModelInstance, FlexoPerspective perspective) {
		super();
		setLayout(new BorderLayout());
		this.virtualModelInstance = virtualModelInstance;
		this.perspective = perspective;

		if (!virtualModelInstance.hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE)) {
			logger.severe("Supplied FMLRTVirtualModelInstance does not have the FMLControlledDocXVirtualModelInstanceNature");
		}

		editor = new FlexoDocumentEditor<>(getDocument());
		add(editor.getEditorPanel(), BorderLayout.CENTER);

		browser = new FIBFlexoDocumentBrowser(getDocument(), perspective.getController()) {
			@Override
			public void setSelectedDocumentElement(FlexoDocObject<?, ?> selected) {
				super.setSelectedDocumentElement(selected);
				if (selected instanceof DocXObject) {
					selectElementInDocumentEditor((DocXObject<?>) selected);
				}
			}
		};
		add(browser, BorderLayout.EAST);

		topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		for (ActionScheme actionScheme : virtualModelInstance.getVirtualModel().getActionSchemes()) {
			FlexoActionFactory<ActionSchemeAction, FlexoConceptInstance, VirtualModelInstanceObject> actionType = new ActionSchemeActionFactory(
					actionScheme, virtualModelInstance);
			topPanel.add(new FlexoActionButton(actionType, this, perspective.getController()));
		}
		add(topPanel, BorderLayout.NORTH);

		validate();

		getRepresentedObject().getPropertyChangeSupport().addPropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
	}

	public FMLRTVirtualModelInstance getVirtualModelInstance() {
		return virtualModelInstance;
	}

	public DocXDocument getDocument() {
		return FMLControlledDocXVirtualModelInstanceNature.getDocument(getVirtualModelInstance());
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public void deleteModuleView() {
		getRepresentedObject().getPropertyChangeSupport().removePropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
		perspective.getController().removeModuleView(this);
		// TODO: delete docx editor
	}

	@Override
	public FMLRTVirtualModelInstance getRepresentedObject() {
		return getVirtualModelInstance();
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void willHide() {
		System.out.println("FMLControlledDocXDocumentModuleView WILL HIDE !!!!!!");
	}

	@Override
	public void willShow() {
		System.out.println("FMLControlledDocXDocumentModuleView WILL SHOW !!!!!!");
		getPerspective().focusOnObject(getRepresentedObject());
	}

	public DocXAdapterController getDocXAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DocXAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		perspective.setTopRightView(null);
		perspective.setBottomRightView(null);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(false);
			}
		});

		controller.getControllerModel().setRightViewVisible(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getRepresentedObject() && evt.getPropertyName().equals(getRepresentedObject().getDeletedProperty())) {
			deleteModuleView();
		}
	}

	@Override
	public FlexoConceptInstance getFocusedObject() {
		return getVirtualModelInstance();
	}

	@Override
	public List<VirtualModelInstanceObject> getGlobalSelection() {
		return null;
	}

	@Override
	public FlexoEditor getEditor() {
		return perspective.getController().getEditor();
	}

	protected void selectElementInDocumentEditor(DocXObject<?> element) {

		System.out.println("****************** selectElementInDocumentEditor with " + element);

		try {

			editor.highlight(element);
			scrollTo(element, editor);

			// List<DocXElement> fragmentElements = fragment.getElements();

			/*final List<AbstractDocumentElement<?, ?, ?>> elts = new ArrayList<>();
			
			AbstractDocumentElement<?, ?, ?> docElement = null;
			
			if (element instanceof DocXParagraph) {
				docElement = editor.getElement((element));
				docElement = editor.getElement((element));
				elts.add(docElement);
			}
			if (element instanceof DocXTable) {
				docElement = editor.getElement((element));
				elts.add(docElement);
			}
			
			// Thread.dumpStack();
			editor.setSelectedElements((List) elts);
			
			if (docElement != null) {
				scrollTo(docElement);
			}*/

		} catch (Exception e) {
			e.printStackTrace();
		}

		editor.getEditorPanel().revalidate();
		editor.getEditorPanel().repaint();

	}

	private <D extends FlexoDocument<D, TA>, TA extends TechnologyAdapter> void scrollTo(FlexoDocObject<D, TA> object,
			FlexoDocumentEditor<D, TA> docXEditor) {
		if (!docXEditor.scrollTo(object, true)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					scrollTo(object, docXEditor);
				}
			});
		}
	}

}
