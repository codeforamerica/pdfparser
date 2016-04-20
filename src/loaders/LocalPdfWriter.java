package loaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class LocalPdfWriter implements IPdfLoader {
	private String sourceFile;
	private String destinationFile;
	private FileOutputStream fop;
	private PdfStamper stamper;
	private PdfReader reader;
	
	public LocalPdfWriter(String src, String dest) {
		sourceFile = src;
		destinationFile = dest;
	}

	@Override
	public AcroFields load() {
		try {
			Path inputPath = Paths.get(sourceFile);
			Path fullPath = inputPath.toAbsolutePath();
			reader = new PdfReader(fullPath.toString());
			File file = new File(destinationFile);

			if (!file.exists()) {
				file.createNewFile();
			}
			fop = new FileOutputStream(file);
			if (fop != null) {
				stamper = new PdfStamper(reader, fop);
			}
		} catch (IOException | DocumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (fop != null) {
				try {
					fop.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public void unload() {
		stamper.setFormFlattening(true);
		try {
			if (stamper != null) {
				stamper.close();
			}
			if (fop != null) {
				fop.flush();
				fop.close();
			}
		} catch (DocumentException | IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (fop != null) {
				try {
					fop.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
