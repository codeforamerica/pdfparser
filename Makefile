

field_data:
	java -jar pdfparser.jar get_fields testpdfs/CleanSlateSinglePage.pdf

fill_fields:
	java -jar target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar \
        set_fields \
        testpdfs/CleanSlateSinglePage.pdf \
        test.pdf \
        '{"fields": [{"Arrested outside SF": "No"}, {"How did you hear about the Clean Slate Program": "From a wonderful friend"}, {"US Citizen": "Yes"}, {"Address Zip": "01092"}, {"May we send mail here": "Yes"}, {"Other phone number": ""}, {"If probation where and when?": ""}, {"Address Street": "111 Main Street"}, {"Dates arrested outside SF": ""}, {"Monthly expenses": "1000"}, {"Cell phone number": "999-999-9999"}, {"Address State": "CA"}, {"Work phone number": ""}, {"Employed": "No"}, {"May we leave voicemail": "Yes"}, {"Serving a sentence": "No"}, {"Last Name": "Manatee"}, {"MI": "H"}, {"Charged with a crime": "No"}, {"First Name": "Berry"}, {"On probation or parole": "No"}, {"Social Security Number": "999-99-9999"}, {"Email Address": "berry.happy.manatee@gmail.com"}, {"What is your monthly income": "0"}, {"Date": "09/09/2016"}, {"Drivers License": "D9999999"}, {"Home phone number": ""}, {"Date of Birth": "09/09/9999"}, {"Address City": "Little Town"}]}'