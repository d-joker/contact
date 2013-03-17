package com.musicplayer.helpers;

import java.io.File;
import java.util.ArrayList;

import com.musicplayer.filter.MyFilenameFilter;

public class ReaderHelper {
	private static final String rootPath= "/mnt/emmc/music/";
	
	public static File[] getSongs() {
		File root = new File(rootPath);
		return root.listFiles(new MyFilenameFilter());
		
	}
	

}
