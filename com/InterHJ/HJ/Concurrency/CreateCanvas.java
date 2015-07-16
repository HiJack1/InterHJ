package com.InterHJ.HJ.Concurrency;

import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.Callable;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Chunk;

public class CreateCanvas implements Callable{
	
	private static PdfWriter writer = null;
	private static Font times = null;
	private static BaseFont bf_times = null;
	private static PdfContentByte canvas = null;
	private static Document doc = null;
	
	public CreateCanvas(Document docu, FileOutputStream FOS) throws DocumentException {
		times = new Font(FontFamily.TIMES_ROMAN, 11);
		bf_times = times.getCalculatedBaseFont(false);
		doc = docu;
		writer = PdfWriter.getInstance(doc,FOS);
	}

	public Object call() throws Exception {
		doc.open();
		canvas = writer.getDirectContent();
		canvas.beginText();
		canvas.setFontAndSize(bf_times, 11);
		canvas.showTextAligned(Element.ALIGN_CENTER,
				"Determinazione delle Varianti Emoglobiniche Metodica HPLC",
				300, 800, 0);
		canvas.showTextAligned(Element.ALIGN_CENTER,
				"Valori Normali Escluso Neonati", 300, 785, 0);
		canvas.showTextAligned(Element.ALIGN_CENTER, "Hb fetale fino a 1%",
				300, 770, 0);
		canvas.showTextAligned(Element.ALIGN_CENTER, "Hb A2 da 1.5 a 3.5%",
				300, 755, 0);
		canvas.endText();
		return canvas;
	}

}
