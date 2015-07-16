package com.InterHJ.HJ.Concurrency;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.Chunk;

import org.apache.batik.util.RunnableQueue;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.xmlbeans.XmlException;

import com.InterHJ.HJ.Codee.ConvertFromPdfToPng;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Shape;

public class ReadWord{

	private static String popElement;
	private static String[] returnElement;
	private static String pathElement;
	private static String wheresavefile;
	private static String elementpathelabor;
	private static String elementName;
	private static String returnelementforerror;
	private static Stack<String> stackElementToProcess;
	private static boolean iscreatedirimg = false;
	private static boolean iscreatedirelabor = false;
	private static boolean iscreateparagraph = false;
	private static volatile boolean readDocumentWord = false; 
	private static FileInputStream fs;
	private static HWPFDocument doc;
	private static Logger logger = null;
	private static String outnamefile = null;
	private static boolean processing = false;
	private static Stack<String> stackElementIsProcessed = new Stack<String>();
	private static String[] paragrafi = null;
	private static ConvertFromPdfToPng convertPdftoImg = null;
	private static ExecutorService executor = Executors.newCachedThreadPool();

	public ReadWord(String wheresavefile, String elementpathelabor, Logger log) {
		logger = log;
		this.wheresavefile = wheresavefile;
		this.elementpathelabor = elementpathelabor;
		logger.log(Level.INFO,"Controllo l'esistenza della cartella img.");
		File savepathimg = new File(wheresavefile + "\\img");
		if (!savepathimg.exists()) {
			logger.log(Level.INFO,"Creo la cartella img.");
			iscreatedirimg = savepathimg.mkdirs();
		} else {
			logger.log(Level.INFO,"La cartella img esiste.");
			iscreatedirimg = true;
		}
		logger.log(Level.INFO,"Controllo l'esistenza della cartella elaborati_e_convertiti.");
		File savepathelabor = new File(wheresavefile + "\\elaborati_e_convertiti");
		if (!savepathelabor.exists()) {
			logger.log(Level.INFO,"Creo la cartella elaborati_e_convertiti.");
			iscreatedirelabor = savepathelabor.mkdirs();
		} else {
			logger.log(Level.INFO,"La cartella esiste.");
			iscreatedirelabor = true;
		}
		// pathElement = path;
		fs = null;
		doc = null;
	}

	public void setElementToElabor(String element){
		this.popElement = element;
	}
	
	public boolean isReadWordDocument(){
		return readDocumentWord;
	}
	

	/*public static Stack<String>  throws IOException,XmlException, OpenXML4JException, InterruptedException, ExecutionException, DocumentException {
		
	}*/

	public synchronized Runnable readWordDocument(){
		return new Runnable() {
			
			@Override
			public void run() {
				FutureTask<Object> svg = null;
				paragrafi = null;
				boolean ok = false;
				Document doc = null;
				FileOutputStream FoS = null;
				PdfContentByte Canvaselem = null;
				boolean test = false;
				
				// for(int i=0;i<=stackElementToProcess.size();i++){
				// popElement = stackElementToProcess.pop();
				logger.log(Level.INFO,"Eseguo elaborazione file:'" + popElement + "'");
				// logger.log(Level.INFO, "Eseguo elaborazione file:'"+popElement+"'");
				FileInputStream fISWord = null;
				boolean svgfinish = false;
				FutureTask<Object> futurefileinputstream = (FutureTask<Object>) executor
						.submit(new CreateFileInputStreamFromWord(elementpathelabor,
								popElement+".doc"));
				if (fISWord == null) {
					logger.log(Level.INFO,"Creo il FIS del doc word '"+popElement+"'.");
					try {
						fISWord = (FileInputStream) futurefileinputstream.get();
					} catch (InterruptedException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Fis del file: '"+popElement+"' (InterruptedException) \n", e.fillInStackTrace());
					} catch (ExecutionException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Fis del file: '"+popElement+"' (ExecutionException) \n", e.fillInStackTrace());
					}
				}
				if (iscreatedirimg) {
					logger.log(Level.INFO,"Estraggo immagine svg di '"+popElement+"'.");
					svg = (FutureTask<Object>) executor
							.submit(new ExtractImageWordToFile(fISWord, wheresavefile
									+"\\img\\" + popElement));
					try {
						svgfinish = (Boolean) svg.get();
					} catch (InterruptedException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione del svg del file: '"+popElement+"' (InterruptedException) \n", e.fillInStackTrace());
					} catch (ExecutionException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione del svg del file: '"+popElement+"' (ExecutionException) \n", e.fillInStackTrace());
					}
				} else {
					logger.log(Level.INFO,"Estraggo immagine svg di '"+popElement+"'.");
					FutureTask<Object> createDir = (FutureTask<Object>) executor
							.submit(new Callable<Object>() {

								@Override
								public Object call() throws Exception {
									File savepath = new File(wheresavefile + "\\img");
									iscreatedirimg = savepath.mkdirs();
									return iscreatedirimg;
								}
							});
					try {
						test = (Boolean) createDir.get();
					} catch (InterruptedException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione della cartella: '"+wheresavefile + "\\img' (InterruptedException) \n", e.fillInStackTrace());
					} catch (ExecutionException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione della cartella: '"+wheresavefile + "\\img' (ExecutionException) \n", e.fillInStackTrace());
					}
					if(test){
						svg = (FutureTask<Object>) executor
								.submit(new ExtractImageWordToFile(fISWord, wheresavefile
										+ "\\img\\" + popElement));
						try {
							svgfinish = (Boolean) svg.get();
						} catch (InterruptedException e) {
							logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione del svg del file: '"+popElement+"' (InterruptedException) \n", e.fillInStackTrace());
						} catch (ExecutionException e) {
							logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione del svg del file: '"+popElement+"' (ExecutionException) \n", e.fillInStackTrace());
						}
					}
				}
				while(!svgfinish){

				}
				if(svgfinish){
					logger.log(Level.INFO,"Immagine estratta con successo di '"+popElement+"'.");
				}else{
					logger.log(Level.INFO,"Errore durante l'estrazione dell'immagine '"+popElement+"'.");
				}
				
				
				String[] paragrafi = null;
				logger.log(Level.INFO,"Estraggo paragrafi testuali da file word '"+popElement+"'.");
				FutureTask<Object> extractParag = null;
				try {
					extractParag = (FutureTask<Object>) executor
							.submit(new ExtractParagraphFromWord(fISWord));
				} catch (IOException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione dei paragrafi del file: '"+popElement+"' (IOException) \n", e.fillInStackTrace());
				}
				try {
					paragrafi = (String[]) extractParag.get();
				} catch (InterruptedException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione dei paragrafi del file: '"+popElement+"' (InterruptedException) \n", e.fillInStackTrace());
				} catch (ExecutionException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione dei paragrafi del file: '"+popElement+"' (ExecutionException) \n", e.fillInStackTrace());
				}
				logger.log(Level.INFO,"Paragrafi testuali estratti con successo di '"+popElement+"'.");
				
				while(!extractParag.isDone()&&!svg.isDone()){
					logger.log(Level.INFO,"Attendo il complemento dell'elaborazione del file word '"+popElement+"'.(Estraggo svg e paragrafi!!!)");
				}
				String[] namefile = paragrafi[4].split(":");
				namefile = namefile[1].split("Acquired");
				final String outnamefile = namefile[0].trim();
				logger.log(Level.INFO,"Il nome dei file PDF e JPG sarà: '"+outnamefile+"' di '"+popElement+"'.");
				logger.log(Level.INFO,"Creo il Chunk di '"+popElement+"'.");
				
				FutureTask<Object> Chunk= (FutureTask<Object>) executor
						.submit(new CreateChunk(paragrafi));
				while(!Chunk.isDone()){
					
				}
				Stack<Chunk> lista = null;
				try {
					lista = (Stack<Chunk>) Chunk.get();
				} catch (InterruptedException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Chunk del file: '"+popElement+"' (InterruptedException) \n", e.fillInStackTrace());
				} catch (ExecutionException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Chunk del file: '"+popElement+"' (ExecutionException) \n", e.fillInStackTrace());
				}
				logger.log(Level.INFO,"Chunk creato con successo di '"+popElement+"'.");
				logger.log(Level.INFO,"Creo il documento PDF di '"+popElement+"'.");
				
				FutureTask<Object> PDFDoc= (FutureTask<Object>) executor
						.submit(new CreatePDFDocument(popElement));
				
				logger.log(Level.INFO,"Creo il FOS per scrivere nel PDF di '"+popElement+"'.");
				FutureTask<Object> FOS = null;
				if (iscreatedirelabor) {
					FOS = (FutureTask<Object>) executor.submit(new Callable<Object>() {

						public Object call() throws Exception {
							return new FileOutputStream(wheresavefile+"\\elaborati_e_convertiti\\"+outnamefile+".pdf");
						}
					});
				}else{
					FutureTask<Object> createDir = (FutureTask<Object>) executor
							.submit(new Callable<Object>() {

								@Override
								public Object call() throws Exception {
									File savepath = new File(wheresavefile + "\\elaborati_e_convertiti");
									iscreatedirelabor = savepath.mkdirs();
									return iscreatedirelabor;
								}
							});
					try {
						test = (Boolean) createDir.get();
					} catch (InterruptedException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione della cartella: '"+wheresavefile+" \\elaborati_e_convertiti' (InterruptedException) \n", e.fillInStackTrace());
					} catch (ExecutionException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione della cartella: '"+wheresavefile+" \\elaborati_e_convertiti' (ExecutionException) \n", e.fillInStackTrace());
					}
					if (test){
						FOS = (FutureTask<Object>) executor.submit(new Callable<Object>() {

							@Override
							public Object call() throws Exception {
								return new FileOutputStream(wheresavefile+"\\elaborati_e_convertiti\\"+outnamefile+".pdf");
							}
						});
					}
				}
				
				while(!PDFDoc.isDone() && !FOS.isDone()){
					
				}
				logger.log(Level.INFO,"Documento PDF creato con successo di '"+popElement+"'.");
				logger.log(Level.INFO,"FOS creato di '"+popElement+"'.");
				try {
					doc = (Document) PDFDoc.get();
				} catch (InterruptedException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Document del PDF (InterruptedException) \n", e.fillInStackTrace());
				} catch (ExecutionException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Document del PDF (ExecutionException) \n", e.fillInStackTrace());
				}
				try {
					FoS = (FileOutputStream) FOS.get();
				} catch (InterruptedException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del FOS del PDF (InterruptedException) \n", e.fillInStackTrace());
				} catch (ExecutionException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del FOS del PDF (ExecutionException) \n", e.fillInStackTrace());
				}
				logger.log(Level.INFO,"Creo Canvas di '"+popElement+"'.");
				FutureTask<Object> Canvas = null;
				try {
					Canvas = (FutureTask<Object>) executor
							.submit(new CreateCanvas(doc,FoS));
				} catch (DocumentException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Canvas del PDF (DocumentException) \n", e.fillInStackTrace());
				}
				while(!Canvas.isDone()){
					
				}
				try {
					Canvaselem = (PdfContentByte) Canvas.get();
				} catch (InterruptedException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Canvas del PDF (InterruptedException) \n", e.fillInStackTrace());
				} catch (ExecutionException e) {
					logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Canvas del PDF (ExecutionException) \n", e.fillInStackTrace());
				}
				logger.log(Level.INFO,"Canvas creato di '"+popElement+"'.");
				FutureTask<Object> PDFConc = (FutureTask<Object>) executor.submit(new CreatePDF(doc, lista, wheresavefile
									+ "\\img\\" + popElement + ".svg"));
				while(!PDFConc.isDone()){
					
				}
				FutureTask<Object> ConvertPdfConc = (FutureTask<Object>) executor.submit(new ConvertFromPdfToPng(wheresavefile,outnamefile));
				while(!ConvertPdfConc.isDone()){
					
				}
			}
			};
		}
}
