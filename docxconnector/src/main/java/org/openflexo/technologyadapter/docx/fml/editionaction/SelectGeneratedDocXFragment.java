/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.fml.editionaction;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;

/**
 * This {@link EditionAction} allows to lookup a fragment in a generated document matching a template fragment
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(SelectGeneratedDocXFragment.SelectGeneratedDocXFragmentImpl.class)
@XMLElement
@FML("SelectGeneratedDocXFragment")
public interface SelectGeneratedDocXFragment extends DocXFragmentAction {

	@PropertyIdentifier(type = DocXFragment.class)
	public static final String TEMPLATE_FRAGMENT_KEY = "templateFragment";

	/**
	 * Return the searched fragment in the template resource<br>
	 * Note that is not the fragment that is to be managed at run-time
	 * 
	 * @return
	 */
	@Getter(value = TEMPLATE_FRAGMENT_KEY, isStringConvertable = true)
	@XMLAttribute
	public DocXFragment getTemplateFragment();

	/**
	 * Sets the searched fragment in the template resource<br>
	 * 
	 * @param fragment
	 */
	@Setter(TEMPLATE_FRAGMENT_KEY)
	public void setTemplateFragment(DocXFragment fragment);

	public static abstract class SelectGeneratedDocXFragmentImpl extends DocXFragmentActionImpl implements SelectGeneratedDocXFragment {

		private static final Logger logger = Logger.getLogger(SelectGeneratedDocXFragment.class.getPackage().getName());

		@Override
		public DocXFragment getTemplateFragment() {
			if (getAssignedFlexoProperty() instanceof DocXFragmentRole) {
				return ((DocXFragmentRole) getAssignedFlexoProperty()).getFragment();
			}
			return (DocXFragment) performSuperGetter(TEMPLATE_FRAGMENT_KEY);
		}

		@Override
		public DocXFragment execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {

			if (getModelSlotInstance(evaluationContext) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(evaluationContext).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			DocXDocument document = (DocXDocument) getModelSlotInstance(evaluationContext).getAccessedResourceData();

			/*System.out.println("document: " + document);
			System.out.println("getAssignedFlexoProperty()=" + getAssignedFlexoProperty());
			System.out.println("getTemplateFragment()= " + getTemplateFragment());*/

			int startIndex = -1;
			int endIndex = -1;

			if (getTemplateFragment() != null) {
				for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> templateElement : getTemplateFragment().getElements()) {
					// TODO: handle tables here !!!
					for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : document.getElements()) {
						if (e.getBaseIdentifier() != null && e.getBaseIdentifier().equals(templateElement.getIdentifier())) {
							int index = document.getElements().indexOf(e);
							if (startIndex == -1 || (index < startIndex)) {
								startIndex = index;
							}
							if (endIndex == -1 || (index > endIndex)) {
								endIndex = index;
							}
						}
					}
				}
			}

			if (startIndex > -1 && endIndex > -1) {
				return document.getFragment(document.getElements().get(startIndex), document.getElements().get(endIndex));
			}

			logger.warning("Could not find fragment matching template fragment. Abort.");
			return null;
		}
	}
}
