package com;

import java.io.*;

public class Logger {
	
	File file;
	FileWriter writer;

	public Logger(String fileName, String[] fieldNames) throws IOException {

		File directory = new File("logs\\");
		File file = new File("logs\\" + fileName + ".csv");
		this.file = file;
		writer = new FileWriter(file);

		directory.mkdir();
		file.createNewFile();

		LogLine(fieldNames);

	}

	public Logger(String fileName) throws IOException {

		File directory = new File("logs\\");
		File file = new File("logs\\" + fileName + ".csv");
		this.file = file;
		writer = new FileWriter(file);

		directory.mkdir();
		file.createNewFile();

	}

	public void LogLine(double[] values) throws IOException {

		for (int i = 0; i < values.length; i++) {

			if (i == (values.length - 1)) {
				writer.write(String.valueOf(values[i]));
			} else {
				writer.write(String.valueOf(values[i]) + ", ");
			}

		}

		writer.write((char) 13);
		writer.write((char) 10);

	}

	public void LogLine(String[] values) throws IOException {

		for (int i = 0; i < values.length; i++) {

			if (i == (values.length - 1)) {
				writer.write(values[i]);
			} else {
				writer.write(values[i] + ", ");
			}

		}
		
		writer.write((char) 13);
		writer.write((char) 10);

	}

	public void LogLine(float[] values) throws IOException {

		for (int i = 0; i < values.length; i++) {

			if (i == (values.length - 1)) {
				writer.write(String.valueOf(values[i]));
			} else {
				writer.write(String.valueOf(values[i]) + ", ");
			}

		}

		writer.write((char) 13);
		writer.write((char) 10);

	}

}
