package com.InterHJ.HJ.GUI;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.batik.util.RunnableQueue;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.comparator.NameFileComparator;


import com.InterHJ.HJ.Codee.ConvertFromPdfToPng;
import com.InterHJ.HJ.Concurrency.CreateCanvas;
import com.InterHJ.HJ.Concurrency.CreateChunk;
import com.InterHJ.HJ.Concurrency.CreateFileInputStreamFromWord;
import com.InterHJ.HJ.Concurrency.CreatePDF;
import com.InterHJ.HJ.Concurrency.CreatePDFDocument;
import com.InterHJ.HJ.Concurrency.ExtractImageWordToFile;
import com.InterHJ.HJ.Concurrency.ExtractParagraphFromWord;
import com.InterHJ.HJ.Concurrency.ReadWord;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;

import de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel;

public class MainWindow {

	private static final String CONF_FILE_NAME = "Configuration.XML";
	private static final String PATH_="C:\\interfacciamento";
	private static final String PATH_OLD="C:\\interfacciamento\\file_processati";
	private static final String PATH_RESULT="C:\\interfacciamento\\elaborati_e_convertiti";
	private static final String LOGFILE_PATH = "C:\\interfacciamento\\log";
	private static final String LOGFILE_NAME = "MyLog_"+new SimpleDateFormat("HH mm - DD MM").format(Calendar.getInstance().getTime())+".log";
	
	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	
	private static boolean usePath = false;
	private static String pathtosave = null;
	private static String percfile=null;
	private static final Logger logger = Logger.getLogger("GUI.MainWindow");  
	private static FileHandler fh = null;
	private static ScheduledExecutorService s;
	private static Stack<String> stackElement  = new Stack<String>();
	private static Stack<String> stackelementprocessing = new Stack<String>();
	ClassLoader classLoader = getClass().getClassLoader();
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JButton btnNewButton_3;
	private JPanel pannello_sup;
	private JPanel panello_inf;
	private JButton btnNewButton_2;
	private JPanel pannello_sup_jlabel;
	private JPanel pannello_sup_jtext;
	private JPanel pannello_sup_button;
	private JPanel panello_inf_jlabel;
	private JPanel panello_inf_jtext;
	private JPanel panello_inf_button;
	private JPanel panel_start_stop;
	private static ScheduledThreadPoolExecutor executor;
	private static ScheduledThreadPoolExecutor executorReadWord;
	private static ScheduledThreadPoolExecutor executorStackElement;
	private static ScheduledFuture<?> t;
	private JButton start;
	private JButton pause;
	private JButton stop;
	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread startlog = new Thread(makeDir("LOGFILE_PATH"));
					startlog.start();
					try {
						fh = new FileHandler(LOGFILE_PATH+"\\"+LOGFILE_NAME);
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IOException e){
						e.printStackTrace();
					}
					logger.addHandler(fh);
					SimpleFormatter formatter = new SimpleFormatter();
					fh.setFormatter(formatter);
					logger.log(Level.INFO, "Parte l'applicazione.");
					MainWindow window = new MainWindow(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setConfigurationBuilder(){
	}
	
	
	/**
	 * Create the application.
	 */
	public MainWindow() {
		logger.log(Level.INFO, "Creo l'executors.");
		executor = new ScheduledThreadPoolExecutor(40);
		executorReadWord = new ScheduledThreadPoolExecutor(40);
		executorStackElement = new ScheduledThreadPoolExecutor(40);
		executor.submit(initialize());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private Runnable initialize() {
		return new Runnable() {
			
			@Override
			public void run() {
				GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				try {
					UIManager.setLookAndFeel(SyntheticaSimple2DLookAndFeel.class.getName());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(InstantiationException e){
					e.printStackTrace();
				}catch(IllegalAccessException e){
					e.printStackTrace();
				}catch(UnsupportedLookAndFeelException e){
					e.printStackTrace();
				}
				Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
				int Displaywidth = gd.getDisplayMode().getWidth();
				int Displayheight = gd.getDisplayMode().getHeight();
				int WindowsWidth = Displaywidth/2;
				int WindowsHeight = Displayheight/4;
				logger.log(Level.INFO, "Carico i paramentri grafici.", LOGFILE_PATH);
				frame = new JFrame();
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent arg0) {
						System.gc();
					}
					@Override
					public void windowClosing(WindowEvent e) {
						//List<Future<Runnable>> answers = executor.invokeAll((Collection<? extends Callable<Runnable>>) executor.shutdownNow());
						
						System.exit(0);
					}
				});
				frame.setBounds(center.x-WindowsWidth/2,center.y-WindowsHeight/2,WindowsWidth,WindowsHeight);
				frame.setResizable(false);
				//frame.setBounds(100, 100, 450, 300);
				//frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().setLayout(new GridLayout(3, 4, 5, 5));
				
				Image icon = Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/img/icona.png"));
			    frame.setIconImage(icon);
			    frame.setTitle("InterHJ - Hi Jack");
			    
				panel_start_stop = new JPanel();
				frame.getContentPane().add(panel_start_stop);
				
				start = new JButton("");
				start.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						t = executor.scheduleAtFixedRate(fillStackElement(), 0, 1, TimeUnit.MINUTES);
						start.setIcon(new ImageIcon(MainWindow.class.getResource("/img/play_gray.png")));
					}
				});
				start.setIcon(new ImageIcon(MainWindow.class.getResource("/img/play.png")));
				panel_start_stop.add(start);
				
				pause = new JButton("");
				pause.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						executor.shutdown();
					}
				});
				pause.setIcon(new ImageIcon(MainWindow.class.getResource("/img/pause.png")));
				panel_start_stop.add(pause);
				
				stop = new JButton("");
				stop.setIcon(new ImageIcon(MainWindow.class.getResource("/img/stop.png")));
				stop.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						executor.shutdownNow();
					}
				});
				panel_start_stop.add(stop);
				
				pannello_sup = new JPanel();
				frame.getContentPane().add(pannello_sup);
				pannello_sup.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				
				pannello_sup_jlabel = new JPanel();
				FlowLayout fl_pannello_sup_jlabel = (FlowLayout) pannello_sup_jlabel.getLayout();
				fl_pannello_sup_jlabel.setAlignment(FlowLayout.RIGHT);
				pannello_sup.add(pannello_sup_jlabel);
				
				JLabel lblPathDellaCartella = new JLabel("Path della cartella file *.DOC dello HPLC: ");
				pannello_sup_jlabel.add(lblPathDellaCartella);
				
				pannello_sup_jtext = new JPanel();
				pannello_sup.add(pannello_sup_jtext);
				
				textField = new JTextField();
				pannello_sup_jtext.add(textField);
				textField.setColumns(20);
				
				pannello_sup_button = new JPanel();
				pannello_sup.add(pannello_sup_button);
				
				btnNewButton = new JButton("");
				pannello_sup_button.add(btnNewButton);
				btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				btnNewButton.setToolTipText("Clicca qui per fissare l'indirizzo assoluto dei file *.DOC dello HPLC.");
				btnNewButton.setIcon(new ImageIcon(MainWindow.class.getResource("/de/javasoft/plaf/synthetica/simple2D/icons/folderOpenIcon.png")));
				btnNewButton.setSelectedIcon(null);
				
				btnNewButton_1 = new JButton("");
				pannello_sup_button.add(btnNewButton_1);
				btnNewButton_1.setToolTipText("Cerca percorso di salvataggio dello HPLC.");
				btnNewButton_1.setIcon(new ImageIcon(MainWindow.class.getResource("/de/javasoft/plaf/synthetica/simple2D/icons/listViewIcon.png")));
				
				panello_inf = new JPanel();
				frame.getContentPane().add(panello_inf);
				
				panello_inf_jlabel = new JPanel();
				panello_inf.add(panello_inf_jlabel);
				
				JLabel lblPathDiSalvataggio = new JLabel("Path di salvataggio di InterHJ:                  ");
				panello_inf_jlabel.add(lblPathDiSalvataggio);
				
				panello_inf_jtext = new JPanel();
				panello_inf.add(panello_inf_jtext);
				
				textField_1 = new JTextField();
				panello_inf_jtext.add(textField_1);
				textField_1.setColumns(20);
				
				panello_inf_button = new JPanel();
				panello_inf.add(panello_inf_button);
				
				btnNewButton_2 = new JButton("");
				panello_inf_button.add(btnNewButton_2);
				btnNewButton_2.setIcon(new ImageIcon(MainWindow.class.getResource("/de/javasoft/plaf/synthetica/simple2D/icons/folderOpenIcon.png")));
				
				btnNewButton_3 = new JButton("");
				panello_inf_button.add(btnNewButton_3);
				btnNewButton_3.setToolTipText("Inserisci percorso di salvataggio dell'app.");
				btnNewButton_3.setIcon(new ImageIcon(MainWindow.class.getResource("/de/javasoft/plaf/synthetica/simple2D/icons/listViewIcon.png")));
				frame.setVisible(true);
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser  chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int value = chooser.showOpenDialog(null);
						if(value == JFileChooser.APPROVE_OPTION){
							percfile = chooser.getSelectedFile().getAbsolutePath();
							textField.setText(percfile);
						}
					}
				});
				btnNewButton_2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser  chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int value = chooser.showOpenDialog(null);
						if(value == JFileChooser.APPROVE_OPTION){
							pathtosave = chooser.getSelectedFile().getAbsolutePath();
							textField_1.setText(pathtosave);
						}
					}
				});
				
				
				textField.setText("C:\\Users\\HiJack\\Desktop\\prova");
				textField_1.setText("C:\\Users\\HiJack\\Desktop\\diversa");
			
		
		logger.log(Level.INFO, "Parto con lo stack degli elementi da elaborare in '"+percfile+"'");
		executor.execute(controll_dir());
		executorStackElement.scheduleAtFixedRate(fillStackElement(), 1, 5, TimeUnit.MINUTES);
		}};
		
	}

	private Runnable controll_dir(){
		return new Runnable() {
			
			@Override
			public void run() {
				percfile=textField.getText();
				percfile.replace("\\\\","\\");
				pathtosave = textField_1.getText();
				pathtosave.replace("\\\\", "\\");
				if (pathtosave.isEmpty()) {
					logger.log(Level.INFO, "La textfield del path si salvataggio del programma è vuoto, controllo le cartelle assolute posizionate in 'C:\\interfacciamento'");
					usePath = true;
					executor.execute(makeDir(PATH_OLD));
					executor.execute(makeDir(PATH_RESULT));
				}else{
					logger.log(Level.INFO, "La textfield del path di salvataggio contiene un path, controllo l\'esistenza delle cartelle 'file_processati' e 'elaborati_e_convertiti' in '"+pathtosave+"'");
					usePath = false;
					executor.execute(makeDir(pathtosave+"\\file_processati"));
					executor.execute(makeDir(pathtosave+"\\elaborati_e_convertiti"));
				}
			}
		};
	}
	
	public static Runnable makeDir(final String path)
	{
		return new Runnable() {
			
			@Override
			public void run() {
				File directory = new File(path);
				if(directory.exists()){
					logger.log(Level.INFO, "La directory \""+path+"\" già esiste.");
					if (directory.isFile())		logger.log(Level.INFO, "Impossibile creare \""+path+"\", la cartella già esiste oppure ha lo stesso nome di un file nella root.");
				}else{
					logger.log(Level.INFO, "Creo cartella '"+path+"'.");
					directory.mkdirs();
				}
			}
		};
	}
	
	private Runnable fillStackElement(){
		return new Runnable() {
			
			@Override
			public void run() {
				//executor.execute(controll_dir());
				logger.log(Level.INFO, "Inizio controllo se ci sono nuovi file da elaborare.");
				File dirPercFile = new File(percfile);
				logger.log(Level.INFO, "Elaboro i file nella cartella '"+percfile+"'.");
				File[] fileEntry = dirPercFile.listFiles();
				for (int i = 0; i<fileEntry.length;i++) {
					if (!fileEntry[i].isDirectory()) {
						if((stackElement.search(fileEntry[i].getName())==-1)||((stackelementprocessing.search(fileEntry[i].getName())==-1)&&(stackelementprocessing.isEmpty()))){
							logger.log(Level.INFO, "File trovato: '"+fileEntry[i].getName()+"'");
							stackElement.push(fileEntry[i].getName());
						}
						else logger.log(Level.INFO, "Il File è già presente nello stack.");
					}
				}
				logger.log(Level.INFO, "File: "+stackElement.toString()+".\n\n");
				logger.log(Level.INFO, "Fine controllo se ci sono nuovi file da elaborare.");
				logger.log(Level.INFO, "Inizio elaborazione file nello stack.");
				Thread scheduler = null;
				if(usePath) {
					scheduler = new Thread(scheduleConvertion(PATH_,percfile));
					scheduler.start();
				}else{
					scheduler = new Thread(scheduleConvertion(pathtosave,percfile));
					scheduler.start();
				}
			}
		};
	}
	
	boolean iscreatedirimg = false;
	boolean iscreatedirelabor = false;
	String elementtoprocessing = null;
	String[] paragrafi = null;
	String outnamefile = null;
	
	private Runnable scheduleConvertion(final String wheresavefile, final String elementpathelabor){
		return new Runnable() {


			public synchronized void run() {
				Iterator<String> itStack = stackElement.iterator();
				final String wheresave = wheresavefile;
				String elementpath = elementpathelabor;
				boolean iscreateparagraph = false;
				FutureTask<Object> svg = null;
				boolean ok = false;
				Document doc = null;
				FileOutputStream FoS = null;
				PdfContentByte Canvaselem = null;
				boolean test = false;
				while(itStack.hasNext()){
					elementtoprocessing = itStack.next();
					executor.execute(moveElaboratedFile(elementpath+"\\"+elementtoprocessing, wheresave+"\\"+elementtoprocessing+".doc"));
					logger.log(Level.INFO,"Stack degli elementi contiene: "+stackElement.toString());
					logger.log(Level.INFO,"L'elemento elaborato è: '"+elementtoprocessing+"'");
					FileInputStream fISWord = null;
					boolean svgfinish = false;
					FutureTask<Object> futurefileinputstream = (FutureTask<Object>) executor
							.submit(new CreateFileInputStreamFromWord(wheresave,
									elementtoprocessing+".doc"));
					if (fISWord == null) {
						logger.log(Level.INFO,"Creo il FIS del doc word '"+elementtoprocessing+"'.");
						try {
							fISWord = (FileInputStream) futurefileinputstream.get();
						} catch (InterruptedException e) {
							logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Fis del file: '"+elementtoprocessing+"' (InterruptedException) \n", e.fillInStackTrace());
						} catch (ExecutionException e) {
							logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Fis del file: '"+elementtoprocessing+"' (ExecutionException) \n", e.fillInStackTrace());
						}
					}
					File savepathimg = new File(wheresave + "\\img");
					if (!savepathimg.exists()) {
						logger.log(Level.INFO,"Creo la cartella img.");
						iscreatedirimg = savepathimg.mkdirs();
					} else {
						logger.log(Level.INFO,"La cartella img esiste.");
						iscreatedirimg = true;
					}
					if (iscreatedirimg) {
						logger.log(Level.INFO,"Estraggo immagine svg di '"+elementtoprocessing+"'.");
						svg = (FutureTask<Object>) executor
								.submit(new ExtractImageWordToFile(fISWord, wheresavefile
										+"\\img\\" + elementtoprocessing));
						try {
							svgfinish = (Boolean) svg.get();
						} catch (InterruptedException e) {
							logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione del svg del file: '"+elementtoprocessing+"' (InterruptedException) \n", e.fillInStackTrace());
						} catch (ExecutionException e) {
							logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione del svg del file: '"+elementtoprocessing+"' (ExecutionException) \n", e.fillInStackTrace());
						}
					} else {
						logger.log(Level.INFO,"Creo cartella "+wheresave + "\\img.");
						FutureTask<Object> createDir = (FutureTask<Object>) executor
								.submit(new Callable<Object>() {

									@Override
									public Object call() throws Exception {
										File savepath = new File(wheresave + "\\img");
										iscreatedirimg = savepath.mkdirs();
										return iscreatedirimg;
									}
								});
						try {
							logger.log(Level.INFO,"Creo cartella "+wheresave + "\\img.");
							test = (Boolean) createDir.get();
						} catch (InterruptedException e) {
							logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione della cartella: '"+wheresavefile + "\\img' (InterruptedException) \n", e.fillInStackTrace());
						} catch (ExecutionException e) {
							logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione della cartella: '"+wheresavefile + "\\img' (ExecutionException) \n", e.fillInStackTrace());
						}
						if(test){
							logger.log(Level.INFO,"Estraggo svg da file word.");
							svg = (FutureTask<Object>) executor
									.submit(new ExtractImageWordToFile(fISWord, wheresave
											+ "\\img\\" + elementtoprocessing));
							try {
								svgfinish = (Boolean) svg.get();
							} catch (InterruptedException e) {
								logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione del svg del file: '"+elementtoprocessing+"' (InterruptedException) \n", e.fillInStackTrace());
							} catch (ExecutionException e) {
								logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione del svg del file: '"+elementtoprocessing+"' (ExecutionException) \n", e.fillInStackTrace());
							}
						}
					}
					while(!svgfinish){

					}
					if(svgfinish){
						logger.log(Level.INFO,"Immagine estratta con successo di '"+elementtoprocessing+"'.");
					}else{
						logger.log(Level.INFO,"Errore durante l'estrazione dell'immagine '"+elementtoprocessing+"'.");
					}
					logger.log(Level.INFO,"Estraggo paragrafi testuali da file word '"+elementtoprocessing+"'.");
					FutureTask<Object> extractParag = null;
					try {
						extractParag = (FutureTask<Object>) executor
								.submit(new ExtractParagraphFromWord(fISWord));
					} catch (IOException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione dei paragrafi del file: '"+elementtoprocessing+"' (IOException) \n", e.fillInStackTrace());
					}
					while(!extractParag.isDone()&&paragrafi!=null&&!svg.isDone()){
						System.out.println("Estraggo paragrafi ed immagine!!!");
					}
					try {
						paragrafi = (String[]) extractParag.get();
					} catch (InterruptedException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione dei paragrafi del file: '"+elementtoprocessing+"' (InterruptedException) \n", e.fillInStackTrace());
					} catch (ExecutionException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante l'estrazione dei paragrafi del file: '"+elementtoprocessing+"' (ExecutionException) \n", e.fillInStackTrace());
					}
					logger.log(Level.INFO,"Paragrafi testuali estratti con successo di '"+elementtoprocessing+"'.");
					logger.log(Level.INFO,"Creo il Chunk di '"+elementtoprocessing+"'ed estraggo il nome del file PDF e JPEG.");

					FutureTask<Object> Chunk= (FutureTask<Object>) executor
							.submit(new CreateChunk(paragrafi));
					while(!Chunk.isDone()){

					}
					Stack<Chunk> lista = null;
					try {
						lista = (Stack<Chunk>) Chunk.get();
					} catch (InterruptedException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Chunk del file: '"+elementtoprocessing+"' (InterruptedException) \n", e.fillInStackTrace());
					} catch (ExecutionException e) {
						logger.logp(Level.WARNING, ReadWord.class.getClass().toString(), this.getClass().toString(), "Eccezzione durante la creazione del Chunk del file: '"+elementtoprocessing+"' (ExecutionException) \n", e.fillInStackTrace());
					}
					logger.log(Level.INFO,"Chunk creato con successo di '"+elementtoprocessing+"'.");
					outnamefile = lista.get(lista.size()-1).toString();
					logger.log(Level.INFO,"Il nome del documento pdf e jpeg sarà: "+outnamefile);
					lista.remove(lista.get(lista.size()-1));
					logger.log(Level.INFO,"Creo il documento PDF di '"+elementtoprocessing+"'.");

					FutureTask<Object> PDFDoc= (FutureTask<Object>) executor
							.submit(new CreatePDFDocument(elementtoprocessing));

					logger.log(Level.INFO,"Creo il FOS per scrivere nel PDF di '"+elementtoprocessing+"'.");
					FutureTask<Object> FOS = null;

					File savepathelabor = new File(wheresavefile + "\\elaborati_e_convertiti");
					if (!savepathelabor.exists()) {
						logger.log(Level.INFO,"Creo la cartella elaborati_e_convertiti.");
						iscreatedirelabor = savepathelabor.mkdirs();
					} else {
						logger.log(Level.INFO,"La cartella esiste.");
						iscreatedirelabor = true;
					}
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
					logger.log(Level.INFO,"Documento PDF creato con successo di '"+elementtoprocessing+"'.");
					logger.log(Level.INFO,"FOS creato di '"+elementtoprocessing+"'.");
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
					logger.log(Level.INFO,"Creo Canvas di '"+elementtoprocessing+"'.");
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
					logger.log(Level.INFO,"Canvas creato di '"+elementtoprocessing+"'.");
					FutureTask<Object> PDFConc = (FutureTask<Object>) executor.submit(new CreatePDF(doc, lista, wheresavefile
							+ "\\img\\" + elementtoprocessing + ".svg"));
					while(!PDFConc.isDone()){

					}
					FutureTask<Object> ConvertPdfConc = (FutureTask<Object>) executor.submit(new ConvertFromPdfToPng(wheresavefile,outnamefile));
					while(!ConvertPdfConc.isDone()){

					}
				}
			}
		};
	}
	
	public static Runnable moveElaboratedFile(final String source, final String dest){
		return new Runnable() {
			
			@Override
			public void run() {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new FileInputStream(source);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out = new FileOutputStream(dest);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				byte[] buf = new byte[1024];
				int len;
				try {
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	private static void msgbox(String s){
		JOptionPane.showMessageDialog(null, s);
	}
}
