package converters;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import loaders.PdfLoader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;

/** 
 * Reads a JSON and creates a PDF file based on that JSON
 * The JSON structure should look like the following: 
 * {fields: [{"field1": "value1"}, {"field2": "value2"},...]}
 * 
 * @author rogerawad
 *
 */
public class JsonToPdfConverter {
	private PdfLoader pdfWriter;
	private String jsonString;
	
	public JsonToPdfConverter(PdfLoader writer, final String json) {
		pdfWriter = writer;
		jsonString = json;
	}
	
	public void convert() {
		if (pdfWriter != null) {
			AcroFields fields = pdfWriter.load();
			if (fields != null && jsonString != null) {
				setValues(fields, jsonString);
			}
			pdfWriter.unload();
		}
    }
	
	private void setValues(AcroFields fields, final String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory();
		JsonParser parser;
		JsonNode rootNode;
		ArrayNode fieldsNode;
		try {
			// load the JSON tree from string
			System.out.print(jsonString);
			parser = factory.createJsonParser(jsonString);
			rootNode = mapper.readTree(parser);
			fieldsNode = (ArrayNode) rootNode.get("fields");
			JsonNode node;
			
			Map<String, Item> listOfFields = fields.getFields();
	    	Iterator<Map.Entry<String, Item>> iter = listOfFields.entrySet().iterator();
	    	Map.Entry<String, Item> entry;
	    	String key;
	    	
	    	// loop through the fields and set their values from their respective JSON nodes
	    	while (iter.hasNext()) {
	    		entry = iter.next();
	    		key = entry.getKey();
	    		node = fieldsNode.findValue(key);
	    		if (node != null) {
	    			switch(getFieldType(fields, key)) {
	    	    	case AcroFields.FIELD_TYPE_PUSHBUTTON:
	    	    	case AcroFields.FIELD_TYPE_CHECKBOX:
	    	    	case AcroFields.FIELD_TYPE_RADIOBUTTON:
	    	    	case AcroFields.FIELD_TYPE_TEXT:
	    	    	case AcroFields.FIELD_TYPE_LIST:
	    	    	case AcroFields.FIELD_TYPE_COMBO:
	    	    	case AcroFields.FIELD_TYPE_SIGNATURE:
	    	    		fields.setField(key, node.getTextValue());
	    	    		break;
	    	    	}
	    		}
	    	}

		} catch (IOException | DocumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private int getFieldType(final AcroFields fields, final String name) {
		return fields.getFieldType(name);
	}
}
