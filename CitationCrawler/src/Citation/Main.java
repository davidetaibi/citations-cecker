package Citation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.SeekableByteChannel;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    
	public static void main(String[] args) throws Exception {
		

		// Pattern.UNICODE_CHARACTER_CLASS
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        System.out.print("Enter Author Name : ");
	        String author = br.readLine();
		System.out.print("Citation in Google Scholar : " +crawl_gsc(author));
		//crawl_gsc("Johannes Gehrke");
		//crawl_gsc("alikabar javadi amoli");
		//crawl_gsc("Movies");	
		
	}
	public static void writefile(String html){
		FileOutputStream fop = null;
		File file;
		String content = html;

		try {

			file = new File("c:/newfile.txt");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String GetHtml(String url){
		String content = null;
		URLConnection connection = null;
		try {
		  connection =  new URL(url).openConnection();		  
		  connection.addRequestProperty("User-Agent", 
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			
		  Scanner scanner = new Scanner(connection.getInputStream());
		  scanner.useDelimiter("\\Z");
		  content = scanner.next();
		}catch ( Exception ex ) {
		    ex.printStackTrace();
		}
		return content;
	}
	
	public static int crawl_gsc(String author) throws Exception{
		String[] names = author.split(" ");
		String FinalURL="https://scholar.google.com/scholar?hl=en&q=";
		
		for(int j=0;j<names.length;j++){
			FinalURL += names[j];
			if(j != names.length-1){
				FinalURL += "+";
			}
		}
		
		FinalURL += "&btnG=&as_sdt=1%2C5&as_sdtp=";
		
	
		

		
		String html = GetHtml(FinalURL);
	//	writefile(html);
		
		String regex_exist_person ="\\bUser profiles for\\b";
		Pattern pattern_exist_person = Pattern.compile(regex_exist_person);
		Matcher matcher_exist_person = pattern_exist_person.matcher(html);
		
		if(matcher_exist_person.find())
		{
			//html = html.toLowerCase();
			String selector_start="Cited by ";
			String selector_end = ".*div";
			
		    String regex = selector_start+ "\\w*"+ selector_end;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(html);
			
			List<String> articles = new ArrayList<String>();	
		       while(matcher.find()) {
		    	   int start = matcher.start();
		    	   int end = matcher.end();
		    	 //  System.out.println("start " + start);
		    	  // System.out.println("end " +end);
		    	   String article = html.substring(start+selector_start.length(),start+selector_start.length()+10);
		    	   article = article.replaceAll("[^0-9]", "");
		    	   articles.add(article);
		    	   break;
		      
		      }
		      for(int k=0;k<articles.size();k++){
		    	  System.out.println(articles.get(k));
		      }
			
		      
		}
		else
		{
			// NOT FOUND
			
			return 0;
		}

		
		return 0;		
	}
	/*
	 public List<String> crawl_gsc(String author){

		String url = "https://scholar.google.com/scholar?hl=en&q=Francesco+Ricci&btnG=&as_sdt=1%2C5&as_sdtp=";
		String selector_start="@@";
		String selector_end = "/@@";
		
	    String regex = selector_start+ "\\w*"+ selector_end;
	    
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		
		int number_of_article = 0;
		List<String> articles = new ArrayList<String>();	
	       while(matcher.find()) {
	    	   number_of_article++;
	    	   int start = matcher.start() + selector_start.length();
	    	   int end = matcher.end() - selector_end.length();
	    	   String article = html.substring(start,end);
	    	   articles.add(article);
	         
	      }
	       
	System.out.println("Article : "+ number_of_article);
	       for(int i=0;i<articles.size();i++){
	    	   System.out.println(articles.get(i));
	       } 
		return articles;
		
	}

*/
}
