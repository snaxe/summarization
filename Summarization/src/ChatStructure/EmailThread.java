package ChatStructure;

import java.util.ArrayList;
import java.util.List;

import org.omg.CosNaming.IstringHelper;

public class EmailThread {
	
	private String th_name;
	private String listno;
	
	private List<SingleMail> listmail;
	
	public EmailThread(){
		listmail = new ArrayList<SingleMail>();
	}
	public EmailThread(String tname, String tlist){
		
		th_name = tname;
		listno = tlist;
		listmail = new ArrayList<SingleMail>();
		
	}
	public int size(){
		return listmail.size();
	}
	
	public void setName(String tname){
		th_name = tname;
	}
	
	public void setListNo(String tlist){
		listno = tlist;
	}
	public String getName(){
		return th_name;
	}
	
	public String getListNo(){
		return listno;
	}
	
	public void addMail(SingleMail tmail){
		listmail.add(tmail);
	}
	
	public List<SingleMail> getMails(){
		return listmail;
	}
		
}

