package com.InterHJ.HJ.Concurrency;

import java.util.Stack;
import java.util.concurrent.Callable;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;

public class CreateChunk implements Callable{
	
	private static Stack<Chunk> list = null;
	private static String[] paragraph = null;
	private static Font courier = null;
	private static String outnamefile = null;
	
	public CreateChunk(String[] paragraph) {
		this.paragraph = paragraph;
		courier = new Font(FontFamily.COURIER, 8);
		list = new Stack<Chunk>();
	}

	public Object call() throws Exception {
		for (int i = 0; i < 7; i++) {
			list.add(new Chunk(" ", courier));
		}
		for (int i = 1; i < paragraph.length; i++) {
			list.add(new Chunk(paragraph[i], courier));
			if(paragraph[i].contains("Acquired")){
				String[] namefile = paragraph[i].split(":");
				namefile = namefile[1].split("Acquired");
				outnamefile = namefile[0].trim();
				System.out.println(outnamefile);
				}
		}
		list.add(new Chunk(outnamefile, courier));
		return list;
	}

}
