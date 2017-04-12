/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLModel;

@ModelEntity
@ImplementationClass(AddXMLType.AddXMLTypeImpl.class)
@XMLElement
@FML("AddXMLType")
public interface AddXMLType extends XMLAction<XMLModelSlot, XMLType> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String TYPE_NAME = "typeName";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SUPER_TYPE = "superType";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String METAMODEL = "metamodel";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SIMPLE_TYPE = "simpleType";

	@Getter(value = TYPE_NAME)
	@XMLAttribute
	public DataBinding<String> getTypeName();

	@Setter(TYPE_NAME)
	public void setTypeName(DataBinding<String> name);

	@Getter(value = SUPER_TYPE)
	@XMLAttribute
	public DataBinding<XMLComplexType> getSuperType();

	@Setter(SUPER_TYPE)
	public void setgetSuperType(DataBinding<XMLComplexType> aType);

	@Getter(value = METAMODEL)
	@XMLAttribute
	public DataBinding<XMLMetaModel> getMetamodel();

	@Setter(METAMODEL)
	public void setMetamodel(DataBinding<XMLMetaModel> metamodel);

	@Getter(value = SIMPLE_TYPE, defaultValue = "false")
	@XMLAttribute
	public boolean isSimpleType();

	@Setter(SIMPLE_TYPE)
	public void setIsSimpleType(boolean isSimple);

	/**
	 * Implementation
	 * 
	 * @author xtof
	 *
	 */
	public static abstract class AddXMLTypeImpl extends TechnologySpecificActionImpl<XMLModelSlot, XMLModel, XMLType>
			implements AddXMLType {

		private static final Logger logger = Logger.getLogger(AddXMLType.class.getPackage().getName());

		private final String dataPropertyURI = null;

		// TODO create all the bindings needed

		public AddXMLTypeImpl() {
			super();
		}

		@Override
		public XMLType execute(RunTimeEvaluationContext evaluationContext) {

			XMLType newClass = null;
			try {
				XMLType father = getSuperType().getBindingValue(evaluationContext);
				String newTypeName = null;
				newTypeName = getTypeName().getBindingValue(evaluationContext);

				logger.info("Adding class " + newTypeName + " as " + father);
				// FIXME : Something wrong here!
				XMLMetaModel mm = getMetamodel().getBindingValue(evaluationContext);
				if (mm != null) {

					if (father != null) {
						newClass = getModelSlotInstance(evaluationContext).getAccessedResourceData().getMetaModel()
								.createNewType(father.getURI().replace('#', '/') + "#" + newTypeName, newTypeName, isSimpleType());

						newClass.setSuperType(father);
					}
					else {

						newClass = getModelSlotInstance(evaluationContext).getAccessedResourceData().getMetaModel()
								.createNewType(mm.getURI() + "/" + newTypeName, newTypeName, isSimpleType());
					}
					logger.info("Added class " + newClass.getName() + " as " + father);

				}
				else {
					logger.warning("CANNOT create a new type in a null MetaModel!");
				}
			} catch (TypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullReferenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return newClass;

		}

		@Override
		public TypeAwareModelSlotInstance<XMLModel, XMLMetaModel, XMLModelSlot> getModelSlotInstance(
				RunTimeEvaluationContext evaluationContext) {
			return (TypeAwareModelSlotInstance<XMLModel, XMLMetaModel, XMLModelSlot>) super.getModelSlotInstance(evaluationContext);
		}

		@Override
		public Type getAssignableType() {
			return XMLType.class;
		}

	}
}
