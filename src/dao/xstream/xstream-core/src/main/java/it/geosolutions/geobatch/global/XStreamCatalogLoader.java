/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://geobatch.codehaus.org/
 *  Copyright (C) 2007 - 2011 GeoSolutions S.A.S.
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

package it.geosolutions.geobatch.global;

import it.geosolutions.geobatch.catalog.Catalog;
import it.geosolutions.geobatch.catalog.Service;
import it.geosolutions.geobatch.catalog.dao.DAO;
import it.geosolutions.geobatch.catalog.dao.file.xstream.XStreamCatalogDAO;
import it.geosolutions.geobatch.catalog.dao.file.xstream.XStreamFlowConfigurationDAO;
import it.geosolutions.geobatch.catalog.file.FileBaseCatalog;
import it.geosolutions.geobatch.configuration.flow.file.FileBasedCatalogConfiguration;
import it.geosolutions.geobatch.flow.file.FileBasedFlowManager;
import it.geosolutions.geobatch.registry.AliasRegistrar;
import it.geosolutions.geobatch.settings.GBSettingsCatalog;
import it.geosolutions.geobatch.xstream.Alias;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The application configuration facade.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * 
 */
public class XStreamCatalogLoader extends CatalogHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(XStreamCatalogLoader.class);

    private final Alias alias;

    // enforcing singleton
    private XStreamCatalogLoader(Catalog catalog, Alias alias) {
        CatalogHolder.setCatalog(catalog);
        this.alias = alias;
    }

    @Resource
    private ApplicationContext context;

    /**
     * GeoBatch data dir. This directory is used by the GeoBatch to store Flows configuration files.
     */

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;

    }

    public void init() throws Exception {

        File dataDir = ((FileBaseCatalog) CatalogHolder.getCatalog()).getBaseDirectory();

        
        // //
        //
        // force loading all alias registrars
        //
        // //
        context.getBeansOfType(AliasRegistrar.class);

        // //
        //
        // Now get the catalog we have been injected
        //
        // //
        final Catalog catalog = getCatalog();
        final FileBasedCatalogConfiguration configuration = 
            new FileBasedCatalogConfiguration(catalog.getId(), catalog.getName(), catalog.getDescription(), true);
        catalog.setConfiguration(configuration);
        catalog.setDAO(new XStreamCatalogDAO(dataDir.getAbsolutePath(), alias));
        catalog.load();

        // //
        //
        // Force loading all all services
        //
        // //
        final Map<String, ? extends Service> services = context.getBeansOfType(Service.class);
        for (Entry<String, ? extends Service> servicePair : services.entrySet()) {
            final Service service = servicePair.getValue();
            if (!service.isAvailable()) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Skipping service " + servicePair.getKey() + " (" +service.getClass()+ ")" );
                continue;
            }
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Loading service " + servicePair.getKey() + " (" +service.getClass()+ ")");
            catalog.add(servicePair.getValue());
        }

        // //
        //
        // load all flows
        //
        // //
        final Iterator<File> it = FileUtils.iterateFiles(dataDir, new String[] { "xml" }, false);
        while (it.hasNext()) {
            final File flowConfigFile = it.next();

            // skip catalog config file
            if (flowConfigFile.getName().equalsIgnoreCase(catalog.getId() + ".xml"))
                continue;

            try {

                // loaded
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Loading flow from file " + flowConfigFile.getAbsolutePath());

                // try to load the flow and add it to the catalog
// TODO change this:
                final FileBasedFlowManager flowManager = new FileBasedFlowManager(
                        FilenameUtils.getBaseName(flowConfigFile.getName()),
                        configuration.getName(),
                        configuration.getDescription());
//                flow.setId(FilenameUtils.getBaseName(o.getName()));
                DAO flowLoader = new XStreamFlowConfigurationDAO(dataDir.getAbsolutePath(), alias);
                flowManager.setDAO(flowLoader);
                flowManager.load();
// TODO ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                // add to the catalog
                catalog.add(flowManager);
                
                // loaded
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(new StringBuilder("Loaded flow from file ").append(
                            flowConfigFile.getAbsolutePath()).toString());
            } catch (Throwable t) {
                if (LOGGER.isWarnEnabled())
                    LOGGER.warn("Skipping flow", t);
            }

        }

    }

    //==========================================================================

    public void setSettingsCatalog(GBSettingsCatalog settingsCatalog) {
        super.setSettingsCatalog(settingsCatalog);
    }
    
}
