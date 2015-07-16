package com.InterHJ.HJ.Concurrency;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.xmlbeans.impl.common.IOUtil;

public class ExtractParagraphFromWord implements Callable{

	private static FileInputStream fISWord;
	private static HWPFDocument doc;
	private static String[] paragrafi = null;

	public ExtractParagraphFromWord(FileInputStream FIS) throws IOException{
		fISWord = FIS;
	}

	@Override
	public String[] call() throws Exception {
		POIFSFileSystem fileSystem = null;
		if (fISWord instanceof FileInputStream)
			((FileInputStream) fISWord).getChannel().position(0);
		else
			fISWord.reset();	
		fileSystem = doc.verifyAndBuildPOIFS(fISWord);
		if (fileSystem != null) {
				doc = new HWPFDocument(fileSystem);
			if (doc != null) {
				WordExtractor we = new WordExtractor(doc);
				paragrafi = we.getParagraphText();
			} 
		}
		return paragrafi;
	}


}
