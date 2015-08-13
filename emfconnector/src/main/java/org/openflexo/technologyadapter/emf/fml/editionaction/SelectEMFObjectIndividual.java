/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.fml.editionaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.FetchRequest;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.fml.editionaction.SelectIndividual;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFEnumClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;

/**
 * EMF technology - specific {@link FetchRequest} allowing to retrieve a selection of some {@link EMFObjectIndividual} matching some
 * conditions and a given type.<br>
 * 
 * @author sylvain
 */
@ModelEntity
@ImplementationClass(SelectEMFObjectIndividual.SelectEMFObjectIndividualImpl.class)
@XMLElement
@FML("SelectEMFObjectIndividual")
public interface SelectEMFObjectIndividual extends SelectIndividual<EMFModelSlot, EMFObjectIndividual> {

	public static abstract class SelectEMFObjectIndividualImpl extends SelectIndividualImpl<EMFModelSlot, EMFObjectIndividual> implements
			SelectEMFObjectIndividual {

		private static final Logger logger = Logger.getLogger(SelectEMFObjectIndividual.class.getPackage().getName());

		public SelectEMFObjectIndividualImpl() {
			super();
		}

		@Override
		public List<EMFObjectIndividual> execute(FlexoBehaviourAction action) {
			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			// System.out.println("Selecting EMFObjectIndividuals in " + getModelSlotInstance(action).getModel() + " with type=" +
			// getType());
			List<EMFObjectIndividual> selectedIndividuals = new ArrayList<EMFObjectIndividual>(0);
			EMFModel emfModel = getModelSlotInstance(action).getAccessedResourceData();
			Resource resource = emfModel.getEMFResource();
			IFlexoOntologyClass flexoOntologyClass = getType();
			List<EObject> selectedEMFIndividuals = new ArrayList<EObject>();
			if (flexoOntologyClass instanceof EMFClassClass) {
				TreeIterator<EObject> iterator = resource.getAllContents();
				while (iterator.hasNext()) {
					EObject eObject = iterator.next();
					// FIXME: following commented code was written by gilles
					// Seems to not working
					// Replaced by following
					// Gilles, could you check and explain ?
					/*selectedEMFIndividuals.addAll(EcoreUtility.getAllContents(eObject, ((EMFClassClass) flexoOntologyClass).getObject()
							.getClass()));*/
					EMFClassClass emfObjectIndividualType = emfModel.getMetaModel().getConverter().getClasses().get(eObject.eClass());
					if (emfObjectIndividualType.equals(flexoOntologyClass)
							|| ((EMFClassClass) flexoOntologyClass).isSuperClassOf(emfObjectIndividualType)) {
						selectedEMFIndividuals.add(eObject);
					}
					/*if (eObject.eClass().equals(((EMFClassClass) flexoOntologyClass).getObject())) {
						selectedEMFIndividuals.add(eObject);
					}*/
				}
			} else if (flexoOntologyClass instanceof EMFEnumClass) {
				System.err.println("We shouldn't browse enum individuals of type "
						+ ((EMFEnumClass) flexoOntologyClass).getObject().getName() + ".");
			}

			// System.out.println("selectedEMFIndividuals=" + selectedEMFIndividuals);

			for (EObject eObject : selectedEMFIndividuals) {
				EMFObjectIndividual emfObjectIndividual = emfModel.getConverter().getIndividuals().get(eObject);
				if (emfObjectIndividual != null) {
					selectedIndividuals.add(emfObjectIndividual);
				} else {
					logger.warning("It's weird there shoud be an existing OpenFlexo wrapper existing for EMF Object : "
							+ eObject.toString());
					selectedIndividuals.add(emfModel.getConverter().convertObjectIndividual(emfModel, eObject));
				}
			}

			List<EMFObjectIndividual> returned = filterWithConditions(selectedIndividuals, action);

			// System.out.println("SelectEMFObjectIndividual, without filtering =" + selectedIndividuals + " after filtering=" + returned);

			return returned;
		}

		@Override
		public TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, EMFModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, EMFModelSlot>) super.getModelSlotInstance(action);
		}

	}
}
