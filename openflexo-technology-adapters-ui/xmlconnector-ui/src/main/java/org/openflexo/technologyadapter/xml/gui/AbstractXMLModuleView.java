package org.openflexo.technologyadapter.xml.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.fib.model.FIBBrowser;
import org.openflexo.fib.model.FIBComponent;
import org.openflexo.fib.model.FIBContainer;
import org.openflexo.fib.model.FIBCustom.FIBCustomComponent.CustomComponentParameter;
import org.openflexo.fib.view.widget.FIBBrowserWidget;
import org.openflexo.icon.UtilsIconLibrary;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.ModuleView;
import org.openflexo.view.SelectionSynchronizedFIBView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Abstract ModuleView to represent an XML resource, which can be in a federation context or in a free editing context. Inherit from
 * JScrollPane, to contain XML View. 
 * 
 */
public abstract class AbstractXMLModuleView<T extends XMLObject> extends SelectionSynchronizedFIBView implements ModuleView<T> {


	protected static final Logger logger          = Logger.getLogger(AbstractXMLModuleView.class.getPackage().getName());
	
	protected final FlexoController controller;

	protected T representedObject;

	protected final FlexoPerspective perspective;

	// Properties used to iteract with the view

	protected String filteredName;
	protected final List<XMLObject> matchingValues;
	protected XMLObject selectedValue;
	protected boolean isSearching = false;
	private boolean allowsSearch = true;

	/**
	 * Initialize needed attribute. All are final, no implicit call of super should be done.
	 *
	 * @param controller
	 * @param object
	 * @param perspective
	 */
	protected AbstractXMLModuleView(FlexoController controller, T object, FlexoPerspective perspective, Resource fib_file) {
		super(null, controller, fib_file);
		this.controller = controller;
		this.representedObject = object;
		this.perspective = perspective;
		setDataObject(this);
		matchingValues = new ArrayList<XMLObject>();
	}


	
	@Override
	public T getRepresentedObject() {
		return representedObject;
	}


	/**
	 * Retrieve the browser widget so that it can be queried
	 * 
	 * @return
	 */
	protected FIBBrowserWidget retrieveFIBBrowserWidget() {
		if (getFIBComponent() instanceof FIBContainer) {
			List<FIBComponent> listComponent = ((FIBContainer) getFIBComponent()).getAllSubComponents();
			for (FIBComponent c : listComponent) {
				if (c instanceof FIBBrowser) {
					return (FIBBrowserWidget) getFIBController().viewForComponent(c);
				}
			}
		}
		return null;
	}




	/**
	 * This method is used to retrieve all potential values when implementing completion<br>
	 * Completion will be performed on that selectable values<br>
	 * Default implementation is to iterate on all values of browser, please take care to infinite loops.<br>
	 * 
	 * Override when required
	 */
	protected Vector<XMLObject> getAllSelectableValues() {
		Vector<XMLObject> returned = new Vector<XMLObject>();
		FIBBrowserWidget browserWidget = retrieveFIBBrowserWidget();
		if (browserWidget == null) {
			return null;
		}
		Iterator<Object> it = browserWidget.getBrowserModel().recursivelyExploreModelToRetrieveContents();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof XMLObject) {
				returned.add((XMLObject) o);
			}
		}
		// System.out.println("Returned: (" + returned.size() + ") " + returned);
		return returned;
	}

	
	public String getFilteredName() {
		return filteredName;
	}

	public void setFilteredName(String filteredName) {
		this.filteredName = filteredName;
	}

	public List<XMLObject> getMatchingValues() {
		return matchingValues;
	}

	
	public void search() {
		if (StringUtils.isNotEmpty(getFilteredName())) {
			logger.info("Searching " + getFilteredName());
			matchingValues.clear();
			for (XMLObject o : getAllSelectableValues()) {
				if (o.getName().indexOf(getFilteredName()) > -1) {
					if (!matchingValues.contains(o)) {
						matchingValues.add(o);
					}
				}
			}

			isSearching = true;
			getPropertyChangeSupport().firePropertyChange("isSearching", false, true);
			getPropertyChangeSupport().firePropertyChange("matchingValues", null, matchingValues);

			if (matchingValues.size() == 1) {
				selectValue(matchingValues.get(0));
			}
		}

	}
	


	public boolean getAllowsSearch() {
		return allowsSearch;
	}

	@CustomComponentParameter(name = "allowsSearch", type = CustomComponentParameter.Type.OPTIONAL)
	public void setAllowsSearch(boolean allowsSearch) {
		this.allowsSearch = allowsSearch;
	}

	public void dismissSearch() {
		logger.info("Dismiss search");

		isSearching = false;
		getPropertyChangeSupport().firePropertyChange("isSearching", true, false);
	}

	public boolean isSearching() {
		return isSearching;
	}

	public XMLObject getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(XMLObject selectedValue) {
		XMLObject oldSelected = this.selectedValue;
		this.selectedValue = selectedValue;
		getPropertyChangeSupport().firePropertyChange("selectedValue", oldSelected, selectedValue);
	}

	public void selectValue(XMLObject selectedValue) {
		getFIBController().selectionCleared();
		getFIBController().objectAddedToSelection(selectedValue);
	}

	public ImageIcon getCancelIcon() {
		return UtilsIconLibrary.CANCEL_ICON;
	}

	public ImageIcon getSearchIcon() {
		return UtilsIconLibrary.SEARCH_ICON;
	}
	
	/**
	 * Remove ModuleView from controller. 
	 */
	@Override
	public void deleteModuleView() {
		this.controller.removeModuleView(this);
	}

	/**
	 * @return perspective given during construction of ModuleView.
	 */
	@Override
	public FlexoPerspective getPerspective() {
		return this.perspective;
	}

	/**
	 * Update right view to have Freeplane icon scrollbar.
	 */
	@Override
	public void willShow() {
		// TODO
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willHide() {
		// Nothing to implement
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {
		// TODO
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}
}
