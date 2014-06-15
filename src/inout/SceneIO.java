package inout;

import java.io.*;
import java.util.zip.*;
import javax.swing.*;
import javax.xml.*;

/**
 * 
 * @author Bradley Pellegrini
 * 
 *         Scene Loading and Saving to XML file type as well as compression of
 *         scene folders.
 * 
 */
public class SceneIO {

	// TODO Only the method names and general idea has been written. All actual
	// coding is yet to be done. Work on this class has not been completed due to
	// time constraints and family matters.

	/**
	 * Method to compress the XML files for the scene.
	 * 
	 * @param aFile
	 *            the file to be compressed.
	 * @return true if the file is compressed successfully. false if file is not
	 *         compressed.
	 */
	private boolean compressFile(File aFile) {
		// TODO Write Method
		return true;
	}

	/**
	 * Method to expand a compressed XML file created by the physics engine.
	 * File should be either compressed or deleted at the end of runtime.
	 * 
	 * @param compressedFile
	 *            the compressed file being passed in.
	 * @return true if the file is expanded correctly, false if the file is not
	 *         expanded.
	 */
	private boolean decompress(File compressedFile) {
		// TODO Write Method
		return true;
	}

	/**
	 * expands and opens the file for reading.
	 * 
	 * @param fileName
	 *            the File to open.
	 * @return the file
	 */
	public File loadSceneFile(String fileName) {
		File sceneFile = new File("..//Scenes//" + fileName + ".zip");
		// TODO Write Method
		return sceneFile;
	}

	/**
	 * 
	 * @return true if the file is saved and compressed, false if the save or
	 *         compression fails.
	 */
	private boolean saveSceneFile(File fileName) {
		// TODO Write Method
		return true;
	}

	/**
	 * 
	 * @param images
	 *            all the image files that must be put into the scene file.
	 * @param properties
	 *            all the properties that must be moved with the files.
	 * @return
	 */
	private boolean gatherFiles(String[] images, String[] properties) {
		// TODO Write Method
		return true;
	}

	/**
	 * Writes data to file in XML form. Assumes file is already open
	 * 
	 * @param outputFile
	 *            the file to write to.
	 * @param dataFile
	 *            the dataFile created by gatherFile and the use of a tarball
	 * @return true if file is successfully written to.
	 */
	public boolean writeToFile(File outputFile, File dataFile) {
		try {
			FileWriter writer = new FileWriter(outputFile);
		} catch (FileNotFoundException e) {
			// TODO Create File if it doesn't exist

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
