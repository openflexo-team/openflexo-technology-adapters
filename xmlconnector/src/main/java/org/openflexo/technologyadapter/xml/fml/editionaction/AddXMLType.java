/*
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2014 - Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.xml.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.fml.editionaction.AssignableAction;
import org.openflexo.foundation.fmlrt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fmlrt.action.FlexoBehaviourAction;
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
public interface AddXMLType extends AssignableAction<XMLModelSlot, XMLType> {

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
	public static abstract class AddXMLTypeImpl extends AssignableActionImpl<XMLModelSlot, XMLType> implements AddXMLType {

		private static final Logger logger = Logger.getLogger(AddXMLType.class.getPackage().getName());

		private final String dataPropertyURI = null;


		// TODO create all the bindings needed 

		public AddXMLTypeImpl() {
			super();
		}




		@Override
		public XMLType performAction(FlexoBehaviourAction action) {

			XMLType newClass = null;
			try {
				XMLType father = getSuperType().getBindingValue(action);
				String newTypeName = null;
				newTypeName = getTypeName().getBindingValue(action);

				logger.info("Adding class " + newTypeName + " as " + father);
				// FIXME : Something wrong here!
				XMLMetaModel mm = getMetamodel().getBindingValue(action);
				if (mm != null){

					if (father != null) {					
						newClass = getModelSlotInstance(action).getAccessedResourceData().getMetaModel().createNewType(father.getURI().replace('#', '/') + "#" + newTypeName, newTypeName, isSimpleType());

						newClass.setSuperType(father);
					}
					else {

						newClass = getModelSlotInstance(action).getAccessedResourceData().getMetaModel().createNewType(mm.getURI() + "/" + newTypeName, newTypeName, isSimpleType());
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
		public TypeAwareModelSlotInstance<XMLModel, XMLMetaModel, XMLModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (TypeAwareModelSlotInstance<XMLModel, XMLMetaModel, XMLModelSlot>) super.getModelSlotInstance(action);
		}

	}
}
