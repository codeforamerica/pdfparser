# pdfparser
[![Build Status](https://travis-ci.org/codeforamerica/pdfparser.svg?branch=master)](https://travis-ci.org/codeforamerica/pdfparser)

pdfparser is a java-based command line utility for filling PDF forms. It builds on the [iText](http://developers.itextpdf.com/apis) library and has usage that is vaguely similar to [pdftk](https://www.pdflabs.com/docs/pdftk-man-page/).

This project was principally created by @rogerawad and originated at Code for San Francisco's weekly Hack Night to support the [pdfhook](https://github.com/codeforamerica/pdfhook) library.

## Status of the project

This software is fairly new, but is working reliably for it's core use case. The next priority is reliable support of Unicode input for filling PDF forms. Beyond that, a number of features are being considered. Review the [issues on the original repo](https://github.com/rogerawad/pdfparser/issues) to get an idea.

## Building the code

#### First, install Maven

You should have Maven 3.x (we are currently using Maven 3.3.9). Maven is a tool that will obtain dependencies and build the project.

You can easily install Maven on OS X using Homebrew:

    brew install maven

#### Clone the repo and build the `.jar`

Steps:

    git clone https://github.com/codeforamerica/pdfparser.git
    cd pdfparser        # enter the repo root folder
    mvn clean package   # build the .jar

This produces two jars in `pdfparser/target`:

* `pdfparser-<version>.jar`: Contains only the Java classes from the `pdfparser` library itself, without the third-party dependencies. Use this if you are integrating `pdfparser` into another Java application that manages its own dependencies.
* `pdfparser-<version>-jar-with-dependencies.jar`: Contains the `pdfparser` classes as well as all of the third-party dependencies. This can be run as a standalone application from the command line.

For example, the final path to the jar file that includes dependencies, relative to the root directory of this repository, might be `target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Using the `.jar`

If we assume `$JARPATH` contains the path to the jarfile, for example:

    JARPATH="target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar"

then you can run the program by prefixing the path with `java -jar` and adding arguments, like this:

    java -jar $JARPATH <arguments...>

There are three commands:

- [`get_fields`](#get_fields---get-data-about-the-fields-in-a-pdf-form): Get data about the fields in a pdf form
- [`set_fields`](#set_fields---use-a-json-to-fill-a-pdf-form): Use a JSON to fill a PDF form
- [`concat_files`](#concat_files---combine-multiple-pdfs-into-one): Combine multiple PDFs into one

### `get_fields` - Get data about the fields in a pdf form

This command will output a JSON with detailed information about the fields in a PDF form. There are two arguments: `get_fields`, and the path to the pdf you want it to analyze.

Usage:

    java -jar $JARPATH get_fields <input PDF form path>
    java -jar target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar \ 
        get_fields testpdfs/sample_form-filled.pdf

The output JSON will have a structure similar to this:

https://gist.github.com/bengolder/b7da072fb8538cf2d308bc39dc3f56e1

### `set_fields` - Use a JSON to fill a PDF form

This command allows you to use a JSON to create a filled copy of a PDF form. 

* `set_fields`
* The path to the PDF form you would like to fill
* The path for the output filled pdf form
* A JSON to use for filling the form, [structured like this](https://gist.github.com/bengolder/5af0be1721f08731718219341d5813dd)

Usage:

    java -jar $JARPATH get_fields <input PDF form path> <output PDF form path> <input JSON>
    java -jar target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar \
        set_fields \
        testpdfs/sample_form-filled.pdf \
        output.pdf \
        '{"fields": [{"Given Name Text Box": "Gaurav"},{"Family Name Text Box": "Kulkarni"}]}'


### `concat_files` - Combine multiple PDFs into one

This command allows you to take a series of PDFs and combine them into one single PDF file.

The command can take many arguments, depending on how many PDFs you want to combine. But the first argument is always `concat_files` and the last argument is always the path to the output pdf.

Usage:

    java -jar $JARPATH concat_files <two or more paths to input PDFs> <output PDF path>
    java -jar target/pdfparser-1.0-SNAPSHOT-jar-with-dependencies.jar \
        concat_files \
        testpdfs/checkbox.pdf \
        testpdfs/dropdown.pdf \
        testpdfs/listbox.pdf \
        testpdfs/radio.pdf \
        testpdfs/signature.pdf \
        testpdfs/text.pdf \
        output.pdf



