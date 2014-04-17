package ChatStructure;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import parser.Stemmer;
import parser.StopWords;

public class Message {

	private String sentence, realsentence;
	private String id;
	private int words, totalwords;
	private double length;
	private boolean isQuestion, isURL, isSmall, isMail;
	private double sentiscore, tfidf, tfisf,similarityscore;
	private boolean inSummery;
	
	public double getSimilarityscore() {
		return similarityscore;
	}
	public void setSimilarityscore(double similarityscore) {
		this.similarityscore = similarityscore;
	}

	private List<String> strtoken;
	
	public Message(){
		length = 0;
		words = 0;
		totalwords = 0;
		sentiscore = tfidf = tfisf = 0.0;
		isQuestion = false;
		isURL = false;
	}
	public Message(String str, StringTokenizer st,InputStream fis){
		realsentence = str;
		setSentence(str);
		length = 0.0;
		sentiscore = tfidf = tfisf = 0.0;
		isQuestion = isMail = false;
		isURL = false;
		strtoken = new ArrayList<String>();
		StopWords stopwords = new StopWords(fis);
		Stemmer stem = new Stemmer();
		
		while(st.hasMoreTokens()){
			totalwords++;
			String token = st.nextToken().trim().toLowerCase().trim();
			if(token.compareTo("http") == 0 || token.compareTo("www") == 0 || token.compareTo("https") == 0){
				isURL = true;
			}
			
			if(stopwords.checkNotStopWords(token)){
				continue;
			}						
			stem.add(token.toCharArray(), token.length());
			stem.stem();
			token = stem.toString().trim();
			if(stopwords.checkNotStopWords(token) || (token.length() == 1) || (token.length() == 0)){
				continue;
			}
			
			strtoken.add(token);
		}
		length = words = strtoken.size();
		if(words <= 4)
			isSmall = true;
		else
			isSmall = false;
			
	}
	
	public void setSentence(String str){
		sentence = str;
		if(str.matches(".*[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}).*"))
			isMail = true;
		
		if(str.endsWith("?") ){
			setQuestion();
		}
	}
	
	public void setLength(double len){
		length = len;
	}
	public void setSummery(boolean flag){
		inSummery = flag;
	}
	public void setID(String vid){
		id = vid;
	}
	private void setQuestion(){
		isQuestion = true;
	}
	public void setSentiScore(double score){
		sentiscore = score/words;
	}
	
	public void setTFIDF(double score){
		tfidf = Math.abs(score)/words;
	}
	
	public void setTFISF(double score){
		tfisf = Math.abs(score)/words;
	}
	
	
	public String getSentence(){
		return sentence;
	}
	public String getRealSentence(){
		return realsentence;
	}
	public boolean getSummery(){
		return inSummery;
	}
	public List<String> getStringTokens(){
		return strtoken;
	}
	public double getLength(){
		return length;
	}
	public double getWords(){
		return words;
	}
	public int getTotalWords(){
		return totalwords;
	}
	public boolean getQuestion(){
		return isQuestion;
	}
	
	public double getSentiScore(){
		return sentiscore;
	}
	public double getTFIDF(){
		return tfidf;
	}
	public double getTFISF(){
		return tfisf;
	}
	public double getScore(){
		if(isURL == false && isSmall == false && isMail == false){
			if(isQuestion == false)		
				return  (tfidf+(tfisf) + length+sentiscore + (similarityscore*5))/(4.5);
			else
				return  (0.7*(tfidf+(tfisf) + length+sentiscore + (similarityscore*5)))/4.5;
		}
		else
			return  (0.5*(tfidf+(tfisf)+length+sentiscore + (similarityscore*5)))/4.5;
		//return (tfidf);
	}
	
	public String getID(){
		return id;
	}
	
	
}
