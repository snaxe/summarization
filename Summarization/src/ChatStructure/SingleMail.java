package ChatStructure;

import java.util.ArrayList;
import java.util.List;

public class SingleMail {

	private int mailid;
	private String from;
	private String to;
	private String subject;
	private String timestamp;
	private boolean reply;
	private List<Message> message;
	
	public SingleMail(){
		reply = false;
		message = new ArrayList<Message>();
	}
	public SingleMail(int id){
		reply = false;
		mailid = id;
		message = new ArrayList<Message>();
	}
	public SingleMail(int tid, String tfrom, String  tto, String  tsubject, String  ttime){
		reply = false;
		mailid = tid;
		from = tfrom;
		to = tto;
		subject = tsubject;
		timestamp = ttime;
		
		message = new ArrayList<Message>();		
	}
	public void setReply(){
		reply = true;
	}
	
	public boolean isReply(){
		return reply;
	}

	public int size(){
		return message.size();
	}

	public void setMailId(int tmail){
		mailid = tmail;
	}
	public void setSubject(String tsubject){
		subject = tsubject;
	}
	
	public void setFrom(String tfrom){
		from = tfrom;
	}
	public void setReplyTo(String tto){
		to = tto;
	}
	public void setTimestamp(String ttime){
		timestamp = ttime;
	}

	
	public void setMessageList(List<Message> msg){
		message = msg;
	}
	
	public void setMessage(Message msg){
		message.add(msg);
	}
	
	public int getMailId(){
		return mailid;
	}
	public String getSubject(){
		return subject;
	}
	
	public String getFrom(){
		return from;
	}
	public String getReplyTo(){
		return to;
	}
	public String getTimestamp(){
		return timestamp;
	}
	
	public List<Message> getMessage(){
		return message;
	}
	
}
