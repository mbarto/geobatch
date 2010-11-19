package it.geosolutions.geobatch.nurc.sem.rep10.mars3d;

import it.geosolutions.geobatch.octave.DefaultFunctionBuilder;
import it.geosolutions.geobatch.octave.OctaveFunctionFile;
import it.geosolutions.geobatch.octave.SerializableOctaveFile;
import it.geosolutions.geobatch.octave.SerializableOctaveObject;

import java.util.Vector;

public class MARS3DFunctionBuilder extends DefaultFunctionBuilder {
    private final String filein,fileout;
    
    /**
     * 
     * @param file_in value of the input file (absolute path)
     * @param file_out value of the output file (absolute path)
     */
    public MARS3DFunctionBuilder(String file_in,String file_out){
        super();
        filein=file_in;
        fileout=file_out;
    }
    
    /**
     * The prototype of the mars3d function is:
     * mars3d(file_in,file_out);
     */
    @Override
    protected String buildFunction(OctaveFunctionFile off) throws Exception{
        // name should be -> mars3d
        String function=off.getName();
        
        /**
         * Transforming function arguments to sheet 
         * variable definitions
         */
        Vector<SerializableOctaveObject<?>> arguments=off.getArguments();
        
        if(off.getArguments().size()==2){
            // setting value of arguments
            /**
             * get the first variable definition which is supposed
             * to be the first argument of the function
             * mars3d(file_in,file_out)
             * and set its VALUE to the incoming file
             * This will be transformed by the DefaultFunctionBuilder.preprocess
             * into a sheet variable definition
             */
            ((SerializableOctaveFile) off.getArguments().get(0)).reSetVal(filein);

            /**
             * get the second variable definition which is supposed
             * to be the second argument of the function
             * mars3d(file_in,file_out)
             * set its VALUE to the conventional string obtained by 
             * buildFileName() method
             * This will be transformed by the DefaultFunctionBuilder.preprocess
             * into a sheet variable definition
             */
            ((SerializableOctaveFile) off.getArguments().get(1)).reSetVal(fileout);
        }
        else
            throw new Exception("Bad argument list in function: "+off.getName());
        
        String script=function;
        
        if (arguments!=null){
            /**
             * if function has more than a input parameter
             * it is in the form:
             * ... function(arg1,arg2);
             */
            if (arguments.size()==2) {
                /**
                 * @note: Here we suppose that
                 * getName returns serialized value which is
                 * modified into the execute() method of the
                 * MARS3DAction class.
                 * Variable Name should be substituted with
                 * the file name it is representing:
                 * arguments.get(0).getName() -> file_in
                 * arguments.get(1).getName() -> file_out
                 */
                script+="("+arguments.get(0).getName()+
                    ","+arguments.get(1).getName()+");";
            }
            /**
             * if function has only one input parameter
             * it is int the form:
             * ... function(arg1);
             */
            else
                throw new Exception("Argument list of "+function+
                        " should contain at least 2 arguments!");
        } //endif arguments!=null
        else
            throw new Exception("Argument list of "+function+" is empty!");
        
        return script;
    }
}