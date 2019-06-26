package org.feygo.ksim.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import org.feygo.ksim.tools.AAL;

import com.google.gson.Gson;

public class ConfLoader {

	public static void main(String[] args) {

		SimBoardConf conf=ConfLoader.getSimConfByFile("C:\\1.dev\\workspace\\Simboard\\conf\\SimboardConf.json");
		AAL.a(conf.toString());
	}
	public static SimBoardConf getSimConfByFile(String filePath) {
		Gson gson=new Gson();
		SimBoardConf conf=null;
		try {
			String encoding = "UTF-8";
			File file=new File(filePath);			
			FileReader fileReader =new FileReader(file, Charset.forName(encoding));
			conf=gson.fromJson(fileReader, SimBoardConf.class);			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conf;
	}

}
