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
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.DocXTableRole;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXTable;

/**
 * This {@link EditionAction} allows to lookup a table in a generated document matching a template table
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(SelectGeneratedDocXTable.SelectGeneratedDocXTableImpl.class)
@XMLElement
@FML("SelectGeneratedDocXTable")
public interface SelectGeneratedDocXTable extends DocXTableAction {

	@PropertyIdentifier(type = String.class)
	public static final String TABLE_ID_KEY = "tableId";

	@PropertyIdentifier(type = DataBinding.class)
	public static final String DOCUMENT_FRAGMENT_KEY = "documentFragment";

	/**
	 * Return identifier of table in the template resource<br>
	 * Note that is not the identifier of table that is to be managed at run-time
	 * 
	 * @return
	 */
	@Getter(TABLE_ID_KEY)
	@XMLAttribute
	public String getTableId();

	/**
	 * Sets identifier of table in the template resource<br>
	 * 
	 * @param fragment
	 */
	@Setter(TABLE_ID_KEY)
	public void setTableId(String identifier);

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

	public static abstract class SelectGeneratedDocXTableImpl extends DocXTableActionImpl implements SelectGeneratedDocXTable {

		private static final Logger logger = Logger.getLogger(SelectGeneratedDocXTable.class.getPackage().getName());

		private DataBinding<DocXFragment> documentFragment;

		@Override
		public String getTableId() {
			if (getAssignedFlexoProperty() instanceof DocXTableRole) {
				return ((DocXTableRole) getAssignedFlexoProperty()).getTableId();
			}
			return (String) performSuperGetter(TABLE_ID_KEY);
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
		public DocXTable execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {

			DocXTable docXTable = getReceiver(evaluationContext);
			DocXDocument document = docXTable.getFlexoDocument();

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
					// System.out.println("Restrict search to " + searchAreaFragment);
					searchArea = searchAreaFragment.getElements();
				}
			}

			for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : searchArea) {
				if (e instanceof DocXTable && e.getBaseIdentifier() != null && e.getBaseIdentifier().equals(getTableId())) {
					// Found table !!!!!!!
					return (DocXTable) e;
				}
			}

			logger.warning("Could not find table matching template table " + getTableId() + ". Abort.");

			return null;
		}
	}
}
