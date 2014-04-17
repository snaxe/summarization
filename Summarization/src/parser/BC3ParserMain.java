package parser;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import rank.RankingClass;
import ChatStructure.EmailThread;
import ChatStructure.Message;
import ChatStructure.SingleMail;

public class BC3ParserMain {

public ArrayList<ArrayList<String>> getResponse(String file,InputStream stopwordfis,InputStream sentiwordfis){
	
		BC3Parser bcp = new BC3Parser();
		double startTime = System.currentTimeMillis();
	
		List<EmailThread> convers = new ArrayList<EmailThread>();
		Map<String, Integer> mapwordcount = new TreeMap<String, Integer>(); 
		
//		String uri = "E:\\Dropbox\\Dump\\bc3\\test.xml";
		String uri = file;
//		String uri = "E:\\Dropbox\\Dump\\bc3\\testing.xml";
		//bcp.bc3Parser(uri, convers, mapwordcount);
		bcp.bc3Parser(uri, convers, mapwordcount,stopwordfis,sentiwordfis);
		
		RankingClass rank = new RankingClass();
		return rank.rankMethod(convers);
		
//		RankingClass1 rank1 = new RankingClass1();
//		rank1.rankMethod(convers);
////		
//		uri = "E:\\Dropbox\\Dump\\bc3\\finaltest.dat";
//		int N = 980;
//		SVMTool svm = new SVMTool();
//		try {
//			svm.SVMModelCreate(uri, N);
//			svm.SVMVerify(convers);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//printConvers(convers);
		
		
	}

private static void printConvers(List<EmailThread> convers) {
	// TODO Auto-generated method stub
	FileWriter fw;
//	Path myDir;
//	Path writeFile;

	BufferedWriter bw;
	try{
	File file = new File("E:\\Dropbox\\Dump\\bc3\\outputp.txt");
	fw = new FileWriter(file.getAbsoluteFile());
	
//	myDir = Paths.get(file.getParentFile().toURI());
//
//	writeFile = myDir.resolve(file.getName());
//	bw = Files.newBufferedWriter(writeFile,               
//        Charset.forName("UTF-8"), 
//        new OpenOption[] {StandardOpenOption.WRITE});
	
	for(EmailThread et : convers){
		//fw.write(et.getName()+"\n");
		List<SingleMail> listmail = et.getMails();
		
		for(SingleMail sm : listmail){
//			fw.write("TO : "+sm.getReplyTo() + "      " + "FROM : "+sm.getFrom()+"\n");
//			if(!sm.isReply())
//				fw.write("SUBJECT : "+sm.getSubject()+"\n");
//			else
//				fw.write("SUBJECT-R : "+sm.getSubject()+"\n");
			List<Message> message = sm.getMessage();
			for(Message msg : message){
				//System.out.println(str);
//				fw.write(msg.getID() + " :  " + msg.getSentence());
//				fw.write("\nFEATURES : "+"\n");
//				fw.write("TFIDF : " + msg.getTFIDF() +"\n");
//				fw.write("TFISF : " + msg.getTFISF() +"\n");
//				fw.write("SENTI : " + msg.getSentiScore() +"\n");
//				fw.write("LENGT : " + msg.getLength() +"\n");
//				fw.write("QUEST : " + msg.getQuestion() +"\n");
//				fw.write("SIMILARITY : " + msg.getSimilarityscore() +"\n");
//				fw.write("SCORE : " + msg.getScore() +"\n");
//				if(msg.getSummery() == true)
//					fw.append("+1 ");
//				else
//					fw.append("-1 ");
				
				Set<String> setvalue = new HashSet<String>();
				setvalue.add("1.1");
				setvalue.add("1.2");
				setvalue.add("5.5");
//				setvalue.add("5.13");
//				setvalue.add("5.14");
//				setvalue.add("3.7");
//				setvalue.add("3.8");
//				setvalue.add("3.9");
//				setvalue.add("4.3");
//				setvalue.add("4.4");
//				setvalue.add("5.2");
//				setvalue.add("5.3");
//				setvalue.add("5.13");
//				setvalue.add("5.14");
//				setvalue.add("6.2");
//				setvalue.add("6.4");
//				setvalue.add("6.5");
//				setvalue.add("6.6");

//				
				if(setvalue.contains(msg.getID()))
					fw.append("+1 ");
				else
					fw.append("-1 ");
				fw.append("1:" + msg.getTFIDF() +" ");
				fw.append("2:" + msg.getTFISF() +" ");
				fw.append("3:" + msg.getSentiScore() +" ");
				fw.append("4:" + msg.getLength() +" ");
				if(msg.getQuestion() == true)
					fw.append("5:" + "1" +" ");
				else
					fw.append("5:" + "-1" +" ");	
				fw.append("6:" + msg.getSimilarityscore() +" ");
				//fw.append("1:" + msg.getScore() +"\n\n");
				fw.write("\n");
				
			}
			//System.out.println();
		//	fw.write("\n");
		}
		
	}
	fw.close();
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
}
