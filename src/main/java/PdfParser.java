
import java.io.IOException;

import com.itextpdf.text.DocumentException;

import loaders.LocalPdfReader;
import loaders.LocalPdfWriter;
import converters.JsonToPdfConverter;
import converters.PdfToJsonConverter;

/**
 * 
 *   @author rogerawad
 *
 *   Two operations: 1. getting data about the form fields (get_fields) 2.
 *   filling the form fields (fill_fields)
 * 
 *   ​*get_fields*​ Inputs: are a filepath to pdf 
 *   output: is a JSON to stdout that describes fields with metatdata
 * 
 *   Useful field metadata: 
 *   name 
 *   type
 *   options 
 *   tabIndex 
 *   required 
 *   appearance: font type, font size, color 
 *   altname, mouseover, or tooltip 
 *   position
 * 
 *   ​*fill_fields*​ inputs: filepath to pdf, JSON key value store of field names and desired values 
 *   output: path to filled pdf in stdout
 */

public class PdfParser {
    public static void main(String[] args) throws DocumentException, IOException {
    	if (args.length == 2 && args[0].equals("get_fields") && args[1].length() > 0) {
    		String json = readPdfFields(args[1]);
    		System.out.print(json);
    	} else if (args.length == 4 && args[0].equals("set_fields") && args[1].length() > 0 && 
    			args[2].length() > 0 && args[3].length() > 0) {
    		writeJsonToPdf(args[1], args[2], args[3]);
    	} else {
    		System.err.println(getUsageError(args));
    	}
    }
    
    private static String getUsageError(String[] args) {
    	String error = "Usage: pdfparser get_fields filename or pdfparser set_fields srcFileName destFileName json";
    	if (args.length > 0) {
    		if (args[0].equals("get_fields")) {
    			error = "Usage: pdfparser get_fields filename";
    		} else if (args[0].equals("set_fields")) {
    			error = "Usage: pdfparser set_fields srcFileName destFileName json";
    		}
    	}
    	return error;
    }
    
	private static String readPdfFields(String srcFile) {
		PdfToJsonConverter converter = new PdfToJsonConverter(new LocalPdfReader(srcFile));
		return converter.convert();
	}

	private static void writeJsonToPdf(String src, String dest, String json) {
		JsonToPdfConverter pdfWriter = new JsonToPdfConverter(new LocalPdfWriter(src, dest), json);
		pdfWriter.convert();
	}
}