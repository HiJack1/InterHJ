package com.InterHJ.HJ.Concurrency;

import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePDFDocument implements Callable{
	
	private static String fileName = null;
	private static PdfContentByte[] canvas = null;
	
	public CreatePDFDocument(String filename) {
		fileName = filename;		
	}

	@Override
	public Object call() throws Exception {
		Document document = new Document(PageSize.A4, 70, 30, 10, 15);
		document.open();
		document.addCreationDate();
		document.addCreator("Stefano Tallarino");
		document.addAuthor("Stefano Tallarino - stefano.tallarino@gmail.com - Laboratorio di analisi cliniche Lamberti Sas");
		document.addTitle(fileName);
		document.addSubject("Determinazione delle Varianti Emoglobiniche Metodica HPLC");
		return document;
	}

}
