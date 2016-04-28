package data;


public class JsonWithData {
	private final String CLEAN_SLATE_SINGLE_PAGE_FILE_NAME = "cleanslatesinglepage.pdf";
	private final String CLEAN_SLATE_SINGLE_PAGE_JSON = "{" +
            "\"Address City\": \"Little Town\"," +
            "\"Address State\": \"CA\"," +
            "\"Address Street\": \"111 Main Street\"," +
            "\"Address Zip\": \"01092\"," +
            "\"Arrested outside SF\": \"No\"," +
            "\"Cell phone number\": \"999-999-9999\"," +
            "\"Charged with a crime\": \"No\"," +
            "\"Date\": \"09/09/2016\"," +
            "\"Date of Birth\": \"09/09/9999\"," +
            "\"Dates arrested outside SF\": \"\"," +
            "\"Drivers License\": \"D9999999\"," +
            "\"Email Address\": \"berry.happy.manatee@gmail.com\"," +
            "\"Employed\": \"No\"," +
            "\"First Name\": \"Berry\"," +
            "\"Home phone number\": \"\"," +
            "\"How did you hear about the Clean Slate Program\":" +
            "\"From a wonderful friend\"," +
            "\"If probation where and when?\": \"\"," +
            "\"Last Name\": \"Manatee\"," +
            "\"MI\": \"H\"," + 
            "\"May we leave voicemail\": \"Yes\"," +
            "\"May we send mail here\": \"Yes\"," + 
            "\"Monthly expenses\": \"1000\"," +
            "\"On probation or parole\": \"No\"," +
            "\"Other phone number\": \"\"," +
            "\"Serving a sentence\": \"No\"," +
            "\"Social Security Number\": \"999-99-9999\"," +
            "\"US Citizen\": \"Yes\"," +
            "\"What is your monthly income\": \"0\"," +
            "\"Work phone number\": \"\"," +
        "}";
	private final String CHECK_BOX_FILE_NAME = "checkbox.pdf";
	private final String CHECK_BOX_JSON = "{" +
            "\"Check Box2\": \"Off\"," +
            "\"Check Box3\": \"Yes\"" +
        "}";
	private final String RADIO_BOX_FILE_NAME = "radio.pdf";
	private final String RADIO_BOX_JSON = "{" +
            "\"Radio Buttons\": \"yellow\"" +
        "}";
	private final String TEXT_FILE_NAME = "text.pdf";
	private final String TEXT_JSON = "{\"fields\": [{" +
            "\"multiline\": \"So\\nmany\\nlines\"}," +
            "{\"single\": \"Hello pdf world\"}" +
        "]}";
	private final String UNICODE_TEXT_FILE_NAME = "unicode text.pdf";
	private final String UNICODE_TEXT_JSON = "{" +
            "\"multiline\": \"你好，世界\nஹலோ உலகம்\n안녕하세요 세계\"," +
            "\"single\": \"مرحبا بالعالم\"" +
        "}";
	private final String SAMPLE_FORM_FILE_NAME = "";
	private final String SAMPLE_FORM_JSON = "{" +
            "\"Given Name Text Box\": \"Gaurav\"," +
            "\"Family Name Text Box\": \"Kulkarni\"," + 
            "\"Address 1 Text Box\": \"1 Main St\"," +
            "\"Postcode Text Box\": \"94107\"," + 
            "\"City Text Box\": \"San Francisco\"," +
            "\"Height Formatted Field\": \"150cm\"," +
            "\"Driving License Check Box\": \"Yes\"," +
            "\"Language 2 Check Box\": \"Yes\"," +
        "}";
	
	public String getFileString(String fileName) {
		switch (fileName) {
		case CLEAN_SLATE_SINGLE_PAGE_FILE_NAME: 
			return CLEAN_SLATE_SINGLE_PAGE_JSON;
		case CHECK_BOX_FILE_NAME: 
			return CHECK_BOX_JSON;
		case RADIO_BOX_FILE_NAME:
			return RADIO_BOX_JSON;
		case TEXT_FILE_NAME: 
			return TEXT_JSON;
		case UNICODE_TEXT_FILE_NAME:
			return UNICODE_TEXT_JSON;
		case SAMPLE_FORM_FILE_NAME:
			return SAMPLE_FORM_JSON;
		}
		
		return "";
	}
}
