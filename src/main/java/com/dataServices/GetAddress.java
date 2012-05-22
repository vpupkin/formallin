package com.dataServices;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataConversion.Obj2Xml;
import com.objects.Properties;

/**
 * Servlet implementation class for Servlet: GetAddress
 *
 */
 public class GetAddress extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public GetAddress() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		Properties properties = new Properties();
		String queryString = request.getParameter("searchString").trim();
		queryString = queryString.replaceAll(" ", "*");
		System.out.println(queryString);
		Obj2Xml obj2Xml = new Obj2Xml();
		PrintWriter pw = response.getWriter();
		try {
		AddressSearch addressSearch = AddressSearch.getAddressSearch();
		properties.setProperty(addressSearch.search("*" + queryString + "*"));
		obj2Xml.persistAddressList(properties, pw);
		}
		catch(Exception ex) {
			getServletContext().log(ex, "Exception");
		}
		pw.close();
	}   	  	    
	
	@Override
	public void init() throws ServletException {
		super.init();
		//get the absolute path of the xml file
		String xmlFilePath = getServletConfig().getInitParameter("dataStore") + File.pathSeparator + "nrhp.xml";
		//Initiate data of address data to memory
		try {
			AddressSearch.initialize(xmlFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			getServletContext().log(e, "Exception");
			
		}
	}
}