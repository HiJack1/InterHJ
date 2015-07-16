package com.InterHJ.HJ.Concurrency;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Callable;

import com.aspose.words.Document;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Shape;

public class ExtractImageWordToFile implements Callable {

	private static FileInputStream fISWord = null;
	private static String pathsavefile = null;
	private static Document doc = null;

	public ExtractImageWordToFile(FileInputStream FIS, String pathfilename) {
		fISWord = FIS;
		pathsavefile = pathfilename;
	}

	@Override
	public Object call() throws Exception {
		doc = new Document(fISWord);
		if (doc != null) {
			NodeCollection shapes = doc.getChildNodes(NodeType.SHAPE, true);
			int imageIndex = 0;
			for (Shape shape : (Iterable<Shape>) shapes) {
				if (shape.hasImage()) {
					shape.getImageData().save(pathsavefile + ".svg");
					imageIndex++;
				}
			}
			return true;
		} else return false;
	}

}
