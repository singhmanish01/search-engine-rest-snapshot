package com.mss.searchengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class GetMeta {

	public static void main(String[] args) throws IOException, SAXException, TikaException {
		// TODO Auto-generated method stub
		File file = new File("C:\\Users\\manish $ingh\\Downloads\\General\\Music\\file_example_WAV_1MG.wav");
		String[] arr = file.getName().split("\\.");
		//System.out.println(arr[arr.length-1]);
		Tika tika = new Tika();
		System.out.println(tika.detect(file));
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream is = new FileInputStream(file);
		ParseContext context = new ParseContext();
		parser.parse(is, handler, metadata, context);
		System.out.println(handler.toString() + Arrays.toString(file.getName().split("\\W+")));
		String[] list = metadata.names();
		for(String s: list)
			System.out.println(s);
	}

}
