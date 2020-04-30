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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
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

	@PropertyIdentifier(type = DataBinding.class)
	public static final String DOCUMENT_FRAGMENT_KEY = "documentFragment";

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

	/**
	 * Return the fragment in the document resource (not in the template) where to restrict search<br>
	 * Note that this fragment might be null, in this case, lookup fragment in the whole document
	 * 
	 * @return
	 */
	@Getter(value = DOCUMENT_FRAGMENT_KEY)
	@XMLAttribute
	public DataBinding<DocXFragment> getDocumentFragment();

	/**
	 * Sets the fragment in the document resource (not in the template) where to restrict search<br>
	 * Note that if this fragment is null, the fragment will be searched in the whole document
	 * 
	 * @param fragment
	 */
	@Setter(DOCUMENT_FRAGMENT_KEY)
	public void setDocumentFragment(DataBinding<DocXFragment> fragment);

	public static abstract class SelectGeneratedDocXFragmentImpl extends DocXFragmentActionImpl implements SelectGeneratedDocXFragment {

		private static final Logger logger = Logger.getLogger(SelectGeneratedDocXFragment.class.getPackage().getName());

		private DataBinding<DocXFragment> documentFragment;

		@Override
		public DocXFragment getTemplateFragment() {
			if (getAssignedFlexoProperty() instanceof DocXFragmentRole) {
				return ((DocXFragmentRole) getAssignedFlexoProperty()).getFragment();
			}
			return (DocXFragment) performSuperGetter(TEMPLATE_FRAGMENT_KEY);
		}

		@Override
		public DataBinding<DocXFragment> getDocumentFragment() {
			if (documentFragment == null) {
				documentFragment = new DataBinding<>(this, DocXFragment.class, BindingDefinitionType.GET);
				documentFragment.setBindingName("documentFragment");
			}
			return documentFragment;
		}

		@Override
		public void setDocumentFragment(DataBinding<DocXFragment> documentFragment) {
			if (documentFragment != null) {
				documentFragment.setOwner(this);
				documentFragment.setBindingName("documentFragment");
				documentFragment.setDeclaredType(DocXFragment.class);
				documentFragment.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.documentFragment = documentFragment;
			notifiedBindingChanged(this.documentFragment);
		}

		@Override
		public DocXFragment execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {

			// The idea is to access the underying role, asserting this role was assigned to this EditionAction
			if (getAssignedFlexoRole() != null && getAssignedFlexoRole().getModelSlot() != null) {
				DocXModelSlot modelSlot = (DocXModelSlot) getAssignedFlexoRole().getModelSlot();
				// Try with the FlexoConceptInstance
				DocXDocument document = evaluationContext.getFlexoConceptInstance().getModelSlotInstance(modelSlot)
						.getAccessedResourceData();
				if (document == null) {
					// Try with the parent VirtualModelInstance
					ModelSlotInstance<?, ?> msi = evaluationContext.getVirtualModelInstance().getModelSlotInstance(modelSlot);
					document = (DocXDocument) msi.getAccessedResourceData();
				}

				int startIndex = -1;
				int endIndex = -1;

				if (getTemplateFragment() != null) {
					for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> templateElement : getTemplateFragment().getElements()) {
						// TODO: handle tables here !!!
						List<? extends FlexoDocElement<DocXDocument, DocXTechnologyAdapter>> searchArea = document.getElements();
						if (getDocumentFragment() != null && getDocumentFragment().isSet() && getDocumentFragment().isValid()) {
							DocXFragment searchAreaFragment = null;
							try {
								searchAreaFragment = getDocumentFragment().getBindingValue(evaluationContext);
							} catch (TypeMismatchException e1) {
								e1.printStackTrace();
							} catch (NullReferenceException e1) {
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								e1.printStackTrace();
							}
							if (searchAreaFragment != null) {
								System.out.println("Restrict search to " + searchAreaFragment);
								searchArea = searchAreaFragment.getElements();
							}
						}
						for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : searchArea) {
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
			}

			logger.warning("Could not find fragment matching template fragment. Abort.");
			return null;
		}

	}
}
