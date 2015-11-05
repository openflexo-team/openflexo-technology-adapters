/**
 * 
 * Copyright (c) 2014, Openflexo
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
import org.openflexo.fib.view.GinaViewFactory;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXObject;
import org.openflexo.view.FIBBrowserView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;

/**
 * Browser allowing to browse a DocXDocument<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBDocXDocumentBrowser extends FIBBrowserView<DocXDocument> {
	static final Logger logger = Logger.getLogger(FIBDocXDocumentBrowser.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBDocXDocumentBrowser.fib");

	public FIBDocXDocumentBrowser(DocXDocument document, FlexoController controller) {
		super(document, controller, FIB_FILE);
		if (getFIBController() instanceof DocXDocumentBrowserFIBController) {
			((DocXDocumentBrowserFIBController) getFIBController()).setBrowser(this);
		}
	}

	private DocXObject selectedElement;

	public DocXObject getSelectedDocumentElement() {
		return selectedElement;
	}

	public void setSelectedDocumentElement(DocXObject selected) {
		selectedElement = selected;
	}

	public static class DocXDocumentBrowserFIBController extends FlexoFIBController {
		private FIBDocXDocumentBrowser browser;

		public DocXDocumentBrowserFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
			super(component, viewFactory);
		}

		private void setBrowser(FIBDocXDocumentBrowser browser) {
			this.browser = browser;
		}

		public DocXObject getSelectedDocumentElement() {
			if (browser != null) {
				return browser.getSelectedDocumentElement();
			}
			return null;
		}

		public void setSelectedDocumentElement(DocXObject selected) {
			if (browser != null) {
				browser.setSelectedDocumentElement(selected);
			}
		}

	}

}
