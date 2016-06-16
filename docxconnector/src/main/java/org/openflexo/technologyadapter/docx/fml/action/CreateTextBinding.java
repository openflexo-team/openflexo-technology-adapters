/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.fml.action;

import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.doc.fml.TextBinding;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;

public class CreateTextBinding extends FlexoAction<CreateTextBinding, DocXFragmentRole, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateTextBinding.class.getPackage().getName());

	public static FlexoActionType<CreateTextBinding, DocXFragmentRole, FMLObject> actionType = new FlexoActionType<CreateTextBinding, DocXFragmentRole, FMLObject>(
			"create_text_binding", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateTextBinding makeNewAction(DocXFragmentRole focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
			return new CreateTextBinding(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DocXFragmentRole object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DocXFragmentRole object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateTextBinding.actionType, DocXFragmentRole.class);
	}

	private String name;
	private TextSelection<?, ?> textSelection;
	private DataBinding<String> value;
	private boolean multiline = false;

	private TextBinding<?, ?> newTextBinding;

	CreateTextBinding(DocXFragmentRole focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);

	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException {
		logger.info("Create TextBinding for " + getTextSelection());

		newTextBinding = getFocusedObject().getFMLModelFactory().newInstance(TextBinding.class);
		newTextBinding.setName(getName());
		newTextBinding.setTextSelection((TextSelection) getTextSelection());
		newTextBinding.setValue(getValue());
		newTextBinding.setMultiline(isMultiline());

		getFocusedObject().addToTextBindings((TextBinding) newTextBinding);

	}

	public TextBinding<?, ?> getNewTextBinding() {
		return newTextBinding;
	}

	@Override
	public boolean isValid() {
		if (getTextSelection() == null) {
			return false;
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if ((name == null && this.name != null) || (name != null && !name.equals(this.name))) {
			String oldValue = this.name;
			this.name = name;
			getPropertyChangeSupport().firePropertyChange("name", oldValue, name);
		}
	}

	public TextSelection<?, ?> getTextSelection() {
		return textSelection;
	}

	public void setTextSelection(TextSelection<?, ?> textSelection) {
		if ((textSelection == null && this.textSelection != null) || (textSelection != null && !textSelection.equals(this.textSelection))) {
			TextSelection<?, ?> oldValue = this.textSelection;
			this.textSelection = textSelection;
			getPropertyChangeSupport().firePropertyChange("textSelection", oldValue, textSelection);
		}
	}

	public DataBinding<String> getValue() {
		return value;
	}

	public void setValue(DataBinding<String> value) {
		if ((value == null && this.value != null) || (value != null && !value.equals(this.value))) {
			DataBinding<String> oldValue = this.value;
			this.value = value;
			getPropertyChangeSupport().firePropertyChange("value", oldValue, value);
		}
	}

	public boolean isMultiline() {
		return multiline;
	}

	public void setMultiline(boolean multiline) {
		if (multiline != this.multiline) {
			boolean oldValue = this.multiline;
			this.multiline = multiline;
			getPropertyChangeSupport().firePropertyChange("multiline", oldValue, multiline);
		}
	}

}
