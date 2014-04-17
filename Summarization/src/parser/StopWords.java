package parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StopWords {

	private Set<String> stopwordsmap;
	
	public StopWords(InputStream fis)
	{
		stopwordsmap = new HashSet<String>();
		
		//InputStream    fis;
		BufferedReader br;
		String         line;

		try{
			fis= StopWords.class.getClassLoader().getResourceAsStream("stopwords.txt");
		//	fis = new FileInputStream("stopwords.txt");
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				stopwordsmap.add(line);
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		br = null;
		fis = null;		
	}
	
	public boolean checkNotStopWords(StringBuilder str){		
		return stopwordsmap.contains(str.toString());		
	}
	public boolean checkNotStopWords(String str){		
		return stopwordsmap.contains(str);		
	}
	
}
