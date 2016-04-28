package converters;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import loaders.PdfLoader;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.FieldPosition;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;

/**
 * Reads a pdf file and returns a JSON string that contains all the fields in the file
 * The returned JSON structure looks like the following:
 * {fields: [{"field1": "value1"}, {"field2": "value2"},...]}
 * 
 * @author rogerawad
 *
 */

public class PdfToJsonConverter {
	private PdfLoader pdfLoader;
	
	public PdfToJsonConverter(final PdfLoader loader) {
		pdfLoader = loader;
	}
	
	public String convert() {
		ObjectNode json = null;
		if (pdfLoader != null) {
			AcroFields fields = pdfLoader.load();
			if (fields != null) {
				json = createJson(fields);
			}
		}
        
        return json.toString();
    }
    
	protected ObjectNode createJson(final AcroFields fields) {
    	JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode rootNode = nodeFactory.objectNode();
        ArrayNode jsonFields = nodeFactory.arrayNode();
        
        Map<String, Item> listOfFields = fields.getFields();
    	Iterator<Map.Entry<String, Item>> iter = listOfFields.entrySet().iterator();
    	
    	while (iter.hasNext()) {
    		ObjectNode childNode = createNodeFromEntry(fields, iter.next());
    		jsonFields.add(childNode);
    	}
    	
    	ObjectNode childNode = getAppearanceNode(fields);
    	rootNode.put("appearance", childNode);
    	rootNode.put("fields", jsonFields);
        return rootNode;
    }
    
    protected ObjectNode getFileMetadata(final AcroFields fields) {
    	ObjectNode fileNode = JsonNodeFactory.instance.objectNode();
    	fileNode.put("totalRevisions", fields.getTotalRevisions());
		
    	return fileNode; 
    }

    protected ObjectNode getAppearanceNode(final AcroFields fields) {
		ObjectNode childNode = JsonNodeFactory.instance.objectNode();

    	childNode.put("font", AcroFields.DA_FONT);
    	childNode.put("fontSize", AcroFields.DA_SIZE);
    	childNode.put("fontColor", AcroFields.DA_COLOR);
    	
		return childNode;
	}
    
	protected ObjectNode createNodeFromEntry(final AcroFields fields, final Map.Entry<String, Item> entry) {
    	ObjectNode childNode = JsonNodeFactory.instance.objectNode();
    	
    	String value;
    	String options[];
    	List<FieldPosition> positions;
    	value = fields.getField(entry.getKey());
		options = this.getFieldOptionsBasedOnType(fields, entry.getKey());
		positions = fields.getFieldPositions(entry.getKey());
        childNode.put("name", entry.getKey());
        childNode.put("value", value);
        childNode.put("type", getFieldTypeString(fields, entry.getKey()));
        childNode.put("tabIndex", getTabIndex(fields, entry));
        childNode.put("required", isRequired(entry));
        childNode.put("altText", getAltText(entry));
        if (options != null) {
        	childNode.put("options", getOptionsArrayNode(options));
        }
        if (positions != null) {
        	childNode.put("positions", getFieldPositionNode(positions));
        }
		
		return childNode;
    }
    
    protected boolean isRequired(final Map.Entry<String, Item> entry) {
		boolean required = false;
		if (entry != null) {
			Item item = entry.getValue();
			PdfDictionary dict = item.getMerged(0);
			PdfNumber flags = dict.getAsNumber(PdfName.FF);
			if (flags != null && (flags.intValue() & BaseField.REQUIRED) > 0) {
				required = true;				
			}
		}
		
		return required;
	}
	
	protected String getAltText(final Map.Entry<String, Item> entry) {
		String altText = "";
		if (entry != null) {
			Item item = entry.getValue();
			PdfDictionary dict = item.getMerged(0);
			if (dict.contains(PdfName.ALT)) {
				altText = dict.getAsString(PdfName.ALT).toString();
			}
		}
		
		return altText;
	}
    
	protected int getTabIndex(final AcroFields fields, final Map.Entry<String, Item> entry) {
    	Item item = fields.getFieldItem(entry.getKey());
		return item.getTabOrder(0);
    }
    
	protected ArrayNode getOptionsArrayNode(final String options[]) {
    	ArrayNode optionsArray = JsonNodeFactory.instance.arrayNode();
    	for(String option : options) {
    		optionsArray.add(option);
    	}
    	
    	return optionsArray;
    }
    
	protected ArrayNode getFieldPositionNode(final List<FieldPosition> positions) {
    	ArrayNode positionsArray = JsonNodeFactory.instance.arrayNode();
    	ObjectNode childNode = JsonNodeFactory.instance.objectNode();
    	Rectangle rect;
    	
    	for (FieldPosition position : positions) {
    		childNode.put("page", position.page);
    		rect = position.position;
    		childNode.put("width", rect.getWidth());
    		childNode.put("height", rect.getHeight());
    		childNode.put("left", rect.getLeft());
    		childNode.put("top", rect.getTop());
    		positionsArray.add(childNode);
    	}
    	
    	return positionsArray;
    }
    
	protected String[] getFieldOptionsBasedOnType(final AcroFields fields, final String name) {
    	String options[] = null;
    	
    	switch(fields.getFieldType(name)) {
    	case AcroFields.FIELD_TYPE_PUSHBUTTON:
    		options = fields.getListSelection(name);
    		break;
    	case AcroFields.FIELD_TYPE_CHECKBOX:
    	case AcroFields.FIELD_TYPE_RADIOBUTTON:
    		options = fields.getAppearanceStates(name);
    		break;
    	case AcroFields.FIELD_TYPE_TEXT:
    		break;
    	case AcroFields.FIELD_TYPE_LIST:
    	case AcroFields.FIELD_TYPE_COMBO:
    		options = fields.getListOptionExport(name);
    		//options = fields.getListSelection(name);
    		break;
    	case AcroFields.FIELD_TYPE_SIGNATURE:
    		break;
    	case AcroFields.FIELD_TYPE_NONE:
			// not found
			break;
    	}
    	return options;
    }
    
	protected String getFieldTypeString(final AcroFields fields, final String name) {
    	String type = null;
    	
    	switch(fields.getFieldType(name)) {
    	case AcroFields.FIELD_TYPE_PUSHBUTTON:
    		type = "button";
    		break;
    	case AcroFields.FIELD_TYPE_CHECKBOX:
    		type = "checkbox";
    		break;
    	case AcroFields.FIELD_TYPE_RADIOBUTTON:
    		type = "radio button";
    		break;
    	case AcroFields.FIELD_TYPE_TEXT:
    		type = "text";
    		break;
    	case AcroFields.FIELD_TYPE_LIST:
    		type = "listbox";
    		break;
    	case AcroFields.FIELD_TYPE_COMBO:
    		type = "combo box";
    		break;
    	case AcroFields.FIELD_TYPE_SIGNATURE:
    		type = "signature";
    		break;
    	case AcroFields.FIELD_TYPE_NONE:
			type = "unknown";
			break;
    	}
    	return type;
    }
    
//    private void miscellaneous(AcroFields fields) throws DocumentException, IOException {
//    	if (fields != null) {
//	    	// document signature fields iterate through them and check if the sig field covers whole doc
//	        List signatures = fields.getBlankSignatureNames();
//	        int revisions = fields.getTotalRevisions();
//	                
//	        // fields.getAppearanceStates(arg0)
//	        //BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, false, null, null, false);
//	        //fields.setFieldProperty("Name", "textfont", bf, null);
//	        fields.setField("Name", "\u04e711111");
//    	}
//    }
}
