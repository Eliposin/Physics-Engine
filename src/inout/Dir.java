package inout;

import java.io.File;

public class Dir {
	
	static String sep = File.separator;
	
	public static File scenes = new File("scenes" + sep);
	public static File GUIIcons = new File("res" + sep + "GUI" + sep);
	public static File plugins = new File("plugins" + sep);
	public static File models = new File("res" + sep + "model" + sep);
	public static File logs = new File("logs" + sep);
	
	public static void initPaths() {
		
		if (!scenes.exists()) {
			scenes.mkdir();
		}
		if (!GUIIcons.exists()) {
			GUIIcons.mkdir();
		}
		if (!plugins.exists()) {
			plugins.mkdir();
		}
		if (!models.exists()) {
			models.mkdir();
		}
		if (!logs.exists()) {
			logs.mkdir();
		}
	}

}
