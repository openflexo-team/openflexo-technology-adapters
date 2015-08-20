/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.docx.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.docx4j.wml.ContentAccessor;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.doc.FlexoDocumentElement;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXElement;
import org.openflexo.technologyadapter.docx.model.DocXFragment;

/**
 * Create DOCX fragment
 * 
 * @author sylvain
 * 
 */

@ModelEntity
@ImplementationClass(AddDocXFragment.AddDocXFragmentImpl.class)
@XMLElement
public interface AddDocXFragment extends DocXAction<DocXFragment> {

	@PropertyIdentifier(type = DocXFragment.class)
	public static final String FRAGMENT_KEY = "fragment";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String LOCATION_KEY = "location";
	@PropertyIdentifier(type = LocationSemantics.class)
	public static final String LOCATION_SEMANTICS_KEY = "locationSemantics";

	/**
	 * Return the represented fragment in the template resource<br>
	 * Note that is not the fragment that is to be managed at run-time
	 * 
	 * @return
	 */
	@Getter(value = FRAGMENT_KEY, isStringConvertable = true)
	@XMLAttribute
	public DocXFragment getFragment();

	/**
	 * Sets the represented fragment in the template resource<br>
	 * 
	 * @param fragment
	 */
	@Setter(FRAGMENT_KEY)
	public void setFragment(DocXFragment fragment);

	@Getter(value = LOCATION_KEY)
	@XMLAttribute
	public DataBinding<DocXElement> getLocation();

	@Setter(LOCATION_KEY)
	public void setLocation(DataBinding<DocXElement> element);

	@Getter(value = LOCATION_SEMANTICS_KEY)
	@XMLAttribute
	public LocationSemantics getLocationSemantics();

	@Setter(LOCATION_SEMANTICS_KEY)
	public void setLocationSemantics(LocationSemantics element);

	public static enum LocationSemantics {
		InsertAfter, InsertBefore, InsertAfterLastChild, EndOfDocument
	}

	public static abstract class AddDocXFragmentImpl extends DocXActionImpl<DocXFragment> implements AddDocXFragment {

		private static final Logger logger = Logger.getLogger(AddDocXFragment.class.getPackage().getName());

		public AddDocXFragmentImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return DocXFragment.class;
		}

		@Override
		public DocXFragment execute(FlexoBehaviourAction<?, ?, ?> action) throws FlexoException {

			System.out.println("execute  AddDocXFragment()");

			DocXElement location = null;
			if (getLocation() != null && getLocation().isSet() && getLocation().isValid()) {
				try {
					location = getLocation().getBindingValue(action);
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
				if (location == null) {
					throw new FlexoException("Could not retrieve location");
				}
			}

			DocXDocument document = (DocXDocument) getModelSlotInstance(action).getAccessedResourceData();

			/*System.out.println("document=" + document);
			System.out.println("location=" + location);
			System.out.println("semantics=" + getLocationSemantics());
			System.out.println("fragment=" + getFragment());
			System.out.println("startElement=" + getFragment().getStartElement());
			System.out.println("endElement=" + getFragment().getEndElement());
			
			System.out.println("fragment.getFlexoDocument()=" + getFragment().getFlexoDocument());
			System.out.println("startElement.getFlexoDocument()=" + getFragment().getStartElement().getFlexoDocument());
			System.out.println("endElement.getFlexoDocument()=" + getFragment().getEndElement().getFlexoDocument());
			 */

			int insertIndex = -1;

			switch (getLocationSemantics()) {
			case InsertAfter:
				insertIndex = document.getElements().indexOf(location);
				break;
			case InsertBefore:
				insertIndex = document.getElements().indexOf(location) - 1;
				break;
			case InsertAfterLastChild:
				FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> lastChild = location.getChildrenElements().size() > 0 ? location
						.getChildrenElements().get(location.getChildrenElements().size() - 1) : null;
				if (lastChild != null) {
					insertIndex = document.getElements().indexOf(lastChild);
				} else {
					insertIndex = -1;
				}
				break;
			case EndOfDocument:
				insertIndex = document.getElements().size();
			default:
				break;
			}

			System.out.println("insertIndex=" + insertIndex);

			if (insertIndex > -1) {

				boolean isFirst = true;
				DocXElement startElement = null;
				DocXElement endElement = null;

				/*for (DocXElement element : getFragment().getElements()) {
					if (element instanceof DocXParagraph) {
						System.out.println("*** element " + element + " p=" + ((DocXParagraph) element).getP());
					}
				}*/

				/*System.out.println("BEFORE addDocXFragment");
				System.out.println("document=" + document);
				System.out.println("contents=\n" + document.debugStructuredContents());
				System.out.println("template=" + getFragment().getFlexoDocument());
				System.out.println("contents=\n" + getFragment().getFlexoDocument().debugStructuredContents());*/

				for (DocXElement element : getFragment().getElements()) {

					DocXElement clonedElement = (DocXElement) element.cloneObject();
					clonedElement.setBaseIdentifier(element.getIdentifier());

					/*	System.out.println(
								"> Template element: " + element + " id=" + element.getIdentifier() + " of " + element.getFlexoDocument());
						System.out.println("> Cloned element: " + clonedElement + " id=" + clonedElement.getIdentifier() + " of "
								+ clonedElement.getFlexoDocument());
					
						if (element instanceof DocXParagraph) {
							System.out.println(">>> CLONED paragraph " + clonedElement + " p=" + ((DocXParagraph) clonedElement).getP());
						}*/

					ContentAccessor parent = document.getWordprocessingMLPackage().getMainDocumentPart();
					clonedElement.appendToWordprocessingMLPackage(parent, insertIndex);
					document.insertElementAtIndex(clonedElement, insertIndex);

					insertIndex++;
					if (isFirst) {
						startElement = clonedElement;
						isFirst = false;
					}
					endElement = clonedElement;
				}

				/*System.out.println("AFTER addDocXFragment");
				System.out.println("document=" + document);
				System.out.println("contents=\n" + document.debugStructuredContents());
				System.out.println("template=" + getFragment().getFlexoDocument());
				System.out.println("contents=\n" + getFragment().getFlexoDocument().debugStructuredContents());*/

				return document.getFragment(startElement, endElement);
			}

			// Cannot add at index < 0 !
			return null;
		}

		@Override
		public DocXFragment getFragment() {
			if (getAssignedFlexoProperty() instanceof DocXFragmentRole) {
				return ((DocXFragmentRole) getAssignedFlexoProperty()).getFragment();
			}
			return (DocXFragment) performSuperGetter(FRAGMENT_KEY);
		}

	}
}
