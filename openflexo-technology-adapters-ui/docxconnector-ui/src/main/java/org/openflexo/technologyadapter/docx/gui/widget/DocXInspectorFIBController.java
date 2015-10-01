/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Fml-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.gui.widget;

import java.util.logging.Logger;

import org.openflexo.fib.model.FIBComponent;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.doc.fml.TextBinding;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;
import org.openflexo.technologyadapter.docx.fml.action.CreateTextBinding;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;

/**
 * Represents a controller with basic FML-docX edition facilities<br>
 * Extends FlexoFIBController by supporting features relative to FML edition
 * 
 * @author sylvain
 */
public class DocXInspectorFIBController extends FlexoFIBController {

	protected static final Logger logger = FlexoLogger.getLogger(DocXInspectorFIBController.class.getPackage().getName());

	public DocXInspectorFIBController(FIBComponent component) {
		super(component);
	}

	public DocXInspectorFIBController(FIBComponent component, FlexoController controller) {
		super(component, controller);
	}

	public <D extends FlexoDocument<D, TA>, TA extends TechnologyAdapter> TextBinding<D, TA> createTextBinding(
			DocXFragmentRole fragmentRole, TextSelection<D, TA> textSelection) {
		System.out.println("createTextBinding with " + textSelection);

		CreateTextBinding createTextBinding = CreateTextBinding.actionType.makeNewAction(fragmentRole, null, getEditor());
		createTextBinding.setName("binding" + (fragmentRole.getTextBindings().size() > 0 ? fragmentRole.getTextBindings().size() : ""));
		createTextBinding.setTextSelection(textSelection);
		if ((textSelection.getStartElement() != textSelection.getEndElement())
		/*|| (textSelection.getStartRunIndex() == -1 && textSelection.getEndRunIndex() == -1
				&& textSelection.getStartCharacterIndex() == -1 && textSelection.getEndCharacterIndex() == -1)*/) {
			createTextBinding.setMultiline(true);
		}
		createTextBinding.doAction();
		return (TextBinding<D, TA>) createTextBinding.getNewTextBinding();
	}

	public <D extends FlexoDocument<D, TA>, TA extends TechnologyAdapter> boolean deleteTextBinding(TextBinding<D, TA> textBinding) {
		System.out.println("deleteTextBinding " + textBinding);
		textBinding.getFragmentRole().removeFromTextBindings(textBinding);
		return textBinding.delete();
	}

	public void moveToTop(TextBinding<?, ?> textBinding) {
		System.out.println("moveToTop " + textBinding);
	}

	public void moveUp(TextBinding<?, ?> textBinding) {
		System.out.println("moveToTop " + textBinding);
	}

	public void moveDown(TextBinding<?, ?> textBinding) {
		System.out.println("moveToTop " + textBinding);
	}

	public void moveToBottom(TextBinding<?, ?> textBinding) {
		System.out.println("moveToTop " + textBinding);
	}

	/*public Resource fibForFlexoBehaviour(FlexoBehaviour flexoBehaviour) {
		if (flexoBehaviour == null) {
			return null;
		}
		// No specific TechnologyAdapter, lookup in generic libraries
		return getFIBPanelForObject(flexoBehaviour);
	
	}*/

}
