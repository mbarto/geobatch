/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://geobatch.geo-solutions.it/
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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

package it.geosolutions.geobatch.octave;

import java.io.IOException;
import java.util.List;

import dk.ange.octave.exception.OctaveException;
import java.util.Iterator;

public abstract class SheetBuilder {
    
    /**
     * Generate a string functions serializing informations extracted
     * from returns ad arguments building something like:
     * [ret1,ret2,...,retN]=function_name(arg1,arg2,...,argN);
     * 
     * @return a string as above
     * @throws IOException 
     * @throws IllegalArgumentException 
     * @note this can be overrided
     */
    protected OctaveExecutableSheet buildSheet(OctaveFunctionFile off) throws OctaveException, IllegalArgumentException, IOException{
        List<SerializableOctaveObject<?>> returns=off.getReturns();
        List<SerializableOctaveObject<?>> arguments=off.getDefinitions();
        List<OctaveCommand> commands=off.getCommands();
        
        StringBuilder script=new StringBuilder("");
        /**
         * if function returns more than a value
         * it is in the form:
         * [ret1,ret2,...,retN]=function(...);
         */
        if (returns!=null){
            if (returns.size()>1) {
                Iterator<SerializableOctaveObject<?>> i=returns.iterator();
                script.append("[").append(i.next());
                while (i.hasNext()){
                    script.append(",").append(i.next().getName());
                }
                script.append("]=");
            }
            /**
             * if function return only a value
             * it is int the form:
             * ret1 = function(...);
             */
            else if (returns.size()==1){
                Iterator<SerializableOctaveObject<?>> i=returns.iterator();
                script.append(i.next().getName()).append("=");
            }
            /**
             * else
             * function do not return values
             * function(...);
             */
        }
        
        /**
         * The SheetFunctionFile name represents the function name
         */
        script.append(off.getName());
        
        if (arguments!=null){
            /**
             * if function has more than a input parameter
             * it is in the form:
             * ... function(arg1,arg2,...,argN);
             */
            if (arguments.size()>1) {
                script.append("(");
                for (Iterator<SerializableOctaveObject<?>> argIter = arguments.iterator(); argIter.hasNext();) {
                    script.append(argIter.next().getName());
                    if(argIter.hasNext())
                        script.append(",");
                }
                script.append(")");
            }
            /**
             * if function has only one input parameter
             * it is int the form:
             * ... function(arg1);
             */
            else if (arguments.size()==1){
                Iterator<SerializableOctaveObject<?>> i=arguments.iterator();
                script.append("(").append(i.next().getName()).append(")");
            }
            /**
             * else
             * function has not input parameter
             * ... function;
             */
        } //endif arguments!=null
        script.append(";");
        
        commands.add(new OctaveCommand(script.toString()));
        
        return new OctaveExecutableSheet("DEFAULT_FUNCTION_SHEET",commands,arguments,returns);
    }
}
