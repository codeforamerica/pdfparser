package org.codeforamerica.pdfparser.concat;


import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileConcatenatorTest {

    public File getResourceFile(String relativeFilePath){
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceUrl = classLoader.getResource(relativeFilePath);
        return new File(resourceUrl.getFile());
    }

    public HashMap<String, String> readPdf(String pdfPath) throws IOException {
        HashMap<String, String> keyValueMap = new HashMap<String, String>();
        PdfReader reader = new PdfReader(pdfPath);
        AcroFields fields = reader.getAcroFields();
        Map<String, AcroFields.Item> fieldMap = fields.getFields();
        for (Map.Entry<String, AcroFields.Item> entry : fieldMap.entrySet()) {
            String fieldValue = fields.getField(entry.getKey());
            keyValueMap.put(entry.getKey(), fieldValue);
        };
        return keyValueMap;
    }

    public void assertFirstMapContainsSecond(HashMap<String, String> hashMapA, HashMap<String, String> hashMapB) {
        for (Map.Entry<String, String> entry : hashMapB.entrySet()) {
            boolean hasKey = hashMapA.containsKey(entry.getKey());
            Assert.assertTrue(hasKey);
            String aValue = hashMapA.get(entry.getKey());
            Assert.assertEquals(entry.getValue(), aValue);
        };
    }

    public void assertPdfFormsEqual(String pdfPathA, String pdfPathB) throws IOException {
        HashMap<String, String> fieldsA = readPdf(pdfPathA);
        HashMap<String, String> fieldsB = readPdf(pdfPathB);
        assertFirstMapContainsSecond(fieldsA, fieldsB);
    }

    @Test
    public void testConcatFilesWithOneFileMakesCopy() throws FileNotFoundException, IOException {
        String[] srcFiles = new String[]{"src/test/resources/testpdfs/text.pdf"};
        String outputPath = "output.pdf";
        FileConcatenator concat = new FileConcatenator(srcFiles, outputPath);
        concat.concatenateAndWrite();
        assertPdfFormsEqual(srcFiles[0], outputPath);
    }

    @Test
    public void testConcatFilesWithMultipleFilesKeepsAllFields() throws FileNotFoundException, IOException {
        String[] srcFiles = new String[]{
                "src/test/resources/testpdfs/text.pdf",
                "src/test/resources/testpdfs/checkbox.pdf"
        };
        String outputPath = "output.pdf";
        FileConcatenator concat = new FileConcatenator(srcFiles, outputPath);
        concat.concatenateAndWrite();
        HashMap<String, String> textFields = readPdf(srcFiles[0]);
        HashMap<String, String> checkboxFields = readPdf(srcFiles[1]);
        HashMap<String, String> outputFields = readPdf(outputPath);
        assertFirstMapContainsSecond(outputFields, textFields);
        assertFirstMapContainsSecond(outputFields, checkboxFields);
    }
}
