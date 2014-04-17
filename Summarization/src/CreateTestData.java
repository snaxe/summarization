import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import parser.SentiScore;
import ChatStructure.EmailThread;
import ChatStructure.Message;
import ChatStructure.SingleMail;

import java.io.BufferedWriter; 
import java.io.File; 
import java.io.FileWriter; 
import java.io.IOException; 
import java.util.Calendar; 
import java.util.Date; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.StringTokenizer; 
  
import javax.xml.parsers.ParserConfigurationException; 
import javax.xml.parsers.SAXParser; 
import javax.xml.parsers.SAXParserFactory; 
  
import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException; 
import org.xml.sax.helpers.DefaultHandler; 
  
import parser.SentiScore; 
import ChatStructure.EmailThread; 
import ChatStructure.Message; 
import ChatStructure.SingleMail; 
  
  
public class CreateTestData { 
  
    public static void main(String args[]){ 
          
       // String uri = "E:\\Dropbox\\Dump\\bc3\\testdata.xml"; 
    	//String uri = "G:\\Sem2\\IRE\\Major_Project\\dataset\\bc3&framework.1.1\\bc3corpus.1.0\\testdata.xml";//"E:\\Dropbox\\Dump\\bc3\\testdata.xml";
    	String uri="G:\\Sem2\\IRE\\Major_Project\\dataset\\bc3&framework.1.1\\bc3corpus.1.0\\annotation.xml";
          
        try{ 
            SAXParserFactory factory = SAXParserFactory.newInstance(); 
            SAXParser saxParser = factory.newSAXParser(); 
            final FileWriter fw; 
            BufferedWriter bw; 
            File file; 
            file = new File("G:\\Human\\filenew_one.html"); 
            fw = new FileWriter(file.getAbsoluteFile()); 
            fw.append("<html>\n<head><title>filenew.html</title></head>\n<body bgcolor = \"white\">\n"); 
            DefaultHandler handler = new DefaultHandler() { 
                   
                boolean fsent = false; 
                boolean fthread = false;    
                StringBuilder vsent; 
                int num = 0, num1 = 0; 
                  
                  
                StringBuilder outputstr; 
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException { 
               
                    //System.out.println("Start Element :" + qName); 
                      
                    if (qName.equalsIgnoreCase("SENT")) { 
                        num1++; 
                        fsent = true; 
                        vsent = new StringBuilder(); 
                          
                    } 
               
                } 
               
                public void endElement(String uri, String localName, 
                    String qName) throws SAXException { 
               
                      
                    if (qName.equalsIgnoreCase("SENT")) { 
                        try { 
                            fw.append("<a name=\"" + num1 +"\">[" + num1 +"]</a>"); 
                            fw.append(" <a href=\"#" + num1 + "\" id="+ num1 + ">" + vsent.toString() + "</a>\n"); 
                        } catch (IOException e) { 
                            // TODO Auto-generated catch block 
                            e.printStackTrace(); 
                        } 
                          
                        fsent = false; 
                    } 
                } 
                  
  
              
                public void characters(char ch[], int start, int length) throws SAXException { 
               
                    //System.out.println(new String(ch, start, length)); 
                    if (fsent) { 
                        vsent.append(new String(ch, start, length)); 
                          
                    } 
               
                } 
               
                 }; 
              saxParser.parse(uri, handler); 
              fw.append("</body>\n</html>"); 
              fw.close(); 
                   
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
