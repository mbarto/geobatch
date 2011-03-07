/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://geobatch.codehaus.org/
 *  Copyright (C) 2007-2008-2009 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.geosolutions.geobatch.catalog.impl;

import it.geosolutions.geobatch.catalog.Identifiable;

public abstract class BaseIdentifiable implements Identifiable, Cloneable {

    /**
     * @uml.property  name="id"
     */
    private String id;

    /**
     * @uml.property  name="name"
     */
    private String name;

    /**
     * @uml.property  name="description"
     */
    private String description = "No description set.";

    /**
     * A constructor which do not initialize the resource id
     * @deprecated use the complete constructor
     */
    protected BaseIdentifiable() {
    }

    /**
     * Constructor forcing initialization of: id ,name and description of this resource 
     * @param id
     * @param name
     * @param description
     */
    public BaseIdentifiable(String id, String name, String description) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * @return
     * @uml.property  name="id"
     */
    public String getId() {
        return id;
    }

    /**
     * @return
     * @uml.property  name="name"
     */
    public String getName() {
        return name;
    }

    /**
     * @param id  the id to set
     * @uml.property  name="id"
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param name  the name to set
     * @uml.property  name="name"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param description  the description to set
     * @uml.property  name="description"
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return
     * @uml.property  name="description"
     */
    public String getDescription() {
        return description;
    }

    public BaseIdentifiable clone() {
        try {
            BaseIdentifiable bi = (BaseIdentifiable) super.clone();
            bi.description = this.description;
            bi.id = this.id;
            bi.name = this.name;
            return bi;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

}