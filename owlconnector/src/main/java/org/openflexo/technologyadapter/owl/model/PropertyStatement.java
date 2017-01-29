/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.technologyadapter.owl.model;

import java.util.Vector;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Statement;
import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.ontology.IFlexoOntologyPropertyValue;
import org.openflexo.localization.Language;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.toolbox.StringUtils;

public abstract class PropertyStatement extends OWLStatement implements IFlexoOntologyPropertyValue<OWLTechnologyAdapter> {

	public static final String AS_STRING = "asString";
	public static final String AS_BOOLEAN = "asBoolean";
	public static final String AS_INTEGER = "asInteger";
	public static final String AS_BYTE = "asByte";
	public static final String AS_SHORT = "asShort";
	public static final String AS_LONG = "asLong";
	public static final String AS_CHARACTER = "asCharacter";
	public static final String AS_FLOAT = "asFloat";
	public static final String AS_DOUBLE = "asDouble";

	public PropertyStatement(OWLConcept<?> subject, Statement s, OWLTechnologyAdapter adapter) {
		super(subject, s, adapter);
	}

	@Override
	public abstract OWLProperty getProperty();

	public abstract Literal getLiteral();

	public String getDisplayableRepresentation() {
		return toString();
	}

	public boolean hasLitteralValue() {
		return getLiteral() != null;
	}

	public final boolean isStringValue() {
		return getLiteral() != null && getLiteral().getDatatype().getJavaClass().equals(String.class);
	}

	private Language language = null;
	private String stringValue = null;
	private boolean booleanValue = false;
	private int intValue = 0;
	private byte byteValue = 0;
	private short shortValue = 0;
	private long longValue = 0;
	private char charValue = 0;
	private float floatValue = 0;
	private double doubleValue = 0;

	public final Language getLanguage() {
		if (getLiteral() == null) {
			return null;
		}
		if (language == null) {
			language = Language.retrieveLanguage(getLiteral().getLanguage());
		}
		return language;
	}

	public final String getLanguageTag() {
		if (getLanguage() == null) {
			return "";
		}
		return getLanguage().getTag();
	}

	public final void setLanguage(Language aLanguage) {
		if (aLanguage != getLanguage()) {
			// Take care to this point: this object will disappear and be replaced by a new one
			// during updateOntologyStatements() !!!!!
			if (aLanguage != null) {
				getSubject().getOntResource().addProperty(getProperty().getOntProperty(), getStringValue(), aLanguage.getTag());
			}
			else {
				getSubject().getOntResource().addProperty(getProperty().getOntProperty(), getStringValue());
			}
			getSubject().removePropertyStatement(this);
			language = aLanguage;
		}
	}

	public final void setStringValue(String aValue, String language) {
		if (StringUtils.isSame(aValue, getStringValue())) {
			return;
		}
		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addProperty(getProperty().getOntProperty(), aValue, language);
		getSubject().removePropertyStatement(this);
		stringValue = aValue;
	}

	public final String getStringValue() {
		if (getLiteral() == null) {
			return null;
		}
		if (stringValue == null) {
			stringValue = getLiteral().getString();
		}
		return stringValue;
	}

	/**
	 * Creates a new Statement equals to this one with a new String value
	 * 
	 * @param aValue
	 */
	public final void setStringValue(String aValue) {
		if (StringUtils.isSame(aValue, getStringValue())) {
			return;
		}

		String oldValue = getStringValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		if (getLanguage() != null) {
			getSubject().getOntResource().addProperty(getProperty().getOntProperty(), aValue, getLanguage().getTag());
		}
		else {
			getSubject().getOntResource().addProperty(getProperty().getOntProperty(), aValue);
		}
		getSubject().removePropertyStatement(this);
		stringValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_STRING, oldValue, aValue));
	}

	public abstract boolean isAnnotationProperty();

	public Vector<Language> allLanguages() {
		return Language.availableValues();
	}

	public final boolean getBooleanValue() {
		if (getLiteral() == null) {
			return false;
		}
		booleanValue = getLiteral().getBoolean();
		return booleanValue;
	}

	public final void setBooleanValue(boolean aValue) {
		if (aValue == getBooleanValue()) {
			return;
		}

		boolean oldValue = getBooleanValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().removePropertyStatement(this);
		booleanValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_BOOLEAN, oldValue, aValue));
	}

	public final int getIntegerValue() {
		if (getLiteral() == null) {
			return 0;
		}
		intValue = getLiteral().getInt();
		return intValue;
	}

	public final void setIntegerValue(int aValue) {
		if (aValue == getIntegerValue()) {
			return;
		}

		int oldValue = getIntegerValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().removePropertyStatement(this);
		intValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_INTEGER, oldValue, aValue));
	}

	public final byte getByteValue() {
		if (getLiteral() == null) {
			return 0;
		}
		byteValue = getLiteral().getByte();
		return byteValue;
	}

	public final void setByteValue(byte aValue) {
		if (aValue == getByteValue()) {
			return;
		}

		byte oldValue = getByteValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().removePropertyStatement(this);
		byteValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_BYTE, oldValue, aValue));
	}

	public final short getShortValue() {
		if (getLiteral() == null) {
			return 0;
		}
		shortValue = getLiteral().getShort();
		return shortValue;
	}

	public final void setShortValue(short aValue) {
		if (aValue == getShortValue()) {
			return;
		}

		short oldValue = getShortValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().removePropertyStatement(this);
		shortValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_SHORT, oldValue, aValue));
	}

	public final long getLongValue() {
		if (getLiteral() == null) {
			return 0;
		}
		longValue = getLiteral().getLong();
		return longValue;
	}

	public final void setLongValue(long aValue) {
		if (aValue == getLongValue()) {
			return;
		}

		long oldValue = getLongValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().removePropertyStatement(this);
		longValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_LONG, oldValue, aValue));
	}

	public final float getFloatValue() {
		if (getLiteral() == null) {
			return 0;
		}
		floatValue = getLiteral().getFloat();
		return floatValue;
	}

	public final void setFloatValue(float aValue) {
		if (aValue == getFloatValue()) {
			return;
		}

		float oldValue = getFloatValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().removePropertyStatement(this);
		floatValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_FLOAT, oldValue, aValue));
	}

	public final double getDoubleValue() {
		if (getLiteral() == null) {
			return 0;
		}
		doubleValue = getLiteral().getDouble();
		return doubleValue;
	}

	public final void setDoubleValue(double aValue) {
		if (aValue == getDoubleValue()) {
			return;
		}

		double oldValue = getDoubleValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().removePropertyStatement(this);
		doubleValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_FLOAT, oldValue, aValue));
	}

	public final char getCharacterValue() {
		if (getLiteral() == null) {
			return 0;
		}
		charValue = getLiteral().getChar();
		return charValue;
	}

	public final void setCharacterValue(char aValue) {
		if (aValue == getCharacterValue()) {
			return;
		}

		char oldValue = getCharacterValue();

		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().removePropertyStatement(this);
		charValue = aValue;
		// System.out.println("Notify change " + getProperty().getName() + " from " + oldValue + " to " + aValue);
		setChanged();
		notifyObservers(new DataModification(AS_CHARACTER, oldValue, aValue));
	}
}
