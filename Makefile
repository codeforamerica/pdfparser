.PHONY: default field_data fill_fields arabic_test combine_pdfs test

TESTPDFS=src/test/resources/testpdfs
UNICODE_FONT_MAC='/Library/Fonts/Arial Unicode.ttf'

default:
	mvn clean package

field_data:
	java -jar target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar \
		get_fields $(TESTPDFS)/sample_form-filled.pdf

fill_fields:
	java -jar target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar \
        set_fields \
        $(TESTPDFS)/sample_form-filled.pdf \
        output.pdf \
        '{"fields": [{"Given Name Text Box": "Henry"},{"Family Name Text Box": "Raspberry"}]}'

arabic_test:
	java -jar target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar \
		set_fields \
        $(TESTPDFS)/sample_form-filled.pdf \
		arabic.pdf \
	    '{"fields": [{"Given Name Text Box": "HI البنك المتحد"},{"Family Name Text Box": "\u0627\u0644\u0628\u0646\u0643 \u0627\u0644\u0645\u062a\u062d\u062f"}]}' \
	    font_path $(UNICODE_FONT_MAC)

combine_pdfs:
	java -jar target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar \
        concat_files \
        $(TESTPDFS)/checkbox.pdf \
        $(TESTPDFS)/dropdown.pdf \
        $(TESTPDFS)/listbox.pdf \
        $(TESTPDFS)/radio.pdf \
        $(TESTPDFS)/signature.pdf \
        $(TESTPDFS)/text.pdf \
        output.pdf

test:
	mvn clean package
	make field_data
	make fill_fields
	make combine_pdfs
