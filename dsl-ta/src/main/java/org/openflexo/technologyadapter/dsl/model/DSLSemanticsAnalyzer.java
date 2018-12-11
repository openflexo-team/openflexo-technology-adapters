/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Connie-core, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.dsl.model;

import org.openflexo.technologyadapter.dsl.parser.analysis.DepthFirstAdapter;
import org.openflexo.technologyadapter.dsl.parser.node.AComponentDefinition;
import org.openflexo.technologyadapter.dsl.parser.node.AIdentifierDotIdentifier;
import org.openflexo.technologyadapter.dsl.parser.node.ALinkDefinition;
import org.openflexo.technologyadapter.dsl.parser.node.ASlotDefinition;
import org.openflexo.technologyadapter.dsl.parser.node.ASystemDefinition;
import org.openflexo.technologyadapter.dsl.parser.node.ATailDotIdentifier;
import org.openflexo.technologyadapter.dsl.parser.node.PDotIdentifier;

/**
 * This class implements the semantics analyzer for a parsed DSL.<br>
 * 
 * @author sylvain
 * 
 */
class DSLSemanticsAnalyzer extends DepthFirstAdapter {

	private DSLModelFactory modelFactory;
	private DSLSystem system;
	private DSLComponent currentComponent;

	public DSLSemanticsAnalyzer(DSLModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	public DSLSystem getSystem() {
		return system;
	}

	@Override
	public void inASystemDefinition(ASystemDefinition node) {
		System.out.println("IN System with " + node);
		system = modelFactory.makeSystem();
		super.inASystemDefinition(node);
	}

	@Override
	public void outASystemDefinition(ASystemDefinition node) {
		System.out.println("OUT System with " + node);
		super.outASystemDefinition(node);
	}

	@Override
	public void inAComponentDefinition(AComponentDefinition node) {
		System.out.println("IN Component with " + node);
		currentComponent = modelFactory.makeDSLComponent(node.getName().getText());
		super.inAComponentDefinition(node);
	}

	@Override
	public void outAComponentDefinition(AComponentDefinition node) {
		System.out.println("OUT Component with " + node);
		super.outAComponentDefinition(node);
		system.addToComponents(currentComponent);
		currentComponent = null;
	}

	@Override
	public void inASlotDefinition(ASlotDefinition node) {
		System.out.println("IN Slot with " + node);
		DSLSlot newSlot = modelFactory.makeDSLSlot(node.getName().getText());
		currentComponent.addToSlots(newSlot);
		super.inASlotDefinition(node);
	}

	@Override
	public void outASlotDefinition(ASlotDefinition node) {
		System.out.println("OUT Slot with " + node);
		super.outASlotDefinition(node);
	}

	@Override
	public void inALinkDefinition(ALinkDefinition node) {
		System.out.println("IN Link with " + node);
		DSLLink newLink = modelFactory.makeDSLLink(node.getName().getText(), getSlot(node.getFromName()), getSlot(node.getToName()));
		system.addToLinks(newLink);
		super.inALinkDefinition(node);
	}

	private DSLSlot getSlot(PDotIdentifier dotIndentifier) {
		if (dotIndentifier instanceof ATailDotIdentifier) {
			String componentName = ((ATailDotIdentifier) dotIndentifier).getIdentifier().getText();
			String slotName = ((AIdentifierDotIdentifier) ((ATailDotIdentifier) dotIndentifier).getDotIdentifier()).getIdentifier()
					.getText();
			DSLComponent component = getComponent(componentName);
			if (component != null) {
				return component.getSlot(slotName);
			}
		}
		return null;
	}

	private DSLComponent getComponent(String componentIdentifier) {
		return system.getComponent(componentIdentifier);
	}

	@Override
	public void outALinkDefinition(ALinkDefinition node) {
		System.out.println("OUT Link with " + node);
		super.outALinkDefinition(node);
	}

}
