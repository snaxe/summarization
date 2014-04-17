package com.iiit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import parser.BC3ParserMain;

/**
 * Servlet implementation class Summarize
 */
public class Summarize extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Summarize() {
        super();
        // TODO Auto-generated constructor stub
    }
    ArrayList<ArrayList<String>> output;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String file=request.getParameter("filename");
		int page=Integer.parseInt(request.getParameter("page"));
		if(page==0){
			InputStream stpfis = Summarize.class.getClassLoader().getResourceAsStream("stopwords.txt");
			InputStream sentifis = Summarize.class.getClassLoader().getResourceAsStream("SentiWordNet.txt");
			System.out.println("sentifis"+sentifis);
			//InputStream fis = new FileInputStream("stopwords.txt");
			String line;
			
			BC3ParserMain bc3=new BC3ParserMain();
			System.out.println("********Output:********\n");
			output= bc3.getResponse(file,stpfis,sentifis);
			if(page<output.size()){
				response.getWriter().append("<div style='padding:1%;margin-top:1%;color:crimson'><p style='text-align: left;'>");
				ArrayList<String> summary  = output.get(page);
				for(String sent:summary){
					response.getWriter().append(""+sent+"<br/>");
				}
				response.getWriter().append("</p><hr/></div>");
			}
		}else{
			
			if(page<output.size() && page>=0){
				response.getWriter().append("<div style='padding:1%;margin-top:1%;color:crimson'><p style='text-align: left;'>");
				ArrayList<String> summary  = output.get(page);
				for(String sent:summary){
					response.getWriter().append(""+sent+"<br/>");
				}
				response.getWriter().append("</p><hr/></div>");
			}else{
				response.getWriter().append("<div style='padding:1%;margin-top:1%;color:crimson'><p>");
				response.getWriter().append("No more summaries left</p><hr/></div>");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
