/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.IsAStatement;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;
import org.openflexo.technologyadapter.owl.model.SubClassStatement;

@ModelEntity
@ImplementationClass(AddSubClassStatement.AddSubClassStatementImpl.class)
@XMLElement(xmlTag = "AddIsAPropertyStatement")
@FML("AddSubClassStatement")
public interface AddSubClassStatement extends AddStatement<SubClassStatement> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String FATHER_KEY = "father";

	@Getter(value = FATHER_KEY)
	@XMLAttribute
	public DataBinding<IFlexoOntologyConcept> getFather();

	@Setter(FATHER_KEY)
	public void setFather(DataBinding<IFlexoOntologyConcept> father);

	public static abstract class AddSubClassStatementImpl extends AddStatementImpl<SubClassStatement> implements AddSubClassStatement {

		private static final Logger logger = Logger.getLogger(AddSubClassStatement.class.getPackage().getName());

		public AddSubClassStatementImpl() {
			super();
		}

		@Override
		public IFlexoOntologyStructuralProperty getProperty() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setProperty(IFlexoOntologyStructuralProperty aProperty) {
			// TODO Auto-generated method stub
		}

		public OWLConcept<?> getPropertyFather(FlexoBehaviourAction action) {
			try {
				return (OWLConcept<?>) getFather().getBindingValue(action);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		private DataBinding<IFlexoOntologyConcept> father;

		@Override
		public DataBinding<IFlexoOntologyConcept> getFather() {
			if (father == null) {
				father = new DataBinding<IFlexoOntologyConcept>(this, IFlexoOntologyConcept.class, BindingDefinitionType.GET);
				father.setBindingName("father");
			}
			return father;
		}

		@Override
		public void setFather(DataBinding<IFlexoOntologyConcept> father) {
			if (father != null) {
				father.setOwner(this);
				father.setBindingName("father");
				father.setDeclaredType(IFlexoOntologyConcept.class);
				father.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.father = father;
		}

		@Override
		public Type getAssignableType() {
			return IsAStatement.class;
		}

		@Override
		public SubClassStatement execute(FlexoBehaviourAction<?, ?, ?> action) {
			OWLConcept<?> subject = getPropertySubject(action);
			OWLConcept<?> father = getPropertyFather(action);
			if (father instanceof OWLClass) {
				if (subject instanceof OWLClass) {
					return ((OWLClass) subject).addToSuperClasses((OWLClass) father);
				} else if (subject instanceof OWLIndividual) {
					return ((OWLIndividual) subject).addToTypes((OWLClass) father);
				}
			}
			return null;
		}

	}
}
