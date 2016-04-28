package loaders;

import com.itextpdf.text.pdf.AcroFields;

public interface PdfLoader {
	AcroFields load();
	void unload();
}
