package com.InterHJ.HJ.Concurrency;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

public class CreateFileInputStreamFromWord implements Callable{
	
	private static String fileWordPath = null;
	private static String nameFileWord = null;
	private static FileInputStream fileinputStream = null;
	
	
	public CreateFileInputStreamFromWord(String fileWordPath, String nameFileWord) {
		this.fileWordPath = fileWordPath;
		this.nameFileWord = nameFileWord;
	}

	@Override
	public FileInputStream call() throws IOException, InterruptedException {
		try {
			fileinputStream = new FileInputStream(fileWordPath+"\\"+nameFileWord);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileinputStream;
	}
}
