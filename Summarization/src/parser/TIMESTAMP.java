package parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/*enum DAYS{
Mon("Mon"),Tue("Tue"),Wed("Wed"),Thu("Thu"),Fri("Fri"),Sat("Sat"),Sun("Sun");
String dayname;
private DAYS(String d){
	dayname=d;
}
public String getDayName(){
	return dayname;
}
};*/
enum TIMESTAMP{
today("today"){
	public Date getDate(String date){
		try {
			Date today = dateFormatIST.parse(date);
			return today;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
},Today("Today"){
	public Date getDate(String date){
		try {
			Date today = dateFormatIST.parse(date);
			return today;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
},tomorrow("tomorrow"){
	public Date getDate(String date){
		try {
			Date today = dateFormatIST.parse(date);
			Calendar c=Calendar.getInstance();
			c.setTime(today);
			c.add(Calendar.DATE, 1);
			return c.getTime();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
},Tomorrow("Tomorrow"){
	public Date getDate(String date){
		try {
			Date today = dateFormatIST.parse(date);
			Calendar c=Calendar.getInstance();
			c.setTime(today);
			c.add(Calendar.DATE, 1);
			return c.getTime();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
},yesterday("yesterday"){
	public Date getDate(String date){
		try {
			Date today = dateFormatIST.parse(date);
			Calendar c=Calendar.getInstance();
			c.setTime(today);
			c.add(Calendar.DATE, -1);
			return c.getTime();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
},Yesterday("Yesterday"){
	public Date getDate(String date){
		try {
			Date today = dateFormatIST.parse(date);
			Calendar c=Calendar.getInstance();
			c.setTime(today);
			c.add(Calendar.DATE, -1);
			return c.getTime();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
},week("week") {
	@Override
	public Date getDate(String date) {
		// TODO Auto-generated method stub
		try {
			Date today = dateFormatIST.parse(date);
			return today;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
},Week("Week") {
	@Override
	public Date getDate(String date) {
		// TODO Auto-generated method stub
		try {
			Date today = dateFormatIST.parse(date);
			return today;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
},weeks("weeks") {
	@Override
	public Date getDate(String date) {
		// TODO Auto-generated method stub
		//System.out.println(date);
		try {
			Date today = dateFormatIST.parse(date);
			//System.out.println(today);
			return today;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
},Weeks("Weeks") {
	@Override
	public Date getDate(String date) {
		// TODO Auto-generated method stub
		try {
			Date today = dateFormatIST.parse(date);
			return today;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
};
String time;
DateFormat dateFormatIST = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");


private static final Map lookup = new HashMap();
static{
	for(TIMESTAMP t : EnumSet.allOf(TIMESTAMP.class))
		lookup.put(t.getTime(),t);
}
public static TIMESTAMP get(String t){
	return (TIMESTAMP) lookup.get(t);
}
private TIMESTAMP(String t){
	time=t;
}
public String getTime(){
	return time;
}
public abstract Date getDate(String date);

}