/**
 * 
 */
package org.codeforamerica.pdfparser;

import static org.junit.Assert.*;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author brianhenry
 *
 */
public class PdfParserTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	public JsonNode stringToJson(String rawJson) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(rawJson);
	}

	@Test
	public void testReadPdfFields() throws IOException {

		String sampleFormFilled = "testpdfs/sample_form-filled.pdf";

		String parsedFile = PdfParser.readPdfFields(sampleFormFilled);

		String expected = "{\"appearance\":{\"font\":0,\"fontSize\":1,\"fontColor\":2},\"fields\":[{\"name\":\"Height Formatted Field\",\"value\":\"150cm\",\"type\":\"text\",\"tabIndex\":7,\"required\":false,\"altText\":\"\"},{\"name\":\"Given Name Text Box\",\"value\":\"Gaurav\",\"type\":\"text\",\"tabIndex\":0,\"required\":false,\"altText\":\"\"},{\"name\":\"Postcode Text Box\",\"value\":\"94107\",\"type\":\"text\",\"tabIndex\":5,\"required\":false,\"altText\":\"\"},{\"name\":\"Language 4 Check Box\",\"value\":\"Off\",\"type\":\"checkbox\",\"tabIndex\":12,\"required\":false,\"altText\":\"\",\"options\":[\"Yes\"]},{\"name\":\"City Text Box\",\"value\":\"San Francisco\",\"type\":\"text\",\"tabIndex\":6,\"required\":false,\"altText\":\"\"},{\"name\":\"Address 1 Text Box\",\"value\":\"1 Main St\",\"type\":\"text\",\"tabIndex\":2,\"required\":false,\"altText\":\"\"},{\"name\":\"Family Name Text Box\",\"value\":\"Kulkarni\",\"type\":\"text\",\"tabIndex\":1,\"required\":false,\"altText\":\"\"},{\"name\":\"Language 3 Check Box\",\"value\":\"Off\",\"type\":\"checkbox\",\"tabIndex\":11,\"required\":false,\"altText\":\"\",\"options\":[\"Yes\"]},{\"name\":\"Language 5 Check Box\",\"value\":\"Off\",\"type\":\"checkbox\",\"tabIndex\":13,\"required\":false,\"altText\":\"\",\"options\":[\"Yes\"]},{\"name\":\"Driving License Check Box\",\"value\":\"Yes\",\"type\":\"checkbox\",\"tabIndex\":8,\"required\":false,\"altText\":\"\",\"options\":[\"Yes\"]},{\"name\":\"Address 2 Text Box\",\"value\":\"\",\"type\":\"text\",\"tabIndex\":4,\"required\":false,\"altText\":\"\"},{\"name\":\"Language 2 Check Box\",\"value\":\"Yes\",\"type\":\"checkbox\",\"tabIndex\":10,\"required\":false,\"altText\":\"\",\"options\":[\"Yes\"]},{\"name\":\"Language 1 Check Box\",\"value\":\"Off\",\"type\":\"checkbox\",\"tabIndex\":9,\"required\":false,\"altText\":\"\",\"options\":[\"Yes\"]},{\"name\":\"House nr Text Box\",\"value\":\"\",\"type\":\"text\",\"tabIndex\":3,\"required\":false,\"altText\":\"\"}]}";
		JsonNode expectedJson = stringToJson(expected);
		JsonNode resultingJson = stringToJson(parsedFile);
		Assert.assertEquals(expectedJson, resultingJson);

	}

}
