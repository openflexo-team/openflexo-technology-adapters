/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Copyright (c) 2012-2012, AgileBirds
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

package org.openflexo.technologyadapter.emf.metamodel;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * EMF Uri Builder from Object.
 * 
 * @author gbesancon
 */
public class EMFMetaModelURIBuilder {

	/**
	 * URI of package.
	 * 
	 * @param aPackage
	 * @return
	 */
	public static String getUri(EPackage aPackage) {
		return aPackage.getNsURI();
	}

	/**
	 * URI of class.
	 * 
	 * @param aClass
	 * @return
	 */
	public static String getUri(EClass aClass) {
		return aClass.getEPackage().getNsURI() + '/' + aClass.getName();
	}

	/**
	 * URI of datatype.
	 * 
	 * @param aDataType
	 * @return
	 */
	public static String getUri(EDataType aDataType) {
		return aDataType.getEPackage().getNsURI() + '/' + aDataType.getName();
	}

	/**
	 * 
	 * URI of enum literal.
	 * 
	 * @param aEnumLiteral
	 * @return
	 */
	public static String getUri(EEnumLiteral aEnumLiteral) {
		return aEnumLiteral.getEEnum().getEPackage().getNsURI() + '/' + aEnumLiteral.getEEnum().getName() + '/' + aEnumLiteral.getName();
	}

	/**
	 * URI of StruturalFeature.
	 * 
	 * @param aClass
	 * @return
	 */
	public static String getUri(EStructuralFeature aStructuralFeature) {
		return aStructuralFeature.getEContainingClass().getEPackage().getNsURI() + '/' + aStructuralFeature.getEContainingClass().getName()
				+ '/' + aStructuralFeature.getName();
	}
}
