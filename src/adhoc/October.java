package adhoc;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class October
 */
// Ride.do, Sis.do
@WebFilter("/Sis.do")
public class October implements Filter
{

	/**
	 * Default constructor.
	 */
	public October()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		
		if(req.getServletPath().contains("Ride.do"))
		{
			request.setAttribute("server", request.getServerName().substring(1));
			request.getServletContext().getRequestDispatcher("/Block.jspx").forward(request, response);
		}
		else if(req.getServletPath().contains("Sis.do"))
		{
			if(request.getParameter("sortBy") != null && (!request.getParameter("sortBy").equals("NONE")))
			{
				response.setContentType("text/html");
				Writer out = response.getWriter();
				out.write("Sorting is not available...");
				out.write("<a href=\"Dash.do\"> Dashboard </a>");
			
			}
			else
			{
				chain.doFilter(request, response);
			}
		}
		else
		{
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}
	
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		// TODO Auto-generated method stub
	}

}
