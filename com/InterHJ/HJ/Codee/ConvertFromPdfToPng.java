package com.InterHJ.HJ.Codee;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

public class ConvertFromPdfToPng implements Callable {

	public static String path = "";
	private static String outputNameFilePdf = null;

	public ConvertFromPdfToPng(String path, String pdffilename) {
		this.path = path;
		outputNameFilePdf = pdffilename;
		File controlldirout = new File(path
				+ "\\elaborati_e_convertiti\\immagini");
		if (!controlldirout.exists()) {
			controlldirout.mkdirs();
		}
	}

	public Object call() throws Exception, IOException {
		int resolution;
		String imageFormat = "jpg";
		int imageType = BufferedImage.TYPE_INT_RGB;
		resolution = 600;
		PDDocument document = null;
		document = PDDocument.load(path + "\\elaborati_e_convertiti\\"
				+ outputNameFilePdf + ".pdf");
		int pagenumber = document.getNumberOfPages();
		PDFImageWriter imageWriter = new PDFImageWriter();
		boolean success = false;
		success = imageWriter.writeImage(document, imageFormat, null, 1,
				pagenumber, path + "\\elaborati_e_convertiti\\immagini\\"
						+ outputNameFilePdf, imageType, resolution);
		if (success) {
			return true;
		} else {
			return false;
		}
	}
}
