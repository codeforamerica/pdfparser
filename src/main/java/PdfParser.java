package main.java;

import java.io.IOException;

import main.java.pdfParsers.JsonToPdfWriter;
import main.java.pdfParsers.PdfToJsonConverter;
import main.java.loaders.LocalPdfReader;
import main.java.loaders.LocalPdfWriter;

import com.itextpdf.text.DocumentException;

//import data.JsonWithData;

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
    private static final String SRC = "testpdfs/formwithunicode.pdf";
    private static final String DEST = "testpdf/text.pdf";
	//public static final String SRC = "testpdfs/feafpart1.pdf";
    //private static final String SRC = "testpdfs/long_example_form_with_signature.pdf";
    //private static final String FONT = "/Library/Fonts/Arial Unicode.ttf";
    private static final boolean debug = false;


    public static void main(String[] args) throws DocumentException, IOException {
    	if (debug) {
    		//testLocally();
    		args = testWriteText();
    	}
    	
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
		return converter.getFields();
	}

	private static void writeJsonToPdf(String src, String dest, String json) {
		JsonToPdfWriter pdfWriter = new JsonToPdfWriter(new LocalPdfWriter(src, dest), json);
		pdfWriter.writeFields();
	}
 
    private static String[] testWriteUnicode() {
    	String[] args = new String[3];
    	args[0] = "set_fields";
		args[1] = "testpdfs/unicode text.pdf";
		//args[2] = new JsonWithData().getFileString("unicode text.pdf");
		return args;
    }
    
    private static String[] testWriteText() {
    	String[] args = new String[3];
    	args[0] = "set_fields";
		args[1] = "testpdfs/text.pdf";
		//args[2] = new JsonWithData().getFileString("text.pdf");
		return args;
    }

	private static void testLocally() {
		PdfToJsonConverter converter = new PdfToJsonConverter(new LocalPdfReader(SRC));
        String json = converter.getFields();
        System.out.print(json);
		JsonToPdfWriter pdfWriter = new JsonToPdfWriter(new LocalPdfWriter(SRC, DEST), json);
        pdfWriter.writeFields();
	}    
}