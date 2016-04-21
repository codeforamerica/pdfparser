package loaders;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class LocalPdfReader implements IPdfLoader {
	private PdfReader reader;
	private PdfStamper stamper;
	private InputStream sourceStream;
	
	public LocalPdfReader(String srcFilePath) throws FileNotFoundException {
		sourceStream = new FileInputStream(srcFilePath);
	}

	public LocalPdfReader(InputStream stream) {
		sourceStream = stream;
	}

	@Override
	public AcroFields load() {
		AcroFields fields = null;
		try {
			reader = new PdfReader(sourceStream);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			stamper = new PdfStamper(reader, baos);
			fields = stamper.getAcroFields();
		} catch (DocumentException | IOException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		return fields;
	}

	@Override
	public void unload() {
		
		try {
			if (stamper != null) {
				stamper.setFormFlattening(true);
				stamper.close();
			}
		} catch (DocumentException | IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
