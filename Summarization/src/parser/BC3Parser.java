package parser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ChatStructure.EmailThread;
import ChatStructure.Message;
import ChatStructure.SingleMail;

public class BC3Parser {

	private FileInputStream fstream;
	private DataInputStream in;
	private InputStream sentinetfis,stopwordfis;
	
	private BufferedReader br;
	private Pattern patternI=Pattern.compile("([^A-Z|^a-z|^0-9]|^)(I)\\s+",Pattern.DOTALL|Pattern.MULTILINE);
	private Pattern patternU=Pattern.compile("([^A-Z|^a-z|^0-9]|^)(you)\\s+",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	private Pattern patternT=Pattern.compile("(today)|(Today)|(tomorrow)|(Tomorrow)|(yesterday)|(Yesterday)|(week(s)*)|(Week(s)*)");
	
	private Matcher matcher;
	//String day,date,month,year,time,parsedDate;
	Calendar calendar;
	//DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
	DateFormat dateFormatIST = new SimpleDateFormat("EEE MMM dd hh:mm:ss Z yyyy");
	
	TitleSimilarity title,sentence;
	boolean istitle=false;
	public void bc3Parser(String uri, List<EmailThread> convers, Map<String, Integer> mapwordcount,InputStream stopwordfis,InputStream sentinetfis){
		try{
			fstream =new FileInputStream(new File(uri));
			in=new DataInputStream(fstream) ;
			br = new BufferedReader(new InputStreamReader(in));
			title = new TitleSimilarity();
			sentence = new TitleSimilarity();
			this.stopwordfis=stopwordfis;
			this.sentinetfis=sentinetfis;
			
		}
		catch(IOException e){
			System.out.println("EXCEPTION : "+e.getMessage());
		}
		fileOperation(convers, uri);
		
		
	}
	private void fileOperation(final List<EmailThread> convers, String uri) {
		// TODO Auto-generated method stub
		
		//final StopWords stopwords = new StopWords();
		//final Stemmer stem = new Stemmer();
		
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {
				 
				boolean froot = false;
				boolean fthread = false;
				boolean fname = false;
				boolean flist = false;
				boolean fdoc = false;
				boolean ftimestamp = false;
				boolean ffrom = false;
				boolean fto = false;
				boolean fsubject = false;
				boolean ftext = false;
				boolean fsent = false;
				   
				int mailid, threadid;
				StringBuilder vname, vlist;
				StringBuilder vtime, vfrom, vto, vsubject;
				StringBuilder vsent;
				String vid;
				
				EmailThread tempthread;
				SingleMail tempmail;
				
				int maxtfd = 0, maxtfs = 0;
				double maxlen = 0.0;
				Map<String, Integer> maptfifs ;
				Map<String, Integer> mapwordcount;
				SentiScore sentiwordnet;
				
				public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
			 
					//System.out.println("Start Element :" + qName);
			 
					if (qName.equalsIgnoreCase("ROOT")) {
						froot = true;
						threadid = 1;
						try {
							sentiwordnet = new SentiScore(sentinetfis);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			 
					if (qName.equalsIgnoreCase("THREAD")) {
						fthread = true;						
						mailid = 1;
						
//						System.out.println("NEW THREAD "+ threadid +" STARTS..........");
						
						threadid++;
						tempthread = new EmailThread();
						
						mapwordcount = new HashMap<String, Integer>();
						
					}
			 
					if (qName.equalsIgnoreCase("NAME")) {
						fname = true;
						vname = new StringBuilder();
					}
			 
					if (qName.equalsIgnoreCase("LISTNO")) {
						flist = true;
						vlist = new StringBuilder();
					}
					if (qName.equalsIgnoreCase("DOC")) {
						fdoc = true;
						tempmail = new SingleMail(mailid);
						mailid++;
						
						maptfifs = new HashMap<String, Integer>();
					}
			 
					if (qName.equalsIgnoreCase("RECEIVED")) {
						ftimestamp = true;
						vtime = new StringBuilder();
					}
			 
					if (qName.equalsIgnoreCase("FROM")) {
						ffrom = true;
						vfrom = new StringBuilder();
					}
			 
					if (qName.equalsIgnoreCase("TO")) {
						fto = true;
						vto = new StringBuilder();
					}
					if (qName.equalsIgnoreCase("SUBJECT")) {
						fsubject = true;
						vsubject= new StringBuilder();;
					}
			 
					if (qName.equalsIgnoreCase("TEXT")) {
						ftext = true;
					}
			 
					if (qName.equalsIgnoreCase("SENT")) {
						fsent = true;
						vsent = new StringBuilder();
						vid = attributes.getValue("id");
					}
			 
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
			 
					if (qName.equalsIgnoreCase("ROOT")) {
						froot = false;
					}
			 
					if (qName.equalsIgnoreCase("THREAD")) {
						fthread = false;
						for(SingleMail sm : tempthread.getMails()){
							for(Message msg : sm.getMessage()){
								double score = operateMessageIDF(msg, tempthread.size());
								msg.setTFIDF(score);
								msg.setLength(msg.getLength()/maxlen);
							}
							
						}
						maxlen = 0;
						maxtfd = 0;
						mapwordcount.clear();
						convers.add(tempthread);
					}
			 
					if (qName.equalsIgnoreCase("NAME")) {
						tempthread.setName(vname.toString());
						updateTreeMap(vname.toString(), 5);
						fname = false;
					}
			 
					if (qName.equalsIgnoreCase("LISTNO")) {
						tempthread.setListNo(vlist.toString());
						flist = false;
					}
					if (qName.equalsIgnoreCase("DOC")) {
						tempthread.addMail(tempmail);
						fdoc = false;
						
						for(Message msg : tempmail.getMessage()){
							double score = operateMessageISF(msg, tempmail.size());
							msg.setTFISF(score);
							double sentiscore = sentiCalculate(msg);
							msg.setSentiScore(sentiscore);
						}
						
						maxtfs = 0;
						maptfifs.clear();
					}
			 
					if (qName.equalsIgnoreCase("RECEIVED")) {
						tempmail.setTimestamp(vtime.toString());
						ftimestamp = false;
					}
			 
					if (qName.equalsIgnoreCase("FROM")) {
						parseSender();
						tempmail.setFrom(vfrom.toString().trim());
						ffrom = false;
					}
			 
					if (qName.equalsIgnoreCase("TO")) {
						parseReceiver();
						tempmail.setReplyTo(vto.toString().trim());
						fto = false;
					}
					if (qName.equalsIgnoreCase("SUBJECT")) {
						String str = "";
						istitle=true;						
						if(isReply(vsubject.toString()) == true){
							tempmail.setReply();
							str = vsubject.toString().substring(4);
							tempmail.setSubject(str.trim());
						}
						else{
							str = vsubject.toString();
							tempmail.setSubject(str.trim());
						}
						title.emptyMap();
						updateTreeMap(str, 3);
						fsubject = false;
						istitle=false;
					}
			 
					if (qName.equalsIgnoreCase("TEXT")) {
						ftext = false;
					}
					if (qName.equalsIgnoreCase("SENT")) {
						String str = vsent.toString();
						if(!(str.startsWith(">") || str.startsWith("&amp;gt;") || str.startsWith("&gt;")) ){
							//str = sentOperation(str);
							String strtemp = conversion(str);
							Message msg = updateTreeMap(str, 1);
							if(maxlen < msg.getLength())
								maxlen = msg.getLength();
							msg.setID(vid);
							msg.setSentence(strtemp.trim());
							msg.setSimilarityscore(title.getCosineSimilarityWith(sentence));
							//System.out.println("title:"+title+"sentence:"+sentence+"similarity : "+title.getCosineSimilarityWith(sentence));
							tempmail.setMessage(msg);
							sentence.emptyMap();					
						}
						ftext = false;
					}
			 
				}
				

				private double sentiCalculate(Message msg) {
					// TODO Auto-generated method stub
					double score = 0.0;
					for(String token : msg.getStringTokens()){
						score += sentiwordnet.extract(token, "a");
						score += sentiwordnet.extract(token, "v");
						score += sentiwordnet.extract(token, "r");
						score += sentiwordnet.extract(token, "n");
						
					}
					
					
					return score;
				}

				private double operateMessageIDF(Message msg, int size) {
					// TODO Auto-generated method stub
					double score = 0.0;
					//String str = msg.getSentence();
					List<String> strtoken = msg.getStringTokens();
					for(String token : strtoken){
						double tf = (((double)mapwordcount.get(token))/((double)maxtfd));
						double idf = Math.log(((double)size)/((1 + (double)mapwordcount.get(token))));
						score += (tf*idf);
						if(tf*idf >= 1.0){
							System.out.println();
						}
					}
//					score /= msg.getWords();
//					if(score<0.0)
//						System.out.println("IDF ::: "+msg.getSentence() + " : "+score + "\n");
					return score;
				}
				private double operateMessageISF(Message msg, int size) {
					// TODO Auto-generated method stub
					double score = 0.0;
					//String str = msg.getSentence();
					List<String> strtoken = msg.getStringTokens();
					for(String token : strtoken){
						double tf = (((double)maptfifs.get(token))/((double)maxtfs));
						double idf = Math.log10(((double)size)/((1 + (double)maptfifs.get(token))));
						score += (tf*idf);
					}
//					score /= msg.getWords();
//					if(score<0.0)
//						System.out.println("ISF ::: "+msg.getSentence() + " : "+score + "\n");
					return score;
				}

				private Message updateTreeMap(String str, int value){
					StringTokenizer st=new StringTokenizer(str,",. :;(){}[]-_!@#$%^&*+=<>?/|\\~`'\"0123456789");
					Message msg = new Message(str, st,stopwordfis);
					for(String token : msg.getStringTokens()){
						if(!mapwordcount.containsKey(token)){
							mapwordcount.put(token, value);									
						}
						else{
							mapwordcount.put(token, (mapwordcount.get(token)+value));
						}
						if(mapwordcount.get(token)>maxtfd)
							maxtfd = mapwordcount.get(token);
						if(value == 1){
							if(!maptfifs.containsKey(token)){
								maptfifs.put(token, value);									
							}
							else{
								maptfifs.put(token, (maptfifs.get(token)+value));
							}
							if(maptfifs.get(token)>maxtfs)
								maxtfs = maptfifs.get(token);
						}
						if(istitle){
							title.incCount(token);
						}else
							sentence.incCount(token);
					}
					return msg;
				}
				
				private String sentOperation(String str){
					
					str = conversion(str);
					
					return str;
				}
				
				private String conversion(String str) {
					// TODO Auto-generated method stub

					str = str.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;amp;", "&").replace("&quot;", "'");
					matcher = patternI.matcher(str);
					int index=0;
					if(matcher.find()) {
						index=str.indexOf(matcher.group());
						str = str.substring(0,index+2)+"("+vfrom.toString().trim()+") "+str.substring(index+2);
					}
					matcher = patternU.matcher(str);
					if(matcher.find()) {
						index=str.indexOf(matcher.group());						
						str = str.substring(0,index+4)+"("+vto.toString().trim()+") "+str.substring(index+4);
					}
					str = parseDate(str);
				
					return str;
				}

				private boolean isReply(String string) {
					// TODO Auto-generated method stub
					
					if(string.startsWith("Re:") || string.startsWith("RE:")){
						return true;
					}
					
					return false;
				}
				private void parseSender(){
					int grIndex=vfrom.indexOf("<");
					if(grIndex!=-1){
						vfrom.replace(grIndex, vfrom.indexOf(">")+1,"");
					}
					 grIndex=vfrom.indexOf("@");
					 if(grIndex!=-1){
							vfrom.replace(grIndex,vfrom.length(),"");
					 }
					 grIndex=vfrom.indexOf("\'\"");
					 while(grIndex!=-1){
						 vfrom.replace(grIndex,grIndex+2,"");
							grIndex=vfrom.indexOf("\'\"");
					 }
					 grIndex=vfrom.indexOf("\"\'");
					 while(grIndex!=-1){
						 vfrom.replace(grIndex,grIndex+2,"");
							grIndex=vfrom.indexOf("\"\'");
					 }
					
				}
				
				private void parseReceiver(){
					int grIndex=vto.indexOf("<");
					if(grIndex!=-1){
						vto.replace(grIndex, vto.indexOf(">")+1,"");
					}
					
					 grIndex=vto.indexOf("@");
					 if(grIndex!=-1){
							vto.replace(grIndex,vto.length(),"");
					 }
					 grIndex=vto.indexOf("\'\"");
					 while(grIndex!=-1){
							vto.replace(grIndex,grIndex+2,"");
							grIndex=vto.indexOf("\'\"");
					 }
					 grIndex=vto.indexOf("\"\'");
					 while(grIndex!=-1){
							vto.replace(grIndex,grIndex+2,"");
							grIndex=vto.indexOf("\"\'");
					 }
					
				}
				
				private String parseDate(String str){
					matcher  = patternT.matcher(str);
					int x=0;
					boolean flg=false;
					while(matcher.find()){
						x=str.indexOf(matcher.group(0));
						calendar = Calendar.getInstance();
						Date date = TIMESTAMP.get(matcher.group(0).toString()).getDate(vtime.toString());
						calendar.setTime(date);
						if(matcher.group(0).contains("week")){
							if(!flg){
								str = str.substring(0,x+matcher.group(0).length())+"(i.e from "
								+calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR)+" "
								+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND)+")"
								+str.substring(x+matcher.group(0).length(),str.length());
								flg=true;
							}
						}else{
							str = str.substring(0,x+matcher.group(0).length())+"("
							+calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR)+" "
							+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND)+")"
							+str.substring(x+matcher.group(0).length(),str.length());
							
						}
						
					}
					return str;
					
				}

				public void characters(char ch[], int start, int length) throws SAXException {
			 
					//System.out.println(new String(ch, start, length));
					if (fname) {
						vname.append(new String(ch, start, length));
						
					}
			 
					if (flist) {
						vlist.append(new String(ch, start, length));
					}
			 
					if (ftimestamp) {
						vtime.append(new String(ch, start, length));
						
					}
					if (ffrom) {
						vfrom.append(new String(ch, start, length));
						
					}
			 
					if (fto) {
						vto.append(new String(ch, start, length));
					}
			 
					if (fsubject) {
						vsubject.append(new String(ch, start, length));
						
					}
					if (fsent) {
						vsent.append(new String(ch, start, length));
						
					}
			 
				}
			 
			     };
			  saxParser.parse(uri, handler);
			     
		}
		catch(ParserConfigurationException e){
			e.printStackTrace();
			   System.out.println("EXCEPTION : "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			   System.out.println("EXCEPTION : "+e.getMessage());
		}
		catch (SAXParseException e) 
        {
			e.printStackTrace();
           System.out.println("EXCEPTION : "+e.getMessage());
        }
        catch (SAXException e) 
        {
        	e.printStackTrace();
           System.out.println("EXCEPTION : "+e.getMessage());
        }  
		catch(Exception e){
			e.printStackTrace();
			System.out.println("EXCEPTION : "+e.getMessage());
		}

		
		
		
	}
	
}
