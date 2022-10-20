/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.technologyadapter.powerpoint.view;

import java.util.List;
import java.util.Vector;

import javax.swing.JTabbedPane;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.selection.SelectionListener;
import org.openflexo.selection.SelectionManager;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlide;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;
import org.openflexo.view.SelectionSynchronizedModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This class represent the module view for a ExcelWorkbook.<br>
 * 
 * @author vincent, sylvain
 * 
 */
@SuppressWarnings("serial")
public class PowerpointSlideshowView extends JTabbedPane implements SelectionSynchronizedModuleView<PowerpointSlideshow> {

	private PowerpointSlideshow slideshow;
	private final FlexoPerspective declaredPerspective;

	private final FlexoController controller;

	public PowerpointSlideshowView(PowerpointSlideshow slideshow, FlexoController controller, FlexoPerspective perspective) {
		super();
		this.controller = controller;
		declaredPerspective = perspective;
		for (PowerpointSlide slide : slideshow.getPowerpointSlides()) {
			addTab(slide.getName(), new PowerpointSlideView(slide, controller));
		}
	}

	public FlexoController getFlexoController() {
		return controller;
	}

	@Override
	public FlexoPerspective getPerspective() {
		return declaredPerspective;
	}

	@Override
	public void deleteModuleView() {
		if (getFlexoController() != null) {
			controller.removeModuleView(this);
		}
	}

	@Override
	public List<SelectionListener> getSelectionListeners() {
		Vector<SelectionListener> reply = new Vector<>();
		reply.add(this);
		return reply;
	}

	@Override
	public void willHide() {
	}

	@Override
	public void willShow() {
	}

	@Override
	public void show(FlexoController controller, FlexoPerspective perspective) {
	}

	@Override
	public PowerpointSlideshow getRepresentedObject() {
		return slideshow;
	}

	@Override
	public boolean isAutoscrolled() {
		return false;
	}

	@Override
	public void fireObjectSelected(FlexoObject object) {
	}

	@Override
	public void fireObjectDeselected(FlexoObject object) {
	}

	@Override
	public void fireResetSelection() {
	}

	@Override
	public void fireBeginMultipleSelection() {
	}

	@Override
	public void fireEndMultipleSelection() {
	}

	@Override
	public SelectionManager getSelectionManager() {
		if (getFlexoController() != null) {
			return getFlexoController().getSelectionManager();
		}
		return null;
	}

	@Override
	public Vector<FlexoObject> getSelection() {
		return getSelectionManager().getSelection();
	}

	@Override
	public void resetSelection() {
		getSelectionManager().resetSelection();
	}

	@Override
	public void addToSelected(FlexoObject object) {
		getSelectionManager().addToSelected(object);
	}

	@Override
	public void removeFromSelected(FlexoObject object) {
		getSelectionManager().removeFromSelected(object);
	}

	@Override
	public void addToSelected(Vector<? extends FlexoObject> objects) {
		getSelectionManager().addToSelected(objects);
	}

	@Override
	public void removeFromSelected(Vector<? extends FlexoObject> objects) {
		getSelectionManager().removeFromSelected(objects);
	}
	
	@Override
	public void setSelectedObjects(Vector<? extends FlexoObject> objects) {
		getSelectionManager().setSelectedObjects(objects);
	}

	@Override
	public FlexoObject getFocusedObject() {
		return getSelectionManager().getFocusedObject();
	}

	@Override
	public boolean mayRepresents(FlexoObject anObject) {
		return false;
	}

}
