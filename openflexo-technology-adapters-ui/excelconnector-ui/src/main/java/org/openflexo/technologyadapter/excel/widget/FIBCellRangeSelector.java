/**
 * 
 * Copyright (c) 2014-2017, Openflexo
 * 
 * This file is part of Flexo-Documentation-UI, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * Please not that some parts of that component are freely inspired from
 * Stanislav Lapitsky code (see http://java-sl.com/docx_editor_kit.html)
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

package org.openflexo.technologyadapter.excel.widget;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.components.widget.FIBFlexoObjectSelector;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.listener.FIBSelectionListener;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.view.widget.FIBCustomWidget;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

/**
 * Widget allowing to select an {@link FlexoDocFragment} inside a {@link FlexoDocument}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBCellRangeSelector extends FIBFlexoObjectSelector<ExcelCellRange> {

	static final Logger logger = Logger.getLogger(FIBCellRangeSelector.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBCellRangeSelector.fib");

	private ExcelWorkbook workbook;

	public FIBCellRangeSelector(ExcelCellRange editedObject) {
		super(editedObject);
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	@Override
	public Class<ExcelCellRange> getRepresentedType() {
		return ExcelCellRange.class;
	}

	public ExcelWorkbook getExcelWorkbook() {
		return workbook;
	}

	@CustomComponentParameter(name = "excelWorkbook", type = CustomComponentParameter.Type.MANDATORY)
	public void setExcelWorkbook(ExcelWorkbook workbook) {
		if ((workbook == null && this.workbook != null) || (workbook != null && !workbook.equals(this.workbook))) {
			ExcelWorkbook oldValue = this.workbook;
			this.workbook = workbook;
			getPropertyChangeSupport().firePropertyChange("workbook", oldValue, workbook);
		}
	}

	@Override
	public String renderedString(ExcelCellRange editedObject) {
		if (editedObject != null) {
			return editedObject.getSerializationIdentifier();
		}
		return "";
	}

	/*private void updateWith(List<FlexoDocElement<D, TA>> elements) {
	
		if (document == null) {
			logger.warning("No document defined in FIBDocFragmentSelector");
			return;
		}
	
		F newFragment = null;
		if (elements.size() == 0) {
			newFragment = null;
		}
		else if (elements.size() == 1) {
			FlexoDocElement<D, TA> startElement = elements.get(0);
			try {
				newFragment = (F) document.getFragment(startElement, startElement);
			} catch (FragmentConsistencyException e) {
				e.printStackTrace();
			}
		}
		else {
	
			FlexoDocElement<D, TA> startElement = elements.get(0);
			FlexoDocElement<D, TA> endElement = elements.get(elements.size() - 1);
			try {
				newFragment = (F) document.getFragment(startElement, endElement);
			} catch (FragmentConsistencyException e) {
				System.out.println("This fragment is not valid: start=" + startElement + " end=" + endElement);
				newFragment = null;
			}
	
		}
		// System.out.println("fragment=" + newFragment);
		setEditedObject(newFragment);
	}*/

	private boolean isSelecting = false;

	@Override
	protected RangeSelectorDetailsPanel makeCustomPanel(ExcelCellRange editedObject) {

		RangeSelectorDetailsPanel returned = null;

		if (getServiceManager() != null && getServiceManager().getTaskManager() != null) {
			LoadEditor task = new LoadEditor(editedObject);
			getServiceManager().getTaskManager().scheduleExecution(task);
			getServiceManager().getTaskManager().waitTask(task);
			returned = task.getPanel();
		}
		if (returned == null) {
			returned = new RangeSelectorDetailsPanel(editedObject);
		}

		FIBCustomWidget<?, ?, ?> documentEditorWidget = returned.getWorkbookEditorWidget();
		ExcelWorkbookEditorWidget wbEditor = (ExcelWorkbookEditorWidget) documentEditorWidget.getCustomComponent();
		/*wbEditor.getJEditorPane().addCaretListener(new ExcelWorkbookEditorWidget.FlexoDocumentSelectionListener(wbEditor) {
			@Override
			public void caretUpdate(CaretEvent evt) {
		
				if (isSelecting) {
					return;
				}
		
				super.caretUpdate(evt);
		
				System.out.println("Caret changed with " + evt);
				int start = Math.min(evt.getDot(), evt.getMark());
				int end = Math.max(evt.getDot(), evt.getMark());
				System.out.println("Selection: " + start + ":" + end);
		
				// Better ???
				// int startLocation = getEditor().getJEditorPane().getSelectionStart();
				// int endLocation = getEditor().getJEditorPane().getSelectionEnd();
		
				// If selection is not empty, reduce the selection to be sure to be in a not implied run
				if (start > end) {
					end = end - 1;
				}
		
				FlexoStyledDocument<?, ?> styledDocument = docXEditor.getEditor().getStyledDocument();
		
				Element startCharElement = styledDocument.getCharacterElement(start);
				Element startParElement = styledDocument.getParagraphElement(start);
				System.out.println("startCharElement: " + startCharElement);
				System.out.println("startParElement: " + startParElement);
		
				Element endCharElement = styledDocument.getCharacterElement(end);
				Element endParElement = styledDocument.getParagraphElement(end);
				System.out.println("endCharElement: " + endCharElement);
				System.out.println("endParElement: " + endParElement);
		
				FlexoDocElement startElement = null;
				FlexoDocElement endElement = null;
		
				if (startParElement instanceof AbstractDocumentElement
						&& ((AbstractDocumentElement<?, ?, ?>) startParElement).getDocObject() instanceof FlexoDocElement) {
					startElement = (FlexoDocElement<?, ?>) ((AbstractDocumentElement<?, ?, ?>) startParElement).getDocObject();
				}
		
				if (endParElement instanceof AbstractDocumentElement
						&& ((AbstractDocumentElement<?, ?, ?>) endParElement).getDocObject() instanceof FlexoDocElement) {
					endElement = (FlexoDocElement<?, ?>) ((AbstractDocumentElement<?, ?, ?>) endParElement).getDocObject();
				}
		
				F newFragment = null;
		
				if (startElement != null && endElement != null) {
		
					try {
						newFragment = (F) getDocument().getFragment(startElement, endElement);
					} catch (FragmentConsistencyException exception) {
						System.out.println("This fragment is not valid: start=" + startElement + " end=" + endElement);
						newFragment = null;
					}
		
				}
		
				System.out.println("fragment=" + newFragment);
		
				isSelecting = true;
				setEditedObject(newFragment);
				isSelecting = false;
		
			}
		});*/

		/*if (editedObject != null) {
			selectFragmentInDocumentEditor(editedObject, documentEditorWidget);
		}*/

		return returned;
	}

	@Override
	protected CellRangeSelectorFIBController makeCustomFIBController(FIBComponent fibComponent) {
		return new CellRangeSelectorFIBController(fibComponent, this);
	}

	@Override
	protected CellRangeSelectorFIBController getController() {
		return (CellRangeSelectorFIBController) super.getController();
	}

	@Override
	public RangeSelectorDetailsPanel getCustomPanel() {
		return (RangeSelectorDetailsPanel) super.getCustomPanel();
	}

	public static class CellRangeSelectorFIBController extends SelectorFIBController {
		public CellRangeSelectorFIBController(final FIBComponent component, final FIBCellRangeSelector selector) {
			super(component, selector);
			addSelectionListener(new FIBSelectionListener() {
				@Override
				public void selectionChanged(List<Object> selection) {
					/*List<FlexoDocElement<?, ?>> elements = new ArrayList<>();
					FlexoDocument<?, ?> doc = null;
					if (selection != null) {
						for (Object o : selection) {
							if (o instanceof FlexoDocElement && ((FlexoDocElement) o).getFlexoDocument() != null) {
								if (doc == null) {
									doc = ((FlexoDocElement) o).getFlexoDocument();
								}
								if (doc == ((FlexoDocElement) o).getFlexoDocument()) {
									elements.add((FlexoDocElement<?, ?>) o);
								}
							}
						}
					}
					final FlexoDocument<?, ?> docReference = doc;
					Collections.sort(elements, new Comparator<FlexoDocElement>() {
						@Override
						public int compare(FlexoDocElement o1, FlexoDocElement o2) {
							return docReference.getElements().indexOf(o1) - docReference.getElements().indexOf(o2);
						}
					});
					selector.updateWith(elements);*/
				}
			});
		}
	}

	public class RangeSelectorDetailsPanel extends SelectorDetailsPanel {

		private FIBCustomWidget<?, ?, ?> wbEditorWidget = null;

		protected RangeSelectorDetailsPanel(ExcelCellRange anObject) {
			super(anObject);
			update();
		}

		public FIBCustomWidget<?, ?, ?> getWorkbookEditorWidget() {
			if (wbEditorWidget == null) {
				wbEditorWidget = retrieveWorkbookEditorWidget();
			}
			return wbEditorWidget;
		}

		private FIBCustomWidget<?, ?, ?> retrieveWorkbookEditorWidget() {
			List<FIBComponent> listComponent = getFIBComponent().getAllSubComponents();
			for (FIBComponent c : listComponent) {
				if (c instanceof FIBCustom) {
					return (FIBCustomWidget) getController().viewForComponent(c);
				}
			}
			return null;
		}

		@Override
		public CellRangeSelectorFIBController getController() {
			return (CellRangeSelectorFIBController) super.getController();
		}

		// Called whenever the FragmentSelectorDetailsPanel should reflect a fragment selection
		// The browser must reflect the selection and FlexoDocumentEditorWidget should highlight selected fragment
		/*@Override
		protected void selectValue(F value) {
		
			// First notify selectedDocumentElements so that the browser will be notified
			// for its selection to reflect selected fragment
			if (value == null) {
				FIBCellRangeSelector.this.getPropertyChangeSupport().firePropertyChange("selectedDocumentElements", false, null);
			}
			else {
				FIBCellRangeSelector.this.getPropertyChangeSupport().firePropertyChange("selectedDocumentElements", null,
						value.getElements());
			}
		
			selectFragmentInDocumentEditor(value, getDocEditorWidget());
		}*/

	}

	/*public List<? extends FlexoDocElement<D, TA>> getSelectedDocumentElements() {
		if (getEditedObject() != null) {
			return getEditedObject().getElements();
		}
		return Collections.emptyList();
	}
	
	public void setSelectedDocumentElements(List<? extends FlexoDocElement<D, TA>> selection) {
		getPropertyChangeSupport().firePropertyChange("selectedDocumentElements", null, selection);
	}
	
	protected void selectFragmentInDocumentEditor(F fragment, FIBCustomWidget<?, ?, ?> documentEditorWidget) {
	
		// System.out.println("customPanel" + getCustomPanel());
		// System.out.println("docEditorWidget=" + getCustomPanel().getDocEditorWidget());
	
		final ExcelWorkbookEditorWidget docXEditor = (ExcelWorkbookEditorWidget) documentEditorWidget.getCustomComponent();
	
		try {
	
			if (fragment != null) {
				isSelecting = true;
				docXEditor.getEditor().highlightObjects(fragment.getElements());
				scrollTo(fragment.getStartElement(), docXEditor);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		docXEditor.getJEditorPane().revalidate();
		docXEditor.getJEditorPane().repaint();
	
	}*/

	/*private void scrollTo(FlexoDocObject object, ExcelWorkbookEditorWidget docXEditor) {
		if (!docXEditor.getEditor().scrollTo(object, false)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					scrollTo(object, docXEditor);
				}
			});
			isSelecting = true;
			return;
		}
		isSelecting = false;
	}*/

	public class LoadEditor extends FlexoTask {

		private final ExcelCellRange fragment;
		private RangeSelectorDetailsPanel panel;

		public LoadEditor(ExcelCellRange fragment) {
			super(FlexoLocalization.getMainLocalizer().localizedForKey("opening_document_editor"));
			this.fragment = fragment;
		}

		@Override
		public void performTask() throws InterruptedException {
			setExpectedProgressSteps(10);
			// panel = makeCustomPanel(fragment);
			panel = new RangeSelectorDetailsPanel(fragment);
		}

		public RangeSelectorDetailsPanel getPanel() {
			return panel;
		}
	}

}
