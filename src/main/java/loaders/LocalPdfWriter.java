package loaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class LocalPdfWriter implements PdfLoader {
	private String sourceFile;
	private String destinationFile;
	private FileOutputStream fop;
	private PdfStamper stamper;
	private PdfReader reader;

	public LocalPdfWriter(String srcPath, String destPath) {
		sourceFile = srcPath;
		destinationFile = destPath;
	}

	@Override
	public AcroFields load() {
		AcroFields fields = null;
		try {
			reader = new PdfReader(getInputFileFullPath());
			File file = createDestinationFile();
			fop = new FileOutputStream(file);
			if (fop != null) {
				stamper = new PdfStamper(reader, fop);
				fields = stamper.getAcroFields();
			}
 		} catch (IOException | DocumentException e) {
			System.out.println(e.getMessage());
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

	private String getInputFileFullPath() {
		Path inputPath = Paths.get(sourceFile);
		Path fullPath = inputPath.toAbsolutePath();
		return fullPath != null ? fullPath.toString() : "";
	}

	private File createDestinationFile() throws IOException {
		File file = new File(destinationFile);

		if (!file.exists()) {
			Path inputPath = Paths.get(destinationFile);
			Path fullPath = inputPath.toAbsolutePath();
			Files.createFile(fullPath);
		}
		return file;
	}

}
