package org.codeforamerica.pdfparser;

import java.io.FileNotFoundException;

import org.codeforamerica.pdfparser.concat.FileConcatenator;
import org.codeforamerica.pdfparser.converters.JsonToPdfConverter;
import org.codeforamerica.pdfparser.converters.PdfToJsonConverter;
import org.codeforamerica.pdfparser.loaders.LocalPdfReader;
import org.codeforamerica.pdfparser.loaders.LocalPdfWriter;

import java.io.IOException;

import com.lowagie.text.DocumentException;

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
    	} else if (args.length >= 4 && args[0].equals("set_fields") && args[1].length() > 0 &&
    			args[2].length() > 0 && args[3].length() > 0) {
    		String fontPath = "";
    		if (args.length > 4) {
    			fontPath = args[5];
    		}
    		writeJsonToPdf(args[1], args[2], args[3], fontPath);
    	} else if (args.length > 3 && args[0].equals("concat_files")) {
    		concatFiles(args);
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
    		} else if (args[0].equals("concat_files")) {
    			error = "Usage: pdfparser concat_files file1 file2 ... destFileName";
    		}
    	}
    	return error;
    }

	/**
	 * @param srcFile source file path
	 * @return JSON String of PDF fields & information 
	 */
	static String readPdfFields(String srcFile) {
		PdfToJsonConverter converter = new PdfToJsonConverter(new LocalPdfReader(srcFile));
		return converter.convert();
	}

	static void writeJsonToPdf(String src, String dest, String json, String fontPath) {
		JsonToPdfConverter pdfWriter = new JsonToPdfConverter(new LocalPdfWriter(src, dest), json, fontPath);
		pdfWriter.convert();
	}

	static void concatFiles(String[] argv) throws FileNotFoundException {
		String[] srcFiles = new String[argv.length - 2];
		int numArgs = argv.length;
		for (int i = 1; i < numArgs - 1; i++) {
			srcFiles[i - 1] = argv[i];
		}

		FileConcatenator concat = new FileConcatenator(srcFiles, argv[numArgs - 1] /* output path */);
		concat.concatenateAndWrite();
	}
}
