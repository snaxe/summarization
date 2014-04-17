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

public class RankingClass1 {
	static FileWriter fw;
	BufferedWriter bw;

	public void rankMethod(List<EmailThread> convers){
		
		//File file = new File("E:\\Dropbox\\Dump\\bc3\\output1.dat");
		//File file = new File("C:\\Users\\Mridul\\Desktop\\IRE\\SVM\\svm_light_windows64\\output1.dat");
		File file = new File("C:\\Users\\Mridul\\Desktop\\IRE\\SVM\\svm_light_windows64\\testing.dat");
		try {
			fw = new FileWriter(file.getAbsoluteFile());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Map<Double, List<Message>> output = new TreeMap<Double, List<Message>>(Collections.reverseOrder());
		for(EmailThread et : convers){
			output.clear(); 
			int words = 0;
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
			int perc = 5;
			//int num = (output.size()*perc)/100;
			int num = (words*perc)/100;
			int j = 0;
			
			List<Message> outlist = new ArrayList<Message>();
			
			for(Map.Entry<Double, List<Message>> mlist : output.entrySet()){
				 
					 //System.out.println(m.getKey()+"="+m.getValue());
					 for(Message msg : mlist.getValue()){
						 //System.out.println(msg.getScore() +"  :  "+msg.getSentence());
						 j++;
						 if(j >= num)
						 	 msg.setSummery(false);
						 else
							 msg.setSummery(true);
						 outlist.add(msg);
						 
					 }
					 
			 }
			
			try {
				sortListbyID(outlist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			for(Message msg : outlist){
//				System.out.println(msg.getScore() + " :  "+msg.getID() +"  :  "+msg.getSentence());
//				//System.out.print(msg.getSentence()+" " );
//			}
//			System.out.println("\n");
		//	printMsgList(outlist);	
//			
		}
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void printMsgList(List<Message> message) {
		// TODO Auto-generated method stub
		
		try{
			
				for(Message msg : message){
					//System.out.println(str);
					//fw.append(msg.getSentence()+"\n");
					//fw.append("\nFEATURES : "+"\n");
					if(msg.getSummery() == true)
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
						fw.append("5:" + "0" +" ");	
					fw.append("6:" + msg.getSimilarityscore() +" ");
					//fw.append("1:" + msg.getScore() +"\n\n");
					fw.write("\n");
					
				}
				//System.out.println();
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
