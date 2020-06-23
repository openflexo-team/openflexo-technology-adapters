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

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.doc.FlexoDocTable;
import org.openflexo.foundation.doc.FlexoDocTableCell;
import org.openflexo.foundation.fml.editionaction.TechnologySpecificActionDefiningReceiver;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
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
public interface AddDocXFragment extends TechnologySpecificActionDefiningReceiver<DocXModelSlot, DocXDocument, DocXFragment> {

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
	public DataBinding<? extends FlexoDocElement<?, ?>> getLocation();

	@Setter(LOCATION_KEY)
	public void setLocation(DataBinding<? extends FlexoDocElement<?, ?>> element);

	@Getter(value = LOCATION_SEMANTICS_KEY)
	@XMLAttribute
	public LocationSemantics getLocationSemantics();

	@Setter(LOCATION_SEMANTICS_KEY)
	public void setLocationSemantics(LocationSemantics element);

	public static enum LocationSemantics {
		InsertAfter, InsertBefore, InsertAfterLastChild, InsertBeforeLastChild, EndOfDocument
	}

	public static abstract class AddDocXFragmentImpl
			extends TechnologySpecificActionDefiningReceiverImpl<DocXModelSlot, DocXDocument, DocXFragment> implements AddDocXFragment {

		private static final Logger logger = Logger.getLogger(AddDocXFragment.class.getPackage().getName());

		private DataBinding<? extends FlexoDocElement<?, ?>> location;

		public AddDocXFragmentImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return DocXFragment.class;
		}

		@Override
		public DataBinding<? extends FlexoDocElement<?, ?>> getLocation() {
			if (location == null) {
				location = new DataBinding<>(this, FlexoDocElement.class, BindingDefinitionType.GET);
				location.setBindingName("location");
			}
			return location;
		}

		@Override
		public void setLocation(DataBinding<? extends FlexoDocElement<?, ?>> location) {
			if (location != null) {
				location.setOwner(this);
				location.setBindingName("location");
				location.setDeclaredType(FlexoDocElement.class);
				location.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.location = location;
			notifiedBindingChanged(this.location);
		}

		@Override
		public DocXFragment execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {

			System.out.println("execute  AddDocXFragment(), location=" + getLocation());

			FlexoDocElement<?, ?> location = null;
			if (getLocation() != null && getLocation().isSet() && getLocation().isValid()) {
				try {
					location = getLocation().getBindingValue(evaluationContext);
				} catch (TypeMismatchException e) {
					e.printStackTrace();
				} catch (NullReferenceException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				if (location == null) {
					throw new FlexoException("Could not retrieve location");
				}
			}

			DocXDocument document = getReceiver(evaluationContext);

			int insertIndex = -1;

			switch (getLocationSemantics()) {
				case InsertAfter:
					insertIndex = document.getElements().indexOf(location) + 1;
					break;
				case InsertBefore:
					insertIndex = document.getElements().indexOf(location);
					break;
				case InsertAfterLastChild:
					FlexoDocElement<?, ?> lastDeepChild = getLastDeepChild((DocXElement) location);
					if (lastDeepChild != null) {
						insertIndex = document.getElements().indexOf(lastDeepChild) + 1;
					}
					else {
						insertIndex = -1;
					}
					break;
				case InsertBeforeLastChild:
					FlexoDocElement<?, ?> lastDeepChild2 = getLastDeepChild((DocXElement) location);
					if (lastDeepChild2 != null) {
						insertIndex = document.getElements().indexOf(lastDeepChild2);
					}
					else {
						insertIndex = -1;
					}
					break;
				case EndOfDocument:
					insertIndex = document.getElements().size();
				default:
					break;
			}

			if (insertIndex > -1) {

				boolean isFirst = true;
				DocXElement startElement = null;
				DocXElement endElement = null;

				System.out.println("BEFORE addDocXFragment");
				System.out.println("document=" + document);
				System.out.println("contents=\n" + document.debugStructuredContents());
				System.out.println("fragment=" + getFragment());
				System.out.println("getAssignedFlexoProperty()=" + getAssignedFlexoProperty());
				System.out.println("FML=" + getFMLRepresentation());
				System.out.println("Owner=" + getOwner().getFMLRepresentation());

				for (DocXElement element : getFragment().getElements()) {

					DocXElement clonedElement = (DocXElement) element.cloneObject();
					clonedElement.setBaseIdentifier(element.getIdentifier());

					if (clonedElement instanceof FlexoDocTable) {
						FlexoDocTable<?, ?> templateTable = (FlexoDocTable<?, ?>) element;
						FlexoDocTable<?, ?> clonedTable = (FlexoDocTable<?, ?>) clonedElement;
						for (int row = 0; row < templateTable.getTableRows().size(); row++) {
							for (int col = 0; col < templateTable.getTableRows().get(row).getTableCells().size(); col++) {
								FlexoDocTableCell<?, ?> templateCell = templateTable.getCell(row, col);
								FlexoDocTableCell<?, ?> clonedCell = clonedTable.getCell(row, col);
								for (int i = 0; i < templateCell.getElements().size(); i++) {
									clonedCell.getElements().get(i).setBaseIdentifier(templateCell.getElements().get(i).getIdentifier());
								}
							}

						}
					}

					document.insertElementAtIndex(clonedElement, insertIndex);

					insertIndex++;
					if (isFirst) {
						startElement = clonedElement;
						isFirst = false;
					}
					endElement = clonedElement;
				}

				// System.out.println("AFTER addDocXFragment");
				// System.out.println("document=" + document);
				// System.out.println("contents=\n" + document.debugStructuredContents());

				return document.getFragment(startElement, endElement);
			}

			// Cannot add at index < 0 !
			return null;
		}

		private DocXElement getLastDeepChild(DocXElement e) {
			DocXElement lastChild = e.getChildrenElements().size() > 0
					? (DocXElement) e.getChildrenElements().get(e.getChildrenElements().size() - 1) : null;
			if (lastChild == null) {
				return e;
			}
			else {
				return getLastDeepChild(lastChild);
			}

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
