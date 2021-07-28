package ctrl;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


public class CtrlUtil
{
	public static final String SERVICE_COUNTER = "service_counter"; //attribute name for object map to store the stats of service use
	public static final String USERS = "users"; //attribute name for object Integer to store the number of total users 
	public static final String S3_S4_USERS = "s3_s4_users"; //attribute name for object Integer to store the number of users that have used both S3 & S4
	
	public static void log_request(PrintStream out, HttpServletRequest request, String stage)
	{
		out.println();
		out.println("----------------Request: "+stage+ "----------------");
		out.println("Context Path: " + request.getContextPath());
		out.println("Servlet Path: " + request.getServletPath());
		out.println("Request URI: " + request.getRequestURI());
		out.println("Request URL: " + request.getRequestURL());
		out.println("Server Name: " + request.getServerName());
		out.println("Server Port: " + request.getServerPort());
		
		out.println("Remote Address: " + request.getRemoteAddr());
		out.println("Remote Host: " + request.getRemoteHost());
		out.println("Remote Port: " + request.getRemotePort());
		out.println("Remote User: " + request.getRemoteUser());

		out.println("Local Address: " + request.getLocalAddr());
		out.println("Local Name: " + request.getLocalName());
		out.println("Local Port: " + request.getLocalPort());
		out.println("Path Info: " + request.getPathInfo());
		out.println("Path Translated: " + request.getPathTranslated());
	
		out.println("Method: " + request.getMethod());
		out.println("Content Type: " + request.getContentType());
		out.println("Query String: " + request.getQueryString());
			
		out.println("Request Parameters: ");
		List<String> parameters = Collections.list(request.getParameterNames());
		for(String parm: parameters)
		{
			out.println(" " + parm + "=" + request.getParameter(parm));
		}
		out.println("Request Attributes: ");		
		List<String> attributes = Collections.list(request.getAttributeNames());
		for(String attr: attributes)
		{
			out.print(" " + attr);
			if (request.getAttribute(attr) instanceof String)
			{
				out.print("=" + request.getAttribute(attr));
			}
			out.println();
		}
		
		out.println("  ------Request Session info------");
		HttpSession session = request.getSession();
		out.println("Session ID: " + session.getId());
		out.println("Session Creation Time: " + session.getCreationTime());
		out.println("Session Last Accessed Time: " + session.getLastAccessedTime());
		out.println("Session Attributes: ");		
		attributes = Collections.list(session.getAttributeNames());
		for(String attr: attributes)
		{
			out.print(" " + attr);
//			if (session.getAttribute(attr) instanceof String)
//			{
				out.print("=" + session.getAttribute(attr));
//			}
			out.println();
		}
		out.println("  ------Servlet Context info------");		
		ServletContext servlet_context = session.getServletContext();
		List<String> servlet_attributes = Collections.list(servlet_context.getAttributeNames());
		out.println("Servlet Attributes: ");
		for(String attr: servlet_attributes)
		{
			out.print(" " + attr);
//			if (session.getAttribute(attr) instanceof String)
//			{
				out.print("=" + servlet_context.getAttribute(attr));
//			}
			out.println();
		}
		out.println();
	}
	
}































