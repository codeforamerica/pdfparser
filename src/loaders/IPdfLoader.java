package loaders;

import com.itextpdf.text.pdf.AcroFields;


public interface IPdfLoader {
	AcroFields load();
	void unload();
}
