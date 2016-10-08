package org.codeforamerica.pdfparser.concat;

import com.lowagie.text.pdf.PdfCopyForms;
import com.lowagie.text.pdf.PdfReader;
import org.junit.Test;
import junitx.framework.FileAssert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileConcatenatorTest {

    public File getResourceFile(String relativeFilePath){
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceUrl = classLoader.getResource(relativeFilePath);
        return new File(resourceUrl.getFile());
    }

    @Test
    public void testAddSrcFileWithOneInputCopiesFile() throws FileNotFoundException, IOException {
        String[] srcFiles = new String[]{"src/test/resources/testpdfs/text.pdf"};
        String outputPath = "output.pdf";
        FileConcatenator concat = new FileConcatenator(srcFiles, outputPath);
        concat.concatenateAndWrite();
    }
}
