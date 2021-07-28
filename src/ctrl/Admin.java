package ctrl;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Admin.do")
public class Admin extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Admin() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String analyze = request.getParameter("analyze");
		if(analyze != null )
		{
			try
			{
				ServletContext servlet_context = this.getServletContext();
				String service;
				
				/**
				 * Below service finds out the percentage of times "Prime" service is requested
				 * or the likelihood of using Service 3 and Service 4 together
				 */
				if(analyze.startsWith("Service Uses Percentage"))
				{
					if (analyze.contains("Prime"))
						service = "Prime";
					else
						service = "Drone";
					double result = getRequestPercentage(servlet_context, service);
					request.setAttribute("result", result);					
				}
				else if (analyze.startsWith("Likelihood"))
				{
					double result = getLikelihoodUsingS3AndS4(servlet_context);
					request.setAttribute("result", result);
				}
			}
			catch (Exception e)
			{
				request.setAttribute("error", e.getMessage());
			}
			finally
			{
				request.setAttribute("analyze", analyze);
			}
		}
		this.getServletContext().getRequestDispatcher("/Admin.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

	//helper method to get percentage use of a service
	private double getRequestPercentage(ServletContext servlet_context, String service)
	{
		System.out.println("going to calc use of " + service);
		int serv_counter = 0;
		int total = 0;
		Map<String, Integer> counter = (Map<String, Integer>) servlet_context.getAttribute(CtrlUtil.SERVICE_COUNTER);
		Set<String> keys = counter.keySet();
		for(String key : keys)
		{
			System.out.println("key=" + key +", value=" + counter.get(key));
			Integer c = (Integer)counter.get(key);
			if(key.equals(service))
			{
				serv_counter = serv_counter + c;
			}
			total = total + c;
		}
		System.out.println("total=" + total);
		System.out.println("count=" + serv_counter);
		return (double)serv_counter/(double)total;
	}
	
	private double getLikelihoodUsingS3AndS4(ServletContext servlet_context)
	{
		//get total users;
		Integer total = (Integer) servlet_context.getAttribute(CtrlUtil.USERS);
		if (total == null)
			return 0;
		Integer s3s4Users = (Integer) servlet_context.getAttribute(CtrlUtil.S3_S4_USERS);
		if (s3s4Users==null)
			return 0;
		return (double) s3s4Users / (double) total;
	}
}
