package ctrl;

import java.io.IOException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Engine;

/**
 * Servlet implementation class Prime
 */
@WebServlet("/Prime.do")
public class Prime extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(request.getParameter("calc") == null && request.getParameter("recalc") == null)
		{
			
		}
		else
		{
			Engine engine = Engine.getInstance();
			String min = request.getParameter("min");
			String max = request.getParameter("max");
			request.setAttribute("min", min);
			request.setAttribute("max", max);
			
			try
			{
				if(request.getParameter("recalc") != null)
				{
					String last = request.getParameter("last");
					request.setAttribute("min", last);
					String result = engine.doPrime(last, max);
					request.setAttribute("result", result);
					request.setAttribute("last", result);
				}
				
				else	
				{
					String result = engine.doPrime(min, max);
					request.setAttribute("result", result);
					request.setAttribute("last", result);
				}
				
			}
			catch (Exception e)
			{
				request.setAttribute("error", e.getMessage());
				
			}
			
		}
		this.getServletContext().getRequestDispatcher("/Prime.jspx").forward(request, response);
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
