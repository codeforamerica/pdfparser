package org.codeforamerica.pdfparser.loaders;

import com.lowagie.text.pdf.AcroFields;

public interface PdfLoader {
	AcroFields load();
	void unload();
}
