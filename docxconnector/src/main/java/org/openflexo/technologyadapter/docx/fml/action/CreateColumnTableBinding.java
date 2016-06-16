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
import org.openflexo.foundation.doc.fml.ColumnTableBinding;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.DocXTableRole;

public class CreateColumnTableBinding extends FlexoAction<CreateColumnTableBinding, DocXTableRole, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateColumnTableBinding.class.getPackage().getName());

	public static FlexoActionType<CreateColumnTableBinding, DocXTableRole, FMLObject> actionType = new FlexoActionType<CreateColumnTableBinding, DocXTableRole, FMLObject>(
			"create_column_table_binding", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateColumnTableBinding makeNewAction(DocXTableRole focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
			return new CreateColumnTableBinding(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DocXTableRole object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DocXTableRole object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateColumnTableBinding.actionType, DocXTableRole.class);
	}

	private String name;
	private int index;
	private DataBinding<String> value;

	private ColumnTableBinding<?, ?> newColumnTableBinding;

	CreateColumnTableBinding(DocXTableRole focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
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
		logger.info("Create ColumnTableBinding for " + getFocusedObject());

		newColumnTableBinding = getFocusedObject().getFMLModelFactory().newInstance(ColumnTableBinding.class);
		newColumnTableBinding.setColumnName(getName());
		newColumnTableBinding.setColumnIndex(getIndex());
		newColumnTableBinding.setValue(getValue());

		getFocusedObject().addToColumnBindings((ColumnTableBinding) newColumnTableBinding);

	}

	public ColumnTableBinding<?, ?> getNewColumnTableBinding() {
		return newColumnTableBinding;
	}

	@Override
	public boolean isValid() {
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (index != this.index) {
			int oldValue = this.index;
			this.index = index;
			getPropertyChangeSupport().firePropertyChange("index", oldValue, index);
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

}
