# Building the code

You should have Maven 3.x (we are currently using Maven 3.3.9).

From the main `pdfparser` directory, run:

    mvn clean package

This produces two jars in `pdfparser/target`:

* `pdfparser-<version>.jar`: Contains only the Java classes from the `pdfparser` library itself, without the third-party dependencies. Use this if you are integrating `pdfparser` into another Java application that manages its own dependencies.
* `pdfparser-<version>-jar-with-dependencies.jar`: Contains the `pdfparser` classes as well as all of the third-party dependencies. This can be run as a standalone application from the command line.

# Running from the command line

If `$JARPATH` is the full file path of `pdfparser-<version>-jar-with-dependencies.jar`:

    java -jar $JARPATH <arguments...>

