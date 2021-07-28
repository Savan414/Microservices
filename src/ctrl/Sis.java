package ctrl;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Engine;

/**
 * Servlet implementation class Prime
 */
@WebServlet("/Sis.do")
public class Sis extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(request.getParameter("calc") == null)
		{
			
		}
		else
		{		
			String prefix = null;
			String minGpa = null;
			String sortBy = null;
			try
			{
				Engine engine = Engine.getInstance();
				prefix = request.getParameter("prefix");
				minGpa = request.getParameter("minGpa");
				sortBy = request.getParameter("sortBy");
				
				
				request.setAttribute("result", engine.doSis(prefix, minGpa, sortBy));
				
			}
			catch (Exception e)
			{
				request.setAttribute("error", e.getMessage());
				
			}
			finally
			{
				request.setAttribute("prefix", prefix);
				request.setAttribute("minGpa", minGpa);
				request.setAttribute("sortBy", sortBy);
			}
			
		}
		this.getServletContext().getRequestDispatcher("/Sis.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
