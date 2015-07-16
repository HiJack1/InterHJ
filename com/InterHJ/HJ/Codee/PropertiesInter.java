package com.InterHJ.HJ.Codee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

public class PropertiesInter {

	private Properties propDefault = null;
	private Properties prop = null;
	private InputStream inputStream = null;
	private FileOutputStream outputStream = null;
	private InputStream inputStreamDefault = null;

	public String PropertiesInter() {
		inputStreamDefault = getClass().getClassLoader().getResourceAsStream(
				"config-default.properties");
		propDefault = new Properties();
		if (inputStreamDefault != null) {
			try {
				propDefault.load(inputStreamDefault);
			} catch (IOException e) {

				e.printStackTrace();
			}
		} else {
			try {
				throw new FileNotFoundException(
						"property file 'config-default.properties' not found in the classpath");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (propDefault.getProperty("read-default").contains("false"))
			ConstructPropertiesFromDefault();
		else
			LoadProperties();
		String user = propDefault.getProperty("read-default");
		return user;
	}

	private void ConstructPropertiesFromDefault() {
		Properties props = new Properties();
		props.setProperty("read-default",
				propDefault.getProperty("read-default"));
		File f = new File("config.properties");
		OutputStream out = null;
		try {
			out = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			props.store(out,
					"Memorizzo le informazioni caricate dal config di default.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void LoadProperties() {

	}

	private static void msgbox(String s) {
		JOptionPane.showMessageDialog(null, s);
	}
}
