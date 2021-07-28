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
@WebServlet("/Ride.do")
public class Ride extends HttpServlet
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
			try
			{
				Engine engine = Engine.getInstance();
				String from = request.getParameter("from");
				String dest = request.getParameter("dest");
			
				request.setAttribute("from", from);
				request.setAttribute("dest", dest);
				double result = engine.doRide(from, dest);
				System.out.println(result);
				request.setAttribute("result", result);
			}
			catch (Exception e)
			{
				request.setAttribute("error", e.getMessage());
				
			}
			
		}
		this.getServletContext().getRequestDispatcher("/Ride.jspx").forward(request, response);
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
