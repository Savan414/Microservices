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
@WebServlet("/Gps.do")
public class Gps extends HttpServlet
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
			Engine engine = Engine.getInstance();
			
			String lat1 = request.getParameter("lat1");
			String lon1 = request.getParameter("lon1");
			String lat2 = request.getParameter("lat2");
			String lon2 = request.getParameter("lon2");
			request.setAttribute("lat1", lat1);
			request.setAttribute("lat2", lat2);
			request.setAttribute("lon1", lon1);
			request.setAttribute("lon2", lon2);
			
			try
			{
				String result = engine.doGps(lat1, lon1, lat2, lon2) + " km";
				request.setAttribute("result", result);
			}
			catch (Exception e)
			{
				request.setAttribute("error", e.getMessage());
				
			}
			
		}
		this.getServletContext().getRequestDispatcher("/Gps.jspx").forward(request, response);
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
