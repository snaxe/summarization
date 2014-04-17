package rank;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import ChatStructure.EmailThread;
import ChatStructure.Message;
import ChatStructure.SingleMail;

public class RankingClass {
	static FileWriter fw;
	BufferedWriter bw;
	static int num=0;
	//public void rankMethod(List<EmailThread> convers){
	public ArrayList<ArrayList<String>> rankMethod(List<EmailThread> convers){
			
		//File file = new File("E:\\Dropbox\\Dump\\bc3\\output1.dat");
		//File file = new File("C:\\Users\\Mridul\\Desktop\\IRE\\SVM\\svm_light_windows64\\output1.dat");
		/*File file = new File("C:\\Users\\Mridul\\Desktop\\IRE\\SVM\\svm_light_windows64\\testing.dat");
		try {
			fw = new FileWriter(file.getAbsoluteFile());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		StringBuilder outputstr = new StringBuilder();
		outputstr.append("<html>\n<head><title>file" + ".html</title></head>\n<body bgcolor = \"white\">\n");
		
		File file = new File("G:\\systemdocs\\file" + ".html");//new File("E:\\Dropbox\\Dump\\bc3\\Doc\\file_" + numsum + ".html");
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			System.out.println("----------o/p:"+outputstr.toString());
			fw.write(outputstr.toString());
			fw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<ArrayList<String>> output_summary = new ArrayList<ArrayList<String>>();
		
		int numsum = 0;
		Map<Double, List<Message>> output = new TreeMap<Double, List<Message>>(Collections.reverseOrder());
		for(EmailThread et : convers){
			output.clear(); 
			int words = 0;
			numsum++;
			for(SingleMail mail : et.getMails()){
				for(Message msg : mail.getMessage()){
					words += msg.getWords();
					if(!Double.isNaN(msg.getScore()))
					{
						if(output.containsKey(msg.getScore())){
						
							List<Message> as = output.get(msg.getScore());
							if(as==null){
								as=new ArrayList<Message>();
								as.add(msg);
								//output.put(msg.getScore(), as);
							}else
							{
								
								output.get(msg.getScore()).add(msg);
								//ts.get(prob).add(sent.get(Integer.parseInt(sf.getKey())));
							}
						   }
						else{
							List<Message> as = new ArrayList<Message>();
							as.add(msg);
							output.put(msg.getScore(), as);
							
						}	
					}
				}
			}
			int perc = 20;
			int num = (output.size()*perc)/100;
			//int num = (words*perc)/100;
			int j = 0;
			
			List<Message> outlist = new ArrayList<Message>();
			
			for(Map.Entry<Double, List<Message>> mlist : output.entrySet()){
				 
					 //System.out.println(m.getKey()+"="+m.getValue());
					 for(Message msg : mlist.getValue()){
						 //System.out.println(msg.getScore() +"  :  "+msg.getSentence());
						 
						 outlist.add(msg);
						 j++;
						 if(j == num)
							 break;
					 }
					 if(j == num)
						 break;
				 
			 }
			
			try {
				sortListbyID(outlist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<String> summary=new ArrayList<String>();
			
			for(Message msg : outlist){
				//System.out.println(msg.getScore() + " :  "+msg.getID() +"  :  "+msg.getSentence());
				//System.out.print(msg.getSentence()+" " );
			}
			System.out.println("\n");
			
			printMsgList(outlist, numsum,summary);	
			
			output_summary.add(summary);
		}
		outputstr.setLength(0);
		outputstr.append("</body>\n</html>");
		try {
			fw = new FileWriter(file.getAbsoluteFile(),true);
			System.out.println("----------o/p:"+outputstr.toString());
			fw.write(outputstr.toString());
			fw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return output_summary;
	}
	private static void printMsgList(List<Message> message, int numsum,ArrayList<String> summary) {
		// TODO Auto-generated method stub
		
		StringBuilder outputstr = new StringBuilder();
		//outputstr.append("<html>\n<head><title>file_" + numsum + ".html</title></head>\n<body bgcolor = \"white\">\n");
		
		try{
			//int num = 0;
				for(Message msg : message){
					num++;
					//System.out.println(str);
					//fw.append(msg.getSentence()+"\n");
					//fw.append("\nFEATURES : "+"\n");
					outputstr.append("<a name=\"" + num +"\">[" + num +"]</a>");
					String sent=msg.getRealSentence();
					sent = (sent.charAt(0)+"").toUpperCase()+sent.substring(1);
					
					outputstr.append(" <a href=\"#" + num + "\" id="+ num + ">" + sent + "</a>\n");
					System.out.println(msg.getSentence());
					 sent=msg.getSentence();
					sent = (sent.charAt(0)+"").toUpperCase()+sent.substring(1);
					summary.add(sent);
//					fw.append("1 ");
//					fw.append("1:" + msg.getTFIDF() +" ");
//					fw.append("2:" + msg.getTFISF() +" ");
//					fw.append("3:" + msg.getSentiScore() +" ");
//					fw.append("4:" + msg.getLength() +" ");
//					if(msg.getQuestion() == true)
//						fw.append("5:" + "1" +" ");
//					else
//						fw.append("5:" + "0" +" ");	
//					fw.append("6:" + msg.getSimilarityscore() +" ");
//					//fw.append("1:" + msg.getScore() +"\n\n");
//					fw.write("\n");
					
				}
				
				File file = new File("G:\\systemdocs\\file" + ".html");//new File("E:\\Dropbox\\Dump\\bc3\\Doc\\file_" + numsum + ".html");
				try {
					fw = new FileWriter(file.getAbsoluteFile(),true);
					System.out.println("----------o/p:"+outputstr.toString());
					fw.write(outputstr.toString());
					fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//System.out.println(outputstr);
				
				System.out.println();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	
	private void sortListbyID(List<Message> list) throws Exception {
		// TODO Auto-generated method stub
		
		Collections.sort( list, new Comparator<Message>()
        {
            public int compare( Message m1, Message m2 )
            {
                return (m1.getID()).compareTo( m2.getID() );
            }
        } );
       // System.out.println();
		
	}
	
}
