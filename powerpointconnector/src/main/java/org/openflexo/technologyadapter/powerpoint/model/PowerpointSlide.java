/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Powerpointconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hslf.model.Slide;
import org.openflexo.technologyadapter.powerpoint.PowerpointTechnologyAdapter;

/**
 * Represents an Excel sheet, implemented as a wrapper of a POI sheet
 * 
 * @author vincent, sylvain
 * 
 */
public class PowerpointSlide extends PowerpointObject {
	private Slide slide;
	private PowerpointSlideshow powerpointSlideshow;
	private List<PowerpointShape> powerpointShapes;
	private int slideNumber;

	public Slide getSlide() {
		return slide;
	}

	public PowerpointSlide(Slide slide, PowerpointSlideshow powerpointSlideshow, PowerpointTechnologyAdapter adapter) {
		super(adapter);
		this.slide = slide;
		this.powerpointSlideshow = powerpointSlideshow;
		powerpointShapes = new ArrayList<>();
	}

	@Override
	public String getName() {
		if(slide.getTitle()==null){
			return Integer.toString(slide.getSlideNumber());
		}
		else{
			return slide.getTitle();
		}
	}

	public PowerpointSlideshow getSlideshow() {
		return powerpointSlideshow;
	}

	public List<PowerpointShape> getPowerpointShapes() {
		return powerpointShapes;
	}

	public void setPowerpointShapes(List<PowerpointShape> powerpointShapes) {
		this.powerpointShapes = powerpointShapes;
	}

	public void addToPowerpointShapes(PowerpointShape powerpointShapes) {
		this.powerpointShapes.add(powerpointShapes);
	}

	public void removeFromPowerpointShapes(PowerpointShape powerpointShapes) {
		this.powerpointShapes.remove(powerpointShapes);
	}

	@Override
	public String getUri() {
		String uri = getSlideshow().getUri()+"Slide="+getName();
		return uri;
	}

	public int getSlideNumber() {
		return slideNumber;
	}

	public void setSlideNumber(int slideNumber) {
		this.slideNumber = slideNumber;
	}

}
