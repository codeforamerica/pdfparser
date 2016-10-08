package org.codeforamerica.pdfparser.concat;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyForms;
import com.lowagie.text.pdf.PdfReader;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileConcatenator {

    private String outputPath;
    private String[] srcFiles;
    private PdfCopyForms pdfCopy;

    public FileConcatenator(String[] _srcFiles, String _outputPath) {
        srcFiles = _srcFiles;
        outputPath = _outputPath;
    }

    void setPdfCopy() throws FileNotFoundException, DocumentException {
        OutputStream outputStream = new FileOutputStream(outputPath);
        pdfCopy = new PdfCopyForms(outputStream);
    }

    PdfReader parseSrcFile(String srcFilePath) throws IOException {
        File inFile = new File(srcFilePath);
        return new PdfReader(inFile.getAbsolutePath());
    }

    void addSrcFile(String srcFilePath) throws IOException, DocumentException {
        PdfReader reader = parseSrcFile(srcFilePath);
        pdfCopy.addDocument(reader);
        reader.close();
    }

    public PdfCopyForms concatenate() throws FileNotFoundException {
        try {
            setPdfCopy();
            for (String srcFile : srcFiles) {
                addSrcFile(srcFile);
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return pdfCopy;
    }

    public void concatenateAndWrite() throws FileNotFoundException {
        concatenate().close();
    }
}
