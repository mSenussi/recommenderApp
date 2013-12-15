package com.recommender.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
	
	public static void generateUserDataPrefFile(File userPreferencesFile) throws IOException{
		Random r = new Random();
		FileWriter fw = new FileWriter(userPreferencesFile);
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i=0; i<100000; i++){
			bw.write(r.nextInt(1000)+","+r.nextInt(1000)+","+(r.nextInt(5)+1)+"\r\n");
		}
		bw.close();		
	}
}
