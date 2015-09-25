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

package org.openflexo.technologyadapter.docx.gui.widget;

import java.util.logging.Logger;

import org.docx4all.swing.text.WordMLDocumentFragment;
import org.docx4all.swing.text.WordMLEditorKit;
import org.openflexo.technologyadapter.docx.model.DocXDocument;

@SuppressWarnings("serial")
public class DocXFragmentEditor extends DocXEditor {

	private static final Logger logger = Logger.getLogger(DocXFragmentEditor.class.getPackage().getName());

	private long startIndex;
	private long endIndex;

	public DocXFragmentEditor(DocXDocument document) {
		super(document, false);
	}

	public long getStartIndex() {
		return startIndex;
	}

	@CustomComponentParameter(name = "startIndex", type = CustomComponentParameter.Type.OPTIONAL)
	public void setStartIndex(long startIndex) {

		System.out.println("startIndex = " + startIndex);

		if (startIndex != this.startIndex) {
			long oldValue = this.startIndex;
			this.startIndex = startIndex;
			// getPropertyChangeSupport().firePropertyChange("startIndex", oldValue, startIndex);
		}
	}

	public long getEndIndex() {
		return endIndex;
	}

	@CustomComponentParameter(name = "endIndex", type = CustomComponentParameter.Type.OPTIONAL)
	public void setEndIndex(long endIndex) {

		System.out.println("endIndex = " + endIndex);

		if (endIndex != this.endIndex) {
			long oldValue = this.endIndex;
			this.endIndex = endIndex;
			// getPropertyChangeSupport().firePropertyChange("endIndex", oldValue, endIndex);
		}
	}

	@CustomComponentParameter(name = "serviceManager", type = CustomComponentParameter.Type.OPTIONAL)
	@Override
	protected WordMLDocumentFragment openDocument(WordMLEditorKit editorKit) {
		Thread.dumpStack();
		return editorKit.openDocumentFragment(document.getWordprocessingMLPackage(), getObjectFactory(), 6, 10 /*(int) getStartIndex(),
																												(int) getEndIndex()*/);
	}

}
