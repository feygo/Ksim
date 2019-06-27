package org.feygo.ksim.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.feygo.ksim.tools.AAL;

import com.google.gson.Gson;

public class ConfLoader {

	public static void main(String[] args) {

		SimBoardConf conf=ConfLoader.getSimConfByFile("C:\\1.dev\\git-repository\\Ksim\\bin\\conf\\SimboardConf.json");
		AAL.a(conf.toString());
	}
	public static SimBoardConf getSimConfByFile(String filePath) {
		Gson gson=new Gson();
		SimBoardConf conf=null;
		try {
			String encoding = "UTF-8";
			File file=new File(filePath);		
			AAL.a(file.toString());
			AAL.a(file.getName());
			FileReader fileReader =new FileReader(file, Charset.forName(encoding));
			conf=gson.fromJson(fileReader, SimBoardConf.class);		
			conf.fresh();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conf;
	}
	public static SimBoardConf getSimConfByFile(URL filePath) {
		Gson gson=new Gson();
		SimBoardConf conf=null;
		try {
			String encoding = "UTF-8";
			URI fileUri=filePath.toURI();
			File file=new File(fileUri);			
			FileReader fileReader =new FileReader(file, Charset.forName(encoding));
			conf=gson.fromJson(fileReader, SimBoardConf.class);		
			conf.fresh();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conf;
	}

}
