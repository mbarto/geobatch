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

package it.geosolutions.geobatch.lamma.configure;

import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import it.geosolutions.geobatch.catalog.impl.BaseService;
import it.geosolutions.geobatch.flow.event.action.ActionService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comments here ...
 * 
 * @author Alessio Fabiani, GeoSolutions
 * 
 */
public class LammaMosaicDataService extends BaseService implements
        ActionService<FileSystemEvent, LammaMosaicDataConfiguration> {

    private final static Logger LOGGER = Logger.getLogger(LammaMosaicDataService.class.toString());

    public LammaMosaicDataService(String id, String name, String description) {
        super(id, name, description);
    }

    /**
     * 
     */
    public LammaMosaicDataAction createAction(LammaMosaicDataConfiguration configuration) {
        try {
            return new LammaMosaicDataAction(configuration);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
            return null;
        }
    }

    public boolean canCreateAction(LammaMosaicDataConfiguration configuration) {
        // final boolean superRetVal = super.canCreateAction(configuration);
        return true;
    }

}
