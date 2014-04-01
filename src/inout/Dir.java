package inout;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Dir {

	static String sep = File.separator;

	public static HashMap<String, File> paths = new HashMap<String, File>();
	public static HashMap<String, File> files = new HashMap<String, File>();

	public static void initPaths() {

		paths.put("res", new File("res" + sep));
		paths.put("scenes", new File("scenes" + sep));
		paths.put("gui", new File("res" + sep + "GUI" + sep));
		paths.put("plugins", new File("plugins" + sep));
		paths.put("models", new File("res" + sep + "model" + sep));
		paths.put("logs", new File("logs" + sep));
		files.put("keymap", new File("res" + sep + "keymap.xml" + sep));

		for (File path : paths.values()) {
			if (!path.exists()) {
				path.mkdir();
			}
		}

		try {
			for (File file : files.values()) {
				if (!file.exists()) {
					file.createNewFile();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
