package com.InterHJ.HJ.Concurrency;

import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Callable;

import com.itextpdf.text.Document;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;

public class CreatePDF implements Callable{
	
	private static Document document = null;
	private static Stack<Chunk> lista = null;
	private static String imagePath = null;
	
	public CreatePDF(Document doc, Stack<Chunk> list, String imagePath) {
		document = doc;
		lista = list;
		this.imagePath = imagePath;
	}

	@Override
	public Object call() throws Exception {
		int i = 0;
		//document.open();
		for (Chunk chunk : lista) {
			if(i == 22){
				Image image;
				image = Image.getInstance(imagePath);
				image.scaleAbsolute(490f, 310f);
				document.add(image);
			}else document.add(new Paragraph(chunk));
			i++;
		}
		document.close();
		return true;
	}

}
