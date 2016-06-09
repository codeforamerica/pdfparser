package loaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class LocalPdfReader implements PdfLoader {
	private PdfReader reader;
	private PdfStamper stamper;
	private String sourceFile;

	public LocalPdfReader(String srcPath) {
		sourceFile = srcPath;
	}

	@Override
	public AcroFields load() {
		AcroFields fields = null;
		try {
			reader = new PdfReader(sourceFile);
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
