#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.model;

import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.model.${technologyPrefix}Model;

/**
 * Abstract Simple implementation. Using Pamela.
 * 
 * @author ${author}
 * 
 */
public abstract class ${technologyPrefix}ModelImpl implements ${technologyPrefix}Model {

    private ${technologyPrefix}TechnologyAdapter technologyAdapter;
    	
    public ${technologyPrefix}ModelImpl() {
    }
    
    public void setTechnologyAdapter(${technologyPrefix}TechnologyAdapter technologyAdapter) {
         this.technologyAdapter = technologyAdapter;
    }

    @Override
    public ${technologyPrefix}TechnologyAdapter getTechnologyAdapter() {
        return this.technologyAdapter;
    }

}
