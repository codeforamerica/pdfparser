package pdfParsers;

import loaders.LocalPdfReader;

import java.io.InputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.node.ArrayNode;
import org.junit.Test;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PdfToJsonConverterTest {
	
	@Test
	public void checkboxPdfHasExpectedFields() throws Exception {
		assertThat(getFieldsFromPdf("checkbox.pdf"),
    		contains(
    			allOf(
    				hasJsonProperty("name", equalTo("Check Box2")),
    				hasJsonProperty("value", equalTo("")),
    				hasJsonProperty("type", equalTo("checkbox")),
    				hasJsonProperty("tabIndex", equalTo(0)),
    				hasJsonProperty("required", equalTo(false)),
    				hasJsonProperty("altText", equalTo(""))
    			),
    			allOf(
    				hasJsonProperty("name", equalTo("Check Box3")),
    				hasJsonProperty("value", equalTo("")),
    				hasJsonProperty("type", equalTo("checkbox")),
    				hasJsonProperty("tabIndex", equalTo(1)),
    				hasJsonProperty("required", equalTo(false)),
    				hasJsonProperty("altText", equalTo(""))
    			)
    		)
    	);
	}

    @Test
    public void textPdfHasExpectedFields() throws Exception {
    	assertThat(getFieldsFromPdf("text.pdf"),
    		contains(
    			allOf(
    				hasJsonProperty("name", equalTo("single")),
    				hasJsonProperty("value", equalTo("")),
    				hasJsonProperty("type", equalTo("text")),
    				hasJsonProperty("tabIndex", equalTo(0)),
    				hasJsonProperty("required", equalTo(false)),
    				hasJsonProperty("altText", equalTo(""))
    			),
    			allOf(
    				hasJsonProperty("name", equalTo("multiline")),
    				hasJsonProperty("value", equalTo("")),
    				hasJsonProperty("type", equalTo("text")),
    				hasJsonProperty("tabIndex", equalTo(1)),
    				hasJsonProperty("required", equalTo(false)),
    				hasJsonProperty("altText", equalTo(""))
    			)
    		)
    	);
    }

    /**
     * A matcher that verifies that the value of some specific property within the given JSON
     * node has a value that passes the specified test.
     */
    private Matcher<JsonNode> hasJsonProperty(final String name, final Matcher<?> test) {
    	return new TypeSafeDiagnosingMatcher<JsonNode>() {
    		@Override
    		protected boolean matchesSafely(JsonNode item, Description mismatchDescription) {
    			JsonNode field = item.get(name);
    			Object value;
    			if (field.isBoolean()) {
    				value = field.getValueAsBoolean();
    			} else if (field.isInt()) {
    				value = field.getValueAsInt();
    			} else if (field.isDouble()) {
    				value = field.getValueAsDouble();
    			} else {
    				value = field.getValueAsText();
    			}
    			if (test.matches(value)) {
    				return true;
    			} else {
    				mismatchDescription.appendText("field \"" + name + "\": ");
    				test.describeMismatch(value, mismatchDescription);
    				return false;
    			}
    		}

    		@Override
    		public void describeTo(Description d) {
    			d.appendText("field \"" + name + "\": ");
    			test.describeTo(d);
    		}
    	};
    }

    /**
     * Parses a PDF from the specified file (which must exist in src/test/resources);
     * takes the resulting JSON data, parses that, and returns the array of fields from
     * its "fields" property.
     */
    private ArrayNode getFieldsFromPdf(String fileName) throws IOException {
    	InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
    	PdfToJsonConverter converter = new PdfToJsonConverter(new LocalPdfReader(stream));
		String json = converter.getFields();
		JsonParser parser = new MappingJsonFactory().createJsonParser(json);
		JsonNode o = parser.readValueAsTree();
		return (ArrayNode) o.get("fields");
    }
}
